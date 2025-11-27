import java.sql.SQLOutput;
import java.util.*;

public class TablaDeSimbolos {
    public static HashMap<String, Simbolo> TS = new HashMap<>();


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
    public static void agregarVarAux(int nroVarAux){
        String varAux = "@aux"+nroVarAux;
        Simbolo s = new Simbolo(varAux,"uint","variable auxiliar");
        if(!TS.containsKey(varAux)){
            TS.put(varAux,s);
        }
    }

    public static void agregar(String val, Token token){
        Simbolo s = new Simbolo(val, token);
        if(!TS.containsKey(s.getValor())){
            TS.put(s.getValor(),s);
        }
    }

    public static boolean agregarVar(String valor, String ambito, String tipo, String uso){

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

    public static boolean agregarVar(String valor, String ambito, String tipo, String uso, String semantica){
        String aux = valor+ambito;
        if(TS.containsKey(aux)){
            return false;
        }
        Simbolo s = TS.get(valor);
        TS.put(aux,s);
        TS.remove(valor);
        TS.get(aux).setTipo(tipo);
        TS.get(aux).setUso(uso);
        TS.get(aux).setSemantica(semantica);
        return true;
    }

    public static boolean esCompatible(String val1, String val2){
        if (val1.contains("[")){
            val1 = "@aux"+val2.replace("[","").replace("]","");
        }
        if (val2.contains("[")){
            val2 = "@aux"+val2.replace("[","").replace("]","");
        }
        return TS.get(val1).getTipo().equals(TS.get(val2).getTipo());
    }

    public static String varDeclarada(String val, String ambito) {
        int idx;
        String aux = val + ambito;
        while (aux.lastIndexOf(":") != -1) {
            if (TS.containsKey(aux)) {
                return aux;
            }
            idx = aux.lastIndexOf(":");
            aux = aux.substring(0, idx);
        }
        return null;
    }

    public static String varPrefijadaDeclarada(String val, String ambitoVar, String ambito){
        int idx;
        while (ambito.lastIndexOf(":")!=-1) {
            if(TS.containsKey(val+ambito)){
                idx = ambito.lastIndexOf(":");
                String aux = ambito.substring(idx+1);
                if(aux.equals(ambitoVar)){
                    return val+ambito;
                }
            }
            idx = ambito.lastIndexOf(":");
            ambito = ambito.substring(0,idx);
        }
        return "";
    }

    public static boolean parametroDeclarado(String val, String ambito, String funcion){
        String aux = val + ambito; // aux = :PROG:F
        if (TS.containsKey(aux+":"+funcion)) {
            return TS.get(aux+":"+funcion).getSemantica() != null;
        } else {
            int ultimoSeparador = ambito.lastIndexOf(":");
            String ambitoPadre = ambito.substring(0, ultimoSeparador);
            String clavePadre = val + ambitoPadre + ":" + funcion;
            if (TS.containsKey(clavePadre)) {
                return TS.get(clavePadre).getSemantica() != null;
            }
        }
        return false;
    }

    public static String getAmbito(String val){
        //String s = TS.get(val).getTipo();
        return TS.get(val).getTipo();
    }

    public static String funcionDeclarada(String val, String ambito){
        int idx;
        String aux = val + ambito;
        while (aux.lastIndexOf(":")!=-1) {
            if (TS.containsKey(aux)) {
                Simbolo s = TS.get(aux);
                if (s.getUso().equals("nombre de funcion")){
                    return aux;
                }
            }
            idx = aux.lastIndexOf(":");
            aux = aux.substring(0, idx); // quitar la última parte
        }
        return null;
    }

    public static int cantParametrosFormales(String nombreFuncion){
        int cant =0;
        for (HashMap.Entry<String, Simbolo> entry : TS.entrySet()) {
            String clave = entry.getKey();
            int indiceComienzo = clave.indexOf(":");
            String ambito = clave.substring(indiceComienzo+1);
            Simbolo s = entry.getValue();
            if (ambito.equals(nombreFuncion) && s.getSemantica() != null) {
                cant++;
            }
        }
        return cant;
    }

    public static boolean esParametroValido(String paramReal, String paramFormal, String ambito, String nombreFuncion){
        int idx;
        if (paramReal.contains("[")){ //checkear
            return true;
        }
        String aux = paramFormal + ambito;
            if (TS.get(paramReal).getUso().equals("constante")) {
                while (aux.lastIndexOf(":") != -1) {
                    String var = aux + ":" + nombreFuncion;
                    if (TS.containsKey(var)) {
                        Simbolo s = TS.get(var);
                        if (s.getSemantica().equals("copia resultado")) {
                            return false;
                        }
                    }
                    idx = aux.lastIndexOf(":");
                    aux = aux.substring(0, idx);
                }
            }
        return true;
    }

    public static boolean esCopiaResultado(String parametro){
        if (TS.containsKey(parametro)) {
            return TS.get(parametro).getSemantica().equals("copia resultado");
        }
        return false;
    }

    public static void eliminar(String val){
        TS.remove(val);
    }

    public static int buscarReturn(String nombreFuncion){
        Deque<String> pila = new LinkedList<>();

        for (Terceto t : Compilador.tercetos) {
            String op = t.getOperacion();
            // Inicio de función (ajustá si el nombre está en otro campo)
            if (op.equals("inicio de funcion")) {
                String nombre = t.getVal1();
                pila.push(nombre);
                continue;
            }
            // Fin de función
            if (op.equals("fin de funcion")) {
                if (!pila.isEmpty()) pila.pop();
                continue;
            }

            // Return: pertenece a la función del tope de la pila
            if (op.equals("return")) {
                if (!pila.isEmpty() && nombreFuncion.equals(pila.peek())) {
                    return t.getIndice();
                }
            }
        }
        return -1; // no se encontró return para la función pedida
    }

    public static void imprimir() {
        if (!TS.isEmpty()) {

            // Encabezado
            System.out.println("---------------------------------- TABLA DE SIMBOLOS ------------------------------------");
            System.out.printf("%-7s | %-25s | %-10s | %-20s | %-10s%n" , "Lexema", "Valor", "Tipo", "Uso","Semantica");
            System.out.println("-----------------------------------------------------------------------------------------");

            // Filas
            for (String clave : TS.keySet()) {
                Simbolo s = TS.get(clave);

                Token tok = s.getToken();
                String lexemaStr = (tok == null)
                        ? ""
                        : String.valueOf(tok.getLexema());

                String tipo = (s.getTipo() == null) ? "" : s.getTipo();
                String uso = (s.getUso() == null) ? "" : s.getUso();
                String semantica = (s.getSemantica() == null) ? "" : s.getSemantica();

                System.out.printf(
                        "%-7s | %-25s | %-10s | %-20s | %-10s%n",
                        lexemaStr,
                        clave,
                        tipo,
                        uso,
                        semantica
                );
            }

            System.out.println("-----------------------------------------------------------------------------------------");
        }
    }

}
