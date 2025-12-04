import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class Traductor {
    private static StringBuilder data = new StringBuilder();
    private static StringBuilder prints = new StringBuilder();
    private static StringBuilder etiqueta = new StringBuilder();
    private static StringBuilder funciones = new StringBuilder();
    private Map<String, List<Terceto>> tercetosFunc = new HashMap<>();

    public void generarAssembler() {
        generarData();
        generarFunc();
        generarCodigo();

        try {
            // Crear carpeta externa si no existe
            File carpeta = new File("assemblers");
            if (!carpeta.exists()) {
                carpeta.mkdirs();
            }

            try (BufferedWriter bw = new BufferedWriter(new FileWriter("assemblers/salida.asm"))) {
                bw.write(".386\n" +
                        ".model flat, stdcall\n" +
                        "option casemap:none\n" +
                        "\n" +
                        "include \\masm32\\include\\windows.inc\n" +
                        "include \\masm32\\include\\kernel32.inc\n" +
                        "includelib \\masm32\\lib\\kernel32.lib\n" +
                        "include \\masm32\\include\\masm32.inc\n" +
                        "includelib \\masm32\\lib\\masm32.lib\n" +
                        "include \\masm32\\include\\masm32rt.inc\n" +
                        "\n" +
                        ".data\n");
                bw.write("buffer_print db 32 dup(?)\n\n");
                bw.write("msg_div0 db \"ERROR: division por cero\", 0\n");
                bw.write("msg_negativo db \"ERROR: resultado negativo\", 0\n");
                bw.write("newcw  dw ?\n\n");
                bw.write(data.toString());
                bw.newLine();
                bw.write(".code\n");

                bw.write(funciones.toString());

                bw.newLine();
                bw.write(prints.toString());
                bw.newLine();
                bw.write(etiqueta.toString());

                bw.write("invoke ExitProcess, 0\n");
                bw.write("division_cero:\n" +
                        "invoke StdOut, addr msg_div0\n" +
                        "invoke ExitProcess, 1\n");
                bw.write("resultado_negativo:\n" +
                        "invoke StdOut, addr msg_negativo\n" +
                        "invoke ExitProcess, 1\n");

                bw.write("end _start");

            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

     private void generarData() {
        for (Map.Entry<String, Simbolo> var : TablaDeSimbolos.TS.entrySet()) {
            String nombreVar = var.getKey();
            Simbolo simbolo = var.getValue();
            nombreVar = nombreVar.replace(":", "_");


            if (simbolo.getUso().equals("nombre de variable")) {
                data.append("_" + nombreVar + " dw" + " ?" + "\n");
                data.append("buffer_" + nombreVar + " db 32 dup(0)\n");
                data.append("msg_" + nombreVar + " db " + "\"_" + nombreVar + " = \" , 0\n\n");
            }
            if (simbolo.getUso().equals("variable auxiliar")) {
                data.append(nombreVar + " dw" + " ?" + "\n\n");
            }
        }
    }
    private void generarCodigo() {
        // 1. Primero las funciones que NO son start
        for (Map.Entry<String, List<Terceto>> funcion : tercetosFunc.entrySet()) {
            if (!funcion.getKey().equals("start")) {
                generarAssemblerDeFuncion(funcion);
            }
        }

        // 2. Después START al final
        generarAssemblerDeFuncion(Map.entry("start", tercetosFunc.get("start")));
    }


    private void generarAssemblerDeFuncion(Map.Entry<String, List<Terceto>> funcion) {
        String nombreFunc = funcion.getKey();
        List<Terceto> tercetos = funcion.getValue();

        funciones.append("_" + nombreFunc.replace(":", "_") + ":\n");
        funciones.append("finit\n" +
                "; ---- Configurar TRUNC permanente ----\n" +
                "fnstcw newcw\n" +
                "mov ax, newcw\n" +
                "or ah, 0Ch\n" +
                "mov newcw, ax\n" +
                "fldcw newcw\n\n");
        for (Terceto terceto : tercetos) {
            String op = terceto.getOperacion();
            String val1 = terceto.getVal1();
            String val2 = terceto.getVal2();
            val1 = getString(val1);
            val2 = getString(val2);

            switch (op) {
                case ":=":
                    funciones.append("mov ax, " + val2 + "\n");
                    funciones.append("mov " + val1 + ", ax\n");
                    break;
                case "+":
                    funciones.append("mov ax, " + val1 + "\n");
                    funciones.append("add ax, " + val2 + "\n");
                    funciones.append("mov @aux" + terceto.getIndice() + ", ax\n");
                    break;
                case "-":
                    funciones.append("mov ax, " + val1 + "\n");
                    funciones.append("sub ax, " + val2 + "\n");
                    funciones.append("mov @aux" + terceto.getIndice() + ", ax\n");
                    funciones.append("js resultado_negativo\n");
                    break;
                case "*":
                    funciones.append("mov bx, " + val2 + "\n");
                    funciones.append("mov ax, " + val1 + "\n");
                    funciones.append("mul bx\n");
                    funciones.append("mov @aux" + terceto.getIndice() + ", ax\n");
                    break;
                case "/":
                    funciones.append("mov dx, 0\n");
                    funciones.append("mov bx, " + val2 + "\n");
                    funciones.append("cmp bx, 0\n");
                    funciones.append("je division_cero\n");
                    funciones.append("mov ax, " + val1 + "\n");
                    funciones.append("div bx\n");
                    funciones.append("mov @aux" + terceto.getIndice() + ", ax\n");
                    break;
                case ">":
                    funciones.append("; --- > ---\n");
                    funciones.append("mov ax, " + val1 + "\n");
                    funciones.append("mov bx, " + val2 + "\n");
                    funciones.append("cmp ax, bx\n");
                    funciones.append("mov @aux" + terceto.getIndice() + ", 0\n");        // por defecto falso
                    funciones.append("jle cmp_end_" + terceto.getIndice() + "\n");       // ax <= bx → falso
                    funciones.append("mov @aux" + terceto.getIndice() + ", 1\n");        // ax > bx → verdadero
                    funciones.append("cmp_end_" + terceto.getIndice() + ":\n\n");
                    break;

                case "<":
                    funciones.append("; --- < ---\n");
                    funciones.append("mov ax, " + val1 + "\n");
                    funciones.append("mov bx, " + val2 + "\n");
                    funciones.append("cmp ax, bx\n");
                    funciones.append("mov @aux" + terceto.getIndice() + ", 0\n");
                    funciones.append("jge cmp_end_" + terceto.getIndice() + "\n");       // ax >= bx
                    funciones.append("mov @aux" + terceto.getIndice() + ", 1\n");        // ax < bx
                    funciones.append("cmp_end_" + terceto.getIndice() + ":\n\n");
                    break;

                case ">=":
                    funciones.append("; --- >= ---\n");
                    funciones.append("mov ax, " + val1 + "\n");
                    funciones.append("mov bx, " + val2 + "\n");
                    funciones.append("cmp ax, bx\n");
                    funciones.append("mov @aux" + terceto.getIndice() + ", 0\n");
                    funciones.append("jl cmp_end_" + terceto.getIndice() + "\n");        // ax < bx
                    funciones.append("mov @aux" + terceto.getIndice() + ", 1\n");        // ax >= bx
                    funciones.append("cmp_end_" + terceto.getIndice() + ":\n\n");
                    break;

                case "<=":
                    funciones.append("; --- <= ---\n");
                    funciones.append("mov ax, " + val1 + "\n");
                    funciones.append("mov bx, " + val2 + "\n");
                    funciones.append("cmp ax, bx\n");
                    funciones.append("mov @aux" + terceto.getIndice() + ", 0\n");
                    funciones.append("jg cmp_end_" + terceto.getIndice() + "\n");        // ax > bx
                    funciones.append("mov @aux" + terceto.getIndice() + ", 1\n");        // ax <= bx
                    funciones.append("cmp_end_" + terceto.getIndice() + ":\n\n");
                    break;

                case "==":
                    funciones.append("; --- == ---\n");
                    funciones.append("mov ax, " + val1 + "\n");
                    funciones.append("mov bx, " + val2 + "\n");
                    funciones.append("cmp ax, bx\n");
                    funciones.append("mov @aux" + terceto.getIndice() + ", 0\n");
                    funciones.append("jne cmp_end_" + terceto.getIndice() + "\n");       // no iguales
                    funciones.append("mov @aux" + terceto.getIndice() + ", 1\n");        // iguales
                    funciones.append("cmp_end_" + terceto.getIndice() + ":\n\n");
                    break;

                case "=!":
                    funciones.append("; --- != ---\n");
                    funciones.append("mov ax, " + val1 + "\n");
                    funciones.append("mov bx, " + val2 + "\n");
                    funciones.append("cmp ax, bx\n");
                    funciones.append("mov @aux" + terceto.getIndice() + ", 0\n");
                    funciones.append("je cmp_end_" + terceto.getIndice() + "\n");        // iguales
                    funciones.append("mov @aux" + terceto.getIndice() + ", 1\n");        // distintos
                    funciones.append("cmp_end_" + terceto.getIndice() + ":\n\n");
                    break;
                case "BF": // esto es para bifurcacion por falso
                    funciones.append("; ---BF---\n");
                    funciones.append("cmp "+val1+", 0\n");
                    funciones.append("je cmp_end_"+val2+"\n");
                    break;
                case "BI": // esto es para el else bifurcacion incondicional
                    funciones.append("; ---BI---\n");
                    funciones.append("jmp cmp_end_"+val1+"\n");
                    funciones.append("cmp_end_"+val2+":\n");
                    break;
                case "fin if":
                    funciones.append("; ---ENDIF---\n");
                    funciones.append("cmp_end_@aux"+terceto.getIndice()+":\n");
                    break;
                case "inicio while":
                    funciones.append("; ---WHILE---\n");
                    funciones.append("cmp_end_@aux"+terceto.getIndice()+":\n");
                    break;
                case "BIW": // esto proboca el bucle del while
                    funciones.append("; ---BIW---\n");
                    funciones.append("jmp cmp_end_"+val1+"\n");
                    break;
                case "fin while":
                    funciones.append("; ---FIN WHILE---\n");
                    funciones.append("cmp_end_@aux"+terceto.getIndice()+":\n");
                    break;
                case "print":
                    funciones.append("; --- PRINT ---\n");
                    if (val1.startsWith("\"")) {
                        // imprimir string literal
                        String etiquetaStr = "_str_" + terceto.getIndice();
                        data.append(etiquetaStr + " db " + val1 + ", 0\n");
                        funciones.append("invoke StdOut, addr " + etiquetaStr + "\n");
                        funciones.append("invoke StdOut, chr$(13,10)\n");
                    } else {
                        // imprimir número
                        funciones.append("movzx eax, " + val1 + "\n");
                        funciones.append("invoke dwtoa, eax, addr buffer_print\n");
                        funciones.append("invoke StdOut, addr buffer_print\n");
                        funciones.append("invoke StdOut, chr$(13,10)\n");
                    }
                    break;
                case "return":
                    funciones.append("; --- RETURN ---\n");
                    funciones.append("mov ax, " + val1+"\n");
                    funciones.append("mov " + val2 + ", ax\n");
                    funciones.append("ret\n");
                    break;
                case "invocacion":
                    funciones.append("; --- INVOCACION ---\n");
                    funciones.append("call " + val1 + "\n");
                    break;
                case "trunc":
                    funciones.append("; --- TRUNC ---\n");
                    data.append("_float_"+terceto.getIndice() +" dd "+ val1 + "\n");

                    funciones.append("fld _float_"+terceto.getIndice() +"\n");
                    funciones.append("fistp @aux"+terceto.getIndice() +"\n");
                    break;
            }
        }
    }

    private String getString(String val) {
        if (TablaDeSimbolos.TS.containsKey(val)){
            Simbolo s = TablaDeSimbolos.TS.get(val);
            if (s.getUso().equals("constante")){

                if(s.getTipo().equals("uint")) {
                    val = val.substring(0, val.length() - 2);
                } else {
                    System.out.println("valor pf: "+val);
                    return val.replace(" ", "")
                            .replace("D", "E")
                            .replace("+", "");
                }
                return val;// quitar "UI"
            }
            if (s.getUso().equals("nombre de variable") || s.getUso().equals("nombre de funcion")){
                val = "_" + val.replace(":","_");
                return val;
            }
        }
        if (val.contains("[")){
            val = "@aux"+val.replace("[","").replace("]","");
            return val;
        }
        return val;
    }

    private void generarFunc() {
        Stack<String> pilaNombreFuncion = new Stack<>(); // Apilamos
        pilaNombreFuncion.push("start");
        tercetosFunc.put("start", new ArrayList<>());

        for (Terceto terceto : Compilador.tercetos){
            if (terceto.getOperacion().equals("inicio de funcion")){
                pilaNombreFuncion.push(terceto.getVal1());
                tercetosFunc.put(terceto.getVal1(), new ArrayList<>());
                tercetosFunc.get(terceto.getVal1()).add(terceto);
            } else if (terceto.getOperacion().equals("fin de funcion")){
                tercetosFunc.get(pilaNombreFuncion.peek()).add(terceto);
                pilaNombreFuncion.pop();
            } else {
                tercetosFunc.get(pilaNombreFuncion.peek()).add(terceto);
            }
        }
    }
}