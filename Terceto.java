public class Terceto {
    private String operacion;
    private String val2;
    private String val3;

    public Terceto(String operacion, String variable, String valor) {
        this.operacion = operacion;
        this.val2 = variable;
        this.val3 = valor;
    }

    public void print(){
        System.out.println(Colores.MAGENTA+"(" + operacion + ", " + val2 + ", " + val3 + ")"+Colores.RESET);
    }

    public void setValor2(String variable){
        this.val2 =variable;
    }

    public void setValor3(String valor){
        this.val3 =valor;
    }
}
