package org.projects.orderservice.store.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "order_table")
public class Order {

  @Id 
  @GeneratedValue(strategy = GenerationType.SEQUENCE) 
  private Long id;

  private BigDecimal totalAmount;

  @OneToMany(mappedBy = "order", fetch = FetchType.LAZY)
  private List<OrderLine> orderLines;

  @Enumerated(EnumType.STRING)
  private PaymentMethod paymentMethod;

  private String userId;

  @CreatedDate
  @Column(updatable = false, nullable = false)
  private Instant createdDate;

  @LastModifiedDate
  @Column(insertable = false)
  private Instant lastModifiedDate;
}
