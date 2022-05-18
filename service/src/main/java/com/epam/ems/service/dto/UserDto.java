package com.epam.ems.service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import org.springframework.hateoas.RepresentationModel;

import javax.validation.constraints.NotNull;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDto extends RepresentationModel<UserDto> implements DataTransferObject{

    private Long id;

    @NotNull(message = "msg.user.name.null")
    @Length(min = 3, max = 128, message = "msg.user.name.wrong.len")
    private String username;

    @NotNull(message = "msg.user.email.null")
    @Length(min = 3, max = 128, message = "msg.user.email.wrong.len")
    private String email;

    @NotNull(message = "msg.user.pass.null")
    @Length(min = 8, max = 256, message = "msg.user.pass.wrong.len")
    private String password;

}
