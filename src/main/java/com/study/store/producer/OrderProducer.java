package com.study.store.producer;

import com.avro.store.Order;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

@Component
@Slf4j
public class OrderProducer {



    @Autowired
    KafkaTemplate<String, com.avro.store.Order> kafkaTemplate;

    String topic = "request-received";

    public ListenableFuture<SendResult<String, Order>> sendOrderEmail(Order order) {

        String key = order.getOrderId();

        ProducerRecord<String, Order> producerRecord = new ProducerRecord<String, Order>(topic, order);
        ListenableFuture<SendResult<String, com.avro.store.Order>> listenableFuture = kafkaTemplate.send(producerRecord);
        listenableFuture.addCallback(new ListenableFutureCallback<SendResult<String, Order>>() {
            @Override
            public void onFailure(Throwable ex) {
                handleFailure(key, order, ex);
            }

            @Override
            public void onSuccess(SendResult<String, com.avro.store.Order> result) {
                handleSuccess(key, order, result);
            }
        });
        return listenableFuture;
    }

    private void handleFailure(String key, com.avro.store.Order value, Throwable ex) {
        log.error("Error sending the message and the exception is {}", ex.getMessage());
        try {
            throw ex;
        } catch (Throwable throwable) {
            log.error("Error in OnFailure: {}", throwable.getMessage());
        }
    }

    private void handleSuccess(String key, com.avro.store.Order value, SendResult<String, com.avro.store.Order> result) {
        log.info("Message Sent SuccessFully for the key : {} and the value is {} , partition is {}", key, value, result.getRecordMetadata().partition());
    }
}
