import java.util.ArrayList;

public class Terceto {
    private String operacion;
    private String variable;
    private String valor;

    public Terceto(String operacion, String variable, String valor) {
        this.operacion = operacion;
        this.variable = variable;
        this.valor = valor;
    }

    public void print(){
        System.out.println(Colores.MAGENTA+"(" + operacion + ", " + variable + ", " + valor + ")"+Colores.RESET);
    }

    public void setValor2(String valor){
        variable=valor;
    }
}
