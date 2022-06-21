package com.epam.ems.service.validation.custom;

import com.epam.ems.service.dto.TagDto;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TagDtoListValidatorTest {
    private final TagDtoListValidator validator = new TagDtoListValidator();

    @Test
    void validTagListTest(){
        Set<TagDto> valid = Set.of(new TagDto(1L,"tag"),new TagDto(2L,"tog"),
                new TagDto(null,"tyg"));

        assertTrue(validator.isValid(valid, null));
    }

    @Test
    void invalidTagListTest(){
        Set<TagDto> invalid = Set.of(new TagDto(1L,"t"),
                new TagDto(2L,"Empty. This was used, man. This was actually used. I wonder how many kikes this little can took out. Huh? Think about it! "),
                new TagDto(3L,null));

        assertFalse(validator.isValid(invalid, null));
    }

    @Test
    void validateNullList(){
        assertTrue(validator.isValid(null,null));
    }
}
