package com.monsanto.labs.mwundo;

/**
 * Created by Ryan Richt on 10/26/15
 */
trait Java2Dable[G] {
  def toJava2D(g: G): Seq[java.awt.Shape]
}
