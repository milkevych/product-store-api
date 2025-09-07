package org.projects.orderservice.api.dto;

public record OrderLineResponseDto(

    Long id,

    Long orderId,

    Long productId,

    Integer quantity
) {}
