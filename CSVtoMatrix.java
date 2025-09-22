import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class CSVtoMatrix {
    private static int fil;
    private static int col;
    public static int[][] CsvToMatrix(String ruta) {
        try (BufferedReader br = new BufferedReader(new FileReader(ruta))) {
            // Leemos todas las líneas primero
            String linea;
            java.util.List<String[]> lineas = new java.util.ArrayList<>();
            while ((linea = br.readLine()) != null) {
                // Separar por comas
                String[] columnas = linea.split(",");
                lineas.add(columnas);
            }

            int filas = lineas.size() - 1;                  // quitamos la primera fila
            int columnas = lineas.get(0).length - 1;        // quitamos la primera columna
            int[][] matriz = new int[filas][columnas];

            for (int i = 1; i < lineas.size(); i++) {       // empezamos desde fila 1
                for (int j = 1; j < lineas.get(i).length; j++) {  // empezamos desde col 1
                    String valor = lineas.get(i)[j].trim()
                            .replaceAll("\"", "")
                            .replace("\uFEFF", "");
                    switch (valor.toUpperCase()) {
                        case "ERROR":
                            matriz[i - 1][j - 1] = -2;
                            break;
                        case "F":
                            matriz[i - 1][j - 1] = -1;
                            break;
                        default:
                            try {
                                matriz[i - 1][j - 1] = Integer.parseInt(valor);
                            } catch (NumberFormatException e) {
                                matriz[i - 1][j - 1] = 0; // valor inesperado
                            }
                    }
                }
            }

            fil = filas;
            col = columnas;


            // Mostrar la matriz
//            for (int[] fila : matriz) {
//                for (int val : fila) {
//                    System.out.print(val + "\t");
//                }
//                System.out.println();
//            }
            fil = filas;
            col = columnas;
            return matriz;

        } catch (IOException e) {
            e.printStackTrace();
        }
        return new int[0][0];
    }
    public int getFil(){
        return fil;
    }
    public int getCol(){
        return col;
    }
}
