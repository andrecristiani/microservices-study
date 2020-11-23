package com.study.store.repository;

import com.study.store.domain.Order;
import org.socialsignin.spring.data.dynamodb.repository.EnableScan;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

@EnableScan
public interface OrderRepository extends CrudRepository<Order, String> {

    Optional<Order> findById(String id);

}
