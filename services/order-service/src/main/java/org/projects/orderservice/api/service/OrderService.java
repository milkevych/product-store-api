package org.projects.orderservice.api.service;

import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;

import org.projects.orderservice.api.customer.User;
import org.projects.orderservice.api.dto.OrderDto;
import org.projects.orderservice.api.dto.OrderLineDto;
import org.projects.orderservice.api.dto.OrderResponseDto;
import org.projects.orderservice.api.exception.NotFoundException;
import org.projects.orderservice.api.kafka.OrderConfirmation;
import org.projects.orderservice.api.kafka.OrderPaymentProducer;
import org.projects.orderservice.api.product.ProductClient;
import org.projects.orderservice.api.product.ProductPurchaseRequest;
import org.projects.orderservice.store.repository.OrderRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class OrderService {

  private final OrderRepository repository;
  private final OrderMapper orderMapper;
  private final OrderLineService orderLineService;
  private final ProductClient productClient;
  private final OrderPaymentProducer producer;

  @Transactional
  public Long createOrder(OrderDto dto) {

    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    User user = ((User) authentication.getPrincipal());

    var order = orderMapper.toOrder(dto);
    order.setUserId(user.getId());
    var saveOrder = repository.save(order);

    var purchaseProducts = productClient.purchaseProducts(dto.getProducts());

    for (ProductPurchaseRequest request : dto.getProducts()) {
        orderLineService.saveOrderLine(
                new OrderLineDto(
                        order.getId(),
                        request.productId(),
                        request.quantity()
                )
        );
    }

    var orderConfirmation = new OrderConfirmation(
            order.getId(),
            dto.getTotalAmount(),
            dto.getPaymentMethod(),
            user,
            purchaseProducts
    );

    producer.sendOrderMessage(orderConfirmation);

    return saveOrder.getId();
  }

  public List<OrderResponseDto> getAllOrders() {
    return repository.findAll()
        .stream()
        .map(orderMapper::toOrderResponseDto)
        .collect(Collectors.toList());
  }

  public OrderResponseDto getOrderById(Long id) {
    return repository.findById(id)
        .map(orderMapper::toOrderResponseDto)
        .orElseThrow(
            () -> new NotFoundException("Order not found with ID: " + id));
  }
}
