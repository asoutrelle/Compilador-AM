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
   20,   20,   20,   20,   22,   22,   17,   17,   24,   21,
   25,   25,   25,   25,   25,   25,   25,   26,   10,   10,
   16,   16,   27,   27,   27,    9,    9,    9,    9,    9,
    9,    9,    9,    9,    9,    9,    9,   30,   32,   31,
   28,   28,   28,   28,   33,   33,   33,   33,   33,   33,
   33,   34,   34,   36,    7,    7,    7,    7,    7,   37,
   37,   35,   35,   35,   35,   35,   14,   38,   38,   39,
   39,   11,   13,   13,   13,   13,   13,   13,   13,   40,
   41,   42,   29,   29,   29,   29,   23,   23,   23,   23,
   44,   44,   43,   12,   45,   45,   45,   46,   46,   46,
};
final static short yylen[] = {                            2,
    4,    3,    3,    3,    2,    3,    2,    4,    3,    3,
    3,    2,    2,    2,    1,    2,    2,    1,    1,    1,
    1,    1,    1,    1,    1,    1,    1,    1,    3,    1,
    3,    2,    4,    3,    3,    2,    3,    3,    1,    3,
    3,    3,    3,    2,    2,    1,    1,    1,    1,    4,
    3,    3,    3,    1,    1,    2,    3,    1,    0,    5,
    3,    2,    1,    5,    4,    3,    1,    1,    5,    4,
    1,    1,    1,    1,    1,    9,    7,    6,    8,    6,
    5,    8,    5,    4,    6,    4,    3,    0,    0,    0,
    5,    4,    4,    3,    1,    1,    1,    1,    1,    1,
    1,    1,    2,    0,    7,    5,    5,    4,    5,    3,
    2,    3,    2,    5,    4,    1,    1,    1,    2,    1,
    1,    5,    8,    5,    5,    5,    4,    4,    3,    0,
    0,    0,    3,    2,    1,    3,    8,    7,    7,    6,
    3,    3,    1,    4,    1,    3,    2,    1,    3,    2,
};
final static short yydefred[] = {                         0,
    0,    0,    0,    0,    0,    0,    0,    0,  130,    0,
  117,   13,    0,   15,   18,   19,   20,   21,   22,   23,
   24,   25,   26,   27,   28,    0,    0,    0,    0,    0,
    0,    0,    0,   17,    0,    0,    0,    0,    0,    0,
   49,    0,    0,    0,    0,    0,   47,    0,    0,   46,
   48,   54,    0,    6,   16,    0,    0,    0,    0,    0,
    0,    0,  147,   11,    0,    0,    4,    9,    2,  116,
    0,    0,    0,   57,   75,   74,    0,    0,    0,    0,
    0,    0,    0,  135,    0,    0,    0,    0,   55,    0,
    0,    0,    0,   45,   44,    0,    0,  101,   95,   96,
   97,   98,    0,    0,   99,  100,    0,    0,    0,   72,
    0,    0,   71,   87,    0,    0,    0,    0,   32,    0,
   29,    0,  146,  148,    0,    8,    1,    0,  113,    0,
    0,   70,    0,    0,    0,    0,  134,  102,    0,  127,
    0,    0,    0,   56,   52,   53,    0,   51,  143,    0,
    0,   37,    0,   38,    0,    0,   43,   40,   42,   41,
    0,   86,    0,   84,    0,    0,    0,    0,    0,  108,
    0,   31,   33,  150,    0,  144,  112,    0,    0,  109,
   69,  122,  124,  136,  133,  103,    0,  126,   67,    0,
    0,    0,   50,    0,    0,   93,   83,    0,    0,    0,
   81,  107,    0,  111,  121,  120,    0,  118,  106,  149,
    0,  115,  132,    0,   60,    0,    0,    0,   91,   85,
    0,    0,   80,    0,   78,    0,  110,  119,  114,    0,
    0,   61,    0,    0,    0,  140,    0,   77,    0,  105,
  123,    0,    0,  139,  138,    0,    0,   82,    0,   79,
   64,  137,  141,  142,   76,
};
final static short yydgoto[] = {                          4,
   13,   14,   15,   16,   17,   18,   19,   20,   21,   22,
   23,   24,   25,   26,   58,  114,   27,   48,   49,   50,
   51,   92,   52,   88,  191,  192,   80,   53,   87,  165,
  163,  166,  107,  139,   73,  168,  170,  207,  208,   39,
  213,  230,  150,  236,   28,  125,
};
final static short yysindex[] = {                       -98,
  373,  324,  477,    0,  -35,  104,   -7,   25,    0,   57,
    0,    0,  334,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,  -40, -193,  -34,  -27,  488,
  381,  -23,  445,    0, -134, -173,  -28,  144,  -36,   43,
    0,  -11,  153,  167,  167,  138,    0,  398,    7,    0,
    0,    0,  228,    0,    0,   61,  -32,  101,  144,   43,
 -127, -119,    0,    0,  -20,  453,    0,    0,    0,    0,
 -108,  -78,    9,    0,    0,    0, -108,  -43,   51,  149,
   31,   57,  356,    0, -193,  -43,  -12,  166,    0,  -58,
  236,  170,    7,    0,    0,  -49,  398,    0,    0,    0,
    0,    0,  259,  305,    0,    0,  144,  131,  179,    0,
  -51,  -43,    0,    0,    0,  184,  114,   67,    0,  -10,
    0,   90,    0,    0,   -4,    0,    0,    2,    0,  -87,
  114,    0,  -43,  -43,  -43,  135,    0,    0,  431,    0,
   57,  -43,  192,    0,    0,    0,  237,    0,    0,  247,
  144,    0,    7,    0,    7,   82,    0,    0,    0,    0,
  -43,    0, -101,    0,   63,  -41,  114, -134,  326,    0,
  114,    0,    0,    0,   53,    0,    0, -108,   70,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,   51,
  110,   54,    0,  268,  132,    0,    0,  -43,   44,  -43,
    0,    0,  125,    0,    0,    0,  389,    0,    0,    0,
   80,    0,    0,  144,    0,  112,  176,  211,    0,    0,
  100,  -43,    0, -101,    0,  114,    0,    0,    0,  -43,
   96,    0,  272,  342,  -60,    0,  -43,    0,  137,    0,
    0,  150,  342,    0,    0,  353,  375,    0,  -43,    0,
    0,    0,    0,    0,    0,
};
final static short yyrindex[] = {                         0,
    0,    0,    0,    0,    0,   34,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,  103,    0,  417,    0,
  429,  461,  463,    0,    0,    0,    0,    0,    0,    1,
    0,    0,    0,    0,    0,    0,    0,    0,   23,    0,
    0,    0,    0,    0,    0,  289,    0,    0,    0,   34,
    0,    0,    0,    0,  467,  476,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,  401,    0,
    0,    0,    0,    0,  410,  157,    0,    0,    0,    0,
    0,    0,   47,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
  415,    0,    0,    0,  -57,  -93,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,  165,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,   69,    0,   93,  115,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,  117,    0,    0,  -24,
    0,  168,    0,    0,  143,    0,    0,  216,  415,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,  212,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
  421,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,  442,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,
};
final static short yygindex[] = {                         0,
   49,  433,  468, -148,    0,    0,    0,    0,    0,    0,
    0,    0,    0,  660,    0,  499,  465,  441,   15,   12,
    0,  391,    0,    0,    0,  280,    0,   -1,  161,    0,
  297,  303,  408, -191,  -52,    0,  -94,    0,  306,    0,
    0,    0,    0, -163,    0,    0,
};
final static int YYTABLESIZE=838;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                         57,
   58,   89,  218,   46,  118,   44,   43,  113,  117,   61,
   45,   77,   78,   44,   43,  113,   68,  113,   45,   68,
  206,   83,   39,   34,    3,  233,   62,   46,   91,   44,
   43,   34,   37,   90,   45,   34,  180,   86,   34,  175,
   59,   58,   58,   58,   58,   58,   36,   58,  108,  131,
   31,   33,  130,  109,  113,   94,   95,   93,  206,   58,
   58,   58,   58,   39,   38,   39,   39,   39,   34,  244,
  245,  134,  202,  103,   59,  104,  209,   58,   66,  252,
  135,   39,   39,   74,   39,  142,   83,   36,   36,   36,
   36,   36,   35,  103,   58,  104,   46,   64,   44,   43,
  116,   68,  113,   45,  126,   36,   36,  171,   36,   34,
  130,   34,   34,   34,   94,  203,  125,  153,  155,  158,
  160,   70,  196,   58,  103,   58,  104,   34,   34,   60,
   34,  240,  103,   35,  104,   35,   35,   35,  124,  187,
   11,   71,   92,   35,  120,   39,  145,   39,  113,   36,
  215,   35,   35,  214,   35,   60,  129,    1,    2,  113,
    7,    8,  104,  145,  128,  226,   11,   10,  130,   36,
   77,   36,  219,   94,  103,  131,  104,   77,  129,   44,
   43,  104,  104,   77,   45,   44,   43,   11,  178,  133,
   45,   34,   77,   34,   44,  113,  246,  247,   89,   45,
  144,   92,   88,   89,  110,  143,   77,  149,   63,  161,
  148,   63,  110,  115,  110,   35,   56,   35,   77,  200,
   40,   41,   60,   70,  167,    7,    8,   75,   40,   41,
   82,   77,   10,   44,   43,   76,  169,   94,   45,   94,
   42,  125,   11,   71,   40,   41,  172,   89,   42,   68,
  235,  110,   62,  174,  141,   62,   58,   58,  177,  184,
   58,   58,   58,   58,   42,   92,   58,   92,   58,   58,
   58,   58,   58,   58,   58,   58,  146,  193,   39,   39,
   90,  129,   39,   39,   39,   39,  113,  194,   39,  128,
   58,   39,   39,   39,   39,   39,   39,   39,   77,  110,
   44,   58,   36,   36,  222,   45,   36,   36,   36,   36,
  210,  235,   36,   40,   41,   36,   36,   36,   36,   36,
   36,   36,  199,  198,   34,   34,  212,  216,   34,   34,
   34,   34,   30,   42,   34,  234,  229,   34,   34,   34,
   34,   34,   34,   34,   77,  110,   44,   30,   35,   35,
   83,   45,   35,   35,   35,   35,  110,  119,   35,  145,
  237,   35,   35,   35,   35,   35,   35,   35,  232,  242,
   94,   94,  131,  125,   94,   94,   94,   94,  125,  125,
   94,  235,  125,   94,  239,  125,  157,   40,   41,   94,
  217,  125,  110,  253,   40,   41,  243,  249,   92,   92,
   40,   41,   92,   92,   92,   92,  251,   42,   92,   40,
   41,   92,   11,  129,   42,  254,   14,   92,  129,  129,
   42,  128,  129,   40,   41,  129,  128,  128,    7,   42,
  128,  129,   60,  128,  159,   40,   41,    7,    8,  128,
  103,   73,  104,   42,   10,   55,   30,  189,   40,   41,
  204,   47,   47,  145,   47,   42,   47,  105,   54,  106,
   12,   66,    5,   55,   66,   55,   10,   60,   42,   47,
  145,   47,    7,    8,   47,    3,   89,   79,   81,   10,
  137,  147,   65,  110,   60,   65,   97,  111,  112,    7,
    8,  145,   63,  231,   89,  224,   10,   12,   55,  122,
  221,   47,   47,   85,  151,   67,   84,   47,   47,   47,
   47,    0,  228,  227,  152,   40,   41,    0,    0,    0,
   84,    0,    0,   47,   60,  123,    0,    0,   60,    7,
    8,    0,    0,    7,    8,   42,   10,   90,    0,    0,
   10,    0,    0,    0,   30,   30,   47,  156,    0,    0,
  138,   47,    0,    0,    0,  185,  121,    0,    0,    0,
  154,   40,   41,    0,    0,    0,    0,   47,   47,   69,
    0,   47,   47,   47,    0,    0,  132,  127,    0,   29,
    6,   42,    6,  190,  140,    7,    8,    7,    8,    9,
    6,  195,   10,    0,   10,    7,    8,    0,   11,    9,
   11,    0,   10,    0,    0,   47,  186,   47,   11,  162,
  164,  136,   60,    0,    0,   47,    0,    7,    8,    0,
  173,    0,    0,  176,   10,    0,    0,    0,    5,    6,
   84,  181,  182,  183,    7,    8,  205,    6,    9,    0,
  188,   10,    7,    8,    0,    6,    9,   11,    0,   10,
    7,    8,    0,   98,  190,   11,    0,   10,    0,  197,
    0,  138,    0,   11,  201,   47,  145,   99,  100,  101,
  102,   90,    0,    0,  205,    0,   90,   90,   47,   47,
   47,   47,   47,   90,  138,  186,    0,   60,    0,    0,
    0,   84,    7,    8,   72,    0,  220,  223,  225,   10,
  186,    6,    0,    0,    0,   96,    7,    8,    0,    6,
    9,    0,    0,   10,    7,    8,   72,    0,    9,   11,
  238,   10,    0,    0,    0,    0,    0,   11,  241,    0,
  128,    0,   32,    6,    0,  248,   96,  250,    7,    8,
    0,    0,    9,   65,    6,   10,    0,  255,    0,    7,
    8,   11,    0,    9,    0,    0,   10,    0,    0,    0,
    0,    0,   11,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,  179,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,   72,    0,    0,
    0,    0,    0,    0,    0,    0,    0,  211,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                         40,
    0,   59,  194,   40,   57,   42,   43,   59,   41,   44,
   47,   40,   41,   42,   43,   59,   41,   59,   47,   44,
  169,  123,    0,   59,  123,  217,   61,   40,   40,   42,
   43,   59,   40,   45,   47,   59,  131,   39,   59,   44,
   40,   41,   42,   43,   44,   45,    0,   47,   42,   41,
    2,    3,   44,   47,   59,   44,   45,   43,  207,   59,
   60,   61,   62,   41,   40,   43,   44,   45,    0,  233,
  234,   41,  167,   43,  268,   45,  171,   44,   30,  243,
   82,   59,   60,  257,   62,   87,  123,   41,   46,   43,
   44,   45,    0,   43,   61,   45,   40,  125,   42,   43,
   40,  125,   59,   47,  125,   59,   60,   41,   62,   41,
   44,   43,   44,   45,    0,  168,    0,  103,  104,  108,
  109,  256,   41,  123,   43,  125,   45,   59,   60,  257,
   62,  226,   43,   41,   45,   43,   44,   45,  258,  141,
  275,  276,    0,   40,   44,  123,   44,  125,   59,   46,
   41,   59,   60,   44,   62,  257,    0,  256,  257,   59,
  262,  263,  256,   61,    0,   41,  275,  269,   44,  123,
   40,  125,   41,   59,   43,   59,   45,   40,  257,   42,
   43,  275,  276,   40,   47,   42,   43,  275,  276,   41,
   47,  123,   40,  125,   42,   59,  257,  258,  256,   47,
  259,   59,  260,  261,  256,   40,   40,  257,   41,  261,
   41,   44,  256,   53,  256,  123,  257,  125,   40,  261,
  257,  258,  257,  256,   41,  262,  263,  256,  257,  258,
  267,   40,  269,   42,   43,  264,  123,  123,   47,  125,
  277,  125,  275,  276,  257,  258,  257,  259,  277,  274,
   40,  256,   41,  258,  267,   44,  256,  257,  257,  125,
  260,  261,  262,  263,  277,  123,  266,  125,  268,  269,
  270,  271,  272,  273,  274,  275,   41,   41,  256,  257,
   45,  125,  260,  261,  262,  263,   59,   41,  266,  125,
  257,  269,  270,  271,  272,  273,  274,  275,   40,  256,
   42,  268,  256,  257,  261,   47,  260,  261,  262,  263,
  258,   40,  266,  257,  258,  269,  270,  271,  272,  273,
  274,  275,  260,  163,  256,  257,  257,  274,  260,  261,
  262,  263,   44,  277,  266,  125,  257,  269,  270,  271,
  272,  273,  274,  275,   40,  256,   42,   59,  256,  257,
  123,   47,  260,  261,  262,  263,  256,  257,  266,  257,
  261,  269,  270,  271,  272,  273,  274,  275,  257,  274,
  256,  257,  256,  257,  260,  261,  262,  263,  262,  263,
  266,   40,  266,  269,  224,  269,  256,  257,  258,  275,
  123,  275,  256,   41,  257,  258,  125,  261,  256,  257,
  257,  258,  260,  261,  262,  263,  257,  277,  266,  257,
  258,  269,  275,  257,  277,   41,    0,  275,  262,  263,
  277,  257,  266,  257,  258,  269,  262,  263,    0,  277,
  266,  275,  257,  269,  256,  257,  258,  262,  263,  275,
   43,   41,   45,  277,  269,   13,  123,  256,  257,  258,
  125,   42,   43,   44,   45,  277,   47,   60,  125,   62,
    0,   41,    0,   31,   44,   33,    0,  257,  277,   60,
   61,   62,  262,  263,   10,    0,  261,   37,   38,  269,
  125,   91,   41,  256,  257,   44,   46,  260,  261,  262,
  263,  256,   28,  214,  259,  199,  269,  125,   66,   59,
  198,   37,   38,   39,   97,  125,   39,   43,   44,   45,
   46,   -1,  207,  125,  256,  257,  258,   -1,   -1,   -1,
   53,   -1,   -1,   59,  257,   61,   -1,   -1,  257,  262,
  263,   -1,   -1,  262,  263,  277,  269,  123,   -1,   -1,
  269,   -1,   -1,   -1,  256,  257,   82,  107,   -1,   -1,
   83,   87,   -1,   -1,   -1,  125,   58,   -1,   -1,   -1,
  256,  257,  258,   -1,   -1,   -1,   -1,  103,  104,  125,
   -1,  107,  108,  109,   -1,   -1,   78,  125,   -1,  256,
  257,  277,  257,  143,   86,  262,  263,  262,  263,  266,
  257,  151,  269,   -1,  269,  262,  263,   -1,  275,  266,
  275,   -1,  269,   -1,   -1,  141,  139,  143,  275,  111,
  112,  256,  257,   -1,   -1,  151,   -1,  262,  263,   -1,
  122,   -1,   -1,  125,  269,   -1,   -1,   -1,  256,  257,
  163,  133,  134,  135,  262,  263,  169,  257,  266,   -1,
  142,  269,  262,  263,   -1,  257,  266,  275,   -1,  269,
  262,  263,   -1,  256,  214,  275,   -1,  269,   -1,  161,
   -1,  194,   -1,  275,  166,  256,  257,  270,  271,  272,
  273,  257,   -1,   -1,  207,   -1,  262,  263,  214,  270,
  271,  272,  273,  269,  217,  218,   -1,  257,   -1,   -1,
   -1,  224,  262,  263,   35,   -1,  198,  199,  200,  269,
  233,  257,   -1,   -1,   -1,   46,  262,  263,   -1,  257,
  266,   -1,   -1,  269,  262,  263,   57,   -1,  266,  275,
  222,  269,   -1,   -1,   -1,   -1,   -1,  275,  230,   -1,
   71,   -1,  256,  257,   -1,  237,   77,  239,  262,  263,
   -1,   -1,  266,  256,  257,  269,   -1,  249,   -1,  262,
  263,  275,   -1,  266,   -1,   -1,  269,   -1,   -1,   -1,
   -1,   -1,  275,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,  130,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,  168,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,  178,
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

//#line 588 "grama.y"
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
//#line 759 "Parser.java"
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
        /*if(TablaDeSimbolos.esCompatible($1.sval,$3.sval, Compilador.getAmbito())){*/
        /*} else yyerror("Los tipos de las variables no coinciden");*/


        String ambito = Compilador.getAmbito();
        String var = val_peek(3).sval;
        crearTerceto(":=", var, val_peek(1).sval);
    }
break;
case 34:
//#line 115 "grama.y"
{
        crearTerceto("+", val_peek(2).sval, val_peek(0).sval);
        int t = Compilador.tercetos.size() - 1;
        yyval=new ParserVal("[" + t + "]");
        TablaDeSimbolos.agregarVarAux(t);
    }
break;
case 35:
//#line 122 "grama.y"
{
        crearTerceto("-", val_peek(2).sval, val_peek(0).sval);
        int t = Compilador.tercetos.size() - 1;
        yyval=new ParserVal("[" + t + "]");
        TablaDeSimbolos.agregarVarAux(t);
    }
break;
case 36:
//#line 128 "grama.y"
{yyerror("falta operando a izquierda de +");}
break;
case 37:
//#line 129 "grama.y"
{yyerror("falta operando a derecha de +");}
break;
case 38:
//#line 130 "grama.y"
{yyerror("falta operando a derecha de -");}
break;
case 40:
//#line 136 "grama.y"
{
        crearTerceto("*", val_peek(2).sval, val_peek(0).sval);
        int t = Compilador.tercetos.size() - 1;
        yyval=new ParserVal("[" + t + "]");
        TablaDeSimbolos.agregarVarAux(t);
    }
break;
case 41:
//#line 143 "grama.y"
{
        crearTerceto("/", val_peek(2).sval, val_peek(0).sval);
        int t = Compilador.tercetos.size() - 1;
        yyval=new ParserVal("[" + t + "]");
        TablaDeSimbolos.agregarVarAux(t);
    }
break;
case 42:
//#line 149 "grama.y"
{yyerror("falta operando a derecha de /");}
break;
case 43:
//#line 150 "grama.y"
{yyerror("falta operando a derecha de *");}
break;
case 44:
//#line 151 "grama.y"
{yyerror("falta operando a izquierda de /");}
break;
case 45:
//#line 152 "grama.y"
{yyerror("falta operando a izquierda de *");}
break;
case 49:
//#line 159 "grama.y"
{yyval=new ParserVal(val_peek(0).sval);}
break;
case 50:
//#line 161 "grama.y"
{
        addEstructura("trunc");
        yyval=new ParserVal(truncar(val_peek(1).sval));
    }
break;
case 51:
//#line 165 "grama.y"
{addEstructura("trunc");yyerror("falta abrir parentesis en trunc");}
break;
case 52:
//#line 166 "grama.y"
{addEstructura("trunc");yyerror("falta cerrar parentesis en trunc");yyerrflag=0;}
break;
case 53:
//#line 167 "grama.y"
{addEstructura("trunc");yyerror("falta argumento en trunc");}
break;
case 54:
//#line 168 "grama.y"
{addEstructura("lambda");}
break;
case 55:
//#line 172 "grama.y"
{check_rango(val_peek(0).sval); yyval=new ParserVal(val_peek(0).sval);}
break;
case 56:
//#line 173 "grama.y"
{check_rango("-"+val_peek(0).sval); yyval=new ParserVal("-"+val_peek(1).sval);}
break;
case 57:
//#line 178 "grama.y"
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
case 58:
//#line 189 "grama.y"
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
case 59:
//#line 204 "grama.y"
{
        nombreFuncion = val_peek(0).sval;
    }
break;
case 60:
//#line 208 "grama.y"
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
case 61:
//#line 239 "grama.y"
{
        String ambito = Compilador.getAmbito()+":"+nombreFuncion;
        String var = val_peek(0).sval;
        if (!TablaDeSimbolos.parametroDeclarado(var, ambito)){
            yyerror("El parametro " + var + " no esta declarado en la funcion "+nombreFuncion);
        }
        if (!TablaDeSimbolos.esParametroValido(val_peek(2).sval, var,Compilador.getAmbito(), nombreFuncion)){
            yyerror("No se puede pasar una constante como a un parametro formal con semantica de copia resultado");
        }
        if (!TablaDeSimbolos.esCopiaResultado(var+ambito)){
            crearTerceto(":=", var+ambito,val_peek(2).sval);
        } else{
            Compilador.tercetosCR.add(new Terceto (":=",val_peek(2).sval, var+ambito));
        }
        TablaDeSimbolos.eliminar(var);
        yyval=val_peek(0);
    }
break;
case 62:
//#line 256 "grama.y"
{yyerror("Falta parametro formal");}
break;
case 63:
//#line 257 "grama.y"
{yyerror("Falta flecha y parametro formal");}
break;
case 64:
//#line 259 "grama.y"
{
        String ambito = Compilador.getAmbito()+":"+nombreFuncion;
        String var = val_peek(0).sval;
        if (!TablaDeSimbolos.parametroDeclarado(var, ambito)){
            yyerror("El parametro " + var + " no esta declarado en la funcion "+nombreFuncion);
        }
         if (!TablaDeSimbolos.esCopiaResultado(var+ambito)){
            crearTerceto(":=", var+ambito,val_peek(2).sval);
        } else{
            Compilador.tercetosCR.add(new Terceto (":=", val_peek(2).sval,var+ambito));
        }
        TablaDeSimbolos.eliminar(var);
        yyval=new ParserVal(val_peek(4).sval + "," + val_peek(0).sval);
    }
break;
case 65:
//#line 273 "grama.y"
{yyerror("Falta parametro formal");}
break;
case 66:
//#line 274 "grama.y"
{yyerror("Falta flecha y parametro formal");}
break;
case 67:
//#line 275 "grama.y"
{yyerror("error en parametros de invocacion");}
break;
case 69:
//#line 284 "grama.y"
{
        crearTerceto("print", val_peek(2).sval, "-");
        int t = Compilador.tercetos.size() - 1;
        yyval=new ParserVal("[" + t + "]");
    }
break;
case 70:
//#line 289 "grama.y"
{yyerror("falta argumento en print");}
break;
case 72:
//#line 294 "grama.y"
{yyerror("falta ;");}
break;
case 75:
//#line 300 "grama.y"
{yyerror("argumento invalido en print");}
break;
case 76:
//#line 306 "grama.y"
{
        int bi = Compilador.pilaSaltos.remove(Compilador.pilaSaltos.size()-1);
        completarBI(bi);
    }
break;
case 77:
//#line 310 "grama.y"
{yyerror("no hay sentencias en else");}
break;
case 79:
//#line 315 "grama.y"
{
        yyerror("falta endif");
    }
break;
case 80:
//#line 319 "grama.y"
{
        yyerror("falta endif");yyerror("no hay sentencias en else");
    }
break;
case 81:
//#line 323 "grama.y"
{
        yyerror("falta endif");
    }
break;
case 82:
//#line 328 "grama.y"
{yyerror("no hay sentencias en then");}
break;
case 83:
//#line 329 "grama.y"
{yyerror("no hay sentencias en then");yyerror("no hay sentencias en else");}
break;
case 84:
//#line 330 "grama.y"
{yyerror("no hay sentencias en then");}
break;
case 85:
//#line 333 "grama.y"
{yyerror("no hay sentencias en then");yyerror("falta endif");}
break;
case 86:
//#line 334 "grama.y"
{yyerror("no hay sentencias en then");yyerror("no hay sentencias en else");yyerror("falta endif");}
break;
case 87:
//#line 335 "grama.y"
{yyerror("no hay sentencias en then");yyerror("falta endif");}
break;
case 88:
//#line 339 "grama.y"
{
        int bf = Compilador.pilaSaltos.remove(Compilador.pilaSaltos.size()-1);
        completarBF(bf, Compilador.tercetos.size()+1);
    }
break;
case 89:
//#line 346 "grama.y"
{
        int bf = Compilador.pilaSaltos.remove(Compilador.pilaSaltos.size()-1);
        completarBF(bf, Compilador.tercetos.size());
    }
break;
case 90:
//#line 352 "grama.y"
{
        crearTerceto("BI", "-", "-");
         int bi = Compilador.tercetos.size() - 1;
        Compilador.pilaSaltos.add(bi);}
break;
case 91:
//#line 359 "grama.y"
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
case 92:
//#line 368 "grama.y"
{yyerror("falta cerrar parentesis");}
break;
case 93:
//#line 369 "grama.y"
{yyerror("falta abrir parentesis");}
break;
case 94:
//#line 370 "grama.y"
{yyerror("faltan parentesis");}
break;
case 95:
//#line 375 "grama.y"
{yyval=new ParserVal("==");}
break;
case 96:
//#line 376 "grama.y"
{yyval=new ParserVal("=!");}
break;
case 97:
//#line 377 "grama.y"
{yyval=new ParserVal("<=");}
break;
case 98:
//#line 378 "grama.y"
{yyval=new ParserVal(">=");}
break;
case 99:
//#line 379 "grama.y"
{yyval=new ParserVal("<");}
break;
case 100:
//#line 380 "grama.y"
{yyval=new ParserVal(">");}
break;
case 101:
//#line 381 "grama.y"
{yyerror("comparacion invalida");}
break;
case 104:
//#line 395 "grama.y"
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
case 105:
//#line 406 "grama.y"
{
        if (!hayReturn){
            yyerror("falta return en la funcion "+ val_peek(5).sval);
        }else hayReturn = false;

        Compilador.salirAmbito();
        crearTerceto("fin de funcion", val_peek(5).sval+Compilador.getAmbito(), "-");

    }
break;
case 106:
//#line 415 "grama.y"
{ yyerror("falta declarar nombre de funcion");}
break;
case 107:
//#line 416 "grama.y"
{yyerror("faltan parametros formales");}
break;
case 108:
//#line 417 "grama.y"
{yyerror("falta declarar nombre de funcion"); yyerror("faltan parametros formales");}
break;
case 109:
//#line 418 "grama.y"
{ yyerror("falta declarar el tipo de la funcion");}
break;
case 111:
//#line 423 "grama.y"
{ yyerror("falta sentencias en la funcion " + val_peek(0).sval);}
break;
case 112:
//#line 428 "grama.y"
{
        String ambito = Compilador.getAmbito();
        String var = val_peek(0).sval;
        if(TablaDeSimbolos.agregarVar(var, ambito, tipo, "nombre de variable", "copia resultado")){
           yyval = new ParserVal(var+ambito);
        } else yyerror("La variable "+var+" ya fue declarada");
    }
break;
case 113:
//#line 436 "grama.y"
{
        String ambito = Compilador.getAmbito();
        String var = val_peek(0).sval;
        if(TablaDeSimbolos.agregarVar(var, ambito, tipo, "nombre de variable","copia valor")){
           yyval = new ParserVal(var+ambito);
        } else yyerror("La variable "+var+" ya fue declarada");
    }
break;
case 114:
//#line 443 "grama.y"
{
        String ambito = Compilador.getAmbito();
        String var = val_peek(0).sval;
        if(TablaDeSimbolos.agregarVar(var, ambito, tipo, "nombre de variable", "copia resultado")){
           yyval = new ParserVal(var+ambito);
        } else yyerror("La variable "+var+" ya fue declarada");
    }
break;
case 115:
//#line 450 "grama.y"
{
        String ambito = Compilador.getAmbito();
        String var = val_peek(0).sval;
        if(TablaDeSimbolos.agregarVar(var, ambito, tipo, "nombre de variable", "copia valor")){
           yyval = new ParserVal(var+ambito);
        } else yyerror("La variable "+var+" ya fue declarada");
    }
break;
case 116:
//#line 457 "grama.y"
{yyerror("error en parametro formal");}
break;
case 117:
//#line 461 "grama.y"
{tipo = "uint";}
break;
case 122:
//#line 477 "grama.y"
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
case 124:
//#line 497 "grama.y"
{yyerror("falta cuerpo sentencias");}
break;
case 125:
//#line 498 "grama.y"
{yyerror("falta de ;");}
break;
case 126:
//#line 500 "grama.y"
{ yyerror("falta while");}
break;
case 127:
//#line 501 "grama.y"
{yyerror("falta cuerpo sentencias"); yyerror("falta while");}
break;
case 128:
//#line 502 "grama.y"
{yyerror("falta de ) en condicion");yyerror("falta de ;");yyerror("falta while");}
break;
case 129:
//#line 503 "grama.y"
{yyerror("falta de ) en condicion");yyerror("falta de ;");yyerror("falta cuerpo sentencias");yyerror("falta while");}
break;
case 130:
//#line 507 "grama.y"
{
        Compilador.pilaSaltos.add(Compilador.tercetos.size());
        crearTerceto("inicio while","-","-", true);
    }
break;
case 131:
//#line 513 "grama.y"
{
        int salto = Compilador.pilaSaltos.remove(0);
        crearTerceto("BIW", "[" +salto+"]", "-");
        crearTerceto("fin while", "-", "-");
    }
break;
case 132:
//#line 521 "grama.y"
{
         int bf = Compilador.pilaSaltos.remove(Compilador.pilaSaltos.size()-1);
         completarBF(bf, Compilador.tercetos.size()-1);
    }
break;
case 133:
//#line 529 "grama.y"
{hayReturn = false;}
break;
case 134:
//#line 530 "grama.y"
{yyerror("no hay sentencias dentro de las llaves");}
break;
case 135:
//#line 531 "grama.y"
{Compilador.salirAmbito();}
break;
case 136:
//#line 532 "grama.y"
{yyerror("Error en sentencia");}
break;
case 137:
//#line 538 "grama.y"
{
        yyval= new ParserVal("lambda");
        crearTerceto("fin de lambda", "-", "-");
    }
break;
case 138:
//#line 542 "grama.y"
{ yyerror("falta abrir llave en cuerpo de sentencia lambda");}
break;
case 139:
//#line 543 "grama.y"
{yyerror("falta cerrar llave en cuerpo de sentencia lambda");}
break;
case 140:
//#line 544 "grama.y"
{ yyerror("faltan llaves en cuerpo de sentencia lambda");}
break;
case 143:
//#line 554 "grama.y"
{
        String ambito = Compilador.getAmbito();
        String var = val_peek(0).sval;
        if(TablaDeSimbolos.agregarVar(var, ambito, tipo, "nombre de variable")){
           yyval = new ParserVal(var+ambito);
        } else yyerror("La variable "+var+" ya fue declarada");
        crearTerceto("inicio de lambda", "-", "-");
    }
break;
case 144:
//#line 567 "grama.y"
{
        asigMultiple(val_peek(3).sval,val_peek(1).sval);
    }
break;
case 146:
//#line 574 "grama.y"
{yyval=new ParserVal(val_peek(2).sval + "," + val_peek(0).sval);}
break;
case 147:
//#line 575 "grama.y"
{yyerror("falta , en lista de variables");}
break;
case 148:
//#line 579 "grama.y"
{yyval=val_peek(0);}
break;
case 149:
//#line 580 "grama.y"
{yyval=new ParserVal(val_peek(2).sval + "," + val_peek(0).sval);}
break;
case 150:
//#line 581 "grama.y"
{yyerror("falta , en lista de constantes");}
break;
//#line 1637 "Parser.java"
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
