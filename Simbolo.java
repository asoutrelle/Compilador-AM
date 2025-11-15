public class Simbolo {
    private Token token;
    private String tipo = "";
    private String valor;

    public Simbolo(String valor, Token token, String tipo) {
        this.valor = valor;
        this.token = token;
        this.tipo = tipo;
    }

    public Simbolo(String valor, Token token) {
        this.valor = valor;
        this.token = token;
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
    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
}
