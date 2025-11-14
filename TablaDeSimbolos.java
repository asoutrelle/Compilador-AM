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
        TS.get(valor).setAmbito(ambito);
    }


    public static void imprimir() {
        if (!TS.isEmpty()) {

            // Encabezado
            System.out.println("---------------- TABLA DE SIMBOLOS ----------------");
            System.out.printf("%-10s | %-10s | %-10s | %-30s%n" , "Lexema", "Valor", "Tipo", "Ambito");
            System.out.println("---------------------------------------------------");

            // Filas
            for (String clave : TS.keySet()) {
                Simbolo s = TS.get(clave);

                int lexema = s.getToken().getLexema();
                String tipo = (s.getTipo() == null) ? "" : s.getTipo();
                String ambito = s.getAmbito();

                System.out.printf(
                        "%-10s | %-10s | %-10s | %-30s%n",
                        lexema,
                        clave,
                        tipo,
                        ambito
                );
            }
        }
    }

}
