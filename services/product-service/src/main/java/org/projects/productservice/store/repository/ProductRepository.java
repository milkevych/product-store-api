package org.projects.productservice.store.repository;

import java.util.List;
import java.util.Optional;
import org.projects.productservice.store.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

  @Query("SELECT p FROM Product p LEFT JOIN FETCH p.category LEFT JOIN FETCH " + "p.brand WHERE p.id = :id")
  Optional<Product> findByIdWithCategoryAndBrand(@Param("id") Long id);

  @Query("SELECT p FROM Product p LEFT JOIN FETCH p.category LEFT JOIN FETCH " + "p.brand")
  List<Product> findAllWithCategoryAndBrand();

  List<Product> findAllByIdInOrderById(List<Long> ids);
}
