%{
import java.lang.Math;
import java.io.*;
import java.util.StringTokenizer;
%}

%token ID CTE PF64 ELSE ENDIF PRINT RETURN CADENA DFLOAT DO WHILE ASIG IF IGUAL NOIGUAL MENORIGUAL MAYORIGUAL FLECHA UINT CR TRUNC


%%
prog
    : ID '{' lista_sentencia '}'
    | error '{' lista_sentencia '}' {yyerror("ERROR falta nombre de programa");}
    | error '{' lista_sentencia error {yyerror("ERROR EN NOMBRE DE PROGRAMA y cierre");}
    | ID '{' lista_sentencia error {yyerror("ERROR falta cierre de programa");}
    | ID error {yyerror("ERROR falta inicio de programa");}
    ;

lista_sentencia: sentencia
    | lista_sentencia sentencia
;
sentencia: sentencia_ejecutable
    | sentencia_declarativa
    | sentencia_de_control
;

sentencia_declarativa: declaracion {System.out.println(Colores.AZUL + "declaracion" + Colores.RESET);}
    | func {System.out.println(Colores.AZUL + "funcion" + Colores.RESET);}
    ;

sentencia_ejecutable
    : asig {System.out.println(Colores.AZUL + "Asignacion" + Colores.RESET);}
    | if  {System.out.println(Colores.AZUL + "Sentencia if" + Colores.RESET);}
    | salida_msj {System.out.println(Colores.AZUL + "Salida de mensaje" + Colores.RESET);}
    | exp_lambda {System.out.println(Colores.AZUL + "Expresion lambda" + Colores.RESET);}
    | return {System.out.println(Colores.AZUL + "Return" + Colores.RESET);}
    | asig_multiple {System.out.println(Colores.AZUL + "Asignacion multiple" + Colores.RESET);}
    ;

sentencia_de_control: do {System.out.println(Colores.AZUL + "Sentencia do" + Colores.RESET);}
    ;

asig: variable ASIG exp ';'
    ;

exp: exp '+' term
    | exp '-' term
    | term
    | TRUNC '(' exp ')'
;

term: term '*' factor
    | term '/' factor
    | factor
;

factor: variable
    | CTE
    | invocacion
;

variable: ID
    | variable_prefijada
    ;

/* -------------- PREFIJADO -------------- */
variable_prefijada: ID '.' ID
;
/* --------------------------------------- */

/* -------------- IF -------------- */
if
    : IF cuerpo_condicion cuerpo_sentencia_control cuerpo_else ENDIF ';'
    ;

cuerpo_condicion
    : '(' cond ')'
    | cond ')' {yyerror("falta ( ");}
    | '(' error {yyerror("falta ) ");}
    | error {yyerror("faltan parentesis ");}
    ;

cond: exp IGUAL exp
    | exp NOIGUAL exp
    | exp MENORIGUAL exp
    | exp MAYORIGUAL exp
    | exp '<' exp
    | exp '>' exp
;

cuerpo_sentencia_control: sentencia_ejecutable
    | '{' lista_sentencia_ejecutable '}'
    | {yyerror("no hay sentencias");}
    | '{' '}' {yyerror("no hay sentencias entre llaves");}
;

lista_sentencia_ejecutable: sentencia_ejecutable
    | lista_sentencia_ejecutable sentencia_ejecutable
;

cuerpo_else: ELSE cuerpo_sentencia_control
    |
    ;
/* -------------- FIN IF -------------- */
declaracion: tipo lista_variables ';'
;

lista_variables
    : variable
    | lista_variables ',' variable
    | lista_variables variable {yyerror("falta , en lista de variables");}
    ;

salida_msj
    : PRINT '(' CADENA ')' ';'
    | PRINT '(' exp ')' ';'
    | PRINT '(' ')' ';' {yyerror("falta argumento en print");}
    ;

tipo: UINT
;

sem_pasaje: CR
;

/* -------------- TRATADO DE FUNCIONES -------------- */

func: tipo ID '(' parametros_formales ')' '{' lista_sentencia_funcion '}'
    | tipo error '(' parametros_formales ')' '{' lista_sentencia_funcion '}' {yyerror("falta nombre de func");}
    | error '(' parametros_formales ')' '{' lista_sentencia_funcion '}' {yyerror("falta tipo de func");}
    ;
parametro
    : tipo ID
    | error ID {yyerror("Falta tipo en parametro");}
    | tipo error {yyerror("Falta nombre en parametro");}
    ;

parametros_formales: sem_pasaje parametro
    | parametros_formales ',' sem_pasaje parametro
    | sem_pasaje error
;


lista_sentencia_funcion: sentencia_funcion
    | lista_sentencia_funcion sentencia_funcion
    ;
sentencia_funcion: sentencia_declarativa
    | sentencia_ejecutable
;

return
    : RETURN '(' exp ')' ';'
    ;

invocacion: ID '(' parametros_reales ')'
;

parametros_reales: parametro_real FLECHA parametro_formal
    | parametros_reales ',' parametro_real FLECHA parametro_formal
    | parametros_reales ',' parametro_real FLECHA error {yyerror("Falta parametro formal");}
    | parametro_real FLECHA error {yyerror("Falta parametro formal");}
    | parametros_reales ',' parametro_real error {yyerror("Falta parametro formal");}
    ;

parametro_real: exp
;

parametro_formal: ID
;

/*------------------------------------------------- FIN FUNCION --------------------------------*/


/* -------------- DO WHILE -------------- */
do: DO cuerpo_sentencia_control WHILE cuerpo_condicion ';'
    ;

/* -------------- EXPRESIONES LAMBDA -------------- */
exp_lambda: '(' tipo ID ')' '{' lista_sentencia_ejecutable '}' '(' factor ')' ';'
    ;


/* -------------- ASIGNACION MULTIPLE -------------- */
asig_multiple: lista_variables '=' lista_constantes ';' /*{checkAsignacionMultiple();}*/

lista_constantes: CTE
    | lista_constantes ',' CTE
    ;




%%
private int nroLinea;

private int yylex(){
    try {
      Token t = AnalizadorLexico.leerCaracter();
        nroLinea = t.getNroLinea() -1;
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
    System.out.println(Colores.ROJO + err + Colores.RESET);
    AnalizadorLexico.addError(Colores.ROJO + err + Colores.RESET);
}