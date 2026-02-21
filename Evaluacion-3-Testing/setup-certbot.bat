@echo off
REM Script PowerShell para setup de Certbot en Windows
REM Usa WSL para ejecutar Certbot

setlocal enabledelayedexpansion

set DOMAIN1=toolrent-tingeso.duckdns.org
set DOMAIN2=auth.toolrent-tingeso.duckdns.org
set EMAIL=patricio0440bustos@gmail.com

echo.
echo ========================================
echo   Certbot Setup - Let's Encrypt
echo ========================================
echo.
echo [INFO] Dominios:
echo        - %DOMAIN1%
echo        - %DOMAIN2%
echo [INFO] Email: %EMAIL%
echo.

REM Verificar si WSL está instalado
wsl --list >nul 2>&1
if %ERRORLEVEL% NEQ 0 (
    echo [ERROR] WSL (Windows Subsystem for Linux) no está instalado
    echo.
    echo Para instalar WSL:
    echo   1. Abre PowerShell como Administrador
    echo   2. Ejecuta: wsl --install
    echo   3. Reinicia la computadora
    echo.
    pause
    exit /b 1
)

echo [OK] WSL detectado
echo.
echo [INFO] Dominio: %DOMAIN%
echo [INFO] Email: %EMAIL%
echo.

REM Ejecutar script bash en WSL
echo [INFO] Ejecutando Certbot en WSL...
echo [INFO] Esto puede tomar algunos minutos...
echo.

wsl bash -c "cd /mnt/d/NIVEL\ 6/Tecnicas\ de\ ingenieria\ de\ software/Material/Tingeso-2025-2/Evaluacion-3-Testing && bash setup-certbot-wsl.sh"

if %ERRORLEVEL% EQU 0 (
    echo.
    echo ========================================
    echo [OK] Certificados listos para usar
    echo ========================================
    echo.
    echo Próximo paso:
    echo   docker-compose up -d
    echo.
) else (
    echo.
    echo [ERROR] Fallo al generar certificados
    echo.
)

pause
