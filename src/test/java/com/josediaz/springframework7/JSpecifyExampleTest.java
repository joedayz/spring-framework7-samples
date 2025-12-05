package com.josediaz.springframework7;

import com.josediaz.springframework7.model.Account;
import com.josediaz.springframework7.service.AccountService;
import org.jspecify.annotations.Nullable;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test para demostrar el uso de anotaciones JSpecify
 * Estas anotaciones mejoran la seguridad de tipos y ayudan a prevenir NullPointerException
 */
@SpringBootTest
class JSpecifyExampleTest {

    @Autowired
    private AccountService accountService;

    @Test
    void testNonNullReturn() {
        // El método getAllAccounts() retorna @NonNull List<Account>
        java.util.List<Account> accounts = accountService.getAllAccounts();
        assertNotNull(accounts);
        assertFalse(accounts.isEmpty());
    }

    @Test
    void testNullableReturn() {
        // El método getAccountById() puede retornar null
        @Nullable Account account = accountService.getAccountById(999L);
        assertNull(account); // No existe, debe ser null

        // Pero cuando existe, no es null
        @Nullable Account existingAccount = accountService.getAccountById(1L);
        assertNotNull(existingAccount);
    }

    @Test
    void testNullableField() {
        Account account = accountService.getAccountById(2L); // Este account tiene phone = null
        assertNotNull(account);
        @Nullable String phone = account.getPhone();
        // phone puede ser null, lo cual es válido según la anotación @Nullable
        assertNull(phone);
    }

    @Test
    void testCreateAccountWithNullablePhone() {
        // Podemos crear una cuenta sin teléfono (phone es @Nullable)
        Account account = accountService.createAccount(
                "Test User",
                "test@example.com",
                null // phone puede ser null
        );
        assertNotNull(account);
        assertNull(account.getPhone());
    }
}

