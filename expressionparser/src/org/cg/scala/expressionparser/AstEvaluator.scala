package org.cg.scala.expressionparser

sealed abstract class AstNode

case class AstStructuralNonTerminal(name: String, children: List[AstNode]) extends AstNode
case class AstNonTerminal(symbol: Token, children: List[AstNode]) extends AstNode
case class AstTerminal(symbol: Token) extends AstNode

object AstEvaluator extends ExprEvaluator[AstNode] {
  
  def trace(v: String) = { System.out.println(v) }
  
  override def evalConst(const: Id): AstNode = {
    AstTerminal(const)
  }

  override def evalUnOp(arg: AstNode, op: Op): AstNode = {
    AstNonTerminal(op, List(arg))
  }

  override def evalRelOp(val1: Token, op: Op, val2: Token): AstNode = {
    AstNonTerminal(op, List(AstTerminal(val1), AstTerminal(val2)))
  }

  override def evalBinOpBoolean(arg1: AstNode, op: Op, arg2: AstNode): AstNode = {
    AstNonTerminal(op, List(arg1, arg2))
  }

  override def evalFunc(name: Id, params: List[Token]): AstNode = {
    AstStructuralNonTerminal("function call",
      List(AstStructuralNonTerminal("function name", List(AstTerminal(name))),
        AstStructuralNonTerminal("parameter list", params.map((x) => AstTerminal(x)))))
  }

  def print(node: AstNode): String = {
    {
      node match {
        case AstTerminal(v) => v.token
        case AstNonTerminal(v, children) => "%s (%s)".format(v.token, children.foldLeft("")((x, y) => x + " " + print(y)))
        case AstStructuralNonTerminal(name, children) => print(AstNonTerminal(Id(name), children))
         
      } 
    }
  }  
}