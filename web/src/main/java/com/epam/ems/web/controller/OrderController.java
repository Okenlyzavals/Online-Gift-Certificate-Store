package com.epam.ems.web.controller;

import com.epam.ems.service.OrderService;
import com.epam.ems.service.dto.OrderDto;
import com.epam.ems.service.dto.UserDto;
import com.epam.ems.web.hateoas.Hateoas;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Min;
import java.util.List;

@RestController
@RequestMapping("/orders")
public class OrderController {

    private final OrderService service;
    private final Hateoas<OrderDto> hateoas;

    @Autowired
    public OrderController(OrderService service, Hateoas<OrderDto> hateoas){
        this.service = service;
        this.hateoas = hateoas;
    }
    @GetMapping
    public List<OrderDto> getOrders(
            @RequestParam(defaultValue = "1") @Min(1) int page,
            @RequestParam(defaultValue = "5") @Min(1)int elements){
        List<OrderDto> orders = service.getAll(page, elements);
        orders.forEach(hateoas::buildHateoas);
        return orders;
    }

    @GetMapping("/{id}")
    public OrderDto get(@PathVariable @Min(1) long id){
        return hateoas.buildHateoas(service.getById(id));
    }

    @GetMapping("/by_user/{id}")
    public List<OrderDto> getByUser(@PathVariable @Min(1) long id, UserDto dto){
        dto.setId(id);
        List<OrderDto> orders = service.getOrdersByUser(dto);
        orders.forEach(hateoas::buildHateoas);
        return orders;
    }

    @PostMapping()
    public OrderDto makeOrder(@RequestBody OrderDto order){
        return hateoas.buildHateoas(service.insert(order));
    }

}
