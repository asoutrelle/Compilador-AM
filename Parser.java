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
    9,    9,    9,    9,    9,    9,    9,   31,   33,   32,
   28,   28,   28,   28,   34,   34,   34,   34,   34,   34,
   34,   35,   35,   37,   38,    7,    7,    7,    7,   36,
   36,   36,   36,   36,   14,   39,   39,   40,   40,   11,
   13,   13,   13,   13,   13,   13,   13,   41,   42,   29,
   30,   30,   30,   30,   23,   23,   23,   23,   44,   44,
   43,   12,   45,   45,   45,   46,   46,   46,
};
final static short yylen[] = {                            2,
    4,    3,    3,    3,    2,    3,    2,    4,    3,    3,
    3,    2,    2,    2,    1,    2,    2,    1,    1,    1,
    1,    1,    1,    1,    1,    1,    1,    1,    3,    1,
    3,    2,    4,    3,    3,    2,    3,    3,    1,    3,
    3,    3,    3,    2,    2,    1,    1,    1,    1,    4,
    3,    3,    3,    1,    1,    2,    3,    1,    0,    5,
    3,    2,    1,    5,    4,    3,    1,    1,    5,    4,
    1,    1,    1,    1,    1,   11,    8,    7,   10,    7,
    6,    9,    5,    4,    7,    4,    3,    0,    0,    0,
    5,    4,    4,    3,    1,    1,    1,    1,    1,    1,
    1,    1,    2,    0,    0,   10,    7,    7,    6,    3,
    2,    5,    4,    1,    1,    1,    2,    1,    1,    5,
    9,    6,    6,    6,    5,    5,    4,    0,    0,    0,
    3,    2,    1,    3,    9,    8,    8,    7,    3,    3,
    1,    4,    1,    3,    2,    1,    3,    2,
};
final static short yydefred[] = {                         0,
    0,    0,    0,    0,    0,    0,    0,    0,  130,    0,
  115,   13,    0,   15,   18,   19,   20,   21,   22,   23,
   24,   25,   26,   27,   28,    0,    0,    0,    0,    0,
    0,    0,    0,   17,    0,    0,    0,  128,    0,   49,
    0,    0,    0,    0,    0,   47,    0,    0,   46,   48,
   54,    0,    6,   16,    0,    0,    0,    0,    0,    0,
  145,   11,    0,    0,    4,    9,    2,   57,   75,   74,
    0,    0,    0,    0,    0,    0,    0,   55,    0,    0,
    0,    0,   45,   44,  130,    0,  101,   95,   96,   97,
   98,    0,    0,   99,  100,    0,    0,    0,   72,    0,
    0,   71,   87,    0,    0,  114,    0,    0,    0,    0,
   32,    0,   29,    0,  144,  146,    0,    8,    1,   70,
    0,    0,    0,    0,  133,    0,    0,    0,    0,   56,
   52,   53,    0,   51,    0,    0,   37,    0,   38,    0,
    0,   43,   40,   42,   41,    0,   86,   90,   84,    0,
    0,    0,    0,    0,  111,    0,    0,   31,   33,  148,
    0,  142,   69,  120,    0,    0,  132,  102,    0,  125,
    0,    0,   67,    0,    0,    0,   50,  141,    0,    0,
   93,   83,    0,    0,    0,    0,    0,  110,  119,  118,
    0,  116,    0,    0,    0,  147,  122,  134,  131,  103,
    0,  124,    0,   60,    0,    0,   91,    0,    0,    0,
   81,    0,  105,  109,  117,    0,  113,    0,   89,    0,
   61,    0,    0,   85,    0,    0,   80,   90,   78,  108,
    0,  112,  107,    0,    0,    0,    0,    0,  138,    0,
   77,    0,    0,  121,   64,    0,  137,  136,    0,    0,
   82,    0,    0,  135,  139,  140,    0,   79,  106,   76,
};
final static short yydgoto[] = {                          4,
   13,   14,   15,   16,   17,   18,   19,   20,   21,   22,
   23,   24,   25,   26,   57,  103,   27,   47,   48,   49,
   50,   81,   51,   77,  175,  176,   74,   52,   38,  128,
  184,  183,  185,   96,  169,  110,  152,  231,  191,  192,
   76,  219,  179,  239,   28,  117,
};
final static short yysindex[] = {                       -98,
  428,  384,  225,    0,  -56,  -13,  -14,   49,    0,   57,
    0,    0,  454,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,  -40, -174,  -34,  -27,  560,
  477,  -23,  491,    0, -161,  -28,  144,    0,  -13,    0,
  -11,  153,  167,  167,  138,    0,  398,    9,    0,    0,
    0,  -57,    0,    0,   61,  -32,  101,  144, -127, -126,
    0,    0,  -20,  507,    0,    0,    0,    0,    0,    0,
 -131,  -43,    7,  109,   36,  -36,  114,    0, -103,  236,
  136,    9,    0,    0,    0,  398,    0,    0,    0,    0,
    0,  259,  305,    0,    0,  144,  131,  179,    0,  -41,
  -43,    0,    0,  444,  149,    0, -131,   74,  -59,   31,
    0,  -49,    0,   90,    0,    0,   -4,    0,    0,    0,
  -43,  -43,   57,  430,    0, -174,  -43,  -12,  192,    0,
    0,    0,  160,    0,  -45,  144,    0,    9,    0,    9,
   82,    0,    0,    0,    0,  -43,    0,    0,    0,    0,
   83, -136,  -10,  -96,    0, -218,  102,    0,    0,    0,
  -21,    0,    0,    0,  -43,  134,    0,    0,  337,    0,
   57,  -43,    0,    7,   67,    4,    0,    0,  219,  434,
    0,    0,  444,   28,   44,  -96,  170,    0,    0,    0,
  321,    0, -131,   54,  -96,    0,    0,    0,    0,    0,
    0,    0,  144,    0,   66,  540,    0,  -43,  137,  -43,
    0,  517,    0,    0,    0,   71,    0,  526,    0,   63,
    0,  251,  211,    0,  100,  -43,    0,    0,    0,    0,
  246,    0,    0,  -43,   94,  272,  330,  -69,    0,  -43,
    0,  444,  -96,    0,    0,  330,    0,    0,  366,  375,
    0,  228,  537,    0,    0,    0,  -43,    0,    0,    0,
};
final static short yyrindex[] = {                         0,
    0,    0,    0,    0,    0,   34,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,  103,    0,  417,    0,
  429,  433,  438,    0,    0,    0,    0,    0,    1,    0,
    0,    0,    0,    0,    0,    0,    0,   23,    0,    0,
    0,  551,    0,    0,  289,    0,    0,    0,    0,    0,
    0,    0,  439,  442,    0,    0,    0,    0,    0,    0,
    0,    0,  406,    0,    0,    0,    0,    0,    0,    0,
    0,   47,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,  551,
    0,    0,    0,    0,  -93,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,  410,  157,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,   69,    0,   93,
  115,    0,    0,    0,    0,    0,    0,    0,    0,  -51,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,  165,    0,  -24,    0,  212,    0,    0,    0,  143,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
  117,    0,    0,    0,  283,    0,    0,  190,  551,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,  341,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,  350,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
};
final static short yygindex[] = {                         0,
   19,  432,  519,  572,    0,    0,    0,    0,    0,    0,
    0,    0,    0,  441,    0,  404,  529,  474,   76,  494,
    0,  379,    0,    0,    0,  261,    0,  -52,  -47,  -67,
    0,  238, -135,  381, -152,  326,    0,    0, -121, -132,
    0,    0,    0,  -95,    0,    0,
};
final static int YYTABLESIZE=835;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                         56,
   58,  102,   34,   45,  104,   43,   42,   89,  108,   59,
   44,   71,   72,   43,   42,  102,   68,  102,   44,   68,
   31,   33,   39,  127,    3,   36,   60,   45,   80,   43,
   42,   34,   35,   79,   44,   34,  150,  135,   34,  161,
   59,   58,   58,   58,   58,   58,   36,   58,   64,   92,
   97,   93,  148,  223,  102,   98,   11,  193,  215,   58,
   58,   58,   58,   39,  212,   39,   39,   39,   34,  236,
  165,  157,  225,  218,  156,  172,  122,   58,   92,  215,
   93,   39,   39,  234,   39,  215,  124,   36,   37,   36,
   36,   36,   35,   58,   58,   68,   45,   62,   43,   42,
  105,   66,  102,   44,  118,   36,   36,  204,   36,   34,
  203,   34,   34,   34,   94,  208,  123,   82,  201,  106,
  215,  253,  181,   58,   92,   58,   93,   34,   34,    6,
   34,  116,   92,   35,   93,   35,   35,   35,   11,  107,
  247,  248,   92,   11,  112,   39,  143,   39,  102,  121,
  254,   35,   35,  129,   35,  130,  127,    1,    2,  102,
    6,  228,  104,  143,  126,    7,    8,  138,  140,   36,
   71,   36,   10,   94,  252,  129,  134,   71,   11,   43,
   42,  104,  104,   71,   44,   43,   42,  249,  250,  151,
   44,   34,   71,   34,   43,  102,  154,  155,   99,   44,
  177,   92,  100,  101,   89,  186,   71,  158,   88,   89,
  213,  178,   99,  156,   99,   35,   55,   35,   71,  146,
   39,   40,    6,  106,  195,    7,    8,   69,   39,   40,
  123,   71,   10,   43,   42,   70,  196,   94,   44,   94,
   41,  123,   11,  107,   39,   40,  188,   78,   41,   68,
  238,   99,   63,  160,  171,   63,   58,   58,  198,  206,
   58,   58,   58,   58,   41,   92,   58,   92,   58,   58,
   58,   58,   58,   58,   58,   58,  132,  205,   39,   39,
   79,  127,   39,   39,   39,   39,  102,  209,   39,  126,
   58,   39,   39,   39,   39,   39,   39,   39,   71,   99,
   43,   58,   36,   36,  210,   44,   36,   36,   36,   36,
  217,  238,   36,   39,   40,   36,   36,   36,   36,   36,
   36,   36,  221,   62,   34,   34,   62,  232,   34,   34,
   34,   34,   30,   41,   34,  237,  235,   34,   34,   34,
   34,   34,   34,   34,   71,   99,   43,   30,   35,   35,
  245,   44,   35,   35,   35,   35,   99,  111,   35,  143,
  240,   35,   35,   35,   35,   35,   35,   35,  243,  238,
   94,   94,  129,  123,   94,   94,   94,   94,  123,  123,
   94,   66,  123,   94,   66,  123,  142,   39,   40,   94,
   65,  123,   99,   65,   39,   40,  246,  226,   92,   92,
   39,   40,   92,   92,   92,   92,  255,   41,   92,   39,
   40,   92,   11,  127,   41,  256,   14,   92,  127,  127,
   41,  126,  127,   39,   40,  127,  126,  126,    7,   41,
  126,  127,   12,  126,  144,   39,   40,    5,   10,  126,
   92,    3,   93,   41,   54,  214,   73,  173,   39,   40,
   89,   47,   47,  143,   47,   41,   47,   94,  133,   95,
  113,  199,   54,  220,   54,  242,  136,    6,   41,   47,
  143,   47,    7,    8,  207,  120,   92,  187,   93,   10,
   32,    6,    0,   99,    0,   85,    7,    8,  257,    0,
    9,  131,    0,   10,   78,   54,  109,    0,    0,   11,
    0,    0,    0,  147,  149,    0,   30,    6,    0,   73,
   75,   85,    7,    8,  137,   39,   40,  159,   86,   10,
  162,    0,    0,    0,  163,  164,    0,    0,    6,    0,
  170,  114,    0,    7,    8,   41,   83,   84,   46,    0,
   10,    0,    0,    0,   30,   30,    0,  153,    0,  182,
    0,    0,   12,    0,  167,    0,   61,    0,    0,    0,
  139,   39,   40,    0,   46,   46,  124,    0,  197,  141,
   46,   46,   46,   46,    0,  202,    0,    6,   53,    0,
    0,   41,    7,    8,    0,    0,   46,  115,  211,   10,
  143,  145,  109,    6,  125,   11,  194,    0,    7,    8,
    0,   65,  174,    0,  126,   10,    0,    0,    0,  180,
    0,  224,  227,  229,    0,   67,    0,    0,    0,    0,
   46,   46,  125,    0,   46,   46,   46,    0,    0,  241,
    0,  119,    0,  216,    0,    0,    0,  244,    0,   29,
    6,  230,  168,  251,    0,    7,    8,    0,    0,    9,
  233,   46,   10,   87,    0,  258,   46,   46,   11,    0,
  260,  259,  222,    0,   46,   47,  143,   88,   89,   90,
   91,    0,  189,  130,    0,    0,  174,    0,    0,   47,
   47,   47,   47,    5,    6,  166,    6,  200,    0,    7,
    8,    7,    8,    9,    0,    0,   10,    0,   10,   46,
    6,  125,   11,    0,  189,    7,    8,    0,    0,  189,
    6,    0,   10,  189,    0,    7,    8,    0,    0,    9,
    0,    0,   10,    0,  168,  190,    0,    0,   11,    0,
  189,   46,    0,    6,    0,    0,  189,    0,    7,    8,
  168,  200,    9,    0,    0,   10,    0,    6,    0,    0,
    0,   11,    7,    8,  200,    0,    9,  190,    0,   10,
  125,  189,  190,    6,    0,   11,  190,    0,    7,    8,
    0,  189,    9,    6,    0,   10,    0,    0,    7,    8,
    0,   11,    6,  190,    0,   10,    0,    7,    8,  190,
    0,   11,    0,    6,   10,    0,    6,    0,    7,    8,
   11,    7,    8,    0,    0,   10,    0,  130,   10,    0,
    0,   11,  130,  130,  190,   63,    6,    0,    0,  130,
    0,    7,    8,    0,  190,    9,    0,    0,   10,    0,
    0,    0,    0,    0,   11,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                         40,
    0,   59,   59,   40,   52,   42,   43,   59,   41,   44,
   47,   40,   41,   42,   43,   59,   41,   59,   47,   44,
    2,    3,    0,   76,  123,   40,   61,   40,   40,   42,
   43,   59,   46,   45,   47,   59,  104,   85,   59,   44,
   40,   41,   42,   43,   44,   45,    0,   47,   30,   43,
   42,   45,  100,  206,   59,   47,  275,  276,  191,   59,
   60,   61,   62,   41,  186,   43,   44,   45,    0,  222,
  123,   41,  208,  195,   44,  128,   41,   44,   43,  212,
   45,   59,   60,  219,   62,  218,  123,   41,   40,   43,
   44,   45,    0,  268,   61,  257,   40,  125,   42,   43,
   40,  125,   59,   47,  125,   59,   60,   41,   62,   41,
   44,   43,   44,   45,    0,  183,    0,   42,  171,  256,
  253,  243,   41,  123,   43,  125,   45,   59,   60,  257,
   62,  258,   43,   41,   45,   43,   44,   45,  275,  276,
  236,  237,    0,  275,   44,  123,   44,  125,   59,   41,
  246,   59,   60,   40,   62,  259,    0,  256,  257,   59,
  257,  209,  256,   61,    0,  262,  263,   92,   93,  123,
   40,  125,  269,   59,  242,   59,   41,   40,  275,   42,
   43,  275,  276,   40,   47,   42,   43,  257,  258,   41,
   47,  123,   40,  125,   42,   59,  123,  257,  256,   47,
   41,   59,  260,  261,  256,  123,   40,  257,  260,  261,
   41,  257,  256,   44,  256,  123,  257,  125,   40,  261,
  257,  258,  257,  256,  123,  262,  263,  256,  257,  258,
  267,   40,  269,   42,   43,  264,  258,  123,   47,  125,
  277,  125,  275,  276,  257,  258,  257,  259,  277,  274,
   40,  256,   41,  258,  267,   44,  256,  257,  125,   41,
  260,  261,  262,  263,  277,  123,  266,  125,  268,  269,
  270,  271,  272,  273,  274,  275,   41,  274,  256,  257,
   45,  125,  260,  261,  262,  263,   59,  260,  266,  125,
  257,  269,  270,  271,  272,  273,  274,  275,   40,  256,
   42,  268,  256,  257,  261,   47,  260,  261,  262,  263,
  257,   40,  266,  257,  258,  269,  270,  271,  272,  273,
  274,  275,  257,   41,  256,  257,   44,  257,  260,  261,
  262,  263,   44,  277,  266,  125,  274,  269,  270,  271,
  272,  273,  274,  275,   40,  256,   42,   59,  256,  257,
  257,   47,  260,  261,  262,  263,  256,  257,  266,  257,
  261,  269,  270,  271,  272,  273,  274,  275,  123,   40,
  256,  257,  256,  257,  260,  261,  262,  263,  262,  263,
  266,   41,  266,  269,   44,  269,  256,  257,  258,  275,
   41,  275,  256,   44,  257,  258,  125,  261,  256,  257,
  257,  258,  260,  261,  262,  263,   41,  277,  266,  257,
  258,  269,  275,  257,  277,   41,    0,  275,  262,  263,
  277,  257,  266,  257,  258,  269,  262,  263,    0,  277,
  266,  275,    0,  269,  256,  257,  258,    0,    0,  275,
   43,    0,   45,  277,   13,  125,   41,  256,  257,  258,
  261,   42,   43,   44,   45,  277,   47,   60,   80,   62,
   57,  125,   31,  203,   33,  228,   86,  257,  277,   60,
   61,   62,  262,  263,   41,   72,   43,  152,   45,  269,
  256,  257,   -1,  256,   -1,   45,  262,  263,  261,   -1,
  266,  256,   -1,  269,  259,   64,   56,   -1,   -1,  275,
   -1,   -1,   -1,  100,  101,   -1,  123,  257,   -1,   36,
   37,   71,  262,  263,  256,  257,  258,  114,   45,  269,
  117,   -1,   -1,   -1,  121,  122,   -1,   -1,  257,   -1,
  127,   58,   -1,  262,  263,  277,   43,   44,   10,   -1,
  269,   -1,   -1,   -1,  256,  257,   -1,  107,   -1,  146,
   -1,   -1,  125,   -1,  125,   -1,   28,   -1,   -1,   -1,
  256,  257,  258,   -1,   36,   37,  123,   -1,  165,   96,
   42,   43,   44,   45,   -1,  172,   -1,  257,  125,   -1,
   -1,  277,  262,  263,   -1,   -1,   58,   59,  185,  269,
   97,   98,  152,  257,   76,  275,  156,   -1,  262,  263,
   -1,  125,  129,   -1,   76,  269,   -1,   -1,   -1,  136,
   -1,  208,  209,  210,   -1,  125,   -1,   -1,   -1,   -1,
   92,   93,  104,   -1,   96,   97,   98,   -1,   -1,  226,
   -1,  125,   -1,  193,   -1,   -1,   -1,  234,   -1,  256,
  257,  125,  124,  240,   -1,  262,  263,   -1,   -1,  266,
  125,  123,  269,  256,   -1,  252,  128,  129,  275,   -1,
  257,  125,  123,   -1,  136,  256,  257,  270,  271,  272,
  273,   -1,  154,  123,   -1,   -1,  203,   -1,   -1,  270,
  271,  272,  273,  256,  257,  256,  257,  169,   -1,  262,
  263,  262,  263,  266,   -1,   -1,  269,   -1,  269,  171,
  257,  183,  275,   -1,  186,  262,  263,   -1,   -1,  191,
  257,   -1,  269,  195,   -1,  262,  263,   -1,   -1,  266,
   -1,   -1,  269,   -1,  206,  154,   -1,   -1,  275,   -1,
  212,  203,   -1,  257,   -1,   -1,  218,   -1,  262,  263,
  222,  223,  266,   -1,   -1,  269,   -1,  257,   -1,   -1,
   -1,  275,  262,  263,  236,   -1,  266,  186,   -1,  269,
  242,  243,  191,  257,   -1,  275,  195,   -1,  262,  263,
   -1,  253,  266,  257,   -1,  269,   -1,   -1,  262,  263,
   -1,  275,  257,  212,   -1,  269,   -1,  262,  263,  218,
   -1,  275,   -1,  257,  269,   -1,  257,   -1,  262,  263,
  275,  262,  263,   -1,   -1,  269,   -1,  257,  269,   -1,
   -1,  275,  262,  263,  243,  256,  257,   -1,   -1,  269,
   -1,  262,  263,   -1,  253,  266,   -1,   -1,  269,   -1,
   -1,   -1,   -1,   -1,  275,
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
"$$2 :",
"$$3 :",
"funcion : tipo ID '(' $$2 parametros_formales ')' $$3 '{' lista_sentencia_funcion '}'",
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

//#line 552 "grama.y"
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
//#line 742 "Parser.java"
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
//#line 61 "grama.y"
{addEstructura("sentencia if");}
break;
case 25:
//#line 62 "grama.y"
{addEstructura("print");}
break;
case 26:
//#line 63 "grama.y"
{addEstructura("return");}
break;
case 27:
//#line 64 "grama.y"
{addEstructura("asignacion multiple");}
break;
case 28:
//#line 68 "grama.y"
{addEstructura("sentencia do while");}
break;
case 30:
//#line 78 "grama.y"
{
        String ambito = Compilador.getAmbito();
        String var = val_peek(0).sval;
        if(TablaDeSimbolos.agregarVar(var, ambito, tipo, "nombre de variable")){
           yyval = new ParserVal(var+ambito);
        } else yyerror("La variable "+var+" ya fue declarada");
    }
break;
case 31:
//#line 86 "grama.y"
{
        String ambito = Compilador.getAmbito();
        String var = val_peek(0).sval;
        if(TablaDeSimbolos.agregarVar(var, ambito, tipo, "nombre de variable")){
           yyval = new ParserVal(val_peek(2).sval+","+var);
        } else yyerror("La variable "+var+" ya fue declarada");
    }
break;
case 32:
//#line 93 "grama.y"
{yyerror("falta , en lista de variables");}
break;
case 33:
//#line 98 "grama.y"
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
//#line 115 "grama.y"
{
        crearTerceto("+", val_peek(2).sval, val_peek(0).sval);
        int t = Compilador.tercetos.size() - 1;
        yyval=new ParserVal("[" + t + "]");
    }
break;
case 35:
//#line 121 "grama.y"
{
        crearTerceto("-", val_peek(2).sval, val_peek(0).sval);
        int t = Compilador.tercetos.size() - 1;
        yyval=new ParserVal("[" + t + "]");
    }
break;
case 36:
//#line 126 "grama.y"
{yyerror("falta operando a izquierda de +");}
break;
case 37:
//#line 127 "grama.y"
{yyerror("falta operando a derecha de +");}
break;
case 38:
//#line 128 "grama.y"
{yyerror("falta operando a derecha de -");}
break;
case 40:
//#line 134 "grama.y"
{
        crearTerceto("*", val_peek(2).sval, val_peek(0).sval);
        int t = Compilador.tercetos.size() - 1;
        yyval=new ParserVal("[" + t + "]");
    }
break;
case 41:
//#line 140 "grama.y"
{
        crearTerceto("/", val_peek(2).sval, val_peek(0).sval);
        int t = Compilador.tercetos.size() - 1;
        yyval=new ParserVal("[" + t + "]");
    }
break;
case 42:
//#line 145 "grama.y"
{yyerror("falta operando a derecha de /");}
break;
case 43:
//#line 146 "grama.y"
{yyerror("falta operando a derecha de *");}
break;
case 44:
//#line 147 "grama.y"
{yyerror("falta operando a izquierda de /");}
break;
case 45:
//#line 148 "grama.y"
{yyerror("falta operando a izquierda de *");}
break;
case 49:
//#line 155 "grama.y"
{yyval=new ParserVal(val_peek(0).sval);}
break;
case 50:
//#line 157 "grama.y"
{
        addEstructura("trunc"); yyval=new ParserVal(truncar(val_peek(1).sval));
    }
break;
case 51:
//#line 160 "grama.y"
{addEstructura("trunc");yyerror("falta abrir parentesis en trunc");}
break;
case 52:
//#line 161 "grama.y"
{addEstructura("trunc");yyerror("falta cerrar parentesis en trunc");yyerrflag=0;}
break;
case 53:
//#line 162 "grama.y"
{addEstructura("trunc");yyerror("falta argumento en trunc");}
break;
case 54:
//#line 163 "grama.y"
{addEstructura("lambda");}
break;
case 55:
//#line 167 "grama.y"
{check_rango(val_peek(0).sval); yyval=new ParserVal(val_peek(0).sval);}
break;
case 56:
//#line 168 "grama.y"
{check_rango("-"+val_peek(0).sval); yyval=new ParserVal("-"+val_peek(1).sval);}
break;
case 57:
//#line 173 "grama.y"
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
//#line 184 "grama.y"
{
        String ambito = Compilador.getAmbito();
        String var = val_peek(0).sval;
        if(!TablaDeSimbolos.varDeclarada(var, ambito)){
            yyerror("La variable "+ var +" no fue declarada");
        } else {
            TablaDeSimbolos.eliminar(var);
            yyval=new ParserVal (var + ambito);
        }
    }
break;
case 59:
//#line 197 "grama.y"
{nombreFuncion = val_peek(0).sval;}
break;
case 60:
//#line 198 "grama.y"
{
        String ambito = Compilador.getAmbito();
        String var = val_peek(4).sval;
        if(!TablaDeSimbolos.funcionDeclarada(var,ambito)){
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
        if(TablaDeSimbolos.cantParametrosFormales(val_peek(4).sval)!=cantParam){
            yyerror("cantidad de parametros reales distinta a parametros formales");
        }
        yyval = new ParserVal(var+ambito);
    }
break;
case 61:
//#line 224 "grama.y"
{
        String ambito = Compilador.getAmbito()+":"+nombreFuncion;
        String var = val_peek(0).sval;
        if (!TablaDeSimbolos.parametroDeclarado(var, ambito)){
            yyerror("El parametro " + var + " no esta declarado en la funcion "+nombreFuncion);
        }
        crearTerceto(":=", var+ambito,val_peek(2).sval);
        TablaDeSimbolos.eliminar(var);
        yyval=val_peek(2);
    }
break;
case 62:
//#line 234 "grama.y"
{yyerror("Falta parametro formal");}
break;
case 63:
//#line 235 "grama.y"
{yyerror("Falta flecha y parametro formal");}
break;
case 64:
//#line 237 "grama.y"
{
        String ambito = Compilador.getAmbito()+":"+nombreFuncion;
        String var = val_peek(0).sval;
        if (!TablaDeSimbolos.parametroDeclarado(var, ambito)){
            yyerror("El parametro " + var + " no esta declarado en la funcion "+nombreFuncion);
        }
        crearTerceto(":=", var+ambito,val_peek(2).sval);
        TablaDeSimbolos.eliminar(var);
        yyval=new ParserVal(val_peek(4).sval + "," + val_peek(2).sval);
    }
break;
case 65:
//#line 247 "grama.y"
{yyerror("Falta parametro formal");}
break;
case 66:
//#line 248 "grama.y"
{yyerror("Falta flecha y parametro formal");}
break;
case 67:
//#line 249 "grama.y"
{yyerror("error en parametros de invocacion");}
break;
case 69:
//#line 258 "grama.y"
{
        crearTerceto("print", val_peek(2).sval, "-");
         int t = Compilador.tercetos.size() - 1;
        yyval=new ParserVal("[" + t + "]");
    }
break;
case 70:
//#line 263 "grama.y"
{yyerror("falta argumento en print");}
break;
case 72:
//#line 268 "grama.y"
{yyerror("falta ;");}
break;
case 75:
//#line 274 "grama.y"
{yyerror("argumento invalido en print");}
break;
case 76:
//#line 279 "grama.y"
{
        int bi = Compilador.pilaSaltos.remove(Compilador.pilaSaltos.size()-1);
        completarBI(bi, Compilador.tercetos.size());
    }
break;
case 77:
//#line 283 "grama.y"
{yyerror("no hay sentencias en else");}
break;
case 79:
//#line 288 "grama.y"
{
        yyerror("falta endif");
    }
break;
case 80:
//#line 292 "grama.y"
{
        yyerror("falta endif");yyerror("no hay sentencias en else");
    }
break;
case 81:
//#line 296 "grama.y"
{
        yyerror("falta endif");
    }
break;
case 82:
//#line 301 "grama.y"
{yyerror("no hay sentencias en then");}
break;
case 83:
//#line 302 "grama.y"
{yyerror("no hay sentencias en then");yyerror("no hay sentencias en else");}
break;
case 84:
//#line 303 "grama.y"
{yyerror("no hay sentencias en then");}
break;
case 85:
//#line 306 "grama.y"
{yyerror("no hay sentencias en then");yyerror("falta endif");}
break;
case 86:
//#line 307 "grama.y"
{yyerror("no hay sentencias en then");yyerror("no hay sentencias en else");yyerror("falta endif");}
break;
case 87:
//#line 308 "grama.y"
{yyerror("no hay sentencias en then");yyerror("falta endif");}
break;
case 88:
//#line 312 "grama.y"
{
        int bf = Compilador.pilaSaltos.remove(Compilador.pilaSaltos.size()-1);
        completarBF(bf, Compilador.tercetos.size()+1);
    }
break;
case 89:
//#line 319 "grama.y"
{
        int bf = Compilador.pilaSaltos.remove(Compilador.pilaSaltos.size()-1);
        completarBF(bf, Compilador.tercetos.size());
    }
break;
case 90:
//#line 325 "grama.y"
{
        crearTerceto("BI", "-", "-");
         int bi = Compilador.tercetos.size() - 1;
        Compilador.pilaSaltos.add(bi);}
break;
case 91:
//#line 332 "grama.y"
{
        crearTerceto(val_peek(2).sval, val_peek(3).sval, val_peek(1).sval);
         int t = Compilador.tercetos.size() - 1;
        crearTerceto("BF", "["+t+"]", "-");
         int bf = Compilador.tercetos.size() - 1;
        Compilador.pilaSaltos.add(bf);
        yyval = new ParserVal("[" + t + "]");
    }
break;
case 92:
//#line 340 "grama.y"
{yyerror("falta cerrar parentesis");}
break;
case 93:
//#line 341 "grama.y"
{yyerror("falta abrir parentesis");}
break;
case 94:
//#line 342 "grama.y"
{yyerror("faltan parentesis");}
break;
case 95:
//#line 347 "grama.y"
{yyval=new ParserVal("==");}
break;
case 96:
//#line 348 "grama.y"
{yyval=new ParserVal("=!");}
break;
case 97:
//#line 349 "grama.y"
{yyval=new ParserVal("<=");}
break;
case 98:
//#line 350 "grama.y"
{yyval=new ParserVal(">=");}
break;
case 99:
//#line 351 "grama.y"
{yyval=new ParserVal("<");}
break;
case 100:
//#line 352 "grama.y"
{yyval=new ParserVal(">");}
break;
case 101:
//#line 353 "grama.y"
{yyerror("en condicion");}
break;
case 104:
//#line 367 "grama.y"
{
        hayReturn = false;
        String ambito = Compilador.getAmbito();
        String var = val_peek(1).sval;
        if(TablaDeSimbolos.agregarVar(var, ambito, tipo, "nombre de funcion")){
           yyval = new ParserVal(var+ambito);
        } else yyerror("La funcion "+var+" ya fue declarada");

        Compilador.entrarAmbito(val_peek(1).sval);
    }
break;
case 105:
//#line 377 "grama.y"
{
        crearTerceto("inicio de funcion", val_peek(4).sval, "-");
    }
break;
case 106:
//#line 380 "grama.y"
{
        if (!hayReturn){
            yyerror("falta return en la funcion "+ val_peek(8).sval);
        }
        hayReturn = true;
        crearTerceto("fin de funcion", val_peek(8).sval, "-");
        Compilador.salirAmbito();
    }
break;
case 107:
//#line 388 "grama.y"
{ yyerror("falta declarar nombre de funcion");}
break;
case 108:
//#line 389 "grama.y"
{yyerror("faltan parametros formales");}
break;
case 109:
//#line 390 "grama.y"
{yyerror("falta declarar nombre de funcion"); yyerror("faltan parametros formales");}
break;
case 110:
//#line 395 "grama.y"
{
        String ambito = Compilador.getAmbito();
        String var = val_peek(0).sval;
        if(TablaDeSimbolos.agregarVar(var, ambito, tipo, "nombre de variable", "copia resultado")){
           yyval = new ParserVal(var+ambito);
        } else yyerror("La variable "+var+" ya fue declarada");
    }
break;
case 111:
//#line 403 "grama.y"
{
        String ambito = Compilador.getAmbito();
        String var = val_peek(0).sval;
        if(TablaDeSimbolos.agregarVar(var, ambito, tipo, "nombre de variable","copia valor")){
           yyval = new ParserVal(var+ambito);
        } else yyerror("La variable "+var+" ya fue declarada");
    }
break;
case 112:
//#line 410 "grama.y"
{
        String ambito = Compilador.getAmbito();
        String var = val_peek(0).sval;
        if(TablaDeSimbolos.agregarVar(var, ambito, tipo, "nombre de variable", "copia resultado")){
           yyval = new ParserVal(var+ambito);
        } else yyerror("La variable "+var+" ya fue declarada");
    }
break;
case 113:
//#line 417 "grama.y"
{
        String ambito = Compilador.getAmbito();
        String var = val_peek(0).sval;
        if(TablaDeSimbolos.agregarVar(var, ambito, tipo, "nombre de variable", "copia valor")){
           yyval = new ParserVal(var+ambito);
        } else yyerror("La variable "+var+" ya fue declarada");
    }
break;
case 114:
//#line 424 "grama.y"
{yyerror("error en parametro formal");}
break;
case 115:
//#line 428 "grama.y"
{tipo = "uint";}
break;
case 120:
//#line 444 "grama.y"
{
        if (hayReturn){
            yyerror("no se permite este return");
        }else{
            if (Compilador.getAmbito().lastIndexOf(":") == 0){ /*pregunto si estoy en main*/
                yyerror("no se permite este return");
            }
            hayReturn = true;
        }
        addEstructura("return");
        crearTerceto("return", val_peek(2).sval, "-");
        int t = Compilador.tercetos.size() - 1;
        yyval=val_peek(2);
        }
break;
case 122:
//#line 466 "grama.y"
{yyerror("falta cuerpo sentencias");}
break;
case 123:
//#line 467 "grama.y"
{yyerror("falta de ;");}
break;
case 124:
//#line 469 "grama.y"
{ yyerror("falta while");}
break;
case 125:
//#line 470 "grama.y"
{yyerror("falta cuerpo sentencias"); yyerror("falta while");}
break;
case 126:
//#line 471 "grama.y"
{yyerror("falta de ) en condicion");yyerror("falta de ;");yyerror("falta while");}
break;
case 127:
//#line 472 "grama.y"
{yyerror("falta de ) en condicion");yyerror("falta de ;");yyerror("falta cuerpo sentencias");yyerror("falta while");}
break;
case 128:
//#line 475 "grama.y"
{Compilador.pilaSaltos.add(Compilador.tercetos.size());}
break;
case 129:
//#line 478 "grama.y"
{
        int salto = Compilador.pilaSaltos.remove(0);
        crearTerceto("BI", "[" +salto+"]", "-");
    }
break;
case 130:
//#line 486 "grama.y"
{
        Compilador.entrarAmbito("ua"+cantUnidadesAnonimas);
        cantUnidadesAnonimas+=1;
    }
break;
case 131:
//#line 492 "grama.y"
{Compilador.salirAmbito();hayReturn = false;}
break;
case 132:
//#line 493 "grama.y"
{yyerror("no hay sentencias dentro de las llaves");}
break;
case 133:
//#line 494 "grama.y"
{Compilador.salirAmbito();}
break;
case 134:
//#line 495 "grama.y"
{yyerror("Error en sentencia");}
break;
case 135:
//#line 501 "grama.y"
{
        yyval= new ParserVal("lambda");
        crearTerceto("fin de lambda", "-", "-");
        Compilador.salirAmbito();
    }
break;
case 136:
//#line 506 "grama.y"
{ yyerror("falta abrir llave en cuerpo de sentencia lambda");}
break;
case 137:
//#line 507 "grama.y"
{yyerror("falta cerrar llave en cuerpo de sentencia lambda");}
break;
case 138:
//#line 508 "grama.y"
{ yyerror("faltan llaves en cuerpo de sentencia lambda");}
break;
case 141:
//#line 518 "grama.y"
{
        String ambito = Compilador.getAmbito();
        String var = val_peek(0).sval;
        if(TablaDeSimbolos.agregarVar(var, ambito, tipo, "nombre de variable")){
           yyval = new ParserVal(var+ambito);
        } else yyerror("La variable "+var+" ya fue declarada");
        crearTerceto("inicio de lambda", "-", "-");
    }
break;
case 142:
//#line 531 "grama.y"
{
        asigMultiple(val_peek(3).sval,val_peek(1).sval);
    }
break;
case 144:
//#line 538 "grama.y"
{yyval=new ParserVal(val_peek(2).sval + "," + val_peek(0).sval);}
break;
case 145:
//#line 539 "grama.y"
{yyerror("falta , en lista de variables");}
break;
case 146:
//#line 543 "grama.y"
{yyval=val_peek(0);}
break;
case 147:
//#line 544 "grama.y"
{yyval=new ParserVal(val_peek(2).sval + "," + val_peek(0).sval);}
break;
case 148:
//#line 545 "grama.y"
{yyerror("falta , en lista de constantes");}
break;
//#line 1592 "Parser.java"
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
