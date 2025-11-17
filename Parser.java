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
   34,   34,   36,    7,    7,    7,    7,   35,   35,   35,
   35,   35,   33,   37,   37,   38,   38,   11,   23,   13,
   13,   13,   13,   13,   13,   13,   39,   40,   26,   27,
   27,   27,   27,   20,   20,   20,   20,   41,   41,   12,
   42,   42,   42,   43,   43,   43,
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
    3,    2,    0,    9,    7,    7,    6,    3,    2,    5,
    4,    1,    1,    1,    2,    1,    1,    5,    1,    9,
    6,    6,    6,    5,    5,    4,    0,    0,    0,    3,
    2,    1,    3,    8,    7,    7,    6,    3,    3,    4,
    1,    3,    2,    1,    3,    2,
};
final static short yydefred[] = {                         0,
    0,    0,    0,    0,    0,    0,    0,    0,  129,    0,
  113,   13,    0,   15,   18,   19,   20,   21,   22,   23,
   24,   25,   26,   27,   28,    0,    0,    0,    0,    0,
    0,    0,    0,   17,    0,    0,    0,  127,    0,   49,
   52,    0,    0,    0,    0,    0,    0,   47,    0,    0,
   46,   48,   50,   51,    0,    6,   16,    0,    0,    0,
    0,    0,    0,  143,   11,    0,    0,    4,    9,    2,
   54,   70,   69,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,   53,    0,    0,   45,   44,   96,   90,
   91,   92,   93,    0,    0,   94,   95,    0,    0,    0,
   67,    0,    0,   66,   82,    0,    0,    0,  112,    0,
    0,    0,    0,  102,    0,   99,  142,  144,    0,    8,
    1,   65,    0,    0,    0,    0,  132,    0,    0,    0,
   63,    0,    0,    0,   38,   39,    0,   37,    0,    0,
   33,    0,   34,    0,    0,   43,   40,   42,   41,    0,
   81,   85,   79,    0,   29,    0,    0,    0,    0,  109,
    0,    0,  101,  146,    0,  140,   64,  118,    0,    0,
  131,   97,    0,  124,    0,    0,   56,    0,    0,   36,
    0,    0,   88,   78,    0,    0,    0,    0,  103,  108,
  117,  116,    0,  114,    0,    0,    0,  145,  121,  133,
  130,   98,    0,  123,    0,   57,   86,    0,    0,    0,
    0,    0,   76,    0,    0,  107,  115,    0,    0,  111,
   84,    0,    0,    0,    0,  137,   80,    0,    0,   75,
   85,   73,  106,    0,  105,  110,    0,   60,    0,  136,
  135,    0,    0,    0,   72,    0,    0,  120,  134,  138,
  139,   77,    0,  104,    0,   74,   71,
};
final static short yydgoto[] = {                          4,
   13,   14,   15,   16,   17,   18,   19,   20,   21,   22,
   23,   24,   25,   26,   49,  105,   50,   51,   52,   53,
   54,  133,  134,   77,   55,   38,  130,  186,  185,  187,
   98,  173,   27,   61,  113,  215,  193,  194,   79,  221,
  226,   28,  119,
};
final static short yysindex[] = {                       -89,
  321,  310,  360,    0,  -37,    3,   33,   58,    0,  418,
    0,    0,  331,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0, -163,  -38,  -29,  -23,  663,
  368,   -4,  478,    0, -139,  -12,  428,    0,   31,    0,
    0,  434,  469, -134,  192,  312,  312,    0,  348,   37,
    0,    0,    0,    0,  -51,    0,    0,  428,  111,  -27,
   -6, -124, -100,    0,    0,   -2,  519,    0,    0,    0,
    0,    0,    0, -112,   22,  102,  123,  128,  -36,  396,
  138,  150,   37,    0,  348,  -82,    0,    0,    0,    0,
    0,    0,    0,  450,  465,    0,    0,  428,  283,  306,
    0,  155,   22,    0,    0, -103,   35,  -20,    0, -112,
   59,  -71,   78,    0,  -70,    0,    0,    0,  -41,    0,
    0,    0,   22,   22,  418,  476,    0, -163,   22,  412,
    0,  102,   91,  -86,    0,    0,  156,    0,  428,  163,
    0,   37,    0,   37,  421,    0,    0,    0,    0,   22,
    0,    0,    0,    0,    0,   75,   98,  -45,  286,    0,
  107, -236,    0,    0,  -33,    0,    0,    0,   22,   95,
    0,    0,  565,    0,  418,   22,    0,  428,   20,    0,
  453,  -73,    0,    0, -103,   27,  222,  286,    0,    0,
    0,    0, -113,    0,  286, -112,   57,    0,    0,    0,
    0,    0,    0,    0,   14,    0,    0,  288,  260,   22,
  252,   22,    0,  376,  204,    0,    0,  403,   77,    0,
    0,   80,  262,  305, -199,    0,    0,   86,   22,    0,
    0,    0,    0,  286,    0,    0,   22,    0,  305,    0,
    0,  307,  317,   22,    0, -103,  405,    0,    0,    0,
    0,    0,  265,    0,   22,    0,    0,
};
final static short yyrindex[] = {                         0,
    0,    0,    0,    0,    0,  -44,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,   42,    0,    0,  361,    0,
  369,  370,  382,    0,    0,    0,    0,    0,    1,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,   23,
    0,    0,    0,    0,  275,    0,    0,    0,   -3,    0,
    0,    0,    0,    0,    0,  388,  389,    0,    0,    0,
    0,    0,    0,    0,    0,  319,    0,    0,    0,    0,
    0,    0,   47,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,  275,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,  442,  157,    0,
    0,  -31,    0,  159,    0,    0,    0,    0,    0,    0,
    0,   69,    0,   93,  115,    0,    0,    0,    0,    0,
    0,    0,    0,   45,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,  165,    0,    0,  167,    0,
  143,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,  117,    0,  271,    0,    0,    0,    0,  140,
  275,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,  292,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,
};
final static short yygindex[] = {                         0,
   97,   41,  699,  659,    0,    0,    0,    0,    0,    0,
    0,    0,    0,  721,  750,  627,   46,  160,    0,    0,
    0,    0,  224,    0,  -55,  -50,  -69,    0,  176, -194,
  332, -157,  446,    0,  313,    0, -169, -117,    0,    0,
 -128,    0,    0,
};
final static int YYTABLESIZE=946;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                         55,
   55,   60,  165,   45,  106,   46,   43,  104,   44,  119,
   47,  216,  119,  111,   62,  228,   55,  104,  214,  126,
  156,   34,   35,  129,  209,  218,  237,   74,   75,   46,
   43,   63,   44,    3,   47,   34,  154,  115,   11,  196,
  100,   55,   55,   55,   55,   55,   32,   55,   35,  208,
  223,  152,  104,   57,   34,  100,   34,  242,  243,   55,
   55,   55,   55,   35,  247,   35,   35,   35,   30,  169,
   80,   57,   36,   57,  176,  217,   35,   94,   99,   95,
  104,   35,   35,  100,   35,  141,  126,   32,   83,   32,
   32,   32,   31,  104,  240,  241,  217,   37,   31,   33,
  217,   65,  141,   84,   58,   32,   32,   57,   32,   30,
  249,   30,   30,   30,   89,  210,  122,   71,  161,  203,
   69,  162,  120,   55,   84,   55,   67,   30,   30,  217,
   30,  177,    6,   31,  178,   31,   31,   31,  189,  142,
  144,  162,   87,    6,   94,   35,   95,   35,    7,    8,
  108,   31,   31,    6,   31,   10,  126,  118,    7,    8,
  231,   11,   11,  123,  125,   10,    1,    2,  124,   32,
   94,   32,   95,   89,  140,  128,  253,   74,  136,   46,
   43,  159,   44,    6,   47,  160,  163,  179,    7,    8,
  138,   30,   94,   30,   95,   10,  180,  188,   94,   59,
   95,   87,   59,  182,  101,   87,   88,   58,  102,  103,
   58,  190,   55,  104,  101,   31,  164,   31,   59,  200,
   39,   40,   41,   55,  198,    7,    8,    6,  109,  195,
  125,   74,   10,   46,   43,  109,   44,   89,   47,   89,
   42,  122,  119,   72,   39,   40,   41,   11,  110,  101,
  114,   73,  100,  100,   11,  110,   55,   55,  147,  149,
   55,   55,   55,   55,   42,   87,   55,   87,   55,   55,
   55,   55,   55,   55,   55,   55,  206,  101,   35,   35,
  104,  126,   35,   35,   35,   35,  211,  222,   35,  125,
  101,   35,   35,   35,   35,   35,   35,   35,  141,  225,
   84,  225,   32,   32,   83,   84,   32,   32,   32,   32,
  104,   62,   32,  220,   62,   32,   32,   32,   32,   32,
   32,   32,   74,  104,   30,   30,  234,   44,   30,   30,
   30,   30,   61,  236,   30,   61,  238,   30,   30,   30,
   30,   30,   30,   30,  225,   74,  244,  250,   31,   31,
   44,   74,   31,   31,   31,   31,   44,  251,   31,   68,
   14,   31,   31,   31,   31,   31,   31,   31,    7,   12,
   89,   89,  128,  122,   89,   89,   89,   89,  122,  122,
   89,    5,  122,   89,  224,  122,  239,   10,    3,   89,
   94,  122,   95,  135,   39,   40,   41,  129,   87,   87,
   84,  205,   87,   87,   87,   87,  246,   96,   87,   97,
  101,   87,   11,  126,   42,  150,  139,   87,  126,  126,
  157,  125,  126,    0,    0,  126,  125,  125,    0,    0,
  125,  126,   30,  125,    0,   74,    0,   46,   43,  125,
   44,    0,   47,    0,    0,   12,    0,    0,   39,   40,
   41,   45,    0,   46,   43,   56,   44,   45,   47,   46,
   43,  183,   44,   94,   47,   95,   11,   74,   42,   46,
   43,    0,   44,   81,   47,   46,   43,  101,   44,    0,
   47,    0,  212,   47,   47,  141,   47,    0,   47,   74,
   86,   46,   68,  207,   44,   94,   47,   95,    0,    0,
  233,   47,  141,   47,   74,  112,   46,  101,   74,   44,
   46,   47,  229,   44,    0,   47,    6,    0,    6,   86,
  101,    7,    8,    7,    8,  255,   86,  235,   10,  254,
   10,  129,    0,    0,    0,    0,  129,  129,  146,   39,
   40,   41,    6,  129,    6,    0,    0,    7,    8,    7,
    8,    0,    0,  112,   10,  158,   10,    0,    0,    0,
   11,  148,   39,   40,   41,   29,    6,    0,   39,   40,
   41,    7,    8,    0,    0,    9,    5,    6,   10,    0,
    0,    0,    7,    8,   11,    0,    9,    6,    0,   10,
    0,    0,    7,    8,    0,   11,    9,    0,    0,   10,
  171,    0,   70,   89,    0,   11,    0,  197,    0,    0,
    0,    0,    0,    0,    0,   32,    6,   90,   91,   92,
   93,    7,    8,    0,    6,    9,    0,    0,   10,    7,
    8,    0,    6,    9,   11,    0,   10,    7,    8,    0,
    0,  219,   11,  121,   10,    0,    0,    0,    0,    0,
   11,  131,   39,   40,   41,    0,    0,    0,    0,    6,
    0,    6,    0,    0,    7,    8,    7,    8,   39,   40,
   41,   10,   42,   10,   39,   40,   41,   11,  175,   11,
    0,    0,    0,    0,   39,   40,   41,  116,   42,  201,
   39,   40,   41,    0,   42,    0,    0,   47,  141,    0,
    0,  122,    0,    0,   42,  141,   39,   40,   41,    0,
   42,   47,   47,   47,   47,    0,    0,    0,    0,    0,
  143,   39,   40,   41,    0,   39,   40,   41,  151,  153,
   48,  170,    6,  155,    6,    0,    0,    7,    8,    7,
    8,    0,    0,    9,   10,  166,   10,    0,   64,  167,
  168,    0,   11,    0,    0,  174,   48,   48,    0,    0,
    0,    0,   48,   48,    0,   48,   48,   48,    0,    0,
    0,    0,    0,    0,    0,    6,  184,  127,   48,    0,
    7,    8,  117,    0,    9,   76,   78,   10,    0,    0,
    0,   82,    0,   11,   85,  199,    0,    0,    0,  128,
   48,   48,  204,    0,  127,    0,    0,  107,    0,    0,
    0,    0,    0,  213,   48,   48,    0,  192,   48,   48,
   48,    6,    0,    0,  172,    0,    7,    8,    0,  132,
  137,    0,    0,   10,    0,    0,  227,  230,  232,    0,
    0,    0,    0,    0,    0,   48,  192,  145,    0,    0,
   48,  192,    0,  192,    0,  245,    0,  191,    0,   48,
    0,    0,    0,  248,    0,    0,    0,    0,    0,    0,
  252,  202,  192,    0,    0,    0,  192,    0,    0,  256,
  172,  257,    0,  127,    0,    0,  191,    0,  181,    0,
    0,  191,  192,  191,    0,   48,    0,    0,   48,    0,
    0,    0,    0,    0,    0,  192,  172,  202,    0,    0,
    0,    0,  191,    0,    0,    0,  191,    0,   66,    6,
    0,  202,    0,    0,    7,    8,    0,  132,    9,    0,
    0,   10,  191,    0,    0,    0,    0,   11,    0,    0,
    0,    0,    0,    0,  127,  191,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                         44,
    0,   40,   44,   40,   55,   42,   43,   59,   45,   41,
   47,  125,   44,   41,   44,  210,   61,   59,  188,  123,
   41,   59,    0,   79,  182,  195,  221,   40,   41,   42,
   43,   61,   45,  123,   47,   59,  106,   44,  275,  276,
   44,   41,   42,   43,   44,   45,    0,   47,   46,  123,
  208,  102,   59,   13,   59,   59,   59,  257,  258,   59,
   60,   61,   62,   41,  234,   43,   44,   45,    0,  125,
   40,   31,   40,   33,  130,  193,   46,   43,   42,   45,
   59,   59,   60,   47,   62,   44,  123,   41,   43,   43,
   44,   45,    0,   59,  223,  224,  214,   40,    2,    3,
  218,  125,   61,   59,  268,   59,   60,   67,   62,   41,
  239,   43,   44,   45,    0,  185,    0,  257,   41,  175,
  125,   44,  125,  123,  259,  125,   30,   59,   60,  247,
   62,   41,  257,   41,   44,   43,   44,   45,   41,   94,
   95,   44,    0,  257,   43,  123,   45,  125,  262,  263,
   40,   59,   60,  257,   62,  269,    0,  258,  262,  263,
  211,  275,  275,   41,    0,  269,  256,  257,   41,  123,
   43,  125,   45,   59,  257,   59,  246,   40,   41,   42,
   43,  123,   45,  257,   47,  257,  257,  274,  262,  263,
   41,  123,   43,  125,   45,  269,   41,  123,   43,   41,
   45,   59,   44,   41,  256,   46,   47,   41,  260,  261,
   44,  257,  257,   59,  256,  123,  258,  125,  257,  125,
  257,  258,  259,  268,  258,  262,  263,  257,  256,  123,
  267,   40,  269,   42,   43,  256,   45,  123,   47,  125,
  277,  125,  274,  256,  257,  258,  259,  275,  276,  256,
  257,  264,  256,  257,  275,  276,  256,  257,   99,  100,
  260,  261,  262,  263,  277,  123,  266,  125,  268,  269,
  270,  271,  272,  273,  274,  275,  257,  256,  256,  257,
   59,  125,  260,  261,  262,  263,  260,  274,  266,  125,
  256,  269,  270,  271,  272,  273,  274,  275,  257,   40,
  256,   40,  256,  257,  260,  261,  260,  261,  262,  263,
   59,   41,  266,  257,   44,  269,  270,  271,  272,  273,
  274,  275,   40,   59,  256,  257,  123,   45,  260,  261,
  262,  263,   41,  257,  266,   44,  257,  269,  270,  271,
  272,  273,  274,  275,   40,   40,  261,   41,  256,  257,
   45,   40,  260,  261,  262,  263,   45,   41,  266,   41,
    0,  269,  270,  271,  272,  273,  274,  275,    0,    0,
  256,  257,  256,  257,  260,  261,  262,  263,  262,  263,
  266,    0,  266,  269,  125,  269,  125,    0,    0,  275,
   43,  275,   45,  256,  257,  258,  259,  123,  256,  257,
  261,  178,  260,  261,  262,  263,  231,   60,  266,   62,
  256,  269,  275,  257,  277,  261,   85,  275,  262,  263,
  108,  257,  266,   -1,   -1,  269,  262,  263,   -1,   -1,
  266,  275,  123,  269,   -1,   40,   -1,   42,   43,  275,
   45,   -1,   47,   -1,   -1,  125,   -1,   -1,  257,  258,
  259,   40,   -1,   42,   43,  125,   45,   40,   47,   42,
   43,   41,   45,   43,   47,   45,  275,   40,  277,   42,
   43,   -1,   45,   40,   47,   42,   43,  256,   45,   -1,
   47,   -1,  261,   42,   43,   44,   45,   -1,   47,   40,
   45,   42,  125,   41,   45,   43,   47,   45,   -1,   -1,
  125,   60,   61,   62,   40,   60,   42,  256,   40,   45,
   42,   47,  261,   45,   -1,   47,  257,   -1,  257,   74,
  256,  262,  263,  262,  263,  261,   81,  125,  269,  125,
  269,  257,   -1,   -1,   -1,   -1,  262,  263,  256,  257,
  258,  259,  257,  269,  257,   -1,   -1,  262,  263,  262,
  263,   -1,   -1,  108,  269,  110,  269,   -1,   -1,   -1,
  275,  256,  257,  258,  259,  256,  257,   -1,  257,  258,
  259,  262,  263,   -1,   -1,  266,  256,  257,  269,   -1,
   -1,   -1,  262,  263,  275,   -1,  266,  257,   -1,  269,
   -1,   -1,  262,  263,   -1,  275,  266,   -1,   -1,  269,
  125,   -1,  125,  256,   -1,  275,   -1,  162,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,  256,  257,  270,  271,  272,
  273,  262,  263,   -1,  257,  266,   -1,   -1,  269,  262,
  263,   -1,  257,  266,  275,   -1,  269,  262,  263,   -1,
   -1,  196,  275,  125,  269,   -1,   -1,   -1,   -1,   -1,
  275,  256,  257,  258,  259,   -1,   -1,   -1,   -1,  257,
   -1,  257,   -1,   -1,  262,  263,  262,  263,  257,  258,
  259,  269,  277,  269,  257,  258,  259,  275,  267,  275,
   -1,   -1,   -1,   -1,  257,  258,  259,   61,  277,  125,
  257,  258,  259,   -1,  277,   -1,   -1,  256,  257,   -1,
   -1,   75,   -1,   -1,  277,  256,  257,  258,  259,   -1,
  277,  270,  271,  272,  273,   -1,   -1,   -1,   -1,   -1,
  256,  257,  258,  259,   -1,  257,  258,  259,  102,  103,
   10,  256,  257,  107,  257,   -1,   -1,  262,  263,  262,
  263,   -1,   -1,  266,  269,  119,  269,   -1,   28,  123,
  124,   -1,  275,   -1,   -1,  129,   36,   37,   -1,   -1,
   -1,   -1,   42,   43,   -1,   45,   46,   47,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,  257,  150,   79,   58,   -1,
  262,  263,   62,   -1,  266,   36,   37,  269,   -1,   -1,
   -1,   42,   -1,  275,   45,  169,   -1,   -1,   -1,   79,
   80,   81,  176,   -1,  106,   -1,   -1,   58,   -1,   -1,
   -1,   -1,   -1,  187,   94,   95,   -1,  159,   98,   99,
  100,  257,   -1,   -1,  126,   -1,  262,  263,   -1,   80,
   81,   -1,   -1,  269,   -1,   -1,  210,  211,  212,   -1,
   -1,   -1,   -1,   -1,   -1,  125,  188,   98,   -1,   -1,
  130,  193,   -1,  195,   -1,  229,   -1,  159,   -1,  139,
   -1,   -1,   -1,  237,   -1,   -1,   -1,   -1,   -1,   -1,
  244,  173,  214,   -1,   -1,   -1,  218,   -1,   -1,  253,
  182,  255,   -1,  185,   -1,   -1,  188,   -1,  139,   -1,
   -1,  193,  234,  195,   -1,  175,   -1,   -1,  178,   -1,
   -1,   -1,   -1,   -1,   -1,  247,  208,  209,   -1,   -1,
   -1,   -1,  214,   -1,   -1,   -1,  218,   -1,  256,  257,
   -1,  223,   -1,   -1,  262,  263,   -1,  178,  266,   -1,
   -1,  269,  234,   -1,   -1,   -1,   -1,  275,   -1,   -1,
   -1,   -1,   -1,   -1,  246,  247,
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
"funcion : tipo ID '(' parametros_formales ')' $$1 '{' lista_sentencia_funcion '}'",
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

//#line 411 "grama.y"
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

  int crearTerceto(String operacion, String variable, String valor){
    Compilador.tercetos.add(new Terceto(operacion, variable, valor));
    return Compilador.tercetos.size() - 1;
  }

  private void completarBF(int index, int destino) {
        Terceto t = Compilador.tercetos.get(index);
        t.setValor3("[" + destino + "]");
    }

private void completarBI(int index, int destino) {
    Terceto t = Compilador.tercetos.get(index);
    t.setValor2("[" + destino + "]");
  }
//#line 707 "Parser.java"
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
        if(!TablaDeSimbolos.checkVar(val_peek(3).sval, "", tipo, "nombre de programa")){
           yyerror("La variable "+val_peek(2).sval+" ya fue declarada");
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
        int t = crearTerceto(":=", val_peek(3).sval, val_peek(1).sval);
    }
break;
case 30:
//#line 77 "grama.y"
{
        int t = crearTerceto("+", val_peek(2).sval, val_peek(0).sval);

        yyval=new ParserVal("[" + t + "]");
    }
break;
case 31:
//#line 83 "grama.y"
{
        int t = crearTerceto("-", val_peek(2).sval, val_peek(0).sval);
        yyval=new ParserVal("[" + t + "]");
    }
break;
case 32:
//#line 87 "grama.y"
{yyerror("falta operando a izquierda de +");}
break;
case 33:
//#line 88 "grama.y"
{yyerror("falta operando a derecha de +");}
break;
case 34:
//#line 89 "grama.y"
{yyerror("falta operando a derecha de -");}
break;
case 35:
//#line 90 "grama.y"
{yyval=val_peek(0);}
break;
case 36:
//#line 91 "grama.y"
{addEstructura("trunc");}
break;
case 37:
//#line 92 "grama.y"
{addEstructura("trunc");yyerror("falta abrir parentesis en trunc");}
break;
case 38:
//#line 93 "grama.y"
{addEstructura("trunc");yyerror("falta cerrar parentesis en trunc");yyerrflag=0;}
break;
case 39:
//#line 94 "grama.y"
{addEstructura("trunc");yyerror("falta argumento en trunc");}
break;
case 40:
//#line 99 "grama.y"
{
        int t = crearTerceto("*", val_peek(2).sval, val_peek(0).sval);
        yyval=new ParserVal("[" + t + "]");
    }
break;
case 41:
//#line 104 "grama.y"
{
        int t = crearTerceto("/", val_peek(2).sval, val_peek(0).sval);
        yyval=new ParserVal("[" + t + "]");
    }
break;
case 42:
//#line 108 "grama.y"
{yyerror("falta operando a derecha de /");}
break;
case 43:
//#line 109 "grama.y"
{yyerror("falta operando a derecha de *");}
break;
case 44:
//#line 110 "grama.y"
{yyerror("falta operando a izquierda de /");}
break;
case 45:
//#line 111 "grama.y"
{yyerror("falta operando a izquierda de *");}
break;
case 46:
//#line 112 "grama.y"
{yyval=val_peek(0);}
break;
case 47:
//#line 116 "grama.y"
{yyval=val_peek(0);}
break;
case 48:
//#line 117 "grama.y"
{yyval=val_peek(0);}
break;
case 49:
//#line 118 "grama.y"
{yyval=new ParserVal(val_peek(0).sval);}
break;
case 50:
//#line 119 "grama.y"
{addEstructura("lambda"); yyval=val_peek(0);}
break;
case 51:
//#line 120 "grama.y"
{yyval=val_peek(0);}
break;
case 52:
//#line 124 "grama.y"
{check_rango(val_peek(0).sval); yyval=new ParserVal(val_peek(0).sval);}
break;
case 53:
//#line 125 "grama.y"
{check_rango("-"+val_peek(0).sval); yyval=new ParserVal("-"+val_peek(1).sval);}
break;
case 54:
//#line 130 "grama.y"
{
        yyval = new ParserVal(val_peek(2).sval + '.' + val_peek(0).sval);

    }
break;
case 55:
//#line 135 "grama.y"
{
        if(!TablaDeSimbolos.estaDeclarado(val_peek(0).sval, Compilador.getAmbito())){
                    yyerror("La variable "+val_peek(0).sval+" no fue declarada");
                    TablaDeSimbolos.eliminar(val_peek(0).sval);
                } else TablaDeSimbolos.eliminar(val_peek(0).sval);
        yyval=val_peek(0);

    }
break;
case 56:
//#line 146 "grama.y"
{addEstructura("invocacion a funcion"); yyval=val_peek(3);}
break;
case 58:
//#line 151 "grama.y"
{yyerror("Falta parametro formal");}
break;
case 59:
//#line 152 "grama.y"
{yyerror("Falta flecha y parametro formal");}
break;
case 61:
//#line 154 "grama.y"
{yyerror("Falta parametro formal");}
break;
case 62:
//#line 155 "grama.y"
{yyerror("Falta flecha y parametro formal");}
break;
case 63:
//#line 156 "grama.y"
{yyerror("error en parametros de invocacion");}
break;
case 64:
//#line 161 "grama.y"
{
        int t = crearTerceto("print", val_peek(2).sval, "-");
        yyval=new ParserVal("[" + t + "]");
    }
break;
case 65:
//#line 165 "grama.y"
{yyerror("falta argumento en print");}
break;
case 67:
//#line 170 "grama.y"
{yyerror("falta ;");}
break;
case 70:
//#line 176 "grama.y"
{yyerror("argumento invalido en print");}
break;
case 71:
//#line 181 "grama.y"
{
        int bi = Compilador.pilaSaltos.remove(Compilador.pilaSaltos.size()-1);
        completarBI(bi, Compilador.tercetos.size());
    }
break;
case 72:
//#line 185 "grama.y"
{yyerror("no hay sentencias en else");}
break;
case 74:
//#line 189 "grama.y"
{ yyerror("falta endif");}
break;
case 75:
//#line 190 "grama.y"
{ yyerror("falta endif");yyerror("no hay sentencias en else");}
break;
case 76:
//#line 191 "grama.y"
{ yyerror("falta endif");}
break;
case 77:
//#line 194 "grama.y"
{yyerror("no hay sentencias en then");}
break;
case 78:
//#line 195 "grama.y"
{yyerror("no hay sentencias en then");yyerror("no hay sentencias en else");}
break;
case 79:
//#line 196 "grama.y"
{yyerror("no hay sentencias en then");}
break;
case 80:
//#line 199 "grama.y"
{yyerror("no hay sentencias en then");yyerror("falta endif");}
break;
case 81:
//#line 200 "grama.y"
{yyerror("no hay sentencias en then");yyerror("no hay sentencias en else");yyerror("falta endif");}
break;
case 82:
//#line 201 "grama.y"
{yyerror("no hay sentencias en then");yyerror("falta endif");}
break;
case 83:
//#line 205 "grama.y"
{
        int bf = Compilador.pilaSaltos.remove(Compilador.pilaSaltos.size()-1);
        completarBF(bf, Compilador.tercetos.size()+1);
    }
break;
case 84:
//#line 212 "grama.y"
{
        int bf = Compilador.pilaSaltos.remove(Compilador.pilaSaltos.size()-1);
        completarBF(bf, Compilador.tercetos.size());
    }
break;
case 85:
//#line 218 "grama.y"
{
        int bi = crearTerceto("BI", "-", "-");
        Compilador.pilaSaltos.add(bi);}
break;
case 86:
//#line 224 "grama.y"
{
        int t = crearTerceto(val_peek(2).sval, val_peek(3).sval, val_peek(1).sval);
        int bf = crearTerceto("BF", "["+t+"]", "-");
        Compilador.pilaSaltos.add(bf);
        yyval = new ParserVal("[" + t + "]");
    }
break;
case 87:
//#line 230 "grama.y"
{yyerror("falta cerrar parentesis");}
break;
case 88:
//#line 231 "grama.y"
{yyerror("falta abrir parentesis");}
break;
case 89:
//#line 232 "grama.y"
{yyerror("faltan parentesis");}
break;
case 90:
//#line 237 "grama.y"
{yyval=new ParserVal("==");}
break;
case 91:
//#line 238 "grama.y"
{yyval=new ParserVal("=!");}
break;
case 92:
//#line 239 "grama.y"
{yyval=new ParserVal("<=");}
break;
case 93:
//#line 240 "grama.y"
{yyval=new ParserVal(">=");}
break;
case 94:
//#line 241 "grama.y"
{yyval=new ParserVal("<");}
break;
case 95:
//#line 242 "grama.y"
{yyval=new ParserVal(">");}
break;
case 96:
//#line 243 "grama.y"
{yyerror("en condicion");}
break;
case 100:
//#line 260 "grama.y"
{
        String ambito = Compilador.getAmbito();
        if(!TablaDeSimbolos.checkVar(val_peek(0).sval, ambito, tipo, "nombre de variable")){
           yyerror("La variable "+val_peek(0).sval+" ya fue declarada");
        }

    }
break;
case 101:
//#line 267 "grama.y"
{yyval = new ParserVal(val_peek(2).sval+","+val_peek(0).sval);}
break;
case 102:
//#line 268 "grama.y"
{yyerror("falta , en lista de variables");}
break;
case 103:
//#line 274 "grama.y"
{
        String ambito = Compilador.getAmbito();
        if(!TablaDeSimbolos.checkVar(val_peek(3).sval, ambito, tipo, "nombre de funcion")){
            yyerror("La variable "+val_peek(3).sval+" ya fue declarada");
        }
        Compilador.entrarAmbito(val_peek(3).sval);
        int t = crearTerceto("inicio de funcion", val_peek(3).sval, "-");
    }
break;
case 104:
//#line 282 "grama.y"
{
        int t = crearTerceto("fin de funcion", val_peek(7).sval, "-");
        Compilador.salirAmbito();
    }
break;
case 105:
//#line 286 "grama.y"
{ yyerror("falta declarar nombre de funcion");}
break;
case 106:
//#line 287 "grama.y"
{yyerror("faltan parametros formales");}
break;
case 107:
//#line 288 "grama.y"
{yyerror("falta declarar nombre de funcion"); yyerror("faltan parametros formales");}
break;
case 108:
//#line 293 "grama.y"
{
        String ambito = Compilador.getAmbito();
                if(!TablaDeSimbolos.checkVar(val_peek(0).sval, ambito, tipo, "nombre de parametro")){
                   yyerror("La variable "+val_peek(0).sval+" ya fue declarada");
                }
    }
break;
case 109:
//#line 300 "grama.y"
{
        String ambito = Compilador.getAmbito();
                if(!TablaDeSimbolos.checkVar(val_peek(0).sval, ambito, tipo, "nombre de parametro")){
                   yyerror("La variable "+val_peek(0).sval+" ya fue declarada");
                }
    }
break;
case 112:
//#line 308 "grama.y"
{yyerror("error en parametro formal");}
break;
case 113:
//#line 312 "grama.y"
{tipo = "uint";}
break;
case 118:
//#line 328 "grama.y"
{
        addEstructura("return");
        int t = crearTerceto("return", val_peek(2).sval, "-");
        yyval=new ParserVal("[" + t + "]");
        }
break;
case 121:
//#line 346 "grama.y"
{yyerror("falta cuerpo sentencias");}
break;
case 122:
//#line 347 "grama.y"
{yyerror("falta de ;");}
break;
case 123:
//#line 349 "grama.y"
{ yyerror("falta while");}
break;
case 124:
//#line 350 "grama.y"
{yyerror("falta cuerpo sentencias"); yyerror("falta while");}
break;
case 125:
//#line 351 "grama.y"
{yyerror("falta de ) en condicion");yyerror("falta de ;");yyerror("falta while");}
break;
case 126:
//#line 352 "grama.y"
{yyerror("falta de ) en condicion");yyerror("falta de ;");yyerror("falta cuerpo sentencias");yyerror("falta while");}
break;
case 127:
//#line 355 "grama.y"
{Compilador.pilaSaltos.add(Compilador.tercetos.size());}
break;
case 128:
//#line 358 "grama.y"
{
        int salto = Compilador.pilaSaltos.remove(0);
        int bi = crearTerceto("BI", "[" +salto+"]", "-");
    }
break;
case 129:
//#line 364 "grama.y"
{Compilador.entrarAmbito("ua"+cantUnidadesAnonimas); cantUnidadesAnonimas+= 1;}
break;
case 130:
//#line 367 "grama.y"
{Compilador.salirAmbito();}
break;
case 131:
//#line 368 "grama.y"
{yyerror("no hay sentencias dentro de las llaves");}
break;
case 132:
//#line 369 "grama.y"
{Compilador.salirAmbito();}
break;
case 133:
//#line 370 "grama.y"
{yyerror("Error en sentencia");}
break;
case 134:
//#line 375 "grama.y"
{yyval= new ParserVal("soy un lamnda");}
break;
case 135:
//#line 376 "grama.y"
{ yyerror("falta abrir llave en cuerpo de sentencia lambda");}
break;
case 136:
//#line 377 "grama.y"
{yyerror("falta cerra llave en cuerpo de sentencia lambda");}
break;
case 137:
//#line 378 "grama.y"
{ yyerror("faltan llaves en cuerpo de sentencia lambda");}
break;
case 140:
//#line 389 "grama.y"
{
        int t = crearTerceto("=", val_peek(3).sval, val_peek(1).sval);
        yyval=new ParserVal("[" + t + "]");
    }
break;
case 141:
//#line 396 "grama.y"
{yyval=val_peek(0);}
break;
case 142:
//#line 397 "grama.y"
{yyval=new ParserVal("{"+val_peek(2).sval + ", " + val_peek(0).sval+"}");}
break;
case 143:
//#line 398 "grama.y"
{yyerror("falta , en lista de variables");}
break;
case 144:
//#line 402 "grama.y"
{yyval=val_peek(0);}
break;
case 145:
//#line 403 "grama.y"
{yyval=new ParserVal("{"+val_peek(2).sval + ", " + val_peek(0).sval+"}");}
break;
case 146:
//#line 404 "grama.y"
{yyerror("falta , en lista de constantes");}
break;
//#line 1429 "Parser.java"
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
