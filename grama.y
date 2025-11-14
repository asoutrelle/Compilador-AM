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
    : ID '{' lista_sentencia '}' {Compilador.salirAmbito();}
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
        int t = crearTerceto(":=", $1.sval, $3.sval);
    }
    ;


exp
    : exp '+' termino
    {
        int t = crearTerceto("+", $1.sval, $3.sval);

        $$=new ParserVal("[" + t + "]");
    }
    | exp '-' termino
    {
        int t = crearTerceto("-", $1.sval, $3.sval);
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
        int t = crearTerceto("*", $1.sval, $3.sval);
        $$=new ParserVal("[" + t + "]");
    }
    | termino '/' factor
    {
        int t = crearTerceto("/", $1.sval, $3.sval);
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
    | CTE {$$=$1;}
    | exp_lambda {addEstructura("lambda"); $$=$1;}
    | punto_flotante {$$=$1;}
    ;

punto_flotante
    : PF64 {check_rango($1.sval);}
    | '-' PF64 {check_rango("-"+$2.sval);}
    ;

variable
    : ID '.' ID {$$= new ParserVal($1.sval + '.' + $3.sval);}
    | ID {$$=$1;}
    ;

invocacion
    : ID '(' parametros_de_invocacion ')' {addEstructura("invocacion a funcion"); $$=$1;}
    ;

parametros_de_invocacion
    : parametro_real FLECHA ID
    | parametro_real FLECHA {yyerror("Falta parametro formal");}
    | parametro_real {yyerror("Falta flecha y parametro formal");}
    | parametros_de_invocacion ',' parametro_real FLECHA ID
    | parametros_de_invocacion ',' parametro_real FLECHA {yyerror("Falta parametro formal");}
    | parametros_de_invocacion ',' parametro_real {yyerror("Falta flecha y parametro formal");}
    | error {yyerror("error en parametros de invocacion");}
    ;

salida_msj
    : PRINT '(' argumento_print ')' punto_coma
    {
        int t = crearTerceto("print", $3.sval, "-");
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
    : IF cuerpo_condicion nuevo_ambito cuerpo_sentencia_ejecutable ELSE nuevo_ambito cuerpo_sentencia_ejecutable ENDIF punto_coma
    {
        int t = crearTerceto("BF", $2.sval, "salto a ");
        $$=new ParserVal("[" + t + "]");
    }
    | IF cuerpo_condicion nuevo_ambito cuerpo_sentencia_ejecutable ELSE ENDIF punto_coma {yyerror("no hay sentencias en else");}
    | IF cuerpo_condicion nuevo_ambito cuerpo_sentencia_ejecutable ENDIF punto_coma
    {
        int t = crearTerceto("BF", $2.sval, "salto a ");
        $$=new ParserVal("[" + t + "]");
    }
    /*sin endif*/
    | IF cuerpo_condicion nuevo_ambito cuerpo_sentencia_ejecutable ELSE nuevo_ambito cuerpo_sentencia_ejecutable punto_coma { yyerror("falta endif");}
    | IF cuerpo_condicion nuevo_ambito cuerpo_sentencia_ejecutable ELSE  punto_coma { yyerror("falta endif");yyerror("no hay sentencias en else");}
    | IF cuerpo_condicion nuevo_ambito cuerpo_sentencia_ejecutable punto_coma { yyerror("falta endif");}

    /*con endif, sin then*/
    | IF cuerpo_condicion ELSE nuevo_ambito cuerpo_sentencia_ejecutable ENDIF punto_coma {yyerror("no hay sentencias en then");}
    | IF cuerpo_condicion ELSE  ENDIF punto_coma {yyerror("no hay sentencias en then");yyerror("no hay sentencias en else");}
    | IF cuerpo_condicion ENDIF punto_coma {yyerror("no hay sentencias en then");}

    /*sin endif, sin then*/
    | IF cuerpo_condicion  ELSE nuevo_ambito cuerpo_sentencia_ejecutable punto_coma {yyerror("no hay sentencias en then");yyerror("falta endif");}
    | IF cuerpo_condicion  ELSE punto_coma {yyerror("no hay sentencias en then");yyerror("no hay sentencias en else");yyerror("falta endif");}
    | IF cuerpo_condicion  punto_coma {yyerror("no hay sentencias en then");yyerror("falta endif");}
    ;

cuerpo_condicion
    : '(' exp comparador exp ')'
    {
        int t = crearTerceto($3.sval, $2.sval, $4.sval);
        $$=new ParserVal("[" + t + "]");
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
    : ID {TablaDeSimbolos.addAmbito($1.sval, Compilador.getAmbito());}
    | lista_variables_declaracion ',' ID
    | lista_variables_declaracion ID {yyerror("falta , en lista de variables");}
    ;

/* -------------- TRATADO DE FUNCIONES -------------- */

funcion
    : tipo ID '(' parametros_formales ')' {Compilador.entrarAmbito($2.sval);} '{' lista_sentencia_funcion '}' {Compilador.salirAmbito();}
    | tipo '(' parametros_formales ')' '{' lista_sentencia_funcion '}' { yyerror("falta declarar nombre de funcion");}
    | tipo ID '(' ')' '{' lista_sentencia_funcion '}' {yyerror("faltan parametros formales");}
    | tipo '(' ')' '{' lista_sentencia_funcion '}' {yyerror("falta declarar nombre de funcion"); yyerror("faltan parametros formales");}
    ;

parametros_formales
    : CR tipo ID
    | tipo ID
    | parametros_formales ',' CR tipo ID
    | parametros_formales ',' tipo ID
    | error {yyerror("error en parametro formal");}
    ;

tipo
    : UINT
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
        int t = crearTerceto("return", $3.sval, "-");
        $$=new ParserVal("[" + t + "]");
        }
    ;


parametro_real
    : exp
    ;

/*--------------------------------------------------------------------------------*/


/* ------------------------------------------ DO WHILE ------------------------------------------ */
do
    : DO nuevo_ambito cuerpo_sentencia_ejecutable WHILE cuerpo_condicion punto_coma
    | DO nuevo_ambito WHILE cuerpo_condicion punto_coma {yyerror("falta cuerpo sentencias");}
    | DO nuevo_ambito cuerpo_sentencia_ejecutable WHILE cuerpo_condicion {yyerror("falta de ;");}

    | DO nuevo_ambito cuerpo_sentencia_ejecutable cuerpo_condicion punto_coma { yyerror("falta while");}
    | DO nuevo_ambito cuerpo_condicion punto_coma {yyerror("falta cuerpo sentencias"); yyerror("falta while");}
    | DO nuevo_ambito cuerpo_sentencia_ejecutable cuerpo_condicion {yyerror("falta de ) en condicion");yyerror("falta de ;");yyerror("falta while");}
    | DO nuevo_ambito cuerpo_condicion {yyerror("falta de ) en condicion");yyerror("falta de ;");yyerror("falta cuerpo sentencias");yyerror("falta while");}
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
        int t = crearTerceto("=", $1.sval, $3.sval);
        $$=new ParserVal("[" + t + "]");
    }
    ;

lista_variables
    : variable {$$=$1;}
    | lista_variables ',' variable {$$=new ParserVal("{"+$1.sval + ", " + $3.sval+"}");}
    | lista_variables variable {yyerror("falta , en lista de variables");}
    ;

lista_constantes
    : CTE {$$=$1;}
    | lista_constantes ',' CTE {$$=new ParserVal("{"+$1.sval + ", " + $3.sval+"}");}
    | lista_constantes CTE {yyerror("falta , en lista de constantes");}
    ;




%%
private ArrayList<Token> tokens = new ArrayList<>();
private ArrayList<String> estructurasDetectadas = new ArrayList<>();
private int cantUnidadesAnonimas = 1;



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
    System.out.println(Colores.AZUL+"------------------ ESTRUCTURAS DETECTADAS ------------------"+Colores.RESET);
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

  int crearTerceto(String operacion, String variable, String valor){
    Compilador.tercetos.add(new Terceto(operacion, variable, valor));
    return Compilador.tercetos.size() - 1;
  }

