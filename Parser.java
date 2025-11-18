//### This file created by BYACC 1.8(/Java extension  1.15)
//### Java capabilities added 7 Jan 97, Bob Jamison
//### Updated : 27 Nov 97  -- Bob Jamison, Joe Nieten
//###           01 Jan 98  -- Bob Jamison -- fixed generic semantic constructor
//###           01 Jun 99  -- Bob Jamison -- added Runnable support
//###           06 Aug 00  -- Bob Jamison -- made state variables class-global
//###           03 Jan 01  -- Bob Jamison -- improved flags, tracing
//###           16 May 01  -- Bob Jamison -- added custom stack sizing
//###           04 Mar 02  -- Yuval Oren  -- improved java performance, added options
//###           14 Mar 02  -- Tomas Hurka -- -d support, static initializer workaround
//### Please send bug reports to tom@hukatronic.cz
//### static char yysccsid[] = "@(#)yaccpar	1.8 (Berkeley) 01/20/90";






//#line 2 "grama.y"
import java.lang.Math;
import java.io.*;
import java.util.StringTokenizer;
import java.util.ArrayList;
import java.math.BigDecimal;
//#line 23 "Parser.java"




public class Parser
{

boolean yydebug;        //do I want debug output?
int yynerrs;            //number of errors so far
int yyerrflag;          //was there an error?
int yychar;             //the current working character

//########## MESSAGES ##########
//###############################################################
// method: debug
//###############################################################
void debug(String msg)
{
  if (yydebug)
    System.out.println(msg);
}

//########## STATE STACK ##########
final static int YYSTACKSIZE = 500;  //maximum stack size
int statestk[] = new int[YYSTACKSIZE]; //state stack
int stateptr;
int stateptrmax;                     //highest index of stackptr
int statemax;                        //state when highest index reached
//###############################################################
// methods: state stack push,pop,drop,peek
//###############################################################
final void state_push(int state)
{
  try {
		stateptr++;
		statestk[stateptr]=state;
	 }
	 catch (ArrayIndexOutOfBoundsException e) {
     int oldsize = statestk.length;
     int newsize = oldsize * 2;
     int[] newstack = new int[newsize];
     System.arraycopy(statestk,0,newstack,0,oldsize);
     statestk = newstack;
     statestk[stateptr]=state;
  }
}
final int state_pop()
{
  return statestk[stateptr--];
}
final void state_drop(int cnt)
{
  stateptr -= cnt; 
}
final int state_peek(int relative)
{
  return statestk[stateptr-relative];
}
//###############################################################
// method: init_stacks : allocate and prepare stacks
//###############################################################
final boolean init_stacks()
{
  stateptr = -1;
  val_init();
  return true;
}
//###############################################################
// method: dump_stacks : show n levels of the stacks
//###############################################################
void dump_stacks(int count)
{
int i;
  System.out.println("=index==state====value=     s:"+stateptr+"  v:"+valptr);
  for (i=0;i<count;i++)
    System.out.println(" "+i+"    "+statestk[i]+"      "+valstk[i]);
  System.out.println("======================");
}


//########## SEMANTIC VALUES ##########
//public class ParserVal is defined in ParserVal.java


String   yytext;//user variable to return contextual strings
ParserVal yyval; //used to return semantic vals from action routines
ParserVal yylval;//the 'lval' (result) I got from yylex()
ParserVal valstk[];
int valptr;
//###############################################################
// methods: value stack push,pop,drop,peek.
//###############################################################
void val_init()
{
  valstk=new ParserVal[YYSTACKSIZE];
  yyval=new ParserVal();
  yylval=new ParserVal();
  valptr=-1;
}
void val_push(ParserVal val)
{
  if (valptr>=YYSTACKSIZE)
    return;
  valstk[++valptr]=val;
}
ParserVal val_pop()
{
  if (valptr<0)
    return new ParserVal();
  return valstk[valptr--];
}
void val_drop(int cnt)
{
int ptr;
  ptr=valptr-cnt;
  if (ptr<0)
    return;
  valptr = ptr;
}
ParserVal val_peek(int relative)
{
int ptr;
  ptr=valptr-relative;
  if (ptr<0)
    return new ParserVal();
  return valstk[ptr];
}
final ParserVal dup_yyval(ParserVal val)
{
  ParserVal dup = new ParserVal();
  dup.ival = val.ival;
  dup.dval = val.dval;
  dup.sval = val.sval;
  dup.obj = val.obj;
  return dup;
}
//#### end semantic value section ####
public final static short ID=257;
public final static short CTE=258;
public final static short PF64=259;
public final static short ELSE=260;
public final static short ENDIF=261;
public final static short PRINT=262;
public final static short RETURN=263;
public final static short CADENA=264;
public final static short DFLOAT=265;
public final static short DO=266;
public final static short WHILE=267;
public final static short ASIG=268;
public final static short IF=269;
public final static short IGUAL=270;
public final static short NOIGUAL=271;
public final static short MENORIGUAL=272;
public final static short MAYORIGUAL=273;
public final static short FLECHA=274;
public final static short UINT=275;
public final static short CR=276;
public final static short TRUNC=277;
public final static short YYERRCODE=256;
final static short yylhs[] = {                           -1,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    1,    1,    1,    2,    2,    2,
    4,    4,    3,    3,    3,    3,    3,    5,    6,   15,
   15,   15,    8,   18,   18,   18,   18,   18,   18,   19,
   19,   19,   19,   19,   19,   19,   20,   20,   20,   20,
   20,   20,   20,   20,   23,   23,   17,   17,   21,   24,
   24,   24,   24,   24,   24,   24,   10,   10,   16,   16,
   26,   26,   26,    9,    9,    9,    9,    9,    9,    9,
    9,    9,    9,    9,    9,   30,   32,   31,   27,   27,
   27,   27,   33,   33,   33,   33,   33,   33,   33,   34,
   34,   36,   37,    7,    7,    7,    7,   35,   35,   35,
   35,   35,   14,   38,   38,   39,   39,   11,   25,   13,
   13,   13,   13,   13,   13,   13,   40,   41,   28,   29,
   29,   29,   29,   22,   22,   22,   22,   43,   43,   42,
   12,   44,   44,   44,   45,   45,   45,
};
final static short yylen[] = {                            2,
    4,    3,    3,    3,    2,    3,    2,    4,    3,    3,
    3,    2,    2,    2,    1,    2,    2,    1,    1,    1,
    1,    1,    1,    1,    1,    1,    1,    1,    3,    1,
    3,    2,    4,    3,    3,    2,    3,    3,    1,    3,
    3,    3,    3,    2,    2,    1,    1,    1,    1,    1,
    4,    3,    3,    3,    1,    2,    3,    1,    4,    3,
    2,    1,    5,    4,    3,    1,    5,    4,    1,    1,
    1,    1,    1,   11,    8,    7,   10,    7,    6,    9,
    5,    4,    7,    4,    3,    0,    0,    0,    5,    4,
    4,    3,    1,    1,    1,    1,    1,    1,    1,    1,
    2,    0,    0,   10,    7,    7,    6,    3,    2,    5,
    4,    1,    1,    1,    2,    1,    1,    5,    1,    9,
    6,    6,    6,    5,    5,    4,    0,    0,    0,    3,
    2,    1,    3,    9,    8,    8,    7,    3,    3,    1,
    4,    1,    3,    2,    1,    3,    2,
};
final static short yydefred[] = {                         0,
    0,    0,    0,    0,    0,    0,    0,    0,  129,    0,
  113,   13,    0,   15,   18,   19,   20,   21,   22,   23,
   24,   25,   26,   27,   28,    0,    0,    0,    0,    0,
    0,    0,    0,   17,    0,    0,    0,  127,    0,   49,
    0,    0,    0,    0,    0,   47,    0,    0,   46,   48,
   50,    0,    6,   16,    0,    0,    0,    0,    0,    0,
  144,   11,    0,    0,    4,    9,    2,   57,   73,   72,
    0,    0,    0,    0,    0,    0,    0,   55,    0,    0,
    0,    0,   45,   44,  129,    0,   99,   93,   94,   95,
   96,    0,    0,   97,   98,    0,    0,    0,   70,    0,
    0,   69,   85,    0,    0,  112,    0,    0,    0,    0,
   32,    0,   29,    0,  143,  145,    0,    8,    1,   68,
    0,    0,    0,    0,  132,    0,    0,    0,   66,    0,
    0,    0,   56,   53,   54,    0,   52,    0,    0,   37,
    0,   38,    0,    0,   43,   40,   42,   41,    0,   84,
   88,   82,    0,    0,    0,    0,    0,  109,    0,    0,
   31,   33,  147,    0,  141,   67,  118,    0,    0,  131,
  100,    0,  124,    0,    0,    0,   59,    0,   51,  140,
    0,    0,   91,   81,    0,    0,    0,    0,    0,  108,
  117,  116,    0,  114,    0,    0,    0,  146,  121,  133,
  130,  101,    0,  123,    0,   60,    0,   89,    0,    0,
    0,   79,    0,  103,  107,  115,    0,  111,    0,   87,
    0,    0,    0,   83,    0,    0,   78,   88,   76,  106,
    0,  110,  105,    0,   63,    0,    0,    0,  137,    0,
   75,    0,    0,  120,    0,  136,  135,    0,    0,   80,
    0,    0,  134,  138,  139,    0,   77,  104,   74,
};
final static short yydgoto[] = {                          4,
   13,   14,   15,   16,   17,   18,   19,   20,   21,   22,
   23,   24,   25,   26,   57,  103,   27,   47,   48,   49,
   50,   51,   81,  131,  132,   74,   52,   38,  128,  186,
  185,  187,   96,  172,  110,  155,  231,  193,  194,   76,
  220,  181,  239,   28,  117,
};
final static short yysindex[] = {                       -90,
  320,  271,  520,    0,  -50,  -30,    9,   19,    0,  144,
    0,    0,  245,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,  -20, -191,  -34,  -22,  542,
  322,   -9,  370,    0, -176,  -28,  153,    0,  -14,    0,
  -16,  159,  167,  167,  138,    0,  408,   29,    0,    0,
    0,  -51,    0,    0,   49,  179,   21,  153, -156, -147,
    0,    0,   -7,  393,    0,    0,    0,    0,    0,    0,
 -152,  -37,  134,   89,   53,  -36,  -12,    0, -112,   -5,
  110,   29,    0,    0,    0,  408,    0,    0,    0,    0,
    0,  192,  208,    0,    0,  153,  131,  185,    0,   90,
  -37,    0,    0,  210,  119,    0, -152,   59,  -93,  170,
    0,  -67,    0,   -4,    0,    0,  -41,    0,    0,    0,
  -37,  -37,  144,  433,    0, -191,  -37,   57,    0,  134,
  212,  -76,    0,    0,    0,  171,    0,  -49,  153,    0,
   29,    0,   29,  113,    0,    0,    0,    0,  -37,    0,
    0,    0,    0,  136, -131,   24,  292,    0, -136,  164,
    0,    0,    0,   30,    0,    0,    0,  -37,  180,    0,
    0,  476,    0,  144,  -37,  153,    0,   55,    0,    0,
  265,  561,    0,    0,  210,   51,  240,  292,  316,    0,
    0,    0,  273,    0, -152,   66,  292,    0,    0,    0,
    0,    0,    0,    0,   62,    0,  306,    0,  -37,  242,
  -37,    0,  441,    0,    0,    0,   70,    0,  456,    0,
   71,  -94,  220,    0,   76,  -37,    0,    0,    0,    0,
  229,    0,    0,  -37,    0,  260,  329,  -54,    0,  -37,
    0,  210,  292,    0,  329,    0,    0,  341,  350,    0,
  243,  473,    0,    0,    0,  -37,    0,    0,    0,
};
final static short yyrindex[] = {                         0,
    0,    0,    0,    0,    0,  -44,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,  -10,    0,  397,    0,
  407,  438,  439,    0,    0,    0,    0,    0,    1,    0,
    0,    0,    0,    0,    0,    0,    0,   23,    0,    0,
    0,  403,    0,    0,   91,    0,    0,    0,    0,    0,
    0,    0,  452,  471,    0,    0,    0,    0,    0,    0,
    0,    0,  352,    0,    0,    0,    0,    0,    0,    0,
    0,   47,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,  403,
    0,    0,    0,    0, -114,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,  414,  157,    0,    0,  -39,
    0,  317,    0,    0,    0,    0,    0,    0,    0,    0,
   69,    0,   93,  115,    0,    0,    0,    0,    0,    0,
    0,    0,  232,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,  165,    0,    0,  419,    0,    0,
    0,  143,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,  117,    0,  437,    0,    0,    0,  172,  403,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
  446,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,
};
final static short yygindex[] = {                         0,
   54,   41,  415,  575,    0,    0,    0,    0,    0,    0,
    0,    0,    0,  569,    0,  443,  664,  713,   28,   35,
    0,    0,  357,    0,  270,    0,   -1,  -27,  -83,    0,
  252, -101,  398, -169,  339,    0,    0, -102,  293,    0,
    0,    0,  -48,    0,    0,
};
final static int YYTABLESIZE=889;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                         58,
   58,  119,  164,   45,  119,   43,   42,  102,   34,   59,
   44,   71,   72,   43,   42,   35,   58,  102,   44,   56,
  153,  102,   39,   80,  104,   77,   60,   71,   79,   43,
   42,   35,    3,  142,   44,  135,   34,  223,   92,   79,
   93,   58,   58,   58,   58,   58,   36,   58,   36,   34,
  142,   34,  236,   54,  102,   31,   33,  138,   37,   58,
   58,   58,   58,   39,  112,   39,   39,   39,   34,   82,
   97,   54,  151,   54,  127,   98,   58,   83,   84,  102,
   68,   39,   39,   64,   39,  213,  124,   36,  105,   36,
   36,   36,   35,  122,  219,   92,   45,   93,   43,   42,
    6,  209,   62,   44,   54,   36,   36,  225,   36,   34,
  116,   34,   34,   34,   92,   66,  122,  118,  234,  141,
  143,  168,   11,   58,  106,   58,  175,   34,   34,  121,
   34,  146,  148,   35,   30,   35,   35,   35,   11,  195,
  252,  102,   90,   11,  107,   39,  133,   39,  102,   30,
  137,   35,   35,  183,   35,   92,  126,   93,  251,  154,
  102,  102,    6,  158,  125,    1,    2,    7,    8,   36,
   71,   36,  203,   92,   10,  128,   92,   71,   93,   43,
   42,  157,  228,   45,   44,   43,   42,  246,  247,  161,
   44,   34,   71,   34,   43,   42,  253,  178,   71,   44,
   43,   90,  248,  249,   99,   44,   71,  180,  100,  101,
  160,  179,   58,  159,   99,   35,  163,   35,   99,  108,
   39,   40,    6,   58,   71,    7,    8,   69,   39,   40,
  123,   71,   10,   43,  119,   70,   55,   92,   44,   92,
   41,  122,   78,  129,   39,   40,  142,   71,   41,   43,
  134,   99,  177,   78,   44,  176,   58,   58,  188,  238,
   58,   58,   58,   58,   41,   90,   58,   90,   58,   58,
   58,   58,   58,   58,   58,   58,   99,  111,   39,   39,
  190,  126,   39,   39,   39,   39,  197,  198,   39,  125,
   87,   39,   39,   39,   39,   39,   39,   39,  102,  238,
  102,  102,   36,   36,  200,  207,   36,   36,   36,   36,
  210,  206,   36,   39,   40,   36,   36,   36,   36,   36,
   36,   36,  218,  174,   34,   34,  232,  235,   34,   34,
   34,   34,  124,   41,   34,  221,  240,   34,   34,   34,
   34,   34,   34,   34,  237,   99,   30,   30,   35,   35,
  149,  243,   35,   35,   35,   35,  214,   62,   35,  159,
   62,   35,   35,   35,   35,   35,   35,   35,  238,   53,
   92,   92,  128,  122,   92,   92,   92,   92,  122,  122,
   92,  254,  122,   92,  245,  122,  145,   39,   40,   92,
  255,  122,   71,   30,   39,   40,   14,  215,   90,   90,
   39,   40,   90,   90,   90,   90,    7,   41,   90,   39,
   40,   90,   11,  126,   41,   39,   40,   90,  126,  126,
   41,  125,  126,   39,   40,  126,  125,  125,  222,   41,
  125,  126,   87,  125,  106,   41,  136,   12,    5,  125,
  147,   39,   40,   41,   12,  205,   65,  140,   39,   40,
   92,   10,   93,   11,  107,   47,   47,  142,   47,   61,
   47,   41,   61,  142,   39,   40,    6,   94,   41,   95,
    3,    7,    8,   47,  142,   47,    6,   65,   10,  242,
   65,    7,    8,  139,   41,  216,   64,   87,   10,   64,
  125,   86,   87,  189,   67,   99,    0,   99,   99,  113,
  211,    6,  226,  256,    0,  216,    7,    8,    0,    0,
    9,  216,    0,   10,  120,    0,    6,  119,  125,   11,
    0,    7,    8,    0,    0,  129,   29,    6,   10,    6,
    0,    0,    7,    8,    7,    8,    9,    0,  171,   10,
    0,   10,  150,  152,  216,   11,    0,   11,    6,    0,
    0,    0,    0,    7,    8,    0,  162,  170,    0,  165,
   10,    0,    6,  166,  167,  230,   11,    7,    8,  173,
    0,  191,    0,    0,   10,    5,    6,    0,    6,    0,
  233,    7,    8,    7,    8,    9,  202,    9,   10,    0,
   10,  184,    0,    0,   11,    0,   11,  258,    0,  125,
  201,  208,  191,   92,    0,   93,    0,  191,    0,    0,
  199,  191,    0,   85,    0,    0,    0,  204,    0,    0,
    0,  171,    0,    0,  109,    0,    6,  191,    0,  212,
    0,    7,    8,  191,    0,    9,  171,  202,   10,   85,
    0,    0,    0,    0,   11,    0,    0,    0,    0,    6,
  202,  224,  227,  229,    7,    8,  125,  191,    9,  129,
    0,   10,    0,   87,  129,  129,  191,   11,  241,   47,
  142,  129,    0,   46,    0,  156,  244,   88,   89,   90,
   91,    0,  250,   47,   47,   47,   47,    0,  169,    6,
    0,   61,    0,  257,    7,    8,    0,    6,  259,   46,
   46,   10,    7,    8,    0,   46,   46,   46,   46,   10,
    0,    0,    6,    0,    0,   11,    0,    7,    8,    0,
    0,   46,  115,  109,   10,    0,    0,  196,    0,    6,
   11,  192,    6,    0,    7,    8,    0,    7,    8,  126,
   46,   10,    0,    0,   10,    0,    0,   11,   73,   75,
    0,    0,    0,    0,    0,   46,   46,   86,    0,   46,
   46,   46,  192,  217,    0,    0,    0,  192,    0,    0,
  114,  192,    0,    0,    0,   32,    6,    0,    0,    0,
    0,    7,    8,    0,    0,    9,   46,  192,   10,  130,
    0,   46,    0,  192,   11,    0,    0,   63,    6,    0,
    0,    0,   46,    7,    8,    0,    0,    9,  144,    0,
   10,    0,    0,    0,    0,    0,   11,  192,    0,    0,
    0,    0,    0,    0,    0,    0,  192,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,   46,    0,   46,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,  182,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,  130,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                         44,
    0,   41,   44,   40,   44,   42,   43,   59,   59,   44,
   47,   40,   41,   42,   43,   46,   61,   59,   47,   40,
  104,   59,    0,   40,   52,   40,   61,   40,   45,   42,
   43,   46,  123,   44,   47,   41,   59,  207,   43,   45,
   45,   41,   42,   43,   44,   45,    0,   47,   40,   59,
   61,   59,  222,   13,   59,    2,    3,   85,   40,   59,
   60,   61,   62,   41,   44,   43,   44,   45,    0,   42,
   42,   31,  100,   33,   76,   47,  268,   43,   44,   59,
  257,   59,   60,   30,   62,  188,  123,   41,   40,   43,
   44,   45,    0,   41,  197,   43,   40,   45,   42,   43,
  257,  185,  125,   47,   64,   59,   60,  209,   62,   41,
  258,   43,   44,   45,    0,  125,    0,  125,  220,   92,
   93,  123,  275,  123,  256,  125,  128,   59,   60,   41,
   62,   97,   98,   41,   44,   43,   44,   45,  275,  276,
  243,  256,    0,  275,  276,  123,  259,  125,   59,   59,
   41,   59,   60,   41,   62,   43,    0,   45,  242,   41,
  275,  276,  257,  257,    0,  256,  257,  262,  263,  123,
   40,  125,  174,   59,  269,   59,   43,   40,   45,   42,
   43,  123,  210,   40,   47,   42,   43,  236,  237,  257,
   47,  123,   40,  125,   42,   43,  245,  274,   40,   47,
   42,   59,  257,  258,  256,   47,   40,  257,  260,  261,
   41,   41,  257,   44,  256,  123,  258,  125,  256,   41,
  257,  258,  257,  268,   40,  262,  263,  256,  257,  258,
  267,   40,  269,   42,  274,  264,  257,  123,   47,  125,
  277,  125,  259,  256,  257,  258,  257,   40,  277,   42,
  256,  256,   41,  259,   47,   44,  256,  257,  123,   40,
  260,  261,  262,  263,  277,  123,  266,  125,  268,  269,
  270,  271,  272,  273,  274,  275,  256,  257,  256,  257,
  257,  125,  260,  261,  262,  263,  123,  258,  266,  125,
   59,  269,  270,  271,  272,  273,  274,  275,   59,   40,
   59,   59,  256,  257,  125,   41,  260,  261,  262,  263,
  260,  257,  266,  257,  258,  269,  270,  271,  272,  273,
  274,  275,  257,  267,  256,  257,  257,  257,  260,  261,
  262,  263,  123,  277,  266,  274,  261,  269,  270,  271,
  272,  273,  274,  275,  125,  256,  256,  257,  256,  257,
  261,  123,  260,  261,  262,  263,   41,   41,  266,   44,
   44,  269,  270,  271,  272,  273,  274,  275,   40,  125,
  256,  257,  256,  257,  260,  261,  262,  263,  262,  263,
  266,   41,  266,  269,  125,  269,  256,  257,  258,  275,
   41,  275,   41,  123,  257,  258,    0,  125,  256,  257,
  257,  258,  260,  261,  262,  263,    0,  277,  266,  257,
  258,  269,  275,  257,  277,  257,  258,  275,  262,  263,
  277,  257,  266,  257,  258,  269,  262,  263,  123,  277,
  266,  275,  261,  269,  256,  277,   80,    0,    0,  275,
  256,  257,  258,  277,  125,  176,  125,  256,  257,  258,
   43,    0,   45,  275,  276,   42,   43,   44,   45,   41,
   47,  277,   44,  256,  257,  258,  257,   60,  277,   62,
    0,  262,  263,   60,   61,   62,  257,   41,  269,  228,
   44,  262,  263,   86,  277,  193,   41,  256,  269,   44,
   76,  260,  261,  155,  125,  256,   -1,  256,  256,   57,
  261,  257,  261,  261,   -1,  213,  262,  263,   -1,   -1,
  266,  219,   -1,  269,   72,   -1,  257,  125,  104,  275,
   -1,  262,  263,   -1,   -1,  123,  256,  257,  269,  257,
   -1,   -1,  262,  263,  262,  263,  266,   -1,  124,  269,
   -1,  269,  100,  101,  252,  275,   -1,  275,  257,   -1,
   -1,   -1,   -1,  262,  263,   -1,  114,  125,   -1,  117,
  269,   -1,  257,  121,  122,  125,  275,  262,  263,  127,
   -1,  157,   -1,   -1,  269,  256,  257,   -1,  257,   -1,
  125,  262,  263,  262,  263,  266,  172,  266,  269,   -1,
  269,  149,   -1,   -1,  275,   -1,  275,  125,   -1,  185,
  125,   41,  188,   43,   -1,   45,   -1,  193,   -1,   -1,
  168,  197,   -1,   45,   -1,   -1,   -1,  175,   -1,   -1,
   -1,  207,   -1,   -1,   56,   -1,  257,  213,   -1,  187,
   -1,  262,  263,  219,   -1,  266,  222,  223,  269,   71,
   -1,   -1,   -1,   -1,  275,   -1,   -1,   -1,   -1,  257,
  236,  209,  210,  211,  262,  263,  242,  243,  266,  257,
   -1,  269,   -1,  256,  262,  263,  252,  275,  226,  256,
  257,  269,   -1,   10,   -1,  107,  234,  270,  271,  272,
  273,   -1,  240,  270,  271,  272,  273,   -1,  256,  257,
   -1,   28,   -1,  251,  262,  263,   -1,  257,  256,   36,
   37,  269,  262,  263,   -1,   42,   43,   44,   45,  269,
   -1,   -1,  257,   -1,   -1,  275,   -1,  262,  263,   -1,
   -1,   58,   59,  155,  269,   -1,   -1,  159,   -1,  257,
  275,  157,  257,   -1,  262,  263,   -1,  262,  263,   76,
   77,  269,   -1,   -1,  269,   -1,   -1,  275,   36,   37,
   -1,   -1,   -1,   -1,   -1,   92,   93,   45,   -1,   96,
   97,   98,  188,  195,   -1,   -1,   -1,  193,   -1,   -1,
   58,  197,   -1,   -1,   -1,  256,  257,   -1,   -1,   -1,
   -1,  262,  263,   -1,   -1,  266,  123,  213,  269,   77,
   -1,  128,   -1,  219,  275,   -1,   -1,  256,  257,   -1,
   -1,   -1,  139,  262,  263,   -1,   -1,  266,   96,   -1,
  269,   -1,   -1,   -1,   -1,   -1,  275,  243,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,  252,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,  174,   -1,  176,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,  139,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,  176,
};
}
final static short YYFINAL=4;
final static short YYMAXTOKEN=277;
final static String yyname[] = {
"end-of-file",null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,"'('","')'","'*'","'+'","','",
"'-'","'.'","'/'",null,null,null,null,null,null,null,null,null,null,null,"';'",
"'<'","'='","'>'",null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
"'{'",null,"'}'",null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,"ID","CTE","PF64","ELSE","ENDIF","PRINT",
"RETURN","CADENA","DFLOAT","DO","WHILE","ASIG","IF","IGUAL","NOIGUAL",
"MENORIGUAL","MAYORIGUAL","FLECHA","UINT","CR","TRUNC",
};
final static String yyrule[] = {
"$accept : prog",
"prog : ID '{' lista_sentencia '}'",
"prog : '{' lista_sentencia '}'",
"prog : ID '{' lista_sentencia",
"prog : ID lista_sentencia '}'",
"prog : '{' lista_sentencia",
"prog : error lista_sentencia '}'",
"prog : ID lista_sentencia",
"prog : ID '{' error '}'",
"prog : '{' error '}'",
"prog : ID '{' error",
"prog : ID error '}'",
"prog : '{' error",
"prog : error '}'",
"prog : ID error",
"lista_sentencia : sentencia",
"lista_sentencia : lista_sentencia sentencia",
"lista_sentencia : error ';'",
"sentencia : sentencia_ejecutable",
"sentencia : sentencia_declarativa",
"sentencia : sentencia_de_control",
"sentencia_declarativa : declaracion",
"sentencia_declarativa : funcion",
"sentencia_ejecutable : asig",
"sentencia_ejecutable : if",
"sentencia_ejecutable : salida_msj",
"sentencia_ejecutable : return",
"sentencia_ejecutable : asig_multiple",
"sentencia_de_control : do",
"declaracion : tipo lista_variables_declaracion punto_coma",
"lista_variables_declaracion : ID",
"lista_variables_declaracion : lista_variables_declaracion ',' ID",
"lista_variables_declaracion : lista_variables_declaracion ID",
"asig : variable ASIG exp punto_coma",
"exp : exp '+' termino",
"exp : exp '-' termino",
"exp : '+' termino",
"exp : exp '+' error",
"exp : exp '-' error",
"exp : termino",
"termino : termino '*' factor",
"termino : termino '/' factor",
"termino : termino '/' error",
"termino : termino '*' error",
"termino : '/' factor",
"termino : '*' factor",
"termino : factor",
"factor : variable",
"factor : invocacion",
"factor : CTE",
"factor : exp_lambda",
"factor : TRUNC '(' punto_flotante ')'",
"factor : TRUNC punto_flotante ')'",
"factor : TRUNC '(' error",
"factor : TRUNC '(' ')'",
"punto_flotante : PF64",
"punto_flotante : '-' PF64",
"variable : ID '.' ID",
"variable : ID",
"invocacion : ID '(' parametros_de_invocacion ')'",
"parametros_de_invocacion : parametro_real FLECHA ID",
"parametros_de_invocacion : parametro_real FLECHA",
"parametros_de_invocacion : parametro_real",
"parametros_de_invocacion : parametros_de_invocacion ',' parametro_real FLECHA ID",
"parametros_de_invocacion : parametros_de_invocacion ',' parametro_real FLECHA",
"parametros_de_invocacion : parametros_de_invocacion ',' parametro_real",
"parametros_de_invocacion : error",
"salida_msj : PRINT '(' argumento_print ')' punto_coma",
"salida_msj : PRINT '(' ')' punto_coma",
"punto_coma : ';'",
"punto_coma : error",
"argumento_print : exp",
"argumento_print : CADENA",
"argumento_print : error",
"if : IF cuerpo_condicion nuevo_ambito_ua cuerpo_sentencia_ejecutable completar_bf_else ELSE nuevo_ambito_ua crear_bi cuerpo_sentencia_ejecutable ENDIF punto_coma",
"if : IF cuerpo_condicion nuevo_ambito_ua cuerpo_sentencia_ejecutable completar_bf_else ELSE ENDIF punto_coma",
"if : IF cuerpo_condicion nuevo_ambito_ua cuerpo_sentencia_ejecutable completar_bf ENDIF punto_coma",
"if : IF cuerpo_condicion nuevo_ambito_ua cuerpo_sentencia_ejecutable completar_bf_else ELSE nuevo_ambito_ua crear_bi cuerpo_sentencia_ejecutable punto_coma",
"if : IF cuerpo_condicion nuevo_ambito_ua cuerpo_sentencia_ejecutable completar_bf_else ELSE punto_coma",
"if : IF cuerpo_condicion nuevo_ambito_ua cuerpo_sentencia_ejecutable completar_bf punto_coma",
"if : IF cuerpo_condicion ELSE nuevo_ambito_ua crear_bi cuerpo_sentencia_ejecutable completar_bf ENDIF punto_coma",
"if : IF cuerpo_condicion ELSE ENDIF punto_coma",
"if : IF cuerpo_condicion ENDIF punto_coma",
"if : IF cuerpo_condicion ELSE nuevo_ambito_ua crear_bi cuerpo_sentencia_ejecutable punto_coma",
"if : IF cuerpo_condicion ELSE punto_coma",
"if : IF cuerpo_condicion punto_coma",
"completar_bf_else :",
"completar_bf :",
"crear_bi :",
"cuerpo_condicion : '(' exp comparador exp ')'",
"cuerpo_condicion : '(' exp comparador exp",
"cuerpo_condicion : exp comparador exp ')'",
"cuerpo_condicion : exp comparador exp",
"comparador : IGUAL",
"comparador : NOIGUAL",
"comparador : MENORIGUAL",
"comparador : MAYORIGUAL",
"comparador : '<'",
"comparador : '>'",
"comparador : error",
"lista_sentencia_ejecutable : sentencia_ejecutable",
"lista_sentencia_ejecutable : lista_sentencia_ejecutable sentencia_ejecutable",
"$$1 :",
"$$2 :",
"funcion : tipo ID '(' $$1 parametros_formales ')' $$2 '{' lista_sentencia_funcion '}'",
"funcion : tipo '(' parametros_formales ')' '{' lista_sentencia_funcion '}'",
"funcion : tipo ID '(' ')' '{' lista_sentencia_funcion '}'",
"funcion : tipo '(' ')' '{' lista_sentencia_funcion '}'",
"parametros_formales : CR tipo ID",
"parametros_formales : tipo ID",
"parametros_formales : parametros_formales ',' CR tipo ID",
"parametros_formales : parametros_formales ',' tipo ID",
"parametros_formales : error",
"tipo : UINT",
"lista_sentencia_funcion : sentencia_funcion",
"lista_sentencia_funcion : lista_sentencia_funcion sentencia_funcion",
"sentencia_funcion : sentencia_declarativa",
"sentencia_funcion : sentencia_ejecutable",
"return : RETURN '(' exp ')' punto_coma",
"parametro_real : exp",
"do : DO nuevo_ambito_ua inicio_while cuerpo_sentencia_ejecutable WHILE cuerpo_condicion crear_bi_while completar_bf punto_coma",
"do : DO nuevo_ambito_ua inicio_while WHILE cuerpo_condicion punto_coma",
"do : DO nuevo_ambito_ua inicio_while cuerpo_sentencia_ejecutable WHILE cuerpo_condicion",
"do : DO nuevo_ambito_ua inicio_while cuerpo_sentencia_ejecutable cuerpo_condicion punto_coma",
"do : DO nuevo_ambito_ua inicio_while cuerpo_condicion punto_coma",
"do : DO nuevo_ambito_ua inicio_while cuerpo_sentencia_ejecutable cuerpo_condicion",
"do : DO nuevo_ambito_ua inicio_while cuerpo_condicion",
"inicio_while :",
"crear_bi_while :",
"nuevo_ambito_ua :",
"cuerpo_sentencia_ejecutable : '{' lista_sentencia_ejecutable '}'",
"cuerpo_sentencia_ejecutable : '{' '}'",
"cuerpo_sentencia_ejecutable : sentencia_ejecutable",
"cuerpo_sentencia_ejecutable : '{' error '}'",
"exp_lambda : '(' tipo nuevo_ambito_ua validar_id ')' '{' lista_sentencia_ejecutable '}' argumento_lambda",
"exp_lambda : '(' tipo nuevo_ambito_ua validar_id ')' lista_sentencia_ejecutable '}' argumento_lambda",
"exp_lambda : '(' tipo nuevo_ambito_ua validar_id ')' '{' lista_sentencia_ejecutable argumento_lambda",
"exp_lambda : '(' tipo nuevo_ambito_ua validar_id ')' lista_sentencia_ejecutable argumento_lambda",
"argumento_lambda : '(' ID ')'",
"argumento_lambda : '(' CTE ')'",
"validar_id : ID",
"asig_multiple : lista_variables '=' lista_constantes punto_coma",
"lista_variables : variable",
"lista_variables : lista_variables ',' variable",
"lista_variables : lista_variables variable",
"lista_constantes : CTE",
"lista_constantes : lista_constantes ',' CTE",
"lista_constantes : lista_constantes CTE",
};

//#line 507 "grama.y"
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
//#line 747 "Parser.java"
//###############################################################
// method: yylexdebug : check lexer state
//###############################################################
void yylexdebug(int state,int ch)
{
String s=null;
  if (ch < 0) ch=0;
  if (ch <= YYMAXTOKEN) //check index bounds
     s = yyname[ch];    //now get it
  if (s==null)
    s = "illegal-symbol";
  debug("state "+state+", reading "+ch+" ("+s+")");
}





//The following are now global, to aid in error reporting
int yyn;       //next next thing to do
int yym;       //
int yystate;   //current parsing state from state table
String yys;    //current token string


//###############################################################
// method: yyparse : parse input and execute indicated items
//###############################################################
int yyparse()
{
boolean doaction;
  init_stacks();
  yynerrs = 0;
  yyerrflag = 0;
  yychar = -1;          //impossible char forces a read
  yystate=0;            //initial state
  state_push(yystate);  //save it
  val_push(yylval);     //save empty value
  while (true) //until parsing is done, either correctly, or w/error
    {
    doaction=true;
    if (yydebug) debug("loop"); 
    //#### NEXT ACTION (from reduction table)
    for (yyn=yydefred[yystate];yyn==0;yyn=yydefred[yystate])
      {
      if (yydebug) debug("yyn:"+yyn+"  state:"+yystate+"  yychar:"+yychar);
      if (yychar < 0)      //we want a char?
        {
        yychar = yylex();  //get next token
        if (yydebug) debug(" next yychar:"+yychar);
        //#### ERROR CHECK ####
        if (yychar < 0)    //it it didn't work/error
          {
          yychar = 0;      //change it to default string (no -1!)
          if (yydebug)
            yylexdebug(yystate,yychar);
          }
        }//yychar<0
      yyn = yysindex[yystate];  //get amount to shift by (shift index)
      if ((yyn != 0) && (yyn += yychar) >= 0 &&
          yyn <= YYTABLESIZE && yycheck[yyn] == yychar)
        {
        if (yydebug)
          debug("state "+yystate+", shifting to state "+yytable[yyn]);
        //#### NEXT STATE ####
        yystate = yytable[yyn];//we are in a new state
        state_push(yystate);   //save it
        val_push(yylval);      //push our lval as the input for next rule
        yychar = -1;           //since we have 'eaten' a token, say we need another
        if (yyerrflag > 0)     //have we recovered an error?
           --yyerrflag;        //give ourselves credit
        doaction=false;        //but don't process yet
        break;   //quit the yyn=0 loop
        }

    yyn = yyrindex[yystate];  //reduce
    if ((yyn !=0 ) && (yyn += yychar) >= 0 &&
            yyn <= YYTABLESIZE && yycheck[yyn] == yychar)
      {   //we reduced!
      if (yydebug) debug("reduce");
      yyn = yytable[yyn];
      doaction=true; //get ready to execute
      break;         //drop down to actions
      }
    else //ERROR RECOVERY
      {
      if (yyerrflag==0)
        {
        yyerror("syntax error");
        yynerrs++;
        }
      if (yyerrflag < 3) //low error count?
        {
        yyerrflag = 3;
        while (true)   //do until break
          {
          if (stateptr<0)   //check for under & overflow here
            {
            yyerror("stack underflow. aborting...");  //note lower case 's'
            return 1;
            }
          yyn = yysindex[state_peek(0)];
          if ((yyn != 0) && (yyn += YYERRCODE) >= 0 &&
                    yyn <= YYTABLESIZE && yycheck[yyn] == YYERRCODE)
            {
            if (yydebug)
              debug("state "+state_peek(0)+", error recovery shifting to state "+yytable[yyn]+" ");
            yystate = yytable[yyn];
            state_push(yystate);
            val_push(yylval);
            doaction=false;
            break;
            }
          else
            {
            if (yydebug)
              debug("error recovery discarding state "+state_peek(0)+" ");
            if (stateptr<0)   //check for under & overflow here
              {
              yyerror("Stack underflow. aborting...");  //capital 'S'
              return 1;
              }
            state_pop();
            val_pop();
            }
          }
        }
      else            //discard this token
        {
        if (yychar == 0)
          return 1; //yyabort
        if (yydebug)
          {
          yys = null;
          if (yychar <= YYMAXTOKEN) yys = yyname[yychar];
          if (yys == null) yys = "illegal-symbol";
          debug("state "+yystate+", error recovery discards token "+yychar+" ("+yys+")");
          }
        yychar = -1;  //read another
        }
      }//end error recovery
    }//yyn=0 loop
    if (!doaction)   //any reason not to proceed?
      continue;      //skip action
    yym = yylen[yyn];          //get count of terminals on rhs
    if (yydebug)
      debug("state "+yystate+", reducing "+yym+" by rule "+yyn+" ("+yyrule[yyn]+")");
    if (yym>0)                 //if count of rhs not 'nil'
      yyval = val_peek(yym-1); //get current semantic value
    yyval = dup_yyval(yyval); //duplicate yyval if ParserVal is used as semantic value
    switch(yyn)
      {
//########## USER-SUPPLIED ACTIONS ##########
case 1:
//#line 17 "grama.y"
{
        Compilador.salirAmbito();
        String ambito = Compilador.getAmbito();
        String var = val_peek(3).sval;
        if(TablaDeSimbolos.agregarVar(var, ambito, tipo, "nombre de variable")){
           yyval = new ParserVal(var+ambito);
        } else yyerror("La variable "+var+" ya fue declarada");
    }
break;
case 2:
//#line 25 "grama.y"
{yyerror("falta nombre de programa");}
break;
case 3:
//#line 26 "grama.y"
{yyerror("falta llave de cierre de programa");}
break;
case 4:
//#line 27 "grama.y"
{yyerror("falta llave de inicio de programa");}
break;
case 5:
//#line 28 "grama.y"
{yyerror("falta nombre del programa");yyerror("falta llave de cierre de programa");}
break;
case 6:
//#line 29 "grama.y"
{yyerror("falta nombre del programa");yyerror("falta llave de inicio de programa");}
break;
case 7:
//#line 30 "grama.y"
{yyerror("no hay llaves del programa");}
break;
case 8:
//#line 32 "grama.y"
{yyerror("error en programa");}
break;
case 9:
//#line 33 "grama.y"
{yyerror("falta nombre de programa");yyerror("error en programa");}
break;
case 10:
//#line 34 "grama.y"
{yyerror("falta llave de cierre de programa");yyerror("error en programa");}
break;
case 11:
//#line 35 "grama.y"
{yyerror("falta llave de inicio de programa");yyerror("error en programa");}
break;
case 12:
//#line 36 "grama.y"
{yyerror("falta nombre del programa");yyerror("falta llave de cierre de programa");yyerror("error en programa");}
break;
case 13:
//#line 37 "grama.y"
{yyerror("falta nombre del programa");yyerror("error en programa");}
break;
case 14:
//#line 38 "grama.y"
{yyerror("no hay llaves del programa");yyerror("error en programa");}
break;
case 17:
//#line 43 "grama.y"
{yyerror("error en sentencia");}
break;
case 21:
//#line 53 "grama.y"
{addEstructura("declaracion de variable");}
break;
case 22:
//#line 54 "grama.y"
{addEstructura("declaracion de funcion");}
break;
case 23:
//#line 58 "grama.y"
{addEstructura("asignacion");}
break;
case 24:
//#line 59 "grama.y"
{addEstructura("sentencia if");}
break;
case 25:
//#line 60 "grama.y"
{addEstructura("print");}
break;
case 26:
//#line 61 "grama.y"
{addEstructura("return");}
break;
case 27:
//#line 62 "grama.y"
{addEstructura("asignacion multiple");}
break;
case 28:
//#line 66 "grama.y"
{addEstructura("sentencia do while");}
break;
case 30:
//#line 76 "grama.y"
{
        String ambito = Compilador.getAmbito();
        String var = val_peek(0).sval;
        if(TablaDeSimbolos.agregarVar(var, ambito, tipo, "nombre de variable")){
           yyval = new ParserVal(var+ambito);
        } else yyerror("La variable "+var+" ya fue declarada");

    }
break;
case 31:
//#line 85 "grama.y"
{
        String ambito = Compilador.getAmbito();
        String var = val_peek(0).sval;
        if(TablaDeSimbolos.agregarVar(var, ambito, tipo, "nombre de variable")){
           yyval = new ParserVal(val_peek(2).sval+","+var);
        } else yyerror("La variable "+var+" ya fue declarada");
    }
break;
case 32:
//#line 92 "grama.y"
{yyerror("falta , en lista de variables");}
break;
case 33:
//#line 97 "grama.y"
{
        /*if(TablaDeSimbolos.esCompatible($1.sval,$3.sval, Compilador.getAmbito())){*/
          /*  System.out.println("val1:"+$1.sval+" val2:"+$3.sval);*/
          /*  crearTerceto(":=", $1.sval, $3.sval);*/
        /*} else yyerror("Los tipos de las variables no coinciden");*/

        String ambito = Compilador.getAmbito();
        String var = val_peek(3).sval;
        if(TablaDeSimbolos.varDeclarada(var, ambito)){
            crearTerceto(":=", var, val_peek(1).sval);
        }
    }
break;
case 34:
//#line 114 "grama.y"
{
        crearTerceto("+", val_peek(2).sval, val_peek(0).sval);
        int t = Compilador.tercetos.size() - 1;
        yyval=new ParserVal("[" + t + "]");
    }
break;
case 35:
//#line 120 "grama.y"
{
        crearTerceto("-", val_peek(2).sval, val_peek(0).sval);
        int t = Compilador.tercetos.size() - 1;
        yyval=new ParserVal("[" + t + "]");
    }
break;
case 36:
//#line 125 "grama.y"
{yyerror("falta operando a izquierda de +");}
break;
case 37:
//#line 126 "grama.y"
{yyerror("falta operando a derecha de +");}
break;
case 38:
//#line 127 "grama.y"
{yyerror("falta operando a derecha de -");}
break;
case 39:
//#line 128 "grama.y"
{yyval=val_peek(0);}
break;
case 40:
//#line 133 "grama.y"
{
        crearTerceto("*", val_peek(2).sval, val_peek(0).sval);
        int t = Compilador.tercetos.size() - 1;
        yyval=new ParserVal("[" + t + "]");
    }
break;
case 41:
//#line 139 "grama.y"
{
        crearTerceto("/", val_peek(2).sval, val_peek(0).sval);
        int t = Compilador.tercetos.size() - 1;
        yyval=new ParserVal("[" + t + "]");
    }
break;
case 42:
//#line 144 "grama.y"
{yyerror("falta operando a derecha de /");}
break;
case 43:
//#line 145 "grama.y"
{yyerror("falta operando a derecha de *");}
break;
case 44:
//#line 146 "grama.y"
{yyerror("falta operando a izquierda de /");}
break;
case 45:
//#line 147 "grama.y"
{yyerror("falta operando a izquierda de *");}
break;
case 46:
//#line 148 "grama.y"
{yyval=val_peek(0);}
break;
case 47:
//#line 152 "grama.y"
{yyval=val_peek(0);}
break;
case 48:
//#line 153 "grama.y"
{yyval=val_peek(0);}
break;
case 49:
//#line 154 "grama.y"
{yyval=new ParserVal(val_peek(0).sval);}
break;
case 50:
//#line 155 "grama.y"
{addEstructura("lambda"); yyval=val_peek(0);}
break;
case 51:
//#line 156 "grama.y"
{addEstructura("trunc"); yyval=new ParserVal(truncar(val_peek(1).sval));}
break;
case 52:
//#line 157 "grama.y"
{addEstructura("trunc");yyerror("falta abrir parentesis en trunc");}
break;
case 53:
//#line 158 "grama.y"
{addEstructura("trunc");yyerror("falta cerrar parentesis en trunc");yyerrflag=0;}
break;
case 54:
//#line 159 "grama.y"
{addEstructura("trunc");yyerror("falta argumento en trunc");}
break;
case 55:
//#line 163 "grama.y"
{check_rango(val_peek(0).sval); yyval=new ParserVal(val_peek(0).sval);}
break;
case 56:
//#line 164 "grama.y"
{check_rango("-"+val_peek(0).sval); yyval=new ParserVal("-"+val_peek(1).sval);}
break;
case 57:
//#line 169 "grama.y"
{
        yyval = new ParserVal(val_peek(2).sval + '.' + val_peek(0).sval);
        if(!TablaDeSimbolos.varPrefijadaDeclarada(val_peek(0).sval, val_peek(2).sval)){
            yyerror("La variable "+val_peek(0).sval+" no fue declarada en el ambito " + val_peek(2).sval);
        }
        TablaDeSimbolos.eliminar(val_peek(0).sval);
    }
break;
case 58:
//#line 177 "grama.y"
{
        String ambito = Compilador.getAmbito();
        String var = val_peek(0).sval;
        if(!TablaDeSimbolos.varDeclarada(var, ambito)){
                    yyerror("La variable "+var+" no fue declarada");
        } else{
            TablaDeSimbolos.eliminar(var);
            yyval=new ParserVal (var+ambito);
        }
    }
break;
case 59:
//#line 191 "grama.y"
{
        String ambito = Compilador.getAmbito();
        String var = val_peek(3).sval;
        yyval=new ParserVal(var+ambito+"("+val_peek(1).sval+")");
        if(!TablaDeSimbolos.funcionDeclarada(var,ambito)){
            yyerror("La funcion " + var + " no esta declarada");
        } else{
            TablaDeSimbolos.eliminar(var);
            addEstructura("invocacion a funcion");
        }
    }
break;
case 60:
//#line 206 "grama.y"
{
        yyval=new ParserVal( val_peek(2).sval );
        TablaDeSimbolos.eliminar(val_peek(0).sval);
    }
break;
case 61:
//#line 210 "grama.y"
{yyerror("Falta parametro formal");}
break;
case 62:
//#line 211 "grama.y"
{yyerror("Falta flecha y parametro formal");}
break;
case 63:
//#line 213 "grama.y"
{
        yyval = new ParserVal(val_peek(4).sval+","+val_peek(2).sval);
    }
break;
case 64:
//#line 216 "grama.y"
{yyerror("Falta parametro formal");}
break;
case 65:
//#line 217 "grama.y"
{yyerror("Falta flecha y parametro formal");}
break;
case 66:
//#line 218 "grama.y"
{yyerror("error en parametros de invocacion");}
break;
case 67:
//#line 223 "grama.y"
{
        crearTerceto("print", val_peek(2).sval, "-");
         int t = Compilador.tercetos.size() - 1;
        yyval=new ParserVal("[" + t + "]");
    }
break;
case 68:
//#line 228 "grama.y"
{yyerror("falta argumento en print");}
break;
case 70:
//#line 233 "grama.y"
{yyerror("falta ;");}
break;
case 73:
//#line 239 "grama.y"
{yyerror("argumento invalido en print");}
break;
case 74:
//#line 244 "grama.y"
{
        int bi = Compilador.pilaSaltos.remove(Compilador.pilaSaltos.size()-1);
        completarBI(bi, Compilador.tercetos.size());
    }
break;
case 75:
//#line 248 "grama.y"
{yyerror("no hay sentencias en else");}
break;
case 77:
//#line 253 "grama.y"
{
        yyerror("falta endif");
    }
break;
case 78:
//#line 257 "grama.y"
{
        yyerror("falta endif");yyerror("no hay sentencias en else");
    }
break;
case 79:
//#line 261 "grama.y"
{
        yyerror("falta endif");
    }
break;
case 80:
//#line 266 "grama.y"
{yyerror("no hay sentencias en then");}
break;
case 81:
//#line 267 "grama.y"
{yyerror("no hay sentencias en then");yyerror("no hay sentencias en else");}
break;
case 82:
//#line 268 "grama.y"
{yyerror("no hay sentencias en then");}
break;
case 83:
//#line 271 "grama.y"
{yyerror("no hay sentencias en then");yyerror("falta endif");}
break;
case 84:
//#line 272 "grama.y"
{yyerror("no hay sentencias en then");yyerror("no hay sentencias en else");yyerror("falta endif");}
break;
case 85:
//#line 273 "grama.y"
{yyerror("no hay sentencias en then");yyerror("falta endif");}
break;
case 86:
//#line 277 "grama.y"
{
        int bf = Compilador.pilaSaltos.remove(Compilador.pilaSaltos.size()-1);
        completarBF(bf, Compilador.tercetos.size()+1);
    }
break;
case 87:
//#line 284 "grama.y"
{
        int bf = Compilador.pilaSaltos.remove(Compilador.pilaSaltos.size()-1);
        completarBF(bf, Compilador.tercetos.size());
    }
break;
case 88:
//#line 290 "grama.y"
{
        crearTerceto("BI", "-", "-");
         int bi = Compilador.tercetos.size() - 1;
        Compilador.pilaSaltos.add(bi);}
break;
case 89:
//#line 297 "grama.y"
{
        crearTerceto(val_peek(2).sval, val_peek(3).sval, val_peek(1).sval);
         int t = Compilador.tercetos.size() - 1;
        crearTerceto("BF", "["+t+"]", "-");
         int bf = Compilador.tercetos.size() - 1;
        Compilador.pilaSaltos.add(bf);
        yyval = new ParserVal("[" + t + "]");
    }
break;
case 90:
//#line 305 "grama.y"
{yyerror("falta cerrar parentesis");}
break;
case 91:
//#line 306 "grama.y"
{yyerror("falta abrir parentesis");}
break;
case 92:
//#line 307 "grama.y"
{yyerror("faltan parentesis");}
break;
case 93:
//#line 312 "grama.y"
{yyval=new ParserVal("==");}
break;
case 94:
//#line 313 "grama.y"
{yyval=new ParserVal("=!");}
break;
case 95:
//#line 314 "grama.y"
{yyval=new ParserVal("<=");}
break;
case 96:
//#line 315 "grama.y"
{yyval=new ParserVal(">=");}
break;
case 97:
//#line 316 "grama.y"
{yyval=new ParserVal("<");}
break;
case 98:
//#line 317 "grama.y"
{yyval=new ParserVal(">");}
break;
case 99:
//#line 318 "grama.y"
{yyerror("en condicion");}
break;
case 102:
//#line 332 "grama.y"
{
        String ambito = Compilador.getAmbito();
        String var = val_peek(1).sval;
        if(TablaDeSimbolos.agregarVar(var, ambito, tipo, "nombre de variable")){
           yyval = new ParserVal(var+ambito);
        } else yyerror("La funcion "+var+" ya fue declarada");

        Compilador.entrarAmbito(val_peek(1).sval);
    }
break;
case 103:
//#line 341 "grama.y"
{
        crearTerceto("inicio de funcion", val_peek(4).sval, "-");
    }
break;
case 104:
//#line 344 "grama.y"
{
        crearTerceto("fin de funcion", val_peek(8).sval, "-");
        Compilador.salirAmbito();
    }
break;
case 105:
//#line 348 "grama.y"
{ yyerror("falta declarar nombre de funcion");}
break;
case 106:
//#line 349 "grama.y"
{yyerror("faltan parametros formales");}
break;
case 107:
//#line 350 "grama.y"
{yyerror("falta declarar nombre de funcion"); yyerror("faltan parametros formales");}
break;
case 108:
//#line 355 "grama.y"
{
        String ambito = Compilador.getAmbito();
        String var = val_peek(0).sval;
        if(TablaDeSimbolos.agregarVar(var, ambito, tipo, "nombre de variable")){
           yyval = new ParserVal(var+ambito);
        } else yyerror("La variable "+var+" ya fue declarada");
    }
break;
case 109:
//#line 363 "grama.y"
{
        String ambito = Compilador.getAmbito();
        String var = val_peek(0).sval;
        if(TablaDeSimbolos.agregarVar(var, ambito, tipo, "nombre de variable")){
           yyval = new ParserVal(var+ambito);
        } else yyerror("La variable "+var+" ya fue declarada");
    }
break;
case 110:
//#line 370 "grama.y"
{
        String ambito = Compilador.getAmbito();
        String var = val_peek(0).sval;
        if(TablaDeSimbolos.agregarVar(var, ambito, tipo, "nombre de variable")){
           yyval = new ParserVal(var+ambito);
        } else yyerror("La variable "+var+" ya fue declarada");
    }
break;
case 111:
//#line 377 "grama.y"
{
        String ambito = Compilador.getAmbito();
        String var = val_peek(0).sval;
        if(TablaDeSimbolos.agregarVar(var, ambito, tipo, "nombre de variable")){
           yyval = new ParserVal(var+ambito);
        } else yyerror("La variable "+var+" ya fue declarada");
    }
break;
case 112:
//#line 384 "grama.y"
{yyerror("error en parametro formal");}
break;
case 113:
//#line 388 "grama.y"
{tipo = "uint";}
break;
case 118:
//#line 404 "grama.y"
{
        addEstructura("return");
        crearTerceto("return", val_peek(2).sval, "-");
         int t = Compilador.tercetos.size() - 1;
        yyval=new ParserVal("[" + t + "]");
        }
break;
case 121:
//#line 423 "grama.y"
{yyerror("falta cuerpo sentencias");}
break;
case 122:
//#line 424 "grama.y"
{yyerror("falta de ;");}
break;
case 123:
//#line 426 "grama.y"
{ yyerror("falta while");}
break;
case 124:
//#line 427 "grama.y"
{yyerror("falta cuerpo sentencias"); yyerror("falta while");}
break;
case 125:
//#line 428 "grama.y"
{yyerror("falta de ) en condicion");yyerror("falta de ;");yyerror("falta while");}
break;
case 126:
//#line 429 "grama.y"
{yyerror("falta de ) en condicion");yyerror("falta de ;");yyerror("falta cuerpo sentencias");yyerror("falta while");}
break;
case 127:
//#line 432 "grama.y"
{Compilador.pilaSaltos.add(Compilador.tercetos.size());}
break;
case 128:
//#line 435 "grama.y"
{
        int salto = Compilador.pilaSaltos.remove(0);
        crearTerceto("BI", "[" +salto+"]", "-");
    }
break;
case 129:
//#line 443 "grama.y"
{
        Compilador.entrarAmbito("ua"+cantUnidadesAnonimas);
        cantUnidadesAnonimas+= 1;
    }
break;
case 130:
//#line 449 "grama.y"
{Compilador.salirAmbito();}
break;
case 131:
//#line 450 "grama.y"
{yyerror("no hay sentencias dentro de las llaves");}
break;
case 132:
//#line 451 "grama.y"
{Compilador.salirAmbito();}
break;
case 133:
//#line 452 "grama.y"
{yyerror("Error en sentencia");}
break;
case 134:
//#line 458 "grama.y"
{
        yyval= new ParserVal("lambda");
        Compilador.salirAmbito();
    }
break;
case 135:
//#line 462 "grama.y"
{ yyerror("falta abrir llave en cuerpo de sentencia lambda");}
break;
case 136:
//#line 463 "grama.y"
{yyerror("falta cerra llave en cuerpo de sentencia lambda");}
break;
case 137:
//#line 464 "grama.y"
{ yyerror("faltan llaves en cuerpo de sentencia lambda");}
break;
case 140:
//#line 474 "grama.y"
{
        String ambito = Compilador.getAmbito();
        String var = val_peek(0).sval;
        if(TablaDeSimbolos.agregarVar(var, ambito, tipo, "nombre de variable")){
           yyval = new ParserVal(var+ambito);
        } else yyerror("La variable "+var+" ya fue declarada");
    }
break;
case 141:
//#line 486 "grama.y"
{
        asigMultiple(val_peek(3).sval,val_peek(1).sval);
    }
break;
case 143:
//#line 493 "grama.y"
{yyval=new ParserVal(val_peek(2).sval + "," + val_peek(0).sval);}
break;
case 144:
//#line 494 "grama.y"
{yyerror("falta , en lista de variables");}
break;
case 145:
//#line 498 "grama.y"
{yyval=val_peek(0);}
break;
case 146:
//#line 499 "grama.y"
{yyval=new ParserVal(val_peek(2).sval + "," + val_peek(0).sval);}
break;
case 147:
//#line 500 "grama.y"
{yyerror("falta , en lista de constantes");}
break;
//#line 1565 "Parser.java"
//########## END OF USER-SUPPLIED ACTIONS ##########
    }//switch
    //#### Now let's reduce... ####
    if (yydebug) debug("reduce");
    state_drop(yym);             //we just reduced yylen states
    yystate = state_peek(0);     //get new state
    val_drop(yym);               //corresponding value drop
    yym = yylhs[yyn];            //select next TERMINAL(on lhs)
    if (yystate == 0 && yym == 0)//done? 'rest' state and at first TERMINAL
      {
      if (yydebug) debug("After reduction, shifting from state 0 to state "+YYFINAL+"");
      yystate = YYFINAL;         //explicitly say we're done
      state_push(YYFINAL);       //and save it
      val_push(yyval);           //also save the semantic value of parsing
      if (yychar < 0)            //we want another character?
        {
        yychar = yylex();        //get next character
        if (yychar<0) yychar=0;  //clean, if necessary
        if (yydebug)
          yylexdebug(yystate,yychar);
        }
      if (yychar == 0)          //Good exit (if lex returns 0 ;-)
         break;                 //quit the loop--all DONE
      }//if yystate
    else                        //else not done yet
      {                         //get next state and push, for next yydefred[]
      yyn = yygindex[yym];      //find out where to go
      if ((yyn != 0) && (yyn += yystate) >= 0 &&
            yyn <= YYTABLESIZE && yycheck[yyn] == yystate)
        yystate = yytable[yyn]; //get new state
      else
        yystate = yydgoto[yym]; //else go to new defred
      if (yydebug) debug("after reduction, shifting from state "+state_peek(0)+" to state "+yystate+"");
      state_push(yystate);     //going again, so push state & val...
      val_push(yyval);         //for next action
      }
    }//main loop
  return 0;//yyaccept!!
}
//## end of method parse() ######################################



//## run() --- for Thread #######################################
/**
 * A default run method, used for operating this parser
 * object in the background.  It is intended for extending Thread
 * or implementing Runnable.  Turn off with -Jnorun .
 */
public void run()
{
  yyparse();
}
//## end of method run() ########################################



//## Constructors ###############################################
/**
 * Default constructor.  Turn off with -Jnoconstruct .

 */
public Parser()
{
  //nothing to do
}


/**
 * Create a parser, setting the debug to true or false.
 * @param debugMe true for debugging, false for no debug.
 */
public Parser(boolean debugMe)
{
  yydebug=debugMe;
}
//###############################################################



}
//################### END OF CLASS ##############################
