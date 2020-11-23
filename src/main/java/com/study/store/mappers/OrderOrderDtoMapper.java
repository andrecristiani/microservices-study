package com.study.store.mappers;

import com.study.store.domain.Order;
import com.study.store.dto.OrderDto;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel="spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface OrderOrderDtoMapper {

    OrderOrderDtoMapper INSTANCE = Mappers.getMapper(OrderOrderDtoMapper.class);

    Order OrderDtoToOrder(OrderDto orderDto);
}
