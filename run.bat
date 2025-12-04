@echo off

echo Ejecutando Compilador...
if "%~1"=="" (
    echo Uso: run.bat archivo.txt
    echo Ejemplo: run.bat codigos-de-prueba\codigo1.txt
    goto end
)
echo ---------------------------------- PROGRAMA ----------------------------------
java -jar Compilador-AM.jar "%~1"
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
echo ---------------------------------- CODIGO ----------------------------------
type "%~1"
echo.
echo ---------------------------------- SALIDA DE ASSEMBLER ----------------------------------
.\assemblers\salida.exe
:end