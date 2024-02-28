package com.startit.itemservice.service;

import com.startit.itemservice.entity.CategoryEntity;
import com.startit.itemservice.repository.CategoryRepo;
import com.startit.itemservice.transfer.Category;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;


@SpringBootTest
@RunWith(SpringRunner.class)
public class CategoryServiceTests {
    @Mock
    CategoryRepo repo;

    @InjectMocks
    CategoryService service;

    @Test
    public void when_save_user_it_should_return_id_set_by_repo() {
        Category category = new Category();
        category.setName("Category");
        category.setDescription("Description");

        var returnedEntity = new CategoryEntity();
        returnedEntity.setId(5L);
        returnedEntity.setName("Category");
        returnedEntity.setDescription("Description");

        when(repo.save(any(CategoryEntity.class))).thenReturn(returnedEntity);

        Long createdCategoryId = service.save(category);

        assertEquals(5L, createdCategoryId.longValue());
    }

    @Test
    public void when_save_user_unsuccessful_it_should_return_exception() {
        Category category = new Category();
        category.setName("Category");
        category.setDescription("Description");

        var returnedEntity = new CategoryEntity();
        returnedEntity.setId(5L);
        returnedEntity.setName("Category");
        returnedEntity.setDescription("Description");

        when(repo.save(any(CategoryEntity.class))).thenThrow(new RuntimeException("Не сохранили объект"));

        assertThrows(RuntimeException.class, () -> {
            service.save(category);
        });
    }

    @Test
    public void when_get_all_category_it_should_return_page() {
        var entity = new CategoryEntity();
        entity.setId(5L);
        entity.setName("Category");
        entity.setDescription("Description");

        when(repo.findAll(any(Pageable.class))).thenReturn(new PageImpl<>(List.of(entity)));

        var status = service.getAll(Pageable.unpaged());

        assertEquals(entity.getId(), status.stream().toList().get(0).getId());
        assertEquals(entity.getName(), status.stream().toList().get(0).getName());
        assertEquals(entity.getDescription(), status.stream().toList().get(0).getDescription());
    }
}

