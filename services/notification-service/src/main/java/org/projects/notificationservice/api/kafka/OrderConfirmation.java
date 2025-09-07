package org.projects.notificationservice.api.kafka;

import java.math.BigDecimal;
import java.util.List;

import org.projects.notificationservice.store.entity.PaymentMethod;
import org.projects.notificationservice.store.entity.ProductPurchaseResponse;
import org.projects.notificationservice.store.entity.User;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class OrderConfirmation{

       private Long orderId;
       private BigDecimal totalAmount;
       private PaymentMethod paymentMethod;
       private User user;
       private List<ProductPurchaseResponse> products;

}
