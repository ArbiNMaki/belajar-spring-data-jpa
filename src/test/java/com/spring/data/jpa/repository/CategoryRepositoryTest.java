package com.spring.data.jpa.repository;

import com.spring.data.jpa.entity.Category;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class CategoryRepositoryTest {

    @Autowired
    private CategoryRepository categoryRepository;

    @Test
    void insert() {
        Category category = new Category();
        category.setName("GADGET");

        categoryRepository.save(category);

        assertNotNull(category.getId());
    }

    @Test
    void update() {
        Category category = categoryRepository.findById(4L).orElse(null);
        assertNotNull(category);

        category.setName("GADGET MURAH");
        categoryRepository.save(category);

        category = categoryRepository.findById(4L).orElse(null);
        assertNotNull(category);
        assertEquals("GADGET MURAH", category.getName());
    }

    @Test
    void delete() {
        Category category = categoryRepository.findById(4L).orElse(null);
        assertNotNull(category);

        categoryRepository.delete(category);

        category = categoryRepository.findById(4L).orElse(null);
        assertNull(category);
    }

    @Test
    void queryMethod() {
        Category category = categoryRepository.findFirstByNameEquals("GADGET MURAH").orElse(null);
        assertNotNull(category);
        assertEquals("GADGET MURAH", category.getName());

        List<Category> categories = categoryRepository.findAllByNameLike("%GADGET%");
        assertEquals(1, categories.size());
        assertEquals("GADGET MURAH", categories.get(0).getName());
    }

    @Test
    void audit() {
        Category category = new Category();
        category.setName("Sample Audit");
        categoryRepository.save(category);

        assertNotNull(category.getId());
        assertNotNull(category.getCreatedDate());
        assertNotNull(category.getLastModifiedDate());
    }
}