package com.sandipsky.inventory_system.common.dto.filter;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SortDTO {
    private String field;
    private String orderType;
}
