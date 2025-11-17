import java.util.HashMap;

public class TablaDeSimbolos {
    private static HashMap<String, Simbolo> TS = new HashMap<>();
    public TablaDeSimbolos() {
    }
    public static void agregar(String val, Token token, String tipo, String uso){
        Simbolo s = new Simbolo(val, token, tipo, uso);
        if(!TS.containsKey(s.getValor())){
            TS.put(s.getValor(),s);
        }
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

    public static boolean checkVar(String valor, String ambito, String tipo, String uso){
        String aux = valor+ambito;
        if(TS.containsKey(aux)){
            return false;
        }
        Simbolo s = TS.get(valor);
        TS.put(aux,s);
        TS.remove(valor);
        TS.get(aux).setTipo(tipo);
        TS.get(aux).setUso(uso);
        return true;
    }
    public static boolean esCompatible(String val1, String val2, String ambito){
        val1 = val1+ambito;
        val2 = val2+ambito;
        while(!TS.containsKey(val1)){
            int idx = val1.lastIndexOf(":");
            val1 = val1.substring(0, idx);
            if (idx == -1) {
                return false;
            }
        }
        while(!TS.containsKey(val2)){
            int idx = val2.lastIndexOf(":");
            val2 = val2.substring(0, idx);
            if (idx == -1) {
                return false;
            }
        }
        return TS.get(val1).getTipo().equals(TS.get(val2).getTipo());
    }
    public static boolean varDeclarada(String val, String ambito){
        String aux = val + ambito;
        while (aux.lastIndexOf(":")!=-1) {
            if (TS.containsKey(aux)) {
                return true;
            }
            int idx = aux.lastIndexOf(":");
            if (idx == -1) {
                return false; // no hay más ámbitos para quitar
            } else {
                aux = aux.substring(0, idx); // quitar la última parte
            }
        }
        return false;
    }

    public static boolean varPrefijadaDeclarada(String val, String ambito){
        String aux = val + ":" + ambito;
        return TS.containsKey(aux);
    }

    public static boolean funcionDeclarada(String val, String ambito){
        String aux = val + ambito;
        while (aux.lastIndexOf(":")!=-1) {
            if (TS.containsKey(aux)) {
                Simbolo s = TS.get(aux);
                if (s.getUso().equals("nombre de funcion")){
                    return true;
                }
            }
            int idx = aux.lastIndexOf(":");
            if (idx == -1) {
                return false; // no hay más ámbitos para quitar
            } else {
                aux = aux.substring(0, idx); // quitar la última parte
            }
        }
        return false;
    }

    public static void eliminar(String val){
        TS.remove(val);
    }






    public static void imprimir() {
        if (!TS.isEmpty()) {

            // Encabezado
            System.out.println("--------------------------- TABLA DE SIMBOLOS ---------------------------");
            System.out.printf("%-7s | %-25s | %-10s | %-20s%n" , "Lexema", "Valor", "Tipo", "Uso");
            System.out.println("-------------------------------------------------------------------------");

            // Filas
            for (String clave : TS.keySet()) {
                Simbolo s = TS.get(clave);

                int lexema = s.getToken().getLexema();
                String tipo = (s.getTipo() == null) ? "" : s.getTipo();
                String uso = (s.getUso() == null) ? "" : s.getUso();


                System.out.printf(
                        "%-7s | %-25s | %-10s | %-20s%n",
                        lexema,
                        clave,
                        tipo,
                        uso
                );
            }
            System.out.println("-------------------------------------------------------------------------");
        }
    }

}
