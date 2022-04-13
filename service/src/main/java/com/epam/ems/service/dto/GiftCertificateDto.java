package com.epam.ems.service.dto;


import com.epam.ems.service.validation.OnCreate;
import com.epam.ems.service.validation.OnUpdate;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GiftCertificateDto implements DataTransferObject {

    @Min(value = 1,groups = {OnCreate.class, OnUpdate.class})
    private Long id;

    @Size(min=3,max=45,groups = {OnCreate.class, OnUpdate.class})
    @NotBlank(groups = OnCreate.class)
    private String name;

    @Size(min=10,max=512,groups = {OnCreate.class, OnUpdate.class})
    private String description;

    @DecimalMin(value = "0.0",groups = {OnCreate.class, OnUpdate.class})
    @DecimalMax(value = "999999",groups = {OnCreate.class, OnUpdate.class})
    @NotNull(groups = OnCreate.class)
    private BigDecimal price;

    @DecimalMin(value = "0.0",groups = {OnCreate.class, OnUpdate.class})
    @DecimalMax(value = "999999",groups = {OnCreate.class, OnUpdate.class})
    @NotNull(groups = OnCreate.class)
    private Integer duration;

    @NotNull(groups = OnCreate.class)
    @PastOrPresent(groups = {OnCreate.class, OnUpdate.class})
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm'Z'")
    private LocalDateTime createDate;

    @NotNull(groups = OnCreate.class)
    @PastOrPresent(groups = {OnCreate.class, OnUpdate.class})
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm'Z'")
    private LocalDateTime lastUpdateDate;

    private @Valid List<TagDto> tags;
}
