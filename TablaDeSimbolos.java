import java.util.HashMap;

public class TablaDeSimbolos {
    private static HashMap<String, Simbolo> TS = new HashMap<>();
    public TablaDeSimbolos() {
    }


    public static void agregar(String val, Token token, String tipo){
        Simbolo s = new Simbolo(val, token, tipo);
        if(!TS.containsKey(s.getValor())){
            TS.put(s.getValor(),s);
        }
    }

    public static void agregar(String val, Token token){
        Simbolo s = new Simbolo(val, token);
        if(!TS.containsKey(s.getValor())){
            TS.put(s.getValor(),s);
        }
    }

    public static void addAmbito(String valor, String ambito){
        Simbolo s = TS.get(valor);
        TS.put(valor+":"+ambito,s);
        TS.remove(valor);
    }
    public static void addTipo(String valor, String tipo){
        TS.get(valor).setTipo(tipo);
    }

    public static boolean estaDeclarado(String val, String ambito){
        System.out.println("la key es:"+val+":"+ambito);
        return TS.containsKey(val+":"+ambito);
    }





    public static void imprimir() {
        if (!TS.isEmpty()) {

            // Encabezado
            System.out.println("---------------- TABLA DE SIMBOLOS ----------------");
            System.out.printf("%-7s | %-30s | %-10s%n" , "Lexema", "Valor", "Tipo");
            System.out.println("---------------------------------------------------");

            // Filas
            for (String clave : TS.keySet()) {
                Simbolo s = TS.get(clave);

                int lexema = s.getToken().getLexema();
                String tipo = (s.getTipo() == null) ? "" : s.getTipo();

                System.out.printf(
                        "%-7s | %-30s | %-10s%n",
                        lexema,
                        clave,
                        tipo
                );
            }
        }
    }

}
