%{
import java.lang.Math;
import java.io.*;
import java.util.StringTokenizer;
import java.util.ArrayList;
import java.math.BigDecimal;
%}

%token ID CTE PF64 ELSE ENDIF PRINT RETURN CADENA
%token DFLOAT DO WHILE ASIG IF IGUAL NOIGUAL
%token MENORIGUAL MAYORIGUAL FLECHA UINT CR TRUNC


%%
prog
    : ID '{' lista_sentencia '}'
    {
        Compilador.salirAmbito();
        if(!TablaDeSimbolos.checkVar($1.sval, Compilador.getAmbito(), "", "nombre de programa")){
           yyerror("La variable "+$1.sval+" ya fue declarada");
        }
    }
    | '{' lista_sentencia '}' {yyerror("falta nombre de programa");}
    | ID '{' lista_sentencia  {yyerror("falta llave de cierre de programa");}
    | ID lista_sentencia '}' {yyerror("falta llave de inicio de programa");}
    | '{' lista_sentencia  {yyerror("falta nombre del programa");yyerror("falta llave de cierre de programa");}
    | error lista_sentencia '}' {yyerror("falta nombre del programa");yyerror("falta llave de inicio de programa");}
    | ID lista_sentencia {yyerror("no hay llaves del programa");}

    | ID '{' error '}' {yyerror("error en programa");}
    | '{' error '}' {yyerror("falta nombre de programa");yyerror("error en programa");}
    | ID '{' error  {yyerror("falta llave de cierre de programa");yyerror("error en programa");}
    | ID error '}' {yyerror("falta llave de inicio de programa");yyerror("error en programa");}
    | '{' error  {yyerror("falta nombre del programa");yyerror("falta llave de cierre de programa");yyerror("error en programa");}
    | error '}' {yyerror("falta nombre del programa");yyerror("error en programa");}
    | ID error {yyerror("no hay llaves del programa");yyerror("error en programa");}
    ;
lista_sentencia
    : sentencia
    | lista_sentencia sentencia
    | error ';' {yyerror("error en sentencia");}
    ;

sentencia
    : sentencia_ejecutable
    | sentencia_declarativa
    | sentencia_de_control
    ;

sentencia_declarativa
    : declaracion {addEstructura("declaracion de variable");}
    | funcion {addEstructura("declaracion de funcion");}
    ;

sentencia_ejecutable
    : asig {addEstructura("asignacion");}
    | if {addEstructura("sentencia if");}
    | salida_msj {addEstructura("print");}
    | return {addEstructura("return");}
    | asig_multiple {addEstructura("asignacion multiple");}
    ;

sentencia_de_control
    : do {addEstructura("sentencia do while");}
    ;

asig
    : variable ASIG exp punto_coma
    {
        crearTerceto(":=", $1.sval, $3.sval);
    }
    ;


exp
    : exp '+' termino
    {
        crearTerceto("+", $1.sval, $3.sval);
        int t = Compilador.tercetos.size() - 1;
        $$=new ParserVal("[" + t + "]");
    }
    | exp '-' termino
    {
        crearTerceto("-", $1.sval, $3.sval);
        int t = Compilador.tercetos.size() - 1;
        $$=new ParserVal("[" + t + "]");
    }
    | '+' termino {yyerror("falta operando a izquierda de +");}
    | exp '+' error {yyerror("falta operando a derecha de +");}
    | exp '-' error {yyerror("falta operando a derecha de -");}
    | termino {$$=$1;}
    | TRUNC '(' exp ')' {addEstructura("trunc");}
    | TRUNC  exp ')' {addEstructura("trunc");yyerror("falta abrir parentesis en trunc");}
    | TRUNC '(' error {addEstructura("trunc");yyerror("falta cerrar parentesis en trunc");yyerrflag=0;}
    | TRUNC  '(' ')' {addEstructura("trunc");yyerror("falta argumento en trunc");}
    ;

termino
    : termino '*' factor
    {
        crearTerceto("*", $1.sval, $3.sval);
         int t = Compilador.tercetos.size() - 1;
        $$=new ParserVal("[" + t + "]");
    }
    | termino '/' factor
    {
        crearTerceto("/", $1.sval, $3.sval);
         int t = Compilador.tercetos.size() - 1;
        $$=new ParserVal("[" + t + "]");
    }
    | termino '/' error {yyerror("falta operando a derecha de /");}
    | termino '*' error {yyerror("falta operando a derecha de *");}
    | '/' factor {yyerror("falta operando a izquierda de /");}
    | '*' factor {yyerror("falta operando a izquierda de *");}
    | factor {$$=$1;}
    ;

factor
    : variable {$$=$1;}
    | invocacion {$$=$1;}
    | CTE {$$=new ParserVal($1.sval);}
    | exp_lambda {addEstructura("lambda"); $$=$1;}
    | punto_flotante {$$=$1;}
    ;

punto_flotante
    : PF64 {check_rango($1.sval); $$=new ParserVal($1.sval);}
    | '-' PF64 {check_rango("-"+$2.sval); $$=new ParserVal("-"+$1.sval);}
    ;

variable
    : ID '.' ID
    {
        $$ = new ParserVal($1.sval + '.' + $3.sval);

    }
    | ID
    {
        if(!TablaDeSimbolos.estaDeclarado($1.sval, Compilador.getAmbito())){
                    yyerror("La variable "+$1.sval+" no fue declarada");
                    TablaDeSimbolos.eliminar($1.sval);
                } else TablaDeSimbolos.eliminar($1.sval);
        $$=$1;

    }
    ;

invocacion
    : ID '(' parametros_de_invocacion ')'
    {
        addEstructura("invocacion a funcion");
        $$=new ParserVal($1.sval+"("+$3.sval+")");
    }
    ;

parametros_de_invocacion
    : parametro_real FLECHA ID
    {
        $$=new ParserVal( $1.sval );
    }
    | parametro_real FLECHA {yyerror("Falta parametro formal");}
    | parametro_real {yyerror("Falta flecha y parametro formal");}
    | parametros_de_invocacion ',' parametro_real FLECHA ID
    {
        $$ = new ParserVal($1.sval+","+$3.sval);
    }
    | parametros_de_invocacion ',' parametro_real FLECHA {yyerror("Falta parametro formal");}
    | parametros_de_invocacion ',' parametro_real {yyerror("Falta flecha y parametro formal");}
    | error {yyerror("error en parametros de invocacion");}
    ;

salida_msj
    : PRINT '(' argumento_print ')' punto_coma
    {
        crearTerceto("print", $3.sval, "-");
         int t = Compilador.tercetos.size() - 1;
        $$=new ParserVal("[" + t + "]");
    }
    | PRINT '('  ')' punto_coma {yyerror("falta argumento en print");}
    ;

punto_coma
    : ';'
    | error {yyerror("falta ;");}
    ;

argumento_print
    : exp
    | CADENA
    | error {yyerror("argumento invalido en print");}
    ;
/* -------------------------------------------------------- IF -------------------------------------------------------- */
if
    : IF cuerpo_condicion nuevo_ambito cuerpo_sentencia_ejecutable completar_bf_else ELSE nuevo_ambito crear_bi cuerpo_sentencia_ejecutable ENDIF punto_coma
    {
        int bi = Compilador.pilaSaltos.remove(Compilador.pilaSaltos.size()-1);
        completarBI(bi, Compilador.tercetos.size());
    }
    | IF cuerpo_condicion nuevo_ambito cuerpo_sentencia_ejecutable completar_bf_else ELSE ENDIF punto_coma {yyerror("no hay sentencias en else");}
    | IF cuerpo_condicion nuevo_ambito cuerpo_sentencia_ejecutable completar_bf ENDIF punto_coma

    /*sin endif*/
    | IF cuerpo_condicion nuevo_ambito cuerpo_sentencia_ejecutable completar_bf_else ELSE nuevo_ambito crear_bi cuerpo_sentencia_ejecutable punto_coma { yyerror("falta endif");}
    | IF cuerpo_condicion nuevo_ambito cuerpo_sentencia_ejecutable completar_bf_else ELSE  punto_coma { yyerror("falta endif");yyerror("no hay sentencias en else");}
    | IF cuerpo_condicion nuevo_ambito cuerpo_sentencia_ejecutable completar_bf punto_coma { yyerror("falta endif");}

    /*con endif, sin then*/
    | IF cuerpo_condicion ELSE nuevo_ambito crear_bi cuerpo_sentencia_ejecutable completar_bf ENDIF punto_coma {yyerror("no hay sentencias en then");}
    | IF cuerpo_condicion ELSE  ENDIF punto_coma {yyerror("no hay sentencias en then");yyerror("no hay sentencias en else");}
    | IF cuerpo_condicion ENDIF punto_coma {yyerror("no hay sentencias en then");}

    /*sin endif, sin then*/
    | IF cuerpo_condicion  ELSE nuevo_ambito crear_bi cuerpo_sentencia_ejecutable punto_coma {yyerror("no hay sentencias en then");yyerror("falta endif");}
    | IF cuerpo_condicion  ELSE punto_coma {yyerror("no hay sentencias en then");yyerror("no hay sentencias en else");yyerror("falta endif");}
    | IF cuerpo_condicion  punto_coma {yyerror("no hay sentencias en then");yyerror("falta endif");}
    ;
completar_bf_else
    :
    {
        int bf = Compilador.pilaSaltos.remove(Compilador.pilaSaltos.size()-1);
        completarBF(bf, Compilador.tercetos.size()+1);
    }
    ;
completar_bf
    :
    {
        int bf = Compilador.pilaSaltos.remove(Compilador.pilaSaltos.size()-1);
        completarBF(bf, Compilador.tercetos.size());
    }
    ;
crear_bi
    : {
        crearTerceto("BI", "-", "-");
         int bi = Compilador.tercetos.size() - 1;
        Compilador.pilaSaltos.add(bi);}
    ;
cuerpo_condicion
    : '(' exp comparador exp ')'
    {
        crearTerceto($3.sval, $2.sval, $4.sval);
         int t = Compilador.tercetos.size() - 1;
        crearTerceto("BF", "["+t+"]", "-");
         int bf = Compilador.tercetos.size() - 1;
        Compilador.pilaSaltos.add(bf);
        $$ = new ParserVal("[" + t + "]");
    }
    | '(' exp comparador exp {yyerror("falta cerrar parentesis");}
    |  exp comparador exp ')' {yyerror("falta abrir parentesis");}
    |  exp comparador exp {yyerror("faltan parentesis");}
    ;


comparador
    : IGUAL {$$=new ParserVal("==");}
    | NOIGUAL {$$=new ParserVal("=!");}
    | MENORIGUAL {$$=new ParserVal("<=");}
    | MAYORIGUAL {$$=new ParserVal(">=");}
    | '<' {$$=new ParserVal("<");}
    | '>' {$$=new ParserVal(">");}
    | error {yyerror("en condicion");}
    ;


lista_sentencia_ejecutable
    : sentencia_ejecutable
    | lista_sentencia_ejecutable sentencia_ejecutable
    ;

/* -------------- FIN IF -------------- */
declaracion
    : tipo lista_variables_declaracion punto_coma
    ;


lista_variables_declaracion
    : ID
    {
        String ambito = Compilador.getAmbito();
        if(!TablaDeSimbolos.checkVar($1.sval, ambito, tipo, "nombre de variable")){
           yyerror("La variable "+$1.sval+" ya fue declarada");
        }

    }
    | lista_variables_declaracion ',' ID
    {
        $$ = new ParserVal($1.sval+","+$3.sval);
        String ambito = Compilador.getAmbito();
        if(!TablaDeSimbolos.checkVar($3.sval, ambito, tipo, "nombre de variable")){
           yyerror("La variable "+$3.sval+" ya fue declarada");
        }
    }
    | lista_variables_declaracion ID {yyerror("falta , en lista de variables");}
    ;

/* -------------- TRATADO DE FUNCIONES -------------- */
funcion
    : tipo ID '(' {Compilador.entrarAmbito($2.sval);} parametros_formales ')' {
        String ambito = Compilador.getAmbito();
        if(!TablaDeSimbolos.checkVar($2.sval, ambito, tipo, "nombre de funcion")){
            yyerror("La funcion "+$2.sval+" ya fue declarada");
        }
        crearTerceto("inicio de funcion", $2.sval, "-");
    }
    '{' lista_sentencia_funcion '}' {
        crearTerceto("fin de funcion", $2.sval, "-");
        Compilador.salirAmbito();
    }
    | tipo '(' parametros_formales ')' '{' lista_sentencia_funcion '}' { yyerror("falta declarar nombre de funcion");}
    | tipo ID '(' ')' '{' lista_sentencia_funcion '}' {yyerror("faltan parametros formales");}
    | tipo '(' ')' '{' lista_sentencia_funcion '}' {yyerror("falta declarar nombre de funcion"); yyerror("faltan parametros formales");}
    ;

parametros_formales
    : CR tipo ID
    {
        String ambito = Compilador.getAmbito();
                if(!TablaDeSimbolos.checkVar($3.sval, ambito, tipo, "nombre de parametro")){
                   yyerror("La variable "+$3.sval+" ya fue declarada");
                }
    }
    | tipo ID
    {
        String ambito = Compilador.getAmbito();
                if(!TablaDeSimbolos.checkVar($2.sval, ambito, tipo, "nombre de parametro")){
                   yyerror("La variable "+$2.sval+" ya fue declarada");
                }
    }
    | parametros_formales ',' CR tipo ID
    | parametros_formales ',' tipo ID
    | error {yyerror("error en parametro formal");}
    ;

tipo
    : UINT {tipo = "uint";}
    ;


lista_sentencia_funcion
    : sentencia_funcion
    | lista_sentencia_funcion sentencia_funcion
    ;

sentencia_funcion
    : sentencia_declarativa
    | sentencia_ejecutable
    ;

return
    : RETURN '(' exp ')' punto_coma
    {
        addEstructura("return");
        crearTerceto("return", $3.sval, "-");
         int t = Compilador.tercetos.size() - 1;
        $$=new ParserVal("[" + t + "]");
        }
    ;


parametro_real
    : exp
    ;

/*--------------------------------------------------------------------------------*/


/* ------------------------------------------ DO WHILE ------------------------------------------ */
do
    : DO nuevo_ambito inicio_while cuerpo_sentencia_ejecutable WHILE cuerpo_condicion crear_bi_while completar_bf punto_coma
    | DO nuevo_ambito inicio_while WHILE cuerpo_condicion punto_coma {yyerror("falta cuerpo sentencias");}
    | DO nuevo_ambito inicio_while cuerpo_sentencia_ejecutable WHILE cuerpo_condicion {yyerror("falta de ;");}

    | DO nuevo_ambito inicio_while cuerpo_sentencia_ejecutable cuerpo_condicion punto_coma { yyerror("falta while");}
    | DO nuevo_ambito inicio_while cuerpo_condicion punto_coma {yyerror("falta cuerpo sentencias"); yyerror("falta while");}
    | DO nuevo_ambito inicio_while cuerpo_sentencia_ejecutable cuerpo_condicion {yyerror("falta de ) en condicion");yyerror("falta de ;");yyerror("falta while");}
    | DO nuevo_ambito inicio_while  cuerpo_condicion {yyerror("falta de ) en condicion");yyerror("falta de ;");yyerror("falta cuerpo sentencias");yyerror("falta while");}
    ;
inicio_while
    : {Compilador.pilaSaltos.add(Compilador.tercetos.size());}
    ;
crear_bi_while
    : {
        int salto = Compilador.pilaSaltos.remove(0);
        crearTerceto("BI", "[" +salto+"]", "-");
    }
    ;
nuevo_ambito
    : {Compilador.entrarAmbito("ua"+cantUnidadesAnonimas); cantUnidadesAnonimas+= 1;}
    ;
cuerpo_sentencia_ejecutable
    : '{' lista_sentencia_ejecutable '}' {Compilador.salirAmbito();}
    | '{' '}' {yyerror("no hay sentencias dentro de las llaves");}
    | sentencia_ejecutable {Compilador.salirAmbito();}
    | '{' error '}' {yyerror("Error en sentencia");}
    ;

/* -------------- EXPRESIONES LAMBDA -------------- */
exp_lambda
    : '(' tipo ID ')' '{' lista_sentencia_ejecutable '}' argumento_lambda {$$= new ParserVal("soy un lamnda");}
    | '(' tipo ID ')'  lista_sentencia_ejecutable '}' argumento_lambda { yyerror("falta abrir llave en cuerpo de sentencia lambda");}
    | '(' tipo ID ')'  '{' lista_sentencia_ejecutable argumento_lambda {yyerror("falta cerra llave en cuerpo de sentencia lambda");}
    | '(' tipo ID ')'   lista_sentencia_ejecutable argumento_lambda { yyerror("faltan llaves en cuerpo de sentencia lambda");}
    ;


argumento_lambda
    : '(' ID ')'
    | '(' CTE ')'
    ;
/* -------------- ASIGNACION MULTIPLE -------------- */
asig_multiple
    : lista_variables '=' lista_constantes punto_coma
    {
        asigMultiple($1.sval,$3.sval);
    }
    ;

lista_variables
    : variable
    | lista_variables ',' variable {$$=new ParserVal($1.sval + "," + $3.sval);}
    | lista_variables variable {yyerror("falta , en lista de variables");}
    ;

lista_constantes
    : CTE {$$=$1;}
    | lista_constantes ',' CTE {$$=new ParserVal($1.sval + "," + $3.sval);}
    | lista_constantes CTE {yyerror("falta , en lista de constantes");}
    ;




%%
private ArrayList<Token> tokens = new ArrayList<>();
private ArrayList<String> estructurasDetectadas = new ArrayList<>();
private int cantUnidadesAnonimas = 1;
private String tipo = "";


private int yylex(){
    try {
      Token t = AnalizadorLexico.leerCaracter();
        tokens.add(t);
        int aux = t.getLexema();
        if(!AnalizadorLexico.valorTs.isEmpty()){
            yylval = new ParserVal(AnalizadorLexico.valorTs);
            AnalizadorLexico.valorTs = "";
        }
        return aux;
    } catch (IOException e) {
        throw new RuntimeException(e);
    }
}

private void yyerror(String err) {
    if ("syntax error".equals(err))
        return;

    String str ="ERROR LINEA " + nroLinea() + ": " + err;
    Compilador.addError(str);
}


private int nroLinea(){
  return tokens.get(tokens.size() - 1).getNroLinea();
}
private void addEstructura(String str){
  estructurasDetectadas.add(Colores.AZUL+str+Colores.RESET);
}

public void printEstructuras(){
    if (!estructurasDetectadas.isEmpty()){
    System.out.println(Colores.AZUL+"------------- ESTRUCTURAS DETECTADAS --------------"+Colores.RESET);
    for(String str : estructurasDetectadas){
        System.out.println(str);
    }
    }
}

public void check_rango(String valor){
    String valor_normalizado = valor.replace(" ", "")
            .replace("D", "E")
            .replace("+", "");
    BigDecimal numero = new BigDecimal(valor_normalizado);

    BigDecimal min = new BigDecimal("2.2250738585072014E-308");
    BigDecimal max = new BigDecimal("1.7976931348623157E308");
    BigDecimal cero = BigDecimal.ZERO;
    // chequeo de rango
    boolean enRango =
            numero.compareTo(cero) == 0 ||
                    (numero.compareTo(min) >= 0 && numero.compareTo(max) <= 0) ||
                    (numero.compareTo(min.negate()) <= 0 && numero.compareTo(max.negate()) >= 0);

    if (enRango) {
      Token t = new Token(PF64,nroLinea());
      TablaDeSimbolos.agregar(valor, t);
      yylval = new ParserVal(valor);
    } else {
      yyerror("NO está en el rango de un float 64 bits");
    }
  }

 private void crearTerceto(String operacion, String variable, String valor){
    Compilador.tercetos.add(new Terceto(operacion, variable, valor));
  }

private void asigMultiple(String var, String cte) {

    ArrayList<String> vars = new ArrayList<>();
    ArrayList<String> ctes = new ArrayList<>();

    for (String v : var.split(",")) {
        vars.add(v);
    }

    for (String c : cte.split(",")) {
        ctes.add(c);
    }

    while (!(vars.isEmpty() && ctes.isEmpty())){
    crearTerceto(":=", vars.remove(0), ctes.remove(0));
        if(vars.isEmpty()){
          if(!ctes.isEmpty()){
            yyWarning("Hay mas constantes que variables");
            break;
          }
        }
        if(!vars.isEmpty()){
          if(ctes.isEmpty()){
            yyerror("Hay mas variables que constantes");
            break;
          }
        }

    }
}


  private void completarBF(int index, int destino) {
        Terceto t = Compilador.tercetos.get(index);
        t.setValor3("[" + destino + "]");
    }

private void completarBI(int index, int destino) {
    Terceto t = Compilador.tercetos.get(index);
    t.setValor2("[" + destino + "]");
  }
  private void yyWarning(String war){
      String str ="WARNING LINEA " + nroLinea() + ": " + war;
  Compilador.addWarning(str);
  }