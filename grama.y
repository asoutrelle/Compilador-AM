%{
import java.lang.Math;
import java.io.*;
import java.util.StringTokenizer;
import java.util.ArrayList;
%}

%token ID CTE PF64 ELSE ENDIF PRINT RETURN CADENA DFLOAT DO WHILE ASIG IF IGUAL NOIGUAL MENORIGUAL MAYORIGUAL FLECHA UINT CR TRUNC


%%
prog
    : ID '{' lista_sentencia '}'
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
    : declaracion
    | funcion
    ;

sentencia_ejecutable
    : asig
    | if
    | salida_msj
    | return
    | asig_multiple
    ;

sentencia_de_control
    : do
    ;

asig
    : variable ASIG exp punto_coma {print("asignacion");}
    ;


exp
    : exp '+' termino
    | exp '-' termino
    | '+' termino {yyerror("falta operando a izquierda de +");}
    | exp '+' error {yyerror("falta operando a derecha de +");}
    | exp '-' error {yyerror("falta operando a derecha de -");}
    | termino
    | TRUNC '(' exp ')' {print("trunc");}
    | TRUNC  exp ')' {print("trunc");yyerror("falta abrir parentesis en trunc");}
    | TRUNC '(' error {print("trunc");yyerror("falta cerrar parentesis en trunc");yyerrflag=0;}
    | TRUNC  '(' ')' {print("trunc");yyerror("falta argumento en trunc");}
    ;

termino
    : termino '*' factor
    | termino '/' factor
    | termino '/' error {yyerror("falta operando a derecha de /");}
    | termino '*' error {yyerror("falta operando a derecha de *");}
    | '/' factor {yyerror("falta operando a izquierda de /");}
    | '*' factor {yyerror("falta operando a izquierda de *");}
    | factor
    ;

factor
    : variable
    | invocacion
    | CTE
    | exp_lambda
    | punto_flotante
    ;

punto_flotante
    : PF64
    | '-' PF64 {TablaDeSimbolos.agregar("-"+$2.sval,new Token(PF64,nroLinea())); yylval = new ParserVal("-"+$2.sval);}
    ;


variable
    : ID '.' ID
    | ID
    ;

invocacion
    : ID '(' parametros_de_invocacion ')' {print("invocacion a funcion");}
    ;

parametros_de_invocacion
    : parametro_real FLECHA ID
    | parametro_real FLECHA {yyerror("Falta parametro formal");}
    | parametro_real {yyerror("Falta flecha y parametro formal");}
    | parametros_de_invocacion ',' parametro_real FLECHA ID
    | parametros_de_invocacion ',' parametro_real FLECHA {yyerror("Falta parametro formal");}
    | parametros_de_invocacion ',' parametro_real {yyerror("Falta flecha y parametro formal");}
    ;

salida_msj
    : PRINT '(' argumento_print ')' punto_coma
    | PRINT '('  ')' punto_coma {yyerror("falta argumento en print");}
    ;

punto_coma
    : ';'
    | error {yyerror("falta ;");}
    ;

argumento_print
    : exp
    | CADENA
    ;
/* -------------------------------------------------------- IF -------------------------------------------------------- */
if
    : IF cuerpo_condicion cuerpo_sentencia_ejecutable ELSE cuerpo_sentencia_ejecutable ENDIF punto_coma {print("sentencia if");}
    | IF cuerpo_condicion cuerpo_sentencia_ejecutable ELSE ENDIF punto_coma {print("sentencia if");yyerror("no hay sentencias en else");}
    | IF cuerpo_condicion cuerpo_sentencia_ejecutable ENDIF punto_coma {print("sentencia if");}
    /*sin endif*/
    | IF cuerpo_condicion cuerpo_sentencia_ejecutable ELSE cuerpo_sentencia_ejecutable punto_coma {print("sentencia if"); yyerror("falta endif");}
    | IF cuerpo_condicion cuerpo_sentencia_ejecutable ELSE  punto_coma {print("sentencia if"); yyerror("falta endif");yyerror("no hay sentencias en else");}
    | IF cuerpo_condicion cuerpo_sentencia_ejecutable punto_coma {print("sentencia if"); yyerror("falta endif");}

    /*con endif, sin then*/
    | IF cuerpo_condicion ELSE cuerpo_sentencia_ejecutable ENDIF punto_coma {print("sentencia if");yyerror("no hay sentencias en then");}
    | IF cuerpo_condicion ELSE  ENDIF punto_coma {print("sentencia if");yyerror("no hay sentencias en then");yyerror("no hay sentencias en else");}
    | IF cuerpo_condicion ENDIF punto_coma {print("sentencia if");yyerror("no hay sentencias en then");}

    /*sin endif, sin then*/
    | IF cuerpo_condicion ELSE cuerpo_sentencia_ejecutable punto_coma {print("sentencia if");yyerror("no hay sentencias en then");yyerror("falta endif");}
    | IF cuerpo_condicion ELSE punto_coma {print("sentencia if");yyerror("no hay sentencias en then");yyerror("no hay sentencias en else");yyerror("falta endif");}
    | IF cuerpo_condicion punto_coma {print("sentencia if");yyerror("no hay sentencias en then");yyerror("falta endif");}
    ;

cuerpo_condicion
    : '(' exp comparador exp ')'
    | '(' exp comparador exp {yyerror("falta cerrar parentesis");}
    |  exp comparador exp ')' {yyerror("falta abrir parentesis");}
    |  exp comparador exp {yyerror("faltan parentesis");}
    ;


comparador
    : IGUAL
    | NOIGUAL
    | MENORIGUAL
    | MAYORIGUAL
    | '<'
    | '>'
    | error {yyerror("falta comparador");}
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
    | lista_variables_declaracion ',' ID
    | lista_variables_declaracion ID {yyerror("falta , en lista de variables");}
    ;

/* -------------- TRATADO DE FUNCIONES -------------- */

funcion
    : tipo ID '(' parametros_formales ')' '{' lista_sentencia_funcion '}' {print("funcion");}
    | tipo '(' parametros_formales ')' '{' lista_sentencia_funcion '}' {print("funcion"); yyerror("falta declarar nombre de funcion");}
    | tipo ID '(' ')' '{' lista_sentencia_funcion '}' {yyerror("faltan parametros formales");}
    | tipo '(' ')' '{' lista_sentencia_funcion '}' {yyerror("falta declarar nombre de funcion"); yyerror("faltan parametros formales");}
    ;

parametros_formales
    : CR tipo ID
    | tipo ID
    | parametros_formales ',' CR tipo ID
    | parametros_formales ',' tipo ID
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
    : RETURN '(' exp ')' punto_coma {print("return");}
    ;


parametro_real
    : exp
   /* | exp_error */
    ;

/*--------------------------------------------------------------------------------*/


/* ------------------------------------------ DO WHILE ------------------------------------------ */
do
    : DO cuerpo_sentencia_ejecutable WHILE cuerpo_condicion punto_coma {print("sentencia do while");}
    | DO WHILE cuerpo_condicion punto_coma {print("sentencia do while");yyerror("falta cuerpo sentencias");}
    | DO cuerpo_sentencia_ejecutable WHILE cuerpo_condicion {print("sentencia do while");yyerror("falta de ;");}

    | DO cuerpo_sentencia_ejecutable cuerpo_condicion punto_coma {print("sentencia do while"); yyerror("falta while");}
    | DO cuerpo_condicion punto_coma {print("sentencia do while");yyerror("falta cuerpo sentencias"); yyerror("falta while");}
    | DO cuerpo_sentencia_ejecutable cuerpo_condicion {print("sentencia do while");yyerror("falta de ) en condicion");yyerror("falta de ;");yyerror("falta while");}
    | DO cuerpo_condicion {print("sentencia do while");yyerror("falta de ) en condicion");yyerror("falta de ;");yyerror("falta cuerpo sentencias");yyerror("falta while");}
    ;

cuerpo_sentencia_ejecutable
    : '{' lista_sentencia_ejecutable '}'
    | '{' '}' {yyerror("error en sentencias dentro de { }");}
    | sentencia_ejecutable
    ;

/* -------------- EXPRESIONES LAMBDA -------------- */
exp_lambda
    : '(' tipo ID ')' '{' lista_sentencia_ejecutable '}' argumento_lambda {print("lambda");}
    | '(' tipo ID ')'  lista_sentencia_ejecutable '}' argumento_lambda {print("lambda"); yyerror("falta abrir llave en cuerpo de sentencia lambda");}
    | '(' tipo ID ')'  '{' lista_sentencia_ejecutable argumento_lambda {print("lambda"); yyerror("falta cerra llave en cuerpo de sentencia lambda");}
    | '(' tipo ID ')'   lista_sentencia_ejecutable argumento_lambda {print("lambda"); yyerror("faltan llaves en cuerpo de sentencia lambda");}
    ;


argumento_lambda
    : '(' ID ')'
    | '(' CTE ')'
    ;
/* -------------- ASIGNACION MULTIPLE -------------- */
asig_multiple
    : lista_variables '=' lista_constantes punto_coma {print("asignacion multiple");}
    ;

lista_variables
    : variable
    | lista_variables ',' variable
    | lista_variables variable {yyerror("falta , en lista de variables");}
    ;

lista_constantes
    : CTE
    | lista_constantes ',' CTE
    | lista_constantes CTE {yyerror("falta , en lista de constantes");}
    ;




%%
private ArrayList<Token> tokens = new ArrayList<>();

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

private void yyerror(String err){
  String str = Colores.ROJO + "ERROR LINEA "+ nroLinea() +": "+ err + Colores.RESET;
    System.out.println(str);
    AnalizadorLexico.addError(str);
}

private void print(String str){
  Main.estructurasDetectadas.add(Colores.AZUL+str+Colores.RESET);
  System.out.println(Colores.AZUL+str+Colores.RESET);
}
private int nroLinea(){
  return tokens.get(tokens.size() - 1).getNroLinea();
}