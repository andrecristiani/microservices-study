package com.study.store.unit;

import com.github.tomakehurst.wiremock.junit.WireMockRule;
import com.study.store.clients.StockClient;
import com.study.store.domain.Item;
import com.study.store.domain.Order;
import com.study.store.dto.ItemDto;
import com.study.store.dto.OrderDto;
import com.study.store.exception.decoder.FeignErrorDecoder;
import com.study.store.exception.handler.FeignExceptionHandler;
import com.study.store.mappers.OrderOrderAvroMapperImpl;
import com.study.store.mappers.OrderOrderDtoMapperImpl;
import com.study.store.producer.OrderProducer;
import com.study.store.repository.OrderRepository;
import com.study.store.service.OrderService;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.TopicPartition;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.support.SendResult;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.concurrent.SettableListenableFuture;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = {OrderOrderAvroMapperImpl.class, OrderOrderDtoMapperImpl.class, FeignExceptionHandler.class, FeignErrorDecoder.class})
public class StoreServiceTest {

    @InjectMocks
    public OrderService orderService;

    @Mock
    public OrderRepository orderRepository;

    @Mock
    private OrderProducer orderProducer;

    @Rule
    public WireMockRule wireMockRule = new WireMockRule(8087);

    @Mock
    StockClient stockClient;

    public Order order;

    public List<Item> items;

    public String requestReceivedTopic = "request-received";

    @Before
    public void setup() {
        items = new ArrayList<>();
        items.add(new Item(1, "TV Led", 2));
        order = new Order("1234", "10", "André Luis Cristiani", items);

        when(orderRepository.save(order)).thenReturn(order);

//        stubFor(put(urlMatching("/sales/stock/.*")).willReturn(aResponse()));

        SettableListenableFuture future = new SettableListenableFuture();

        ProducerRecord<String, com.avro.store.Order> producerRecord = new ProducerRecord(requestReceivedTopic, order);
        RecordMetadata recordMetadata = new RecordMetadata(new TopicPartition(requestReceivedTopic, 1),
                1,1,342,System.currentTimeMillis(), 1, 2);
        SendResult<String, com.avro.store.Order> sendResult = new SendResult<String, com.avro.store.Order>(producerRecord,recordMetadata);

        future.set(sendResult);

        when(orderProducer.sendOrderEmail(isA(com.avro.store.Order.class))).thenReturn(future);
    }

    @Test
    public void Save_Valid_Order() {
        List<ItemDto> itemsDto = new ArrayList<>();
        itemsDto.add(new ItemDto(1, "TV Led", 2));
        OrderDto orderDto = new OrderDto("1234", "10", "André Luis Cristiani", itemsDto);

        when(stockClient.updateStock(anyLong(), anyObject())).thenReturn(new ResponseEntity(HttpStatus.OK));

        ResponseEntity responseEntity = orderService.saveOrder(orderDto);
        Order orderSaved = (Order) responseEntity.getBody();

        Assert.assertEquals(200, responseEntity.getStatusCodeValue());
        Assert.assertEquals(order.getOrderId(), orderSaved.getOrderId());
    }

    @Test
    public void Saves_Order_With_Nonexistent_Item() {
        List<ItemDto> itemsDto = new ArrayList<>();
        itemsDto.add(new ItemDto(2, "Smartphone X", 2));
        OrderDto orderDto = new OrderDto("1234", "10", "André Luis Cristiani", itemsDto);

        when(stockClient.updateStock(anyLong(), anyObject())).thenReturn(new ResponseEntity("Estoque insuficiente para essa compra.", HttpStatus.BAD_REQUEST));

        ResponseEntity responseEntity = orderService.saveOrder(orderDto);

        Assert.assertEquals(400, responseEntity.getStatusCodeValue());
    }
}
