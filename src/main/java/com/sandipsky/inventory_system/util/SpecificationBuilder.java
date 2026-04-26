package com.sandipsky.inventory_system.util;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;

import com.sandipsky.inventory_system.dto.filter.FilterDTO;
import com.sandipsky.inventory_system.dto.filter.SortDTO;

import jakarta.persistence.criteria.*;

import java.util.ArrayList;
import java.util.List;

public class SpecificationBuilder<T> {
    
    public Specification<T> buildSpecification(List<FilterDTO> filters) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (filters != null) {
                for (FilterDTO filter : filters) {
                    if (filter.getField() != null && filter.getValue() != null) {
                        try {
                            Path<?> path = getPath(root, filter.getField());
                            Expression<String> expression = cb.lower(path.as(String.class));
                            predicates.add(cb.like(expression, "%" + filter.getValue().toLowerCase() + "%"));
                        } catch (IllegalArgumentException e) {
                            System.out.println("Skipping unknown filter field: " + filter.getField());
                        }
                    }
                }
            }
            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }

    private Path<?> getPath(Root<?> root, String fieldName) {
        if (fieldName.contains(".")) {
            String[] parts = fieldName.split("\\.");
            Path<?> path = root.get(parts[0]);
            for (int i = 1; i < parts.length; i++) {
                path = path.get(parts[i]);
            }
            return path;
        } else {
            return root.get(fieldName);
        }
    }

    public Sort buildSort(List<SortDTO> sortDTOs) {
        List<Sort.Order> orders = new ArrayList<>();
        if (sortDTOs != null) {
            for (SortDTO dto : sortDTOs) {
                if (dto.getField() != null && dto.getOrderType() != null) {
                    String property = dto.getField();
                    Sort.Order order = "desc".equalsIgnoreCase(dto.getOrderType())
                            ? Sort.Order.desc(property)
                            : Sort.Order.asc(property);
                    orders.add(order);
                }
            }
        }
        return orders.isEmpty() ? Sort.unsorted() : Sort.by(orders);
    }
}
