#!/bin/bash

echo "Ejecutando yacc..."
yacc -J -v grama.y
if [ $? -ne 0 ]; then
    echo "Error en yacc"
    exit 1
fi

echo "Compilando Java..."
javac -d . *.java
if [ $? -ne 0 ]; then
    echo "Error compilando Java"
    exit 1
fi

echo "Ejecutando Compilador..."

# Verificar parámetro
if [ -z "$1" ]; then
    echo "Uso: ./run.sh archivo.txt"
    echo "Ejemplo: ./run.sh codigos-de-prueba/codigo1.txt"
    exit 1
fi

echo "---------------------------------- PROGRAMA ----------------------------------"
java Compilador "$1"
if [ $? -ne 0 ]; then
    exit 1
fi


# Crear carpeta assemblers si no existe
mkdir -p assemblers
cd assemblers || exit 1

# Ejecutar ml oculto
echo "Ejecutando ml..."
ml /c /coff salida.asm >/dev/null 2>&1
if [ $? -ne 0 ]; then
    echo "Error en ensamblado, mostrando detalles..."
    ml /c /coff salida.asm
    exit 1
fi

link /SUBSYSTEM:CONSOLE salida.obj >/dev/null
cd ..

echo
echo "---------------------------------- SALIDA DE ASSEMBLER ----------------------------------"
./assemblers/salida.exe
