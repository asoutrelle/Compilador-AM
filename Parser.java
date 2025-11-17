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
    4,    4,    3,    3,    3,    3,    3,    5,    8,   15,
   15,   15,   15,   15,   15,   15,   15,   15,   15,   17,
   17,   17,   17,   17,   17,   17,   18,   18,   18,   18,
   18,   21,   21,   14,   14,   19,   22,   22,   22,   22,
   22,   22,   22,   10,   10,   16,   16,   24,   24,   24,
    9,    9,    9,    9,    9,    9,    9,    9,    9,    9,
    9,    9,   28,   30,   29,   25,   25,   25,   25,   31,
   31,   31,   31,   31,   31,   31,   32,   32,    6,   34,
   34,   34,   36,   37,    7,    7,    7,    7,   35,   35,
   35,   35,   35,   33,   38,   38,   39,   39,   11,   23,
   13,   13,   13,   13,   13,   13,   13,   40,   41,   26,
   27,   27,   27,   27,   20,   20,   20,   20,   42,   42,
   12,   43,   43,   43,   44,   44,   44,
};
final static short yylen[] = {                            2,
    4,    3,    3,    3,    2,    3,    2,    4,    3,    3,
    3,    2,    2,    2,    1,    2,    2,    1,    1,    1,
    1,    1,    1,    1,    1,    1,    1,    1,    4,    3,
    3,    2,    3,    3,    1,    4,    3,    3,    3,    3,
    3,    3,    3,    2,    2,    1,    1,    1,    1,    1,
    1,    1,    2,    3,    1,    4,    3,    2,    1,    5,
    4,    3,    1,    5,    4,    1,    1,    1,    1,    1,
   11,    8,    7,   10,    7,    6,    9,    5,    4,    7,
    4,    3,    0,    0,    0,    5,    4,    4,    3,    1,
    1,    1,    1,    1,    1,    1,    1,    2,    3,    1,
    3,    2,    0,    0,   10,    7,    7,    6,    3,    2,
    5,    4,    1,    1,    1,    2,    1,    1,    5,    1,
    9,    6,    6,    6,    5,    5,    4,    0,    0,    0,
    3,    2,    1,    3,    8,    7,    7,    6,    3,    3,
    4,    1,    3,    2,    1,    3,    2,
};
final static short yydefred[] = {                         0,
    0,    0,    0,    0,    0,    0,    0,    0,  130,    0,
  114,   13,    0,   15,   18,   19,   20,   21,   22,   23,
   24,   25,   26,   27,   28,    0,    0,    0,    0,    0,
    0,    0,    0,   17,    0,    0,    0,  128,    0,   49,
   52,    0,    0,    0,    0,    0,    0,   47,    0,    0,
   46,   48,   50,   51,    0,    6,   16,    0,    0,    0,
    0,    0,    0,  144,   11,    0,    0,    4,    9,    2,
   54,   70,   69,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,   53,    0,    0,   45,   44,   96,   90,
   91,   92,   93,    0,    0,   94,   95,    0,    0,    0,
   67,    0,    0,   66,   82,    0,    0,    0,  113,    0,
    0,    0,    0,  102,    0,   99,  143,  145,    0,    8,
    1,   65,    0,    0,    0,    0,  133,    0,    0,    0,
   63,    0,    0,    0,   38,   39,    0,   37,    0,    0,
   33,    0,   34,    0,    0,   43,   40,   42,   41,    0,
   81,   85,   79,    0,   29,    0,    0,    0,    0,  110,
    0,    0,  101,  147,    0,  141,   64,  119,    0,    0,
  132,   97,    0,  125,    0,    0,   56,    0,    0,   36,
    0,    0,   88,   78,    0,    0,    0,    0,    0,  109,
  118,  117,    0,  115,    0,    0,    0,  146,  122,  134,
  131,   98,    0,  124,    0,   57,   86,    0,    0,    0,
    0,    0,   76,    0,  104,  108,  116,    0,    0,  112,
   84,    0,    0,    0,    0,  138,   80,    0,    0,   75,
   85,   73,  107,    0,  106,  111,    0,   60,    0,  137,
  136,    0,    0,    0,   72,    0,    0,  121,  135,  139,
  140,   77,    0,    0,    0,   74,  105,   71,
};
final static short yydgoto[] = {                          4,
   13,   14,   15,   16,   17,   18,   19,   20,   21,   22,
   23,   24,   25,   26,   49,  105,   50,   51,   52,   53,
   54,  133,  134,   77,   55,   38,  130,  186,  185,  187,
   98,  173,   27,   61,  113,  157,  234,  193,  194,   79,
  221,  226,   28,  119,
};
final static short yysindex[] = {                       -89,
  312,  310,  366,    0,  -32,    5,   25,   44,    0,  418,
    0,    0,  320,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0, -228,  -38,  -29,   -3,  517,
  331,   -2,  368,    0, -168,  -12,  428,    0,   56,    0,
    0,  434,  469, -162,  192,   -4,   -4,    0,  348,   83,
    0,    0,    0,    0,  -51,    0,    0,  428,   65,  -27,
   -6, -146, -131,    0,    0,   14,  474,    0,    0,    0,
    0,    0,    0, -143,  -20,  119,   92,  106,  -36,  396,
  138,  128,   83,    0,  348, -115,    0,    0,    0,    0,
    0,    0,    0,  450,  465,    0,    0,  428,  283,  306,
    0,  155,  -20,    0,    0, -103,   35,  122,    0, -143,
   52,  -99,  147,    0,  -80,    0,    0,    0,  -41,    0,
    0,    0,  -20,  -20,  418,  292,    0, -228,  -20,  412,
    0,  119,  215,  -92,    0,    0,  163,    0,  428,  145,
    0,   83,    0,   83,  421,    0,    0,    0,    0,  -20,
    0,    0,    0,    0,    0,   64, -177,  -64,  532,    0,
   76, -217,    0,    0,  -47,    0,    0,    0,  -20,   78,
    0,    0,  501,    0,  418,  -20,    0,  428,  -45,    0,
  453,  -73,    0,    0, -103,  -40,  222,  532,  271,    0,
    0,    0,  376,    0,  532, -143,    3,    0,    0,    0,
    0,    0,    0,    0,  -49,    0,    0,  -62,  260,  -20,
  252,  -20,    0,  403,    0,    0,    0,  405,   20,    0,
    0,   21,  262,  190, -139,    0,    0,   26,  -20,    0,
    0,    0,    0,  191,    0,    0,  -20,    0,  190,    0,
    0,  247,  286,  -20,    0, -103,  532,    0,    0,    0,
    0,    0,  265,  485,  -20,    0,    0,    0,
};
final static short yyrindex[] = {                         0,
    0,    0,    0,    0,    0,  -44,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,   42,    0,    0,  333,    0,
  336,  345,  352,    0,    0,    0,    0,    0,    1,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,   23,
    0,    0,    0,    0,  275,    0,    0,    0,   91,    0,
    0,    0,    0,    0,    0,  369,  370,    0,    0,    0,
    0,    0,    0,    0,    0,  341,    0,    0,    0,    0,
    0,    0,   47,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,  275,    0,    0,    0,    0,    0, -135,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,  442,  157,    0,
    0,  -31,    0,  293,    0,    0,    0,    0,    0,    0,
    0,   69,    0,   93,  115,    0,    0,    0,    0,    0,
    0,    0,    0,   45,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,  165,    0,    0,  316,    0,
  143,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,  117,    0,  317,    0,    0,    0,    0,  127,
  275,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,  492,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,
};
final static short yygindex[] = {                         0,
   19,   41,  639,  686,    0,    0,    0,    0,    0,    0,
    0,    0,    0,  830,  730,  627,  -18,   98,    0,    0,
    0,    0,  211,    0,  -55,  -50,  -90,    0,  170, -184,
  322, -127,  446,    0,  245,    0,    0, -176,  228,    0,
    0, -123,    0,    0,
};
final static int YYTABLESIZE=1008;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                         55,
   55,   60,  165,   45,  106,   46,   43,  104,   44,  120,
   47,  214,  120,  111,   62,  154,   55,  104,  218,  126,
   31,   33,   35,  129,   83,  228,   34,   74,   75,   46,
   43,   63,   44,    3,   47,   74,  237,  115,  104,   58,
   44,   55,   55,   55,   55,   55,   32,   55,   67,  208,
   35,  152,  104,   57,  209,   34,   34,   11,  196,   55,
   55,   55,   55,   35,   36,   35,   35,   35,   30,  169,
  254,   57,   34,   57,  176,  142,  144,   94,  109,   95,
  223,   35,   35,   37,   35,  142,  126,   32,   71,   32,
   32,   32,   31,  104,  210,   80,   84,   11,  110,  240,
  241,   35,  142,   84,  108,   32,   32,   57,   32,   30,
    6,   30,   30,   30,   89,  249,  123,  242,  243,  203,
  103,   65,   69,   55,   99,   55,  118,   30,   30,  100,
   30,   11,  123,   31,  100,   31,   31,   31,  120,  103,
  103,  140,   87,   87,   88,   35,  124,   35,   94,  100,
   95,   31,   31,    6,   31,  253,  127,  160,    7,    8,
  231,   94,  156,   95,  126,   10,    1,    2,  138,   32,
   94,   32,   95,   89,  159,  129,  163,   74,  136,   46,
   43,  179,   44,    6,   47,  182,  188,  161,    7,    8,
  162,   30,  190,   30,    6,   10,  147,  149,  195,    7,
    8,   87,  200,  180,  101,   94,   10,   95,  102,  103,
  198,  206,   55,  104,  101,   31,  164,   31,   59,  211,
   39,   40,   41,   55,  222,    7,    8,    6,  109,  225,
  125,   74,   10,   46,   43,  101,   44,   89,   47,   89,
   42,  123,  120,   72,   39,   40,   41,   11,  110,  101,
  114,   73,   39,   40,   41,  177,   55,   55,  178,  220,
   55,   55,   55,   55,   42,   87,   55,   87,   55,   55,
   55,   55,   55,   55,   55,   55,  236,  238,   35,   35,
  104,  127,   35,   35,   35,   35,  244,  250,   35,  126,
  101,   35,   35,   35,   35,   35,   35,   35,  142,  225,
   84,  225,   32,   32,   83,   84,   32,   32,   32,   32,
  104,  215,   32,  247,  162,   32,   32,   32,   32,   32,
   32,   32,   74,  104,   30,   30,  251,   44,   30,   30,
   30,   30,   14,   59,   30,    7,   59,   30,   30,   30,
   30,   30,   30,   30,   12,   74,  100,  100,   31,   31,
   44,    5,   31,   31,   31,   31,   58,   62,   31,   58,
   62,   31,   31,   31,   31,   31,   31,   31,   10,    3,
   89,   89,  129,  123,   89,   89,   89,   89,  123,  123,
   89,   68,  123,   89,  224,  123,  239,   84,  205,   89,
   94,  123,   95,  135,   39,   40,   41,  130,   87,   87,
  246,  189,   87,   87,   87,   87,  139,   96,   87,   97,
  101,   87,   11,  127,   42,  150,  171,   87,  127,  127,
  217,  126,  127,    0,    0,  127,  126,  126,    0,    0,
  126,  127,   30,  126,    0,   74,   12,   46,   43,  126,
   44,  217,   47,    0,   56,  217,    0,    0,   39,   40,
   41,   45,    0,   46,   43,   68,   44,   45,   47,   46,
   43,  183,   44,   94,   47,   95,   11,   74,   42,   46,
   43,    0,   44,   81,   47,   46,   43,  101,   44,    0,
   47,  217,  212,   47,   47,  142,   47,    0,   47,   74,
   86,   46,   70,  207,   44,   94,   47,   95,    0,    0,
  216,   47,  142,   47,   74,  112,   46,  101,   74,   44,
   46,   47,  229,   44,    0,   47,    6,    0,    6,   86,
  101,    7,    8,    7,    8,  255,   86,  233,   10,  235,
   10,  130,   61,    0,    0,   61,  130,  130,  146,   39,
   40,   41,    0,  130,    0,    0,    0,  170,    6,    0,
    0,    0,    0,    7,    8,  158,    0,    0,    0,    0,
   10,  148,   39,   40,   41,   29,    6,    5,    6,    0,
    0,    7,    8,    7,    8,    9,    6,    9,   10,    0,
   10,    7,    8,    0,   11,    9,   11,    6,   10,    0,
    0,    0,    7,    8,   11,    0,    9,    0,  121,   10,
    0,    0,  112,   89,    0,   11,    0,  197,    0,  257,
    0,    0,    0,    0,    0,    0,    0,   90,   91,   92,
   93,   32,    6,    0,    6,  201,    0,    7,    8,    7,
    8,    9,    6,    9,   10,    0,   10,    7,    8,    0,
   11,  219,   11,    0,   10,    0,    0,    0,    0,    0,
   11,  131,   39,   40,   41,    0,    0,    0,    0,    6,
    0,    6,    0,    0,    7,    8,    7,    8,   39,   40,
   41,   10,   42,   10,   39,   40,   41,   11,  175,   11,
    0,    0,    0,    0,   39,   40,   41,  116,   42,    0,
   39,   40,   41,    0,   42,    0,    0,   47,  142,    0,
    0,  122,    0,    0,   42,  141,   39,   40,   41,    0,
   42,   47,   47,   47,   47,    0,    0,  127,    0,    0,
  143,   39,   40,   41,    0,   39,   40,   41,  151,  153,
    6,    0,    0,  155,    0,    7,    8,    0,    0,    9,
    0,    6,   10,    0,  127,  166,    7,    8,   11,  167,
  168,    0,    0,   10,    0,  174,    0,    6,    0,   11,
    0,    0,    7,    8,  172,   76,   78,    0,    0,   10,
    0,   82,   66,    6,   85,    0,  184,    0,    7,    8,
    0,    0,    9,    0,    0,   10,    0,  107,    6,    0,
    0,   11,    0,    7,    8,  199,    0,  191,    0,    0,
   10,    0,  204,    0,    0,    0,   11,    0,    0,  132,
  137,  202,    0,  213,    0,    0,    0,    0,    0,    0,
  172,    0,    0,  127,    0,    0,  191,  145,    0,    0,
    0,  191,    0,  191,    0,    0,  227,  230,  232,   48,
    0,    0,    0,    0,  192,    0,  172,  202,    0,    0,
    0,    0,  191,    0,    0,  245,  191,   64,    0,    0,
    0,  202,    0,  248,    0,   48,   48,    0,  181,    0,
  252,   48,   48,  192,   48,   48,   48,    0,  192,  256,
  192,  258,    0,    0,  127,  191,    0,   48,    0,    0,
    0,  117,  191,    0,    0,    0,    0,    0,    0,  192,
    0,    0,    0,  192,    0,    0,    0,  132,  128,   48,
   48,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,   48,   48,    0,    0,   48,   48,   48,
    0,    0,  192,    0,    0,    0,    0,    0,    0,  192,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,   48,    0,    0,    0,    0,   48,
    0,    0,    0,    0,    0,    0,    0,    0,   48,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,   48,    0,    0,   48,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                         44,
    0,   40,   44,   40,   55,   42,   43,   59,   45,   41,
   47,  188,   44,   41,   44,  106,   61,   59,  195,  123,
    2,    3,    0,   79,   43,  210,   59,   40,   41,   42,
   43,   61,   45,  123,   47,   40,  221,   44,   59,  268,
   45,   41,   42,   43,   44,   45,    0,   47,   30,  123,
   46,  102,   59,   13,  182,   59,   59,  275,  276,   59,
   60,   61,   62,   41,   40,   43,   44,   45,    0,  125,
  247,   31,   59,   33,  130,   94,   95,   43,  256,   45,
  208,   59,   60,   40,   62,   44,  123,   41,  257,   43,
   44,   45,    0,   59,  185,   40,  259,  275,  276,  223,
  224,   46,   61,   59,   40,   59,   60,   67,   62,   41,
  257,   43,   44,   45,    0,  239,    0,  257,  258,  175,
  256,  125,  125,  123,   42,  125,  258,   59,   60,   47,
   62,  275,   41,   41,   44,   43,   44,   45,  125,  275,
  276,  257,    0,   46,   47,  123,   41,  125,   43,   59,
   45,   59,   60,  257,   62,  246,    0,  257,  262,  263,
  211,   43,   41,   45,    0,  269,  256,  257,   41,  123,
   43,  125,   45,   59,  123,   59,  257,   40,   41,   42,
   43,  274,   45,  257,   47,   41,  123,   41,  262,  263,
   44,  123,  257,  125,  257,  269,   99,  100,  123,  262,
  263,   59,  125,   41,  256,   43,  269,   45,  260,  261,
  258,  257,  257,   59,  256,  123,  258,  125,  257,  260,
  257,  258,  259,  268,  274,  262,  263,  257,  256,   40,
  267,   40,  269,   42,   43,  256,   45,  123,   47,  125,
  277,  125,  274,  256,  257,  258,  259,  275,  276,  256,
  257,  264,  257,  258,  259,   41,  256,  257,   44,  257,
  260,  261,  262,  263,  277,  123,  266,  125,  268,  269,
  270,  271,  272,  273,  274,  275,  257,  257,  256,  257,
   59,  125,  260,  261,  262,  263,  261,   41,  266,  125,
  256,  269,  270,  271,  272,  273,  274,  275,  257,   40,
  256,   40,  256,  257,  260,  261,  260,  261,  262,  263,
   59,   41,  266,  123,   44,  269,  270,  271,  272,  273,
  274,  275,   40,   59,  256,  257,   41,   45,  260,  261,
  262,  263,    0,   41,  266,    0,   44,  269,  270,  271,
  272,  273,  274,  275,    0,   40,  256,  257,  256,  257,
   45,    0,  260,  261,  262,  263,   41,   41,  266,   44,
   44,  269,  270,  271,  272,  273,  274,  275,    0,    0,
  256,  257,  256,  257,  260,  261,  262,  263,  262,  263,
  266,   41,  266,  269,  125,  269,  125,  261,  178,  275,
   43,  275,   45,  256,  257,  258,  259,  123,  256,  257,
  231,  157,  260,  261,  262,  263,   85,   60,  266,   62,
  256,  269,  275,  257,  277,  261,  125,  275,  262,  263,
  193,  257,  266,   -1,   -1,  269,  262,  263,   -1,   -1,
  266,  275,  123,  269,   -1,   40,  125,   42,   43,  275,
   45,  214,   47,   -1,  125,  218,   -1,   -1,  257,  258,
  259,   40,   -1,   42,   43,  125,   45,   40,   47,   42,
   43,   41,   45,   43,   47,   45,  275,   40,  277,   42,
   43,   -1,   45,   40,   47,   42,   43,  256,   45,   -1,
   47,  254,  261,   42,   43,   44,   45,   -1,   47,   40,
   45,   42,  125,   41,   45,   43,   47,   45,   -1,   -1,
  125,   60,   61,   62,   40,   60,   42,  256,   40,   45,
   42,   47,  261,   45,   -1,   47,  257,   -1,  257,   74,
  256,  262,  263,  262,  263,  261,   81,  125,  269,  125,
  269,  257,   41,   -1,   -1,   44,  262,  263,  256,  257,
  258,  259,   -1,  269,   -1,   -1,   -1,  256,  257,   -1,
   -1,   -1,   -1,  262,  263,  110,   -1,   -1,   -1,   -1,
  269,  256,  257,  258,  259,  256,  257,  256,  257,   -1,
   -1,  262,  263,  262,  263,  266,  257,  266,  269,   -1,
  269,  262,  263,   -1,  275,  266,  275,  257,  269,   -1,
   -1,   -1,  262,  263,  275,   -1,  266,   -1,  125,  269,
   -1,   -1,  157,  256,   -1,  275,   -1,  162,   -1,  125,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,  270,  271,  272,
  273,  256,  257,   -1,  257,  125,   -1,  262,  263,  262,
  263,  266,  257,  266,  269,   -1,  269,  262,  263,   -1,
  275,  196,  275,   -1,  269,   -1,   -1,   -1,   -1,   -1,
  275,  256,  257,  258,  259,   -1,   -1,   -1,   -1,  257,
   -1,  257,   -1,   -1,  262,  263,  262,  263,  257,  258,
  259,  269,  277,  269,  257,  258,  259,  275,  267,  275,
   -1,   -1,   -1,   -1,  257,  258,  259,   61,  277,   -1,
  257,  258,  259,   -1,  277,   -1,   -1,  256,  257,   -1,
   -1,   75,   -1,   -1,  277,  256,  257,  258,  259,   -1,
  277,  270,  271,  272,  273,   -1,   -1,   79,   -1,   -1,
  256,  257,  258,  259,   -1,  257,  258,  259,  102,  103,
  257,   -1,   -1,  107,   -1,  262,  263,   -1,   -1,  266,
   -1,  257,  269,   -1,  106,  119,  262,  263,  275,  123,
  124,   -1,   -1,  269,   -1,  129,   -1,  257,   -1,  275,
   -1,   -1,  262,  263,  126,   36,   37,   -1,   -1,  269,
   -1,   42,  256,  257,   45,   -1,  150,   -1,  262,  263,
   -1,   -1,  266,   -1,   -1,  269,   -1,   58,  257,   -1,
   -1,  275,   -1,  262,  263,  169,   -1,  159,   -1,   -1,
  269,   -1,  176,   -1,   -1,   -1,  275,   -1,   -1,   80,
   81,  173,   -1,  187,   -1,   -1,   -1,   -1,   -1,   -1,
  182,   -1,   -1,  185,   -1,   -1,  188,   98,   -1,   -1,
   -1,  193,   -1,  195,   -1,   -1,  210,  211,  212,   10,
   -1,   -1,   -1,   -1,  159,   -1,  208,  209,   -1,   -1,
   -1,   -1,  214,   -1,   -1,  229,  218,   28,   -1,   -1,
   -1,  223,   -1,  237,   -1,   36,   37,   -1,  139,   -1,
  244,   42,   43,  188,   45,   46,   47,   -1,  193,  253,
  195,  255,   -1,   -1,  246,  247,   -1,   58,   -1,   -1,
   -1,   62,  254,   -1,   -1,   -1,   -1,   -1,   -1,  214,
   -1,   -1,   -1,  218,   -1,   -1,   -1,  178,   79,   80,
   81,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   94,   95,   -1,   -1,   98,   99,  100,
   -1,   -1,  247,   -1,   -1,   -1,   -1,   -1,   -1,  254,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,  125,   -1,   -1,   -1,   -1,  130,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,  139,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,  175,   -1,   -1,  178,
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
"asig : variable ASIG exp punto_coma",
"exp : exp '+' termino",
"exp : exp '-' termino",
"exp : '+' termino",
"exp : exp '+' error",
"exp : exp '-' error",
"exp : termino",
"exp : TRUNC '(' exp ')'",
"exp : TRUNC exp ')'",
"exp : TRUNC '(' error",
"exp : TRUNC '(' ')'",
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
"factor : punto_flotante",
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
"if : IF cuerpo_condicion nuevo_ambito cuerpo_sentencia_ejecutable completar_bf_else ELSE nuevo_ambito crear_bi cuerpo_sentencia_ejecutable ENDIF punto_coma",
"if : IF cuerpo_condicion nuevo_ambito cuerpo_sentencia_ejecutable completar_bf_else ELSE ENDIF punto_coma",
"if : IF cuerpo_condicion nuevo_ambito cuerpo_sentencia_ejecutable completar_bf ENDIF punto_coma",
"if : IF cuerpo_condicion nuevo_ambito cuerpo_sentencia_ejecutable completar_bf_else ELSE nuevo_ambito crear_bi cuerpo_sentencia_ejecutable punto_coma",
"if : IF cuerpo_condicion nuevo_ambito cuerpo_sentencia_ejecutable completar_bf_else ELSE punto_coma",
"if : IF cuerpo_condicion nuevo_ambito cuerpo_sentencia_ejecutable completar_bf punto_coma",
"if : IF cuerpo_condicion ELSE nuevo_ambito crear_bi cuerpo_sentencia_ejecutable completar_bf ENDIF punto_coma",
"if : IF cuerpo_condicion ELSE ENDIF punto_coma",
"if : IF cuerpo_condicion ENDIF punto_coma",
"if : IF cuerpo_condicion ELSE nuevo_ambito crear_bi cuerpo_sentencia_ejecutable punto_coma",
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
"declaracion : tipo lista_variables_declaracion punto_coma",
"lista_variables_declaracion : ID",
"lista_variables_declaracion : lista_variables_declaracion ',' ID",
"lista_variables_declaracion : lista_variables_declaracion ID",
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
"do : DO nuevo_ambito inicio_while cuerpo_sentencia_ejecutable WHILE cuerpo_condicion crear_bi_while completar_bf punto_coma",
"do : DO nuevo_ambito inicio_while WHILE cuerpo_condicion punto_coma",
"do : DO nuevo_ambito inicio_while cuerpo_sentencia_ejecutable WHILE cuerpo_condicion",
"do : DO nuevo_ambito inicio_while cuerpo_sentencia_ejecutable cuerpo_condicion punto_coma",
"do : DO nuevo_ambito inicio_while cuerpo_condicion punto_coma",
"do : DO nuevo_ambito inicio_while cuerpo_sentencia_ejecutable cuerpo_condicion",
"do : DO nuevo_ambito inicio_while cuerpo_condicion",
"inicio_while :",
"crear_bi_while :",
"nuevo_ambito :",
"cuerpo_sentencia_ejecutable : '{' lista_sentencia_ejecutable '}'",
"cuerpo_sentencia_ejecutable : '{' '}'",
"cuerpo_sentencia_ejecutable : sentencia_ejecutable",
"cuerpo_sentencia_ejecutable : '{' error '}'",
"exp_lambda : '(' tipo ID ')' '{' lista_sentencia_ejecutable '}' argumento_lambda",
"exp_lambda : '(' tipo ID ')' lista_sentencia_ejecutable '}' argumento_lambda",
"exp_lambda : '(' tipo ID ')' '{' lista_sentencia_ejecutable argumento_lambda",
"exp_lambda : '(' tipo ID ')' lista_sentencia_ejecutable argumento_lambda",
"argumento_lambda : '(' ID ')'",
"argumento_lambda : '(' CTE ')'",
"asig_multiple : lista_variables '=' lista_constantes punto_coma",
"lista_variables : variable",
"lista_variables : lista_variables ',' variable",
"lista_variables : lista_variables variable",
"lista_constantes : CTE",
"lista_constantes : lista_constantes ',' CTE",
"lista_constantes : lista_constantes CTE",
};

//#line 433 "grama.y"
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
//#line 755 "Parser.java"
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
        if(!TablaDeSimbolos.checkVar(val_peek(3).sval, Compilador.getAmbito(), "", "nombre de programa")){
           yyerror("La variable "+val_peek(3).sval+" ya fue declarada");
        }
    }
break;
case 2:
//#line 23 "grama.y"
{yyerror("falta nombre de programa");}
break;
case 3:
//#line 24 "grama.y"
{yyerror("falta llave de cierre de programa");}
break;
case 4:
//#line 25 "grama.y"
{yyerror("falta llave de inicio de programa");}
break;
case 5:
//#line 26 "grama.y"
{yyerror("falta nombre del programa");yyerror("falta llave de cierre de programa");}
break;
case 6:
//#line 27 "grama.y"
{yyerror("falta nombre del programa");yyerror("falta llave de inicio de programa");}
break;
case 7:
//#line 28 "grama.y"
{yyerror("no hay llaves del programa");}
break;
case 8:
//#line 30 "grama.y"
{yyerror("error en programa");}
break;
case 9:
//#line 31 "grama.y"
{yyerror("falta nombre de programa");yyerror("error en programa");}
break;
case 10:
//#line 32 "grama.y"
{yyerror("falta llave de cierre de programa");yyerror("error en programa");}
break;
case 11:
//#line 33 "grama.y"
{yyerror("falta llave de inicio de programa");yyerror("error en programa");}
break;
case 12:
//#line 34 "grama.y"
{yyerror("falta nombre del programa");yyerror("falta llave de cierre de programa");yyerror("error en programa");}
break;
case 13:
//#line 35 "grama.y"
{yyerror("falta nombre del programa");yyerror("error en programa");}
break;
case 14:
//#line 36 "grama.y"
{yyerror("no hay llaves del programa");yyerror("error en programa");}
break;
case 17:
//#line 41 "grama.y"
{yyerror("error en sentencia");}
break;
case 21:
//#line 51 "grama.y"
{addEstructura("declaracion de variable");}
break;
case 22:
//#line 52 "grama.y"
{addEstructura("declaracion de funcion");}
break;
case 23:
//#line 56 "grama.y"
{addEstructura("asignacion");}
break;
case 24:
//#line 57 "grama.y"
{addEstructura("sentencia if");}
break;
case 25:
//#line 58 "grama.y"
{addEstructura("print");}
break;
case 26:
//#line 59 "grama.y"
{addEstructura("return");}
break;
case 27:
//#line 60 "grama.y"
{addEstructura("asignacion multiple");}
break;
case 28:
//#line 64 "grama.y"
{addEstructura("sentencia do while");}
break;
case 29:
//#line 69 "grama.y"
{
        crearTerceto(":=", val_peek(3).sval, val_peek(1).sval);
    }
break;
case 30:
//#line 77 "grama.y"
{
        crearTerceto("+", val_peek(2).sval, val_peek(0).sval);
        int t = Compilador.tercetos.size() - 1;
        yyval=new ParserVal("[" + t + "]");
    }
break;
case 31:
//#line 83 "grama.y"
{
        crearTerceto("-", val_peek(2).sval, val_peek(0).sval);
        int t = Compilador.tercetos.size() - 1;
        yyval=new ParserVal("[" + t + "]");
    }
break;
case 32:
//#line 88 "grama.y"
{yyerror("falta operando a izquierda de +");}
break;
case 33:
//#line 89 "grama.y"
{yyerror("falta operando a derecha de +");}
break;
case 34:
//#line 90 "grama.y"
{yyerror("falta operando a derecha de -");}
break;
case 35:
//#line 91 "grama.y"
{yyval=val_peek(0);}
break;
case 36:
//#line 92 "grama.y"
{addEstructura("trunc");}
break;
case 37:
//#line 93 "grama.y"
{addEstructura("trunc");yyerror("falta abrir parentesis en trunc");}
break;
case 38:
//#line 94 "grama.y"
{addEstructura("trunc");yyerror("falta cerrar parentesis en trunc");yyerrflag=0;}
break;
case 39:
//#line 95 "grama.y"
{addEstructura("trunc");yyerror("falta argumento en trunc");}
break;
case 40:
//#line 100 "grama.y"
{
        crearTerceto("*", val_peek(2).sval, val_peek(0).sval);
         int t = Compilador.tercetos.size() - 1;
        yyval=new ParserVal("[" + t + "]");
    }
break;
case 41:
//#line 106 "grama.y"
{
        crearTerceto("/", val_peek(2).sval, val_peek(0).sval);
         int t = Compilador.tercetos.size() - 1;
        yyval=new ParserVal("[" + t + "]");
    }
break;
case 42:
//#line 111 "grama.y"
{yyerror("falta operando a derecha de /");}
break;
case 43:
//#line 112 "grama.y"
{yyerror("falta operando a derecha de *");}
break;
case 44:
//#line 113 "grama.y"
{yyerror("falta operando a izquierda de /");}
break;
case 45:
//#line 114 "grama.y"
{yyerror("falta operando a izquierda de *");}
break;
case 46:
//#line 115 "grama.y"
{yyval=val_peek(0);}
break;
case 47:
//#line 119 "grama.y"
{yyval=val_peek(0);}
break;
case 48:
//#line 120 "grama.y"
{yyval=val_peek(0);}
break;
case 49:
//#line 121 "grama.y"
{yyval=new ParserVal(val_peek(0).sval);}
break;
case 50:
//#line 122 "grama.y"
{addEstructura("lambda"); yyval=val_peek(0);}
break;
case 51:
//#line 123 "grama.y"
{yyval=val_peek(0);}
break;
case 52:
//#line 127 "grama.y"
{check_rango(val_peek(0).sval); yyval=new ParserVal(val_peek(0).sval);}
break;
case 53:
//#line 128 "grama.y"
{check_rango("-"+val_peek(0).sval); yyval=new ParserVal("-"+val_peek(1).sval);}
break;
case 54:
//#line 133 "grama.y"
{
        yyval = new ParserVal(val_peek(2).sval + '.' + val_peek(0).sval);

    }
break;
case 55:
//#line 138 "grama.y"
{
        if(!TablaDeSimbolos.estaDeclarado(val_peek(0).sval, Compilador.getAmbito())){
                    yyerror("La variable "+val_peek(0).sval+" no fue declarada");
                    TablaDeSimbolos.eliminar(val_peek(0).sval);
                } else TablaDeSimbolos.eliminar(val_peek(0).sval);
        yyval=val_peek(0);

    }
break;
case 56:
//#line 150 "grama.y"
{
        addEstructura("invocacion a funcion");
        yyval=new ParserVal(val_peek(3).sval+"("+val_peek(1).sval+")");
    }
break;
case 57:
//#line 158 "grama.y"
{
        yyval=new ParserVal( val_peek(2).sval );
    }
break;
case 58:
//#line 161 "grama.y"
{yyerror("Falta parametro formal");}
break;
case 59:
//#line 162 "grama.y"
{yyerror("Falta flecha y parametro formal");}
break;
case 60:
//#line 164 "grama.y"
{
        yyval = new ParserVal(val_peek(4).sval+","+val_peek(2).sval);
    }
break;
case 61:
//#line 167 "grama.y"
{yyerror("Falta parametro formal");}
break;
case 62:
//#line 168 "grama.y"
{yyerror("Falta flecha y parametro formal");}
break;
case 63:
//#line 169 "grama.y"
{yyerror("error en parametros de invocacion");}
break;
case 64:
//#line 174 "grama.y"
{
        crearTerceto("print", val_peek(2).sval, "-");
         int t = Compilador.tercetos.size() - 1;
        yyval=new ParserVal("[" + t + "]");
    }
break;
case 65:
//#line 179 "grama.y"
{yyerror("falta argumento en print");}
break;
case 67:
//#line 184 "grama.y"
{yyerror("falta ;");}
break;
case 70:
//#line 190 "grama.y"
{yyerror("argumento invalido en print");}
break;
case 71:
//#line 195 "grama.y"
{
        int bi = Compilador.pilaSaltos.remove(Compilador.pilaSaltos.size()-1);
        completarBI(bi, Compilador.tercetos.size());
    }
break;
case 72:
//#line 199 "grama.y"
{yyerror("no hay sentencias en else");}
break;
case 74:
//#line 203 "grama.y"
{ yyerror("falta endif");}
break;
case 75:
//#line 204 "grama.y"
{ yyerror("falta endif");yyerror("no hay sentencias en else");}
break;
case 76:
//#line 205 "grama.y"
{ yyerror("falta endif");}
break;
case 77:
//#line 208 "grama.y"
{yyerror("no hay sentencias en then");}
break;
case 78:
//#line 209 "grama.y"
{yyerror("no hay sentencias en then");yyerror("no hay sentencias en else");}
break;
case 79:
//#line 210 "grama.y"
{yyerror("no hay sentencias en then");}
break;
case 80:
//#line 213 "grama.y"
{yyerror("no hay sentencias en then");yyerror("falta endif");}
break;
case 81:
//#line 214 "grama.y"
{yyerror("no hay sentencias en then");yyerror("no hay sentencias en else");yyerror("falta endif");}
break;
case 82:
//#line 215 "grama.y"
{yyerror("no hay sentencias en then");yyerror("falta endif");}
break;
case 83:
//#line 219 "grama.y"
{
        int bf = Compilador.pilaSaltos.remove(Compilador.pilaSaltos.size()-1);
        completarBF(bf, Compilador.tercetos.size()+1);
    }
break;
case 84:
//#line 226 "grama.y"
{
        int bf = Compilador.pilaSaltos.remove(Compilador.pilaSaltos.size()-1);
        completarBF(bf, Compilador.tercetos.size());
    }
break;
case 85:
//#line 232 "grama.y"
{
        crearTerceto("BI", "-", "-");
         int bi = Compilador.tercetos.size() - 1;
        Compilador.pilaSaltos.add(bi);}
break;
case 86:
//#line 239 "grama.y"
{
        crearTerceto(val_peek(2).sval, val_peek(3).sval, val_peek(1).sval);
         int t = Compilador.tercetos.size() - 1;
        crearTerceto("BF", "["+t+"]", "-");
         int bf = Compilador.tercetos.size() - 1;
        Compilador.pilaSaltos.add(bf);
        yyval = new ParserVal("[" + t + "]");
    }
break;
case 87:
//#line 247 "grama.y"
{yyerror("falta cerrar parentesis");}
break;
case 88:
//#line 248 "grama.y"
{yyerror("falta abrir parentesis");}
break;
case 89:
//#line 249 "grama.y"
{yyerror("faltan parentesis");}
break;
case 90:
//#line 254 "grama.y"
{yyval=new ParserVal("==");}
break;
case 91:
//#line 255 "grama.y"
{yyval=new ParserVal("=!");}
break;
case 92:
//#line 256 "grama.y"
{yyval=new ParserVal("<=");}
break;
case 93:
//#line 257 "grama.y"
{yyval=new ParserVal(">=");}
break;
case 94:
//#line 258 "grama.y"
{yyval=new ParserVal("<");}
break;
case 95:
//#line 259 "grama.y"
{yyval=new ParserVal(">");}
break;
case 96:
//#line 260 "grama.y"
{yyerror("en condicion");}
break;
case 100:
//#line 277 "grama.y"
{
        String ambito = Compilador.getAmbito();
        if(!TablaDeSimbolos.checkVar(val_peek(0).sval, ambito, tipo, "nombre de variable")){
           yyerror("La variable "+val_peek(0).sval+" ya fue declarada");
        }

    }
break;
case 101:
//#line 285 "grama.y"
{
        yyval = new ParserVal(val_peek(2).sval+","+val_peek(0).sval);
        String ambito = Compilador.getAmbito();
        if(!TablaDeSimbolos.checkVar(val_peek(0).sval, ambito, tipo, "nombre de variable")){
           yyerror("La variable "+val_peek(0).sval+" ya fue declarada");
        }
    }
break;
case 102:
//#line 292 "grama.y"
{yyerror("falta , en lista de variables");}
break;
case 103:
//#line 297 "grama.y"
{Compilador.entrarAmbito(val_peek(1).sval);}
break;
case 104:
//#line 297 "grama.y"
{
        String ambito = Compilador.getAmbito();
        if(!TablaDeSimbolos.checkVar(val_peek(4).sval, ambito, tipo, "nombre de funcion")){
            yyerror("La funcion "+val_peek(4).sval+" ya fue declarada");
        }
        crearTerceto("inicio de funcion", val_peek(4).sval, "-");
    }
break;
case 105:
//#line 304 "grama.y"
{
        crearTerceto("fin de funcion", val_peek(8).sval, "-");
        Compilador.salirAmbito();
    }
break;
case 106:
//#line 308 "grama.y"
{ yyerror("falta declarar nombre de funcion");}
break;
case 107:
//#line 309 "grama.y"
{yyerror("faltan parametros formales");}
break;
case 108:
//#line 310 "grama.y"
{yyerror("falta declarar nombre de funcion"); yyerror("faltan parametros formales");}
break;
case 109:
//#line 315 "grama.y"
{
        String ambito = Compilador.getAmbito();
                if(!TablaDeSimbolos.checkVar(val_peek(0).sval, ambito, tipo, "nombre de parametro")){
                   yyerror("La variable "+val_peek(0).sval+" ya fue declarada");
                }
    }
break;
case 110:
//#line 322 "grama.y"
{
        String ambito = Compilador.getAmbito();
                if(!TablaDeSimbolos.checkVar(val_peek(0).sval, ambito, tipo, "nombre de parametro")){
                   yyerror("La variable "+val_peek(0).sval+" ya fue declarada");
                }
    }
break;
case 113:
//#line 330 "grama.y"
{yyerror("error en parametro formal");}
break;
case 114:
//#line 334 "grama.y"
{tipo = "uint";}
break;
case 119:
//#line 350 "grama.y"
{
        addEstructura("return");
        crearTerceto("return", val_peek(2).sval, "-");
         int t = Compilador.tercetos.size() - 1;
        yyval=new ParserVal("[" + t + "]");
        }
break;
case 122:
//#line 369 "grama.y"
{yyerror("falta cuerpo sentencias");}
break;
case 123:
//#line 370 "grama.y"
{yyerror("falta de ;");}
break;
case 124:
//#line 372 "grama.y"
{ yyerror("falta while");}
break;
case 125:
//#line 373 "grama.y"
{yyerror("falta cuerpo sentencias"); yyerror("falta while");}
break;
case 126:
//#line 374 "grama.y"
{yyerror("falta de ) en condicion");yyerror("falta de ;");yyerror("falta while");}
break;
case 127:
//#line 375 "grama.y"
{yyerror("falta de ) en condicion");yyerror("falta de ;");yyerror("falta cuerpo sentencias");yyerror("falta while");}
break;
case 128:
//#line 378 "grama.y"
{Compilador.pilaSaltos.add(Compilador.tercetos.size());}
break;
case 129:
//#line 381 "grama.y"
{
        int salto = Compilador.pilaSaltos.remove(0);
        crearTerceto("BI", "[" +salto+"]", "-");
    }
break;
case 130:
//#line 387 "grama.y"
{Compilador.entrarAmbito("ua"+cantUnidadesAnonimas); cantUnidadesAnonimas+= 1;}
break;
case 131:
//#line 390 "grama.y"
{Compilador.salirAmbito();}
break;
case 132:
//#line 391 "grama.y"
{yyerror("no hay sentencias dentro de las llaves");}
break;
case 133:
//#line 392 "grama.y"
{Compilador.salirAmbito();}
break;
case 134:
//#line 393 "grama.y"
{yyerror("Error en sentencia");}
break;
case 135:
//#line 398 "grama.y"
{yyval= new ParserVal("soy un lamnda");}
break;
case 136:
//#line 399 "grama.y"
{ yyerror("falta abrir llave en cuerpo de sentencia lambda");}
break;
case 137:
//#line 400 "grama.y"
{yyerror("falta cerra llave en cuerpo de sentencia lambda");}
break;
case 138:
//#line 401 "grama.y"
{ yyerror("faltan llaves en cuerpo de sentencia lambda");}
break;
case 141:
//#line 412 "grama.y"
{
        asigMultiple(val_peek(3).sval,val_peek(1).sval);
    }
break;
case 143:
//#line 419 "grama.y"
{yyval=new ParserVal(val_peek(2).sval + "," + val_peek(0).sval);}
break;
case 144:
//#line 420 "grama.y"
{yyerror("falta , en lista de variables");}
break;
case 145:
//#line 424 "grama.y"
{yyval=val_peek(0);}
break;
case 146:
//#line 425 "grama.y"
{yyval=new ParserVal(val_peek(2).sval + "," + val_peek(0).sval);}
break;
case 147:
//#line 426 "grama.y"
{yyerror("falta , en lista de constantes");}
break;
//#line 1504 "Parser.java"
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
