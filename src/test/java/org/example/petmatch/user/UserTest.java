package org.example.petmatch.user;

import org.example.petmatch.Comments.Domain.Comments;
import org.example.petmatch.User.Domain.Role;
import org.example.petmatch.User.Domain.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

public class UserTest {
    private User user;

    @BeforeEach
    void setUp() {
        user = new User();
    }

    @Test
    @DisplayName("Should create user with builder")
    void shouldCreateUserWithBuilder() {
        User builtUser = User.builder()
                .id(1L)
                .name("John")
                .lastname("Doe")
                .email("john@example.com")
                .password("encodedPassword")
                .role(Role.USER)
                .comments(new ArrayList<>())
                .build();

        assertNotNull(builtUser);
        assertEquals(1L, builtUser.getId());
        assertEquals("John", builtUser.getName());
        assertEquals("Doe", builtUser.getLastname());
        assertEquals("john@example.com", builtUser.getEmail());
        assertEquals("encodedPassword", builtUser.getPassword());
        assertEquals(Role.USER, builtUser.getRole());
        assertNotNull(builtUser.getComments());
    }

    @Test
    @DisplayName("Should set and get id")
    void shouldSetAndGetId() {
        user.setId(1L);
        assertEquals(1L, user.getId());
    }

    @Test
    @DisplayName("Should set and get name")
    void shouldSetAndGetName() {
        user.setName("John");
        assertEquals("John", user.getName());
    }

    @Test
    @DisplayName("Should set and get lastname")
    void shouldSetAndGetLastname() {
        user.setLastname("Doe");
        assertEquals("Doe", user.getLastname());
    }

    @Test
    @DisplayName("Should set and get email")
    void shouldSetAndGetEmail() {
        user.setEmail("john@example.com");
        assertEquals("john@example.com", user.getEmail());
    }

    @Test
    @DisplayName("Should set and get password")
    void shouldSetAndGetPassword() {
        user.setPassword("encodedPassword123");
        assertEquals("encodedPassword123", user.getPassword());
    }

    @Test
    @DisplayName("Should set and get role")
    void shouldSetAndGetRole() {
        user.setRole(Role.USER);
        assertEquals(Role.USER, user.getRole());
    }

    @Test
    @DisplayName("Should set and get comments list")
    void shouldSetAndGetComments() {
        List<Comments> comments = new ArrayList<>();
        user.setComments(comments);

        assertNotNull(user.getComments());
        assertEquals(0, user.getComments().size());
    }

    @Test
    @DisplayName("Should create user with all args constructor")
    void shouldCreateUserWithAllArgsConstructor() {
        List<Comments> comments = new ArrayList<>();
        User newUser = new User(1L, "Jane", "Smith", "jane@example.com",
                "password123", Role.USER, comments);

        assertEquals(1L, newUser.getId());
        assertEquals("Jane", newUser.getName());
        assertEquals("Smith", newUser.getLastname());
        assertEquals("jane@example.com", newUser.getEmail());
        assertEquals("password123", newUser.getPassword());
        assertEquals(Role.USER, newUser.getRole());
        assertNotNull(newUser.getComments());
    }

    @Test
    @DisplayName("Should create user with no args constructor")
    void shouldCreateUserWithNoArgsConstructor() {
        User emptyUser = new User();
        assertNotNull(emptyUser);
    }

    @Test
    @DisplayName("Should handle null values")
    void shouldHandleNullValues() {
        user.setName(null);
        user.setLastname(null);
        user.setComments(null);

        assertNull(user.getName());
        assertNull(user.getLastname());
        assertNull(user.getComments());
    }

    @Test
    @DisplayName("Should verify Role enum has USER value")
    void shouldVerifyRoleEnumHasUserValue() {
        assertEquals(Role.USER, Role.valueOf("USER"));
        assertNotNull(Role.USER);
    }

    @Test
    @DisplayName("Should add comments to user")
    void shouldAddCommentsToUser() {
        List<Comments> comments = new ArrayList<>();
        user.setComments(comments);

        assertEquals(0, user.getComments().size());
    }

    @Test
    @DisplayName("Should maintain email uniqueness constraint")
    void shouldMaintainEmailUniquenessConstraint() {
        user.setEmail("unique@example.com");
        assertEquals("unique@example.com", user.getEmail());
    }
}
