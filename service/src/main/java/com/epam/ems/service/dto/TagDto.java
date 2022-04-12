package com.epam.ems.service.dto;

import com.epam.ems.service.validation.OnCreate;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TagDto implements DataTransferObject {

    private Long id;

    @NotNull
    @Length(min = 3, max = 45)
    private String name;
}
