package com.study.store.clients;

import com.study.store.dto.ItemSoldDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "stock", url = "http://localhost:8087")
public interface StockClient {

    @PutMapping("/sales/stock/{id}")
    ResponseEntity<?> updateStock(@PathVariable long id, @RequestBody ItemSoldDto itemSoldDto);

}
