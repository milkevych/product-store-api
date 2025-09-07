package org.projects.orderservice.api.controller;

import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.projects.orderservice.api.dto.OrderDto;
import org.projects.orderservice.api.dto.OrderResponseDto;
import org.projects.orderservice.api.service.OrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/api/v1/orders")
@RestController
public class OrderController {

  private final OrderService service;

  @PostMapping
  public ResponseEntity<Long> createOrder(
          @RequestBody @Valid OrderDto dto
  ) {
    Long savedId = service.createOrder(dto);
    return ResponseEntity.created(URI.create("/api/v1/orders" + savedId)).body(savedId);
  }

  @GetMapping
  public ResponseEntity<List<OrderResponseDto>> getAllOrders() {
    return ResponseEntity.ok(service.getAllOrders());
  }

  @GetMapping("/{order-id}")
  public ResponseEntity<OrderResponseDto> getOrderById(
          @PathVariable("order-id") Long id
  ) {
    return ResponseEntity.ok(service.getOrderById(id));
  }
}
