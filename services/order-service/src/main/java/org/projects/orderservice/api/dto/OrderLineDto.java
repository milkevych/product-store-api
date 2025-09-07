package org.projects.orderservice.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;

public record OrderLineDto(

    @NotNull
    @JsonProperty("order_id") 
    Long orderId,

    @NotNull
    @JsonProperty("product_id")
    Long productId,

    @NotNull 
    Integer quantity
) {}
