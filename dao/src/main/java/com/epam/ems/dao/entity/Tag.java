package com.epam.ems.dao.entity;

import lombok.*;
import lombok.experimental.SuperBuilder;

@Data
@Builder
@AllArgsConstructor
public class Tag{
    protected Long id;
    private String name;
}
