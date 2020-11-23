package com.study.store;

import com.github.tomakehurst.wiremock.junit.WireMockRule;
import com.study.store.domain.Item;
import com.study.store.domain.Order;
import com.study.store.dto.ItemDto;
import com.study.store.dto.ItemSoldDto;
import com.study.store.dto.OrderDto;
import com.study.store.service.OrderService;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.github.tomakehurst.wiremock.client.WireMock.ok;

@SpringBootTest
class StoreApplicationTests {

	@Autowired
	OrderService orderService;

	@Rule
	WireMockRule wireMockRule = new WireMockRule(8087);

	@Test
	void case1() {
		OrderDto orderDto = new OrderDto();
		orderDto.setOrderId("4567");
		orderDto.setCustomerId("1");
		orderDto.setName("André Luis Cristiani");
		ItemDto item = new ItemDto();
		item.setId(1);
		item.setDescription("Iphone XR");
		item.setAmount(5);
		orderDto.addItem(item);
		ItemSoldDto itemSoldDto = new ItemSoldDto(item.getId(), item.getAmount());
		stubFor(put(urlMatching("/sales/stock/.*")).willReturn(ok()));
        ResponseEntity response = orderService.saveOrder(orderDto);
		Assert.assertEquals(response.toString(), 200, response.getStatusCodeValue());
	}

}
