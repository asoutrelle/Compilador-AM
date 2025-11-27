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
import java.util.HashSet;
import java.util.Set;
//#line 25 "Parser.java"




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
   20,   20,   20,   20,   20,   22,   22,   17,   17,   24,
   21,   25,   25,   25,   25,   25,   25,   25,   26,   10,
   10,   16,   16,   27,   27,   27,    9,    9,    9,    9,
    9,    9,    9,    9,    9,    9,    9,    9,   30,   32,
   31,   28,   28,   28,   28,   33,   33,   33,   33,   33,
   33,   33,   34,   34,   36,    7,    7,    7,    7,    7,
   37,   37,   35,   35,   35,   35,   35,   14,   38,   38,
   39,   39,   11,   13,   13,   13,   13,   13,   13,   13,
   40,   41,   42,   29,   29,   29,   29,   23,   23,   23,
   23,   44,   44,   43,   12,   45,   45,   45,   46,   46,
   46,
};
final static short yylen[] = {                            2,
    4,    3,    3,    3,    2,    3,    2,    4,    3,    3,
    3,    2,    2,    2,    1,    2,    2,    1,    1,    1,
    1,    1,    1,    1,    1,    1,    1,    1,    3,    1,
    3,    2,    4,    3,    3,    2,    3,    3,    1,    3,
    3,    3,    3,    2,    2,    1,    1,    1,    1,    4,
    3,    3,    3,    1,    1,    1,    2,    3,    1,    0,
    5,    3,    2,    1,    5,    4,    3,    1,    1,    5,
    4,    1,    1,    1,    1,    1,    9,    7,    6,    8,
    6,    5,    8,    5,    4,    6,    4,    3,    0,    0,
    0,    5,    4,    4,    3,    1,    1,    1,    1,    1,
    1,    1,    1,    2,    0,    7,    5,    5,    4,    5,
    3,    2,    3,    2,    5,    4,    1,    1,    1,    2,
    1,    1,    5,    8,    5,    5,    5,    4,    4,    3,
    0,    0,    0,    3,    2,    1,    3,    8,    7,    7,
    6,    3,    3,    1,    4,    1,    3,    2,    1,    3,
    2,
};
final static short yydefred[] = {                         0,
    0,    0,    0,    0,    0,    0,    0,    0,  131,    0,
  118,   13,    0,   15,   18,   19,   20,   21,   22,   23,
   24,   25,   26,   27,   28,    0,    0,    0,    0,    0,
    0,    0,    0,   17,    0,    0,    0,    0,    0,    0,
   49,   56,    0,    0,    0,    0,    0,    0,   47,    0,
    0,   46,   48,   55,   54,    0,    6,   16,    0,    0,
    0,    0,    0,    0,    0,  148,   11,    0,    0,    4,
    9,    2,  117,    0,    0,    0,   58,   76,   75,    0,
    0,    0,    0,    0,    0,    0,  136,    0,    0,    0,
    0,    0,    0,    0,   57,   45,   44,    0,    0,  102,
   96,   97,   98,   99,    0,    0,  100,  101,    0,    0,
    0,   73,    0,    0,   72,   88,    0,    0,    0,    0,
   32,    0,   29,    0,  147,  149,    0,    8,    1,    0,
  114,    0,    0,   71,    0,    0,    0,    0,  135,  103,
    0,  128,    0,    0,    0,   52,   53,    0,   51,  144,
    0,    0,   37,    0,   38,    0,    0,   43,   40,   42,
   41,    0,   87,    0,   85,    0,    0,    0,    0,    0,
  109,    0,   31,   33,  151,    0,  145,  113,    0,    0,
  110,   70,  123,  125,  137,  134,  104,    0,  127,   68,
    0,    0,    0,   50,    0,    0,   94,   84,    0,    0,
    0,   82,  108,    0,  112,  122,  121,    0,  119,  107,
  150,    0,  116,  133,    0,   61,    0,    0,    0,   92,
   86,    0,    0,   81,    0,   79,    0,  111,  120,  115,
    0,    0,   62,    0,    0,    0,  141,    0,   78,    0,
  106,  124,    0,    0,  140,  139,    0,    0,   83,    0,
   80,   65,  138,  142,  143,   77,
};
final static short yydgoto[] = {                          4,
   13,   14,   15,   16,   17,   18,   19,   20,   21,   22,
   23,   24,   25,   26,   61,  116,   27,   50,   51,   52,
   53,   54,   55,   91,  192,  193,   83,   56,   90,  166,
  164,  167,  109,  141,   76,  169,  171,  208,  209,   39,
  214,  231,  151,  237,   28,  127,
};
final static short yysindex[] = {                       -70,
  377,  375,  561,    0,  -43,   62,  -15,   11,    0,  396,
    0,    0,  475,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,  -38, -189,  -29,   -9,  576,
  510,   -3,  524,    0,  -77, -163,  -12,  413,  -36,   52,
    0,    0,   32,  442, -134,  283,  283,  138,    0,  348,
   29,    0,    0,    0,    0,  288,    0,    0,  123,   58,
   21,  413,   52, -127,  -91,    0,    0,   16,  534,    0,
    0,    0,    0, -100,  -80,  -17,    0,    0,    0, -100,
  -45,   97,  148,  128,  396,  -66,    0, -189,  -45,  192,
  153,   56,  171,   29,    0,    0,    0,  -32,  348,    0,
    0,    0,    0,    0,  428,  436,    0,    0,  413,  260,
  266,    0,   -2,  -45,    0,    0,    0,  173,  113,   -4,
    0,   -1,    0,   -7,    0,    0,  -41,    0,    0,   24,
    0, -157,  113,    0,  -45,  -45,  -45,  162,    0,    0,
  244,    0,  396,  -45,  208,    0,    0,  247,    0,    0,
  286,  413,    0,   29,    0,   29,  163,    0,    0,    0,
    0,  -45,    0, -118,    0,   77,   90,  113,  -77,  321,
    0,  113,    0,    0,    0,   94,    0,    0, -100,  103,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
   97,   14,   74,    0, -103,  392,    0,    0,  -45,  201,
  -45,    0,    0,   40,    0,    0,    0,  544,    0,    0,
    0,  104,    0,    0,  413,    0,  130,  -62,  296,    0,
    0,  109,  -45,    0, -118,    0,  113,    0,    0,    0,
  -45,  120,    0,  305,  376,  -28,    0,  -45,    0,  265,
    0,    0,  160,  376,    0,    0,  384,  388,    0,  -45,
    0,    0,    0,    0,    0,    0,
};
final static short yyrindex[] = {                         0,
    0,    0,    0,    0,    0,  -44,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,   42,    0,  444,    0,
  447,  448,  452,    0,    0,    0,    0,    0,    0,    1,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
   23,    0,    0,    0,    0,    0,    0,    0,   45,    0,
    0,    0,  -44,    0,    0,    0,    0,  461,  463,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,  433,    0,    0,    0,    0,    0,  450,  157,    0,
    0,    0,    0,   47,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0, -101,    0,    0,    0,  -51,  126,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,  165,    0,    0,    0,    0,    0,    0,
    0,    0,    0,   69,    0,   93,  115,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,  117,    0,    0,
  -31,    0,   79,    0,    0,  143,    0,    0,  216, -101,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,   91,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,  106,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,  401,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,
};
final static short yygindex[] = {                         0,
  355,  151,  368,  -97,    0,    0,    0,    0,    0,    0,
    0,    0,    0,  679,    0,  621,  619,  551,  -10,  425,
    0,  -22,    0,    0,    0,  264,    0,   15,  -37,    0,
  280,  287,  389, -169,  -48,    0,  -94,    0,  282,    0,
    0,    0,    0,  154,    0,    0,
};
final static int YYTABLESIZE=871;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                         59,
   59,   60,  176,   48,   86,   46,   44,   90,   45,   69,
   47,  120,   69,  115,   64,   34,   59,  115,  117,  218,
   93,   91,   39,  133,   37,  219,  132,   80,   81,   46,
   44,   65,   45,   94,   47,  105,  172,  106,  181,  132,
   60,   59,   59,   59,   59,   59,   36,   59,  234,   34,
   38,  115,    3,   89,  216,   34,  115,  215,  139,   59,
   59,   59,   59,   39,  122,   39,   39,   39,   34,  148,
  110,   92,  207,  203,   34,  111,   45,  210,   62,  115,
  227,   39,   39,  132,   39,  146,   86,   36,   30,   36,
   36,   36,   35,   77,  154,  156,  147,   36,  119,  137,
   45,   35,  146,   30,  144,   36,   36,   36,   36,   34,
  207,   34,   34,   34,   95,   67,  126,   11,  179,   64,
  204,   71,   64,   59,   95,   59,  199,   34,   34,   63,
   34,   63,  241,   35,   63,   35,   35,   35,   63,  105,
  128,  106,   93,    7,    8,   39,   67,   39,  115,   67,
   10,   35,   35,   63,   35,   91,  130,  188,    7,    8,
   91,   91,  118,   58,  129,   10,  126,   91,  136,   36,
  105,   36,  106,   95,   11,  132,  131,   80,   73,   46,
   44,   58,   45,   58,   47,    1,    2,  240,  135,  138,
   63,   34,  145,   34,   63,    7,    8,   11,   74,    7,
    8,   93,   10,  197,   90,  105,   10,  106,   89,   90,
  112,  149,   59,  168,  112,   35,  175,   35,   59,   58,
   40,   41,   42,   59,  150,    7,    8,   63,  247,  248,
   85,   48,   10,   46,   44,  170,   45,   95,   47,   95,
   43,  126,   69,   78,   40,   41,   42,   80,  112,   46,
   44,   79,   45,  112,   47,  173,   59,   59,  162,  115,
   59,   59,   59,   59,   43,   93,   59,   93,   59,   59,
   59,   59,   59,   59,   59,   59,  112,  121,   39,   39,
  178,  130,   39,   39,   39,   39,  185,  194,   39,  129,
   42,   39,   39,   39,   39,   39,   39,   39,  146,   80,
   30,   30,   36,   36,   45,   80,   36,   36,   36,   36,
   45,  146,   36,   73,   42,   36,   36,   36,   36,   36,
   36,   36,   80,  115,   34,   34,  195,   45,   34,   34,
   34,   34,   11,   74,   34,  236,  200,   34,   34,   34,
   34,   34,   34,   34,  236,  112,  115,  217,   35,   35,
  201,  211,   35,   35,   35,   35,   31,   33,   35,  213,
  230,   35,   35,   35,   35,   35,   35,   35,  186,  238,
   95,   95,  132,  126,   95,   95,   95,   95,  126,  126,
   95,  105,  126,   95,   69,  126,  233,  245,  246,   95,
  105,  126,  106,  243,   40,   41,   42,  253,   93,   93,
  105,  105,   93,   93,   93,   93,   87,  107,   93,  108,
   86,   93,   11,  130,   43,  236,  252,   93,  130,  130,
  235,  129,  130,   87,  254,  130,  129,  129,  255,  244,
  129,  130,  220,  129,  105,   48,  106,   46,   44,  129,
   45,   66,   47,   14,   66,  205,    7,   12,   40,   41,
   42,    5,   80,  140,   46,   44,  112,   45,  143,   47,
   10,  223,    3,  190,   40,   41,   42,   80,   43,   46,
   96,   97,   45,   74,   47,   80,   90,   46,  232,  225,
   45,   80,   47,   46,   43,  222,   45,  152,   47,  229,
    0,   47,   47,  146,   47,    0,   47,   30,    0,    0,
   63,   12,    0,    0,    0,    7,    8,    0,  187,   47,
  146,   47,   10,    0,    0,  158,   40,   41,   42,    0,
  112,  160,   40,   41,   42,  250,    0,    0,    0,    0,
    0,   87,    0,    0,  159,  161,   43,  206,    0,   40,
   41,   42,   43,  112,   63,    0,    0,  113,  114,    7,
    8,    0,   63,    0,    0,    0,   10,    7,    8,   43,
    0,   63,  140,    0,   10,    0,    7,    8,    0,    0,
    0,    0,    0,   10,    0,  206,    0,    6,    0,    0,
    0,    0,    7,    8,    0,  140,  187,   82,   84,   10,
    0,    0,   87,    0,    0,   11,    0,    0,   99,   57,
    0,  187,    0,  100,    0,    0,    0,    0,    0,    0,
    0,    0,  124,    0,    0,    0,    0,  101,  102,  103,
  104,    0,    0,    0,    0,    0,    0,    0,   49,    0,
   29,    6,    5,    6,   70,    0,    7,    8,    7,    8,
    9,    0,    9,   10,    0,   10,   66,    0,   72,   11,
    0,   11,   40,   41,   42,   49,   49,   88,  129,  157,
    0,    0,   49,    0,   49,   49,   49,    0,  228,   40,
   41,   42,   43,    0,    0,    0,    0,    0,    0,    0,
   49,  123,  125,  153,   40,   41,   42,    0,    0,   43,
    0,  155,   40,   41,   42,  191,    0,    0,   40,   41,
   42,  134,  196,   49,   43,   47,  146,    0,   49,  142,
    0,    0,   43,   75,    0,    0,    0,    0,   43,   47,
   47,   47,   47,   49,   49,    0,   98,   49,   49,   49,
    0,    6,    0,  163,  165,    0,    7,    8,   75,    0,
    9,    0,    0,   10,  174,    0,    0,  177,    0,   11,
    0,    0,  130,    0,    0,  182,  183,  184,   98,    0,
    0,   49,    0,   49,  189,  191,    6,    0,    0,    0,
   49,    7,    8,    0,    0,    9,    0,    0,   10,    0,
    6,    0,  198,    0,   11,    7,    8,  202,    0,    9,
    6,    0,   10,    0,    0,    7,    8,    0,   11,    9,
    6,    0,   10,    0,    0,    7,    8,    0,   11,    0,
  180,    0,   10,    0,    0,    0,   32,    6,   11,  221,
  224,  226,    7,    8,    0,    0,    9,    0,    0,   10,
    0,   68,    6,   49,    0,   11,    0,    7,    8,    0,
    0,    9,    0,  239,   10,    0,    0,   75,    0,    0,
   11,  242,    0,    0,    0,    0,    0,  212,  249,    0,
  251,    0,    0,    0,    0,    0,    0,    0,    0,    0,
  256,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                         44,
    0,   40,   44,   40,  123,   42,   43,   59,   45,   41,
   47,   60,   44,   59,   44,   59,   61,   59,   56,  123,
   43,  123,    0,   41,   40,  195,   44,   40,   41,   42,
   43,   61,   45,   44,   47,   43,   41,   45,  133,   44,
   40,   41,   42,   43,   44,   45,    0,   47,  218,   59,
   40,   59,  123,   39,   41,   59,   59,   44,  125,   59,
   60,   61,   62,   41,   44,   43,   44,   45,    0,   92,
   42,   40,  170,  168,   59,   47,   45,  172,  268,   59,
   41,   59,   60,   44,   62,   44,  123,   41,   44,   43,
   44,   45,    0,  257,  105,  106,   41,   46,   41,   85,
   45,   40,   61,   59,   90,   59,   60,   46,   62,   41,
  208,   43,   44,   45,    0,  125,    0,  275,  276,   41,
  169,  125,   44,  123,  259,  125,  164,   59,   60,  257,
   62,   41,  227,   41,   44,   43,   44,   45,  257,   43,
  125,   45,    0,  262,  263,  123,   41,  125,   59,   44,
  269,   59,   60,  257,   62,  257,    0,  143,  262,  263,
  262,  263,   40,   13,    0,  269,  258,  269,   41,  123,
   43,  125,   45,   59,  275,   59,  257,   40,  256,   42,
   43,   31,   45,   33,   47,  256,  257,  225,   41,  256,
  257,  123,   40,  125,  257,  262,  263,  275,  276,  262,
  263,   59,  269,   41,  256,   43,  269,   45,  260,  261,
  256,   41,  257,   41,  256,  123,  258,  125,  257,   69,
  257,  258,  259,  268,  257,  262,  263,  257,  257,  258,
  267,   40,  269,   42,   43,  123,   45,  123,   47,  125,
  277,  125,  274,  256,  257,  258,  259,   40,  256,   42,
   43,  264,   45,  256,   47,  257,  256,  257,  261,   59,
  260,  261,  262,  263,  277,  123,  266,  125,  268,  269,
  270,  271,  272,  273,  274,  275,  256,  257,  256,  257,
  257,  125,  260,  261,  262,  263,  125,   41,  266,  125,
  259,  269,  270,  271,  272,  273,  274,  275,  257,   40,
  256,  257,  256,  257,   45,   40,  260,  261,  262,  263,
   45,  256,  266,  256,  259,  269,  270,  271,  272,  273,
  274,  275,   40,   59,  256,  257,   41,   45,  260,  261,
  262,  263,  275,  276,  266,   40,  260,  269,  270,  271,
  272,  273,  274,  275,   40,  256,   59,  274,  256,  257,
  261,  258,  260,  261,  262,  263,    2,    3,  266,  257,
  257,  269,  270,  271,  272,  273,  274,  275,  125,  261,
  256,  257,  256,  257,  260,  261,  262,  263,  262,  263,
  266,  256,  266,  269,   30,  269,  257,  234,  235,  275,
   43,  275,   45,  274,  257,  258,  259,  244,  256,  257,
  275,  276,  260,  261,  262,  263,   39,   60,  266,   62,
  123,  269,  275,  257,  277,   40,  257,  275,  262,  263,
  125,  257,  266,   56,   41,  269,  262,  263,   41,  125,
  266,  275,   41,  269,   43,   40,   45,   42,   43,  275,
   45,   41,   47,    0,   44,  125,    0,    0,  257,  258,
  259,    0,   40,   86,   42,   43,  256,   45,  267,   47,
    0,  261,    0,  256,  257,  258,  259,   40,  277,   42,
   46,   47,   45,   41,   47,   40,  261,   42,  215,  200,
   45,   40,   47,   42,  277,  199,   45,   99,   47,  208,
   -1,   42,   43,   44,   45,   -1,   47,  123,   -1,   -1,
  257,  125,   -1,   -1,   -1,  262,  263,   -1,  141,   60,
   61,   62,  269,   -1,   -1,  256,  257,  258,  259,   -1,
  256,  256,  257,  258,  259,  261,   -1,   -1,   -1,   -1,
   -1,  164,   -1,   -1,  110,  111,  277,  170,   -1,  257,
  258,  259,  277,  256,  257,   -1,   -1,  260,  261,  262,
  263,   -1,  257,   -1,   -1,   -1,  269,  262,  263,  277,
   -1,  257,  195,   -1,  269,   -1,  262,  263,   -1,   -1,
   -1,   -1,   -1,  269,   -1,  208,   -1,  257,   -1,   -1,
   -1,   -1,  262,  263,   -1,  218,  219,   37,   38,  269,
   -1,   -1,  225,   -1,   -1,  275,   -1,   -1,   48,  125,
   -1,  234,   -1,  256,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   62,   -1,   -1,   -1,   -1,  270,  271,  272,
  273,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   10,   -1,
  256,  257,  256,  257,  125,   -1,  262,  263,  262,  263,
  266,   -1,  266,  269,   -1,  269,   28,   -1,  125,  275,
   -1,  275,  257,  258,  259,   37,   38,   39,  125,  109,
   -1,   -1,   44,   -1,   46,   47,   48,   -1,  125,  257,
  258,  259,  277,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   62,   61,   64,  256,  257,  258,  259,   -1,   -1,  277,
   -1,  256,  257,  258,  259,  145,   -1,   -1,  257,  258,
  259,   81,  152,   85,  277,  256,  257,   -1,   90,   89,
   -1,   -1,  277,   35,   -1,   -1,   -1,   -1,  277,  270,
  271,  272,  273,  105,  106,   -1,   48,  109,  110,  111,
   -1,  257,   -1,  113,  114,   -1,  262,  263,   60,   -1,
  266,   -1,   -1,  269,  124,   -1,   -1,  127,   -1,  275,
   -1,   -1,   74,   -1,   -1,  135,  136,  137,   80,   -1,
   -1,  143,   -1,  145,  144,  215,  257,   -1,   -1,   -1,
  152,  262,  263,   -1,   -1,  266,   -1,   -1,  269,   -1,
  257,   -1,  162,   -1,  275,  262,  263,  167,   -1,  266,
  257,   -1,  269,   -1,   -1,  262,  263,   -1,  275,  266,
  257,   -1,  269,   -1,   -1,  262,  263,   -1,  275,   -1,
  132,   -1,  269,   -1,   -1,   -1,  256,  257,  275,  199,
  200,  201,  262,  263,   -1,   -1,  266,   -1,   -1,  269,
   -1,  256,  257,  215,   -1,  275,   -1,  262,  263,   -1,
   -1,  266,   -1,  223,  269,   -1,   -1,  169,   -1,   -1,
  275,  231,   -1,   -1,   -1,   -1,   -1,  179,  238,   -1,
  240,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
  250,
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
"factor : TRUNC '(' punto_flotante ')'",
"factor : TRUNC punto_flotante ')'",
"factor : TRUNC '(' error",
"factor : TRUNC '(' ')'",
"factor : exp_lambda",
"factor : punto_flotante",
"punto_flotante : PF64",
"punto_flotante : '-' PF64",
"variable : ID '.' ID",
"variable : ID",
"$$1 :",
"invocacion : ID $$1 '(' parametros_de_invocacion ')'",
"parametros_de_invocacion : parametro_real FLECHA ID",
"parametros_de_invocacion : parametro_real FLECHA",
"parametros_de_invocacion : parametro_real",
"parametros_de_invocacion : parametros_de_invocacion ',' parametro_real FLECHA ID",
"parametros_de_invocacion : parametros_de_invocacion ',' parametro_real FLECHA",
"parametros_de_invocacion : parametros_de_invocacion ',' parametro_real",
"parametros_de_invocacion : error",
"parametro_real : exp",
"salida_msj : PRINT '(' argumento_print ')' punto_coma",
"salida_msj : PRINT '(' ')' punto_coma",
"punto_coma : ';'",
"punto_coma : error",
"argumento_print : exp",
"argumento_print : CADENA",
"argumento_print : error",
"if : IF cuerpo_condicion cuerpo_sentencia_ejecutable completar_bf_else ELSE crear_bi cuerpo_sentencia_ejecutable ENDIF punto_coma",
"if : IF cuerpo_condicion cuerpo_sentencia_ejecutable completar_bf_else ELSE ENDIF punto_coma",
"if : IF cuerpo_condicion cuerpo_sentencia_ejecutable completar_bf ENDIF punto_coma",
"if : IF cuerpo_condicion cuerpo_sentencia_ejecutable completar_bf_else ELSE crear_bi cuerpo_sentencia_ejecutable punto_coma",
"if : IF cuerpo_condicion cuerpo_sentencia_ejecutable completar_bf_else ELSE punto_coma",
"if : IF cuerpo_condicion cuerpo_sentencia_ejecutable completar_bf punto_coma",
"if : IF cuerpo_condicion ELSE crear_bi cuerpo_sentencia_ejecutable completar_bf ENDIF punto_coma",
"if : IF cuerpo_condicion ELSE ENDIF punto_coma",
"if : IF cuerpo_condicion ENDIF punto_coma",
"if : IF cuerpo_condicion ELSE crear_bi cuerpo_sentencia_ejecutable punto_coma",
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
"$$2 :",
"funcion : tipo ID '(' $$2 parametros_formales ')' lista_sentencia_funcion_aux",
"funcion : tipo '(' parametros_formales ')' lista_sentencia_funcion_aux",
"funcion : tipo ID '(' ')' lista_sentencia_funcion_aux",
"funcion : tipo '(' ')' lista_sentencia_funcion_aux",
"funcion : ID '(' parametros_formales ')' lista_sentencia_funcion_aux",
"lista_sentencia_funcion_aux : '{' lista_sentencia_funcion '}'",
"lista_sentencia_funcion_aux : '{' '}'",
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
"do : DO inicio_while cuerpo_sentencia_ejecutable WHILE cuerpo_condicion crear_bi_while completar_bf_while punto_coma",
"do : DO inicio_while WHILE cuerpo_condicion punto_coma",
"do : DO inicio_while cuerpo_sentencia_ejecutable WHILE cuerpo_condicion",
"do : DO inicio_while cuerpo_sentencia_ejecutable cuerpo_condicion punto_coma",
"do : DO inicio_while cuerpo_condicion punto_coma",
"do : DO inicio_while cuerpo_sentencia_ejecutable cuerpo_condicion",
"do : DO inicio_while cuerpo_condicion",
"inicio_while :",
"crear_bi_while :",
"completar_bf_while :",
"cuerpo_sentencia_ejecutable : '{' lista_sentencia_ejecutable '}'",
"cuerpo_sentencia_ejecutable : '{' '}'",
"cuerpo_sentencia_ejecutable : sentencia_ejecutable",
"cuerpo_sentencia_ejecutable : '{' error '}'",
"exp_lambda : '(' tipo validar_id ')' '{' lista_sentencia_ejecutable '}' argumento_lambda",
"exp_lambda : '(' tipo validar_id ')' lista_sentencia_ejecutable '}' argumento_lambda",
"exp_lambda : '(' tipo validar_id ')' '{' lista_sentencia_ejecutable argumento_lambda",
"exp_lambda : '(' tipo validar_id ')' lista_sentencia_ejecutable argumento_lambda",
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

//#line 596 "grama.y"
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
    Compilador.tercetos.add(new Terceto(operacion, variable, valor, Compilador.tercetos.size()));
  }
  private void crearTerceto(String operacion, String variable, String valor, boolean marca){
     Compilador.tercetos.add(new Terceto(operacion, variable, valor, Compilador.tercetos.size(), marca));
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
        t.setValor2("[" + destino + "]");
        t.setMarcado(true);
    }

private void completarBI(int index) {
    Terceto t = Compilador.tercetos.get(index);
    int val1 =Compilador.tercetos.size();
    t.setValor1("[" + val1 + "]");
    t.setValor2("[" + (index+1) + "]");
    t.setMarcado(true);
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
        System.out.println("trunc: "+ valorStr);
        return valorStr;
  }
  private void backpatching(String func,int index){
          for (Terceto terceto : Compilador.tercetos) {
              if(terceto.getVal2().equals(func)){
                  terceto.setValor2("["+index+"]");
              }
          }
      }
//#line 770 "Parser.java"
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
//#line 19 "grama.y"
{
        Compilador.salirAmbito();
        String ambito = Compilador.getAmbito();
        String var = val_peek(3).sval;
        if(TablaDeSimbolos.agregarVar(var, ambito, "", "nombre de programa")){
           yyval = new ParserVal(var+ambito);
        } else yyerror("La variable "+var+" ya fue declarada");
    }
break;
case 2:
//#line 27 "grama.y"
{yyerror("falta nombre de programa");}
break;
case 3:
//#line 28 "grama.y"
{yyerror("falta llave de cierre de programa");}
break;
case 4:
//#line 29 "grama.y"
{yyerror("falta llave de inicio de programa");}
break;
case 5:
//#line 30 "grama.y"
{yyerror("falta nombre del programa");yyerror("falta llave de cierre de programa");}
break;
case 6:
//#line 31 "grama.y"
{yyerror("falta nombre del programa");yyerror("falta llave de inicio de programa");}
break;
case 7:
//#line 32 "grama.y"
{yyerror("no hay llaves del programa");}
break;
case 8:
//#line 34 "grama.y"
{yyerror("error en programa");}
break;
case 9:
//#line 35 "grama.y"
{yyerror("falta nombre de programa");yyerror("error en programa");}
break;
case 10:
//#line 36 "grama.y"
{yyerror("falta llave de cierre de programa");yyerror("error en programa");}
break;
case 11:
//#line 37 "grama.y"
{yyerror("falta llave de inicio de programa");yyerror("error en programa");}
break;
case 12:
//#line 38 "grama.y"
{yyerror("falta nombre del programa");yyerror("falta llave de cierre de programa");yyerror("error en programa");}
break;
case 13:
//#line 39 "grama.y"
{yyerror("falta nombre del programa");yyerror("error en programa");}
break;
case 14:
//#line 40 "grama.y"
{yyerror("no hay llaves del programa");yyerror("error en programa");}
break;
case 17:
//#line 45 "grama.y"
{yyerror("error en sentencia");}
break;
case 21:
//#line 55 "grama.y"
{addEstructura("declaracion de variable");}
break;
case 22:
//#line 56 "grama.y"
{addEstructura("declaracion de funcion");}
break;
case 23:
//#line 60 "grama.y"
{addEstructura("asignacion");}
break;
case 24:
//#line 62 "grama.y"
{
        addEstructura("sentencia if");
        crearTerceto("fin if","-","-");
    }
break;
case 25:
//#line 66 "grama.y"
{addEstructura("print");}
break;
case 26:
//#line 67 "grama.y"
{addEstructura("return");}
break;
case 27:
//#line 68 "grama.y"
{addEstructura("asignacion multiple");}
break;
case 28:
//#line 72 "grama.y"
{addEstructura("sentencia do while");}
break;
case 30:
//#line 82 "grama.y"
{
        String ambito = Compilador.getAmbito();
        String var = val_peek(0).sval;
        if(TablaDeSimbolos.agregarVar(var, ambito, tipo, "nombre de variable")){
           yyval = new ParserVal(var+ambito);
        } else yyerror("La variable "+var+" ya fue declarada");
    }
break;
case 31:
//#line 90 "grama.y"
{
        String ambito = Compilador.getAmbito();
        String var = val_peek(0).sval;
        if(TablaDeSimbolos.agregarVar(var, ambito, tipo, "nombre de variable")){
           yyval = new ParserVal(val_peek(2).sval+","+var);
        } else yyerror("La variable "+var+" ya fue declarada");
    }
break;
case 32:
//#line 97 "grama.y"
{yyerror("falta , en lista de variables");}
break;
case 33:
//#line 102 "grama.y"
{
        System.out.println(val_peek(3).sval+", "+val_peek(1).sval);
        if(!TablaDeSimbolos.esCompatible(val_peek(3).sval,val_peek(1).sval)){
            yyerror("Los tipos de las variables no coinciden");
        }

        String ambito = Compilador.getAmbito();
        String var = val_peek(3).sval;
        crearTerceto(":=", var, val_peek(1).sval);
    }
break;
case 34:
//#line 116 "grama.y"
{
        crearTerceto("+", val_peek(2).sval, val_peek(0).sval);
        int t = Compilador.tercetos.size() - 1;
        yyval=new ParserVal("[" + t + "]");
        TablaDeSimbolos.agregarVarAux(t);
    }
break;
case 35:
//#line 123 "grama.y"
{
        crearTerceto("-", val_peek(2).sval, val_peek(0).sval);
        int t = Compilador.tercetos.size() - 1;
        yyval=new ParserVal("[" + t + "]");
        TablaDeSimbolos.agregarVarAux(t);
    }
break;
case 36:
//#line 129 "grama.y"
{yyerror("falta operando a izquierda de +");}
break;
case 37:
//#line 130 "grama.y"
{yyerror("falta operando a derecha de +");}
break;
case 38:
//#line 131 "grama.y"
{yyerror("falta operando a derecha de -");}
break;
case 40:
//#line 137 "grama.y"
{
        crearTerceto("*", val_peek(2).sval, val_peek(0).sval);
        int t = Compilador.tercetos.size() - 1;
        yyval=new ParserVal("[" + t + "]");
        TablaDeSimbolos.agregarVarAux(t);
    }
break;
case 41:
//#line 144 "grama.y"
{
        crearTerceto("/", val_peek(2).sval, val_peek(0).sval);
        int t = Compilador.tercetos.size() - 1;
        yyval=new ParserVal("[" + t + "]");
        TablaDeSimbolos.agregarVarAux(t);
    }
break;
case 42:
//#line 150 "grama.y"
{yyerror("falta operando a derecha de /");}
break;
case 43:
//#line 151 "grama.y"
{yyerror("falta operando a derecha de *");}
break;
case 44:
//#line 152 "grama.y"
{yyerror("falta operando a izquierda de /");}
break;
case 45:
//#line 153 "grama.y"
{yyerror("falta operando a izquierda de *");}
break;
case 49:
//#line 160 "grama.y"
{yyval=new ParserVal(val_peek(0).sval);}
break;
case 50:
//#line 162 "grama.y"
{
        addEstructura("trunc");
        crearTerceto("trunc",val_peek(1).sval,"-");
        int t = Compilador.tercetos.size() - 1;
        TablaDeSimbolos.agregarVarAux(t);
        yyval = new ParserVal("["+t+"]");
    }
break;
case 51:
//#line 169 "grama.y"
{addEstructura("trunc");yyerror("falta abrir parentesis en trunc");}
break;
case 52:
//#line 170 "grama.y"
{addEstructura("trunc");yyerror("falta cerrar parentesis en trunc");yyerrflag=0;}
break;
case 53:
//#line 171 "grama.y"
{addEstructura("trunc");yyerror("falta argumento en trunc");}
break;
case 54:
//#line 172 "grama.y"
{addEstructura("lambda");}
break;
case 55:
//#line 173 "grama.y"
{yyerror("no se permiten puntos flotantes");}
break;
case 56:
//#line 177 "grama.y"
{check_rango(val_peek(0).sval); yyval=new ParserVal(val_peek(0).sval);}
break;
case 57:
//#line 178 "grama.y"
{check_rango("-"+val_peek(0).sval); yyval=new ParserVal("-"+val_peek(1).sval);}
break;
case 58:
//#line 183 "grama.y"
{
        String aux = TablaDeSimbolos.varPrefijadaDeclarada(val_peek(0).sval, val_peek(2).sval, Compilador.getAmbito());
        if(aux.equals("")){
            yyerror("La variable "+val_peek(0).sval+" no fue declarada en el ambito " + val_peek(2).sval);
            TablaDeSimbolos.eliminar(val_peek(2).sval);
            TablaDeSimbolos.eliminar(val_peek(0).sval);
        }
        TablaDeSimbolos.eliminar(val_peek(0).sval);
        yyval = new ParserVal(aux);
    }
break;
case 59:
//#line 194 "grama.y"
{
        String ambito = Compilador.getAmbito();
        String var = val_peek(0).sval;
        String aux = TablaDeSimbolos.varDeclarada(var, ambito);
        if(aux == null){
            yyerror("La variable "+ var +" no fue declarada");
        } else {
            TablaDeSimbolos.eliminar(var);
            yyval=new ParserVal (aux);
        }
    }
break;
case 60:
//#line 209 "grama.y"
{
        nombreFuncion =val_peek(0).sval;
    }
break;
case 61:
//#line 213 "grama.y"
{
        String ambito = Compilador.getAmbito();
        String var = val_peek(4).sval;
        String aux = TablaDeSimbolos.funcionDeclarada(var,ambito);
        if(aux == null){
            yyerror("La funcion " + var + " no esta declarada");
        } else{
            TablaDeSimbolos.eliminar(var);
            addEstructura("invocacion a funcion");
        }
        String[] param = val_peek(1).sval.split(",");
        Set<String> set = new HashSet<>();
        for (String p : param) {
            if (!set.add(p)) {
                yyerror("Parametro duplicado");
            }
        }
        int cantParam = param.length;
        if(TablaDeSimbolos.cantParametrosFormales(ambito.substring(1)+":"+var)!=cantParam){
            yyerror("cantidad de parametros reales distinta a parametros formales");
        }
        int t = TablaDeSimbolos.buscarReturn(var+ambito);
        yyval = new ParserVal("["+t+"]");
        crearTerceto("invocacion",aux,"["+t+"]");
        Compilador.tercetos.addAll(Compilador.tercetosCR);
        backpatching(aux,t);
    }
break;
case 62:
//#line 244 "grama.y"
{
        String ambito = Compilador.getAmbito();
        String var = val_peek(0).sval;

        if (!TablaDeSimbolos.parametroDeclarado(var, ambito, nombreFuncion)){
            yyerror("El parametro " + var + " no esta declarado en la funcion "+nombreFuncion);
        }
        if (!TablaDeSimbolos.esParametroValido(val_peek(2).sval, var,Compilador.getAmbito(), nombreFuncion)){
            yyerror("No se puede pasar una constante como a un parametro formal con semantica de copia resultado");
        }
        ambito = Compilador.getAmbito()+":"+nombreFuncion;
        if (!TablaDeSimbolos.esCopiaResultado(var+ambito)){
            crearTerceto(":=", var+ambito,val_peek(2).sval);
        } else{
            Compilador.tercetosCR.add(new Terceto (":=",val_peek(2).sval, var+ambito));
        }
        TablaDeSimbolos.eliminar(var);
        yyval=val_peek(0);
    }
break;
case 63:
//#line 263 "grama.y"
{yyerror("Falta parametro formal");}
break;
case 64:
//#line 264 "grama.y"
{yyerror("Falta flecha y parametro formal");}
break;
case 65:
//#line 266 "grama.y"
{
        String ambito = Compilador.getAmbito();
        String var = val_peek(0).sval;
        if (!TablaDeSimbolos.parametroDeclarado(var, ambito, nombreFuncion)){
            yyerror("El parametro " + var + " no esta declarado en la funcion "+nombreFuncion);
        }
        ambito = Compilador.getAmbito()+":"+nombreFuncion;
         if (!TablaDeSimbolos.esCopiaResultado(var+ambito)){
            crearTerceto(":=", var+ambito,val_peek(2).sval);
        } else{
            Compilador.tercetosCR.add(new Terceto (":=", val_peek(2).sval,var+ambito));
        }
        TablaDeSimbolos.eliminar(var);
        yyval=new ParserVal(val_peek(4).sval + "," + val_peek(0).sval);
    }
break;
case 66:
//#line 281 "grama.y"
{yyerror("Falta parametro formal");}
break;
case 67:
//#line 282 "grama.y"
{yyerror("Falta flecha y parametro formal");}
break;
case 68:
//#line 283 "grama.y"
{yyerror("error en parametros de invocacion");}
break;
case 70:
//#line 292 "grama.y"
{
        crearTerceto("print", val_peek(2).sval, "-");
        int t = Compilador.tercetos.size() - 1;
        yyval=new ParserVal("[" + t + "]");
    }
break;
case 71:
//#line 297 "grama.y"
{yyerror("falta argumento en print");}
break;
case 73:
//#line 302 "grama.y"
{yyerror("falta ;");}
break;
case 76:
//#line 308 "grama.y"
{yyerror("argumento invalido en print");}
break;
case 77:
//#line 314 "grama.y"
{
        int bi = Compilador.pilaSaltos.remove(Compilador.pilaSaltos.size()-1);
        completarBI(bi);
    }
break;
case 78:
//#line 318 "grama.y"
{yyerror("no hay sentencias en else");}
break;
case 80:
//#line 323 "grama.y"
{
        yyerror("falta endif");
    }
break;
case 81:
//#line 327 "grama.y"
{
        yyerror("falta endif");yyerror("no hay sentencias en else");
    }
break;
case 82:
//#line 331 "grama.y"
{
        yyerror("falta endif");
    }
break;
case 83:
//#line 336 "grama.y"
{yyerror("no hay sentencias en then");}
break;
case 84:
//#line 337 "grama.y"
{yyerror("no hay sentencias en then");yyerror("no hay sentencias en else");}
break;
case 85:
//#line 338 "grama.y"
{yyerror("no hay sentencias en then");}
break;
case 86:
//#line 341 "grama.y"
{yyerror("no hay sentencias en then");yyerror("falta endif");}
break;
case 87:
//#line 342 "grama.y"
{yyerror("no hay sentencias en then");yyerror("no hay sentencias en else");yyerror("falta endif");}
break;
case 88:
//#line 343 "grama.y"
{yyerror("no hay sentencias en then");yyerror("falta endif");}
break;
case 89:
//#line 347 "grama.y"
{
        int bf = Compilador.pilaSaltos.remove(Compilador.pilaSaltos.size()-1);
        completarBF(bf, Compilador.tercetos.size()+1);
    }
break;
case 90:
//#line 354 "grama.y"
{
        int bf = Compilador.pilaSaltos.remove(Compilador.pilaSaltos.size()-1);
        completarBF(bf, Compilador.tercetos.size());
    }
break;
case 91:
//#line 360 "grama.y"
{
        crearTerceto("BI", "-", "-");
         int bi = Compilador.tercetos.size() - 1;
        Compilador.pilaSaltos.add(bi);}
break;
case 92:
//#line 367 "grama.y"
{
        crearTerceto(val_peek(2).sval, val_peek(3).sval, val_peek(1).sval);
         int t = Compilador.tercetos.size() - 1;
        crearTerceto("BF", "["+t+"]", "-");
        TablaDeSimbolos.agregarVarAux(t);
         int bf = Compilador.tercetos.size() - 1;
        Compilador.pilaSaltos.add(bf);
        yyval = new ParserVal("[" + t + "]");
    }
break;
case 93:
//#line 376 "grama.y"
{yyerror("falta cerrar parentesis");}
break;
case 94:
//#line 377 "grama.y"
{yyerror("falta abrir parentesis");}
break;
case 95:
//#line 378 "grama.y"
{yyerror("faltan parentesis");}
break;
case 96:
//#line 383 "grama.y"
{yyval=new ParserVal("==");}
break;
case 97:
//#line 384 "grama.y"
{yyval=new ParserVal("=!");}
break;
case 98:
//#line 385 "grama.y"
{yyval=new ParserVal("<=");}
break;
case 99:
//#line 386 "grama.y"
{yyval=new ParserVal(">=");}
break;
case 100:
//#line 387 "grama.y"
{yyval=new ParserVal("<");}
break;
case 101:
//#line 388 "grama.y"
{yyval=new ParserVal(">");}
break;
case 102:
//#line 389 "grama.y"
{yyerror("comparacion invalida");}
break;
case 105:
//#line 403 "grama.y"
{
        hayReturn = false;
        String ambito = Compilador.getAmbito();
        String var = val_peek(1).sval;
        if(TablaDeSimbolos.agregarVar(var, ambito, tipo, "nombre de funcion")){
           yyval = new ParserVal(var+ambito);
        } else yyerror("La funcion "+var+" ya fue declarada");
        crearTerceto("inicio de funcion", var+Compilador.getAmbito(), "-");
        Compilador.entrarAmbito(var);
    }
break;
case 106:
//#line 414 "grama.y"
{
        if (!hayReturn){
            yyerror("falta return en la funcion "+ val_peek(5).sval);
        }else hayReturn = false;

        Compilador.salirAmbito();
        crearTerceto("fin de funcion", val_peek(5).sval+Compilador.getAmbito(), "-");

    }
break;
case 107:
//#line 423 "grama.y"
{ yyerror("falta declarar nombre de funcion");}
break;
case 108:
//#line 424 "grama.y"
{yyerror("faltan parametros formales");}
break;
case 109:
//#line 425 "grama.y"
{yyerror("falta declarar nombre de funcion"); yyerror("faltan parametros formales");}
break;
case 110:
//#line 426 "grama.y"
{ yyerror("falta declarar el tipo de la funcion");}
break;
case 112:
//#line 431 "grama.y"
{ yyerror("falta sentencias en la funcion " + val_peek(0).sval);}
break;
case 113:
//#line 436 "grama.y"
{
        String ambito = Compilador.getAmbito();
        String var = val_peek(0).sval;
        if(TablaDeSimbolos.agregarVar(var, ambito, tipo, "nombre de variable", "copia resultado")){
           yyval = new ParserVal(var+ambito);
        } else yyerror("La variable "+var+" ya fue declarada");
    }
break;
case 114:
//#line 444 "grama.y"
{
        String ambito = Compilador.getAmbito();
        String var = val_peek(0).sval;
        if(TablaDeSimbolos.agregarVar(var, ambito, tipo, "nombre de variable","copia valor")){
           yyval = new ParserVal(var+ambito);
        } else yyerror("La variable "+var+" ya fue declarada");
    }
break;
case 115:
//#line 451 "grama.y"
{
        String ambito = Compilador.getAmbito();
        String var = val_peek(0).sval;
        if(TablaDeSimbolos.agregarVar(var, ambito, tipo, "nombre de variable", "copia resultado")){
           yyval = new ParserVal(var+ambito);
        } else yyerror("La variable "+var+" ya fue declarada");
    }
break;
case 116:
//#line 458 "grama.y"
{
        String ambito = Compilador.getAmbito();
        String var = val_peek(0).sval;
        if(TablaDeSimbolos.agregarVar(var, ambito, tipo, "nombre de variable", "copia valor")){
           yyval = new ParserVal(var+ambito);
        } else yyerror("La variable "+var+" ya fue declarada");
    }
break;
case 117:
//#line 465 "grama.y"
{yyerror("error en parametro formal");}
break;
case 118:
//#line 469 "grama.y"
{tipo = "uint";}
break;
case 123:
//#line 485 "grama.y"
{
        if (hayReturn || (Compilador.getAmbito().lastIndexOf(":") == 0) ){
            yyerror("no se permite este return");
        }else{
            hayReturn = true;
        }
        addEstructura("return");
        int t = Compilador.tercetos.size();
        crearTerceto("return", val_peek(2).sval, "["+t+"]");
        TablaDeSimbolos.agregarVarAux(t);
        yyval=val_peek(2);
        }
break;
case 125:
//#line 505 "grama.y"
{yyerror("falta cuerpo sentencias");}
break;
case 126:
//#line 506 "grama.y"
{yyerror("falta de ;");}
break;
case 127:
//#line 508 "grama.y"
{ yyerror("falta while");}
break;
case 128:
//#line 509 "grama.y"
{yyerror("falta cuerpo sentencias"); yyerror("falta while");}
break;
case 129:
//#line 510 "grama.y"
{yyerror("falta de ) en condicion");yyerror("falta de ;");yyerror("falta while");}
break;
case 130:
//#line 511 "grama.y"
{yyerror("falta de ) en condicion");yyerror("falta de ;");yyerror("falta cuerpo sentencias");yyerror("falta while");}
break;
case 131:
//#line 515 "grama.y"
{
        Compilador.pilaSaltos.add(Compilador.tercetos.size());
        crearTerceto("inicio while","-","-", true);
    }
break;
case 132:
//#line 521 "grama.y"
{
        int salto = Compilador.pilaSaltos.remove(0);
        crearTerceto("BIW", "[" +salto+"]", "-");
        crearTerceto("fin while", "-", "-");
    }
break;
case 133:
//#line 529 "grama.y"
{
         int bf = Compilador.pilaSaltos.remove(Compilador.pilaSaltos.size()-1);
         completarBF(bf, Compilador.tercetos.size()-1);
    }
break;
case 134:
//#line 537 "grama.y"
{hayReturn = false;}
break;
case 135:
//#line 538 "grama.y"
{yyerror("no hay sentencias dentro de las llaves");}
break;
case 136:
//#line 539 "grama.y"
{Compilador.salirAmbito();}
break;
case 137:
//#line 540 "grama.y"
{yyerror("Error en sentencia");}
break;
case 138:
//#line 546 "grama.y"
{
        yyval= new ParserVal("lambda");
        crearTerceto("fin de lambda", "-", "-");
    }
break;
case 139:
//#line 550 "grama.y"
{ yyerror("falta abrir llave en cuerpo de sentencia lambda");}
break;
case 140:
//#line 551 "grama.y"
{yyerror("falta cerrar llave en cuerpo de sentencia lambda");}
break;
case 141:
//#line 552 "grama.y"
{ yyerror("faltan llaves en cuerpo de sentencia lambda");}
break;
case 144:
//#line 562 "grama.y"
{
        String ambito = Compilador.getAmbito();
        String var = val_peek(0).sval;
        if(TablaDeSimbolos.agregarVar(var, ambito, tipo, "nombre de variable")){
           yyval = new ParserVal(var+ambito);
        } else yyerror("La variable "+var+" ya fue declarada");
        crearTerceto("inicio de lambda", "-", "-");
    }
break;
case 145:
//#line 575 "grama.y"
{
        asigMultiple(val_peek(3).sval,val_peek(1).sval);
    }
break;
case 147:
//#line 582 "grama.y"
{yyval=new ParserVal(val_peek(2).sval + "," + val_peek(0).sval);}
break;
case 148:
//#line 583 "grama.y"
{yyerror("falta , en lista de variables");}
break;
case 149:
//#line 587 "grama.y"
{yyval=val_peek(0);}
break;
case 150:
//#line 588 "grama.y"
{yyval=new ParserVal(val_peek(2).sval + "," + val_peek(0).sval);}
break;
case 151:
//#line 589 "grama.y"
{yyerror("falta , en lista de constantes");}
break;
//#line 1659 "Parser.java"
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
