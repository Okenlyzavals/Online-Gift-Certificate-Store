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

    private Long id;

    @Size(min=3,max=45, message = "msg.cert.name.len.wrong",groups = {OnCreate.class, OnUpdate.class})
    @NotBlank(groups = OnCreate.class, message = "msg.cert.name.blank")
    private String name;

    @Size(min=10,max=512, message = "msg.cert.desc.len.wrong",groups = {OnCreate.class, OnUpdate.class})
    private String description;

    @DecimalMin(value = "0.0", message = "msg.cert.price.negative",groups = {OnCreate.class, OnUpdate.class})
    @DecimalMax(value = "999999", message = "msg.cert.price.large",groups = {OnCreate.class, OnUpdate.class})
    @NotNull(groups = OnCreate.class, message = "msg.cert.price.null")
    private BigDecimal price;

    @Min(value = 0, message = "msg.cert.duration.negative",groups = {OnCreate.class, OnUpdate.class})
    @Max(value = 99999, message = "msg.cert.duration.large",groups = {OnCreate.class, OnUpdate.class})
    @NotNull(groups = OnCreate.class, message = "msg.cert.duration.null")
    private Integer duration;

    @NotNull(groups = OnCreate.class, message = "msg.cert.created.null")
    @PastOrPresent(groups = {OnCreate.class, OnUpdate.class}, message = "msg.cert.created.future")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm'Z'")
    private LocalDateTime createDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm'Z'")
    private LocalDateTime lastUpdateDate;

    private @Valid List<TagDto> tags;
}
