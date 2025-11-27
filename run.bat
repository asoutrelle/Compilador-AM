@echo off

REM ---------------------------------------
REM Validar que se pase un parametro
REM ---------------------------------------
if "%~1"=="" (
    echo ERROR: Debe indicar la ruta del archivo de codigo.
    echo Ejemplo:
    echo    run.bat codigo.txt
    echo    run.bat C:\Users\matii\prog\codigo.txt
    exit /b 1
)

set ARCHIVO=%~1

if not exist "%ARCHIVO%" (
    echo ERROR: El archivo "%ARCHIVO%" no existe.
    exit /b 1
)

echo Usando archivo: %ARCHIVO%
echo.

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
echo ---------------------------------- PROGRAMA ----------------------------------
java Compilador "%ARCHIVO%"
if errorlevel 1 goto end

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

echo ---------------------------------- CODIGO ----------------------------------
type "%ARCHIVO%"
echo.
echo ---------------------------------- SALIDA DE ASSEMBLER ----------------------------------
.\assemblers\salida.exe

:end
