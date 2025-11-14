import java.io.IOException;
import java.util.ArrayList;

public class Compilador {
    public static ArrayList<String> erroresDetectados = new ArrayList<>();
    public static ArrayList<String> warningsDetectados = new ArrayList<>();
    public static ArrayList<Terceto> tercetos = new ArrayList<>();
    public static ArrayList<Integer> pilaSaltos = new ArrayList<>();
    public static String ambitoActual = "global";
    public static ArrayList<String> pilaAmbitos = new ArrayList<>();

    public static void entrarAmbito(String nombre) {
        pilaAmbitos.add(nombre);
        ambitoActual = nombre;
    }

    public static void salirAmbito() {
        if (!pilaAmbitos.isEmpty()) {
            pilaAmbitos.remove(pilaAmbitos.size() - 1); // elimina el último
        }
        if (!pilaAmbitos.isEmpty()) {
            ambitoActual = pilaAmbitos.get(pilaAmbitos.size() - 1); // obtiene el último
        } else {
            ambitoActual = "global";
        }
    }

    public static String getAmbito(){
        if (pilaAmbitos.isEmpty()) {
            return "global";
        }
        return String.join(":", pilaAmbitos);
    }

    public Compilador (){

    }
    public static void addError(String error){
        String str = Colores.ROJO + error + Colores.RESET;
        erroresDetectados.add(str);
    }
    public static void addWarning(String war){
        String str = Colores.AMARILLO + war + Colores.RESET;
        erroresDetectados.add(str);
    }
    public void printWarnings(){
        if(!warningsDetectados.isEmpty()) {
            System.out.println(Colores.AMARILLO + "---------------- WARNINGS DETECTADOS ----------------" + Colores.RESET);
            for (String warning : warningsDetectados) {
                System.out.println(warning);
            }
        }
    }
    public void printErrores(){
        if(erroresDetectados.size() > 0) {
            System.out.println(Colores.ROJO + "---------------- ERRORES DETECTADOS ----------------" + Colores.RESET);
            for (String err : erroresDetectados) {
                System.out.println(err);
            }
        }
    }
    public void printTercetos(){
        System.out.println(Colores.MAGENTA + "---------------- TERCETOS DETECTADOS ----------------" + Colores.RESET);
        if(!tercetos.isEmpty()) {
            for(int i = 0; i < tercetos.size(); i++){
                System.out.print(Colores.MAGENTA+i+"."+Colores.RESET);
                tercetos.get(i).print();
            }
        } else System.out.println(Colores.MAGENTA +"NO HAY TERCETOS"+ Colores.RESET);
    }

    public static void main(String[] args) throws IOException {

        if (args.length < 1) {
            System.out.println("Uso: java Compilador <ruta-de-codigo-fuente.txt>");
            return;
        }
        Compilador compilador = new Compilador();

        int[][] matriz_estados = CSVtoMatrix.CsvToMatrix("MatrizEstado - Hoja 1.csv");
        StateMatrix matrisEstados = new StateMatrix(matriz_estados);
        AnalizadorLexico analizadorLexico = new AnalizadorLexico(args[0]);

        int[][] matriz_as = CSVtoMatrix.CsvToMatrix("MatrizAS - Hoja 1.csv");
        ASMatrix matrizas = new ASMatrix(matriz_as, analizadorLexico);

        Parser parser = new Parser();
        int parserval= parser.yyparse();
        if(parserval == 0){
            System.out.println(Colores.AMARILLO+"PARSER TERMINO CORRECTAMENTE "+Colores.RESET);
        } else{
            System.out.println(Colores.AMARILLO+"PARSER TERMINO MAL "+Colores.RESET);
        }
        analizadorLexico.printTokensDetectados();
        TablaDeSimbolos.imprimir();
        parser.printEstructuras();
        compilador.printWarnings();
        compilador.printErrores();
        compilador.printTercetos();
    }

}