grammar Expr ;
@header {
package com.lifeng.parser.antlr;
}
prog : stat+ ;
stat : expr NEWLINE         # printExpr
     | ID '=' expr NEWLINE  # assign
     | NEWLINE              # blank
     ;

expr : expr op=('*'|'/') expr  # MultiDiv
     | expr op=('+'|'-') expr  # AddSub
     | ID                      # id
     | INT                     # int
     | '(' expr ')'            # parents
     ;

INT : [0-9]+ ;
ID : [a-zA-Z]+ ;
NEWLINE : '\r'? '\n' ;
WS : [ \t]+ -> skip ;

MUL : '*' ;
DIV : '/' ;
ADD : '+' ;
SUB : '-' ;
