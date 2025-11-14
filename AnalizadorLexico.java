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
    private static ArrayList<String> tokensDetectados = new ArrayList<>();
    public static String valorTs ="-";


    public AnalizadorLexico(String codigoFuente) throws IOException {
        this.codigoFuente = new PushbackReader(new FileReader(codigoFuente),1);
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
                tokensDetectados.add(Colores.VERDE+"Identificador "+aux+Colores.RESET);
                token = new Token(Parser.ID,nroLinea);
                tokens.add(token);
                if (tokens.size() == 1){
                    Compilador.pilaAmbitos.addFirst(aux);
                }
                return token;
            case 6:
                if(tablaTokens.containsKey(aux)){
                     tokensDetectados.add(Colores.VERDE+"Palabra reservada "+aux+Colores.RESET);
                    int cod = tablaTokens.get(aux);
                    token = new Token(cod,nroLinea);
                    tokens.add(token);
                    return token;
                } else return token;
            case 8:
                 tokensDetectados.add(Colores.VERDE+"Constante entera "+aux+Colores.RESET);
                token = new Token(Parser.CTE,nroLinea);
                tokens.add(token);
                return token;
            case 10:
                 tokensDetectados.add(Colores.VERDE+"Punto flotante "+aux+Colores.RESET);
                token = new Token(Parser.PF64,nroLinea);
                tokens.add(token);
                return token;
            case 11:
                 tokensDetectados.add(Colores.VERDE+"Punto flotante "+aux+Colores.RESET);
                token = new Token(Parser.PF64,nroLinea);
                tokens.add(token);
                return token;
            case 14:
                 tokensDetectados.add(Colores.VERDE+"Punto flotante "+aux+Colores.RESET);
                token = new Token(Parser.PF64,nroLinea);
                tokens.add(token);
                return token;
            case 15:
                 tokensDetectados.add(Colores.VERDE+"Cadena de una linea " + aux+Colores.RESET);
                token = new Token(Parser.CADENA,nroLinea);
                tokens.add(token);
                return token;
        }
        if(tablaTokens.containsKey(aux)){
            int cod = tablaTokens.get(aux);
             tokensDetectados.add(Colores.VERDE+aux+Colores.RESET);
            token = new Token(cod,nroLinea);
            tokens.add(token);
            return token;
        }
        return token;
    }

    public void printTokensDetectados() {
        if (!tokensDetectados.isEmpty()){
            System.out.println(Colores.VERDE + "---------------- TOKENS DETECTADOS ----------------" + Colores.RESET);
        for (String t : tokensDetectados) {
            System.out.println(t);
        }
    }
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
        TablaDeSimbolos.agregar(valor, t, "indefinido");
    }
}