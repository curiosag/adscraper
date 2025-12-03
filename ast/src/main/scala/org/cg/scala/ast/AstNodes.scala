package org.cg.scala.ast
import scala.collection.JavaConverters._
/**
  * Created by ssmertnig on 5/3/17.
  */
class Token(val v: String)
{
  def value() = v
  def token = v
}

case class Id(override val value: String) extends Token(value)

sealed abstract class AstNode

case class AstStructuralNonTerminal(name: String, children: List[AstNode]) extends AstNode
case class AstNonTerminal(symbol: Token, children: List[AstNode]) extends AstNode
case class AstTerminal(symbol: Token) extends AstNode

object AstNodes{
  def toScala(list: java.util.List[AstNode]): List[AstNode] = list.asScala.toList
}
