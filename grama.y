%{
import java.lang.Math;
import java.io.*;
import java.util.StringTokenizer;
%}

%token ID CTE PF64 ELSE ENDIF PRINT RETURN CADENA DFLOAT DO WHILE ASIG IF IGUAL NOIGUAL MENORIGUAL MAYORIGUAL FLECHA UINT CR


%%
prog: ID '{' list_sentencia '}'
;

list_sentencia: sentencia
    | list_sentencia sentencia
;

sentencia: asig ';'
    | if ';'
    | declaracion ';'
    | salida_msj ';'
    | func
    | invocacion ';'
    | do
    | exp_lambda
;

asig: ID ASIG exp
;

exp: exp '+' term
    | exp '-' term
    | term
;

term: term '*' factor
    | term '/' factor
    | factor
;

factor: ID
    | CTE
;

if: IF '(' cond ')' cuerpo_if cuerpo_else ENDIF
;

cond: exp IGUAL exp
    | exp NOIGUAL exp
    | exp MENORIGUAL exp
    | exp MAYORIGUAL exp
    | exp '<' exp
    | exp '>' exp
;

cuerpo_if: sentencia
    | bloque_sin_return
;

bloque_sin_return: '{' list_sentencia '}'
;

cuerpo_else: ELSE cuerpo_if
    | ELSE
;

declaracion: tipo list_variables
;

list_variables: ID
    | list_variables ',' ID
;

salida_msj: PRINT '(' CADENA ')'
    | PRINT '(' exp ')'
;

tipo: UINT
;

/*sem_pasaje: CR
; */

/* -------------- TRATADO DE FUNCIONES -------------- */

func: tipo ID '(' parametros_formales ')' cuerpo_funcion
;

parametros_formales: /*sem_pasaje*/ tipo ID
    | parametros_formales ',' /*sem_pasaje*/ tipo ID
;

cuerpo_funcion: bloque_en_funcion
;

bloque_en_funcion: '{' list_sentencia_en_funcion return '}'
    | '{' return '}'
;

list_sentencia_en_funcion: sentencia_en_funcion
    | list_sentencia_en_funcion sentencia_en_funcion
;

return: RETURN '(' exp ')' ';'
;

sentencia_en_funcion: asig ';'
    | if_en_funcion ';'
    | declaracion ';'
    | salida_msj ';'
    | func
    | invocacion ';'
;

if_en_funcion: IF '(' cond ')' cuerpo_if_en_funcion cuerpo_else_en_funcion ENDIF
;

cuerpo_if_en_funcion: sentencia_en_funcion
    | bloque_en_funcion
;

cuerpo_else_en_funcion: ELSE cuerpo_if_en_funcion
    | ELSE return
    |
;

invocacion: ID '(' parametros reales ')'
;

parametros_reales: parametro_real FLECHA parametro_formal
    | parametro_real FLECHA parametro_formal ','

parametro_real: exp
;

parametro_formal: ID
;

/* -------------- DO WHILE -------------- */
do: DO cuerpo_do WHILE '(' cond ')'
;

cuerpo_do: bloque_sin_return
    | sentencia
;

/* -------------- PREFIJADO -------------- */
prefijado: ID '.' ID
;
/* podriamos cambiar en los lugares donde esta ID y se refiera a una variable por variable que pueda ser un ID o variable prefijada ID.ID? */

/* -------------- EXPRESIONES LAMBDA -------------- */
exp_lambda: parametro_lambda cuerpo argumento
;

parametro_lambda: tipo ID
;

cuerpo: bloque_sin_return
;

argumento: '(' ID ')'
    | '(' CTE ')'
;

%%
private int yylex(){
    try {
        int aux = AnalizadorLexico.leerCaracter().getLexema();
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
}
