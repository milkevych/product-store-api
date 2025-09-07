package org.projects.orderservice.store.repository;

import java.util.List;
import java.util.Optional;
import org.projects.orderservice.store.entity.OrderLine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderLineRepository extends JpaRepository<OrderLine, Long> {

  @Query("SELECT l FROM OrderLine l LEFT JOIN FETCH l.order WHERE l.id = :id")
  Optional<OrderLine> findByIdWithOrder(@Param("id") Long id);

  @Query("SELECT l FROM OrderLine l LEFT JOIN FETCH l.order")
  List<OrderLine> findAllWithOrder();
}
