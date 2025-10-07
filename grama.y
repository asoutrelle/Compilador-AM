%{
import java.lang.Math;
import java.io.*;
import java.util.StringTokenizer;
%}

%token ID CTE PF64 ELSE ENDIF PRINT RETURN CADENA DFLOAT DO WHILE ASIG IF IGUAL NOIGUAL MENORIGUAL MAYORIGUAL FLECHA UINT CR TRUNC


%%
prog
    : ID '{' lista_sentencia '}'
    | '{' lista_sentencia '}' {yyerror("ERROR falta nombre de programa");}
    | ID '{' lista_sentencia  {yyerror("ERROR falta cierre de programa");}
    | ID  lista_sentencia '}' {yyerror("ERROR falta inicio de programa");}
    | '{' lista_sentencia  {yyerror("ERROR EN NOMBRE DE PROGRAMA y cierre");}
    | error lista_sentencia '}' {yyerror("ERROR EN NOMBRE DE PROGRAMA e inicio");}
    | ID lista_sentencia {yyerror("ERROR no hay llaves del programa");}
    ;

lista_sentencia: sentencia
    | lista_sentencia sentencia
;
sentencia: sentencia_ejecutable
    | sentencia_declarativa
    | sentencia_de_control
;

sentencia_declarativa: declaracion
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

sentencia_de_control: do
    ;

asig: variable ASIG exp ';' {print("asignacion");}
    ;

exp
    : exp '+' termino
    | exp '-' termino
    | termino
    | TRUNC '(' exp ')'
    ;

termino: termino '*' factor
    | termino '/' factor
    | factor
    | termino error {yyerror("falta operador");}
;

factor: variable
    | CTE
    | invocacion
;

salida_msj
    : PRINT '(' CADENA ')' ';' {print("print");}
    | PRINT '(' exp ')' ';' {print("print");}
    | PRINT '(' ')' ';' {yyerror("falta argumento en print");}
    ;

/* -------------- PREFIJADO -------------- */
variable: ID
    | variable '.' ID
    ;
/* --------------------------------------- */
invocacion
    : ID '(' parametros_de_invocacion ')' {print("invocacion a funcion");}
    ;


/* -------------- IF -------------- */
if
    : IF cuerpo_condicion cuerpo_sentencia_control cuerpo_else ENDIF ';' {print("sentencia if");}
    | IF cuerpo_condicion cuerpo_sentencia_control cuerpo_else ';' {yyerror("ERRROR FALTA ENDIF");}
    ;

cuerpo_condicion
    : '(' cond ')'
    | cond ')' {yyerror("falta abrir parentesis");}
    | '(' cond {yyerror("falta cerrar parentesis");}
    | cond {yyerror("faltan parentesis en condicion ");}
    ;

cond: exp IGUAL exp
    | exp NOIGUAL exp
    | exp MENORIGUAL exp
    | exp MAYORIGUAL exp
    | exp '<' exp
    | exp '>' exp
    | exp exp {yyerror("falta comparador");}
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
declaracion: tipo lista_variables ';' {print("declaracion");}
;

lista_variables
    : variable
    | lista_variables ',' variable
    | lista_variables variable {yyerror("falta , en lista de variables");}
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
    | parametros_formales ',' sem_pasaje parametro
    | sem_pasaje {yyerror("falta tipo e ID en parametros de funcion");}
    ;

tipo
    : UINT
    ;

sem_pasaje
    : CR
    ;

lista_sentencia_funcion: sentencia_funcion
    | lista_sentencia_funcion sentencia_funcion
    ;

sentencia_funcion: sentencia_declarativa
    | sentencia_ejecutable
    ;

return
    : RETURN '(' exp ')' ';' {print("return");}
    ;


parametros_de_invocacion
    : parametro_real FLECHA parametro_formal
    | parametro_real FLECHA {yyerror("Falta parametro formal");}
    | parametro_real {yyerror("Falta flecha y parametro formal");}
    | parametros_de_invocacion ',' parametro_real FLECHA parametro_formal
    | parametros_de_invocacion ',' parametro_real FLECHA {yyerror("Falta parametro formal");}
    | parametros_de_invocacion ',' parametro_real {yyerror("Falta flecha y parametro formal");}
    ;

parametro_real: exp
;

parametro_formal: ID
;

/*------------------------------------------------- FIN FUNCION --------------------------------*/


/* -------------- DO WHILE -------------- */
do
    : DO cuerpo_sentencia_control WHILE cuerpo_condicion ';' {print("sentencia do while");}
    | DO cuerpo_sentencia_control error cuerpo_condicion ';' {yyerror("falta while");}
    ;

/* -------------- EXPRESIONES LAMBDA -------------- */
exp_lambda: '(' tipo ID ')' '{' lista_sentencia_ejecutable '}' '(' factor ')' ';' {print("lambda");}
    ;


/* -------------- ASIGNACION MULTIPLE -------------- */
asig_multiple: lista_variables '=' lista_constantes ';' /*{checkAsignacionMultiple();}*/ {print("asignacion multiple");}

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

private void print(String str){
  Main.estructurasDetectadas.add(Colores.AZUL+str+Colores.RESET);
  System.out.println(Colores.AZUL+str+Colores.RESET);
}