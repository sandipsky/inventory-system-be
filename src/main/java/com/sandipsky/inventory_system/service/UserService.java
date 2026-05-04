package com.sandipsky.inventory_system.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.sandipsky.inventory_system.dto.UserDTO;
import com.sandipsky.inventory_system.dto.filter.RequestDTO;
import com.sandipsky.inventory_system.entity.Role;
import com.sandipsky.inventory_system.entity.User;
import com.sandipsky.inventory_system.exception.DuplicateResourceException;
import com.sandipsky.inventory_system.exception.ResourceNotFoundException;
import com.sandipsky.inventory_system.repository.RoleRepository;
import com.sandipsky.inventory_system.repository.UserRepository;
import com.sandipsky.inventory_system.util.SpecificationBuilder;

import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository repository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private final SpecificationBuilder<User> specBuilder = new SpecificationBuilder<>();

    @Transactional
    public User saveUser(UserDTO dto, MultipartFile imageFile) {
        if (repository.existsByUsername(dto.getUsername())) {
            throw new DuplicateResourceException("Username already exists");
        }
        if (repository.existsByEmail(dto.getEmail())) {
            throw new DuplicateResourceException("Email already exists");
        }

        User user = new User();
        mapDtoToEntity(dto, user);
        if (imageFile != null && !imageFile.isEmpty()) {
            String imageUrl = saveImageFile(imageFile);
            user.setImageUrl(imageUrl);
        }
        return repository.save(user);
    }

    public Page<UserDTO> getPaginatedUsersList(RequestDTO request) {
        Pageable pageable = PageRequest.of(
                request.getPagination() != null ? request.getPagination().getPageIndex() : 0,
                request.getPagination() != null ? request.getPagination().getPageSize() : 25,
                specBuilder.buildSort(request.getSortDTO()));

        Specification<User> spec = specBuilder.buildSpecification(request.getFilter());
        Page<User> productPage = repository.findAll(spec, pageable);
        return productPage.map(this::mapToDTO);
    }

    public List<UserDTO> getUsers() {
        return repository.findAll().stream().map(this::mapToDTO).toList();
    }

    public UserDTO getUserById(int id) {
        User user = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        return mapToDTO(user);
    }

    public User updateUser(int id, UserDTO dto, MultipartFile imageFile) {
        User existing = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if (repository.existsByUsernameAndIdNot(dto.getUsername(), id)) {
            throw new DuplicateResourceException("Username already exists");
        }

        if (repository.existsByEmailAndIdNot(dto.getEmail(), id)) {
            throw new DuplicateResourceException("Email already exists");
        }

        mapDtoToEntity(dto, existing);
        if (imageFile != null && !imageFile.isEmpty()) {
            deleteImageFileIfExists(existing.getImageUrl());
            String imageUrl = saveImageFile(imageFile);
            existing.setImageUrl(imageUrl);
        }
        return repository.save(existing);
    }

    public void deleteUser(int id) {
        User user = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        deleteImageFileIfExists(user.getImageUrl());
        repository.deleteById(id);
    }

    private UserDTO mapToDTO(User user) {
        UserDTO dto = new UserDTO();
        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
        dto.setFullName(user.getFullName());
        dto.setEmail(user.getEmail());
        dto.setPassword("");
        dto.setGender(user.getGender());
        dto.setRemarks(user.getRemarks());
        dto.setContact(user.getContact());
        dto.setActive(user.isActive());
        if (user.getRole() != null) {
            dto.setRoleId(user.getRole().getId());
            dto.setRoleName(user.getRole().getName());
        }
        dto.setImageUrl(user.getImageUrl());
        return dto;
    }

    private void mapDtoToEntity(UserDTO dto, User user) {
        user.setUsername(dto.getUsername().trim());
        user.setFullName(dto.getFullName());
        user.setEmail(dto.getEmail().trim());
        String hashedPassword = passwordEncoder.encode(dto.getPassword());
        user.setPassword(hashedPassword);
        user.setGender(dto.getGender());
        user.setContact(dto.getContact());
        user.setActive(dto.isActive());
        user.setRemarks(dto.getRemarks());
        if (dto.getRoleId() != null && dto.getRoleId() > 0) {
            Role role = roleRepository.findById(dto.getRoleId())
                    .orElseThrow(() -> new ResourceNotFoundException("Role not found"));
            user.setRole(role);
        } else {
            user.setRole(null);
        }
    }

    private String saveImageFile(MultipartFile file) {
        try {
            String uploadDir = "uploads/users/";
            String fileName = file.getOriginalFilename();
            Path uploadPath = Paths.get(uploadDir);

            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            Path filePath = uploadPath.resolve(fileName);
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            return "/" + uploadDir + fileName; // e.g., /uploads/users/user_123.jpg
        } catch (IOException e) {
            throw new RuntimeException("Failed to save image file", e);
        }
    }

    private void deleteImageFileIfExists(String imageUrl) {
        if (imageUrl != null && !imageUrl.isBlank()) {
            try {
                Path filePath = Paths.get(imageUrl.replaceFirst("/", "")).toAbsolutePath();
                Files.deleteIfExists(filePath);
            } catch (IOException e) {
                // Optionally log warning but don't block main flow
                System.err.println("Failed to delete image file: " + e.getMessage());
            }
        }
    }
}
