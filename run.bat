@echo off

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
java Compilador codigo.txt
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
type codigo.txt
echo.
echo ---------------------------------- SALIDA DE ASSEMBLER ----------------------------------
.\assemblers\salida.exe
:end