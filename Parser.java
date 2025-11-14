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
    9,    9,   25,   25,   25,   25,   28,   28,   28,   28,
   28,   28,   28,   29,   29,    6,   31,   31,   31,   33,
    7,    7,    7,    7,   32,   32,   32,   32,   32,   30,
   34,   34,   35,   35,   11,   23,   13,   13,   13,   13,
   13,   13,   13,   26,   27,   27,   27,   27,   20,   20,
   20,   20,   36,   36,   12,   37,   37,   37,   38,   38,
   38,
};
final static short yylen[] = {                            2,
    4,    3,    3,    3,    2,    3,    2,    4,    3,    3,
    3,    2,    2,    2,    1,    2,    2,    1,    1,    1,
    1,    1,    1,    1,    1,    1,    1,    1,    4,    3,
    3,    2,    3,    3,    1,    4,    3,    3,    3,    3,
    3,    3,    3,    2,    2,    1,    1,    1,    1,    1,
    1,    1,    2,    3,    1,    4,    3,    2,    1,    5,
    4,    3,    1,    5,    4,    1,    1,    1,    1,    1,
    9,    7,    6,    8,    6,    5,    7,    5,    4,    6,
    4,    3,    5,    4,    4,    3,    1,    1,    1,    1,
    1,    1,    1,    1,    2,    3,    1,    3,    2,    0,
    9,    7,    7,    6,    3,    2,    5,    4,    1,    1,
    1,    2,    1,    1,    5,    1,    6,    5,    5,    5,
    4,    4,    3,    0,    3,    2,    1,    3,    8,    7,
    7,    6,    3,    3,    4,    1,    3,    2,    1,    3,
    2,
};
final static short yydefred[] = {                         0,
    0,    0,    0,    0,    0,    0,    0,    0,  124,    0,
  110,   13,    0,   15,   18,   19,   20,   21,   22,   23,
   24,   25,   26,   27,   28,    0,    0,    0,    0,    0,
    0,    0,    0,   17,    0,    0,    0,    0,    0,   49,
   52,    0,    0,    0,    0,    0,    0,   47,    0,    0,
   46,   48,   50,   51,    0,    6,   16,    0,    0,    0,
    0,    0,    0,  138,   11,    0,    0,    4,    9,    2,
   54,   70,   69,    0,    0,    0,    0,    0,    0,    0,
  127,    0,    0,    0,    0,    0,    0,    0,   53,    0,
    0,   45,   44,   93,   87,   88,   89,   90,    0,    0,
   91,   92,    0,    0,    0,   67,    0,    0,   66,   82,
    0,    0,    0,  109,    0,    0,    0,    0,   99,    0,
   96,  137,  139,    0,    8,    1,   65,    0,    0,    0,
    0,  126,   94,    0,  121,    0,    0,   63,    0,    0,
    0,   38,   39,    0,   37,    0,    0,   33,    0,   34,
    0,    0,   43,   40,   42,   41,    0,   81,    0,   79,
    0,   29,    0,    0,    0,    0,  106,    0,    0,   98,
  141,    0,  135,   64,  115,  118,  128,  125,   95,    0,
  120,   56,    0,    0,   36,    0,    0,   85,   78,    0,
    0,    0,   76,    0,  100,  105,  114,  113,    0,  111,
    0,    0,    0,  140,  117,    0,   57,   83,    0,    0,
    0,   80,    0,   75,    0,   73,    0,    0,  104,  112,
    0,    0,  108,    0,    0,    0,    0,  132,   77,   72,
    0,  103,    0,  102,  107,   60,    0,  131,  130,    0,
    0,    0,   74,    0,  129,  133,  134,   71,  101,
};
final static short yydgoto[] = {                          4,
   13,   14,   15,   16,   17,   18,   19,   20,   21,   22,
   23,   24,   25,   26,   49,  110,   50,   51,   52,   53,
   54,  140,  141,   77,   55,   38,   84,  103,  134,   27,
   61,  118,  218,  199,  200,  228,   28,  124,
};
final static short yysindex[] = {                       -69,
  331,  323,  562,    0,  -43,   27,  -18,   37,    0,  418,
    0,    0,  371,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0, -184,  -38,  -29,  -25,  605,
  373,  -23,  478,    0, -156,  -12,  428,  -36,   49,    0,
    0,  434,  473, -139,  192,  312,  312,    0,  348,  -22,
    0,    0,    0,    0,  -51,    0,    0,  428,   95,  -27,
   -6, -117, -111,    0,    0,   -7,  486,    0,    0,    0,
    0,    0,    0, -130,   22,   82,  123,   78,  418,  594,
    0, -184,   22,  396,  412,  138,  141,  -22,    0,  348,
  -99,    0,    0,    0,    0,    0,    0,    0,  450,  467,
    0,    0,  428,  283,  306,    0,  155,   22,    0,    0,
  -96,   35,  -20,    0, -130,   46,  -80,   64,    0,  -68,
    0,    0,    0,  -41,    0,    0,    0,   22,   22,   22,
   68,    0,    0,  537,    0,  418,   22,    0,   82,   98,
  -75,    0,    0,  163,    0,  428,  170,    0,  -22,    0,
  -22,  392,    0,    0,    0,    0,   22,    0,  -96,    0,
   45,    0,   89,  110,  -37,  476,    0,  102, -179,    0,
    0,  -28,    0,    0,    0,    0,    0,    0,    0,   22,
    0,    0,  428,    2,    0,  493,  237,    0,    0,  222,
  265,   22,    0,  476,    0,    0,    0,    0, -113,    0,
  476, -130,    3,    0,    0,   26,    0,    0,  -62,  248,
   22,    0,   22,    0,  -96,    0,  421,  154,    0,    0,
  520,   30,    0,   70,  262,  294,  -60,    0,    0,    0,
  274,    0,  476,    0,    0,    0,  294,    0,    0,  295,
  296,   22,    0,  541,    0,    0,    0,    0,    0,
};
final static short yyrindex[] = {                         0,
    0,    0,    0,    0,    0,  -44,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,   42,    0,    0,  345,    0,
  347,  358,  361,    0,    0,    0,    0,    0,    1,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,   23,
    0,    0,    0,    0,  298,    0,    0,    0,   -3,    0,
    0,    0,    0,    0,    0,  369,  370,    0,    0,    0,
    0,    0,    0,    0,    0,  307,    0,    0,    0,    0,
    0,  442,  117,    0,    0,    0,    0,   47,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,  298,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,  157,    0,  -31,    0,
  119,    0,    0,    0,    0,    0,    0,    0,   69,    0,
   93,  115,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,  165,
    0,    0,    0,  127,    0,  143,    0,    0,    0,    0,
  298,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,  270,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,  271,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,
};
final static short yygindex[] = {                         0,
  173,  449,  563,  408,    0,    0,    0,    0,    0,    0,
    0,    0,    0,  802,   13,  685,  -24,   86,    0,    0,
    0,    0,  199,    0,  -14,  -50,  -85,  299, -150,  463,
    0,  272,    0, -122,  333, -186,    0,    0,
};
final static int YYTABLESIZE=985;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                         55,
   55,   60,  172,   45,  111,   46,   43,  109,   44,  116,
   47,  219,  116,  116,   62,   34,   55,  109,   88,  104,
  163,   36,   35,   83,  105,  161,   80,   74,   75,   46,
   43,   63,   44,   34,   47,   34,  210,  120,  238,  239,
   97,   55,   55,   55,   55,   55,   32,   55,   76,   78,
  245,   34,  109,    3,   87,   97,  159,   90,  225,   55,
   55,   55,   55,   35,  130,   35,   35,   35,   30,  137,
  112,  217,   35,  190,  149,  151,   37,   99,  221,  100,
  109,   35,   35,   58,   35,  136,   80,   32,   85,   32,
   32,   32,   31,  109,   35,   11,  202,  139,  144,   65,
   71,   69,  136,  109,  168,   32,   32,  169,   32,   30,
  244,   30,   30,   30,   86,  152,  123,  125,  129,   89,
   99,  180,  100,   55,   99,   55,  100,   30,   30,  231,
   30,   92,   93,   31,  113,   31,   31,   31,  182,    6,
  215,  183,   84,    6,   11,   35,  123,   35,    7,    8,
  195,   31,   31,  169,   31,   10,  122,  147,  186,   59,
    6,   11,   59,  128,  119,    7,    8,   58,  166,   32,
   58,   32,   10,   86,   31,   33,  167,   74,  143,   46,
   43,  145,   44,   99,   47,  100,    1,    2,  170,  154,
  156,   30,  177,   30,    6,  139,  240,  241,  184,    7,
    8,   84,   67,  185,  106,   99,   10,  100,  107,  108,
  187,  194,   55,  109,  106,   31,  171,   31,   59,  196,
   39,   40,   41,   55,  201,    7,    8,    6,  114,  204,
   79,   74,   10,   46,   43,  114,   44,   86,   47,   86,
   42,  123,  116,   72,   39,   40,   41,   11,  115,  106,
  119,   73,   97,   97,   11,  115,   55,   55,  207,  223,
   55,   55,   55,   55,   42,   84,   55,   84,   55,   55,
   55,   55,   55,   55,   55,   55,  233,  106,   35,   35,
  109,  122,   35,   35,   35,   35,  235,  227,   35,  119,
  106,   35,   35,   35,   35,   35,   35,   35,  136,  224,
  106,  227,   32,   32,  191,  192,   32,   32,   32,   32,
   62,   61,   32,   62,   61,   32,   32,   32,   32,   32,
   32,   32,   74,  109,   30,   30,  236,   44,   30,   30,
   30,   30,  109,  227,   30,  246,  247,   30,   30,   30,
   30,   30,   30,   30,   14,   74,    7,   68,   31,   31,
   44,   74,   31,   31,   31,   31,   44,   12,   31,  209,
    5,   31,   31,   31,   31,   31,   31,   31,   10,    3,
   86,   86,  226,  123,   86,   86,   86,   86,  123,  123,
   86,  206,  123,   86,  164,  123,  237,    0,  146,   86,
   99,  123,  100,  142,   39,   40,   41,    0,   84,   84,
    0,    0,   84,   84,   84,   84,    0,  101,   84,  102,
  106,   84,   11,  122,   42,  157,    0,   84,  122,  122,
  124,  119,  122,    0,    0,  122,  119,  119,    0,    0,
  119,  122,  188,  119,   99,   45,  100,   46,   43,  119,
   44,    0,   47,    0,    0,   30,    0,    0,   39,   40,
   41,   74,    0,   46,   43,   12,   44,   45,   47,   46,
   43,   57,   44,    0,   47,    0,   11,   74,   42,   46,
   43,    0,   44,   86,   47,   46,   43,  106,   44,   57,
   47,   57,  211,   47,   47,  136,   47,    0,   47,   74,
    0,   46,    0,    6,   44,   56,   47,   68,    7,    8,
    0,   47,  136,   47,    6,   10,   74,   91,   46,    7,
    8,   44,   74,   47,   46,   57,   10,   44,    6,   47,
  106,    0,  117,    7,    8,  213,    0,    0,    0,  106,
   10,  220,    0,  208,  242,   99,   91,  100,  153,   39,
   40,   41,    0,    0,    0,  232,    0,    0,   91,  220,
    0,    0,    0,  220,  124,    0,    0,    0,    0,  124,
  124,  155,   39,   40,   41,    0,  124,    0,   39,   40,
   41,    0,    0,  198,    0,  117,  220,  165,   29,    6,
    0,    0,    0,    0,    7,    8,    5,    6,    9,    0,
    0,   10,    7,    8,    0,    0,    9,   11,    0,   10,
   81,  198,   70,   94,    0,   11,  198,    0,  198,    0,
  126,    0,    0,    0,    0,    0,    0,   95,   96,   97,
   98,    0,    0,    0,  198,    0,    0,    6,  198,    6,
    0,  203,    7,    8,    7,    8,    9,    0,    9,   10,
  198,   10,  133,    0,  234,   11,    0,   11,    0,    0,
    0,  198,   39,   40,   41,    0,    0,    0,    0,    0,
    0,  178,  136,    0,  222,  249,    0,  138,   39,   40,
   41,    0,   42,   81,   39,   40,   41,    6,    0,    0,
    0,    0,    7,    8,   39,   40,   41,    0,   42,   10,
   39,   40,   41,    0,   42,   11,  179,   47,  136,    0,
    0,    0,    0,    0,   42,  148,   39,   40,   41,    0,
   42,   47,   47,   47,   47,    0,    0,    0,  132,    0,
    0,   81,  150,   39,   40,   41,    0,    0,  197,   39,
   40,   41,    6,    0,    6,    0,    0,    7,    8,    7,
    8,    0,    6,    9,   10,  121,   10,    7,    8,  133,
   11,    9,   11,    0,   10,    0,  197,    0,    0,  127,
   11,  197,    0,  197,    0,    0,    0,  135,    0,    0,
    0,  133,  179,    0,    0,    0,    6,   81,    0,  197,
    0,    7,    8,  197,    0,    0,    0,  179,   10,    0,
    0,  158,  160,    6,   11,  197,  162,    6,    7,    8,
    0,    0,    7,    8,    0,   10,  197,    0,  173,   10,
    0,   48,  174,  175,  176,   11,    0,   32,    6,    0,
    0,  181,    0,    7,    8,    0,    0,    9,    0,   64,
   10,    0,    0,    0,    0,    0,   11,   48,   48,   82,
    0,  189,    0,   48,   48,  193,   48,   48,   48,  131,
    6,    0,    0,    0,    0,    7,    8,    0,    0,   48,
   66,    6,   10,  122,  205,    0,    7,    8,    0,    0,
    9,    0,    0,   10,  212,  214,  216,    0,    0,   11,
   48,    0,    0,    0,    0,   48,   48,   48,    0,    0,
    0,    0,    0,    0,    0,  229,    0,  230,    0,    0,
   48,   48,    0,    0,   48,   48,   48,    0,    0,    0,
    0,    0,    0,    0,    0,  243,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,  248,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,   48,    0,    0,
    0,    0,    0,    0,    0,    0,    0,   48,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,   48,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                         44,
    0,   40,   44,   40,   55,   42,   43,   59,   45,   41,
   47,  125,   44,   41,   44,   59,   61,   59,   43,   42,
   41,   40,    0,   38,   47,  111,  123,   40,   41,   42,
   43,   61,   45,   59,   47,   59,  187,   44,  225,  226,
   44,   41,   42,   43,   44,   45,    0,   47,   36,   37,
  237,   59,   59,  123,   42,   59,  107,   45,  209,   59,
   60,   61,   62,   41,   79,   43,   44,   45,    0,   84,
   58,  194,   46,  159,   99,  100,   40,   43,  201,   45,
   59,   59,   60,  268,   62,   44,  123,   41,   40,   43,
   44,   45,    0,   59,   46,  275,  276,   85,   86,  125,
  257,  125,   61,   59,   41,   59,   60,   44,   62,   41,
  233,   43,   44,   45,    0,  103,    0,  125,   41,  259,
   43,  136,   45,  123,   43,  125,   45,   59,   60,  215,
   62,   46,   47,   41,   40,   43,   44,   45,   41,  257,
  191,   44,    0,  257,  275,  123,  258,  125,  262,  263,
   41,   59,   60,   44,   62,  269,    0,  257,  146,   41,
  257,  275,   44,   41,    0,  262,  263,   41,  123,  123,
   44,  125,  269,   59,    2,    3,  257,   40,   41,   42,
   43,   41,   45,   43,   47,   45,  256,  257,  257,  104,
  105,  123,  125,  125,  257,  183,  257,  258,  274,  262,
  263,   59,   30,   41,  256,   43,  269,   45,  260,  261,
   41,  123,  257,   59,  256,  123,  258,  125,  257,  257,
  257,  258,  259,  268,  123,  262,  263,  257,  256,  258,
  267,   40,  269,   42,   43,  256,   45,  123,   47,  125,
  277,  125,  274,  256,  257,  258,  259,  275,  276,  256,
  257,  264,  256,  257,  275,  276,  256,  257,  257,  257,
  260,  261,  262,  263,  277,  123,  266,  125,  268,  269,
  270,  271,  272,  273,  274,  275,  123,  256,  256,  257,
   59,  125,  260,  261,  262,  263,  257,   40,  266,  125,
  256,  269,  270,  271,  272,  273,  274,  275,  257,  274,
  256,   40,  256,  257,  260,  261,  260,  261,  262,  263,
   41,   41,  266,   44,   44,  269,  270,  271,  272,  273,
  274,  275,   40,   59,  256,  257,  257,   45,  260,  261,
  262,  263,   59,   40,  266,   41,   41,  269,  270,  271,
  272,  273,  274,  275,    0,   40,    0,   41,  256,  257,
   45,   40,  260,  261,  262,  263,   45,    0,  266,  123,
    0,  269,  270,  271,  272,  273,  274,  275,    0,    0,
  256,  257,  125,  257,  260,  261,  262,  263,  262,  263,
  266,  183,  266,  269,  113,  269,  125,   -1,   90,  275,
   43,  275,   45,  256,  257,  258,  259,   -1,  256,  257,
   -1,   -1,  260,  261,  262,  263,   -1,   60,  266,   62,
  256,  269,  275,  257,  277,  261,   -1,  275,  262,  263,
  123,  257,  266,   -1,   -1,  269,  262,  263,   -1,   -1,
  266,  275,   41,  269,   43,   40,   45,   42,   43,  275,
   45,   -1,   47,   -1,   -1,  123,   -1,   -1,  257,  258,
  259,   40,   -1,   42,   43,  125,   45,   40,   47,   42,
   43,   13,   45,   -1,   47,   -1,  275,   40,  277,   42,
   43,   -1,   45,   40,   47,   42,   43,  256,   45,   31,
   47,   33,  261,   42,   43,   44,   45,   -1,   47,   40,
   -1,   42,   -1,  257,   45,  125,   47,  125,  262,  263,
   -1,   60,   61,   62,  257,  269,   40,   45,   42,  262,
  263,   45,   40,   47,   42,   67,  269,   45,  257,   47,
  256,   -1,   60,  262,  263,  261,   -1,   -1,   -1,  256,
  269,  199,   -1,   41,  261,   43,   74,   45,  256,  257,
  258,  259,   -1,   -1,   -1,  125,   -1,   -1,   86,  217,
   -1,   -1,   -1,  221,  257,   -1,   -1,   -1,   -1,  262,
  263,  256,  257,  258,  259,   -1,  269,   -1,  257,  258,
  259,   -1,   -1,  166,   -1,  113,  244,  115,  256,  257,
   -1,   -1,   -1,   -1,  262,  263,  256,  257,  266,   -1,
   -1,  269,  262,  263,   -1,   -1,  266,  275,   -1,  269,
   38,  194,  125,  256,   -1,  275,  199,   -1,  201,   -1,
  125,   -1,   -1,   -1,   -1,   -1,   -1,  270,  271,  272,
  273,   -1,   -1,   -1,  217,   -1,   -1,  257,  221,  257,
   -1,  169,  262,  263,  262,  263,  266,   -1,  266,  269,
  233,  269,   80,   -1,  125,  275,   -1,  275,   -1,   -1,
   -1,  244,  257,  258,  259,   -1,   -1,   -1,   -1,   -1,
   -1,  125,  267,   -1,  202,  125,   -1,  256,  257,  258,
  259,   -1,  277,  111,  257,  258,  259,  257,   -1,   -1,
   -1,   -1,  262,  263,  257,  258,  259,   -1,  277,  269,
  257,  258,  259,   -1,  277,  275,  134,  256,  257,   -1,
   -1,   -1,   -1,   -1,  277,  256,  257,  258,  259,   -1,
  277,  270,  271,  272,  273,   -1,   -1,   -1,  125,   -1,
   -1,  159,  256,  257,  258,  259,   -1,   -1,  166,  257,
  258,  259,  257,   -1,  257,   -1,   -1,  262,  263,  262,
  263,   -1,  257,  266,  269,   61,  269,  262,  263,  187,
  275,  266,  275,   -1,  269,   -1,  194,   -1,   -1,   75,
  275,  199,   -1,  201,   -1,   -1,   -1,   83,   -1,   -1,
   -1,  209,  210,   -1,   -1,   -1,  257,  215,   -1,  217,
   -1,  262,  263,  221,   -1,   -1,   -1,  225,  269,   -1,
   -1,  107,  108,  257,  275,  233,  112,  257,  262,  263,
   -1,   -1,  262,  263,   -1,  269,  244,   -1,  124,  269,
   -1,   10,  128,  129,  130,  275,   -1,  256,  257,   -1,
   -1,  137,   -1,  262,  263,   -1,   -1,  266,   -1,   28,
  269,   -1,   -1,   -1,   -1,   -1,  275,   36,   37,   38,
   -1,  157,   -1,   42,   43,  161,   45,   46,   47,  256,
  257,   -1,   -1,   -1,   -1,  262,  263,   -1,   -1,   58,
  256,  257,  269,   62,  180,   -1,  262,  263,   -1,   -1,
  266,   -1,   -1,  269,  190,  191,  192,   -1,   -1,  275,
   79,   -1,   -1,   -1,   -1,   84,   85,   86,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,  211,   -1,  213,   -1,   -1,
   99,  100,   -1,   -1,  103,  104,  105,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,  231,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,  242,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,  136,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,  146,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,  183,
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

//#line 696 "Parser.java"
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
//#line 17 "grama.y"
{yyerror("falta nombre de programa");}
break;
case 3:
//#line 18 "grama.y"
{yyerror("falta llave de cierre de programa");}
break;
case 4:
//#line 19 "grama.y"
{yyerror("falta llave de inicio de programa");}
break;
case 5:
//#line 20 "grama.y"
{yyerror("falta nombre del programa");yyerror("falta llave de cierre de programa");}
break;
case 6:
//#line 21 "grama.y"
{yyerror("falta nombre del programa");yyerror("falta llave de inicio de programa");}
break;
case 7:
//#line 22 "grama.y"
{yyerror("no hay llaves del programa");}
break;
case 8:
//#line 24 "grama.y"
{yyerror("error en programa");}
break;
case 9:
//#line 25 "grama.y"
{yyerror("falta nombre de programa");yyerror("error en programa");}
break;
case 10:
//#line 26 "grama.y"
{yyerror("falta llave de cierre de programa");yyerror("error en programa");}
break;
case 11:
//#line 27 "grama.y"
{yyerror("falta llave de inicio de programa");yyerror("error en programa");}
break;
case 12:
//#line 28 "grama.y"
{yyerror("falta nombre del programa");yyerror("falta llave de cierre de programa");yyerror("error en programa");}
break;
case 13:
//#line 29 "grama.y"
{yyerror("falta nombre del programa");yyerror("error en programa");}
break;
case 14:
//#line 30 "grama.y"
{yyerror("no hay llaves del programa");yyerror("error en programa");}
break;
case 17:
//#line 35 "grama.y"
{yyerror("error en sentencia");}
break;
case 21:
//#line 45 "grama.y"
{addEstructura("declaracion de variable");}
break;
case 22:
//#line 46 "grama.y"
{addEstructura("declaracion de funcion");}
break;
case 23:
//#line 50 "grama.y"
{addEstructura("asignacion");}
break;
case 24:
//#line 51 "grama.y"
{addEstructura("sentencia if");}
break;
case 25:
//#line 52 "grama.y"
{addEstructura("print");}
break;
case 26:
//#line 53 "grama.y"
{addEstructura("return");}
break;
case 27:
//#line 54 "grama.y"
{addEstructura("asignacion multiple");}
break;
case 28:
//#line 58 "grama.y"
{addEstructura("sentencia do while");}
break;
case 29:
//#line 63 "grama.y"
{
        int t = crearTerceto(":=", val_peek(3).sval, val_peek(1).sval);
    }
break;
case 30:
//#line 71 "grama.y"
{
        int t = crearTerceto("+", val_peek(2).sval, val_peek(0).sval);

        yyval=new ParserVal("[" + t + "]");
    }
break;
case 31:
//#line 77 "grama.y"
{
        int t = crearTerceto("-", val_peek(2).sval, val_peek(0).sval);
        yyval=new ParserVal("[" + t + "]");
    }
break;
case 32:
//#line 81 "grama.y"
{yyerror("falta operando a izquierda de +");}
break;
case 33:
//#line 82 "grama.y"
{yyerror("falta operando a derecha de +");}
break;
case 34:
//#line 83 "grama.y"
{yyerror("falta operando a derecha de -");}
break;
case 35:
//#line 84 "grama.y"
{yyval=val_peek(0);}
break;
case 36:
//#line 85 "grama.y"
{addEstructura("trunc");}
break;
case 37:
//#line 86 "grama.y"
{addEstructura("trunc");yyerror("falta abrir parentesis en trunc");}
break;
case 38:
//#line 87 "grama.y"
{addEstructura("trunc");yyerror("falta cerrar parentesis en trunc");yyerrflag=0;}
break;
case 39:
//#line 88 "grama.y"
{addEstructura("trunc");yyerror("falta argumento en trunc");}
break;
case 40:
//#line 93 "grama.y"
{
        int t = crearTerceto("*", val_peek(2).sval, val_peek(0).sval);
        yyval=new ParserVal("[" + t + "]");
    }
break;
case 41:
//#line 98 "grama.y"
{
        int t = crearTerceto("/", val_peek(2).sval, val_peek(0).sval);
        yyval=new ParserVal("[" + t + "]");
    }
break;
case 42:
//#line 102 "grama.y"
{yyerror("falta operando a derecha de /");}
break;
case 43:
//#line 103 "grama.y"
{yyerror("falta operando a derecha de *");}
break;
case 44:
//#line 104 "grama.y"
{yyerror("falta operando a izquierda de /");}
break;
case 45:
//#line 105 "grama.y"
{yyerror("falta operando a izquierda de *");}
break;
case 46:
//#line 106 "grama.y"
{yyval=val_peek(0);}
break;
case 47:
//#line 110 "grama.y"
{yyval=val_peek(0);}
break;
case 48:
//#line 111 "grama.y"
{yyval=val_peek(0);}
break;
case 49:
//#line 112 "grama.y"
{yyval=val_peek(0);}
break;
case 50:
//#line 113 "grama.y"
{addEstructura("lambda"); yyval=val_peek(0);}
break;
case 51:
//#line 114 "grama.y"
{yyval=val_peek(0);}
break;
case 52:
//#line 118 "grama.y"
{check_rango(val_peek(0).sval);}
break;
case 53:
//#line 119 "grama.y"
{check_rango("-"+val_peek(0).sval);}
break;
case 54:
//#line 123 "grama.y"
{yyval= new ParserVal(val_peek(2).sval + '.' + val_peek(0).sval);}
break;
case 55:
//#line 124 "grama.y"
{yyval=val_peek(0);}
break;
case 56:
//#line 128 "grama.y"
{addEstructura("invocacion a funcion"); yyval=val_peek(3);}
break;
case 58:
//#line 133 "grama.y"
{yyerror("Falta parametro formal");}
break;
case 59:
//#line 134 "grama.y"
{yyerror("Falta flecha y parametro formal");}
break;
case 61:
//#line 136 "grama.y"
{yyerror("Falta parametro formal");}
break;
case 62:
//#line 137 "grama.y"
{yyerror("Falta flecha y parametro formal");}
break;
case 63:
//#line 138 "grama.y"
{yyerror("error en parametros de invocacion");}
break;
case 64:
//#line 143 "grama.y"
{
        int t = crearTerceto("print", val_peek(2).sval, "-");
        yyval=new ParserVal("[" + t + "]");
    }
break;
case 65:
//#line 147 "grama.y"
{yyerror("falta argumento en print");}
break;
case 67:
//#line 152 "grama.y"
{yyerror("falta ;");}
break;
case 70:
//#line 158 "grama.y"
{yyerror("argumento invalido en print");}
break;
case 71:
//#line 163 "grama.y"
{
        int t = crearTerceto("BF", val_peek(7).sval, "salto a ");
        yyval=new ParserVal("[" + t + "]");
    }
break;
case 72:
//#line 167 "grama.y"
{yyerror("no hay sentencias en else");}
break;
case 73:
//#line 169 "grama.y"
{
        int t = crearTerceto("BF", val_peek(4).sval, "salto a ");
        yyval=new ParserVal("[" + t + "]");
    }
break;
case 74:
//#line 174 "grama.y"
{ yyerror("falta endif");}
break;
case 75:
//#line 175 "grama.y"
{ yyerror("falta endif");yyerror("no hay sentencias en else");}
break;
case 76:
//#line 176 "grama.y"
{ yyerror("falta endif");}
break;
case 77:
//#line 179 "grama.y"
{yyerror("no hay sentencias en then");}
break;
case 78:
//#line 180 "grama.y"
{yyerror("no hay sentencias en then");yyerror("no hay sentencias en else");}
break;
case 79:
//#line 181 "grama.y"
{yyerror("no hay sentencias en then");}
break;
case 80:
//#line 184 "grama.y"
{yyerror("no hay sentencias en then");yyerror("falta endif");}
break;
case 81:
//#line 185 "grama.y"
{yyerror("no hay sentencias en then");yyerror("no hay sentencias en else");yyerror("falta endif");}
break;
case 82:
//#line 186 "grama.y"
{yyerror("no hay sentencias en then");yyerror("falta endif");}
break;
case 83:
//#line 191 "grama.y"
{
        int t = crearTerceto(val_peek(2).sval, val_peek(3).sval, val_peek(1).sval);
        yyval=new ParserVal("[" + t + "]");
    }
break;
case 84:
//#line 195 "grama.y"
{yyerror("falta cerrar parentesis");}
break;
case 85:
//#line 196 "grama.y"
{yyerror("falta abrir parentesis");}
break;
case 86:
//#line 197 "grama.y"
{yyerror("faltan parentesis");}
break;
case 87:
//#line 202 "grama.y"
{yyval=new ParserVal("==");}
break;
case 88:
//#line 203 "grama.y"
{yyval=new ParserVal("=!");}
break;
case 89:
//#line 204 "grama.y"
{yyval=new ParserVal("<=");}
break;
case 90:
//#line 205 "grama.y"
{yyval=new ParserVal(">=");}
break;
case 91:
//#line 206 "grama.y"
{yyval=new ParserVal("<");}
break;
case 92:
//#line 207 "grama.y"
{yyval=new ParserVal(">");}
break;
case 93:
//#line 208 "grama.y"
{yyerror("en condicion");}
break;
case 97:
//#line 223 "grama.y"
{TablaDeSimbolos.addAmbito(val_peek(0).sval, Compilador.getAmbito());}
break;
case 99:
//#line 225 "grama.y"
{yyerror("falta , en lista de variables");}
break;
case 100:
//#line 231 "grama.y"
{Compilador.entrarAmbito(val_peek(3).sval);}
break;
case 101:
//#line 231 "grama.y"
{Compilador.salirAmbito();}
break;
case 102:
//#line 232 "grama.y"
{ yyerror("falta declarar nombre de funcion");}
break;
case 103:
//#line 233 "grama.y"
{yyerror("faltan parametros formales");}
break;
case 104:
//#line 234 "grama.y"
{yyerror("falta declarar nombre de funcion"); yyerror("faltan parametros formales");}
break;
case 109:
//#line 242 "grama.y"
{yyerror("error en parametro formal");}
break;
case 115:
//#line 262 "grama.y"
{
        addEstructura("return");
        int t = crearTerceto("return", val_peek(2).sval, "-");
        yyval=new ParserVal("[" + t + "]");
        }
break;
case 118:
//#line 280 "grama.y"
{yyerror("falta cuerpo sentencias");}
break;
case 119:
//#line 281 "grama.y"
{yyerror("falta de ;");}
break;
case 120:
//#line 283 "grama.y"
{ yyerror("falta while");}
break;
case 121:
//#line 284 "grama.y"
{yyerror("falta cuerpo sentencias"); yyerror("falta while");}
break;
case 122:
//#line 285 "grama.y"
{yyerror("falta de ) en condicion");yyerror("falta de ;");yyerror("falta while");}
break;
case 123:
//#line 286 "grama.y"
{yyerror("falta de ) en condicion");yyerror("falta de ;");yyerror("falta cuerpo sentencias");yyerror("falta while");}
break;
case 124:
//#line 289 "grama.y"
{Compilador.entrarAmbito("ua"+cantUnidadesAnonimas); cantUnidadesAnonimas+= 1;}
break;
case 125:
//#line 292 "grama.y"
{Compilador.salirAmbito();}
break;
case 126:
//#line 293 "grama.y"
{yyerror("no hay sentencias dentro de las llaves");}
break;
case 127:
//#line 294 "grama.y"
{Compilador.salirAmbito();}
break;
case 128:
//#line 295 "grama.y"
{yyerror("Error en sentencia");}
break;
case 129:
//#line 300 "grama.y"
{yyval= new ParserVal("soy un lamnda");}
break;
case 130:
//#line 301 "grama.y"
{ yyerror("falta abrir llave en cuerpo de sentencia lambda");}
break;
case 131:
//#line 302 "grama.y"
{yyerror("falta cerra llave en cuerpo de sentencia lambda");}
break;
case 132:
//#line 303 "grama.y"
{ yyerror("faltan llaves en cuerpo de sentencia lambda");}
break;
case 135:
//#line 314 "grama.y"
{
        int t = crearTerceto("=", val_peek(3).sval, val_peek(1).sval);
        yyval=new ParserVal("[" + t + "]");
    }
break;
case 136:
//#line 321 "grama.y"
{yyval=val_peek(0);}
break;
case 137:
//#line 322 "grama.y"
{yyval=new ParserVal("{"+val_peek(2).sval + ", " + val_peek(0).sval+"}");}
break;
case 138:
//#line 323 "grama.y"
{yyerror("falta , en lista de variables");}
break;
case 139:
//#line 327 "grama.y"
{yyval=val_peek(0);}
break;
case 140:
//#line 328 "grama.y"
{yyval=new ParserVal("{"+val_peek(2).sval + ", " + val_peek(0).sval+"}");}
break;
case 141:
//#line 329 "grama.y"
{yyerror("falta , en lista de constantes");}
break;
//#line 1335 "Parser.java"
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
