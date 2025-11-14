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
    2,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    1,    1,    1,    3,    3,
    3,    5,    5,    4,    4,    4,    4,    4,    6,    9,
   16,   16,   16,   16,   16,   16,   16,   16,   16,   16,
   18,   18,   18,   18,   18,   18,   18,   19,   19,   19,
   19,   19,   22,   22,   15,   15,   20,   23,   23,   23,
   23,   23,   23,   23,   11,   11,   17,   17,   25,   25,
   25,   10,   10,   10,   10,   10,   10,   10,   10,   10,
   10,   10,   10,   26,   26,   26,   26,   29,   29,   29,
   29,   29,   29,   29,   30,   30,    7,   32,   32,   32,
   34,    8,    8,    8,    8,   33,   33,   33,   33,   33,
   31,   35,   35,   36,   36,   12,   24,   14,   14,   14,
   14,   14,   14,   14,   27,   28,   28,   28,   28,   21,
   21,   21,   21,   37,   37,   13,   38,   38,   38,   39,
   39,   39,
};
final static short yylen[] = {                            2,
    0,    5,    3,    3,    3,    2,    3,    2,    4,    3,
    3,    3,    2,    2,    2,    1,    2,    2,    1,    1,
    1,    1,    1,    1,    1,    1,    1,    1,    1,    4,
    3,    3,    2,    3,    3,    1,    4,    3,    3,    3,
    3,    3,    3,    3,    2,    2,    1,    1,    1,    1,
    1,    1,    1,    2,    3,    1,    4,    3,    2,    1,
    5,    4,    3,    1,    5,    4,    1,    1,    1,    1,
    1,    9,    7,    6,    8,    6,    5,    7,    5,    4,
    6,    4,    3,    5,    4,    4,    3,    1,    1,    1,
    1,    1,    1,    1,    1,    2,    3,    1,    3,    2,
    0,    9,    7,    7,    6,    3,    2,    5,    4,    1,
    1,    1,    2,    1,    1,    5,    1,    6,    5,    5,
    5,    4,    4,    3,    0,    3,    2,    1,    3,    8,
    7,    7,    6,    3,    3,    4,    1,    3,    2,    1,
    3,    2,
};
final static short yydefred[] = {                         0,
    0,    0,    0,    0,    0,    0,    0,    0,  125,    0,
  111,   14,    0,   16,   19,   20,   21,   22,   23,   24,
   25,   26,   27,   28,   29,    0,    0,    0,    0,    0,
    0,    0,    0,   18,    0,    0,    0,    0,    0,   50,
   53,    0,    0,    0,    0,    0,    0,   48,    0,    0,
   47,   49,   51,   52,    0,    7,   17,    0,    0,    0,
    0,    0,    0,  139,   12,    0,    0,    5,   10,    3,
   55,   71,   70,    0,    0,    0,    0,    0,    0,    0,
  128,    0,    0,    0,    0,    0,    0,    0,   54,    0,
    0,   46,   45,   94,   88,   89,   90,   91,    0,    0,
   92,   93,    0,    0,    0,   68,    0,    0,   67,   83,
    0,    0,    0,  110,    0,    0,    0,    0,  100,    0,
   97,  138,  140,    0,    9,    1,   66,    0,    0,    0,
    0,  127,   95,    0,  122,    0,    0,   64,    0,    0,
    0,   39,   40,    0,   38,    0,    0,   34,    0,   35,
    0,    0,   44,   41,   43,   42,    0,   82,    0,   80,
    0,   30,    0,    0,    0,    0,  107,    0,    0,   99,
  142,    0,  136,    2,   65,  116,  119,  129,  126,   96,
    0,  121,   57,    0,    0,   37,    0,    0,   86,   79,
    0,    0,    0,   77,    0,  101,  106,  115,  114,    0,
  112,    0,    0,    0,  141,  118,    0,   58,   84,    0,
    0,    0,   81,    0,   76,    0,   74,    0,    0,  105,
  113,    0,    0,  109,    0,    0,    0,    0,  133,   78,
   73,    0,  104,    0,  103,  108,   61,    0,  132,  131,
    0,    0,    0,   75,    0,  130,  134,  135,   72,  102,
};
final static short yydgoto[] = {                          4,
   13,  174,   14,   15,   16,   17,   18,   19,   20,   21,
   22,   23,   24,   25,   26,   49,  110,   50,   51,   52,
   53,   54,  140,  141,   77,   55,   38,   84,  103,  134,
   27,   61,  118,  219,  200,  201,  229,   28,  124,
};
final static short yysindex[] = {                       -89,
  339,  319,  317,    0,  -34,   11,   32,   36,    0,  418,
    0,    0,  366,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0, -228,  -38,  -29,  -54,  619,
  368,   -7,  534,    0, -168,  -12,  428,  -36,  -24,    0,
    0,  434,  473, -148,  192,  312,  312,    0,  348,  116,
    0,    0,    0,    0,  -51,    0,    0,  428,   87,  -27,
   -6, -122, -111,    0,    0,   -5,  591,    0,    0,    0,
    0,    0,    0, -130,   22,    6,  120,   78,  418,  292,
    0, -228,   22,  396,  412,  138,  132,  116,    0,  348,
  -81,    0,    0,    0,    0,    0,    0,    0,  450,  467,
    0,    0,  428,  283,  306,    0,  155,   22,    0,    0,
 -103,   35,  -20,    0, -130,   59,  -64,   58,    0,  -58,
    0,    0,    0,  -41,    0,    0,    0,   22,   22,   22,
   86,    0,    0,  376,    0,  418,   22,    0,    6,   64,
    3,    0,    0,  163,    0,  428,  162,    0,  116,    0,
  116,  392,    0,    0,    0,    0,   22,    0, -103,    0,
   45,    0,   89,  147,  -37,  483,    0,  107,  -78,    0,
    0,    2,    0,    0,    0,    0,    0,    0,    0,    0,
   22,    0,    0,  428,   30,    0,  403,  -73,    0,    0,
  222,  265,   22,    0,  483,    0,    0,    0,    0, -113,
    0,  483, -130,   43,    0,    0,   53,    0,    0,  -62,
  248,   22,    0,   22,    0, -103,    0,  485,  213,    0,
    0,  523,   90,    0,  101,  262,  320,  -71,    0,    0,
    0,  274,    0,  483,    0,    0,    0,  320,    0,    0,
  328,  329,   22,    0,  575,    0,    0,    0,    0,    0,
};
final static short yyrindex[] = {                         0,
    0,    0,    0,    0,    0,  -44,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,   42,    0,    0,  361,    0,
  382,  385,  388,    0,    0,    0,    0,    0,    1,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,   23,
    0,    0,    0,    0,  275,    0,    0,    0,   -3,    0,
    0,    0,    0,    0,    0,  389,  401,    0,    0,    0,
    0,    0,    0,    0,    0,  380,    0,    0,    0,    0,
    0,  442,  117,    0,    0,    0,    0,   47,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,  275,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,  157,    0,  -31,    0,
  270,    0,    0,    0,    0,    0,    0,    0,   69,    0,
   93,  115,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
  165,    0,    0,    0,  271,    0,  143,    0,    0,    0,
    0,  275,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,  293,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,  304,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
};
final static short yygindex[] = {                         0,
   95,    0,  449,  456,  656,    0,    0,    0,    0,    0,
    0,    0,    0,    0,  731,  810,  627,   -4,   28,    0,
    0,    0,    0,  218,    0,  -14,  -28,  -75,  334, -151,
   56,    0,  294,    0, -176, -145,  -87,    0,    0,
};
final static int YYTABLESIZE=994;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                         56,
   56,   60,  172,   45,   34,   46,   43,  109,   44,  117,
   47,  220,  117,  116,   62,   85,   56,  109,  218,   80,
  163,   35,   36,   83,   34,  222,  111,   74,   75,   46,
   43,   63,   44,    3,   47,  161,  211,  120,   88,   58,
   98,   56,   56,   56,   56,   56,   33,   56,   99,  210,
  100,   34,  109,   34,  221,   98,   35,  245,  226,   56,
   56,   56,   56,   36,  130,   36,   36,   36,   31,  137,
   65,   36,  221,   92,   93,   37,  221,   99,  159,  100,
  109,   36,   36,  191,   36,  137,   80,   33,   71,   33,
   33,   33,   32,  109,  149,  151,   31,   33,  168,  221,
   91,  169,  137,  109,  183,   33,   33,  184,   33,   31,
   89,   31,   31,   31,   87,  117,  124,   69,  129,  125,
   99,  181,  100,   56,   67,   56,  113,   31,   31,   91,
   31,  154,  156,   32,    6,   32,   32,   32,  239,  240,
  232,   91,   85,    6,   11,   36,  123,   36,    7,    8,
  246,   32,   32,    6,   32,   10,  123,  104,    7,    8,
  128,   11,  105,  216,  120,   10,    1,    2,  117,   33,
  165,   33,  145,   87,   99,  147,  100,   74,  143,   46,
   43,  166,   44,    6,   47,  241,  242,  196,    7,    8,
  169,   31,  167,   31,    6,   10,   11,  203,  170,    7,
    8,   85,  188,  186,  106,   99,   10,  100,  107,  108,
  178,  195,   56,  109,  106,   32,  171,   32,   59,  197,
   39,   40,   41,   56,  204,    7,    8,    6,  114,  202,
   79,   74,   10,   46,   43,  114,   44,   87,   47,   87,
   42,  124,  117,   72,   39,   40,   41,   11,  115,  106,
  119,   73,   98,   98,   11,  115,   56,   56,  223,  205,
   56,   56,   56,   56,   42,   85,   56,   85,   56,   56,
   56,   56,   56,   56,   56,   56,  185,  106,   36,   36,
  109,  123,   36,   36,   36,   36,  208,  228,   36,  120,
  106,   36,   36,   36,   36,   36,   36,   36,  137,  224,
  106,  228,   33,   33,  192,  193,   33,   33,   33,   33,
   60,   59,   33,   60,   59,   33,   33,   33,   33,   33,
   33,   33,   74,  109,   31,   31,  225,   44,   31,   31,
   31,   31,  109,   63,   31,  234,   63,   31,   31,   31,
   31,   31,   31,   31,   62,   74,  236,   62,   32,   32,
   44,   74,   32,   32,   32,   32,   44,  237,   32,  228,
   15,   32,   32,   32,   32,   32,   32,   32,  247,  248,
   87,   87,  227,  124,   87,   87,   87,   87,  124,  124,
   87,    8,  124,   87,   13,  124,  238,    6,   11,   87,
   99,  124,  100,  142,   39,   40,   41,  125,   85,   85,
    4,  207,   85,   85,   85,   85,  164,  101,   85,  102,
  106,   85,   11,  123,   42,  157,  132,   85,  123,  123,
   69,  120,  123,  146,    0,  123,  120,  120,    0,    0,
  120,  123,  189,  120,   99,   45,  100,   46,   43,  120,
   44,   30,   47,  209,    0,   99,    0,  100,   39,   40,
   41,   74,    0,   46,   43,    0,   44,   45,   47,   46,
   43,   57,   44,   12,   47,    0,   11,   74,   42,   46,
   43,    0,   44,   86,   47,   46,   43,  106,   44,   57,
   47,   57,  212,   48,   48,  137,   48,    0,   48,   74,
   56,   46,   68,   81,   44,    0,   47,    0,    0,    0,
  179,   48,  137,   48,    6,    0,   74,    0,   46,    7,
    8,   44,   74,   47,   46,   57,   10,   44,    6,   47,
  106,    0,    0,    7,    8,  214,    0,    0,    0,  106,
   10,  125,    0,    0,  243,  133,  125,  125,  153,   39,
   40,   41,    0,  125,    0,    0,    0,  131,    6,    0,
    0,    0,    0,    7,    8,    0,    0,    0,    0,    0,
   10,  155,   39,   40,   41,    0,   81,    0,   39,   40,
   41,    0,   32,    6,   29,    6,    0,    0,    7,    8,
    7,    8,    9,    0,    9,   10,    0,   10,    0,  180,
    0,   11,    0,   11,    5,    6,    0,    0,    0,    0,
    7,    8,    0,   94,    9,    0,    0,   10,    0,  233,
    0,    0,    0,   11,   81,    0,    0,   95,   96,   97,
   98,  198,    6,    0,    6,    0,    0,    7,    8,    7,
    8,    9,    6,    9,   10,    0,   10,    7,    8,    0,
   11,    0,   11,  133,   10,    0,    0,  235,    0,    0,
  198,    0,   39,   40,   41,  198,    0,  198,   70,    0,
    0,    0,  136,    0,    0,  133,  180,  138,   39,   40,
   41,   81,   42,  198,   39,   40,   41,  198,    0,    0,
    0,  180,    0,    0,   39,   40,   41,  121,   42,  198,
   39,   40,   41,    0,   42,    0,    0,   48,  137,  250,
  198,  127,    0,    0,   42,  148,   39,   40,   41,  135,
   42,   48,   48,   48,   48,  126,    0,    0,    0,    0,
    0,    0,  150,   39,   40,   41,    0,    0,    0,   39,
   40,   41,    0,  158,  160,    0,    0,    0,  162,    6,
   48,    6,    0,    0,    7,    8,    7,    8,    0,    0,
  173,   10,    0,   10,  175,  176,  177,   11,   64,   11,
    0,    0,    0,  182,    0,    0,   48,   48,   82,    0,
    0,    0,   48,   48,    0,   48,   48,   48,    0,    6,
    0,    0,    0,  190,    7,    8,    0,  194,   48,    0,
    6,   10,  122,    0,    0,    7,    8,   11,    0,    9,
    0,    0,   10,    0,    0,    0,    0,  206,   11,   48,
    0,    0,    0,    0,   48,   48,   48,  213,  215,  217,
    0,  199,    0,    0,    0,    0,    0,    0,    0,   48,
   48,    6,    0,   48,   48,   48,    7,    8,  230,    0,
  231,    0,    0,   10,    0,   76,   78,    6,    0,   11,
  199,   87,    7,    8,   90,  199,    9,  199,  244,   10,
    0,    0,    0,    0,    0,   11,   48,  112,    0,  249,
    0,    0,    0,  199,   66,    6,   48,  199,    0,    0,
    7,    8,    0,    0,    9,    0,    0,   10,    0,  199,
    0,    0,    0,   11,  139,  144,    0,    0,    0,    0,
  199,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,  152,    0,   48,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,  187,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,  139,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                         44,
    0,   40,   44,   40,   59,   42,   43,   59,   45,   41,
   47,  125,   44,   41,   44,   40,   61,   59,  195,  123,
   41,   46,    0,   38,   59,  202,   55,   40,   41,   42,
   43,   61,   45,  123,   47,  111,  188,   44,   43,  268,
   44,   41,   42,   43,   44,   45,    0,   47,   43,  123,
   45,   59,   59,   59,  200,   59,   46,  234,  210,   59,
   60,   61,   62,   41,   79,   43,   44,   45,    0,   84,
  125,   40,  218,   46,   47,   40,  222,   43,  107,   45,
   59,   59,   60,  159,   62,   44,  123,   41,  257,   43,
   44,   45,    0,   59,   99,  100,    2,    3,   41,  245,
   45,   44,   61,   59,   41,   59,   60,   44,   62,   41,
  259,   43,   44,   45,    0,   60,    0,  125,   41,  125,
   43,  136,   45,  123,   30,  125,   40,   59,   60,   74,
   62,  104,  105,   41,  257,   43,   44,   45,  226,  227,
  216,   86,    0,  257,  275,  123,  258,  125,  262,  263,
  238,   59,   60,  257,   62,  269,    0,   42,  262,  263,
   41,  275,   47,  192,    0,  269,  256,  257,  113,  123,
  115,  125,   41,   59,   43,  257,   45,   40,   41,   42,
   43,  123,   45,  257,   47,  257,  258,   41,  262,  263,
   44,  123,  257,  125,  257,  269,  275,  276,  257,  262,
  263,   59,   41,   41,  256,   43,  269,   45,  260,  261,
  125,  123,  257,   59,  256,  123,  258,  125,  257,  257,
  257,  258,  259,  268,  169,  262,  263,  257,  256,  123,
  267,   40,  269,   42,   43,  256,   45,  123,   47,  125,
  277,  125,  274,  256,  257,  258,  259,  275,  276,  256,
  257,  264,  256,  257,  275,  276,  256,  257,  203,  258,
  260,  261,  262,  263,  277,  123,  266,  125,  268,  269,
  270,  271,  272,  273,  274,  275,  274,  256,  256,  257,
   59,  125,  260,  261,  262,  263,  257,   40,  266,  125,
  256,  269,  270,  271,  272,  273,  274,  275,  257,  257,
  256,   40,  256,  257,  260,  261,  260,  261,  262,  263,
   41,   41,  266,   44,   44,  269,  270,  271,  272,  273,
  274,  275,   40,   59,  256,  257,  274,   45,  260,  261,
  262,  263,   59,   41,  266,  123,   44,  269,  270,  271,
  272,  273,  274,  275,   41,   40,  257,   44,  256,  257,
   45,   40,  260,  261,  262,  263,   45,  257,  266,   40,
    0,  269,  270,  271,  272,  273,  274,  275,   41,   41,
  256,  257,  125,  257,  260,  261,  262,  263,  262,  263,
  266,    0,  266,  269,    0,  269,  125,    0,    0,  275,
   43,  275,   45,  256,  257,  258,  259,  123,  256,  257,
    0,  184,  260,  261,  262,  263,  113,   60,  266,   62,
  256,  269,  275,  257,  277,  261,  125,  275,  262,  263,
   41,  257,  266,   90,   -1,  269,  262,  263,   -1,   -1,
  266,  275,   41,  269,   43,   40,   45,   42,   43,  275,
   45,  123,   47,   41,   -1,   43,   -1,   45,  257,  258,
  259,   40,   -1,   42,   43,   -1,   45,   40,   47,   42,
   43,   13,   45,  125,   47,   -1,  275,   40,  277,   42,
   43,   -1,   45,   40,   47,   42,   43,  256,   45,   31,
   47,   33,  261,   42,   43,   44,   45,   -1,   47,   40,
  125,   42,  125,   38,   45,   -1,   47,   -1,   -1,   -1,
  125,   60,   61,   62,  257,   -1,   40,   -1,   42,  262,
  263,   45,   40,   47,   42,   67,  269,   45,  257,   47,
  256,   -1,   -1,  262,  263,  261,   -1,   -1,   -1,  256,
  269,  257,   -1,   -1,  261,   80,  262,  263,  256,  257,
  258,  259,   -1,  269,   -1,   -1,   -1,  256,  257,   -1,
   -1,   -1,   -1,  262,  263,   -1,   -1,   -1,   -1,   -1,
  269,  256,  257,  258,  259,   -1,  111,   -1,  257,  258,
  259,   -1,  256,  257,  256,  257,   -1,   -1,  262,  263,
  262,  263,  266,   -1,  266,  269,   -1,  269,   -1,  134,
   -1,  275,   -1,  275,  256,  257,   -1,   -1,   -1,   -1,
  262,  263,   -1,  256,  266,   -1,   -1,  269,   -1,  125,
   -1,   -1,   -1,  275,  159,   -1,   -1,  270,  271,  272,
  273,  166,  257,   -1,  257,   -1,   -1,  262,  263,  262,
  263,  266,  257,  266,  269,   -1,  269,  262,  263,   -1,
  275,   -1,  275,  188,  269,   -1,   -1,  125,   -1,   -1,
  195,   -1,  257,  258,  259,  200,   -1,  202,  125,   -1,
   -1,   -1,  267,   -1,   -1,  210,  211,  256,  257,  258,
  259,  216,  277,  218,  257,  258,  259,  222,   -1,   -1,
   -1,  226,   -1,   -1,  257,  258,  259,   61,  277,  234,
  257,  258,  259,   -1,  277,   -1,   -1,  256,  257,  125,
  245,   75,   -1,   -1,  277,  256,  257,  258,  259,   83,
  277,  270,  271,  272,  273,  125,   -1,   -1,   -1,   -1,
   -1,   -1,  256,  257,  258,  259,   -1,   -1,   -1,  257,
  258,  259,   -1,  107,  108,   -1,   -1,   -1,  112,  257,
   10,  257,   -1,   -1,  262,  263,  262,  263,   -1,   -1,
  124,  269,   -1,  269,  128,  129,  130,  275,   28,  275,
   -1,   -1,   -1,  137,   -1,   -1,   36,   37,   38,   -1,
   -1,   -1,   42,   43,   -1,   45,   46,   47,   -1,  257,
   -1,   -1,   -1,  157,  262,  263,   -1,  161,   58,   -1,
  257,  269,   62,   -1,   -1,  262,  263,  275,   -1,  266,
   -1,   -1,  269,   -1,   -1,   -1,   -1,  181,  275,   79,
   -1,   -1,   -1,   -1,   84,   85,   86,  191,  192,  193,
   -1,  166,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   99,
  100,  257,   -1,  103,  104,  105,  262,  263,  212,   -1,
  214,   -1,   -1,  269,   -1,   36,   37,  257,   -1,  275,
  195,   42,  262,  263,   45,  200,  266,  202,  232,  269,
   -1,   -1,   -1,   -1,   -1,  275,  136,   58,   -1,  243,
   -1,   -1,   -1,  218,  256,  257,  146,  222,   -1,   -1,
  262,  263,   -1,   -1,  266,   -1,   -1,  269,   -1,  234,
   -1,   -1,   -1,  275,   85,   86,   -1,   -1,   -1,   -1,
  245,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,  103,   -1,  184,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,  146,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,  184,
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
"$$1 :",
"prog : ID '{' lista_sentencia '}' $$1",
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
"if : IF cuerpo_condicion nuevo_ambito cuerpo_sentencia_ejecutable ELSE nuevo_ambito cuerpo_sentencia_ejecutable ENDIF punto_coma",
"if : IF cuerpo_condicion nuevo_ambito cuerpo_sentencia_ejecutable ELSE ENDIF punto_coma",
"if : IF cuerpo_condicion nuevo_ambito cuerpo_sentencia_ejecutable ENDIF punto_coma",
"if : IF cuerpo_condicion nuevo_ambito cuerpo_sentencia_ejecutable ELSE nuevo_ambito cuerpo_sentencia_ejecutable punto_coma",
"if : IF cuerpo_condicion nuevo_ambito cuerpo_sentencia_ejecutable ELSE punto_coma",
"if : IF cuerpo_condicion nuevo_ambito cuerpo_sentencia_ejecutable punto_coma",
"if : IF cuerpo_condicion ELSE nuevo_ambito cuerpo_sentencia_ejecutable ENDIF punto_coma",
"if : IF cuerpo_condicion ELSE ENDIF punto_coma",
"if : IF cuerpo_condicion ENDIF punto_coma",
"if : IF cuerpo_condicion ELSE nuevo_ambito cuerpo_sentencia_ejecutable punto_coma",
"if : IF cuerpo_condicion ELSE punto_coma",
"if : IF cuerpo_condicion punto_coma",
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
"$$2 :",
"funcion : tipo ID '(' parametros_formales ')' $$2 '{' lista_sentencia_funcion '}'",
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
"do : DO nuevo_ambito cuerpo_sentencia_ejecutable WHILE cuerpo_condicion punto_coma",
"do : DO nuevo_ambito WHILE cuerpo_condicion punto_coma",
"do : DO nuevo_ambito cuerpo_sentencia_ejecutable WHILE cuerpo_condicion",
"do : DO nuevo_ambito cuerpo_sentencia_ejecutable cuerpo_condicion punto_coma",
"do : DO nuevo_ambito cuerpo_condicion punto_coma",
"do : DO nuevo_ambito cuerpo_sentencia_ejecutable cuerpo_condicion",
"do : DO nuevo_ambito cuerpo_condicion",
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

//#line 336 "grama.y"
private ArrayList<Token> tokens = new ArrayList<>();
private ArrayList<String> estructurasDetectadas = new ArrayList<>();
private int cantUnidadesAnonimas = 1;



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
    System.out.println(Colores.AZUL+"------------------ ESTRUCTURAS DETECTADAS ------------------"+Colores.RESET);
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

//#line 699 "Parser.java"
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
//#line 16 "grama.y"
{Compilador.salirAmbito();}
break;
case 2:
//#line 16 "grama.y"
{TablaDeSimbolos.addAmbito(val_peek(4).sval, Compilador.getAmbito());System.out.println("el ambito es: "+Compilador.getAmbito());}
break;
case 3:
//#line 17 "grama.y"
{yyerror("falta nombre de programa");}
break;
case 4:
//#line 18 "grama.y"
{yyerror("falta llave de cierre de programa");}
break;
case 5:
//#line 19 "grama.y"
{yyerror("falta llave de inicio de programa");}
break;
case 6:
//#line 20 "grama.y"
{yyerror("falta nombre del programa");yyerror("falta llave de cierre de programa");}
break;
case 7:
//#line 21 "grama.y"
{yyerror("falta nombre del programa");yyerror("falta llave de inicio de programa");}
break;
case 8:
//#line 22 "grama.y"
{yyerror("no hay llaves del programa");}
break;
case 9:
//#line 24 "grama.y"
{yyerror("error en programa");}
break;
case 10:
//#line 25 "grama.y"
{yyerror("falta nombre de programa");yyerror("error en programa");}
break;
case 11:
//#line 26 "grama.y"
{yyerror("falta llave de cierre de programa");yyerror("error en programa");}
break;
case 12:
//#line 27 "grama.y"
{yyerror("falta llave de inicio de programa");yyerror("error en programa");}
break;
case 13:
//#line 28 "grama.y"
{yyerror("falta nombre del programa");yyerror("falta llave de cierre de programa");yyerror("error en programa");}
break;
case 14:
//#line 29 "grama.y"
{yyerror("falta nombre del programa");yyerror("error en programa");}
break;
case 15:
//#line 30 "grama.y"
{yyerror("no hay llaves del programa");yyerror("error en programa");}
break;
case 18:
//#line 35 "grama.y"
{yyerror("error en sentencia");}
break;
case 22:
//#line 45 "grama.y"
{addEstructura("declaracion de variable");}
break;
case 23:
//#line 46 "grama.y"
{addEstructura("declaracion de funcion");}
break;
case 24:
//#line 50 "grama.y"
{addEstructura("asignacion");}
break;
case 25:
//#line 51 "grama.y"
{addEstructura("sentencia if");}
break;
case 26:
//#line 52 "grama.y"
{addEstructura("print");}
break;
case 27:
//#line 53 "grama.y"
{addEstructura("return");}
break;
case 28:
//#line 54 "grama.y"
{addEstructura("asignacion multiple");}
break;
case 29:
//#line 58 "grama.y"
{addEstructura("sentencia do while");}
break;
case 30:
//#line 63 "grama.y"
{
        int t = crearTerceto(":=", val_peek(3).sval, val_peek(1).sval);
    }
break;
case 31:
//#line 71 "grama.y"
{
        int t = crearTerceto("+", val_peek(2).sval, val_peek(0).sval);

        yyval=new ParserVal("[" + t + "]");
    }
break;
case 32:
//#line 77 "grama.y"
{
        int t = crearTerceto("-", val_peek(2).sval, val_peek(0).sval);
        yyval=new ParserVal("[" + t + "]");
    }
break;
case 33:
//#line 81 "grama.y"
{yyerror("falta operando a izquierda de +");}
break;
case 34:
//#line 82 "grama.y"
{yyerror("falta operando a derecha de +");}
break;
case 35:
//#line 83 "grama.y"
{yyerror("falta operando a derecha de -");}
break;
case 36:
//#line 84 "grama.y"
{yyval=val_peek(0);}
break;
case 37:
//#line 85 "grama.y"
{addEstructura("trunc");}
break;
case 38:
//#line 86 "grama.y"
{addEstructura("trunc");yyerror("falta abrir parentesis en trunc");}
break;
case 39:
//#line 87 "grama.y"
{addEstructura("trunc");yyerror("falta cerrar parentesis en trunc");yyerrflag=0;}
break;
case 40:
//#line 88 "grama.y"
{addEstructura("trunc");yyerror("falta argumento en trunc");}
break;
case 41:
//#line 93 "grama.y"
{
        int t = crearTerceto("*", val_peek(2).sval, val_peek(0).sval);
        yyval=new ParserVal("[" + t + "]");
    }
break;
case 42:
//#line 98 "grama.y"
{
        int t = crearTerceto("/", val_peek(2).sval, val_peek(0).sval);
        yyval=new ParserVal("[" + t + "]");
    }
break;
case 43:
//#line 102 "grama.y"
{yyerror("falta operando a derecha de /");}
break;
case 44:
//#line 103 "grama.y"
{yyerror("falta operando a derecha de *");}
break;
case 45:
//#line 104 "grama.y"
{yyerror("falta operando a izquierda de /");}
break;
case 46:
//#line 105 "grama.y"
{yyerror("falta operando a izquierda de *");}
break;
case 47:
//#line 106 "grama.y"
{yyval=val_peek(0);}
break;
case 48:
//#line 110 "grama.y"
{yyval=val_peek(0);}
break;
case 49:
//#line 111 "grama.y"
{yyval=val_peek(0);}
break;
case 50:
//#line 112 "grama.y"
{yyval=val_peek(0);}
break;
case 51:
//#line 113 "grama.y"
{addEstructura("lambda"); yyval=val_peek(0);}
break;
case 52:
//#line 114 "grama.y"
{yyval=val_peek(0);}
break;
case 53:
//#line 118 "grama.y"
{check_rango(val_peek(0).sval);}
break;
case 54:
//#line 119 "grama.y"
{check_rango("-"+val_peek(0).sval);}
break;
case 55:
//#line 123 "grama.y"
{yyval= new ParserVal(val_peek(2).sval + '.' + val_peek(0).sval);}
break;
case 56:
//#line 124 "grama.y"
{yyval=val_peek(0);}
break;
case 57:
//#line 128 "grama.y"
{addEstructura("invocacion a funcion"); yyval=val_peek(3);}
break;
case 59:
//#line 133 "grama.y"
{yyerror("Falta parametro formal");}
break;
case 60:
//#line 134 "grama.y"
{yyerror("Falta flecha y parametro formal");}
break;
case 62:
//#line 136 "grama.y"
{yyerror("Falta parametro formal");}
break;
case 63:
//#line 137 "grama.y"
{yyerror("Falta flecha y parametro formal");}
break;
case 64:
//#line 138 "grama.y"
{yyerror("error en parametros de invocacion");}
break;
case 65:
//#line 143 "grama.y"
{
        int t = crearTerceto("print", val_peek(2).sval, "-");
        yyval=new ParserVal("[" + t + "]");
    }
break;
case 66:
//#line 147 "grama.y"
{yyerror("falta argumento en print");}
break;
case 68:
//#line 152 "grama.y"
{yyerror("falta ;");}
break;
case 71:
//#line 158 "grama.y"
{yyerror("argumento invalido en print");}
break;
case 72:
//#line 163 "grama.y"
{
        int t = crearTerceto("BF", val_peek(7).sval, "salto a ");
        yyval=new ParserVal("[" + t + "]");
    }
break;
case 73:
//#line 167 "grama.y"
{yyerror("no hay sentencias en else");}
break;
case 74:
//#line 169 "grama.y"
{
        int t = crearTerceto("BF", val_peek(4).sval, "salto a ");
        yyval=new ParserVal("[" + t + "]");
    }
break;
case 75:
//#line 174 "grama.y"
{ yyerror("falta endif");}
break;
case 76:
//#line 175 "grama.y"
{ yyerror("falta endif");yyerror("no hay sentencias en else");}
break;
case 77:
//#line 176 "grama.y"
{ yyerror("falta endif");}
break;
case 78:
//#line 179 "grama.y"
{yyerror("no hay sentencias en then");}
break;
case 79:
//#line 180 "grama.y"
{yyerror("no hay sentencias en then");yyerror("no hay sentencias en else");}
break;
case 80:
//#line 181 "grama.y"
{yyerror("no hay sentencias en then");}
break;
case 81:
//#line 184 "grama.y"
{yyerror("no hay sentencias en then");yyerror("falta endif");}
break;
case 82:
//#line 185 "grama.y"
{yyerror("no hay sentencias en then");yyerror("no hay sentencias en else");yyerror("falta endif");}
break;
case 83:
//#line 186 "grama.y"
{yyerror("no hay sentencias en then");yyerror("falta endif");}
break;
case 84:
//#line 191 "grama.y"
{
        int t = crearTerceto(val_peek(2).sval, val_peek(3).sval, val_peek(1).sval);
        yyval=new ParserVal("[" + t + "]");
    }
break;
case 85:
//#line 195 "grama.y"
{yyerror("falta cerrar parentesis");}
break;
case 86:
//#line 196 "grama.y"
{yyerror("falta abrir parentesis");}
break;
case 87:
//#line 197 "grama.y"
{yyerror("faltan parentesis");}
break;
case 88:
//#line 202 "grama.y"
{yyval=new ParserVal("==");}
break;
case 89:
//#line 203 "grama.y"
{yyval=new ParserVal("=!");}
break;
case 90:
//#line 204 "grama.y"
{yyval=new ParserVal("<=");}
break;
case 91:
//#line 205 "grama.y"
{yyval=new ParserVal(">=");}
break;
case 92:
//#line 206 "grama.y"
{yyval=new ParserVal("<");}
break;
case 93:
//#line 207 "grama.y"
{yyval=new ParserVal(">");}
break;
case 94:
//#line 208 "grama.y"
{yyerror("en condicion");}
break;
case 98:
//#line 223 "grama.y"
{TablaDeSimbolos.addAmbito(val_peek(0).sval, Compilador.getAmbito());}
break;
case 100:
//#line 225 "grama.y"
{yyerror("falta , en lista de variables");}
break;
case 101:
//#line 231 "grama.y"
{Compilador.entrarAmbito(val_peek(3).sval);}
break;
case 102:
//#line 231 "grama.y"
{Compilador.salirAmbito();TablaDeSimbolos.addAmbito(val_peek(7).sval, Compilador.getAmbito());}
break;
case 103:
//#line 232 "grama.y"
{ yyerror("falta declarar nombre de funcion");}
break;
case 104:
//#line 233 "grama.y"
{yyerror("faltan parametros formales");}
break;
case 105:
//#line 234 "grama.y"
{yyerror("falta declarar nombre de funcion"); yyerror("faltan parametros formales");}
break;
case 106:
//#line 238 "grama.y"
{TablaDeSimbolos.addAmbito(val_peek(0).sval, Compilador.getAmbito());}
break;
case 107:
//#line 239 "grama.y"
{TablaDeSimbolos.addAmbito(val_peek(0).sval, Compilador.getAmbito());}
break;
case 110:
//#line 242 "grama.y"
{yyerror("error en parametro formal");}
break;
case 116:
//#line 262 "grama.y"
{
        addEstructura("return");
        int t = crearTerceto("return", val_peek(2).sval, "-");
        yyval=new ParserVal("[" + t + "]");
        }
break;
case 119:
//#line 280 "grama.y"
{yyerror("falta cuerpo sentencias");}
break;
case 120:
//#line 281 "grama.y"
{yyerror("falta de ;");}
break;
case 121:
//#line 283 "grama.y"
{ yyerror("falta while");}
break;
case 122:
//#line 284 "grama.y"
{yyerror("falta cuerpo sentencias"); yyerror("falta while");}
break;
case 123:
//#line 285 "grama.y"
{yyerror("falta de ) en condicion");yyerror("falta de ;");yyerror("falta while");}
break;
case 124:
//#line 286 "grama.y"
{yyerror("falta de ) en condicion");yyerror("falta de ;");yyerror("falta cuerpo sentencias");yyerror("falta while");}
break;
case 125:
//#line 289 "grama.y"
{Compilador.entrarAmbito("ua"+cantUnidadesAnonimas); cantUnidadesAnonimas+= 1;}
break;
case 126:
//#line 292 "grama.y"
{Compilador.salirAmbito();}
break;
case 127:
//#line 293 "grama.y"
{yyerror("no hay sentencias dentro de las llaves");}
break;
case 128:
//#line 294 "grama.y"
{Compilador.salirAmbito();}
break;
case 129:
//#line 295 "grama.y"
{yyerror("Error en sentencia");}
break;
case 130:
//#line 300 "grama.y"
{yyval= new ParserVal("soy un lamnda");}
break;
case 131:
//#line 301 "grama.y"
{ yyerror("falta abrir llave en cuerpo de sentencia lambda");}
break;
case 132:
//#line 302 "grama.y"
{yyerror("falta cerra llave en cuerpo de sentencia lambda");}
break;
case 133:
//#line 303 "grama.y"
{ yyerror("faltan llaves en cuerpo de sentencia lambda");}
break;
case 136:
//#line 314 "grama.y"
{
        int t = crearTerceto("=", val_peek(3).sval, val_peek(1).sval);
        yyval=new ParserVal("[" + t + "]");
    }
break;
case 137:
//#line 321 "grama.y"
{yyval=val_peek(0);}
break;
case 138:
//#line 322 "grama.y"
{yyval=new ParserVal("{"+val_peek(2).sval + ", " + val_peek(0).sval+"}");}
break;
case 139:
//#line 323 "grama.y"
{yyerror("falta , en lista de variables");}
break;
case 140:
//#line 327 "grama.y"
{yyval=val_peek(0);}
break;
case 141:
//#line 328 "grama.y"
{yyval=new ParserVal("{"+val_peek(2).sval + ", " + val_peek(0).sval+"}");}
break;
case 142:
//#line 329 "grama.y"
{yyerror("falta , en lista de constantes");}
break;
//#line 1350 "Parser.java"
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
