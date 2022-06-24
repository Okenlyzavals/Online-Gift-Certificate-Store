package com.epam.ems.dao.entity.role;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter(autoApply = true)
public class RoleConverter implements AttributeConverter<Role, Long> {

    @Override
    public Long convertToDatabaseColumn(Role attribute) {
        return attribute.id;
    }

    @Override
    public Role convertToEntityAttribute(Long dbData) {
        return Role.idValueOf(dbData);
    }
}
