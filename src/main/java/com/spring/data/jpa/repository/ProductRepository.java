package com.spring.data.jpa.repository;

import com.spring.data.jpa.entity.Category;
import com.spring.data.jpa.entity.Product;
import jakarta.persistence.LockModeType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long>, JpaSpecificationExecutor<Product> {

    Long countByCategory_name(String name);

    boolean existsByName(String name);

    @Transactional
    int deleteByName(String name);

    @Modifying
    @Query("DELETE FROM Product p WHERE p.name = :name")
    int deleteProductUsingName(@Param("name") String name);

    @Modifying
    @Query("UPDATE Product p SET p.price = 0 WHERE p.id = :id")
    int updateProductPriceToZero(@Param("id") Long id);

    List<Product> findAllByCategory_Name(String name);

    List<Product> findAllByCategory_Name(String name, Sort sort);

    Page<Product> findAllByCategory_Name(String name, Pageable pageable);

    List<Product> searchProductUsingName(@Param("name") String name, Pageable pageable);

    @Query(
            value = "SELECT p FROM Product p WHERE p.name LIKE :name OR p.category.name LIKE :name",
            countQuery = "SELECT count(p) FROM Product p WHERE p.name LIKE :name OR p.category.name LIKE :name"
    )
    Page<Product> searchProduct(@Param("name") String name, Pageable pageable);

    Stream<Product> streamAllByCategory(Category category);

    Slice<Product> findAllByCategory(Category category, Pageable pageable);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<Product> findFirstByIdEquals(Long id);
}
