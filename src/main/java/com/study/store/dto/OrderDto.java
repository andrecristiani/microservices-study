package com.study.store.dto;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Data
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class OrderDto {
    private String orderId;
    private String customerId;
    private String name;
    private List<ItemDto> items = new ArrayList<>();

    public void addItem(ItemDto item) {
        this.items.add(item);
    }
}
