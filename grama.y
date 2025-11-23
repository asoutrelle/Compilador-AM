%{
import java.lang.Math;
import java.io.*;
import java.util.StringTokenizer;
import java.util.ArrayList;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;
%}

%token ID CTE PF64 ELSE ENDIF PRINT RETURN CADENA
%token DFLOAT DO WHILE ASIG IF IGUAL NOIGUAL
%token MENORIGUAL MAYORIGUAL FLECHA UINT CR TRUNC


%%
prog
    : ID '{' lista_sentencia '}'
    {
        Compilador.salirAmbito();
        String ambito = Compilador.getAmbito();
        String var = $1.sval;
        if(TablaDeSimbolos.agregarVar(var, ambito, "", "nombre de programa")){
           $$ = new ParserVal(var+ambito);
        } else yyerror("La variable "+var+" ya fue declarada");
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

declaracion
    : tipo lista_variables_declaracion punto_coma
    ;


lista_variables_declaracion
    : ID
    {
        String ambito = Compilador.getAmbito();
        String var = $1.sval;
        if(TablaDeSimbolos.agregarVar(var, ambito, tipo, "nombre de variable")){
           $$ = new ParserVal(var+ambito);
        } else yyerror("La variable "+var+" ya fue declarada");
    }
    | lista_variables_declaracion ',' ID
    {
        String ambito = Compilador.getAmbito();
        String var = $3.sval;
        if(TablaDeSimbolos.agregarVar(var, ambito, tipo, "nombre de variable")){
           $$ = new ParserVal($1.sval+","+var);
        } else yyerror("La variable "+var+" ya fue declarada");
    }
    | lista_variables_declaracion ID {yyerror("falta , en lista de variables");}
    ;

asig
    : variable ASIG exp punto_coma
    {
        //if(TablaDeSimbolos.esCompatible($1.sval,$3.sval, Compilador.getAmbito())){
          //  System.out.println("val1:"+$1.sval+" val2:"+$3.sval);
          //  crearTerceto(":=", $1.sval, $3.sval);
        //} else yyerror("Los tipos de las variables no coinciden");


        String ambito = Compilador.getAmbito();
        String var = $1.sval;
        if(TablaDeSimbolos.varDeclarada(var, ambito)){
            crearTerceto(":=", var, $3.sval);
        }
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
    | termino
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
    | factor
    ;

factor
    : variable
    | invocacion
    | CTE {$$=new ParserVal($1.sval);}
    | TRUNC '(' punto_flotante ')'
    {
        addEstructura("trunc"); $$=new ParserVal(truncar($3.sval));
    }
    | TRUNC  punto_flotante ')' {addEstructura("trunc");yyerror("falta abrir parentesis en trunc");}
    | TRUNC '(' error {addEstructura("trunc");yyerror("falta cerrar parentesis en trunc");yyerrflag=0;}
    | TRUNC  '(' ')' {addEstructura("trunc");yyerror("falta argumento en trunc");}
    | exp_lambda {addEstructura("lambda");}
    ;

punto_flotante
    : PF64 {check_rango($1.sval); $$=new ParserVal($1.sval);}
    | '-' PF64 {check_rango("-"+$2.sval); $$=new ParserVal("-"+$1.sval);}
    ;

variable
    : ID '.' ID
    {
        String aux = TablaDeSimbolos.varPrefijadaDeclarada($3.sval, $1.sval, Compilador.getAmbito());
        if(aux.equals("")){
            yyerror("La variable "+$3.sval+" no fue declarada en el ambito " + $1.sval);
            TablaDeSimbolos.eliminar($1.sval);
            TablaDeSimbolos.eliminar($3.sval);
        }
        TablaDeSimbolos.eliminar($3.sval);
        $$ = new ParserVal(aux);
    }
    | ID
    {
        String ambito = Compilador.getAmbito();
        String var = $1.sval;
        if(!TablaDeSimbolos.varDeclarada(var, ambito)){
            yyerror("La variable "+ var +" no fue declarada");
        } else {
            TablaDeSimbolos.eliminar(var);
            $$=new ParserVal (var + ambito);
        }
    }
    ;

invocacion
    : ID {nombreFuncion = $1.sval;} '(' parametros_de_invocacion ')'
    {
        String ambito = Compilador.getAmbito();
        String var = $1.sval;
        if(!TablaDeSimbolos.funcionDeclarada(var,ambito)){
            yyerror("La funcion " + var + " no esta declarada");
        } else{
            TablaDeSimbolos.eliminar(var);
            addEstructura("invocacion a funcion");
        }
        String[] param = $4.sval.split(",");
        Set<String> set = new HashSet<>();
        for (String p : param) {
            if (!set.add(p)) {
                yyerror("Parametro duplicado");
            }
        }
        int cantParam = param.length;
        if(TablaDeSimbolos.cantParametrosFormales($1.sval)!=cantParam){
            yyerror("cantidad de parametros reales distinta a parametros formales");
        }
        $$ = new ParserVal(var+ambito);
    }
    ;

parametros_de_invocacion
    : parametro_real FLECHA ID
    {
        String ambito = Compilador.getAmbito()+":"+nombreFuncion;
        String var = $3.sval;
        if (!TablaDeSimbolos.parametroDeclarado(var, ambito)){
            yyerror("El parametro " + var + " no esta declarado en la funcion "+nombreFuncion);
        }
        crearTerceto(":=", var+ambito,$1.sval);
        TablaDeSimbolos.eliminar(var);
        $$=$1;
    }
    | parametro_real FLECHA {yyerror("Falta parametro formal");}
    | parametro_real {yyerror("Falta flecha y parametro formal");}
    | parametros_de_invocacion ',' parametro_real FLECHA ID
    {
        String ambito = Compilador.getAmbito()+":"+nombreFuncion;
        String var = $5.sval;
        if (!TablaDeSimbolos.parametroDeclarado(var, ambito)){
            yyerror("El parametro " + var + " no esta declarado en la funcion "+nombreFuncion);
        }
        crearTerceto(":=", var+ambito,$3.sval);
        TablaDeSimbolos.eliminar(var);
        $$=new ParserVal($1.sval + "," + $3.sval);
    }
    | parametros_de_invocacion ',' parametro_real FLECHA {yyerror("Falta parametro formal");}
    | parametros_de_invocacion ',' parametro_real {yyerror("Falta flecha y parametro formal");}
    | error {yyerror("error en parametros de invocacion");}
    ;

parametro_real
    : exp
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
    : IF cuerpo_condicion nuevo_ambito_ua cuerpo_sentencia_ejecutable completar_bf_else ELSE nuevo_ambito_ua crear_bi cuerpo_sentencia_ejecutable ENDIF punto_coma
    {
        int bi = Compilador.pilaSaltos.remove(Compilador.pilaSaltos.size()-1);
        completarBI(bi, Compilador.tercetos.size());
    }
    | IF cuerpo_condicion nuevo_ambito_ua cuerpo_sentencia_ejecutable completar_bf_else ELSE ENDIF punto_coma {yyerror("no hay sentencias en else");}
    | IF cuerpo_condicion nuevo_ambito_ua cuerpo_sentencia_ejecutable completar_bf ENDIF punto_coma

    /*sin endif*/
    | IF cuerpo_condicion nuevo_ambito_ua cuerpo_sentencia_ejecutable completar_bf_else ELSE nuevo_ambito_ua crear_bi cuerpo_sentencia_ejecutable punto_coma
    {
        yyerror("falta endif");
    }
    | IF cuerpo_condicion nuevo_ambito_ua cuerpo_sentencia_ejecutable completar_bf_else ELSE  punto_coma
    {
        yyerror("falta endif");yyerror("no hay sentencias en else");
    }
    | IF cuerpo_condicion nuevo_ambito_ua cuerpo_sentencia_ejecutable completar_bf punto_coma
    {
        yyerror("falta endif");
    }

    /*con endif, sin then*/
    | IF cuerpo_condicion ELSE nuevo_ambito_ua crear_bi cuerpo_sentencia_ejecutable completar_bf ENDIF punto_coma {yyerror("no hay sentencias en then");}
    | IF cuerpo_condicion ELSE  ENDIF punto_coma {yyerror("no hay sentencias en then");yyerror("no hay sentencias en else");}
    | IF cuerpo_condicion ENDIF punto_coma {yyerror("no hay sentencias en then");}

    /*sin endif, sin then*/
    | IF cuerpo_condicion  ELSE nuevo_ambito_ua crear_bi cuerpo_sentencia_ejecutable punto_coma {yyerror("no hay sentencias en then");yyerror("falta endif");}
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


/* -------------- TRATADO DE FUNCIONES -------------- */
funcion
    : tipo ID '(' {
        hayReturn = false;
        String ambito = Compilador.getAmbito();
        String var = $2.sval;
        if(TablaDeSimbolos.agregarVar(var, ambito, tipo, "nombre de funcion")){
           $$ = new ParserVal(var+ambito);
        } else yyerror("La funcion "+var+" ya fue declarada");

        Compilador.entrarAmbito($2.sval);
    }
    parametros_formales ')' {
        crearTerceto("inicio de funcion", $2.sval, "-");
    }
    '{' lista_sentencia_funcion '}' {
        if (!hayReturn){
            yyerror("falta return en la funcion "+ $2.sval);
        }
        hayReturn = true;
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
        String var = $3.sval;
        if(TablaDeSimbolos.agregarVar(var, ambito, tipo, "nombre de variable", "copia resultado")){
           $$ = new ParserVal(var+ambito);
        } else yyerror("La variable "+var+" ya fue declarada");
    }
    | tipo ID
    {
        String ambito = Compilador.getAmbito();
        String var = $2.sval;
        if(TablaDeSimbolos.agregarVar(var, ambito, tipo, "nombre de variable","copia valor")){
           $$ = new ParserVal(var+ambito);
        } else yyerror("La variable "+var+" ya fue declarada");
    }
    | parametros_formales ',' CR tipo ID{
        String ambito = Compilador.getAmbito();
        String var = $5.sval;
        if(TablaDeSimbolos.agregarVar(var, ambito, tipo, "nombre de variable", "copia resultado")){
           $$ = new ParserVal(var+ambito);
        } else yyerror("La variable "+var+" ya fue declarada");
    }
    | parametros_formales ',' tipo ID{
        String ambito = Compilador.getAmbito();
        String var = $4.sval;
        if(TablaDeSimbolos.agregarVar(var, ambito, tipo, "nombre de variable", "copia valor")){
           $$ = new ParserVal(var+ambito);
        } else yyerror("La variable "+var+" ya fue declarada");
    }
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
        if (hayReturn){
            yyerror("no se permite este return");
        }else{
            if (Compilador.getAmbito().lastIndexOf(":") == 0){ //pregunto si estoy en main
                yyerror("no se permite este return");
            }
            hayReturn = true;
        }
        addEstructura("return");
        crearTerceto("return", $3.sval, "-");
        int t = Compilador.tercetos.size() - 1;
        $$=$3;
        }
    ;

/*--------------------------------------------------------------------------------*/


/* ------------------------------------------ DO WHILE ------------------------------------------ */
do
    : DO nuevo_ambito_ua inicio_while cuerpo_sentencia_ejecutable WHILE cuerpo_condicion crear_bi_while completar_bf punto_coma
    | DO nuevo_ambito_ua inicio_while WHILE cuerpo_condicion punto_coma {yyerror("falta cuerpo sentencias");}
    | DO nuevo_ambito_ua inicio_while cuerpo_sentencia_ejecutable WHILE cuerpo_condicion {yyerror("falta de ;");}

    | DO nuevo_ambito_ua inicio_while cuerpo_sentencia_ejecutable cuerpo_condicion punto_coma { yyerror("falta while");}
    | DO nuevo_ambito_ua inicio_while cuerpo_condicion punto_coma {yyerror("falta cuerpo sentencias"); yyerror("falta while");}
    | DO nuevo_ambito_ua inicio_while cuerpo_sentencia_ejecutable cuerpo_condicion {yyerror("falta de ) en condicion");yyerror("falta de ;");yyerror("falta while");}
    | DO nuevo_ambito_ua inicio_while  cuerpo_condicion {yyerror("falta de ) en condicion");yyerror("falta de ;");yyerror("falta cuerpo sentencias");yyerror("falta while");}
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
    
nuevo_ambito_ua
    :
    {
        Compilador.entrarAmbito("ua"+cantUnidadesAnonimas);
        cantUnidadesAnonimas+=1;
    }
    ;
cuerpo_sentencia_ejecutable
    : '{' lista_sentencia_ejecutable '}' {Compilador.salirAmbito();hayReturn = false;}
    | '{' '}' {yyerror("no hay sentencias dentro de las llaves");}
    | sentencia_ejecutable {Compilador.salirAmbito();}
    | '{' error '}' {yyerror("Error en sentencia");}
    ;

/* -------------- EXPRESIONES LAMBDA -------------- */
exp_lambda
    : '('  tipo nuevo_ambito_ua  validar_id ')' '{' lista_sentencia_ejecutable '}' argumento_lambda
    {
        $$= new ParserVal("lambda");
        crearTerceto("fin de lambda", "-", "-");
        Compilador.salirAmbito();
    }
    |  '(' tipo nuevo_ambito_ua  validar_id ')'  lista_sentencia_ejecutable '}' argumento_lambda { yyerror("falta abrir llave en cuerpo de sentencia lambda");}
    |  '(' tipo nuevo_ambito_ua  validar_id ')'  '{' lista_sentencia_ejecutable argumento_lambda {yyerror("falta cerrar llave en cuerpo de sentencia lambda");}
    |  '(' tipo nuevo_ambito_ua  validar_id ')'   lista_sentencia_ejecutable argumento_lambda { yyerror("faltan llaves en cuerpo de sentencia lambda");}
    ;

argumento_lambda
    : '(' ID ')'
    | '(' CTE ')'
    ;

validar_id
    : ID
    {
        String ambito = Compilador.getAmbito();
        String var = $1.sval;
        if(TablaDeSimbolos.agregarVar(var, ambito, tipo, "nombre de variable")){
           $$ = new ParserVal(var+ambito);
        } else yyerror("La variable "+var+" ya fue declarada");
        crearTerceto("inicio de lambda", "-", "-");
    }
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
private String nombreFuncion;
private boolean hayReturn = true;


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

  private String truncar(String pf){
        String normalized = pf.replace('D', 'E').replace('d', 'E');
        double val;
        try {
            val = Double.parseDouble(normalized);
        } catch (NumberFormatException e) {
            yyerror("Literal punto flotante inválido en trunc(): " + pf);
            return null;
        }
        long truncadoLong = (long) val;
        String valorStr = Long.toString(truncadoLong)+"UI";
        Token t = new Token(258,nroLinea());
        TablaDeSimbolos.agregar(valorStr,t, "uint", "variable auxiliar");
        return valorStr;
  }