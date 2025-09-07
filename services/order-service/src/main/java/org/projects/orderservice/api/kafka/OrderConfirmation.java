package org.projects.orderservice.api.kafka;

import org.projects.orderservice.api.customer.User;
import org.projects.orderservice.api.product.ProductPurchaseResponse;
import org.projects.orderservice.store.entity.PaymentMethod;

import java.math.BigDecimal;
import java.util.List;

public record OrderConfirmation(
       Long orderId,
       BigDecimal totalAmount,
       PaymentMethod paymentMethod,
       User user,
       List<ProductPurchaseResponse> products
) {
}
