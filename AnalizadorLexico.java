import java.io.FileReader;
import java.io.IOException;
import java.io.PushbackReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AnalizadorLexico {
    public static StringBuilder cadena = new StringBuilder();
    private int nroLinea = 1;
    private final PushbackReader codigoFuente;
    private char caracterLeido;
    private List<Token> tokens = new ArrayList<>();
    private HashMap <String, Integer> tablaTokens = new HashMap<>();
    private TablaDeSimbolos tablaDeSimbolos = new TablaDeSimbolos();
    private ArrayList<String> errores = new ArrayList<>();
    private ArrayList<String> warnings = new ArrayList<>();



    public AnalizadorLexico(String rutaArchivo) throws IOException {
        codigoFuente = new PushbackReader(new FileReader(rutaArchivo),1);
        tablaTokens.put("+",2);
        tablaTokens.put("-",3);
        tablaTokens.put("*",4);
        tablaTokens.put("/",5);
        tablaTokens.put(":=",6);
        tablaTokens.put("==",7);
        tablaTokens.put("=!",8);
        tablaTokens.put("<",9);
        tablaTokens.put(">",10);
        tablaTokens.put("<=",11);
        tablaTokens.put(">=",12);
        tablaTokens.put("(",13);
        tablaTokens.put(")",14);
        tablaTokens.put("{",15);
        tablaTokens.put("}",16);
        tablaTokens.put("_",17);
        tablaTokens.put(";",18);
        tablaTokens.put("->",19);
        tablaTokens.put("if",20);
        tablaTokens.put("else",21);
        tablaTokens.put("endif",22);
        tablaTokens.put("print",23);
        tablaTokens.put("return",24);
        tablaTokens.put("CTE",25);
        tablaTokens.put("PF64",26);
        tablaTokens.put("dfloat",28);
        tablaTokens.put("do",29);
        tablaTokens.put("while",30);
        tablaTokens.put("=",31);
    }

    public void leerCaracter() throws IOException {
        int codigo = 0;
        while (codigo != -1) {
            codigo = codigoFuente.read();
            switch (codigo) {
                case '\n':
                    nroLinea++;
                    break;
                case '\t':
                    continue;
                case '\r':
                    break;
            }
            char caracter = (char) codigo;
            caracterLeido = caracter;
            System.out.println("caracter leido: " + caracter);
            StateMatrix.siguiente_estado(caracter,nroLinea);
        }
        for(String error: errores){
            System.out.println(error);
        }
        for (String warning: warnings){
            System.out.println(warning);
        }
        System.out.println("-------------------Impresion de Tabla de Simbolos-----------------------");
        tablaDeSimbolos.imprimir();
        System.out.println("-------------------Token-----------------------");

        for (Token token : tokens) {
            System.out.println(imprimirToken(token.getLexema()));
        }
    }

    public Token reconocerToken(int estadoAnterior) {
        String aux = cadena.toString();
        Token token;
        switch (estadoAnterior){
            case 1:
                System.out.println("Token ID "+aux);
                token = new Token(1,nroLinea);
                tokens.add(token);
                return token;
            case 8:
                System.out.println("Token CTE "+aux);
                token = new Token(25,nroLinea);
                tokens.add(token);
                return token;
            case 11:
                System.out.println("Token PF64 "+aux);
                token = new Token(26,nroLinea);
                tokens.add(token);
                return token;
            case 10:
                System.out.println("Token PF64 "+aux);
                token = new Token(26,nroLinea);
                tokens.add(token);
                return token;
            case 12:
                System.out.println("Token PF64 "+aux);
                token = new Token(26,nroLinea);
                tokens.add(token);
                return token;
            case 15:
                System.out.println("Token PF64 "+aux);
                token = new Token(26,nroLinea);
                tokens.add(token);
                return token;
            case 16:
                System.out.println("Token CADENA 1 LINEA " +aux);
                token = new Token(27,nroLinea);
                tokens.add(token);
                return token;
        }
        if(tablaTokens.containsKey(aux)){
            System.out.println("Token "+aux);
            token = new Token(tablaTokens.get(aux),nroLinea);
            tokens.add(token);
        } else{
            token = null;
        }
        return token;
    }

    public  void vaciarCadena(){
        cadena.setLength(0);
    }

    public  int tamanioCadena(){
        return cadena.length();
    }

    public void devolverCaracterAEntrada() throws IOException {
        codigoFuente.unread(caracterLeido);
    }
    public void agregarCaracter(){
        cadena.append(caracterLeido);
    }
    public int getNroLinea(){
        return nroLinea;
    }
    public void agregarToken(Token token){
        tokens.add(token);
    }
    public String devolverCadena(){
        return cadena.toString();
    }
    public void agregarTS(String valor, Token t){
        tablaDeSimbolos.agregar(valor, t);
    }
    public void addError(String err){
        if(!errores.contains(err)){
            errores.add(err);
        }
    }
    public void addWarning(String war){
        warnings.add(war);
    }

    private String imprimirToken(int codigo){
        switch (codigo) {
            case 1:
                return "ID";
            case 2:
                return "+";
            case 3:
                return "-";
            case 4:
                return "*";
            case 5:
                return "/";
            case 6:
                return ":=";
            case 7:
                return "==";
            case 8:
                return "=!";
            case 9:
                return "<";
            case 10:
                return ">";
            case 11:
                return "<=";
            case 12:
                return ">=";
            case 13:
                return "(";
            case 14:
                return ")";
            case 15:
                return "{";
            case 16:
                return "}";
            case 17:
                return "_";
            case 18:
                return ";";
            case 19:
                return "->";
            case 20:
                return "if";
            case 21:
                return "else";
            case 22:
                return "endif";
            case 23:
                return "print";
            case 24:
                return "return";
            case 25:
                return "CTE";
            case 26:
                return "PF64";
            case 27:
                return "Cadena";
            case 28:
                return "dfloat";
            case 29:
                return "do";
            case 30:
                return "while";
            case 31:
                return "=";
            default:
                throw new IllegalArgumentException("Código de token no reconocido: " + codigo);
        }

    }
}
