package com.monsanto.labs.mwundo

/**
 * Created by Ryan Richt on 10/26/15
 */
import javax.swing.JPanel

import breeze.linalg.DenseMatrix
import com.vividsolutions.jts.geom.GeometryFactory

case class GeoJsonViewer[G <: GeoJson.Geometry : Java2Dable : GeoTransformer](geos: Seq[G], windowWidthMax: Int = 700, windowHeightMax: Int = 700)(implicit offset: Int = 10) extends JPanel {
  import java.awt.Graphics
  import java.awt.Graphics2D

  import javax.swing.JFrame

  val viewerBarHeight = 23
//  implicit val offset = 10

  override def paint(g: Graphics) = {

    val transformer = implicitly[GeoTransformer[G]]

    val translated = GeoJsonViewer.transformToJava2DLocalCoordinates(windowWidthMax, windowHeightMax, geos)

    val sw = implicitly[Java2Dable[G]]
    val shapes = translated.flatMap(g => sw.toJava2D(g))

    shapes.foreach{ s => g.asInstanceOf[Graphics2D].draw(s) }
  }

  def display() = {
    val scalingInfo = ScalingInformation(geos, windowHeightMax, windowWidthMax)
    val geoWidth = ((scalingInfo.maxX - scalingInfo.minX) * scalingInfo.upScale).toInt
    val geoHeight = ((scalingInfo.maxY - scalingInfo.minY) * scalingInfo.upScale).toInt
    val correctedWindowWidth = geoWidth + 2 * offset
    val correctedWindowHeight = geoHeight + 2 * offset + viewerBarHeight
    val f = new JFrame()
    f.getContentPane.add(this)
    f.setSize(correctedWindowWidth, correctedWindowHeight)
    f.setVisible(true)
  }
}

object GeoJsonViewer {
  def transformToJava2DLocalCoordinates[G <: GeoJson.Geometry : Java2Dable : GeoTransformer]
  (windowWidthMax: Double, windowHeightMax: Double, geos: Seq[G])(implicit offset: Int) = {

    val transformer = implicitly[GeoTransformer[G]]

    val scalingInfo = ScalingInformation(geos, windowHeightMax, windowWidthMax)

    geos.map{ geo =>
      val translated = transformer.translate(-1 * scalingInfo.minX, -1 * scalingInfo.minY)(geo)
      val scaled = transformer.scale(scalingInfo.upScale, -1 * scalingInfo.upScale)(translated)
      transformer.translate(offset, offset + (scalingInfo.maxY - scalingInfo.minY) * scalingInfo.upScale)(scaled)
    }
  }
}

private case class ScalingInformation[G <: GeoJson.Geometry : Java2Dable : GeoTransformer]
(
  geometries: Seq[G],
  windowHeightMax: Double,
  windowWidthMax: Double
  ){

  val transformer = implicitly[GeoTransformer[G]]

  val maxX = geometries.map( geometry => transformer.maxX(geometry) ).max
  val maxY = geometries.map( geometry => transformer.maxY(geometry) ).max
  val minX = geometries.map( geometry => transformer.minX(geometry) ).min
  val minY = geometries.map( geometry => transformer.minY(geometry) ).min

  val upScale = Math.min( windowHeightMax / (maxY - minY), windowWidthMax / (maxX - minX) )
}
