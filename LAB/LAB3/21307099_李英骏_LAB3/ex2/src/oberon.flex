import java.io.*;
import exceptions.*;

%%
%class OberonScanner

%public
%eofval{
	return Symbol.EOF;
%eofval}
%ignorecase
%line

%column
%type int
%yylexthrow LexicalException
Number = 0[0-7]*  |  [1-9][0-9]*
Identifier = [:jletter:][:jletterdigit:]*
WhiteSpace = " " | \r | \n | \r\n | [ \t\f]
Comment = "(*" ~ "*)"
IllegalComment = "(*" ([^\*] | "*"+[^\)])* | ([^\(]|"("+[^\*])* "*)"
IllegalOctal = 0[0-7]*[9|8]+[0-9]*
IllegalInteger = {Number} + {Identifier}+

%%

<YYINITIAL> {

        "MODULE"                       { return Symbol.MODULE; }
        "BEGIN"                        { return Symbol.BEGIN; }
        "END"                          { return Symbol.END; }
        "CONST"                        { return Symbol.CONST; }
        "TYPE"                         { return Symbol.TYPE; }
        "VAR"                          { return Symbol.VAR; }
        "PROCEDURE"                    { return Symbol.PROCEDURE; }
        "RECORD"                       { return Symbol.RECORD; }
        "ARRAY"                        { return Symbol.ARRAY; }
        "OF"                           { return Symbol.OF; }
        "WHILE"                        { return Symbol.WHILE; }
        "DO"                           { return Symbol.DO; }
        "IF"                           { return Symbol.IF; }
        "ELSE"                         { return Symbol.ELSE; }
        "ELSIF"                        { return Symbol.ELSIF; }
        "THEN"                         { return Symbol.THEN; }
        "BOOLEAN"                      { return Symbol.BOOLEAN; }
        "INTEGER"                      { return Symbol.INTEGER; }
        "TRUE"                         { return Symbol.TRUE; }
        "FALSE"                        { return Symbol.FALSE; }
        ";"                            { return Symbol.SEMICOLON; }
        "."                            { return Symbol.POINT; }
        ":"                            { return Symbol.COLON; }
        "("                            { return Symbol.LeftParenthesis; }
        ")"                            { return Symbol.RightParenthesis; }
        "["                            { return Symbol.LeftBracket; }
        "]"                            { return Symbol.RightBracket; }
        ","                            { return Symbol.COMMA; }
        "DIV"                          { return Symbol.DIV; }
        "MOD"                          { return Symbol.MOD; }
        "OR"                           { return Symbol.OR; }
        "="                            { return Symbol.EQUAL; }
        ":="                           { return Symbol.ASSIGNMENT; }
        ">"                            { return Symbol.GreatThan; }
        "<"                            { return Symbol.LessThan; }
        "~"                            { return Symbol.NOT; }
        "<="                           { return Symbol.LessThanOrEqual; }
        ">="                           { return Symbol.GreatThanOrEqual; }
        "#"                            { return Symbol.NOTEQUAL; }
        "&"                            { return Symbol.AND; }
        "+"                            { return Symbol.PLUS; }
        "-"                            { return Symbol.MINUS; }
        "*"                            { return Symbol.TIMES ; }
        "READ"		                 { return Symbol.READ;}
        "WRITE"		                 { return Symbol.WRITE;}
        "WRITELN"		                 { return Symbol.WRITELN;}
        {WhiteSpace}                   {  }
        {Comment}                      {  }
        {IllegalComment}               { throw new MismatchedCommentException(); }
        {Identifier}                   {
                                         if(yylength() > 24) throw new IllegalIdentifierLengthException();
                                         else return Symbol.IDENTIFIER;
                                     }
        {Number}                       {
                                         if(yylength() > 12) throw new IllegalIntegerRangeException();
                                         else return Symbol.IDENTIFIER;
                                     }
        {IllegalInteger}               { throw new IllegalIntegerException(); }
        {IllegalOctal}                 { throw new IllegalOctalException(); }
        .                              { throw new IllegalSymbolException(); }
        [^]		                         {throw new IllegalSymbolException();}
}
