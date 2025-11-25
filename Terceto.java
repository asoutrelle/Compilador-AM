public class Terceto {
    private String operacion;
    private String val1;
    private String val2;
    private int indice;
    private boolean marcado = false;

    public Terceto(String operacion, String variable, String valor, int indice) {
        this.operacion = operacion;
        this.val1 = variable;
        this.val2 = valor;
        this.indice = indice;
    }
    public Terceto(String operacion, String variable, String valor) {
        this.operacion = operacion;
        this.val1 = variable;
        this.val2 = valor;
    }

    public void print1(){
        System.out.println(Colores.MAGENTA+"(" + operacion + ", " + val1 + ", " + val2 + " )"+Colores.RESET);
    }
    public void print2(){
        String valor2 = val1.split(":")[0];
        String valor3 = val2.split(":")[0];
        System.out.println(Colores.MAGENTA+"(" + operacion + ", " + valor2 + ", " + valor3 + " )"+Colores.RESET);
    }

    public void setValor1(String variable){
        this.val1 =variable;
    }

    public void setValor2(String valor){
        this.val2 =valor;
    }

    public boolean estaMarcado() {
        return marcado;
    }

    public void setMarcado(boolean marcado) {
        this.marcado = marcado;
    }

    public boolean isMarcado() {
        return marcado;
    }

    public String getVal2() {
        return val2;
    }

    public String getVal1() {
        return val1;
    }

    public String getOperacion() {
        return operacion;
    }
    public int getIndice() {
        return indice;
    }
}
