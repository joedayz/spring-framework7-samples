package com.josediaz.springframework7.controller;

import com.josediaz.springframework7.model.Account;
import com.josediaz.springframework7.service.AccountService;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/accounts")
public class AccountController {

    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    // Nueva característica: Versionado de API nativo en Spring Framework 7
    // Usando el atributo 'version' directamente en las anotaciones
    
    // Versión 1.0 - Retorna solo información básica
    @GetMapping(path = "/{id}", version = "1.0")
    public ResponseEntity<Account> getAccountV1_0(@PathVariable Long id) {
        Account account = accountService.getAccountById(id);
        if (account != null) {
            // En v1.0, ocultamos el teléfono
            Account response = new Account(account.getId(), account.getName(), account.getEmail(), null);
            return ResponseEntity.ok(response);
        }
        return ResponseEntity.notFound().build();
    }

    // Versión 1.1 - Retorna información completa incluyendo teléfono
    @GetMapping(path = "/{id}", version = "1.1")
    public ResponseEntity<Account> getAccountV1_1(@PathVariable Long id) {
        Account account = accountService.getAccountById(id);
        if (account != null) {
            return ResponseEntity.ok(account);
        }
        return ResponseEntity.notFound().build();
    }

    // Versión 2.0 - Retorna información con formato mejorado
    @GetMapping(path = "/{id}", version = "2.0")
    public ResponseEntity<AccountResponseV2> getAccountV2_0(@PathVariable Long id) {
        Account account = accountService.getAccountById(id);
        if (account != null) {
            AccountResponseV2 response = new AccountResponseV2(
                    account.getId(),
                    account.getName(),
                    account.getEmail(),
                    account.getPhone(),
                    account.getCreatedAt()
            );
            return ResponseEntity.ok(response);
        }
        return ResponseEntity.notFound().build();
    }

    // Endpoint sin versión específica - funciona sin header cuando no hay configuración de versionado
    // Si el versionado está activo, puede requerir un header por defecto
    @GetMapping
    public ResponseEntity<List<Account>> getAllAccounts() {
        return ResponseEntity.ok(accountService.getAllAccounts());
    }

    @PostMapping
    public ResponseEntity<Account> createAccount(
            @RequestParam @NonNull String name,
            @RequestParam @NonNull String email,
            @RequestParam(required = false) @Nullable String phone) {
        Account account = accountService.createAccount(name, email, phone);
        return ResponseEntity.status(HttpStatus.CREATED).body(account);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Account> updateAccount(
            @PathVariable Long id,
            @RequestParam @NonNull String name,
            @RequestParam @NonNull String email,
            @RequestParam(required = false) @Nullable String phone) {
        Account account = accountService.updateAccount(id, name, email, phone);
        if (account != null) {
            return ResponseEntity.ok(account);
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAccount(@PathVariable Long id) {
        if (accountService.deleteAccount(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    // Clase de respuesta para la versión 2.0
    public static class AccountResponseV2 {
        private Long id;
        private String name;
        private String email;
        private String phone;
        private java.time.LocalDateTime createdAt;

        public AccountResponseV2(Long id, String name, String email, String phone, java.time.LocalDateTime createdAt) {
            this.id = id;
            this.name = name;
            this.email = email;
            this.phone = phone;
            this.createdAt = createdAt;
        }

        // Getters
        public Long getId() { return id; }
        public String getName() { return name; }
        public String getEmail() { return email; }
        public String getPhone() { return phone; }
        public java.time.LocalDateTime getCreatedAt() { return createdAt; }
    }
}

