package org.projects.paymentservice.api.kafka.order;

import lombok.Getter;
import lombok.Setter;
import org.projects.paymentservice.store.entity.PaymentMethod;
import org.projects.paymentservice.store.entity.Product;
import org.projects.paymentservice.store.entity.User;

import lombok.ToString;

import java.math.BigDecimal;
import java.util.List;

@ToString
@Getter
@Setter
public class OrderConfirmation {
       private Long orderId;
       private BigDecimal totalAmount;
       private PaymentMethod paymentMethod;
       private User user;
       private List<Product> products;
}


