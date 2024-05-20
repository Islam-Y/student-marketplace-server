package com.startit.userservice.service;

import com.startit.userservice.entity.UserEntity;
import com.startit.userservice.repository.UserRepo;
import com.startit.shared.transfer.User;
import com.startit.shared.transfer.Role;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
@RunWith(SpringRunner.class)
public class UserServiceTests {

    @Mock
    UserRepo repo;

    @InjectMocks
    UserService service;

    @Test
    public void when_save_user_it_should_return_id_set_by_repo() {
        var user = new User();
        user.setId(null);
        user.setName("Example");
        user.setUsername("Example_Username");
        user.setFamilyName("Example_FamilyName");
        user.setRole(Role.SELLER);

        var returnedEntity = new UserEntity();
        returnedEntity.setId(5L);
        returnedEntity.setName("Example");
        returnedEntity.setUsername("Example_Username");
        returnedEntity.setFamilyName("Example_FamilyName");
        returnedEntity.setRole(Role.SELLER);
        when(repo.save(any(UserEntity.class))).thenReturn(returnedEntity);

        Long createdUserId = service.save(user);

        assertEquals(5L, createdUserId.longValue());
    }

    @Test
    public void when_find_by_id_should_request_repo_find_by_id() {
        var returnedEntity = new UserEntity();
        returnedEntity.setId(5L);
        returnedEntity.setName("Example");
        returnedEntity.setUsername("Example_Username");
        returnedEntity.setFamilyName("Example_FamilyName");
        returnedEntity.setRole(Role.SELLER);
        when(repo.findById(5L)).thenReturn(Optional.of(returnedEntity));

        Optional<User> user = service.findById(5L).block();

        assertTrue(user.isPresent());
        var rawUser = user.get();
        assertEquals(5L, user.get().getId().longValue());
        assertEquals("Example", rawUser.getName());
        assertEquals("Example_Username", rawUser.getUsername());
        assertEquals("Example_FamilyName", rawUser.getFamilyName());
        assertEquals(Role.SELLER, rawUser.getRole());
    }

    @Test
    public void when_find_by_username_should_request_repo_find_by_username() {
        var returnedEntity = new UserEntity();
        returnedEntity.setId(5L);
        returnedEntity.setName("Example");
        returnedEntity.setUsername("Example_Username");
        returnedEntity.setFamilyName("Example_FamilyName");
        returnedEntity.setRole(Role.SELLER);
        when(repo.findByUsername("Example")).thenReturn(Optional.of(returnedEntity));

        Optional<User> user = service.findByUsername("Example").block();

        assertTrue(user.isPresent());
        var rawUser = user.get();
        assertEquals(5L, user.get().getId().longValue());
        assertEquals("Example", rawUser.getName());
        assertEquals("Example_Username", rawUser.getUsername());
        assertEquals("Example_FamilyName", rawUser.getFamilyName());
        assertEquals(Role.SELLER, rawUser.getRole());
    }
}
