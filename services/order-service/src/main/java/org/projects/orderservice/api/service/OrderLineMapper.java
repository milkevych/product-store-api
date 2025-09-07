package org.projects.orderservice.api.service;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.projects.orderservice.api.dto.OrderLineDto;
import org.projects.orderservice.api.dto.OrderLineResponseDto;
import org.projects.orderservice.store.entity.Order;
import org.projects.orderservice.store.entity.OrderLine;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderLineMapper {

  public OrderLine toOrderLine(OrderLineDto dto) {
    var order = Order.builder().id(dto.orderId()).build();
    return OrderLine.builder()
            .order(order)
            .productId(dto.productId())
            .quantity(dto.quantity())
            .build();
  }

  public OrderLineResponseDto toOrderLineResponseDto(OrderLine orderLine) {

    return new OrderLineResponseDto(
            orderLine.getId(),
            orderLine.getOrder().getId(),
            orderLine.getProductId(),
            orderLine.getQuantity()
    );
  }
}
