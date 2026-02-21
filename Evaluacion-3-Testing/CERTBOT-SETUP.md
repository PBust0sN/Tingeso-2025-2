# 🔐 Configuración de Certificados Let's Encrypt con Certbot

Este documento explica cómo usar **Certbot** para generar certificados Let's Encrypt reales en tu máquina local.

## ¿Por qué Certbot?

- ✅ Certificados reales de Let's Encrypt (no autofirmados)
- ✅ Válidos en navegadores web
- ✅ Renovación automática
- ✅ Mismos certificados que usas en producción en la nube

## Requisitos Previos

### Para Windows

Necesitas **WSL (Windows Subsystem for Linux)**:

1. **Verificar si tiene WSL instalado:**
   ```powershell
   wsl --list
   ```

2. **Si no está instalado, instálalo:**
   ```powershell
   # Abre PowerShell como Administrador
   wsl --install
   # Reinicia la computadora
   ```

3. **Después de instalar WSL, abre una terminal WSL:**
   ```bash
   wsl
   ```

### Para macOS/Linux

```bash
sudo apt-get install certbot  # Debian/Ubuntu
# o
brew install certbot          # macOS
```

## Pasos para Generar Certificados

### Opción 1: Script Automático (Recomendado)

#### En Windows (PowerShell como Administrador):
```powershell
cd "d:\NIVEL 6\Tecnicas de ingenieria de software\Material\Tingeso-2025-2\Evaluacion-3-Testing"
.\setup-certbot.bat
```

#### En macOS/Linux:
```bash
cd /ruta/a/tu/proyecto
chmod +x setup-certbot-wsl.sh
bash setup-certbot-wsl.sh
```

### Opción 2: Manual en WSL/Terminal

```bash
# 1. Instalar Certbot
sudo apt-get update
sudo apt-get install -y certbot python3-certbot-nginx

# 2. Generar certificados para ambos dominios
sudo certbot certonly --standalone \
    -d toolrent-tingeso.duckdns.org \
    -d auth.toolrent-tingeso.duckdns.org \
    --agree-tos \
    -m tu-email@example.com \
    --non-interactive \
    --preferred-challenges=http
```

## Después de Generar los Certificados

### ✅ Verificar que los certificados se crearon

```bash
# En WSL/Terminal
ls -lah /etc/letsencrypt/live/toolrent-tingeso.duckdns.org/

# Deberías ver:
# fullchain.pem  -> ../../archive/toolrent-tingeso.duckdns.org/fullchain1.pem
# privkey.pem    -> ../../archive/toolrent-tingeso.duckdns.org/privkey1.pem
```

### ✅ Levantar los contenedores

Los certificados en `/etc/letsencrypt/` ya están montados en `docker-compose.yml`:

```dockerfile
volumes:
  - /etc/letsencrypt:/etc/letsencrypt:ro
```

Ambos dominios comparten el mismo certificado (SAN - Subject Alternative Name):
- `toolrent-tingeso.duckdns.org` (frontend)
- `auth.toolrent-tingeso.duckdns.org` (Keycloak)

```bash
docker-compose up -d
```

Verifica que nginx esté corriendo:

```bash
docker logs nginx
```

## Renovación Automática de Certificados

Los certificados de Let's Encrypt son válidos por 90 días. Configura renovación automática:

### En WSL/Linux:

```bash
# Probar que funciona
sudo certbot renew --dry-run

# Configurar cron para renovación automática
sudo systemctl enable certbot.timer
sudo systemctl start certbot.timer

# Verificar estado
sudo systemctl status certbot.timer
```

### En Docker:

Actualiza el `docker-compose.yml` para incluir un servicio de renovación:

```yaml
  certbot:
    image: certbot/certbot
    volumes:
      - /etc/letsencrypt:/etc/letsencrypt
    command: renew --quiet
    schedule: "0 3 * * *"  # Diariamente a las 3 AM
```

## Solución de Problemas

### Error: "Address already in use" (Puerto 80 ocupado)

**Síntoma:**
```
Traceback (most recent call last):
  ...
OSError: [Errno 98] Address already in use
```

**Solución:**

1. Detén los contenedores:
   ```bash
   docker-compose down
   ```

2. Intenta de nuevo:
   ```bash
   sudo certbot certonly --standalone -d toolrent-tingeso.duckdns.org
   ```

3. Levanta los contenedores nuevamente:
   ```bash
   docker-compose up -d
   ```

### Error: "Domain validation failed"

**Síntoma:**
```
Failed to renew certificate toolrent-tingeso.duckdns.org
```

**Causas posibles:**
- El dominio no apunta a tu IP
- El puerto 80 no está accesible
- El firewall bloquea el puerto 80

**Solución:**

Usa validación DNS en lugar de HTTP:

```bash
sudo d auth.toolrent-tingeso.duckdns.org \
    -certbot certonly --manual \
    -d toolrent-tingeso.duckdns.org \
    --preferred-challenges=dns
```

### Error: "No module named 'certbot'"

**Solución:**

```bash
# En WSL/Terminal
sudo apt-get install python3-certbot
```

### Los certificados no se renuevan

**Solución:**

1. Verifica el cron:
   ```bash
   sudo systemctl status certbot.timer
   sudo journalctl -xe
   ```

2. Ejecuta manualmente:
   ```bash
   sudo certbot renew --verbose
   ```

3. Si usas Windows, configura una tarea programada para ejecutar:
   ```bash
   wsl sudo certbot renew --quiet
   ```

## Información de los Certificados

### Ver detalles del certificado:

```bash
# En WSL/Terminal
sudo certbot certificates

# O con openssl
openssl x509 -in /etc/letsencrypt/live/toolrent-tingeso.duckdns.org/fullchain.pem -text -noout
```

### Verificar fecha de expiración:

```bash
openssl x509 -in /etc/letsencrypt/live/toolrent-tingeso.duckdns.org/fullchain.pem -noout -dates
```

## Diferencia: Local vs Producción

| Aspecto | Local (Desarrollo) | Nube (Producción) |
|---------|------------------|------------------|
| **Certificados** | Let's Encrypt (via Certbot) | Let's Encrypt (via Certbot en servidor) |
| **Ubicación** | `/etc/letsencrypt/` en WSL | `/etc/letsencrypt/` en EC2 |
| **Renovación** | Manual o automática en WSL | Automática en servidor |
| **Validez** | Mismos certificados | Mismos certificados |

## Notas Importantes

⚠️ **IMPORTANTE**: 
- Los certificados Let's Encrypt caducan en 90 días
- Sin renovación automática, tu aplicación dejará de funcionar
- Certbot puede renovar automáticamente
- En Docker, es recomendable usar un contenedor dedicado a renovación

✅ **VENTAJA**: 
- Puedes probar toda la cadena de certificación localmente
- Los mismos certificados que usas en AWS/Azure
- Validan en navegadores (no hay warnings de certificados inválidos)

## Cambios en docker-compose.yml

La configuración ya está optimizada para Certbot:

```yaml
nginx:
  volumes:
    - /etc/letsencrypt:/etc/letsencrypt:ro  # ✅ Monta certificados de Certbot
    - ./monoliticoFrontend/nginx/nginx.conf:/etc/nginx/conf.d/default.conf:ro

keycloak:
  volumes:
    - /etc/letsencrypt:/etc/letsencrypt:ro
```

Esto funciona perfectamente tanto en local como en producción en la nube.
