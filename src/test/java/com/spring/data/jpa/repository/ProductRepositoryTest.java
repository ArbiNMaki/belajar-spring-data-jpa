package com.spring.data.jpa.repository;

import com.spring.data.jpa.entity.Category;
import com.spring.data.jpa.entity.Product;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class ProductRepositoryTest {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Test
    void createProduct() {
        Category category = categoryRepository.findById(4L).orElse(null);
        assertNotNull(category);

        {
            Product product = new Product();
            product.setName("Apple Iphone 14 Pro Max");
            product.setPrice(25_000_000L);
            product.setCategory(category);
            productRepository.save(product);
        }
        {
            Product product = new Product();
            product.setName("Apple Iphone 15 Pro Max");
            product.setPrice(35_000_000L);
            product.setCategory(category);
            productRepository.save(product);
        }
    }

    @Test
    void findProducts() {
        List<Product> products = productRepository
                .findAllByCategory_Name("GADGET MURAH");

        assertEquals(2, products.size());
        assertEquals("Apple Iphone 14 Pro Max", products.get(0).getName());
        assertEquals("Apple Iphone 15 Pro Max", products.get(1).getName());
    }

    @Test
    void findProductsSort() {
        Sort sort = Sort.by(Sort.Order.desc("id"));
        List<Product> products = productRepository.findAllByCategory_Name("GADGET MURAH", sort);

        assertEquals(2, products.size());
        assertEquals("Apple Iphone 15 Pro Max", products.get(0).getName());
        assertEquals("Apple Iphone 14 Pro Max", products.get(1).getName());
    }

    @Test
    void findProductsWithPageable() {
        // Page 0
        Pageable pageable = PageRequest.of
                (0, 1, Sort.by(Sort.Order.desc("id")));
        Page<Product> products = productRepository.findAllByCategory_Name("GADGET MURAH", pageable);

        assertEquals(1, products.getContent().size());
        assertEquals(0, products.getNumber());
        assertEquals(2, products.getTotalElements());
        assertEquals(2, products.getTotalPages());
        assertEquals("Apple Iphone 15 Pro Max", products.getContent().get(0).getName());

        // Page 1
        pageable = PageRequest.of
                (1, 1, Sort.by(Sort.Order.desc("id")));
        products = productRepository.findAllByCategory_Name("GADGET MURAH", pageable);

        assertEquals(1, products.getContent().size());
        assertEquals(1, products.getNumber());
        assertEquals(2, products.getTotalElements());
        assertEquals(2, products.getTotalPages());
        assertEquals("Apple Iphone 14 Pro Max", products.getContent().get(0).getName());
    }

    @Test
    void count() {
        Long count = productRepository.count();
        assertEquals(2L, count);

        count = productRepository.countByCategory_name("GADGET MURAH");
        assertEquals(2L, count);
    }
}