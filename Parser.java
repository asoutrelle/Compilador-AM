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
    9,    9,   25,   25,   25,   25,   27,   27,   27,   27,
   27,   27,   27,   28,   28,    6,   30,   30,   30,    7,
    7,    7,    7,   31,   31,   31,   31,   31,   29,   32,
   32,   33,   33,   11,   23,   13,   13,   13,   13,   13,
   13,   13,   26,   26,   26,   26,   20,   20,   20,   20,
   34,   34,   12,   35,   35,   35,   36,   36,   36,
};
final static short yylen[] = {                            2,
    4,    3,    3,    3,    2,    3,    2,    4,    3,    3,
    3,    2,    2,    2,    1,    2,    2,    1,    1,    1,
    1,    1,    1,    1,    1,    1,    1,    1,    4,    3,
    3,    2,    3,    3,    1,    4,    3,    3,    3,    3,
    3,    3,    3,    2,    2,    1,    1,    1,    1,    1,
    1,    1,    2,    3,    1,    4,    3,    2,    1,    5,
    4,    3,    1,    5,    4,    1,    1,    1,    1,    1,
    7,    6,    5,    6,    5,    4,    6,    5,    4,    5,
    4,    3,    5,    4,    4,    3,    1,    1,    1,    1,
    1,    1,    1,    1,    2,    3,    1,    3,    2,    8,
    7,    7,    6,    3,    2,    5,    4,    1,    1,    1,
    2,    1,    1,    5,    1,    5,    4,    4,    4,    3,
    3,    2,    3,    2,    1,    3,    8,    7,    7,    6,
    3,    3,    4,    1,    3,    2,    1,    3,    2,
};
final static short yydefred[] = {                         0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
  109,   13,    0,   15,   18,   19,   20,   21,   22,   23,
   24,   25,   26,   27,   28,    0,    0,    0,    0,    0,
    0,    0,    0,   17,    0,    0,    0,    0,   49,   52,
    0,    0,    0,    0,    0,    0,    0,    0,  125,    0,
    0,    0,   46,   48,   50,   51,    0,    0,   47,    0,
    6,   16,    0,    0,    0,    0,    0,    0,  136,   11,
    0,    0,    4,    9,    2,   54,   70,   69,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,  124,   94,
    0,    0,   53,    0,    0,   45,   44,   93,   87,   88,
   89,   90,    0,    0,   91,   92,    0,    0,    0,   67,
   66,  120,    0,    0,    0,    0,   82,    0,    0,    0,
  108,    0,    0,    0,    0,   99,    0,   96,  135,  137,
    0,    8,    1,   65,    0,    0,   63,    0,    0,    0,
  117,   38,   39,    0,   37,  126,  123,   95,    0,    0,
   33,    0,   34,    0,    0,   43,   40,   42,   41,    0,
  119,    0,   81,    0,   79,    0,    0,   76,   29,    0,
    0,    0,    0,  105,    0,    0,   98,  139,    0,  133,
   64,  114,   56,    0,    0,   36,    0,    0,   85,  116,
   78,    0,   80,    0,   75,    0,   73,    0,    0,  104,
  113,  112,    0,  110,    0,    0,    0,  138,    0,   57,
   83,    0,    0,   77,   72,    0,   74,    0,    0,  103,
  111,    0,    0,  107,    0,    0,    0,    0,  130,   71,
  102,    0,  101,  106,   60,    0,  129,  128,    0,    0,
  100,  127,  131,  132,
};
final static short yydgoto[] = {                          4,
   13,   14,   15,   16,   17,   18,   19,   20,   21,   22,
   23,   24,   25,   26,   51,  112,   52,   53,   54,   55,
   56,  139,  140,   82,   57,   58,  107,   91,   27,   66,
  125,  203,  204,  229,   28,  131,
};
final static short yysindex[] = {                       -89,
  331,  500,  640,    0,   -7,   12,   33,   37,  -36,  418,
    0,    0,  522,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0, -231,  -38,  -29,  -54,  662,
  559,  -23,  569,    0, -168,  -12,  428,   11,    0,    0,
  418,  434,  -66,  473, -141,  192,  283,  283,    0, -231,
  348,   32,    0,    0,    0,    0,   22,  396,    0,  288,
    0,    0,  428,   56,  -27,   -6, -134, -118,    0,    0,
   -9,  594,    0,    0,    0,    0,    0,    0, -117,   22,
   90,  134,   54,  412,   22,  138,  128,   57,    0,    0,
  312,   32,    0,  348,  -64,    0,    0,    0,    0,    0,
    0,    0,  450,  467,    0,    0,  428,  306,  384,    0,
    0,    0,  418,   22,  265,   22,    0,  -51,   35,  -20,
    0, -117,   76,  -57,  -22,    0,  -56,    0,    0,    0,
  -41,    0,    0,    0,   22,   22,    0,   90,   67,  -67,
    0,    0,    0,  163,    0,    0,    0,    0,  428,  171,
    0,   32,    0,   32,  401,    0,    0,    0,    0,   22,
    0,   22,    0,   45,    0,  376,   22,    0,    0,   97,
   78,    2,  327,    0,  107, -112,    0,    0,   29,    0,
    0,    0,    0,  428,    3,    0,  453, -103,    0,    0,
    0,   22,    0,   22,    0,  222,    0,  327,  154,    0,
    0,    0, -113,    0,  327, -117,   43,    0,   31,    0,
    0,  297,  248,    0,    0,   22,    0,  489,  327,    0,
    0,  596,   55,    0,   58,  262,  287,  -70,    0,    0,
    0,  610,    0,    0,    0,  287,    0,    0,  292,  293,
    0,    0,    0,    0,
};
final static short yyrindex[] = {                         0,
    0,    0,    0,    0,    0,  -44,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,   42,    0,    0,  336,    0,
  337,  345,  352,    0,    0,    0,    0,    1,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,  442,
    0,   23,    0,    0,    0,    0,  117,    0,    0,    0,
    0,    0,    0,   -3,    0,    0,    0,    0,    0,    0,
  357,  358,    0,    0,    0,    0,    0,    0,    0,    0,
  307,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,   47,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,  157,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,  -31,    0,  101,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,   69,    0,   93,  115,    0,    0,    0,    0,  165,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,  145,    0,  143,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,  170,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,  270,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,
};
final static short yygindex[] = {                         0,
   24,  539,  373,  695,    0,    0,    0,    0,    0,    0,
    0,    0,    0,  692,  803,  688,  -28,   -8,    0,    0,
    0,    0,  176,    0,   14,   10,  267, -163,   19,    0,
  249,  -21,  -71, -106,    0,    0,
};
final static int YYTABLESIZE=987;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                         55,
   55,   65,  179,   46,   34,   47,   44,  111,   45,  115,
   48,  220,  115,  123,   67,   92,   55,  111,  175,  212,
  170,  176,   35,   60,  213,   31,   33,   79,   80,   47,
   44,   68,   45,    3,   48,   34,   63,  127,   96,   97,
   97,   55,   55,   55,   55,   55,   32,   55,  226,   34,
   84,   34,  111,   72,   85,   97,   35,   35,   89,   55,
   55,   55,   55,   35,   95,   35,   35,   35,   30,  118,
   70,  114,   36,  108,  152,  154,   37,  103,  109,  104,
  111,   35,   35,  124,   35,  134,   43,   32,   76,   32,
   32,   32,   31,  111,  136,  120,  103,   95,  104,  157,
  159,   74,  134,  111,   95,   32,   32,  183,   32,   30,
  184,   30,   30,   30,   86,  132,  122,   93,  199,  237,
  238,  176,    6,   55,  164,   55,  160,   30,   30,  242,
   30,  221,  103,   31,  104,   31,   31,   31,  124,  130,
  172,   59,   84,    6,   59,   35,  221,   35,    7,    8,
  221,   31,   31,    6,   31,   10,  121,   11,    7,    8,
  221,   11,   11,  206,  118,   10,    1,    2,  145,   32,
  103,   32,  104,   86,  135,  196,  218,   79,  143,   47,
   44,  146,   45,  222,   48,   58,  239,  240,   58,   88,
    6,   30,  150,   30,  207,    7,    8,  232,  173,  174,
  177,   84,   10,  186,  110,  103,  185,  104,  166,  167,
   62,  188,   55,   62,  110,   31,  178,   31,   64,  198,
   38,   39,   40,   55,  223,    7,    8,    6,  121,  205,
   41,   79,   10,   47,   44,  121,   45,   86,   48,   86,
   42,  122,  115,   77,   38,   39,   40,   11,  122,  110,
  126,   78,   97,   97,   11,  122,   55,   55,  200,  210,
   55,   55,   55,   55,   42,   84,   55,   84,   55,   55,
   55,   55,   55,   55,   55,   55,  219,  110,   35,   35,
  111,  121,   35,   35,   35,   35,  208,  228,   35,  118,
  110,   35,   35,   35,   35,   35,   35,   35,  134,  224,
  110,  228,   32,   32,  225,  192,   32,   32,   32,   32,
   61,  234,   32,   61,  235,   32,   32,   32,   32,   32,
   32,   32,   79,  111,   30,   30,  228,   45,   30,   30,
   30,   30,  243,  244,   30,   14,    7,   30,   30,   30,
   30,   30,   30,   30,   12,   79,  111,   68,   31,   31,
   45,    5,   31,   31,   31,   31,   10,    3,   31,  209,
  149,   31,   31,   31,   31,   31,   31,   31,  171,    0,
   86,   86,  227,  122,   86,   86,   86,   86,  122,  122,
   86,   49,  122,   86,    0,  122,  236,   43,    0,   86,
  103,  122,  104,  142,   38,   39,   40,    0,   84,   84,
    0,    0,   84,   84,   84,   84,    0,  105,   84,  106,
   43,   84,   11,  121,   42,   90,    0,   84,  121,  121,
    0,  118,  121,   79,    0,  121,  118,  118,   45,    0,
  118,  121,   49,  118,  111,   46,  147,   47,   44,  118,
   45,  189,   48,  103,    0,  104,    0,    0,   38,   39,
   40,   79,    0,   47,   44,   12,   45,   46,   48,   47,
   44,    0,   45,  148,   48,    0,   11,   79,   42,   47,
   44,    0,   45,   86,   48,   47,   44,  110,   45,    0,
   48,    0,  216,   47,   47,  134,   47,   49,   47,   79,
    0,   47,    0,  211,   45,  103,   48,  104,   43,    0,
    0,   47,  134,   47,    6,    0,   79,    0,   47,    7,
    8,   45,   79,   48,   47,    0,   10,   45,    6,   48,
  110,    6,    0,    7,    8,  162,    7,    8,    0,    0,
   10,    0,    0,   10,    0,    0,    0,    0,   49,   38,
   39,   40,    0,  110,    6,  201,    0,  115,  116,    7,
    8,   62,    0,    6,    0,    0,   10,    0,    7,    8,
   90,  156,   38,   39,   40,   10,    0,    0,    6,   62,
  201,   62,    0,    7,    8,  201,    0,  201,    0,    0,
   10,    0,    0,    6,   90,  148,    5,    6,    7,    8,
  201,  201,    7,    8,  201,   10,    9,    0,  148,   10,
    0,   11,    0,   98,  201,   11,    0,    0,    0,    0,
   62,    0,    0,  231,    0,    0,    0,   99,  100,  101,
  102,    0,   30,    0,    0,    0,    0,    0,    0,    0,
    0,  110,    6,    0,    0,    0,  194,    7,    8,  158,
   38,   39,   40,    0,   10,    0,   61,    0,    0,    0,
    0,    0,   38,   39,   40,    0,    0,    0,    0,    0,
    0,    0,  113,    0,    0,    0,    0,  137,   38,   39,
   40,    0,   42,    0,   38,   39,   40,    0,    0,    0,
    0,    0,    0,   73,   38,   39,   40,    0,   42,    0,
   38,   39,   40,   75,   42,    0,    0,   47,  134,    0,
   50,   59,    0,    0,   42,  151,   38,   39,   40,    0,
   42,   47,   47,   47,   47,    0,    0,    0,  133,   69,
  233,    0,  153,   38,   39,   40,    0,   59,   59,   38,
   39,   40,   59,   59,  241,   59,    0,   59,   59,   59,
    0,    0,    0,    0,    0,    6,    0,  117,    0,   59,
    7,    8,    0,  128,   59,   29,    6,   10,  129,    0,
    0,    7,    8,   11,    0,    9,    0,  134,   10,    0,
    0,    0,  141,    0,   11,   59,    0,   59,    6,    0,
    0,    0,    0,    7,    8,    0,    0,    9,    0,    0,
   10,    0,    0,    0,   59,   59,   11,    0,   59,   59,
   59,  161,  163,  165,   59,  168,  169,    0,    0,    0,
    0,    0,    0,    0,    0,    6,    0,    0,  180,    0,
    7,    8,  181,  182,    9,    6,    0,   10,    0,    0,
    7,    8,    0,   11,    9,    0,    0,   10,   81,   83,
   59,    0,    0,   11,   87,    0,    0,  190,   94,  191,
    6,  193,    6,  195,  197,    7,    8,    7,    8,    9,
    0,    0,   10,    0,   10,  119,    6,  202,   11,    0,
   11,    7,    8,    0,    0,   59,    0,    0,   10,  214,
    0,  215,    0,  217,   11,    0,  138,    0,  144,    0,
    0,    0,  202,    0,    0,   32,    6,  202,    0,  202,
    0,    7,    8,  230,    0,    9,    0,    0,   10,  155,
    0,    0,  202,  202,   11,    0,  202,   71,    6,    0,
    0,    0,    0,    7,    8,    0,  202,    9,    0,    0,
   10,    0,    0,    0,    0,    0,   11,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,  187,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,  138,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                         44,
    0,   40,   44,   40,   59,   42,   43,   59,   45,   41,
   47,  125,   44,   41,   44,   44,   61,   59,   41,  123,
   41,   44,    0,   10,  188,    2,    3,   40,   41,   42,
   43,   61,   45,  123,   47,   59,  268,   44,   47,   48,
   44,   41,   42,   43,   44,   45,    0,   47,  212,   59,
   40,   59,   59,   30,   41,   59,   46,   46,  125,   59,
   60,   61,   62,   41,   46,   43,   44,   45,    0,   60,
  125,   58,   40,   42,  103,  104,   40,   43,   47,   45,
   59,   59,   60,   65,   62,   44,  123,   41,  257,   43,
   44,   45,    0,   59,   41,   40,   43,   79,   45,  108,
  109,  125,   61,   59,   86,   59,   60,   41,   62,   41,
   44,   43,   44,   45,    0,  125,    0,  259,   41,  226,
  227,   44,  257,  123,  115,  125,  113,   59,   60,  236,
   62,  203,   43,   41,   45,   43,   44,   45,  120,  258,
  122,   41,    0,  257,   44,  123,  218,  125,  262,  263,
  222,   59,   60,  257,   62,  269,    0,  275,  262,  263,
  232,  275,  275,  276,    0,  269,  256,  257,   41,  123,
   43,  125,   45,   59,   41,  166,  198,   40,   41,   42,
   43,  125,   45,  205,   47,   41,  257,  258,   44,  256,
  257,  123,  257,  125,  176,  262,  263,  219,  123,  257,
  257,   59,  269,   41,  256,   43,  274,   45,  260,  261,
   41,   41,  257,   44,  256,  123,  258,  125,  257,  123,
  257,  258,  259,  268,  206,  262,  263,  257,  256,  123,
  267,   40,  269,   42,   43,  256,   45,  123,   47,  125,
  277,  125,  274,  256,  257,  258,  259,  275,  276,  256,
  257,  264,  256,  257,  275,  276,  256,  257,  257,  257,
  260,  261,  262,  263,  277,  123,  266,  125,  268,  269,
  270,  271,  272,  273,  274,  275,  123,  256,  256,  257,
   59,  125,  260,  261,  262,  263,  258,   40,  266,  125,
  256,  269,  270,  271,  272,  273,  274,  275,  257,  257,
  256,   40,  256,  257,  274,  261,  260,  261,  262,  263,
   41,  257,  266,   44,  257,  269,  270,  271,  272,  273,
  274,  275,   40,   59,  256,  257,   40,   45,  260,  261,
  262,  263,   41,   41,  266,    0,    0,  269,  270,  271,
  272,  273,  274,  275,    0,   40,   59,   41,  256,  257,
   45,    0,  260,  261,  262,  263,    0,    0,  266,  184,
   94,  269,  270,  271,  272,  273,  274,  275,  120,   -1,
  256,  257,  125,  257,  260,  261,  262,  263,  262,  263,
  266,    9,  266,  269,   -1,  269,  125,  123,   -1,  275,
   43,  275,   45,  256,  257,  258,  259,   -1,  256,  257,
   -1,   -1,  260,  261,  262,  263,   -1,   60,  266,   62,
  123,  269,  275,  257,  277,   43,   -1,  275,  262,  263,
   -1,  257,  266,   40,   -1,  269,  262,  263,   45,   -1,
  266,  275,   60,  269,   59,   40,  125,   42,   43,  275,
   45,   41,   47,   43,   -1,   45,   -1,   -1,  257,  258,
  259,   40,   -1,   42,   43,  125,   45,   40,   47,   42,
   43,   -1,   45,   91,   47,   -1,  275,   40,  277,   42,
   43,   -1,   45,   40,   47,   42,   43,  256,   45,   -1,
   47,   -1,  261,   42,   43,   44,   45,  115,   47,   40,
   -1,   42,   -1,   41,   45,   43,   47,   45,  123,   -1,
   -1,   60,   61,   62,  257,   -1,   40,   -1,   42,  262,
  263,   45,   40,   47,   42,   -1,  269,   45,  257,   47,
  256,  257,   -1,  262,  263,  261,  262,  263,   -1,   -1,
  269,   -1,   -1,  269,   -1,   -1,   -1,   -1,  166,  257,
  258,  259,   -1,  256,  257,  173,   -1,  260,  261,  262,
  263,   13,   -1,  257,   -1,   -1,  269,   -1,  262,  263,
  188,  256,  257,  258,  259,  269,   -1,   -1,  257,   31,
  198,   33,   -1,  262,  263,  203,   -1,  205,   -1,   -1,
  269,   -1,   -1,  257,  212,  213,  256,  257,  262,  263,
  218,  219,  262,  263,  222,  269,  266,   -1,  226,  269,
   -1,  275,   -1,  256,  232,  275,   -1,   -1,   -1,   -1,
   72,   -1,   -1,  125,   -1,   -1,   -1,  270,  271,  272,
  273,   -1,  123,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,  256,  257,   -1,   -1,   -1,  261,  262,  263,  256,
  257,  258,  259,   -1,  269,   -1,  125,   -1,   -1,   -1,
   -1,   -1,  257,  258,  259,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,  267,   -1,   -1,   -1,   -1,  256,  257,  258,
  259,   -1,  277,   -1,  257,  258,  259,   -1,   -1,   -1,
   -1,   -1,   -1,  125,  257,  258,  259,   -1,  277,   -1,
  257,  258,  259,  125,  277,   -1,   -1,  256,  257,   -1,
    9,   10,   -1,   -1,  277,  256,  257,  258,  259,   -1,
  277,  270,  271,  272,  273,   -1,   -1,   -1,  125,   28,
  125,   -1,  256,  257,  258,  259,   -1,   36,   37,  257,
  258,  259,   41,   42,  125,   44,   -1,   46,   47,   48,
   -1,   -1,   -1,   -1,   -1,  257,   -1,   60,   -1,   58,
  262,  263,   -1,   66,   63,  256,  257,  269,   67,   -1,
   -1,  262,  263,  275,   -1,  266,   -1,   80,  269,   -1,
   -1,   -1,   85,   -1,  275,   84,   -1,   86,  257,   -1,
   -1,   -1,   -1,  262,  263,   -1,   -1,  266,   -1,   -1,
  269,   -1,   -1,   -1,  103,  104,  275,   -1,  107,  108,
  109,  114,  115,  116,  113,  118,  119,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,  257,   -1,   -1,  131,   -1,
  262,  263,  135,  136,  266,  257,   -1,  269,   -1,   -1,
  262,  263,   -1,  275,  266,   -1,   -1,  269,   36,   37,
  149,   -1,   -1,  275,   42,   -1,   -1,  160,   46,  162,
  257,  164,  257,  166,  167,  262,  263,  262,  263,  266,
   -1,   -1,  269,   -1,  269,   63,  257,  173,  275,   -1,
  275,  262,  263,   -1,   -1,  184,   -1,   -1,  269,  192,
   -1,  194,   -1,  196,  275,   -1,   84,   -1,   86,   -1,
   -1,   -1,  198,   -1,   -1,  256,  257,  203,   -1,  205,
   -1,  262,  263,  216,   -1,  266,   -1,   -1,  269,  107,
   -1,   -1,  218,  219,  275,   -1,  222,  256,  257,   -1,
   -1,   -1,   -1,  262,  263,   -1,  232,  266,   -1,   -1,
  269,   -1,   -1,   -1,   -1,   -1,  275,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,  149,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,  184,
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
"if : IF cuerpo_condicion cuerpo_sentencia_ejecutable ELSE cuerpo_sentencia_ejecutable ENDIF punto_coma",
"if : IF cuerpo_condicion cuerpo_sentencia_ejecutable ELSE ENDIF punto_coma",
"if : IF cuerpo_condicion cuerpo_sentencia_ejecutable ENDIF punto_coma",
"if : IF cuerpo_condicion cuerpo_sentencia_ejecutable ELSE cuerpo_sentencia_ejecutable punto_coma",
"if : IF cuerpo_condicion cuerpo_sentencia_ejecutable ELSE punto_coma",
"if : IF cuerpo_condicion cuerpo_sentencia_ejecutable punto_coma",
"if : IF cuerpo_condicion ELSE cuerpo_sentencia_ejecutable ENDIF punto_coma",
"if : IF cuerpo_condicion ELSE ENDIF punto_coma",
"if : IF cuerpo_condicion ENDIF punto_coma",
"if : IF cuerpo_condicion ELSE cuerpo_sentencia_ejecutable punto_coma",
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
"funcion : tipo ID '(' parametros_formales ')' '{' lista_sentencia_funcion '}'",
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
"do : DO cuerpo_sentencia_ejecutable WHILE cuerpo_condicion punto_coma",
"do : DO WHILE cuerpo_condicion punto_coma",
"do : DO cuerpo_sentencia_ejecutable WHILE cuerpo_condicion",
"do : DO cuerpo_sentencia_ejecutable cuerpo_condicion punto_coma",
"do : DO cuerpo_condicion punto_coma",
"do : DO cuerpo_sentencia_ejecutable cuerpo_condicion",
"do : DO cuerpo_condicion",
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

//#line 291 "grama.y"
private ArrayList<Token> tokens = new ArrayList<>();
private ArrayList<String> estructurasDetectadas = new ArrayList<>();
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
//#line 682 "Parser.java"
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
//#line 36 "grama.y"
{yyerror("error en sentencia");}
break;
case 21:
//#line 46 "grama.y"
{addEstructura("declaracion de variable");}
break;
case 22:
//#line 47 "grama.y"
{addEstructura("declaracion de funcion");}
break;
case 23:
//#line 51 "grama.y"
{addEstructura("asignacion");}
break;
case 24:
//#line 52 "grama.y"
{addEstructura("sentencia if");}
break;
case 25:
//#line 53 "grama.y"
{addEstructura("print");}
break;
case 26:
//#line 54 "grama.y"
{addEstructura("return");}
break;
case 27:
//#line 55 "grama.y"
{addEstructura("asignacion multilpe");}
break;
case 28:
//#line 59 "grama.y"
{addEstructura("sentencia do while");}
break;
case 32:
//#line 70 "grama.y"
{yyerror("falta operando a izquierda de +");}
break;
case 33:
//#line 71 "grama.y"
{yyerror("falta operando a derecha de +");}
break;
case 34:
//#line 72 "grama.y"
{yyerror("falta operando a derecha de -");}
break;
case 36:
//#line 74 "grama.y"
{addEstructura("trunc");}
break;
case 37:
//#line 75 "grama.y"
{addEstructura("trunc");yyerror("falta abrir parentesis en trunc");}
break;
case 38:
//#line 76 "grama.y"
{addEstructura("trunc");yyerror("falta cerrar parentesis en trunc");yyerrflag=0;}
break;
case 39:
//#line 77 "grama.y"
{addEstructura("trunc");yyerror("falta argumento en trunc");}
break;
case 42:
//#line 83 "grama.y"
{yyerror("falta operando a derecha de /");}
break;
case 43:
//#line 84 "grama.y"
{yyerror("falta operando a derecha de *");}
break;
case 44:
//#line 85 "grama.y"
{yyerror("falta operando a izquierda de /");}
break;
case 45:
//#line 86 "grama.y"
{yyerror("falta operando a izquierda de *");}
break;
case 50:
//#line 94 "grama.y"
{addEstructura("lambda");}
break;
case 52:
//#line 99 "grama.y"
{check_rango(val_peek(0).sval);}
break;
case 53:
//#line 100 "grama.y"
{check_rango("-"+val_peek(0).sval);}
break;
case 56:
//#line 110 "grama.y"
{addEstructura("invocacion a funcion");}
break;
case 58:
//#line 115 "grama.y"
{yyerror("Falta parametro formal");}
break;
case 59:
//#line 116 "grama.y"
{yyerror("Falta flecha y parametro formal");}
break;
case 61:
//#line 118 "grama.y"
{yyerror("Falta parametro formal");}
break;
case 62:
//#line 119 "grama.y"
{yyerror("Falta flecha y parametro formal");}
break;
case 63:
//#line 120 "grama.y"
{yyerror("error en parametros de invocacion");}
break;
case 65:
//#line 125 "grama.y"
{yyerror("falta argumento en print");}
break;
case 67:
//#line 130 "grama.y"
{yyerror("falta ;");}
break;
case 70:
//#line 136 "grama.y"
{yyerror("argumento invalido en print");}
break;
case 72:
//#line 141 "grama.y"
{yyerror("no hay sentencias en else");}
break;
case 73:
//#line 142 "grama.y"
{}
break;
case 74:
//#line 144 "grama.y"
{ yyerror("falta endif");}
break;
case 75:
//#line 145 "grama.y"
{ yyerror("falta endif");yyerror("no hay sentencias en else");}
break;
case 76:
//#line 146 "grama.y"
{ yyerror("falta endif");}
break;
case 77:
//#line 149 "grama.y"
{yyerror("no hay sentencias en then");}
break;
case 78:
//#line 150 "grama.y"
{yyerror("no hay sentencias en then");yyerror("no hay sentencias en else");}
break;
case 79:
//#line 151 "grama.y"
{yyerror("no hay sentencias en then");}
break;
case 80:
//#line 154 "grama.y"
{yyerror("no hay sentencias en then");yyerror("falta endif");}
break;
case 81:
//#line 155 "grama.y"
{yyerror("no hay sentencias en then");yyerror("no hay sentencias en else");yyerror("falta endif");}
break;
case 82:
//#line 156 "grama.y"
{yyerror("no hay sentencias en then");yyerror("falta endif");}
break;
case 84:
//#line 161 "grama.y"
{yyerror("falta cerrar parentesis");}
break;
case 85:
//#line 162 "grama.y"
{yyerror("falta abrir parentesis");}
break;
case 86:
//#line 163 "grama.y"
{yyerror("faltan parentesis");}
break;
case 93:
//#line 174 "grama.y"
{yyerror("falta comparador");}
break;
case 99:
//#line 191 "grama.y"
{yyerror("falta , en lista de variables");}
break;
case 101:
//#line 198 "grama.y"
{ yyerror("falta declarar nombre de funcion");}
break;
case 102:
//#line 199 "grama.y"
{yyerror("faltan parametros formales");}
break;
case 103:
//#line 200 "grama.y"
{yyerror("falta declarar nombre de funcion"); yyerror("faltan parametros formales");}
break;
case 108:
//#line 208 "grama.y"
{yyerror("error en parametro formal");}
break;
case 114:
//#line 227 "grama.y"
{addEstructura("return");}
break;
case 116:
//#line 240 "grama.y"
{}
break;
case 117:
//#line 241 "grama.y"
{yyerror("falta cuerpo sentencias");}
break;
case 118:
//#line 242 "grama.y"
{yyerror("falta de ;");}
break;
case 119:
//#line 244 "grama.y"
{ yyerror("falta while");}
break;
case 120:
//#line 245 "grama.y"
{yyerror("falta cuerpo sentencias"); yyerror("falta while");}
break;
case 121:
//#line 246 "grama.y"
{yyerror("falta de ) en condicion");yyerror("falta de ;");yyerror("falta while");}
break;
case 122:
//#line 247 "grama.y"
{yyerror("falta de ) en condicion");yyerror("falta de ;");yyerror("falta cuerpo sentencias");yyerror("falta while");}
break;
case 124:
//#line 252 "grama.y"
{yyerror("no hay sentencias dentro de las llaves");}
break;
case 126:
//#line 254 "grama.y"
{yyerror("Error en sentencia");}
break;
case 128:
//#line 260 "grama.y"
{ yyerror("falta abrir llave en cuerpo de sentencia lambda");}
break;
case 129:
//#line 261 "grama.y"
{yyerror("falta cerra llave en cuerpo de sentencia lambda");}
break;
case 130:
//#line 262 "grama.y"
{ yyerror("faltan llaves en cuerpo de sentencia lambda");}
break;
case 136:
//#line 278 "grama.y"
{yyerror("falta , en lista de variables");}
break;
case 139:
//#line 284 "grama.y"
{yyerror("falta , en lista de constantes");}
break;
//#line 1151 "Parser.java"
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
