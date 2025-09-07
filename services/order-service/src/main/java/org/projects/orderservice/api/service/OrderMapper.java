package org.projects.orderservice.api.service;

import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.projects.orderservice.api.dto.OrderDto;
import org.projects.orderservice.api.dto.OrderLineResponseDto;
import org.projects.orderservice.api.dto.OrderResponseDto;
import org.projects.orderservice.store.entity.Order;
import org.projects.orderservice.store.entity.OrderLine;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderMapper {

  public Order toOrder(OrderDto dto) {
      return Order.builder()
          .totalAmount(dto.getTotalAmount())
          .paymentMethod(dto.getPaymentMethod())
          .userId(dto.getUserId())
          .build();
  }

  public OrderResponseDto toOrderResponseDto(Order order) {

    return new OrderResponseDto(
            order.getId(),
            order.getTotalAmount(),
            order.getPaymentMethod(),
            order.getUserId()
    );
  }
}
