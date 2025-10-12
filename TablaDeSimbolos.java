import java.util.HashMap;

public class TablaDeSimbolos {
    private static HashMap<String, Token> TS = new HashMap<>();
    public TablaDeSimbolos() {
    }

    public static void agregar(String val, Token token) {
        if(!TS.containsKey(val)){
            TS.put(val,token);
        }
    }
    public void imprimir(){
        for (String clave : TS.keySet()) {
            Token token = TS.get(clave);
            System.out.println("Lexema: " + token.getLexema() + " | valor: " + clave);
        }
    }
}
