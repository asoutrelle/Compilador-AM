import java.io.IOException;
import java.util.ArrayList;

public class Compilador {
    static ArrayList<String> estructurasDetectadas = new ArrayList<>();
    public static void main(String[] args) throws IOException {

        if (args.length < 1) {
            System.out.println("Uso: java Compilador <ruta-de-codigo-fuente.txt>");
            return;
        }


        int[][] matriz_estados = CSVtoMatrix.CsvToMatrix("MatrizEstado - Hoja 1.csv");
        StateMatrix matrisEstados = new StateMatrix(matriz_estados);
        AnalizadorLexico analizadorLexico = new AnalizadorLexico(args[0]);

        int[][] matriz_as = CSVtoMatrix.CsvToMatrix("MatrizAS - Hoja 1.csv");
        ASMatrix matrizas = new ASMatrix(matriz_as, analizadorLexico);

        Parser parser = new Parser();
        System.out.println(Colores.VERDE+"------------------ LEXEMAS DETECTADOS ------------------"+Colores.RESET);
        int parserval= parser.yyparse();
        if(parserval == 0){
            System.out.println(Colores.AMARILLO+"PARSER TERMINO CORRECTAMENTE "+Colores.RESET);
        } else{
            System.out.println(Colores.AMARILLO+"PARSER TERMINO MAL "+Colores.RESET);
        }
        analizadorLexico.print();
        System.out.println(Colores.AZUL+"------------------ ESTRUCTURAS DETECTADAS ------------------"+Colores.RESET);
        for(String str : estructurasDetectadas){
            System.out.println(str);
        }
    }

}