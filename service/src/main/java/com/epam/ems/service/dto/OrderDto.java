package com.epam.ems.service.dto;

import com.epam.ems.service.validation.custom.constraint.OrderDtoUserConstraint;
import com.epam.ems.service.validation.custom.constraint.OrderedCertificateListConstraint;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderDto extends RepresentationModel<OrderDto> {

    private Long id;

    private BigDecimal price;

    @NotNull(message = "msg.order.created.null")
    @PastOrPresent(message = "msg.order.created.future")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm'Z'")
    private LocalDateTime date;

    @OrderDtoUserConstraint
    private UserDto user;

    @OrderedCertificateListConstraint
    private List<GiftCertificateDto> certificates;
}
