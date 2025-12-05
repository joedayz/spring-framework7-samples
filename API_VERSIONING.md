# Estrategias de Versionado de API en Spring Framework 7

Spring Framework 7 introduce soporte nativo para el versionado de APIs. Este documento explica las 4 estrategias disponibles y cómo usarlas.

## Estrategias Disponibles

### 1. Path-Based Versioning (Basado en Ruta)

**URLs:** `/api/v1/greeting`, `/api/v2/greeting`

**Configuración:**
```java
@Configuration
public class PathBasedApiVersioningConfig implements WebMvcConfigurer {
    @Override
    public void configureApiVersioning(ApiVersionConfigurer configurer) {
        configurer.usePathSegment(1);
    }

    @Override
    public void configurePathMatch(PathMatchConfigurer configurer) {
        configurer.addPathPrefix("/api/v{version}", 
            HandlerTypePredicate.forAnnotation(RestController.class));
    }
}
```

**Controlador:**
```java
@RestController
@RequestMapping(path = "/api/v{version}/greeting")
public class PathBasedVersionController {
    @GetMapping(version = "1", produces = MediaType.TEXT_PLAIN_VALUE)
    public String greetingV1() {
        return "Hello from API v1";
    }

    @GetMapping(version = "2", produces = MediaType.TEXT_PLAIN_VALUE)
    public String greetingV2() {
        return "Hello from API v2";
    }
}
```

**Uso:**
```bash
curl http://localhost:8080/api/v1/greeting
curl http://localhost:8080/api/v2/greeting
```

---

### 2. Query Parameter-Based Versioning (Basado en Parámetro de Consulta)

**URLs:** `/greeting?version=1`, `/greeting?version=2`

**Configuración:**
```java
@Configuration
public class ApiVersioningConfig implements WebMvcConfigurer {
    @Override
    public void configureApiVersioning(ApiVersionConfigurer configurer) {
        configurer.useQueryParameter("version");
    }
}
```

**Controlador:**
```java
@RestController
@RequestMapping("/greeting")
public class QueryParamVersionController {
    @GetMapping(version = "1", produces = MediaType.TEXT_PLAIN_VALUE)
    public String greetingV1() {
        return "Greeting v1 via query parameter";
    }

    @GetMapping(version = "2", produces = MediaType.TEXT_PLAIN_VALUE)
    public String greetingV2() {
        return "Greeting v2 via query parameter";
    }
}
```

**Uso:**
```bash
curl http://localhost:8080/greeting?version=1
curl http://localhost:8080/greeting?version=2
```

---

### 3. Request Header-Based Versioning (Basado en Header de Solicitud)

**Headers:** `X-API-Version: 1`, `X-API-Version: 2`

**Configuración:**
```java
@Configuration
public class HeaderBasedApiVersioningConfig implements WebMvcConfigurer {
    @Override
    public void configureApiVersioning(ApiVersionConfigurer configurer) {
        configurer.useRequestHeader("X-API-Version");
    }
}
```

**Controlador:**
```java
@RestController
@RequestMapping("/header-greeting")
public class HeaderVersionController {
    @GetMapping(version = "1", produces = MediaType.TEXT_PLAIN_VALUE)
    public String greetingV1() {
        return "Greeting v1 via header";
    }

    @GetMapping(version = "2", produces = MediaType.TEXT_PLAIN_VALUE)
    public String greetingV2() {
        return "Greeting v2 via header";
    }
}
```

**Uso:**
```bash
curl -H "X-API-Version: 1" http://localhost:8080/header-greeting
curl -H "X-API-Version: 2" http://localhost:8080/header-greeting
```

---

### 4. Media Type-Based Versioning (Basado en Tipo de Medio)

**Headers:** `Accept: application/json;version=1`, `Accept: application/json;version=2`

**Configuración:**
```java
@Configuration
public class MediaTypeApiVersioningConfig implements WebMvcConfigurer {
    @Override
    public void configureApiVersioning(ApiVersionConfigurer configurer) {
        // Usar el parámetro version en el header Accept
        configurer.useMediaTypeParameterVersioning();
    }
}
```

**Controlador:**
```java
@RestController
@RequestMapping("/mediatype-greeting")
public class MediaTypeVersionController {
    @GetMapping(version = "1", produces = MediaType.APPLICATION_JSON_VALUE)
    public GreetingResponse greetingV1() {
        return new GreetingResponse("Greeting v1 via media type", 1);
    }

    @GetMapping(version = "2", produces = MediaType.APPLICATION_JSON_VALUE)
    public GreetingResponse greetingV2() {
        return new GreetingResponse("Greeting v2 via media type", 2);
    }
}
```

**Uso:**
```bash
curl -H "Accept: application/json;version=1" http://localhost:8080/mediatype-greeting
curl -H "Accept: application/json;version=2" http://localhost:8080/mediatype-greeting
```

---

## Versionado a Nivel de Clase

También puedes especificar la versión a nivel de clase:

```java
@RestController
@RequestMapping(path = "/hello", version = "3")
public class HelloWorldV3Controller {
    @GetMapping(produces = MediaType.TEXT_PLAIN_VALUE)
    public String sayHello() {
        return "Hey World";
    }
}
```

## Cambiar entre Estrategias

Para cambiar entre estrategias, puedes:

1. **Usar Profiles:** Activa el profile correspondiente (ej: `spring.profiles.active=path-based`)
2. **Comentar/Descomentar:** Comenta la configuración actual y descomenta la que necesites
3. **Múltiples Configuraciones:** Usa `@Profile` para tener múltiples configuraciones disponibles

## Ejemplos en este Proyecto

Este proyecto incluye ejemplos de todas las estrategias:

- `PathBasedVersionController` - Path-based versioning
- `QueryParamVersionController` - Query parameter versioning  
- `HeaderVersionController` - Header-based versioning
- `MediaTypeVersionController` - Media type versioning

## Referencias

- [Spring Framework 7 API Versioning Documentation](https://docs.spring.io/spring-framework/reference/web/webmvc/mvc-controller/ann-requestmapping.html#api-version)
- [Spring Blog: API Versioning in Spring](https://spring.io/blog/2025/09/16/api-versioning-in-spring)

