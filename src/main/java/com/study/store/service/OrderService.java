package com.study.store.service;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.study.store.clients.StockClient;
import com.study.store.domain.Order;
import com.study.store.dto.ItemSoldDto;
import com.study.store.dto.OrderDto;
import com.study.store.mappers.OrderOrderAvroMapper;
import com.study.store.mappers.OrderOrderDtoMapper;
import com.study.store.producer.OrderProducer;
import com.study.store.repository.OrderRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderProducer orderProducer;

    @Autowired
    private OrderOrderDtoMapper orderOrderDtoMapper;

    @Autowired
    private OrderOrderAvroMapper orderOrderAvroMapper;

    @Autowired
    private StockClient stockClient;

    Logger logger = LoggerFactory.getLogger(OrderService.class);

    @HystrixCommand(threadPoolKey = "getOrderByIdThreadPool")
    public Order getOrderById(String id) {
        Optional<Order> order = this.orderRepository.findById(id);
        if(order != null) {
            return order.get();
        } else {
            return new Order();
        }
    }

    public Iterable<Order> getAllOrders() {
        return this.orderRepository.findAll();
    }

    @HystrixCommand(fallbackMethod = "saveNewOrderFallback", threadPoolKey = "saveOrderThreadPool")
    public ResponseEntity<?> saveOrder(OrderDto orderDto) {
        logger.info("This is a test");
        Order order = orderOrderDtoMapper.INSTANCE.OrderDtoToOrder(orderDto);
//        com.avro.store.Order orderAvr2o = orderOrderAvroMapper.INSTANCE.OrderToOrderAvro(new Order("12","13","23", new ArrayList<>()));
//        ResponseEntity responseEntity = stockClient.updateStock(order.getItems().get(0).getId(), new ItemSoldDto(order.getItems().get(0).getId(), order.getItems().get(0).getAmount()));
        Order orderSaved = this.orderRepository.save(order);
//        com.avro.store.Order orderAvro = orderOrderAvroMapper.INSTANCE.OrderToOrderAvro(orderSaved);
//        orderProducer.sendOrderEmail(orderAvro);
//        return ResponseEntity.status(responseEntity.getStatusCode()).body(orderSaved);
        return ResponseEntity.status(HttpStatus.OK).body(orderSaved);
    }

    public ResponseEntity<?> saveNewOrderFallback(OrderDto orderDto) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Seu pedido número " + orderDto.getOrderId() + " não pode ser realizado, tente novamente mais tarde.");
    }
}
