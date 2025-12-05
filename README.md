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

### 2. Null Safety con JSpecify

Spring Framework 7 adopta JSpecify como estándar para anotaciones de nullabilidad, reemplazando las múltiples anotaciones que existían en el ecosistema Java (@Nonnull, @Nullable, @NotNull, etc.).

**Beneficios:**
- Mejora las herramientas del IDE (detección de posibles NullPointerException)
- Mejora la interoperabilidad con Kotlin
- Reduce el riesgo de NullPointerException en codebases grandes
- Estándar unificado para null safety

**Ejemplo en Configuración:**
```java
@Configuration
public class ApiVersioningConfig implements WebMvcConfigurer {
    
    @Override
    public void configureApiVersioning(@NonNull ApiVersionConfigurer configurer) {
        configurer.useRequestHeader("X-API-Version");
    }
}
```

**Ejemplo en Servicios:**
```java
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

@Service
public class UserService {
    // @NonNull garantiza que nunca retorna null
    public @NonNull Map<Long, User> getAllUsers() {
        return new HashMap<>(users);
    }
    
    // @Nullable indica que puede retornar null
    public @Nullable User getUserById(@NonNull Long id) {
        return users.get(id);
    }
    
    // Parámetros @NonNull son requeridos, @Nullable son opcionales
    public @NonNull User createUser(@NonNull String name, @NonNull String email, @Nullable String phone) {
        return new User(name, email, phone);
    }
}
```

**Ejemplo en Modelos:**
```java
public class User {
    private @NonNull String name;  // Nunca puede ser null
    private @NonNull String email; // Nunca puede ser null
    private @Nullable String phone; // Puede ser null
    
    public @Nullable String getPhone() {
        return phone;
    }
    
    // Método helper para valores por defecto
    public @NonNull String getPhoneOrDefault(@NonNull String defaultValue) {
        return phone != null ? phone : defaultValue;
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

### 5. Anotaciones de Resiliencia (@Retryable y @ConcurrencyLimit)

Spring Framework 7 introduce anotaciones de resiliencia integradas que simplifican agregar patrones de resiliencia sin bibliotecas adicionales como Resilience4j.

**Características:**
- `@Retryable` - Reintentos automáticos en caso de fallos
- `@ConcurrencyLimit` - Límite de llamadas concurrentes
- `@EnableResilientMethods` - Habilita las anotaciones de resiliencia

**Configuración:**
```java
@Configuration
@EnableResilientMethods
public class HttpClientConfig {
    // Las anotaciones @Retryable y @ConcurrencyLimit ahora están activas
}
```

**Ejemplo en Cliente HTTP:**
```java
@HttpExchange
public interface QuoteClient {
    @GetExchange("/jokes/random")
    @Retryable
    @ConcurrencyLimit(3)
    ChuckNorrisJoke getRandomJoke();
}
```

**Ejemplo en Servicio:**
```java
@Service
public class ResilientQuoteService {
    
    @Retryable
    public ChuckNorrisJoke getRandomJokeWithRetry() {
        return quoteClient.getRandomJoke();
    }
    
    @ConcurrencyLimit(5)
    public String[] getCategoriesWithLimit() {
        return quoteClient.getCategories();
    }
    
    @Retryable
    @ConcurrencyLimit(3)
    public ChuckNorrisJoke getRandomJokeResilient() {
        return quoteClient.getRandomJoke();
    }
}
```

### 6. Múltiples TaskDecorator Beans

Spring Framework 7 permite definir múltiples `TaskDecorator` beans que se componen automáticamente en una cadena. Esto elimina la necesidad de crear decoradores compuestos manualmente.

**Configuración:**
```java
@Configuration
@EnableAsync
public class AsyncConfiguration {
}

@Configuration
public class TaskDecoratorConfiguration {
    
    @Bean
    @Order(2)
    TaskDecorator loggingTaskDecorator() {
        return runnable -> () -> {
            log.info("Running Task: {}", runnable);
            try {
                runnable.run();
            } finally {
                log.info("Finished Task: {}", runnable);
            }
        };
    }
    
    @Bean
    @Order(1)
    TaskDecorator measuringTaskDecorator() {
        return runnable -> () -> {
            final var startTime = System.currentTimeMillis();
            try {
                runnable.run();
            } finally {
                final var endTime = System.currentTimeMillis();
                log.info("Finished within {}ms", endTime - startTime);
            }
        };
    }
}
```

**Uso con eventos asíncronos:**
```java
@Component
public class HelloWorldEventLogger {
    
    @Async
    @EventListener
    public void logHelloWorldEvent(HelloWorldEvent event) {
        log.info("Hello World Event: {}", event.message());
    }
}
```

Los decoradores se aplican automáticamente en el orden especificado por `@Order`.

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
- `GET /quotes/random/json` - Chiste aleatorio de Chuck Norris (JSON completo)
- `GET /quotes/random/category?category={category}` - Chiste aleatorio por categoría
- `GET /quotes/categories` - Lista de categorías disponibles
- `GET /resilient-quotes/random` - Chiste aleatorio con reintentos automáticos
- `GET /resilient-quotes/categories` - Categorías con límite de concurrencia
- `GET /resilient-quotes/random-resilient` - Chiste aleatorio con ambas políticas de resiliencia
- `GET /events/hello?message={message}` - Publica un evento asíncrono (demuestra TaskDecorator)
- `GET /users` - Obtener todos los usuarios (demuestra JSpecify null safety)
- `GET /users/{id}` - Obtener usuario por ID
- `GET /users/search?email={email}` - Buscar usuario por email
- `POST /users` - Crear nuevo usuario
- `PUT /users/{id}` - Actualizar usuario
- `DELETE /users/{id}` - Eliminar usuario

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
