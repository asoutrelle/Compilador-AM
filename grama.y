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
    | ID '{' lista_sentencia  {yyerror("falta cierre de programa");}
    | ID lista_sentencia '}' {yyerror("falta inicio de programa");}
    | '{' lista_sentencia  {yyerror("falta nombre del programa y cierre");}
    | error lista_sentencia '}' {yyerror("falta nombre del programa e inicio");}
    | ID lista_sentencia {yyerror("no hay llaves del programa");}
    ;

lista_sentencia
    : sentencia
    | lista_sentencia sentencia
    ;

sentencia
    : sentencia_ejecutable
    | sentencia_declarativa
    | sentencia_de_control
    ;

sentencia_declarativa
    : declaracion
    | func
    ;

sentencia_ejecutable
    : asig
    | if
    | salida_msj
    | exp_lambda
    | return
    | asig_multiple
    ;

sentencia_de_control
    : do
    ;

asig
    : variable ASIG exp ';' {print("asignacion normal");}
    | variable ASIG exp error {yyerror("falta ; en asignacion");} {print("asignacion mal");}
    ;

exp
    : exp '+' termino
    | exp '-' termino
    | exp '+' error {yyerror("falta operando después de '+'"); }
    | exp '-' error {yyerror("falta operando después de '-'"); }
    | error '+' termino {yyerror("falta operando antes de '+'"); }
    | error '-' termino {yyerror("falta operando antes de '-'"); }
    | termino
    ;

exp_error
    : exp exp {yyerror("falta operador en expresion"); }
    ;

termino
    : termino '*' factor
    | termino '/' factor
    | termino '*' error {yyerror("falta operando después de *"); }
    | termino '/' error {yyerror("falta operando después de /"); }
    | error '*' factor {yyerror("falta operando antes de *"); }
    | error '/' factor {yyerror("falta operando antes de /"); }
    | factor
    ;

factor
    : variable
    | invocacion
    | CTE
    | TRUNC '(' exp ')'
    | TRUNC '(' ')' {yyerror("ERROR LINEA "+nroLinea()+": falta argumento en trunc"); }
    | TRUNC  exp ')' {yyerror("ERROR LINEA "+nroLinea()+": falta ( en trunc"); }
    | TRUNC '(' exp error {yyerror("ERROR LINEA "+nroLinea()+": falta ) en trunc"); }
    | TRUNC exp error {yyerror("faltan parentesis en trunc"); }
;

variable
    : variable '.' ID
    | ID
    ;

invocacion
    : ID '(' parametros_de_invocacion ')' {print("invocacion a funcion");}
    ;

salida_msj
    : PRINT '(' CADENA ')' ';' {print("print");}
    | PRINT '(' exp ')' ';' {print("print");}
    | PRINT '(' ')' ';' {yyerror("falta argumento en print");}
    | PRINT '(' CADENA ')' error {yyerror("falta ; print");}
    | PRINT '(' exp ')' error {yyerror("falta ; print");} {print("print");}
    | PRINT '(' exp_error ')' error {print("print");}
    ;
/* -------------- IF -------------- */
if
    : IF cuerpo_condicion cuerpo_sentencia_control cuerpo_else ENDIF ';' {print("sentencia if");}
    | IF cuerpo_condicion cuerpo_sentencia_control cuerpo_else ';' {yyerror("ERRROR FALTA ENDIF");}
    ;

cuerpo_condicion
    : '(' comparacion ')'
    | comparacion ')' {yyerror("falta abrir parentesis");}
    | '(' comparacion ';'{yyerror("falta cerrar parentesis");}
    | comparacion  {yyerror("faltan parentesis en condicion ");}
    ;

comparacion
    : exp comparador exp {print("comparacion");}
    ;

comparador
    : IGUAL
    | NOIGUAL
    | MENORIGUAL
    | MAYORIGUAL
    | '<'
    | '>'
    | {yyerror("falta comparador");}
    ;

cuerpo_sentencia_control
    : sentencia_ejecutable
    | '{' lista_sentencia_ejecutable '}'
    | {yyerror("no hay sentencias");}
    | '{' '}' {yyerror("no hay sentencias entre llaves");}
    | lista_sentencia_ejecutable '}' {yyerror("falta llave {");}
    | '{' lista_sentencia_ejecutable {yyerror("falta llave }");}
    ;

lista_sentencia_ejecutable
    : sentencia_ejecutable
    | lista_sentencia_ejecutable sentencia_ejecutable
    ;

cuerpo_else
    : ELSE cuerpo_sentencia_control
    |
    ;
/* -------------- FIN IF -------------- */
declaracion
    : tipo lista_variables_declaracion ';' {print("declaracion");}
    ;

lista_variables_declaracion
    : ID
    | lista_variables ',' ID
    | lista_variables ID {yyerror("falta , en lista de variables");}
    ;

/* -------------- TRATADO DE FUNCIONES -------------- */

func
    : tipo ID '(' parametros_formales ')' '{' lista_sentencia_funcion '}' {print("funcion");}
    | tipo '(' parametros_formales ')' '{' lista_sentencia_funcion '}' {yyerror("falta nombre de func");}
    ;

parametro
    : tipo ID
    | ID {yyerror("Falta tipo en parametro");}
    | tipo {yyerror("Falta nombre en parametro");}
    ;

parametros_formales
    : sem_pasaje parametro
    | parametro
    | parametros_formales ',' sem_pasaje parametro
    | sem_pasaje {yyerror("falta tipo e ID en parametros de funcion");}
    ;

tipo
    : UINT
    ;

sem_pasaje
    : CR
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
    : RETURN '(' exp ')' ';' {print("return");}
    | RETURN '(' exp_error ')' ';' {print("return");}
    ;


parametros_de_invocacion
    : parametro_real FLECHA parametro_formal
    | parametro_real FLECHA {yyerror("Falta parametro formal");}
    | parametro_real {yyerror("Falta flecha y parametro formal");}
    | parametros_de_invocacion ',' parametro_real FLECHA parametro_formal
    | parametros_de_invocacion ',' parametro_real FLECHA {yyerror("Falta parametro formal");}
    | parametros_de_invocacion ',' parametro_real {yyerror("Falta flecha y parametro formal");}
    ;

parametro_real
    : exp
    | exp_error
    ;

parametro_formal
    : ID
    ;

/*------------------------------------------------- FIN FUNCION --------------------------------*/


/* -------------- DO WHILE -------------- */
do
    : DO cuerpo_sentencia_control WHILE cuerpo_condicion ';' {print("sentencia do while");}
    | DO cuerpo_sentencia_control error cuerpo_condicion ';' {yyerror("falta while");}
    ;

/* -------------- EXPRESIONES LAMBDA -------------- */
exp_lambda
    : '(' tipo ID ')' '{' lista_sentencia_ejecutable '}' '(' argumento_lambda ')' ';' {print("lambda");}
    | '(' tipo ID ')' error lista_sentencia_ejecutable '}' '(' argumento_lambda ')' ';' {yyerror("falta { en lambda");}
    | '(' tipo ID ')' '{' lista_sentencia_ejecutable error ')' ';' {yyerror("falta } en lambda");}
    ;

argumento_lambda
    : ID
    | CTE
    ;

/* -------------- ASIGNACION MULTIPLE -------------- */
asig_multiple
    : lista_variables '=' lista_constantes ';' {print("asignacion multiple");}
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
  return tokens.getLast().getNroLinea();
}