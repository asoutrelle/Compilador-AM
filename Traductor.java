import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

public class Traductor {
    private static StringBuilder data = new StringBuilder();
    private static StringBuilder code = new StringBuilder();
    private static StringBuilder prints = new StringBuilder();
    private static StringBuilder etiqueta = new StringBuilder();

    public static void generarAssembler() {
        generarData();
        generarCodigo();
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
            bw.write(data.toString());
            bw.write("msg_div0 db \"ERROR: division por cero\", 0\n");
            bw.write("msg_negativo db \"ERROR: resultado negativo\", 0\n");
            bw.newLine();
            bw.write(".code\n"+
                    "start:\n");

            bw.write(code.toString());
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

            bw.write("end start");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void generarData() {
        for (Map.Entry<String, Simbolo> var : TablaDeSimbolos.TS.entrySet()) {
            String nombreVar = var.getKey();
            Simbolo simbolo = var.getValue();
            nombreVar = nombreVar.replace(":", "_");


            if (simbolo.getUso().equals("nombre de variable")) {
                data.append("_" + nombreVar + " dw" + " ?" + "\n");
                data.append("buffer_" + nombreVar + " db 32 dup(0)\n");
                data.append("msg_" + nombreVar + " db " + "\"_" + nombreVar + " = \" , 0\n\n");

                prints.append("movzx eax, _" + nombreVar + "\n" +
                        "invoke dwtoa, eax, addr buffer_" + nombreVar + "\n" +
                        "invoke StdOut, addr msg_" + nombreVar + "\n" +
                        "invoke StdOut, addr buffer_" + nombreVar + "\n" +
                        "invoke StdOut, chr$(13,10)\n");
            }
            if (simbolo.getUso().equals("variable auxiliar")) {
                data.append(nombreVar + " dw" + " ?" + "\n");
            }
        }
    }

    private static void generarCodigo() {
        for (Terceto terceto : Compilador.tercetos) {
            String op = terceto.getOperacion();
            String val1 = terceto.getVal1();
            String val2 = terceto.getVal2();
            val1 = getString(val1);
            val2 = getString(val2);

            switch (op) {
                case ":=":
                    code.append("mov " + "ax, " + val2 + "\n");
                    code.append("mov " + val1 + ", ax" + "\n");
                    break;
                case "+":

                    code.append("mov " + "ax, " +val1 + "\n");
                    code.append("add " + "ax, " + val2 + "\n");
                    code.append("mov @aux"+ terceto.getIndice() +", ax" + "\n");
                    break;
                case "-":
                    code.append("mov " + "ax, " +val1 + "\n");
                    code.append("sub " + "ax, " + val2 + "\n");
                    code.append("mov @aux"+ terceto.getIndice() +", ax" + "\n");
                    code.append("js resultado_negativo\n");
                    break;
                case "*":
                    code.append("mov bx, "+val2+"\n");
                    code.append("mov " + "ax, " +val1 + "\n");
                    code.append("mul bx\n");
                    code.append("mov @aux"+ terceto.getIndice() +", ax" + "\n");
                    break;
                case "/":
                    code.append("mov dx, 0\n"); // Si no inicializas DX, la división podría fallar si el dividendo es grande
                    code.append("mov bx, "+val2+"\n");
                    code.append("cmp bx, 0\n");
                    code.append("je division_cero\n");
                    code.append("mov " + "ax, " +val1 + "\n");
                    code.append("div bx \n");
                    code.append("mov @aux"+ terceto.getIndice() +", ax" + "\n");
                    break;
            }
        }
    }

    private static String getString(String val2) {
        if (TablaDeSimbolos.TS.containsKey(val2)){
            if (TablaDeSimbolos.TS.get(val2).getUso().equals("constante")) {
                val2 = val2.substring(0, val2.length() - 2); // quitar "UI"
            } else {
                val2 = "_" + val2.replace(":","_");
            }
        } else if (val2.contains("[")){
            val2 = "@aux"+val2.replace("[","").replace("]","");
        }
        return val2;
    }
}