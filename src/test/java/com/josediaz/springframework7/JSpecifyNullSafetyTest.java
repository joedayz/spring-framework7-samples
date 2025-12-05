package com.josediaz.springframework7;

import com.josediaz.springframework7.service.UserService;
import org.jspecify.annotations.NonNull;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Test para demostrar el uso de JSpecify para null safety
 * 
 * Estos tests verifican que las anotaciones @NonNull y @Nullable
 * funcionan correctamente y ayudan a prevenir NullPointerException
 */
@SpringBootTest
class JSpecifyNullSafetyTest {

    @Autowired
    private @NonNull UserService userService;

    @Test
    void testGetAllUsersReturnsNonNull() {
        // @NonNull garantiza que getAllUsers() nunca retorna null
        Map<Long, UserService.User> users = userService.getAllUsers();
        assertThat(users).isNotNull();
        assertThat(users.size()).isGreaterThan(0);
    }

    @Test
    void testGetUserByIdCanReturnNull() {
        // @Nullable indica que puede retornar null
        UserService.User user = userService.getUserById(999L);
        assertThat(user).isNull();

        // Usuario existente
        UserService.User existingUser = userService.getUserById(1L);
        assertThat(existingUser).isNotNull();
        assertThat(existingUser.getName()).isNotNull(); // @NonNull en getName()
    }

    @Test
    void testCreateUserWithNullablePhone() {
        // phone puede ser null según @Nullable
        UserService.User user1 = userService.createUser("Test", "test@example.com", null);
        assertThat(user1).isNotNull();
        assertThat(user1.getPhone()).isNull(); // @Nullable permite null

        // phone también puede tener un valor
        UserService.User user2 = userService.createUser("Test2", "test2@example.com", "123-456-7890");
        assertThat(user2).isNotNull();
        assertThat(user2.getPhone()).isNotNull();
    }

    @Test
    void testNonNullFields() {
        UserService.User user = userService.getUserById(1L);
        assertThat(user).isNotNull();

        // @NonNull garantiza que estos campos nunca son null
        assertThat(user.getName()).isNotNull();
        assertThat(user.getEmail()).isNotNull();
    }

    @Test
    void testNullablePhoneField() {
        UserService.User user = userService.getUserById(2L); // Bob no tiene teléfono
        assertThat(user).isNotNull();

        // @Nullable permite que phone sea null
        String phone = user.getPhone();
        // phone puede ser null, así que verificamos antes de usar
        if (phone != null) {
            assertThat(phone).isNotEmpty();
        } else {
            // Usar valor por defecto si es null
            String defaultPhone = user.getPhoneOrDefault("No phone");
            assertThat(defaultPhone).isEqualTo("No phone");
        }
    }

    @Test
    void testFindByEmailCanReturnNull() {
        // @Nullable indica que puede retornar null si no existe
        UserService.User user = userService.findByEmail("nonexistent@example.com");
        assertThat(user).isNull();

        // Verificar que podemos buscar usuarios existentes
        Map<Long, UserService.User> allUsers = userService.getAllUsers();
        if (!allUsers.isEmpty()) {
            UserService.User firstUser = allUsers.values().iterator().next();
            UserService.User foundUser = userService.findByEmail(firstUser.getEmail());
            assertThat(foundUser).isNotNull();
            assertThat(foundUser.getEmail()).isEqualTo(firstUser.getEmail());
        }
    }

    @Test
    void testUpdateUserReturnsNullable() {
        // @Nullable indica que puede retornar null si el usuario no existe
        UserService.User updated = userService.updateUser(999L, "New Name", "new@example.com", null);
        assertThat(updated).isNull();

        // Usuario existente
        UserService.User existing = userService.updateUser(1L, "Updated Name", "updated@example.com", "111-222-3333");
        assertThat(existing).isNotNull();
        assertThat(existing.getName()).isEqualTo("Updated Name");
    }
}
