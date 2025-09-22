import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public class ASMatrix {
    @FunctionalInterface
    interface AccionSemantica {
        void ejecutar() throws IOException;
    }
    private static Map<Integer, AccionSemantica> mapa = new HashMap<>();
    private static int[][] matrix;
    private static int estadoAnterior;


    public ASMatrix(int[][] matrix, AnalizadorLexico analizadorLexico){
        ASMatrix.matrix = matrix;

        mapa.put(0, new AccionSemantica() {
            public void ejecutar() {
            }
        });
        mapa.put(2, new AccionSemantica() {
            public void ejecutar() {
                analizadorLexico.agregarCaracter();
            }
        });
        mapa.put(3, new AccionSemantica() {
            public void ejecutar() throws IOException {
                analizadorLexico.devolverCaracterAEntrada();
                Token t = analizadorLexico.reconocerToken(estadoAnterior);
                String valor = analizadorLexico.devolverCadena();
                if (valor.length() > 20) {
                    System.out.println("Identificador con mas de 20 caracteres en linea: " + analizadorLexico.getNroLinea());
                    valor = valor.substring(0, 20); // truncar a 20 caracteres
                }
                if(!(t == null)){
                    analizadorLexico.agregarTS(valor, t);
                }
                analizadorLexico.vaciarCadena();
            }
        });
        mapa.put(4, new AccionSemantica() {
            public void ejecutar() throws IOException {
                analizadorLexico.devolverCaracterAEntrada();
                analizadorLexico.reconocerToken(estadoAnterior);
                analizadorLexico.vaciarCadena();
            }
        });
        mapa.put(5, new AccionSemantica() {
            public void ejecutar() throws IOException {
                analizadorLexico.devolverCaracterAEntrada();
                Token t = analizadorLexico.reconocerToken(estadoAnterior);
                if(t == null){
                    String err ="ERROR LINEA "+ analizadorLexico.getNroLinea() + ": La palabra reservada "+analizadorLexico.devolverCadena()+ " no existe";
                    analizadorLexico.addError(err);
                }
                analizadorLexico.vaciarCadena();
            }
        });
        mapa.put(6, new AccionSemantica() {
            public void ejecutar() {
                analizadorLexico.agregarCaracter();
                String valor = analizadorLexico.devolverCadena();
                // Extraer solo los dígitos al inicio
                String numeroStr = valor.replaceAll("[^0-9-]", "");
                int numero = Integer.parseInt(numeroStr);
                if (numero < 0 || numero > 0xFFFF) {
                    String err ="ERROR LINEA "+ analizadorLexico.getNroLinea() + ": NO está en el rango de unsigned 16 bits";
                    analizadorLexico.addError(err);
                } else {
                    Token t = analizadorLexico.reconocerToken(estadoAnterior);
                    if(!(t == null)){
                        analizadorLexico.agregarTS(valor, t);
                    }
                }
                    analizadorLexico.vaciarCadena();
            }
        });
        mapa.put(7, new AccionSemantica() {
            public void ejecutar() throws IOException {
                analizadorLexico.devolverCaracterAEntrada();
                String valor = analizadorLexico.devolverCadena();
                String valor_normalizado = valor.replace(" D", "E")
                        .replace("D", "E")
                        .replace("+", "");
                BigDecimal numero = new BigDecimal(valor_normalizado);
                System.out.println("Numero: " + numero);

                BigDecimal min = new BigDecimal("2.2250738585072014E-308");
                BigDecimal max = new BigDecimal("1.7976931348623157E308");
                BigDecimal cero = BigDecimal.ZERO;
                // chequeo de rango
                boolean enRango =
                        numero.compareTo(cero) == 0 ||
                                (numero.compareTo(min) >= 0 && numero.compareTo(max) <= 0) ||
                                (numero.compareTo(min.negate()) <= 0 && numero.compareTo(max.negate()) >= 0);

                if (enRango) {
                    Token t = analizadorLexico.reconocerToken(estadoAnterior);
                    if (t != null) {
                        analizadorLexico.agregarTS(valor, t);
                    }
                } else {
                    String err = "ERROR LINEA " + analizadorLexico.getNroLinea() +
                            ": NO está en el rango de un float 64 bits";
                    analizadorLexico.addError(err);
                }
                analizadorLexico.vaciarCadena();
            }
        });
        mapa.put(8, new AccionSemantica() {
            public void ejecutar() {
                analizadorLexico.agregarCaracter();
                analizadorLexico.reconocerToken(estadoAnterior);
                analizadorLexico.vaciarCadena();
            }
        });
        mapa.put(-1, new AccionSemantica() {
            public void ejecutar() throws IOException {
                analizadorLexico.devolverCaracterAEntrada();
                String err = "ERROR LINEA "+ analizadorLexico.getNroLinea()+ ": Caracter invalido, las asignaciones se escriben con :=";
                analizadorLexico.addError(err);
                analizadorLexico.vaciarCadena();
            }
        });
        mapa.put(-2, new AccionSemantica() {
            public void ejecutar() throws IOException {
                analizadorLexico.devolverCaracterAEntrada();
                String err ="ERROR LINEA "+ analizadorLexico.getNroLinea()+ ": Salto de linea dentro de cadena de 1 linea";
                analizadorLexico.addError(err);
                analizadorLexico.vaciarCadena();
            }
        });
        mapa.put(-3, new AccionSemantica() {
            public void ejecutar() throws IOException {
                analizadorLexico.devolverCaracterAEntrada();
                String err ="ERROR LINEA "+ analizadorLexico.getNroLinea()+ ": Caracter invalido abriendo o cerrando comentario multilinea";
                analizadorLexico.addError(err);
                analizadorLexico.vaciarCadena();
            }
        });
        mapa.put(-4, new AccionSemantica() {
            public void ejecutar() throws IOException {
                analizadorLexico.devolverCaracterAEntrada();
                String err ="ERROR LINEA "+ analizadorLexico.getNroLinea()+ ": Caracter invalido declarando un numero";
                analizadorLexico.addError(err);
                analizadorLexico.vaciarCadena();
            }
        });
        mapa.put(-5, new AccionSemantica() {
            public void ejecutar() throws IOException {
                analizadorLexico.devolverCaracterAEntrada();
                String err ="ERROR LINEA "+ analizadorLexico.getNroLinea()+ ": Caracter invalido declarando punto flotante";
                analizadorLexico.addError(err);
                analizadorLexico.vaciarCadena();
            }
        });
        mapa.put(-6, new AccionSemantica() {
            public void ejecutar() throws IOException {
                analizadorLexico.devolverCaracterAEntrada();
                String err ="ERROR LINEA "+ analizadorLexico.getNroLinea()+ ": Caracter invalido, debe ir I para declarar entero sin signo";
                analizadorLexico.addError(err);
                analizadorLexico.vaciarCadena();
            }
        });
        mapa.put(-7, new AccionSemantica() {
            public void ejecutar() throws IOException {
                analizadorLexico.devolverCaracterAEntrada();
                String err ="ERROR LINEA "+ analizadorLexico.getNroLinea()+ ": Operador no permitido";
                analizadorLexico.addError(err);
                analizadorLexico.vaciarCadena();
            }
        });
        mapa.put(-8, new AccionSemantica() {
            public void ejecutar() throws IOException {
                analizadorLexico.devolverCaracterAEntrada();
                String err ="ERROR LINEA "+ analizadorLexico.getNroLinea()+ ": El exponente debe tener al menos un digito";
                analizadorLexico.addError(err);
                analizadorLexico.vaciarCadena();
            }
        });
        mapa.put(-9, new AccionSemantica() {
            public void ejecutar() {
                String err ="ERROR LINEA "+ analizadorLexico.getNroLinea()+ ": Caracter invalido";
                analizadorLexico.addError(err);
                analizadorLexico.vaciarCadena();
            }
        });
    }
    public static void ejecutarAccion(int fil, int col, char caracter, int nroLinea) throws IOException {
        int as = matrix[fil][col];
        estadoAnterior = fil;
        System.out.println("se ejecuta A.S." + as );
        mapa.get(as).ejecutar();
    }
}