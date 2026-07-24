package com.sandipsky.inventory_system.common.dto.filter;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RequestDTO {
    private List<FilterDTO> filter;
    private PaginationDTO pagination;
    private List<SortDTO> sortDTO;
}
