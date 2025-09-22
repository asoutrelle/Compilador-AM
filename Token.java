public class Token {
    private int lexema;
    private int nroLinea;

    public Token(int lexema, int nroLinea) {
        this.lexema=lexema;
        this.nroLinea=nroLinea;
    }

    public int getLexema(){
        return lexema;
    }
}
