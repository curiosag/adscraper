package org.cg.scala.expressionparser

trait ExprEvaluator[T] {
  def evalConst(const: Id): T
  def evalUnOp(arg: T, op: Op): T
  def evalRelOp(val1: Token, op: Op, val2: Token): T
  def evalBinOpBoolean(arg1: T, op: Op, arg2: T): T
  def evalFunc(name: Id, params: List[Token]): T
}

object tracingEvaluator extends ExprEvaluator[String] {
  def trace(v: String) = {System.out.println(v)}

  override def evalConst(const: Id): String = {
    trace("evalConst: " + const.token)
    const.token
  }

  override def evalUnOp(arg: String, op: Op): String = {
    val ret = "(%s %s)".format(op.token, arg)
    trace("evalUnOp: " + ret)
    ret
  }

  override def evalRelOp(val1: Token, op: Op, val2: Token): String = {
    val ret = "%s %s %s".format(val1.token, op.token, val2.token)
    trace("evalRelOp: " + ret)
    ret
  }

  override def evalBinOpBoolean(arg1: String, op: Op, arg2: String): String = {
    val ret = "(%s %s %s)".format(arg1, op.token, arg2)
    trace("evalBinOpBoolean: " + ret)
    ret
  }

  override def evalFunc(name: Id, params: List[Token]): String = {
    val ret = "function: %s params: %s".format(name.token, params.foldLeft("")((x, y) => x + " " + y.token))
    trace(ret)
    ret
  }
}