# Guía de Docker para Microservicios

Este proyecto contiene Dockerfiles para cada microservicio, optimizados para desarrollo.

## Servicios Disponibles

- **config-service**: Servidor de configuración (Puerto 8888)
- **eureka-service**: Servidor de descubrimiento Eureka (Puerto 8761)

## Construcción de Imágenes

### Construir todos los servicios

```bash
docker-compose build
```

### Construir un servicio específico

```bash
# Config Service
cd config-service
docker build -t config-service:dev .

# Eureka Service
cd eureka-service
docker build -t eureka-service:dev .
```

## Ejecución de Servicios

### Ejecutar todos los servicios con Docker Compose

```bash
docker-compose up
```

Para ejecutar en segundo plano:

```bash
docker-compose up -d
```

### Ejecutar servicios individuales

```bash
# Config Service
docker run -p 8888:8888 --name config-service config-service:dev

# Eureka Service (necesita conectarse al config-service)
docker run -p 8761:8761 --name eureka-service \
  -e SPRING_CONFIG_IMPORT=optional:configserver:http://config-service:8888 \
  --link config-service \
  eureka-service:dev
```

## Verificación de Servicios

Una vez que los servicios estén ejecutándose, puedes verificarlos en:

- **Config Service**: http://localhost:8888
- **Eureka Dashboard**: http://localhost:8761

## Comandos Útiles

### Ver logs de los servicios

```bash
# Todos los servicios
docker-compose logs -f

# Un servicio específico
docker-compose logs -f config-service
docker-compose logs -f eureka-service
```

### Detener servicios

```bash
docker-compose down
```

### Reconstruir y reiniciar servicios

```bash
docker-compose up --build
```

### Limpiar contenedores y volúmenes

```bash
docker-compose down -v
```

## Estructura de Archivos Docker

Cada servicio tiene:
- `Dockerfile`: Configuración multi-stage para construcción optimizada
- `.dockerignore`: Archivos excluidos del contexto de construcción

## Características de los Dockerfiles

- **Multi-stage build**: Separa la etapa de construcción de la ejecución
- **Imagen base ligera**: Usa `eclipse-temurin:17-jre-alpine` para runtime
- **Cache de dependencias**: Descarga dependencias antes de copiar el código fuente
- **Optimizado para desarrollo**: Construcción rápida y fácil debugging

## Troubleshooting

### Los servicios no se conectan entre sí

Asegúrate de usar `docker-compose` en lugar de `docker run` individual, ya que docker-compose crea automáticamente una red para que los servicios se comuniquen.

### Error de puerto en uso

Si recibes un error de puerto en uso, detén cualquier instancia local de los servicios:

```bash
# Windows
netstat -ano | findstr :8888
netstat -ano | findstr :8761

# Luego mata el proceso con:
taskkill /PID <PID> /F
```

### Reconstruir desde cero

Si necesitas reconstruir completamente sin usar cache:

```bash
docker-compose build --no-cache
```
