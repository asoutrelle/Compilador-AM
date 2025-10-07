import java.io.IOException;
import java.util.ArrayList;

public class Main {
    static ArrayList<String> estructurasDetectadas = new ArrayList<>();
    public static void main(String[] args) throws IOException {

        /*if (args.length < 1) {
            System.out.println("Uso: java LeerArchivo <ruta-del-archivo.txt>");
            return;
        }*/


        int[][] matriz_estados = CSVtoMatrix.CsvToMatrix("MatrizEstado - Hoja 1.csv");
        StateMatrix matrisEstados = new StateMatrix(matriz_estados);
        AnalizadorLexico analizadorLexico = new AnalizadorLexico();

        int[][] matriz_as = CSVtoMatrix.CsvToMatrix("MatrizAS - Hoja 1.csv");
        ASMatrix matrizas = new ASMatrix(matriz_as, analizadorLexico);
        AnalizadorLexico lexer = new AnalizadorLexico();

        Parser parser = new Parser(true);
        parser.yyparse();
        lexer.print();
        System.out.println(Colores.AZUL+"------------------ ESTRUCTURAS DETECTADAS ------------------"+Colores.RESET);
        for(String str : estructurasDetectadas){
            System.out.println(str);
        }
    }

}