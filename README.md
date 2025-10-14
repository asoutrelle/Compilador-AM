# Ejecución del Compilador en Windows

## Requisitos previos
Antes de ejecutar el compilador, asegurate de tener instaladas las siguientes herramientas:
 
- **Java JDK** – para compilar y ejecutar el código generado

## Instrucciones de ejecución

1. Abre la carpeta del compilador en el **Explorador de archivos**.
2. En la barra de direcciones, escribe `cmd` y presiona **Enter** para abrir una consola en esa carpeta.
3. Ejecuta el siguiente comando:

```cmd
javac Compilador.java & java Compilador codigos-de-prueba/Test-if.txt
```

> 💡 **Nota:**  
> Podés reemplazar `codigos-de-prueba/Test-if.txt` por el nombre del archivo de código fuente que quieras compilar.  
> Si el archivo no está en la carpeta del compilador, debés escribir su **ruta completa**.

## Ejemplo

```cmd
java Compilador C:\Users\Axel\Documentos\mi_codigo.txt
```

# Ejecución del Compilador en Linux

## Requisitos previos
Antes de ejecutar el compilador, asegurate de tener instaladas las siguientes herramientas:

- **Java JDK** – para compilar y ejecutar el código generado

## Instrucciones de ejecución

1. Abre una terminal en la carpeta del compilador.
   - Por ejemplo, si el compilador está en `~/Documentos/Compilador`, ejecutá:
   ```bash
   cd ~/Documentos/Compilador
   ```

2. Ejecutá el siguiente comando:

```bash
javac Compilador.java && java Compilador codigos-de-prueba/Test-if.txt
```

> 💡 **Nota:**  
> Podés reemplazar `codigos-de-prueba/Test-if.txt` por el nombre del archivo de código fuente que quieras compilar.  
> Si el archivo no está en la carpeta del compilador, debés escribir su **ruta completa**.

## Ejemplo

```bash
java Compilador /home/axel/Documentos/mi_codigo.txt
```

