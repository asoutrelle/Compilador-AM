%{
import java.lang.Math;
import java.io.*;
import java.util.StringTokenizer;
%}

%token ID CTE PF64 ELSE ENDIF PRINT RETURN CADENA DFLOAT DO WHILE ASIG IF IGUAL NOIGUAL MENORIGUAL MAYORIGUAL FLECHA UINT CR TRUNC


%%
prog: ID '{' lista_sentencia '}'
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
lista_sentencia_declarativa: sentencia_declarativa
    | lista_sentencia_declarativa sentencia_declarativa
    ;
sentencia_ejecutable: invocacion {System.out.println(Colores.AZUL + "invocacion" + Colores.RESET);}
    | asig {System.out.println(Colores.AZUL + "Asignacion" + Colores.RESET);}
    | if {System.out.println(Colores.AZUL + "Sentencia if" + Colores.RESET);}
    | salida_msj {System.out.println(Colores.AZUL + "Salida de mensaje" + Colores.RESET);}
    | exp_lambda {System.out.println(Colores.AZUL + "Expresion lambda" + Colores.RESET);}
    | return {System.out.println(Colores.AZUL + "Return" + Colores.RESET);}
    | asig_multiple {System.out.println(Colores.AZUL + "Asignacion multiple" + Colores.RESET);}
    ;
lista_sentencia_ejecutable: sentencia_ejecutable
    | lista_sentencia_ejecutable sentencia_ejecutable
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
if: IF '(' cond ')' cuerpo_if cuerpo_else ENDIF ';'
;

cond: exp IGUAL exp
    | exp NOIGUAL exp
    | exp MENORIGUAL exp
    | exp MAYORIGUAL exp
    | exp '<' exp
    | exp '>' exp
;
cuerpo_if: sentencia_ejecutable
    | bloque_sentencia_ejecutable
;

bloque_sentencia_ejecutable: '{' lista_sentencia_ejecutable '}'
    ;

cuerpo_else: ELSE cuerpo_if
    |
    ;
/* -------------- FIN IF -------------- */
declaracion: tipo lista_variables ';'
;

lista_variables: variable
    | lista_variables ',' variable
;

salida_msj: PRINT '(' CADENA ')' ';'
    | PRINT '(' exp ')' ';'
;

tipo: UINT
;

sem_pasaje: CR
;

/* -------------- TRATADO DE FUNCIONES -------------- */

func: parametro '(' parametros_formales ')' cuerpo_funcion
;

parametros_formales: sem_pasaje parametro
    | parametros_formales ',' sem_pasaje parametro
;

cuerpo_funcion: '{' lista_bloque_funcion '}'
;

lista_bloque_funcion: bloque_funcion
    | lista_bloque_funcion bloque_funcion
    ;
bloque_funcion: lista_sentencia_declarativa
    | lista_sentencia_ejecutable
;

return: RETURN '(' exp ')' ';'
;

invocacion: ID '(' parametros_reales ')' ';'
;

parametros_reales: parametro_real FLECHA parametro_formal
    | parametro_real FLECHA parametro_formal ','

parametro_real: exp
;

parametro_formal: ID
;
/*------------------------------------------------- FIN FUNCION --------------------------------*/


/* -------------- DO WHILE -------------- */
do: DO cuerpo_do WHILE '(' cond ')' ';'
    ;

cuerpo_do: bloque_sentencia_ejecutable
    | sentencia_ejecutable
    ;
/* -------------- EXPRESIONES LAMBDA -------------- */
exp_lambda: '(' parametro ')' bloque_sentencia_ejecutable '(' factor ')' ';'
    ;

parametro: tipo ID
    ;

/* -------------- ASIGNACION MULTIPLE -------------- */
asig_multiple: lista_variables '=' lista_constantes ';'
    ;

lista_constantes: CTE
    | lista_constantes ',' CTE
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
