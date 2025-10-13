import java.io.IOException;
import java.util.ArrayList;

public class Compilador {
    public static ArrayList<String> erroresDetectados = new ArrayList<>();
    public static ArrayList<String> warningsDetectados = new ArrayList<>();
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
        analizadorLexico.printTablaSimbolos();
        parser.printEstructuras();
        compilador.printWarnings();
        compilador.printErrores();
    }

}