# Notas sobre Versionado de API en Spring Framework 7

## Hallazgo Importante: Mezcla de Métodos con y sin Versión

Después de pruebas exhaustivas, se ha confirmado que **Spring Framework 7 NO permite mezclar métodos con atributo `version` y métodos sin `version` cuando comparten la misma ruta**, ya sea en el mismo controlador o en controladores diferentes.

### Comportamiento Observado

1. **Cuando el versionado está activo** (`ApiVersionConfigurer` configurado):
   - Los métodos **con** atributo `version` funcionan correctamente cuando se envía el header de versión
   - **NO puedes tener dos controladores con la misma ruta** donde uno tiene métodos con `version` y el otro no
   - Si intentas esto, Spring Framework 7 priorizará incorrectamente o causará conflictos de routing

2. **Recomendación basada en pruebas**:
   - **NO mezclar** métodos con `version` y sin `version` en el mismo controlador
   - **NO usar la misma ruta** en controladores diferentes si uno tiene versión y el otro no
   - **Solución**: Usar rutas diferentes para endpoints sin versión (ej: `/hello` vs `/hello-default`)
   - O bien, asigna una versión a todos los métodos que comparten la misma ruta

### Solución Implementada

En este proyecto:
- `HelloController`: Solo contiene métodos **con** versión (v1, v2) en la ruta `/hello`
- `HelloDefaultController`: Contiene el método **sin** versión en la ruta `/hello-default` (ruta diferente para evitar conflictos)
- `AccountController`: Todos los métodos de `/accounts/{id}` tienen versión; `/accounts` (sin versión) funciona porque tiene una ruta diferente

**IMPORTANTE**: La clave es usar **rutas diferentes** cuando mezclas endpoints con y sin versión.

### Configuración Actual

```java
@Configuration
public class ApiVersioningConfig implements WebMvcConfigurer {
    @Override
    public void configureApiVersioning(ApiVersionConfigurer configurer) {
        configurer.useRequestHeader("X-API-Version");
    }
}
```

### Tests

Los tests deben enviar el header `X-API-Version` incluso para endpoints sin versión cuando el versionado está activo, o el endpoint puede fallar con `400 BAD_REQUEST`.

### Referencias

- [Spring Framework 7 API Versioning Documentation](https://docs.spring.io/spring-framework/reference/7.0/web/webmvc/mvc-config/api-version.html)
- [Spring Blog: API Versioning in Spring](https://spring.io/blog/2025/09/16/api-versioning-in-spring)

### Conclusión

**Spring Framework 7 NO permite que endpoints con `version` y sin `version` compartan la misma ruta cuando el versionado está activo.**

**Soluciones recomendadas:**
1. ✅ **Usar rutas diferentes** para endpoints sin versión (ej: `/hello` vs `/hello-default`)
2. ✅ Asignar versiones a todos los métodos que comparten la misma ruta
3. ✅ Separar completamente en controladores diferentes con rutas diferentes
4. ❌ NO usar la misma ruta en controladores diferentes si uno tiene versión y el otro no

**Este comportamiento ha sido confirmado mediante pruebas exhaustivas.**

