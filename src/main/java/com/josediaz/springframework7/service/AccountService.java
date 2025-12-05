package com.josediaz.springframework7.service;

import com.josediaz.springframework7.model.Account;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AccountService {

    private final List<Account> accounts = new ArrayList<>();
    private Long nextId = 1L;

    public AccountService() {
        // Datos de ejemplo
        accounts.add(new Account(1L, "Juan Pérez", "juan@example.com", "+34 600 123 456"));
        accounts.add(new Account(2L, "María García", "maria@example.com", null));
        accounts.add(new Account(3L, "Carlos López", "carlos@example.com", "+34 600 789 012"));
    }

    public @NonNull List<Account> getAllAccounts() {
        return new ArrayList<>(accounts);
    }

    public @Nullable Account getAccountById(@NonNull Long id) {
        return accounts.stream()
                .filter(account -> account.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    public @NonNull Account createAccount(@NonNull String name, @NonNull String email, @Nullable String phone) {
        Account account = new Account(nextId++, name, email, phone);
        accounts.add(account);
        return account;
    }

    public @Nullable Account updateAccount(@NonNull Long id, @NonNull String name, @NonNull String email, @Nullable String phone) {
        Account account = getAccountById(id);
        if (account != null) {
            account.setName(name);
            account.setEmail(email);
            account.setPhone(phone);
        }
        return account;
    }

    public boolean deleteAccount(@NonNull Long id) {
        return accounts.removeIf(account -> account.getId().equals(id));
    }
}

