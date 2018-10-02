package com.monsanto.labs.mwundo

import java.awt.geom.Point2D

import org.locationtech.jts.awt.PolygonShape

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
    /**
     * converts GeoJson MultiPolygon into JTS MultiPolygon
     * @param g
     * @return
     */
    def toJava2D(g: GeoJson.MultiPolygon) = {
      g.coordinates.map { polygon =>
        val outPolygon = new PolygonShape()
        polygon.foreach { ring =>
          ring.foreach { coord =>
            addToRing.invoke(outPolygon, new Point2D.Double(coord.x.toDouble, coord.y.toDouble).asInstanceOf[Object])
          }
          endRing.invoke(outPolygon)
        }
        outPolygon
      }
    }
  }

  implicit object PolygonJava2D extends Java2Dable[GeoJson.Polygon] {

    /**
     * converts GeoJson Polygon into JTS Polygon
     * @param polygon
     * @return
     */
    def toJava2D(polygon: GeoJson.Polygon) = {
      val outPolygon = new PolygonShape()
      polygon.coordinates.foreach { ring =>
        ring.foreach { coord =>
          addToRing.invoke(outPolygon, new Point2D.Double(coord.x.toDouble, coord.y.toDouble).asInstanceOf[Object])
        }
        endRing.invoke(outPolygon)
      }
      Seq(outPolygon)
    }
  }
}
