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
   10,   10,   10,   29,   31,   30,   26,   26,   26,   26,
   32,   32,   32,   32,   32,   32,   32,   33,   33,    7,
   35,   35,   35,   37,    8,    8,    8,    8,   36,   36,
   36,   36,   36,   34,   38,   38,   39,   39,   12,   24,
   14,   14,   14,   14,   14,   14,   14,   40,   41,   27,
   28,   28,   28,   28,   21,   21,   21,   21,   42,   42,
   13,   43,   43,   43,   44,   44,   44,
};
final static short yylen[] = {                            2,
    0,    5,    3,    3,    3,    2,    3,    2,    4,    3,
    3,    3,    2,    2,    2,    1,    2,    2,    1,    1,
    1,    1,    1,    1,    1,    1,    1,    1,    1,    4,
    3,    3,    2,    3,    3,    1,    4,    3,    3,    3,
    3,    3,    3,    3,    2,    2,    1,    1,    1,    1,
    1,    1,    1,    2,    3,    1,    4,    3,    2,    1,
    5,    4,    3,    1,    5,    4,    1,    1,    1,    1,
    1,   11,    8,    7,   10,    7,    6,    9,    5,    4,
    7,    4,    3,    0,    0,    0,    5,    4,    4,    3,
    1,    1,    1,    1,    1,    1,    1,    1,    2,    3,
    1,    3,    2,    0,    9,    7,    7,    6,    3,    2,
    5,    4,    1,    1,    1,    2,    1,    1,    5,    1,
    9,    6,    6,    6,    5,    5,    4,    0,    0,    0,
    3,    2,    1,    3,    8,    7,    7,    6,    3,    3,
    4,    1,    3,    2,    1,    3,    2,
};
final static short yydefred[] = {                         0,
    0,    0,    0,    0,    0,    0,    0,    0,  130,    0,
  114,   14,    0,   16,   19,   20,   21,   22,   23,   24,
   25,   26,   27,   28,   29,    0,    0,    0,    0,    0,
    0,    0,    0,   18,    0,    0,    0,  128,    0,   50,
   53,    0,    0,    0,    0,    0,    0,   48,    0,    0,
   47,   49,   51,   52,    0,    7,   17,    0,    0,    0,
    0,    0,    0,  144,   12,    0,    0,    5,   10,    3,
   55,   71,   70,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,   54,    0,    0,   46,   45,   97,   91,
   92,   93,   94,    0,    0,   95,   96,    0,    0,    0,
   68,    0,    0,   67,   83,    0,    0,    0,  113,    0,
    0,    0,    0,  103,    0,  100,  143,  145,    0,    9,
    1,   66,    0,    0,    0,    0,  133,    0,    0,    0,
   64,    0,    0,    0,   39,   40,    0,   38,    0,    0,
   34,    0,   35,    0,    0,   44,   41,   43,   42,    0,
   82,   86,   80,    0,   30,    0,    0,    0,    0,  110,
    0,    0,  102,  147,    0,  141,    2,   65,  119,    0,
    0,  132,   98,    0,  125,    0,    0,   57,    0,    0,
   37,    0,    0,   89,   79,    0,    0,    0,    0,  104,
  109,  118,  117,    0,  115,    0,    0,    0,  146,  122,
  134,  131,   99,    0,  124,    0,   58,   87,    0,    0,
    0,    0,    0,   77,    0,    0,  108,  116,    0,    0,
  112,   85,    0,    0,    0,    0,  138,   81,    0,    0,
   76,   86,   74,  107,    0,  106,  111,    0,   61,    0,
  137,  136,    0,    0,    0,   73,    0,    0,  121,  135,
  139,  140,   78,    0,  105,    0,   75,   72,
};
final static short yydgoto[] = {                          4,
   13,  167,   14,   15,   16,   17,   18,   19,   20,   21,
   22,   23,   24,   25,   26,   49,  105,   50,   51,   52,
   53,   54,  133,  134,   77,   55,   38,  130,  187,  186,
  188,   98,  174,   27,   61,  113,  216,  194,  195,   79,
  222,  227,   28,  119,
};
final static short yysindex[] = {                       -89,
  321,  310,  640,    0,  -33,   -7,   19,   34,    0,  418,
    0,    0,  331,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0, -138,  -38,  -29,   -5,  655,
  369,   -4,  371,    0, -132,  -12,  428,    0,   31,    0,
    0,  434,  469, -127,  192,  312,  312,    0,  348,   80,
    0,    0,    0,    0,  -51,    0,    0,  428,   99,  -27,
   -6, -117, -111,    0,    0,   -2,  474,    0,    0,    0,
    0,    0,    0, -130,   22,   30,  110,  130,  -36,  396,
  138,  150,   80,    0,  348,  -99,    0,    0,    0,    0,
    0,    0,    0,  450,  465,    0,    0,  428,  283,  306,
    0,  155,   22,    0,    0, -103,   35,  -20,    0, -130,
   59,  -80,   54,    0,  -71,    0,    0,    0,  -41,    0,
    0,    0,   22,   22,  418,  526,    0, -138,   22,  412,
    0,   30,   67,  -87,    0,    0,  156,    0,  428,  147,
    0,   80,    0,   80,  421,    0,    0,    0,    0,   22,
    0,    0,    0,    0,    0,   75,  120,  -53,  286,    0,
   89, -239,    0,    0,  -28,    0,    0,    0,    0,   22,
   95,    0,    0, -113,    0,  418,   22,    0,  428,  -32,
    0,  560,  -73,    0,    0, -103,   17,  222,  286,    0,
    0,    0,    0,  403,    0,  286, -130,   57,    0,    0,
    0,    0,    0,    0,    0,   13,    0,    0,  288,  260,
   22,  252,   22,    0,  405,  204,    0,    0,  498,   77,
    0,    0,   88,  262,  248, -139,    0,    0,   76,   22,
    0,    0,    0,    0,  286,    0,    0,   22,    0,  248,
    0,    0,  307,  317,   22,    0, -103,  523,    0,    0,
    0,    0,    0,  265,    0,   22,    0,    0,
};
final static short yyrindex[] = {                         0,
    0,    0,    0,    0,    0,  -44,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,   42,    0,    0,  347,    0,
  360,  361,  370,    0,    0,    0,    0,    0,    1,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,   23,
    0,    0,    0,    0,  275,    0,    0,    0,   -3,    0,
    0,    0,    0,    0,    0,  382,  388,    0,    0,    0,
    0,    0,    0,    0,    0,  328,    0,    0,    0,    0,
    0,    0,   47,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,  275,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,  442,  157,    0,
    0,  -31,    0,  159,    0,    0,    0,    0,    0,    0,
    0,   69,    0,   93,  115,    0,    0,    0,    0,    0,
    0,    0,    0,   45,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,  165,    0,    0,  167,
    0,  143,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,  117,    0,  271,    0,    0,    0,    0,
  128,  275,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,  292,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,
};
final static short yygindex[] = {                         0,
  139,    0,   66,  675,  647,    0,    0,    0,    0,    0,
    0,    0,    0,    0,  729,  775,  627,    6,  160,    0,
    0,    0,    0,  223,    0,  -60,  -50,  -84,    0,  169,
 -195,  322, -125,  446,    0,  309,    0, -100, -143,    0,
    0, -200,    0,    0,
};
final static int YYTABLESIZE=954;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                         56,
   56,   60,  165,   45,  106,   46,   43,  104,   44,  120,
   47,  202,  120,  111,   62,  229,   56,  104,  129,  126,
  156,  154,   36,  241,  242,   34,  238,   74,   75,   46,
   43,   63,   44,    3,   47,   11,  197,  115,   35,  250,
  101,   56,   56,   56,   56,   56,   33,   56,   83,  209,
  218,  152,  104,   34,   34,  101,   34,  210,   36,   56,
   56,   56,   56,   36,  170,   36,   36,   36,   31,  177,
   80,  218,   94,   37,   95,  218,   35,   94,   57,   95,
  104,   36,   36,  224,   36,  142,  126,   33,  215,   33,
   33,   33,   32,  104,  161,  219,   57,  162,   57,  142,
  144,  211,  142,   85,  218,   33,   33,  178,   33,   31,
  179,   31,   31,   31,   90,  204,  123,  243,  244,   65,
   69,   99,  120,   56,   71,   56,  100,   31,   31,   58,
   31,   84,   57,   32,  248,   32,   32,   32,  108,    6,
   31,   33,   88,    6,   11,   36,  118,   36,    7,    8,
  123,   32,   32,    6,   32,   10,  127,  140,    7,    8,
  190,  232,  254,  162,  126,   10,    1,    2,   67,   33,
  124,   33,   94,   90,   95,  129,  160,   74,  136,   46,
   43,  159,   44,    6,   47,  163,  180,  183,    7,    8,
  138,   31,   94,   31,   95,   10,  181,  189,   94,   60,
   95,   88,   60,  191,  101,   87,   88,   59,  102,  103,
   59,  196,   56,  104,  101,   32,  164,   32,   59,  201,
   39,   40,   41,   56,  207,    7,    8,    6,  109,  199,
  125,   74,   10,   46,   43,  109,   44,   90,   47,   90,
   42,  123,  120,   72,   39,   40,   41,   11,  110,  101,
  114,   73,  101,  101,   11,  110,   56,   56,  147,  149,
   56,   56,   56,   56,   42,   88,   56,   88,   56,   56,
   56,   56,   56,   56,   56,   56,  212,  101,   36,   36,
  104,  127,   36,   36,   36,   36,  223,  226,   36,  126,
  101,   36,   36,   36,   36,   36,   36,   36,  142,  226,
   85,  226,   33,   33,   84,   85,   33,   33,   33,   33,
  104,   63,   33,  221,   63,   33,   33,   33,   33,   33,
   33,   33,   74,  104,   31,   31,  235,   44,   31,   31,
   31,   31,   62,  237,   31,   62,  245,   31,   31,   31,
   31,   31,   31,   31,  239,   74,   15,  251,   32,   32,
   44,   74,   32,   32,   32,   32,   44,  252,   32,    8,
   13,   32,   32,   32,   32,   32,   32,   32,   69,    6,
   90,   90,  129,  123,   90,   90,   90,   90,  123,  123,
   90,   11,  123,   90,  225,  123,  240,    4,   85,   90,
   94,  123,   95,  135,   39,   40,   41,  130,   88,   88,
  247,  206,   88,   88,   88,   88,  139,   96,   88,   97,
  101,   88,   11,  127,   42,  150,  157,   88,  127,  127,
    0,  126,  127,    0,    0,  127,  126,  126,    0,    0,
  126,  127,   30,  126,    0,   74,    0,   46,   43,  126,
   44,    0,   47,    0,    0,   12,    0,    0,   39,   40,
   41,   45,    0,   46,   43,   56,   44,   45,   47,   46,
   43,  184,   44,   94,   47,   95,   11,   74,   42,   46,
   43,    0,   44,   81,   47,   46,   43,  101,   44,    0,
   47,    0,  213,   48,   48,  142,   48,    0,   48,   74,
   86,   46,    0,   68,   44,   70,   47,    0,    0,    0,
    0,   48,  142,   48,   74,  112,   46,  101,   74,   44,
   46,   47,  230,   44,    0,   47,    6,    0,    6,   86,
  101,    7,    8,    7,    8,  256,   86,  217,   10,  234,
   10,  130,    0,    0,    0,    0,  130,  130,  146,   39,
   40,   41,    6,  130,    6,    0,    0,    7,    8,    7,
    8,    0,    0,  112,   10,  158,   10,    0,    0,    0,
   11,  148,   39,   40,   41,   29,    6,    0,   39,   40,
   41,    7,    8,    0,    0,    9,    5,    6,   10,    0,
    0,    0,    7,    8,   11,    0,    9,    6,    0,   10,
    0,    0,    7,    8,    0,   11,    9,    0,  121,   10,
  208,    0,   94,   89,   95,   11,    0,  198,    0,    0,
    0,    0,    0,    0,    0,    0,    0,   90,   91,   92,
   93,    0,  236,    0,    0,    6,    0,    6,    0,    0,
    7,    8,    7,    8,    9,    0,    9,   10,    0,   10,
    0,    0,  220,   11,    0,   11,    0,  255,    0,    0,
  172,  131,   39,   40,   41,    0,    0,    0,    0,    6,
    0,    6,    0,    0,    7,    8,    7,    8,   39,   40,
   41,   10,   42,   10,   39,   40,   41,   11,  176,   11,
    0,    0,    0,    0,   39,   40,   41,  116,   42,    0,
   39,   40,   41,    0,   42,    0,    0,   48,  142,    0,
    0,  122,    0,    0,   42,  141,   39,   40,   41,    0,
   42,   48,   48,   48,   48,    0,    0,    0,    0,    0,
  143,   39,   40,   41,    0,   39,   40,   41,  151,  153,
    6,    0,    0,  155,    0,    7,    8,    0,   48,    9,
    0,    0,   10,    0,    0,  166,    0,    0,   11,  168,
  169,    0,    0,  127,    6,  175,   64,    0,    0,    7,
    8,    0,    0,    0,   48,   48,   10,    0,    0,    0,
   48,   48,   11,   48,   48,   48,  185,    0,    0,    6,
  127,  171,    6,    0,    7,    8,   48,    7,    8,    0,
  117,   10,    0,    0,   10,    0,  200,   11,    0,    0,
  173,    0,    0,  205,    0,  193,    0,  128,   48,   48,
   76,   78,    0,    0,  214,    0,   82,    0,    0,   85,
    0,    0,   48,   48,    0,    0,   48,   48,   48,    0,
    0,    0,  107,  192,    0,  193,    0,  228,  231,  233,
  193,    0,  193,    0,    0,    0,    0,    0,  203,    0,
    0,    0,    0,   48,  132,  137,  246,  173,   48,    0,
  127,  193,    0,  192,  249,  193,    0,   48,  192,    0,
  192,  253,  145,    0,    0,    0,    0,    0,    0,    0,
  257,  193,  258,  173,  203,    0,    0,    0,    0,  192,
    0,    0,    0,  192,  193,   32,    6,    0,  203,    0,
    0,    7,    8,    0,   48,    9,    0,   48,   10,  192,
   66,    6,    0,  182,   11,    0,    7,    8,    0,    0,
    9,  127,  192,   10,    0,    0,    0,    0,    0,   11,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,  132,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                         44,
    0,   40,   44,   40,   55,   42,   43,   59,   45,   41,
   47,  125,   44,   41,   44,  211,   61,   59,   79,  123,
   41,  106,    0,  224,  225,   59,  222,   40,   41,   42,
   43,   61,   45,  123,   47,  275,  276,   44,   46,  240,
   44,   41,   42,   43,   44,   45,    0,   47,   43,  123,
  194,  102,   59,   59,   59,   59,   59,  183,   40,   59,
   60,   61,   62,   41,  125,   43,   44,   45,    0,  130,
   40,  215,   43,   40,   45,  219,   46,   43,   13,   45,
   59,   59,   60,  209,   62,   44,  123,   41,  189,   43,
   44,   45,    0,   59,   41,  196,   31,   44,   33,   94,
   95,  186,   61,   59,  248,   59,   60,   41,   62,   41,
   44,   43,   44,   45,    0,  176,    0,  257,  258,  125,
  125,   42,  125,  123,  257,  125,   47,   59,   60,  268,
   62,  259,   67,   41,  235,   43,   44,   45,   40,  257,
    2,    3,    0,  257,  275,  123,  258,  125,  262,  263,
   41,   59,   60,  257,   62,  269,    0,  257,  262,  263,
   41,  212,  247,   44,    0,  269,  256,  257,   30,  123,
   41,  125,   43,   59,   45,   59,  257,   40,   41,   42,
   43,  123,   45,  257,   47,  257,  274,   41,  262,  263,
   41,  123,   43,  125,   45,  269,   41,  123,   43,   41,
   45,   59,   44,  257,  256,   46,   47,   41,  260,  261,
   44,  123,  257,   59,  256,  123,  258,  125,  257,  125,
  257,  258,  259,  268,  257,  262,  263,  257,  256,  258,
  267,   40,  269,   42,   43,  256,   45,  123,   47,  125,
  277,  125,  274,  256,  257,  258,  259,  275,  276,  256,
  257,  264,  256,  257,  275,  276,  256,  257,   99,  100,
  260,  261,  262,  263,  277,  123,  266,  125,  268,  269,
  270,  271,  272,  273,  274,  275,  260,  256,  256,  257,
   59,  125,  260,  261,  262,  263,  274,   40,  266,  125,
  256,  269,  270,  271,  272,  273,  274,  275,  257,   40,
  256,   40,  256,  257,  260,  261,  260,  261,  262,  263,
   59,   41,  266,  257,   44,  269,  270,  271,  272,  273,
  274,  275,   40,   59,  256,  257,  123,   45,  260,  261,
  262,  263,   41,  257,  266,   44,  261,  269,  270,  271,
  272,  273,  274,  275,  257,   40,    0,   41,  256,  257,
   45,   40,  260,  261,  262,  263,   45,   41,  266,    0,
    0,  269,  270,  271,  272,  273,  274,  275,   41,    0,
  256,  257,  256,  257,  260,  261,  262,  263,  262,  263,
  266,    0,  266,  269,  125,  269,  125,    0,  261,  275,
   43,  275,   45,  256,  257,  258,  259,  123,  256,  257,
  232,  179,  260,  261,  262,  263,   85,   60,  266,   62,
  256,  269,  275,  257,  277,  261,  108,  275,  262,  263,
   -1,  257,  266,   -1,   -1,  269,  262,  263,   -1,   -1,
  266,  275,  123,  269,   -1,   40,   -1,   42,   43,  275,
   45,   -1,   47,   -1,   -1,  125,   -1,   -1,  257,  258,
  259,   40,   -1,   42,   43,  125,   45,   40,   47,   42,
   43,   41,   45,   43,   47,   45,  275,   40,  277,   42,
   43,   -1,   45,   40,   47,   42,   43,  256,   45,   -1,
   47,   -1,  261,   42,   43,   44,   45,   -1,   47,   40,
   45,   42,   -1,  125,   45,  125,   47,   -1,   -1,   -1,
   -1,   60,   61,   62,   40,   60,   42,  256,   40,   45,
   42,   47,  261,   45,   -1,   47,  257,   -1,  257,   74,
  256,  262,  263,  262,  263,  261,   81,  125,  269,  125,
  269,  257,   -1,   -1,   -1,   -1,  262,  263,  256,  257,
  258,  259,  257,  269,  257,   -1,   -1,  262,  263,  262,
  263,   -1,   -1,  108,  269,  110,  269,   -1,   -1,   -1,
  275,  256,  257,  258,  259,  256,  257,   -1,  257,  258,
  259,  262,  263,   -1,   -1,  266,  256,  257,  269,   -1,
   -1,   -1,  262,  263,  275,   -1,  266,  257,   -1,  269,
   -1,   -1,  262,  263,   -1,  275,  266,   -1,  125,  269,
   41,   -1,   43,  256,   45,  275,   -1,  162,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,  270,  271,  272,
  273,   -1,  125,   -1,   -1,  257,   -1,  257,   -1,   -1,
  262,  263,  262,  263,  266,   -1,  266,  269,   -1,  269,
   -1,   -1,  197,  275,   -1,  275,   -1,  125,   -1,   -1,
  125,  256,  257,  258,  259,   -1,   -1,   -1,   -1,  257,
   -1,  257,   -1,   -1,  262,  263,  262,  263,  257,  258,
  259,  269,  277,  269,  257,  258,  259,  275,  267,  275,
   -1,   -1,   -1,   -1,  257,  258,  259,   61,  277,   -1,
  257,  258,  259,   -1,  277,   -1,   -1,  256,  257,   -1,
   -1,   75,   -1,   -1,  277,  256,  257,  258,  259,   -1,
  277,  270,  271,  272,  273,   -1,   -1,   -1,   -1,   -1,
  256,  257,  258,  259,   -1,  257,  258,  259,  102,  103,
  257,   -1,   -1,  107,   -1,  262,  263,   -1,   10,  266,
   -1,   -1,  269,   -1,   -1,  119,   -1,   -1,  275,  123,
  124,   -1,   -1,   79,  257,  129,   28,   -1,   -1,  262,
  263,   -1,   -1,   -1,   36,   37,  269,   -1,   -1,   -1,
   42,   43,  275,   45,   46,   47,  150,   -1,   -1,  257,
  106,  256,  257,   -1,  262,  263,   58,  262,  263,   -1,
   62,  269,   -1,   -1,  269,   -1,  170,  275,   -1,   -1,
  126,   -1,   -1,  177,   -1,  159,   -1,   79,   80,   81,
   36,   37,   -1,   -1,  188,   -1,   42,   -1,   -1,   45,
   -1,   -1,   94,   95,   -1,   -1,   98,   99,  100,   -1,
   -1,   -1,   58,  159,   -1,  189,   -1,  211,  212,  213,
  194,   -1,  196,   -1,   -1,   -1,   -1,   -1,  174,   -1,
   -1,   -1,   -1,  125,   80,   81,  230,  183,  130,   -1,
  186,  215,   -1,  189,  238,  219,   -1,  139,  194,   -1,
  196,  245,   98,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
  254,  235,  256,  209,  210,   -1,   -1,   -1,   -1,  215,
   -1,   -1,   -1,  219,  248,  256,  257,   -1,  224,   -1,
   -1,  262,  263,   -1,  176,  266,   -1,  179,  269,  235,
  256,  257,   -1,  139,  275,   -1,  262,  263,   -1,   -1,
  266,  247,  248,  269,   -1,   -1,   -1,   -1,   -1,  275,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,  179,
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

//#line 407 "grama.y"
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
//#line 710 "Parser.java"
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
{TablaDeSimbolos.addAmbito(val_peek(4).sval, Compilador.getAmbito());}
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
        String ambito = Compilador.getAmbito();
        if (!TablaDeSimbolos.estaDeclarado(val_peek(3).sval, ambito)){
            yyerror("la variable "+val_peek(3).sval+" no fue delarada");
        }
        int t = crearTerceto(":=", val_peek(3).sval, val_peek(1).sval);
    }
break;
case 31:
//#line 75 "grama.y"
{
        int t = crearTerceto("+", val_peek(2).sval, val_peek(0).sval);

        yyval=new ParserVal("[" + t + "]");
    }
break;
case 32:
//#line 81 "grama.y"
{
        int t = crearTerceto("-", val_peek(2).sval, val_peek(0).sval);
        yyval=new ParserVal("[" + t + "]");
    }
break;
case 33:
//#line 85 "grama.y"
{yyerror("falta operando a izquierda de +");}
break;
case 34:
//#line 86 "grama.y"
{yyerror("falta operando a derecha de +");}
break;
case 35:
//#line 87 "grama.y"
{yyerror("falta operando a derecha de -");}
break;
case 36:
//#line 88 "grama.y"
{yyval=val_peek(0);}
break;
case 37:
//#line 89 "grama.y"
{addEstructura("trunc");}
break;
case 38:
//#line 90 "grama.y"
{addEstructura("trunc");yyerror("falta abrir parentesis en trunc");}
break;
case 39:
//#line 91 "grama.y"
{addEstructura("trunc");yyerror("falta cerrar parentesis en trunc");yyerrflag=0;}
break;
case 40:
//#line 92 "grama.y"
{addEstructura("trunc");yyerror("falta argumento en trunc");}
break;
case 41:
//#line 97 "grama.y"
{
        int t = crearTerceto("*", val_peek(2).sval, val_peek(0).sval);
        yyval=new ParserVal("[" + t + "]");
    }
break;
case 42:
//#line 102 "grama.y"
{
        int t = crearTerceto("/", val_peek(2).sval, val_peek(0).sval);
        yyval=new ParserVal("[" + t + "]");
    }
break;
case 43:
//#line 106 "grama.y"
{yyerror("falta operando a derecha de /");}
break;
case 44:
//#line 107 "grama.y"
{yyerror("falta operando a derecha de *");}
break;
case 45:
//#line 108 "grama.y"
{yyerror("falta operando a izquierda de /");}
break;
case 46:
//#line 109 "grama.y"
{yyerror("falta operando a izquierda de *");}
break;
case 47:
//#line 110 "grama.y"
{yyval=val_peek(0);}
break;
case 48:
//#line 114 "grama.y"
{yyval=val_peek(0);}
break;
case 49:
//#line 115 "grama.y"
{yyval=val_peek(0);}
break;
case 50:
//#line 116 "grama.y"
{yyval=val_peek(0);}
break;
case 51:
//#line 117 "grama.y"
{addEstructura("lambda"); yyval=val_peek(0);}
break;
case 52:
//#line 118 "grama.y"
{yyval=val_peek(0);}
break;
case 53:
//#line 122 "grama.y"
{check_rango(val_peek(0).sval);}
break;
case 54:
//#line 123 "grama.y"
{check_rango("-"+val_peek(0).sval);}
break;
case 55:
//#line 128 "grama.y"
{
        yyval = new ParserVal(val_peek(2).sval + '.' + val_peek(0).sval);
        /*checkDeclaracion($$.sval);*/
    }
break;
case 56:
//#line 132 "grama.y"
{yyval=val_peek(0);}
break;
case 57:
//#line 136 "grama.y"
{addEstructura("invocacion a funcion"); yyval=val_peek(3);}
break;
case 59:
//#line 141 "grama.y"
{yyerror("Falta parametro formal");}
break;
case 60:
//#line 142 "grama.y"
{yyerror("Falta flecha y parametro formal");}
break;
case 62:
//#line 144 "grama.y"
{yyerror("Falta parametro formal");}
break;
case 63:
//#line 145 "grama.y"
{yyerror("Falta flecha y parametro formal");}
break;
case 64:
//#line 146 "grama.y"
{yyerror("error en parametros de invocacion");}
break;
case 65:
//#line 151 "grama.y"
{
        int t = crearTerceto("print", val_peek(2).sval, "-");
        yyval=new ParserVal("[" + t + "]");
    }
break;
case 66:
//#line 155 "grama.y"
{yyerror("falta argumento en print");}
break;
case 68:
//#line 160 "grama.y"
{yyerror("falta ;");}
break;
case 71:
//#line 166 "grama.y"
{yyerror("argumento invalido en print");}
break;
case 72:
//#line 171 "grama.y"
{
        int bi = Compilador.pilaSaltos.removeLast();
        completarBI(bi, Compilador.tercetos.size());
    }
break;
case 73:
//#line 175 "grama.y"
{yyerror("no hay sentencias en else");}
break;
case 75:
//#line 179 "grama.y"
{ yyerror("falta endif");}
break;
case 76:
//#line 180 "grama.y"
{ yyerror("falta endif");yyerror("no hay sentencias en else");}
break;
case 77:
//#line 181 "grama.y"
{ yyerror("falta endif");}
break;
case 78:
//#line 184 "grama.y"
{yyerror("no hay sentencias en then");}
break;
case 79:
//#line 185 "grama.y"
{yyerror("no hay sentencias en then");yyerror("no hay sentencias en else");}
break;
case 80:
//#line 186 "grama.y"
{yyerror("no hay sentencias en then");}
break;
case 81:
//#line 189 "grama.y"
{yyerror("no hay sentencias en then");yyerror("falta endif");}
break;
case 82:
//#line 190 "grama.y"
{yyerror("no hay sentencias en then");yyerror("no hay sentencias en else");yyerror("falta endif");}
break;
case 83:
//#line 191 "grama.y"
{yyerror("no hay sentencias en then");yyerror("falta endif");}
break;
case 84:
//#line 195 "grama.y"
{
        int bf = Compilador.pilaSaltos.removeLast();
        completarBF(bf, Compilador.tercetos.size()+1);
    }
break;
case 85:
//#line 202 "grama.y"
{
        int bf = Compilador.pilaSaltos.removeLast();
        completarBF(bf, Compilador.tercetos.size());
    }
break;
case 86:
//#line 208 "grama.y"
{
        int bi = crearTerceto("BI", "-", "-");
        Compilador.pilaSaltos.add(bi);}
break;
case 87:
//#line 214 "grama.y"
{
        int t = crearTerceto(val_peek(2).sval, val_peek(3).sval, val_peek(1).sval);
        int bf = crearTerceto("BF", "["+t+"]", "-");
        Compilador.pilaSaltos.add(bf);
        yyval = new ParserVal("[" + t + "]");
    }
break;
case 88:
//#line 220 "grama.y"
{yyerror("falta cerrar parentesis");}
break;
case 89:
//#line 221 "grama.y"
{yyerror("falta abrir parentesis");}
break;
case 90:
//#line 222 "grama.y"
{yyerror("faltan parentesis");}
break;
case 91:
//#line 227 "grama.y"
{yyval=new ParserVal("==");}
break;
case 92:
//#line 228 "grama.y"
{yyval=new ParserVal("=!");}
break;
case 93:
//#line 229 "grama.y"
{yyval=new ParserVal("<=");}
break;
case 94:
//#line 230 "grama.y"
{yyval=new ParserVal(">=");}
break;
case 95:
//#line 231 "grama.y"
{yyval=new ParserVal("<");}
break;
case 96:
//#line 232 "grama.y"
{yyval=new ParserVal(">");}
break;
case 97:
//#line 233 "grama.y"
{yyerror("en condicion");}
break;
case 101:
//#line 248 "grama.y"
{
        String ambito = Compilador.getAmbito();
        if (!TablaDeSimbolos.estaDeclarado(val_peek(0).sval, ambito)){
            TablaDeSimbolos.addAmbito(val_peek(0).sval, ambito);
            TablaDeSimbolos.addTipo(val_peek(0).sval+":"+ambito, tipo);
        } else{
            yyerror("la variable "+val_peek(0).sval+" ya fue declarada");
        }
    }
break;
case 102:
//#line 257 "grama.y"
{yyval = new ParserVal(val_peek(2).sval+","+val_peek(0).sval);}
break;
case 103:
//#line 258 "grama.y"
{yyerror("falta , en lista de variables");}
break;
case 104:
//#line 264 "grama.y"
{Compilador.entrarAmbito(val_peek(3).sval);}
break;
case 105:
//#line 265 "grama.y"
{
        String ID = val_peek(7).sval;
        Compilador.salirAmbito();
        String ambito = Compilador.getAmbito();
        if (!TablaDeSimbolos.estaDeclarado(ID, ambito)){
            TablaDeSimbolos.addAmbito(ID, ambito);
            TablaDeSimbolos.addTipo(ID+":"+ambito, tipo);
        } else{
            yyerror("la variable "+ID+" ya fue declarada");
        }
    }
break;
case 106:
//#line 276 "grama.y"
{ yyerror("falta declarar nombre de funcion");}
break;
case 107:
//#line 277 "grama.y"
{yyerror("faltan parametros formales");}
break;
case 108:
//#line 278 "grama.y"
{yyerror("falta declarar nombre de funcion"); yyerror("faltan parametros formales");}
break;
case 109:
//#line 283 "grama.y"
{
        String ambito = Compilador.getAmbito();
        if (!TablaDeSimbolos.estaDeclarado(val_peek(0).sval, ambito)){
            TablaDeSimbolos.addAmbito(val_peek(0).sval, ambito);
            TablaDeSimbolos.addTipo(val_peek(0).sval+":"+ambito, tipo);
        } else{
            yyerror("la variable "+val_peek(0).sval+" ya fue declarada");
        }
    }
break;
case 110:
//#line 293 "grama.y"
{
        String ambito = Compilador.getAmbito();
        if (!TablaDeSimbolos.estaDeclarado(val_peek(0).sval, ambito)){
            TablaDeSimbolos.addAmbito(val_peek(0).sval, ambito);
            TablaDeSimbolos.addTipo(val_peek(0).sval+":"+ambito, tipo);
        } else{
            yyerror("la variable "+val_peek(0).sval+" ya fue declarada");
        }
    }
break;
case 113:
//#line 304 "grama.y"
{yyerror("error en parametro formal");}
break;
case 114:
//#line 308 "grama.y"
{tipo = "uint";}
break;
case 119:
//#line 324 "grama.y"
{
        addEstructura("return");
        int t = crearTerceto("return", val_peek(2).sval, "-");
        yyval=new ParserVal("[" + t + "]");
        }
break;
case 122:
//#line 342 "grama.y"
{yyerror("falta cuerpo sentencias");}
break;
case 123:
//#line 343 "grama.y"
{yyerror("falta de ;");}
break;
case 124:
//#line 345 "grama.y"
{ yyerror("falta while");}
break;
case 125:
//#line 346 "grama.y"
{yyerror("falta cuerpo sentencias"); yyerror("falta while");}
break;
case 126:
//#line 347 "grama.y"
{yyerror("falta de ) en condicion");yyerror("falta de ;");yyerror("falta while");}
break;
case 127:
//#line 348 "grama.y"
{yyerror("falta de ) en condicion");yyerror("falta de ;");yyerror("falta cuerpo sentencias");yyerror("falta while");}
break;
case 128:
//#line 351 "grama.y"
{Compilador.pilaSaltos.add(Compilador.tercetos.size());}
break;
case 129:
//#line 354 "grama.y"
{
        int salto = Compilador.pilaSaltos.removeFirst();
        int bi = crearTerceto("BI", "[" +salto+"]", "-");
    }
break;
case 130:
//#line 360 "grama.y"
{Compilador.entrarAmbito("ua"+cantUnidadesAnonimas); cantUnidadesAnonimas+= 1;}
break;
case 131:
//#line 363 "grama.y"
{Compilador.salirAmbito();}
break;
case 132:
//#line 364 "grama.y"
{yyerror("no hay sentencias dentro de las llaves");}
break;
case 133:
//#line 365 "grama.y"
{Compilador.salirAmbito();}
break;
case 134:
//#line 366 "grama.y"
{yyerror("Error en sentencia");}
break;
case 135:
//#line 371 "grama.y"
{yyval= new ParserVal("soy un lamnda");}
break;
case 136:
//#line 372 "grama.y"
{ yyerror("falta abrir llave en cuerpo de sentencia lambda");}
break;
case 137:
//#line 373 "grama.y"
{yyerror("falta cerra llave en cuerpo de sentencia lambda");}
break;
case 138:
//#line 374 "grama.y"
{ yyerror("faltan llaves en cuerpo de sentencia lambda");}
break;
case 141:
//#line 385 "grama.y"
{
        int t = crearTerceto("=", val_peek(3).sval, val_peek(1).sval);
        yyval=new ParserVal("[" + t + "]");
    }
break;
case 142:
//#line 392 "grama.y"
{yyval=val_peek(0);}
break;
case 143:
//#line 393 "grama.y"
{yyval=new ParserVal("{"+val_peek(2).sval + ", " + val_peek(0).sval+"}");}
break;
case 144:
//#line 394 "grama.y"
{yyerror("falta , en lista de variables");}
break;
case 145:
//#line 398 "grama.y"
{yyval=val_peek(0);}
break;
case 146:
//#line 399 "grama.y"
{yyval=new ParserVal("{"+val_peek(2).sval + ", " + val_peek(0).sval+"}");}
break;
case 147:
//#line 400 "grama.y"
{yyerror("falta , en lista de constantes");}
break;
//#line 1436 "Parser.java"
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
