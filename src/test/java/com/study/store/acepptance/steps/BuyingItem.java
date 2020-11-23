package com.study.store.acepptance.steps;

import com.github.tomakehurst.wiremock.junit.WireMockRule;
import com.study.store.clients.StockClient;
import com.study.store.domain.Item;
import com.study.store.domain.Order;
import com.study.store.dto.ItemDto;
import com.study.store.dto.OrderDto;
import com.study.store.mappers.OrderOrderAvroMapperImpl;
import com.study.store.mappers.OrderOrderDtoMapperImpl;
import com.study.store.producer.OrderProducer;
import com.study.store.repository.OrderRepository;
import com.study.store.service.OrderService;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.cucumber.junit.Cucumber;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.TopicPartition;
import org.junit.Before;
import org.junit.Rule;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.support.SendResult;

import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.concurrent.SettableListenableFuture;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {OrderOrderAvroMapperImpl.class, OrderOrderDtoMapperImpl.class})
public class BuyingItem {

    @Bean
    OrderService orderService() {
        return new OrderService();
    }

    @InjectMocks
    public OrderService orderService;

    @Mock
    public OrderRepository orderRepository;

    @Mock
    private OrderProducer orderProducer;

    @Mock
    public StockClient stockClient;

    @Rule
    public WireMockRule wireMockRule = new WireMockRule(8087);

    OrderDto orderDto;
    ResponseEntity response;
    public String requestReceivedTopic = "request-received";

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        List<Item> items = new ArrayList<>();
        items.add(new Item(1, "TV Led", 2));
        Order order = new Order("1234", "10", "André Luis Cristiani", items);
        when(orderRepository.save(isA(Order.class))).thenReturn(order);

        when(stockClient.updateStock(anyLong(), anyObject())).thenReturn(new ResponseEntity(HttpStatus.OK));

        SettableListenableFuture future = new SettableListenableFuture();

        ProducerRecord<String, com.avro.store.Order> producerRecord = new ProducerRecord(requestReceivedTopic, order);
        RecordMetadata recordMetadata = new RecordMetadata(new TopicPartition(requestReceivedTopic, 1),
                1,1,342,System.currentTimeMillis(), 1, 2);
        SendResult<String, com.avro.store.Order> sendResult = new SendResult<String, com.avro.store.Order>(producerRecord,recordMetadata);

        future.set(sendResult);

        when(orderProducer.sendOrderEmail(isA(com.avro.store.Order.class))).thenReturn(future);
    }

    @Given("Dado um pedido de compra")
    public void dado_um_pedido_de_compra() {
        List<ItemDto> itemsDto = new ArrayList<>();
        itemsDto.add(new ItemDto(1, "TV Led", 2));
        orderDto = new OrderDto("1234", "10", "André Luis Cristiani", itemsDto);
    }

    @Given("Com os seguintes items")
    public void com_os_seguintes_items() {

    }

    @When("Quando tenta salvar o pedido")
    public void quando_tenta_salvar_o_pedido() {
        response = orderService.saveOrder(orderDto);
    }

    @Then("O pedido eh salvo")
    public void o_pedido_eh_salvo() {
//        Order orderSaved = (Order) response.getBody();
//        assertEquals(200, response.getStatusCodeValue());
//        assertEquals(orderDto.getCustomerId(), orderSaved.getOrderId());
    }

}
