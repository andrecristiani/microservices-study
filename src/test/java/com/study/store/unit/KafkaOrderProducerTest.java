package com.study.store.unit;

import com.avro.store.Item;
import com.avro.store.Order;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.study.store.producer.OrderProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.TopicPartition;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.SettableListenableFuture;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class KafkaOrderProducerTest {

    @Mock
    KafkaTemplate<String, Order> kafkaTemplate;

    @InjectMocks
    OrderProducer orderProducer;

    Order order;

    List<Item> items;

    String requestReceivedTopic = "request-received";

    @Before
    public void setup() {
        items = new ArrayList<>();
        items.add(new Item((long) 1, "TV Led", 2));
        order = new Order("1234", "10", "André Luis Cristiani", items);
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void sendEmail_success() throws JsonProcessingException, ExecutionException, InterruptedException {
        //given
        SettableListenableFuture future = new SettableListenableFuture();

        ProducerRecord<String, Order> producerRecord = new ProducerRecord(requestReceivedTopic, order);
        RecordMetadata recordMetadata = new RecordMetadata(new TopicPartition(requestReceivedTopic, 1),
                1,1,342,System.currentTimeMillis(), 1, 2);
        SendResult<String, Order> sendResult = new SendResult<String, Order>(producerRecord,recordMetadata);

        future.set(sendResult);
        when(kafkaTemplate.send(isA(ProducerRecord.class))).thenReturn(future);
        //when
        ListenableFuture<SendResult<String, Order>> listenableFuture =  orderProducer.sendOrderEmail(order);

        //then
        SendResult<String, Order> sendResult1 = listenableFuture.get();
        assert sendResult1.getRecordMetadata().partition()==1;
    }

    @Test
    public void sendEmail_Failure() throws ExecutionException, InterruptedException {
        SettableListenableFuture future = new SettableListenableFuture();

        future.setException(new RuntimeException("Exception Calling Kafka"));
        when(kafkaTemplate.send(isA(ProducerRecord.class))).thenReturn(future);

        assertThrows(Exception.class, () -> orderProducer.sendOrderEmail(order).get());
    }


}
