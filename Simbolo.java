public class Simbolo {
    private Token token;
    private String tipo = "";
    private String valor;
    private String uso;
    String semantica;

    public Simbolo(String valor, Token token, String tipo, String uso) {
        this.valor = valor;
        this.token = token;
        this.tipo = tipo;
        this.uso = uso;
    }

    public Simbolo(String valor, Token token, String tipo, String uso, String semantica) {
        this.valor = valor;
        this.token = token;
        this.tipo = tipo;
        this.uso = uso;
        this.semantica=semantica;
    }

    public Simbolo(String valor, Token token, String tipo) {
        this.valor = valor;
        this.token = token;
        this.tipo = tipo;
    }

    public Simbolo(String valor, Token token) {
        this.valor = valor;
        this.token = token;
    }
    public Simbolo(String valor, String tipo, String uso) {
        this.valor = valor;
        this.tipo = tipo;
        this.uso = uso;
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
    public void setUso(String uso) {
        this.uso = uso;
    }
    public String getUso() {
        return uso;
    }
    public void setSemantica(String semantica){this.semantica = semantica;}
    public String getSemantica(){return semantica;}
}
