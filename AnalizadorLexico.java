import java.io.FileReader;
import java.io.IOException;
import java.io.PushbackReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AnalizadorLexico {
    public static StringBuilder cadena = new StringBuilder();
    private static int nroLinea = 1;
    private static PushbackReader codigoFuente = null;
    private static char caracterLeido;
    private static List<Token> tokens = new ArrayList<>();
    private static HashMap <String, Integer> tablaTokens = new HashMap<>();
    static TablaDeSimbolos tablaDeSimbolos = new TablaDeSimbolos();
    private static ArrayList<String> errores = new ArrayList<>();
    private static ArrayList<String> warnings = new ArrayList<>();
    public static String valorTs ="-";


    public AnalizadorLexico() throws IOException {
        codigoFuente = new PushbackReader(new FileReader("Codigo_Fuente.txt"),1);
        tablaTokens.put(":=",(int) Parser.ASIG);
        tablaTokens.put("==",(int) Parser.IGUAL);
        tablaTokens.put("=!",(int) Parser.NOIGUAL);
        tablaTokens.put("<=",(int) Parser.MENORIGUAL);
        tablaTokens.put(">=",(int) Parser.MAYORIGUAL);
        tablaTokens.put("->",(int) Parser.FLECHA);
        tablaTokens.put("+",43);
        tablaTokens.put("-",45);
        tablaTokens.put("*",42);
        tablaTokens.put("/",47);
        tablaTokens.put("=",61);
        tablaTokens.put(">",62);
        tablaTokens.put("<",60);
        tablaTokens.put(";",59);
        tablaTokens.put("(",40);
        tablaTokens.put(")",41);
        tablaTokens.put("{",123);
        tablaTokens.put("}",125);
        tablaTokens.put("_",95);
        tablaTokens.put("if",(int) Parser.IF);
        tablaTokens.put("dfloat",(int) Parser.DFLOAT);
        tablaTokens.put("else",(int) Parser.ELSE);
        tablaTokens.put("endif",(int) Parser.ENDIF);
        tablaTokens.put("print",(int) Parser.PRINT);
        tablaTokens.put("return",(int) Parser.RETURN);
        tablaTokens.put("do",(int) Parser.DO);
        tablaTokens.put("while",(int) Parser.WHILE);
        tablaTokens.put("uint",(int) Parser.UINT);
        tablaTokens.put("cr",(int) Parser.CR);
        tablaTokens.put("trunc",(int) Parser.TRUNC);
        tablaTokens.put(",",44);
        tablaTokens.put(".",46);
    }


    public static Token leerCaracter() throws IOException {
        int codigo = 0;
        Token token = null;
        while (token==null && codigo != -1) {
            codigo = codigoFuente.read();
            switch (codigo) {
                case '\n':
                    nroLinea++;
                    break;
                case '\t', '\r':
                    continue;
            }
            caracterLeido = (char) codigo;
            //System.out.println("caracter leido: " + caracterLeido);
            token = StateMatrix.siguiente_estado(caracterLeido,nroLinea);
        }
        if (codigo == -1){
            return new Token(0,0);
        }
        return token;
    }
    public Token reconocerToken(int estadoAnterior) {
        Token token = null;
        String aux = cadena.toString();
        switch (estadoAnterior){
            case 1:
                System.out.println(Colores.VERDE+"Token ID "+aux+Parser.ID+Colores.RESET);
                token = new Token(Parser.ID,nroLinea);
                tokens.add(token);
                return token;
            case 8:
                System.out.println(Colores.VERDE+"Token CTE "+aux+Parser.CTE+Colores.RESET);
                token = new Token(Parser.CTE,nroLinea);
                tokens.add(token);
                return token;
            case 11:
                System.out.println(Colores.VERDE+"Token PF64 "+aux+Parser.PF64+Colores.RESET);
                token = new Token(Parser.PF64,nroLinea);
                tokens.add(token);
                return token;
            case 10:
                System.out.println(Colores.VERDE+"Token PF64 "+aux+Parser.PF64+Colores.RESET);
                token = new Token(Parser.PF64,nroLinea);
                tokens.add(token);
                return token;
            case 12:
                System.out.println(Colores.VERDE+"Token PF64 "+aux+Parser.PF64+Colores.RESET);
                token = new Token(Parser.PF64,nroLinea);
                tokens.add(token);
                return token;
            case 15:
                System.out.println(Colores.VERDE+"Token PF64 "+aux+Parser.PF64+Colores.RESET);
                token = new Token(Parser.PF64,nroLinea);
                tokens.add(token);
                return token;
            case 16:
                System.out.println(Colores.VERDE+"Token CADENA 1 LINEA " + aux+Parser.CADENA+Colores.RESET);
                token = new Token(Parser.CADENA,nroLinea);
                tokens.add(token);
                return token;
        }
        if(tablaTokens.containsKey(aux)){
            int cod = tablaTokens.get(aux);
            System.out.println(Colores.VERDE+"Token " + aux+cod+Colores.RESET);
            token = new Token(cod,nroLinea);
            tokens.add(token);
            return token;
        }
        return token;
    }






    public void vaciarCadena(){
        cadena.setLength(0);
    }
    public void devolverCaracterAEntrada() throws IOException {
        if(caracterLeido=='\n'){
            nroLinea--;
        }
        codigoFuente.unread(caracterLeido);
    }
    public void agregarCaracter(){
        cadena.append(caracterLeido);
    }
    public int getNroLinea(){
        return nroLinea;
    }
    public String devolverCadena(){
        return cadena.toString();
    }
    public void agregarTS(String valor, Token t){
        valorTs = valor;
        tablaDeSimbolos.agregar(valor, t);
    }
    public static void addError(String err){
            errores.add(err);

    }
    public void addWarning(String war){
        warnings.add(war);
    }

    public void print(){
        System.out.println("-------------------Impresion de Tabla de Simbolos-----------------------");
        tablaDeSimbolos.imprimir();
        System.out.println("------------------------------------------------------------------------");
        for(String err : errores){
            System.out.println(Colores.ROJO+err+Colores.RESET);
        }
        for(String war : warnings){
            System.out.println(Colores.AMARILLO+war+Colores.RESET);
        }
    }
}