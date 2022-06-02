package com.epam.ems.dao.entity.role;

import java.util.Arrays;

public enum Role {
    ADMIN(1L), USER(2L), LOCKED(3L);

    final Long id;

    Role(Long id) {
        this.id = id;
    }

    static Role idValueOf(Long id){
        return Arrays.stream(values())
                .filter(role -> role.id.equals(id))
                .findFirst()
                .orElseThrow();
    }

}
