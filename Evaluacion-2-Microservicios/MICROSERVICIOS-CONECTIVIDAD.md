# Guía de Conectividad entre Microservicios - ESTRUCTURA FINAL

## Descripción General
Se ha configurado la comunicación entre microservicios utilizando **RestTemplate** con `getForObject()` (queryForObject) y arrays convertidos a List (queryForList) mediante llamadas HTTP REST.

## Estructura de Microservicios

| Microservicio | Puerto | URL Base | Responsabilidades |
|---|---|---|---|
| **ms-loans-service** | 8080 | http://localhost:8080 | Gestión de préstamos, coordinación de otros servicios |
| **ms-clients-service** | 8081 | http://localhost:8081 | Gestión de clientes |
| **ms-inventory-service** | 8082 | http://localhost:8082 | Gestión de herramientas y inventario |
| **ms-rates-service** | 8083 | http://localhost:8083 | Gestión de multas y tasas |
| **ms-kardex-service** | 8084 | http://localhost:8084 | Gestión de registros/kardex |
| **ms-reports-service** | 8085 | http://localhost:8085 | Generación de reportes |

## Estructura de Carpetas por Microservicio

Cada microservicio tiene la siguiente estructura para conectarse con otros:

```
src/main/java/com/example/[ms-nombre]/
├── config/
│   ├── RestTemplateConfig.java        # Bean de RestTemplate (en algunos servicios)
│   ├── SecurityConfig.java
│   └── WebConfig.java
├── Service/
│   ├── [ServicioOriginal]Service.java  # Servicios existentes del microservicio
│   ├── ClientService.java              # Cuando necesita consultar ms-clients-service
│   ├── LoansService.java               # Cuando necesita consultar ms-loans-service
│   ├── ToolsService.java               # Cuando necesita consultar ms-inventory-service
│   ├── FineService.java                # Cuando necesita consultar ms-rates-service
│   └── RecordsService.java             # Cuando necesita consultar ms-kardex-service
├── Models/                             # DTOs para datos externos
│   ├── ClientModel.java
│   ├── LoansModel.java
│   ├── ToolsModel.java
│   ├── FineModel.java
│   └── RecordsModel.java
└── Entities/                           # Solo entidades locales de BD
    └── [EntidadesLocales]Entity.java
```

## Servicios Inter-Microservicios Implementados

### 1. **ms-loans-service** (Puerto 8080)
**Servicios que consume:**
- **ClientService**: Obtiene datos de clientes
  - `getClientById(Long id)` - queryForObject
  - `getAllClients()` - queryForList
  - `hasExpiredLoansById(Long id)` - validación
  - `findAllLoansByClientId(Long clientId)` - queryForList
  - `HasTheSameToolInLoanByClientId(Long clientId, Long toolId)`
  - `updateClient(ClientModel client)` - PUT

- **ToolsService**: Obtiene datos de herramientas
  - `getToolById(Long id)` - queryForObject
  - `getAllTools()` - queryForList
  - `updateToolQuantity(Long toolId, Long quantity)`
  - `isToolAvailable(Long toolId)`

- **FineService**: Obtiene multas de clientes
  - `getFineById(Long id)` - queryForObject
  - `getAllFines()` - queryForList
  - `getAllFinesByClientId(Long clientId)` - queryForList
  - `saveFine(FineModel fine)` - POST
  - `updateFine(FineModel fine)` - PUT
  - `hasFinesByClientId(Long clientId)`

- **RecordsService**: Crea registros en kardex
  - `saveRecord(RecordsModel record)` - POST
  - `getRecordsById(Long id)` - queryForObject
  - `getAllRecords()` - queryForList
  - `updateRecord(RecordsModel record)` - PUT
  - `findByRecordDatesBetween(LocalDate start, LocalDate end)` - queryForList

- **ToolsLoansService**: Gestiona relación herramientas-préstamos
  - `getToolLoanById(Long id)` - queryForObject
  - `getToolLoansByLoanId(Long loanId)` - queryForList
  - `saveToolLoan(ToolsLoansModel toolLoan)` - POST

### 2. **ms-clients-service** (Puerto 8081)
**Servicios que consume:**
- **LoansService**: Consulta préstamos de clientes
  - `getLoanById(Long id)` - queryForObject
  - `getAllLoans()` - queryForList
  - `getLoansByClientId(Long clientId)` - queryForList
  - `saveLoan(LoansModel loan)` - POST

**Servicios propios:** ClientService, ClientLoansService, etc.

### 3. **ms-inventory-service** (Puerto 8082)
**Servicios que consume:**
- **LoansService**: Consulta préstamos
  - `getLoanById(Long id)` - queryForObject
  - `getAllLoans()` - queryForList
  - `saveLoan(LoansModel loan)` - POST

**Servicios propios:** ToolsService, ToolsLoansService, ImageService

### 4. **ms-rates-service** (Puerto 8083)
**Servicios que consume:**
- **ClientService**: Consulta y actualiza clientes
  - `getClientById(Long id)` - queryForObject
  - `getAllClients()` - queryForList
  - `updateClient(ClientModel client)` - PUT

**Servicios propios:** FineService (local)

### 5. **ms-reports-service** (Puerto 8085)
**Servicios que consume:**
- **ClientService**: Consulta clientes
  - `getClientById(Long id)` - queryForObject
  - `getAllClients()` - queryForList
  - `hasExpiredLoansById(Long id)`

- **LoansService**: Consulta préstamos
  - `getLoanById(Long id)` - queryForObject
  - `getAllLoans()` - queryForList
  - `getLoansByClientId(Long clientId)` - queryForList

- **ToolsService**: Consulta herramientas
  - `getToolById(Long id)` - queryForObject
  - `getAllTools()` - queryForList

- **FineService**: Consulta multas
  - `getFineById(Long id)` - queryForObject
  - `getAllFines()` - queryForList
  - `getFinesByClientId(Long clientId)` - queryForList

**Servicios propios:** LoansReportService, FineReportService, etc.

### 6. **ms-kardex-service** (Puerto 8084)
**Servicios que consume:**
- **ToolsService**: Consulta herramientas
  - `getToolById(Long id)` - queryForObject
  - `getAllTools()` - queryForList

**Servicios propios:** RecordsServices (local)

## Patrones de Implementación

### 1. RestTemplate Bean Configuration
```java
@Configuration
public class RestTemplateConfig {
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
```

Nota: En algunos servicios se añadió al WebConfig en lugar de crear un archivo separado.

### 2. Inyección y Consumo
```java
@Service
public class ClientService {
    
    @Autowired
    private RestTemplate restTemplate;
    
    private final String CLIENTS_SERVICE_URL = "http://localhost:8081";
    
    // queryForObject - obtener un cliente por ID
    public ClientModel getClientById(Long id) {
        String url = CLIENTS_SERVICE_URL + "/api/clients/" + id;
        return restTemplate.getForObject(url, ClientModel.class);
    }
    
    // queryForList - obtener múltiples objetos
    public List<ClientModel> getAllClients() {
        String url = CLIENTS_SERVICE_URL + "/api/clients/";
        ClientModel[] clients = restTemplate.getForObject(url, ClientModel[].class);
        return Arrays.asList(clients != null ? clients : new ClientModel[0]);
    }
    
    // POST - crear/actualizar
    public ClientModel updateClient(ClientModel clientModel) {
        String url = CLIENTS_SERVICE_URL + "/api/clients/";
        restTemplate.put(url, clientModel);
        return clientModel;
    }
}
```

## DTOs (Data Transfer Objects) - Models

Se creó una carpeta `Models` en cada microservicio que contiene DTOs simples para recibir datos de otros servicios:

```
Models/
├── ClientModel.java
├── LoansModel.java
├── ToolsModel.java
├── FineModel.java
└── RecordsModel.java
```

Estos Models NO son entidades JPA, solo POJO simples con `@Data`, `@NoArgsConstructor`, `@AllArgsConstructor` de Lombok.

## Comparación: queryForObject vs queryForList

| Patrón | Implementación | Caso de Uso | Ejemplo |
|--------|---|---|---|
| **queryForObject** | `restTemplate.getForObject(url, Model.class)` | Obtener un registro | `getClientById(1)` |
| **queryForList** | `Arrays.asList(restTemplate.getForObject(url, Model[].class))` | Obtener múltiples registros | `getAllClients()` |
| **POST/INSERT** | `restTemplate.postForObject(url, object, Model.class)` | Crear nuevo registro | `saveFine(fineModel)` |
| **PUT/UPDATE** | `restTemplate.put(url, object)` | Actualizar registro | `updateClient(clientModel)` |

## Notas Importantes

1. **URLs Hardcodeadas**: Las URLs están hardcodeadas (`http://localhost:8080`). Se recomienda migrar a propiedades configurables.

2. **Null Safety**: Los métodos de queryForList verifican null antes de retornar listas vacías.

3. **Sin Circuit Breaker**: No hay manejo de tolerancia a fallos. Se recomienda añadir Hystrix o Resilience4j.

4. **Sincronización**: Todas las llamadas son síncronas. Para operaciones intensivas considerar operaciones asincrónicas.

5. **Autenticación**: Los servicios tienen `@PreAuthorize` en controladores pero las llamadas inter-servicio no incluyen autenticación específica.

## Próximos Pasos

1. ✅ **Completado**: Conectividad básica con RestTemplate
2. **Pendiente**: Migrar URLs a application.yaml
3. **Pendiente**: Implementar Circuit Breaker
4. **Pendiente**: Configurar Feign Client como alternativa a RestTemplate
5. **Pendiente**: Implementar logging centralizado
6. **Pendiente**: Añadir métricas con Spring Boot Actuator
7. **Pendiente**: Configurar Eureka para service discovery dinámico

## Archivos Clave

Para cada microservicio que consume otros servicios, se tienen:
- `config/RestTemplateConfig.java` - Configuración del RestTemplate
- `Service/[Servicio]Service.java` - Métodos para consumir otros servicios
- `Models/[Modelo]Model.java` - DTOs para datos externos
- `Entities/[Entidad]Entity.java` - Entidades locales de base de datos (NO incluir modelos externos)

## Ejemplo Completo: Crear un Préstamo

```
1. Cliente llama a ms-loans-service POST /api/loans/new

2. ms-loans-service.LoansService.addLoan():
   a. ClientService.getClientById(clientId) 
      → GET http://localhost:8081/api/clients/{id}
   
   b. FineService.getAllFinesByClientId(clientId)
      → GET http://localhost:8083/api/fines/client/{id}
   
   c. ToolsService.getToolById(toolId)
      → GET http://localhost:8082/api/tools/{id}
   
   d. Guarda préstamo en BD local
   
   e. RecordsService.saveRecord(record)
      → POST http://localhost:8084/api/records/
   
   f. Retorna respuesta al cliente
```

---

**Última actualización:** 28 de Diciembre, 2025
**Estado:** ✅ Conectividad Básica Completada
