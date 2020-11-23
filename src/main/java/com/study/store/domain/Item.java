package com.study.store.domain;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBDocument;
import lombok.*;

@Data
@Builder
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@DynamoDBDocument
public class Item {
    private long id;
    private String description;
    private int amount;
}
