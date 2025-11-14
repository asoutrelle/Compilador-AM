public class Simbolo {
    private Token token;
    private String tipo;
    private String ambito;

    public Simbolo(Token token, String ambito) {
        this.token = token;
        this.ambito = ambito;
    }
    public Simbolo(Token token) {
        this.token = token;
    }

    public Token getToken() {
        return token;
    }

    public String getTipo() {
        return tipo;
    }

    public String getAmbito() {
        return ambito;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public void setAmbito(String ambito) {
        this.ambito = ambito;
    }
}
