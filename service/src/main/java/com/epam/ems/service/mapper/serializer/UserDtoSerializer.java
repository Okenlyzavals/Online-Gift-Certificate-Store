package com.epam.ems.service.mapper.serializer;

import com.epam.ems.service.dto.UserDto;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;

public class UserDtoSerializer extends StdSerializer<UserDto> {

    protected UserDtoSerializer() {
        super(UserDto.class);
    }

    @Override
    public void serialize(UserDto value, JsonGenerator gen, SerializerProvider provider) throws IOException {
        gen.writeStartObject();
        gen.writeNumberField("id", value.getId());
        gen.writeStringField("username", value.getUsername());
        gen.writeStringField("email", value.getEmail());
        gen.writeStringField("role", value.getRole().name());
        gen.writeEndObject();
    }
}
