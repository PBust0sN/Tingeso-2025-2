# Guía de Implementación: Validación de JWT en Microservicios

## ✅ Cambios Implementados

He realizado las siguientes modificaciones para arreglar la integración con Keycloak:

### 1. **Frontend - main.jsx** ✅
- Agregado `initOptions` a ReactKeycloakProvider con configuración completa
- Agregado `LoadingComponent` para mostrar pantalla de carga
- Agregado `onTokens` callback para logs de actualizaciones de tokens
- **Archivo**: [src/main.jsx](../microservicio-frontend/src/main.jsx)

### 2. **Frontend - keycloak.js** ✅
- Mejorado con función `getKeycloakURL()` para soportar desarrollo y producción
- Agregado logging detallado
- Mejorado manejo de errores en `initKeycloak()`
- **Archivo**: [src/services/keycloak.js](../microservicio-frontend/src/services/keycloak.js)

### 3. **Frontend - http-common.js** ✅
- Mejorado interceptor de peticiones con mejor manejo de tokens
- Agregado interceptor de respuestas para manejar 401 y 403
- Agregado logging detallado de tokens
- Manejo de errores en refresh de tokens
- **Archivo**: [src/http-common.js](../microservicio-frontend/src/http-common.js)

### 4. **Frontend - App.jsx** ✅
- Agregada comprobación de `initialized` antes de renderizar
- Mejorada pantalla de autenticación requerida
- **Archivo**: [src/App.jsx](../microservicio-frontend/src/App.jsx)

### 5. **Frontend - LoadingScreen.jsx** ✅
- Componente nuevo para mostrar pantalla de carga
- **Archivo**: [src/components/LoadingScreen.jsx](../microservicio-frontend/src/components/LoadingScreen.jsx)

### 6. **Keycloak Realm Config - realm-export.json** ✅
- Actualizado `rootUrl` a `http://192.168.39.157:30080`
- Actualizado `redirectUris` con URLs correctas (sin wildcards mal usados)
- **Archivo**: [realm-export.json](../keycloak/realm-export.json)

### 7. **Gateway Service Config** ✅
- Agregada configuración de OAuth2 Resource Server
- Agregada referencia a Keycloak para validación de JWT
- Agregado logging para debugging
- **Archivo**: [config-data/gateway-service.yaml](../config-data/gateway-service.yaml)

---

## ⚙️ PASOS ADICIONALES REQUERIDOS

### Paso 1: Agregar dependencias al Gateway Service

En el archivo `gateway-service/pom.xml`, asegúrate de que tenga:

```xml
<!-- Spring Security OAuth2 Resource Server -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-oauth2-resource-server</artifactId>
</dependency>

<!-- Spring Security -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-security</artifactId>
</dependency>
```

### Paso 2: Crear SecurityConfig en Gateway Service

Crea un archivo `gateway-service/src/main/java/com/example/config/SecurityConfig.java`:

```java
package com.example.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import java.util.Arrays;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .authorizeRequests()
                .antMatchers("/actuator/**").permitAll()
                .anyRequest().authenticated()
            .and()
                .oauth2ResourceServer()
                .jwt();
        
        http.cors();
        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList(
            "http://localhost:5173",
            "http://localhost:3000",
            "http://localhost",
            "http://127.0.0.1",
            "http://192.168.39.157:30080",
            "http://192.168.49.2:32000"
        ));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("*"));
        configuration.setAllowCredentials(true);
        
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
```

### Paso 3: Agregar dependencias a los Microservicios

En cada microservicio (`ms-clients-service`, `ms-inventory-service`, etc.), agregar al `pom.xml`:

```xml
<!-- Spring Security OAuth2 Resource Server -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-oauth2-resource-server</artifactId>
</dependency>

<!-- Spring Security -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-security</artifactId>
</dependency>

<!-- Nimbus JOSE + JWT -->
<dependency>
    <groupId>com.nimbusds</groupId>
    <artifactId>nimbus-jose-jwt</artifactId>
    <version>9.37.3</version>
</dependency>
```

### Paso 4: Configurar OAuth2 en cada Microservicio

En `application.yml` de cada microservicio, agregar:

```yaml
spring:
  security:
    oauth2:
      resourceserver:
        jwt:
          issuer-uri: http://keycloak:8080/realms/toolRent
          jwk-set-uri: http://keycloak:8080/realms/toolRent/protocol/openid-connect/certs
```

### Paso 5: Crear SecurityConfig en cada Microservicio

En cada microservicio, crear `src/main/java/com/example/config/SecurityConfig.java`:

```java
package com.example.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .authorizeRequests()
                .antMatchers("/actuator/**").permitAll()
                .antMatchers("/swagger-ui.html", "/swagger-ui/**", "/v3/api-docs/**").permitAll()
                .anyRequest().authenticated()
            .and()
                .oauth2ResourceServer()
                .jwt()
                .jwtAuthenticationConverter(jwt -> {
                    // Puedes personalizar cómo se convierte el JWT a Authentication aquí
                    return null; // Spring usa el converter por defecto
                });
        
        return http.build();
    }
}
```

### Paso 6: Obtener información del usuario en Controllers

En tus controladores, puedes acceder al JWT así:

```java
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUser(Authentication authentication) {
        if (authentication == null) {
            return ResponseEntity.status(401).build();
        }
        
        Jwt jwt = (Jwt) authentication.getPrincipal();
        String username = jwt.getClaimAsString("preferred_username");
        List<String> roles = jwt.getClaimAsStringList("realm_access") != null 
            ? new ArrayList<>(((Map<String, Object>) jwt.getClaim("realm_access")).keySet())
            : new ArrayList<>();
        
        return ResponseEntity.ok(Map.of(
            "username", username,
            "roles", roles,
            "email", jwt.getClaimAsString("email")
        ));
    }
}
```

---

## 🧪 VERIFICACIÓN

### Verificar que todo funciona:

1. **Frontend muestra pantalla de autenticación**
   - Deberías ver el botón "Iniciar Sesión con Keycloak"
   - Se redirige a Keycloak

2. **Después de autenticarse**
   - El token debe aparecer en los logs del navegador
   - Las peticiones deben incluir el header `Authorization: Bearer <token>`

3. **En el backend**
   - Los logs deberían mostrar que el token se valida correctamente
   - Si el token es inválido, devuelve 401
   - Si el usuario no tiene permisos, devuelve 403

4. **Test de una petición**
   ```bash
   # Obtener un token válido desde Keycloak
   TOKEN=$(curl -X POST http://192.168.39.157:30443/realms/toolRent/protocol/openid-connect/token \
     -d "client_id=toolRent-Frontend" \
     -d "username=admin" \
     -d "password=admin" \
     -d "grant_type=password" | jq -r '.access_token')
   
   # Usar el token para hacer una petición
   curl -H "Authorization: Bearer $TOKEN" \
     http://192.168.39.157:30080/api/tools/
   ```

---

## 🔍 DEBUGGING

Si algo no funciona:

1. **Verificar que Keycloak está corriendo**
   ```bash
   kubectl get pods | grep keycloak
   kubectl logs <pod-name>
   ```

2. **Verificar que el Gateway puede conectar a Keycloak**
   ```bash
   kubectl exec <gateway-pod> -- curl http://keycloak:8080/realms/toolRent
   ```

3. **Revisar logs del Gateway**
   ```bash
   kubectl logs <gateway-pod> | grep -i oauth
   kubectl logs <gateway-pod> | grep -i security
   ```

4. **Verificar que el Frontend está enviando el token**
   - Abrir DevTools → Network
   - Hacer una petición a un endpoint
   - Verificar que el header `Authorization` está presente

---

## ⚠️ IMPORTANTE

- **Primero redeploy**: Necesitas hacer `npm run build` en el frontend y reuploaded la imagen Docker
- **Actualizar realm**: El archivo `realm-export.json` debe ser reimportado en Keycloak
- **Configurar Gateway**: El `gateway-service.yaml` debe ser actualizado en el ConfigMap de Kubernetes

