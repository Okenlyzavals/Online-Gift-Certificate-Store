package com.epam.ems.service.dto;

import com.epam.ems.service.mapper.serializer.UserDtoSerializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import java.util.Collection;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Relation
@JsonSerialize(using = UserDtoSerializer.class)
public class UserDto extends RepresentationModel<UserDto>
        implements UserDetails {

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

    @Null(message = "msg.user.role.not.null")
    private Role role;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(role);
    }

    @Override
    public boolean isAccountNonExpired() {
        return !Role.LOCKED.equals(role);
    }

    @Override
    public boolean isAccountNonLocked() {
        return !Role.LOCKED.equals(role);
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return !Role.LOCKED.equals(role);
    }

    @Override
    public boolean isEnabled() {
        return !Role.LOCKED.equals(role);
    }

    public enum Role implements GrantedAuthority{
        ADMIN, USER, LOCKED;

        @Override
        public String getAuthority() {
            return "ROLE_"+ this.name();
        }
    }
}
