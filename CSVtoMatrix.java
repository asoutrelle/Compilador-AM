import java.io.*;


public class CSVtoMatrix {
    private static int fil;
    private static int col;
    public static int[][] CsvToMatrix(String ruta) {
        try {
            // Abrir archivo desde resources dentro del JAR
            InputStream input = CSVtoMatrix.class.getResourceAsStream("/" + ruta);
            if (input == null) {
                throw new FileNotFoundException("No se encontró el archivo dentro del JAR: " + ruta);
            }

            BufferedReader br = new BufferedReader(new InputStreamReader(input));

            // Leemos todas las líneas primero
            String linea;
            java.util.List<String[]> lineas = new java.util.ArrayList<>();

            while ((linea = br.readLine()) != null) {
                String[] columnas = linea.split(",");
                lineas.add(columnas);
            }

            int filas = lineas.size() - 1;           // quitamos la primera fila
            int columnas = lineas.get(0).length - 1; // quitamos la primera columna
            int[][] matriz = new int[filas][columnas];

            for (int i = 1; i < lineas.size(); i++) { // empezamos desde fila 1
                for (int j = 1; j < lineas.get(i).length; j++) { // empezamos desde col 1
                    String valor = lineas.get(i)[j]
                            .trim()
                            .replaceAll("\"", "")
                            .replace("\uFEFF", ""); // quitar BOM si existe

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
            return matriz;

        } catch (Exception e) {
            e.printStackTrace();
            return new int[0][0];
        }
    }

    public int getFil(){
        return fil;
    }
    public int getCol(){
        return col;
    }
}
