package com.study.store.mappers;

import com.study.store.domain.Order;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel="spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface OrderOrderAvroMapper {

    OrderOrderAvroMapper INSTANCE = Mappers.getMapper(OrderOrderAvroMapper.class);

    com.avro.store.Order OrderToOrderAvro(Order order);

}
