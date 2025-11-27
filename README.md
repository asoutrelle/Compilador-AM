# Ejecución del Compilador en Windows

## Requisitos previos
Antes de ejecutar el compilador, asegurate de tener instaladas las siguientes herramientas:
 
- **Java JDK** – para compilar y ejecutar el código generado
- **MASM32** – para compilar y ejecutar el código assembler

## Instrucciones de ejecución

1. Abre la carpeta del compilador en el **Explorador de archivos**.
2. En la barra de direcciones, escribe `cmd` y presiona **Enter** para abrir una consola en esa carpeta.
3. Ejecuta el siguiente comando:

```cmd
run.bat ./codigos-de-prueba-assembler/Test-if.txt
```

> 💡 **Nota:**  
> Podés reemplazar `./codigos-de-prueba-assembler/Test-if.txt` por el nombre del archivo de código fuente que quieras compilar.  
> Si el archivo no está en la carpeta del compilador, debés escribir su **ruta completa**.

## Ejemplo

```cmd
run.bat C:\Users\Axel\Documentos\mi_codigo.txt
```

# Ejecución del Compilador en Linux

## Requisitos previos
Antes de ejecutar el compilador, asegurate de tener instaladas las siguientes herramientas:

- **Java JDK** – para compilar y ejecutar el código generado
- **MASM32** – para compilar y ejecutar el código assembler

## Instrucciones de ejecución

1. Abre una terminal en la carpeta del compilador.

2. Ejecutá el siguiente comando:

```bash
chmod +x run.sh
./run.sh ./codigos-de-prueba-assembler/Test-if.txt
```

> 💡 **Nota:**  
> Podés reemplazar `./codigos-de-prueba-assembler/Test-if.txt` por el nombre del archivo de código fuente que quieras compilar.  
> Si el archivo no está en la carpeta del compilador, debés escribir su **ruta completa**.

## Ejemplo

```bash
./run.sh /home/axel/Documentos/mi_codigo.txt
```

