package com.study.store.dto;

import lombok.*;

@Data
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class ItemSoldDto {
    long id;
    int amount;
}
