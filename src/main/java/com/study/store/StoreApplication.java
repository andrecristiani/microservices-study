package com.study.store;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.model.CreateTableRequest;
import com.amazonaws.services.dynamodbv2.model.ProvisionedThroughput;
import com.study.store.domain.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.annotation.EnableKafka;

@EnableKafka
@EnableFeignClients
@EnableCircuitBreaker
@SpringBootApplication
public class StoreApplication {

//	@Autowired
//	AmazonDynamoDB amazonDynamoDB;
//
//	@Bean
//	public void createDatabase() {
//		DynamoDBMapper dynamoDBMapper = new DynamoDBMapper(amazonDynamoDB);
//		CreateTableRequest tableRequest = dynamoDBMapper
//				.generateCreateTableRequest(Order.class);
//		tableRequest.setProvisionedThroughput(
//				new ProvisionedThroughput(1L, 1L));
//		amazonDynamoDB.createTable(tableRequest);
//	}

	public static void main(String[] args) {
		SpringApplication.run(StoreApplication.class, args);
	}

}
