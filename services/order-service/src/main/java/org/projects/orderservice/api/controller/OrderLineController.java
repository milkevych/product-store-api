package org.projects.orderservice.api.controller;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.projects.orderservice.api.dto.OrderLineResponseDto;
import org.projects.orderservice.api.service.OrderLineService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/api/v1/order-lines")
@RestController
public class OrderLineController {

  private final OrderLineService service;

  @GetMapping("/order/{order-id}")
  public ResponseEntity<List<OrderLineResponseDto>> getOrderLineById(
          @PathVariable("order-id") Long id
  ) {
    return ResponseEntity.ok(service.getOrderLineById(id));
  }
}
