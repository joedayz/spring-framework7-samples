# Spring Framework 7 - Guía Paso a Paso para Webinar

Este proyecto demuestra las nuevas características de **Spring Framework 7** y **Spring Boot 4** de manera práctica y fácil de seguir.

## 📋 Tabla de Contenidos

1. [Requisitos Previos](#requisitos-previos)
2. [Configuración Inicial](#configuración-inicial)
3. [Característica 1: Versionado de API REST Nativo](#característica-1-versionado-de-api-rest-nativo)
4. [Característica 2: Null Safety con JSpecify](#característica-2-null-safety-con-jspecify)
5. [Característica 3: RestTestClient para Pruebas](#característica-3-resttestclient-para-pruebas)
6. [Característica 4: Clientes HTTP Declarativos (@HttpExchange)](#característica-4-clientes-http-declarativos-httpexchange)
7. [Característica 5: Anotaciones de Resiliencia](#característica-5-anotaciones-de-resiliencia)
8. [Característica 6: Múltiples TaskDecorator Beans](#característica-6-múltiples-taskdecorator-beans)
9. [Ejecutar y Probar](#ejecutar-y-probar)

---

## Requisitos Previos

Antes de comenzar, asegúrate de tener instalado:

- **Java 21** o superior
- **Maven 3.6+**
- **IDE** (IntelliJ IDEA, Eclipse, VS Code, etc.)
- **Git** (opcional)

### Verificar Instalación

```bash
java -version  # Debe mostrar Java 21 o superior
mvn -version   # Debe mostrar Maven 3.6+
```

---

## Configuración Inicial

### Paso 1: Clonar o Descargar el Proyecto

```bash
git clone <repository-url>
cd spring-framework7-samples
```

### Paso 2: Compilar el Proyecto

```bash
mvn clean compile
```

**✅ Verificación:** Deberías ver `BUILD SUCCESS`

### Paso 3: Ejecutar los Tests

```bash
mvn test
```

**✅ Verificación:** Todos los tests deben pasar (34 tests, 0 fallos)

---

## Característica 1: Versionado de API REST Nativo

Spring Framework 7 introduce soporte nativo para el versionado de APIs directamente en las anotaciones de mapeo.

### 📝 Paso 1.1: Crear el Controlador con Versionado

Abre el archivo: `src/main/java/com/josediaz/springframework7/controller/AccountController.java`

**Observa:**
- Los métodos tienen el atributo `version` en `@GetMapping`
- Cada versión puede retornar diferentes estructuras de datos

```java
@GetMapping(path = "/{id}", version = "1.0")
public ResponseEntity<Account> getAccountV1_0(@PathVariable Long id) {
    // Versión 1.0 - Información básica (sin teléfono)
}

@GetMapping(path = "/{id}", version = "1.1")
public ResponseEntity<Account> getAccountV1_1(@PathVariable Long id) {
    // Versión 1.1 - Información completa (con teléfono)
}

@GetMapping(path = "/{id}", version = "2.0")
public ResponseEntity<AccountResponseV2> getAccountV2_0(@PathVariable Long id) {
    // Versión 2.0 - Formato mejorado (con fecha de creación)
}
```

### 📝 Paso 1.2: Configurar la Estrategia de Versionado

Abre el archivo: `src/main/java/com/josediaz/springframework7/config/ApiVersioningConfig.java`

**Observa:**
- Usa `@NonNull` de JSpecify (veremos esto en la siguiente característica)
- Configura el header `X-API-Version` para determinar la versión

```java
@Configuration
public class ApiVersioningConfig implements WebMvcConfigurer {
    @Override
    public void configureApiVersioning(@NonNull ApiVersionConfigurer configurer) {
        configurer.useRequestHeader("X-API-Version");
    }
}
```

### 🧪 Paso 1.3: Probar el Versionado

**Opción A: Usar curl**

```bash
# Versión 1.0 - Sin teléfono
curl -H "X-API-Version: 1.0" http://localhost:8080/accounts/1

# Versión 1.1 - Con teléfono
curl -H "X-API-Version: 1.1" http://localhost:8080/accounts/1

# Versión 2.0 - Formato mejorado
curl -H "X-API-Version: 2.0" http://localhost:8080/accounts/1
```

**Opción B: Ejecutar el Test**

```bash
mvn test -Dtest=AccountControllerTest
```

### 📚 Estrategias de Versionado Disponibles

Spring Framework 7 soporta 4 estrategias:

1. **Request Header** (la que estamos usando):
   ```java
   configurer.useRequestHeader("X-API-Version");
   ```

2. **Query Parameter**:
   ```java
   configurer.useQueryParameter("version");
   ```
   Uso: `GET /accounts/1?version=1.0`

3. **Path Segment**:
   ```java
   configurer.usePathSegment(1);
   ```
   Uso: `GET /api/v1/accounts/1`

4. **Media Type Parameter**:
   ```java
   configurer.useMediaTypeParameterVersioning();
   ```
   Uso: `Accept: application/json;version=1.0`

---

## Característica 2: Null Safety con JSpecify

Spring Framework 7 adopta **JSpecify** como estándar para anotaciones de nullabilidad, mejorando la seguridad de tipos y la interoperabilidad con Kotlin.

### 📝 Paso 2.1: Ver el Ejemplo en ApiVersioningConfig

Ya viste esto en el paso anterior:

```java
@Override
public void configureApiVersioning(@NonNull ApiVersionConfigurer configurer) {
    // @NonNull garantiza que configurer nunca será null
    configurer.useRequestHeader("X-API-Version");
}
```

### 📝 Paso 2.2: Ver el Ejemplo en UserService

Abre: `src/main/java/com/josediaz/springframework7/service/UserService.java`

**Observa:**
- `@NonNull` en valores de retorno que nunca son null
- `@Nullable` en valores que pueden ser null
- `@NonNull` en parámetros requeridos

```java
// Siempre retorna una lista no nula
public @NonNull Map<Long, User> getAllUsers() {
    return new HashMap<>(users);
}

// Puede retornar null si no existe
public @Nullable User getUserById(@NonNull Long id) {
    return users.get(id);
}

// Parámetros @NonNull son requeridos, @Nullable son opcionales
public @NonNull User createUser(
    @NonNull String name,      // Requerido
    @NonNull String email,      // Requerido
    @Nullable String phone) {  // Opcional
    // ...
}
```

### 📝 Paso 2.3: Ver el Ejemplo en el Modelo

Abre: `src/main/java/com/josediaz/springframework7/service/UserService.java` (clase interna `User`)

**Observa:**
- Campos anotados con `@NonNull` y `@Nullable`
- Método helper para valores por defecto

```java
public static class User {
    private @NonNull String name;   // Nunca null
    private @NonNull String email;  // Nunca null
    private @Nullable String phone; // Puede ser null
    
    public @NonNull String getPhoneOrDefault(@NonNull String defaultValue) {
        return phone != null ? phone : defaultValue;
    }
}
```

### 🧪 Paso 2.4: Probar Null Safety

```bash
mvn test -Dtest=JSpecifyNullSafetyTest
```

### 💡 Beneficios de JSpecify

- ✅ **Estándar unificado** - Reemplaza múltiples anotaciones (@Nonnull, @Nullable, @NotNull)
- ✅ **Mejor IDE** - Detecta posibles NullPointerException
- ✅ **Kotlin interop** - Mejora la interoperabilidad
- ✅ **Seguridad de tipos** - Ayuda a prevenir errores en tiempo de compilación

---

## Característica 3: RestTestClient para Pruebas

Spring Framework 7 introduce `RestTestClient`, un cliente ligero para probar endpoints REST sin dependencias reactivas.

### 📝 Paso 3.1: Ver el Test de Ejemplo

Abre: `src/test/java/com/josediaz/springframework7/AccountControllerTest.java`

**Observa:**
- Usa `RestTestClient` en lugar de `WebTestClient` o `MockMvc`
- API fluida y fácil de usar
- Se inicializa con `RestTestClient.bindToApplicationContext(context).build()`

```java
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class AccountControllerTest {
    RestTestClient client;

    @BeforeEach
    void setUp(WebApplicationContext context) {
        client = RestTestClient.bindToApplicationContext(context).build();
    }

    @Test
    void testGetAccountV1_0() {
        client.get()
                .uri("/accounts/1")
                .header("X-API-Version", "1.0")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.id").isEqualTo(1)
                .jsonPath("$.phone").doesNotExist(); // v1.0 no incluye teléfono
    }
}
```

### 🧪 Paso 3.2: Ejecutar los Tests con RestTestClient

```bash
mvn test -Dtest=AccountControllerTest
```

### 💡 Ventajas de RestTestClient

- ✅ **Sin dependencias reactivas** - No necesitas WebFlux
- ✅ **API fluida** - Similar a RestClient pero para tests
- ✅ **Ligero** - Más simple que MockMvc para casos básicos

---

## Característica 4: Clientes HTTP Declarativos (@HttpExchange)

Spring Framework 7 introduce soporte nativo para clientes HTTP declarativos usando `@HttpExchange`, similar a Feign pero más ligero e integrado.

### 📝 Paso 4.1: Crear el Cliente HTTP Declarativo

Abre: `src/main/java/com/josediaz/springframework7/client/QuoteClient.java`

**Observa:**
- Interfaz anotada con `@HttpExchange`
- Métodos anotados con `@GetExchange`
- Similar a definir un controlador, pero para consumir APIs externas

```java
@HttpExchange
public interface QuoteClient {
    @GetExchange("/jokes/random")
    @Retryable
    @ConcurrencyLimit(3)
    ChuckNorrisJoke getRandomJoke();
    
    @GetExchange("/jokes/random")
    ChuckNorrisJoke getRandomJokeByCategory(@RequestParam("category") String category);
    
    @GetExchange("/jokes/categories")
    String[] getCategories();
}
```

### 📝 Paso 4.2: Configurar el Cliente HTTP

Abre: `src/main/java/com/josediaz/springframework7/config/HttpClientConfig.java`

**Observa:**
- Crea un bean del cliente usando `RestClient` y `HttpServiceProxyFactory`
- Usa `RestClientAdapter` para adaptar `RestClient` a `HttpServiceProxyFactory`
- Configura la URL base de la API externa

```java
@Configuration
@EnableResilientMethods
public class HttpClientConfig {
    @Bean
    public QuoteClient quoteClient(@Value("${chucknorris.api.base-url:https://api.chucknorris.io}") String baseUrl) {
        RestClient restClient = RestClient.builder()
                .baseUrl(baseUrl)
                .build();
        
        HttpServiceProxyFactory factory = HttpServiceProxyFactory.builder()
                .exchangeAdapter(RestClientAdapter.create(restClient))
                .build();
        
        return factory.createClient(QuoteClient.class);
    }
}
```

### 📝 Paso 4.3: Usar el Cliente en un Controlador

Abre: `src/main/java/com/josediaz/springframework7/controller/QuoteController.java`

**Observa:**
- El cliente se inyecta como cualquier otro bean de Spring
- Se usa de forma natural, como si fuera un servicio local

```java
@RestController
@RequestMapping("/quotes")
public class QuoteController {
    private final QuoteClient quoteClient;

    public QuoteController(QuoteClient quoteClient) {
        this.quoteClient = quoteClient;
    }

    @GetMapping("/random")
    public String getRandomQuote() {
        QuoteClient.ChuckNorrisJoke joke = quoteClient.getRandomJoke();
        return joke != null ? joke.getValue() : "No joke available";
    }
}
```

### 🧪 Paso 4.4: Probar el Cliente HTTP

```bash
# Ejecutar la aplicación
mvn spring-boot:run

# En otra terminal, probar el endpoint
curl http://localhost:8080/quotes/random
curl http://localhost:8080/quotes/categories
```

O ejecutar los tests:

```bash
mvn test -Dtest=QuoteControllerTest
```

### 💡 Ventajas de @HttpExchange

- ✅ **Sin dependencias externas** - No necesitas Feign
- ✅ **Integrado con Spring** - Funciona perfectamente con el ecosistema Spring
- ✅ **Ligero** - Menos overhead que Feign
- ✅ **Type-safe** - Compilación fuerte de tipos

---

## Característica 5: Anotaciones de Resiliencia

Spring Framework 7 introduce anotaciones de resiliencia integradas (`@Retryable` y `@ConcurrencyLimit`) que simplifican agregar patrones de resiliencia sin bibliotecas adicionales.

### 📝 Paso 5.1: Habilitar las Anotaciones de Resiliencia

Ya viste esto en `HttpClientConfig.java`:

```java
@Configuration
@EnableResilientMethods  // ← Habilita @Retryable y @ConcurrencyLimit
public class HttpClientConfig {
    // ...
}
```

### 📝 Paso 5.2: Ver el Ejemplo en QuoteClient

Ya viste esto en `QuoteClient.java`:

```java
@GetExchange("/jokes/random")
@Retryable                    // ← Reintenta automáticamente si falla
@ConcurrencyLimit(3)          // ← Limita a 3 llamadas concurrentes
ChuckNorrisJoke getRandomJoke();
```

### 📝 Paso 5.3: Ver el Ejemplo en ResilientQuoteService

Abre: `src/main/java/com/josediaz/springframework7/service/ResilientQuoteService.java`

**Observa:**
- `@Retryable` para reintentos automáticos
- `@ConcurrencyLimit` para limitar llamadas concurrentes
- Puedes combinar ambas anotaciones

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

### 🧪 Paso 5.4: Probar las Anotaciones de Resiliencia

```bash
mvn test -Dtest=ResilientQuoteServiceTest
```

### 💡 Beneficios

- ✅ **Sin bibliotecas adicionales** - No necesitas Resilience4j
- ✅ **Integrado** - Funciona perfectamente con Spring
- ✅ **Fácil de usar** - Solo agrega anotaciones
- ✅ **Verificable** - Puedes verificar las políticas en runtime

---

## Característica 6: Múltiples TaskDecorator Beans

Spring Framework 7 permite definir múltiples `TaskDecorator` beans que se componen automáticamente en una cadena, eliminando la necesidad de crear decoradores compuestos manualmente.

### 📝 Paso 6.1: Habilitar Procesamiento Asíncrono

Abre: `src/main/java/com/josediaz/springframework7/config/AsyncConfiguration.java`

```java
@Configuration
@EnableAsync  // ← Habilita @Async
public class AsyncConfiguration {
}
```

### 📝 Paso 6.2: Crear Múltiples TaskDecorator

Abre: `src/main/java/com/josediaz/springframework7/config/TaskDecoratorConfiguration.java`

**Observa:**
- Dos `TaskDecorator` beans diferentes
- `@Order` determina el orden de aplicación
- Spring los compone automáticamente

```java
@Configuration
public class TaskDecoratorConfiguration {
    
    @Bean
    @Order(2)  // ← Se aplica después (más interno)
    public TaskDecorator loggingTaskDecorator() {
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
    @Order(1)  // ← Se aplica primero (más externo)
    public TaskDecorator measuringTaskDecorator() {
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

### 📝 Paso 6.3: Crear un Listener Asíncrono

Abre: `src/main/java/com/josediaz/springframework7/listener/HelloWorldEventLogger.java`

**Observa:**
- `@Async` para procesamiento asíncrono
- `@EventListener` para escuchar eventos
- Los `TaskDecorator` se aplican automáticamente

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

### 📝 Paso 6.4: Publicar un Evento

Abre: `src/main/java/com/josediaz/springframework7/controller/EventController.java`

```java
@RestController
@RequestMapping("/events")
public class EventController {
    private final ApplicationEventPublisher eventPublisher;

    @GetMapping("/hello")
    public String publishHelloWorldEvent(@RequestParam String message) {
        HelloWorldEvent event = new HelloWorldEvent(message);
        eventPublisher.publishEvent(event);
        return "Event published: " + message;
    }
}
```

### 🧪 Paso 6.5: Probar los TaskDecorator

```bash
# Ejecutar la aplicación
mvn spring-boot:run

# En otra terminal, publicar un evento
curl "http://localhost:8080/events/hello?message=Hello%20Webinar"

# Observa los logs - deberías ver:
# 1. "Running Task: ..." (de loggingTaskDecorator)
# 2. "Hello World Event: Hello Webinar" (de la tarea real)
# 3. "Finished within Xms" (de measuringTaskDecorator)
# 4. "Finished Task: ..." (de loggingTaskDecorator)
```

O ejecutar los tests:

```bash
mvn test -Dtest=TaskDecoratorTest
```

### 💡 Orden de Aplicación

Los decoradores se aplican en este orden (de más externo a más interno):

1. `measuringTaskDecorator` (@Order(1)) - Mide tiempo
2. `loggingTaskDecorator` (@Order(2)) - Registra inicio/fin
3. Tarea real - Ejecuta la lógica

### 💡 Beneficios

- ✅ **Sin código boilerplate** - No necesitas crear decoradores compuestos manualmente
- ✅ **Orden configurable** - Usa `@Order` para controlar el orden
- ✅ **Composición automática** - Spring los combina automáticamente
- ✅ **Separación de concerns** - Cada decorador tiene una responsabilidad

---

## Ejecutar y Probar

### Compilar el Proyecto

```bash
mvn clean compile
```

### Ejecutar Todos los Tests

```bash
mvn test
```

**Resultado esperado:** 34 tests, 0 fallos, 0 errores

### Ejecutar la Aplicación

```bash
mvn spring-boot:run
```

La aplicación estará disponible en: `http://localhost:8080`

### Probar los Endpoints

```bash
# Versionado de API
curl -H "X-API-Version: 1.0" http://localhost:8080/accounts/1
curl -H "X-API-Version: 2.0" http://localhost:8080/accounts/1

# Cliente HTTP declarativo
curl http://localhost:8080/quotes/random
curl http://localhost:8080/quotes/categories

# Resiliencia
curl http://localhost:8080/resilient-quotes/random

# TaskDecorator
curl "http://localhost:8080/events/hello?message=Test"
```

---

## Estructura del Proyecto

```
spring-framework7-samples/
├── pom.xml
├── README.md
├── src/
│   ├── main/
│   │   ├── java/com/josediaz/springframework7/
│   │   │   ├── SpringFramework7SamplesApplication.java
│   │   │   ├── client/
│   │   │   │   └── QuoteClient.java              # Cliente HTTP declarativo
│   │   │   ├── config/
│   │   │   │   ├── ApiVersioningConfig.java      # Configuración de versionado
│   │   │   │   ├── AsyncConfiguration.java       # Configuración asíncrona
│   │   │   │   ├── HttpClientConfig.java         # Configuración cliente HTTP
│   │   │   │   └── TaskDecoratorConfiguration.java # TaskDecorators
│   │   │   ├── controller/
│   │   │   │   ├── AccountController.java        # Versionado de API
│   │   │   │   ├── QuoteController.java          # Cliente HTTP
│   │   │   │   ├── ResilientQuoteController.java # Resiliencia
│   │   │   │   ├── EventController.java          # TaskDecorator
│   │   │   │   └── UserController.java           # JSpecify null safety
│   │   │   ├── event/
│   │   │   │   └── HelloWorldEvent.java          # Evento para TaskDecorator
│   │   │   ├── listener/
│   │   │   │   └── HelloWorldEventLogger.java   # Listener asíncrono
│   │   │   ├── model/
│   │   │   │   └── Account.java                  # Modelo con JSpecify
│   │   │   └── service/
│   │   │       ├── AccountService.java           # Servicio con JSpecify
│   │   │       ├── UserService.java              # Servicio con JSpecify
│   │   │       └── ResilientQuoteService.java    # Servicio con resiliencia
│   │   └── resources/
│   │       └── application.properties
│   └── test/
│       └── java/com/josediaz/springframework7/
│           ├── AccountControllerTest.java         # Tests con RestTestClient
│           ├── QuoteControllerTest.java         # Tests cliente HTTP
│           ├── ResilientQuoteServiceTest.java   # Tests resiliencia
│           ├── TaskDecoratorTest.java           # Tests TaskDecorator
│           └── JSpecifyNullSafetyTest.java       # Tests null safety
```

---

## Resumen de Características

| Característica | Archivo Principal | Test |
|---------------|-------------------|------|
| **Versionado de API** | `AccountController.java` | `AccountControllerTest.java` |
| **Null Safety (JSpecify)** | `UserService.java` | `JSpecifyNullSafetyTest.java` |
| **RestTestClient** | `AccountControllerTest.java` | `AccountControllerTest.java` |
| **Clientes HTTP Declarativos** | `QuoteClient.java` | `QuoteControllerTest.java` |
| **Resiliencia** | `ResilientQuoteService.java` | `ResilientQuoteServiceTest.java` |
| **TaskDecorator** | `TaskDecoratorConfiguration.java` | `TaskDecoratorTest.java` |

---

## Referencias

- [Spring Framework 7 Documentation](https://docs.spring.io/spring-framework/reference/)
- [Spring Boot 4 Documentation](https://docs.spring.io/spring-boot/docs/current/reference/html/)
- [JSpecify](https://jspecify.dev/)
- [Chuck Norris API](https://api.chucknorris.io/)

---

## Próximos Pasos

1. ✅ Explora cada característica paso a paso
2. ✅ Ejecuta los tests para ver cómo funcionan
3. ✅ Modifica los ejemplos para experimentar
4. ✅ Crea tus propios ejemplos basados en estos patrones

---

## Soporte

Si tienes preguntas durante el webinar:

1. Revisa los comentarios en el código
2. Ejecuta los tests para ver ejemplos funcionales
3. Consulta la documentación oficial de Spring Framework 7

¡Disfruta explorando Spring Framework 7! 🚀
