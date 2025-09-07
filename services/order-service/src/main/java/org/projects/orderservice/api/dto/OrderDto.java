package org.projects.orderservice.api.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.projects.orderservice.api.product.ProductPurchaseRequest;
import org.projects.orderservice.store.entity.PaymentMethod;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderDto {
    private BigDecimal totalAmount;
    private PaymentMethod paymentMethod;
    private String userId;
    private List<ProductPurchaseRequest> products;
}
