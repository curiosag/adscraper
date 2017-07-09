# ExpressionParser

Parses boolean expressions and comparisons on numbers using a generic evaluator. Otherwise the Scala black magic level is trivial, the ordinary application of parser combinators. 3 evaluators are implemented for

* [Tracing](https://github.com/curiosag/expressionparser/blob/master/src/org/cg/scala/expressionparser/ExprEvaluator.scala) and a simple string representation of the parse result
* Abstract syntax tree creation [(AstEvaluator)](https://github.com/curiosag/expressionparser/blob/master/src/org/cg/scala/expressionparser/AstEvaluator.scala)
* [Boolean evaluation](https://github.com/curiosag/expressionparser/blob/master/src/org/cg/scala/expressionparser/BooleanEvaluator.scala) taking an evaluation environment as constructor parameter which interprets numbers, variable values and function results. It deals with the possibility that evaluation may fail anywhere due to missing or flawed data. It shows a pretty paranoid relationship to the parser as well.

There's a converter to generate a [DOT graph description](http://www.graphviz.org/Documentation.php) from the AST for visualization [(Ast2Dot)](https://github.com/curiosag/expressionparser/blob/master/src/org/cg/scala/expressionparser/Ast2Dot.scala). Here's [an example](https://raw.githubusercontent.com/curiosag/expressionparser/master/expression.gif).

By the way at another place there's an [implementation for a boolean evaluation environment](https://github.com/curiosag/AdScraperExpressionFilter/blob/master/src/org/cg/adscraper/exprFilter/ExprEvaluatorAdScraper.scala) operating on raw data in a web scraper environment.

That's covered:
    
    identifiers: x, y ...
    boolean literals: false, true
    numeric literals: 1, -1, 1.1 
    boolean expressions: ! false & true | (false | x)
    numeric comparisons and numeric intervals: ! 1 > 2 & 0 <= x < 100
    function call syntax, allowing only identifiers as params: passes(refvalue, filterid) & someFct(a, x, y)
    
Note that "&" and "|" have equal precedence as defined here

     <b-expression>::= <b-term> ["|"|& <b-term>]*
     <not-factor>  ::= [!] <b-factor>
     <b-factor>    ::= <booleanliteral> | <identifier> | numRelOp | numInterval | functionCall | (<b-expression>) 
     <functionCall>::= <identifier> (<identifier> [<listSep> <identifier>]*)
     <numRelOp> 	  ::= <numericTerminal> <relOp> <numericTerminal>
     <numInterval> ::= <numericTerminal> <relOp> <numericTerminal> <RelOp> <numericTerminal>
     <booleanliteral>   ::= true | false 
     <numericTerminal> ::= <identifier> | <numericLiteral>
     <relOp>				::= > | < | >= | <= | ==
     <listSep>			::= ,
