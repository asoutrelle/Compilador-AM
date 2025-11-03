import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class StateMatrix {
    public static int estado_actual = 0;
    private static int[][] matrix;
    private static Map<Character, Integer> mapa = new HashMap<>();

    public StateMatrix(int[][] matriz) {
        StateMatrix.matrix = matriz;

        for (char c = 'A'; c <= 'Z'; c++) {
            mapa.put(c, 0);
        }
        for (char c = 'a'; c <= 'z'; c++) {
            mapa.put(c, 1);
        }
        for (char c = '0'; c <= '9'; c++) {
            mapa.put(c, 2);
        }
        mapa.put('%', 3);
        mapa.put('.', 4);
        mapa.put(':', 5);
        mapa.put('=', 6);
        mapa.put('<', 7);
        mapa.put('>', 8);
        mapa.put('"', 9);
        mapa.put('#', 10);
        mapa.put(' ', 11);
        mapa.put('!', 12);
        mapa.put('+', 16);
        mapa.put('-', 17);
        mapa.put('*', 18);
        mapa.put('/', 19);
        mapa.put('(', 20);
        mapa.put(')', 21);
        mapa.put('{', 22);
        mapa.put('}', 23);
        mapa.put('_', 24);
        mapa.put(';', 25);
        mapa.put('\n', 26);
        mapa.put('\r', 26);
        mapa.put(',', 28);
        mapa.put('\uFFFF', 30);
    }

    public static Token siguiente_estado(char caracter, int nroLinea) throws IOException {
        Token tokenEncontrado = null;
        if (!mapa.containsKey(caracter)) {
            System.out.println("No se encuentra el estado de la matriz");
            tokenEncontrado = ASMatrix.ejecutarAccion(estado_actual, 29, caracter, nroLinea);
            estado_actual = matrix[estado_actual][29];
        } else {
            int col = mapa.get(caracter);
            if (estado_actual == 7 && caracter == 'U') {
                tokenEncontrado = ASMatrix.ejecutarAccion(estado_actual, 13, caracter, nroLinea);
                estado_actual = matrix[estado_actual][13];
            } else if (estado_actual == 8 && caracter == 'I') {
                tokenEncontrado = ASMatrix.ejecutarAccion(estado_actual, 14, caracter, nroLinea);
                estado_actual = matrix[estado_actual][14];
            } else if ((estado_actual == 10 || estado_actual == 11) && caracter == 'D') {
                tokenEncontrado = ASMatrix.ejecutarAccion(estado_actual, 15, caracter, nroLinea);
                estado_actual = matrix[estado_actual][15];
            } else {
                tokenEncontrado = ASMatrix.ejecutarAccion(estado_actual, col, caracter, nroLinea);
                estado_actual = matrix[estado_actual][col];
            }
        }
        if(estado_actual < 0){
            estado_actual = 0;
        }
        //System.out.println("estado actual: " + estado_actual);
        return tokenEncontrado;
    }

    public int getDimColm() {
        return matrix[0].length;
    }
    public int getDimfil() {
        return matrix.length;
    }
    public static Map<Character, Integer> getMapa() {
        return mapa;
    }
}