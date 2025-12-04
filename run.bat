@echo off

for /f "tokens=2 delims=: " %%i in ('reg query HKCU\Console /v VirtualTerminalLevel 2^>nul') do set "vt=%%i"
if not defined vt reg add HKCU\Console /f /v VirtualTerminalLevel /t REG_DWORD /d 1 >nul

echo Ejecutando yacc...
yacc -J -v grama.y
if errorlevel 1 (
    echo Error en yacc
    exit /b 1
)

echo Compilando Java...
javac -d . *.java
if errorlevel 1 (
    echo Error compilando Java
    exit /b 1
)

echo Ejecutando Compilador...
if "%~1"=="" (
    echo Uso: run.bat archivo.txt
    echo Ejemplo: run.bat codigos-de-prueba\codigo1.txt
    goto end
)
echo ---------------------------------- PROGRAMA ----------------------------------
java Compilador "%~1"
if errorlevel 1 goto end

mkdir assemblers 2>nul
cd .\assemblers

rem Ejecutar ml oculto
ml /c /coff .\salida.asm >nul 2>&1
if errorlevel 1 (
    echo Error en ensamblado, mostrando detalles...
    ml /c /coff .\salida.asm
    goto end
)

link /SUBSYSTEM:CONSOLE .\salida.obj >nul
cd ..

echo.
echo ---------------------------------- SALIDA DE ASSEMBLER ----------------------------------
.\assemblers\salida.exe
echo.
:end
