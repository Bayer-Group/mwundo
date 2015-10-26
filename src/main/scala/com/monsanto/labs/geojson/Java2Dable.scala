package com.monsanto.labs.geojson

import java.awt.geom.Point2D

import com.vividsolutions.jts.awt.PolygonShape

/**
 * Created by Ryan Richt on 10/26/15
 */
trait Java2Dable[G] {
  def toJava2D(g: G): Seq[java.awt.Shape]
}
object Java2Dable {

  private val addToRing = classOf[PolygonShape].getDeclaredMethod("addToRing", classOf[Point2D])
  addToRing.setAccessible(true)
  private val endRing = classOf[PolygonShape].getDeclaredMethod("endRing")
  endRing.setAccessible(true)

  implicit object MultiPolygonJava2D extends Java2Dable[GeoJson.MultiPolygon] {

    def toJava2D(g: GeoJson.MultiPolygon) = {
      g.coordinates.map { polygon =>
        val outPolygon = new PolygonShape()
        polygon.foreach { ring =>
          ring.foreach { coord =>
            addToRing.invoke(outPolygon, new Point2D.Double(coord.y.toDouble, coord.x.toDouble).asInstanceOf[Object]) // also swap X/Y for geo to geom
          }
          endRing.invoke(outPolygon)
        }
        outPolygon
      }
    }
  }

  implicit object PolygonJava2D extends Java2Dable[GeoJson.Polygon] {

    def toJava2D(polygon: GeoJson.Polygon) = {
      val outPolygon = new PolygonShape()
      polygon.coordinates.foreach { ring =>
        ring.foreach { coord =>
          addToRing.invoke(outPolygon, new Point2D.Double(coord.y.toDouble, coord.x.toDouble).asInstanceOf[Object]) // also swap X/Y for geo to geom
        }
        endRing.invoke(outPolygon)
      }
      Seq(outPolygon)
    }
  }
}
