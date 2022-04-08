package com.epam.ems.service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TagDto implements DataTransferObject {
    private Long id;
    private String name;
}
