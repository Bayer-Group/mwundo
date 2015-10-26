package com.monsanto.labs.mwundo

/**
 * Created by Ryan Richt on 10/26/15
 */
import javax.swing.JPanel

import com.vividsolutions.jts.geom.GeometryFactory

case class GeoJsonViewer[G <: GeoJson.Geometry : Java2Dable : GeoTransformer](geos: Seq[G]) extends JPanel {
  import java.awt.Graphics
  import java.awt.Graphics2D

  import javax.swing.JFrame

  val windowWidth = 700
  val windowHeight = 700

  override def paint(g: Graphics) = {

    val transformer = implicitly[GeoTransformer[G]]

    val maxY = geos.map( geo => transformer.maxY(geo) ).max
    val minX = geos.map( geo => transformer.minX(geo) ).min
    val maxX = geos.map( geo => transformer.maxX(geo) ).max
    val minY = geos.map( geo => transformer.minY(geo) ).min

    val upScale = 700.0 / Math.max( maxX - minX, maxY - minY )
    val aspectRatio = (maxX - minX) / (maxY - minY)


    val gf = new GeometryFactory()
    val translated = geos.map{ geo =>

      val translatedCoords1 = transformer.scale(-1 * upScale, upScale)(geo)
      transformer.translate(upScale * minX + windowHeight * aspectRatio, -1 * upScale * minY)(translatedCoords1)
    }

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
