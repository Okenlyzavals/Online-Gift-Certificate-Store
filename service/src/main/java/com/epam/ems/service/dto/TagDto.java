package com.epam.ems.service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import org.springframework.hateoas.RepresentationModel;

import javax.validation.constraints.*;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TagDto extends RepresentationModel<TagDto> implements DataTransferObject {

    private Long id;

    @NotNull(message = "msg.tag.name.empty")
    @Length(min = 2, max = 45, message = "msg.tag.name.len.wrong")
    private String name;
}
