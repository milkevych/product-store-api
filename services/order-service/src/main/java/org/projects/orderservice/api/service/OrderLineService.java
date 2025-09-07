package org.projects.orderservice.api.service;

import jakarta.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.projects.orderservice.api.dto.OrderLineDto;
import org.projects.orderservice.api.dto.OrderLineResponseDto;
import org.projects.orderservice.api.exception.NotFoundException;
import org.projects.orderservice.store.repository.OrderLineRepository;
import org.projects.orderservice.store.repository.OrderRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderLineService {

  private final OrderLineRepository orderLineRepository;
  private final OrderLineMapper mapper;

  public Long saveOrderLine(OrderLineDto orderLineDto) {
    var orderLine = mapper.toOrderLine(orderLineDto);
    orderLineRepository.save(orderLine);
    return orderLine.getId();
  }

  public List<OrderLineResponseDto> getOrderLineById(Long id) {
    return orderLineRepository.findByIdWithOrder(id)
            .stream()
            .map(mapper::toOrderLineResponseDto)
            .collect(Collectors.toList());
  }
}
