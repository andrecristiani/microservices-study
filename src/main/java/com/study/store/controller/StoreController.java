package com.study.store.controller;

import com.study.store.domain.Order;
import com.study.store.dto.OrderDto;
import com.study.store.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/orders")
public class StoreController {

    @Autowired
    private OrderService orderService;

    @GetMapping
    public Iterable<Order> findAllOrders() {
        return this.orderService.getAllOrders();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getOrderById(@PathVariable String id) {
        Order order = orderService.getOrderById(id);
        return ResponseEntity.ok(order);
    }

    @PostMapping
    public ResponseEntity<?> saveOrder(@RequestBody OrderDto orderDto) {
        return orderService.saveOrder(orderDto);
    }


}
