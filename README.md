# Spring Framework 7 Samples

Este proyecto demuestra las nuevas características de Spring Framework 7 y Spring Boot 4.

## Características Demostradas

### 1. Versionado de API REST Nativo

Spring Framework 7 introduce soporte nativo para el versionado de APIs REST directamente en las anotaciones de mapeo.

**Ejemplo:**
```java
@GetMapping(path = "/accounts/{id}", version = "1.0")
public ResponseEntity<Account> getAccountV1_0(@PathVariable Long id) {
    // Versión 1.0 - Información básica
}

@GetMapping(path = "/accounts/{id}", version = "1.1")
public ResponseEntity<Account> getAccountV1_1(@PathVariable Long id) {
    // Versión 1.1 - Información completa
}

@GetMapping(path = "/accounts/{id}", version = "2.0")
public ResponseEntity<AccountResponseV2> getAccountV2_0(@PathVariable Long id) {
    // Versión 2.0 - Formato mejorado
}
```

**Uso:**
```bash
# Versión 1.0
curl -H "Accept-Version: 1.0" http://localhost:8080/accounts/1

# Versión 1.1
curl -H "Accept-Version: 1.1" http://localhost:8080/accounts/1

# Versión 2.0
curl -H "Accept-Version: 2.0" http://localhost:8080/accounts/1
```

### 2. Anotaciones de Seguridad Nula con JSpecify

Spring Framework 7 adopta las anotaciones de JSpecify para mejorar la seguridad en tiempo de compilación y prevenir `NullPointerException`.

**Ejemplo:**
```java
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

public class AccountService {
    public @NonNull List<Account> getAllAccounts() {
        // Siempre retorna una lista no nula
    }
    
    public @Nullable Account getAccountById(@NonNull Long id) {
        // Puede retornar null si no existe
    }
}
```

### 3. Cliente REST Ligero para Pruebas (RestTestClient)

Spring Framework 7 introduce `RestTestClient`, un cliente ligero para probar endpoints REST sin dependencias reactivas.

**Ejemplo:**
```java
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class AccountControllerTest {
    RestTestClient client;
    
    @BeforeEach
    void setUp(WebApplicationContext context) {
        client = RestTestClient.bindToApplicationContext(context).build();
    }
    
    @Test
    void testGetAccount() {
        client.get()
                .uri("/accounts/1")
                .header("X-API-Version", "1.1")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.id").exists();
    }
}
```

### 4. Clientes HTTP Declarativos con @HttpExchange

Spring Framework 7 introduce soporte nativo para clientes HTTP declarativos usando `@HttpExchange` y `@ImportHttpServices`, similar a Feign pero más ligero e integrado.

Este ejemplo usa la API pública de Chuck Norris: [https://api.chucknorris.io/](https://api.chucknorris.io/)

**Definición del Cliente:**
```java
@HttpExchange
public interface QuoteClient {
    @GetExchange("/jokes/random")
    ChuckNorrisJoke getRandomJoke();
    
    @GetExchange("/jokes/random")
    ChuckNorrisJoke getRandomJokeByCategory(@RequestParam("category") String category);
    
    @GetExchange("/jokes/categories")
    String[] getCategories();
}
```

**Configuración:**
```java
@Configuration
@ImportHttpServices(group = "quoteService", types = {QuoteClient.class})
public class HttpClientConfig {
    
    @Bean
    RestClientHttpServiceGroupConfigurer quoteServiceGroupConfigurer() {
        return groups -> {
            groups.filterByName("quoteService")
                .forEachClient((group, clientBuilder) -> {
                    // API pública de Chuck Norris
                    clientBuilder.baseUrl("https://api.chucknorris.io");
                });
        };
    }
}
```

**Uso en Controladores:**
```java
@RestController
@RequestMapping(path = "/hello", version = "4")
public class HelloV4Controller {
    private final QuoteClient quoteClient;
    
    @GetMapping
    public String sayHello() {
        ChuckNorrisJoke joke = this.quoteClient.getRandomJoke();
        return joke != null ? joke.getValue() : "Hello, World!";
    }
}
```

## Requisitos

- Java 21 o superior
- Maven 3.6+
- Spring Boot 4.0.0
- Spring Framework 7

## Cómo Ejecutar

1. **Compilar el proyecto:**
   ```bash
   mvn clean install
   ```

2. **Ejecutar la aplicación:**
   ```bash
   mvn spring-boot:run
   ```

3. **Ejecutar los tests:**
   ```bash
   mvn test
   ```

## Endpoints Disponibles

- `GET /hello` - Endpoint de bienvenida
- `GET /hello` con header `X-API-Version: 4` - Chiste aleatorio de Chuck Norris
- `GET /accounts` - Obtener todas las cuentas
- `GET /accounts/{id}` - Obtener cuenta por ID (con versionado)
- `POST /accounts` - Crear nueva cuenta
- `PUT /accounts/{id}` - Actualizar cuenta
- `DELETE /accounts/{id}` - Eliminar cuenta
- `GET /quotes/random` - Chiste aleatorio de Chuck Norris (texto plano)
- `GET /quotes/random?format=json` - Chiste aleatorio de Chuck Norris (JSON completo)
- `GET /quotes/random?category={category}` - Chiste aleatorio por categoría
- `GET /quotes/categories` - Lista de categorías disponibles

## Estructura del Proyecto

```
src/
├── main/
│   ├── java/
│   │   └── com/josediaz/springframework7/
│   │       ├── SpringFramework7SamplesApplication.java
│   │       ├── controller/
│   │       │   ├── AccountController.java
│   │       │   └── HelloController.java
│   │       ├── model/
│   │       │   └── Account.java
│   │       └── service/
│   │           └── AccountService.java
│   └── resources/
│       └── application.properties
└── test/
    └── java/
        └── com/josediaz/springframework7/
            ├── AccountControllerTest.java
            ├── HelloControllerTest.java
            └── JSpecifyExampleTest.java
```

## Referencias

- [Spring Framework 7 Documentation](https://docs.spring.io/spring-framework/reference/)
- [Spring Boot 4 Documentation](https://docs.spring.io/spring-boot/docs/current/reference/html/)
- [JSpecify](https://jspecify.dev/)
