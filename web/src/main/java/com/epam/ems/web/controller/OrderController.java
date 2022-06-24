package com.epam.ems.web.controller;

import com.epam.ems.service.OrderService;
import com.epam.ems.service.dto.OrderDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@RestController
@RequestMapping("/orders")
public class OrderController {

    private final OrderService service;
    private final RepresentationModelAssembler<OrderDto,OrderDto> hateoas;

    @Autowired
    public OrderController(OrderService service, RepresentationModelAssembler<OrderDto,OrderDto> hateoas){
        this.service = service;
        this.hateoas = hateoas;
    }
    @GetMapping
    public PagedModel<OrderDto> getOrders(
            @RequestParam(defaultValue = "0") @Min(0) int page,
            @RequestParam(defaultValue = "5") @Min(1) int size,
            PagedResourcesAssembler<OrderDto> assembler){
        Page<OrderDto> orders = service.getAll(page, size);
        return assembler.toModel(orders, hateoas);
    }

    @GetMapping("/{id}")
    public OrderDto get(@PathVariable @Min(1) long id){
        return hateoas.toModel(service.getById(id));
    }

    @GetMapping("/user/{id}")
    public PagedModel<OrderDto> getByUser(@PathVariable @Min(1) long id,
                                          @RequestParam(defaultValue = "0") @Min(0) int page,
                                          @RequestParam(defaultValue = "5") @Min(1) int size,
                                          PagedResourcesAssembler<OrderDto> assembler){
        Page<OrderDto> orders = service.getOrdersByUser(id, page, size);
        return assembler.toModel(orders, hateoas);
    }

    @PostMapping()
    public OrderDto makeOrder(@RequestBody @NotNull @Valid OrderDto order){
        return hateoas.toModel(service.insert(order));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrder(
            @PathVariable("id") @Min(value = 1, message = "msg.id.negative") long id){
        service.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
