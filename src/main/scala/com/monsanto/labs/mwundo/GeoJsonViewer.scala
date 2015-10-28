package com.monsanto.labs.mwundo

/**
 * Created by Ryan Richt on 10/26/15
 */
import javax.swing.JPanel

import breeze.linalg.DenseMatrix
import com.vividsolutions.jts.geom.GeometryFactory

case class GeoJsonViewer[G <: GeoJson.Geometry : Java2Dable : GeoTransformer](geos: Seq[G]) extends JPanel {
  import java.awt.Graphics
  import java.awt.Graphics2D

  import javax.swing.JFrame

  val windowWidth = 700
  val windowHeight = 700

  override def paint(g: Graphics) = {

    val transformer = implicitly[GeoTransformer[G]]

    val translated = GeoJsonViewer.transformToJava2DLocalCoordinates(windowWidth, windowHeight, geos)

    val sw = implicitly[Java2Dable[G]]
    val shapes = translated.flatMap(g => sw.toJava2D(g))

    shapes.foreach{ s => g.asInstanceOf[Graphics2D].draw(s) }
  }

  def display() = {
    val f = new JFrame()
    f.getContentPane.add(new GeoJsonViewer(geos))
    f.setSize(windowWidth, windowHeight)
    f.setVisible(true)
  }
}

object GeoJsonViewer {
  def transformToJava2DLocalCoordinates[G <: GeoJson.Geometry : Java2Dable : GeoTransformer]
  (windowWidth: Double, windowHeight: Double, geos: Seq[G]) = {

    val transformer = implicitly[GeoTransformer[G]]

    val maxY = geos.map( geo => transformer.maxY(geo) ).max
    val minX = geos.map( geo => transformer.minX(geo) ).min
    val maxX = geos.map( geo => transformer.maxX(geo) ).max
    val minY = geos.map( geo => transformer.minY(geo) ).min

    val upScale = Math.min( windowHeight / (maxY - minY), windowWidth / (maxX - minX) )

    geos.map{ geo =>
      val translated = transformer.translate(-1 * minX, -1 * minY)(geo)
      val scaled = transformer.scale(upScale, -1 * upScale)(translated)
      transformer.translate(0, windowHeight)(scaled)
    }
  }
}
