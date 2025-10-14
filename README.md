# Ejecución del Compilador en Windows

## Requisitos previos
Antes de ejecutar el compilador, asegurate de tener instaladas las siguientes herramientas:

- **Yacc (Java)** – utilizado para generar el parser a partir de `grama.y`  
- **Java JDK** – para compilar y ejecutar el código generado

## Instrucciones de ejecución

1. Abre la carpeta del compilador en el **Explorador de archivos**.
2. En la barra de direcciones, escribe `cmd` o `powershell` y presiona **Enter** para abrir una consola en esa carpeta.
3. Ejecuta el siguiente comando:

```powershell
yacc -J grama.y; if ($?) { javac Compilador.java; if ($?) { java Compilador Codigo_Fuente.txt } }
```

> 💡 **Nota:**  
> Podés reemplazar `Codigo_Fuente.txt` por el nombre del archivo de código fuente que quieras compilar.  
> Si el archivo no está en la carpeta del compilador, debés escribir su **ruta completa**.

## Ejemplo

```powershell
java Compilador C:\Users\Axel\Documentos\mi_codigo.txt
```

# Ejecución del Compilador en Linux

## Requisitos previos
Antes de ejecutar el compilador, asegurate de tener instaladas las siguientes herramientas:

- **Yacc (Java)** – utilizado para generar el parser a partir de `grama.y`
- **Java JDK** – para compilar y ejecutar el código generado

Podés instalarlas (si no las tenés) con:
```bash
sudo apt install byacc default-jdk
```

## Instrucciones de ejecución

1. Abre una terminal en la carpeta del compilador.
   - Por ejemplo, si el compilador está en `~/Documentos/Compilador`, ejecutá:
   ```bash
   cd ~/Documentos/Compilador
   ```

2. Ejecutá el siguiente comando:

```bash
yacc -J grama.y && javac Compilador.java && java Compilador Codigo_Fuente.txt
```

> 💡 **Nota:**  
> Podés reemplazar `Codigo_Fuente.txt` por el nombre del archivo de código fuente que quieras compilar.  
> Si el archivo no está en la carpeta del compilador, debés escribir su **ruta completa**.

## Ejemplo

```bash
java Compilador /home/axel/Documentos/mi_codigo.txt
```

