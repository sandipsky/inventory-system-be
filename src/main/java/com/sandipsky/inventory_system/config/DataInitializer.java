package com.sandipsky.inventory_system.config;

import com.sandipsky.inventory_system.dto.UserDTO;
import com.sandipsky.inventory_system.repository.RoleRepository;
import com.sandipsky.inventory_system.repository.UserRepository;
import com.sandipsky.inventory_system.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private RoleRepository roleRepository;

    @Override
    public void run(String... args) {
        if (!userRepository.existsByUsername("admin") && !userRepository.existsByEmail("admin@admin.com")) {
            UserDTO adminDto = new UserDTO();
            adminDto.setUsername("admin");
            adminDto.setEmail("admin@admin.com");
            adminDto.setFullName("Admin");
            adminDto.setPassword("Admin@123");
            adminDto.setGender("Other"); // or null or any appropriate value
            adminDto.setContact("0000000000"); // default
            adminDto.setActive(true);
            adminDto.setRemarks("Admin User");
            roleRepository.findByName("Admin").ifPresent(r -> adminDto.setRoleId(r.getId()));
            userService.saveUser(adminDto, null);
        }
    }
}
