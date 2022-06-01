package com.epam.ems.web.controller;

import com.epam.ems.service.OrderService;
import com.epam.ems.service.dto.OrderDto;
import com.epam.ems.web.hateoas.Hateoas;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

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
    public CollectionModel<OrderDto> getOrders(
            @RequestParam(defaultValue = "1") @Min(1) int page,
            @RequestParam(defaultValue = "5") @Min(1) int elements){
        List<OrderDto> orders = service.getAll(page, elements);
        orders.forEach(hateoas::buildHateoas);
        return hateoas.buildPaginationModel(orders,
                ()->page < 2 ? null : methodOn(getClass()).getOrders(1,elements),
                ()->orders.size() < elements ? null : methodOn(getClass()).getOrders(page+1, elements),
                ()->page < 2 ? null : methodOn(getClass()).getOrders(page-1, elements));
    }

    @GetMapping("/{id}")
    public OrderDto get(@PathVariable @Min(1) long id){
        return hateoas.buildHateoas(service.getById(id));
    }

    @GetMapping("/user/{id}")
    public CollectionModel<OrderDto> getByUser(@PathVariable @Min(1) long id,
                                    @RequestParam(defaultValue = "1") @Min(1) int page,
                                    @RequestParam(defaultValue = "5") @Min(1) int elements){
        List<OrderDto> orders = service.getOrdersByUser(id, page, elements);
        orders.forEach(hateoas::buildHateoas);
        return hateoas.buildPaginationModel(orders,
                ()->page < 2 ? null : methodOn(getClass()).getByUser(id,1,elements),
                ()->orders.size() < elements ? null : methodOn(getClass()).getByUser(id, page+1, elements),
                ()->page < 2 ? null : methodOn(getClass()).getByUser(id, page-1, elements));
    }

    @PostMapping()
    public OrderDto makeOrder(@RequestBody @NotNull @Valid OrderDto order){
        return hateoas.buildHateoas(service.insert(order));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrder(
            @PathVariable("id") @Min(value = 1, message = "msg.id.negative") long id){
        service.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
