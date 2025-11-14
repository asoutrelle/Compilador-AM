public class Simbolo {
    private Token token;
    private String tipo = "";
    private StringBuilder ambito;
    private String valor;

    public Simbolo(String valor, Token token, String tipo) {

        ambito = new StringBuilder();
        this.valor = valor + ambito;
        this.token = token;
        this.tipo = tipo;
    }

    public Simbolo(String valor, Token token) {
        ambito = new StringBuilder();
        this.valor = valor + ambito;
        this.token = token;
    }

    public void setAmbito(String ambito) {
        this.ambito = new StringBuilder(ambito);
    }

    public Token getToken() {
        return token;
    }

    public String getTipo() {
        return tipo;
    }

    public String getValor() {
        return valor;
    }
    public String getAmbito(){
        return ambito.toString();
    }
}
