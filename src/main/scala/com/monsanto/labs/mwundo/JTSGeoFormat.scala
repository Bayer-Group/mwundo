package com.monsanto.labs.mwundo

import com.monsanto.labs.mwundo._
import com.vividsolutions.jts.geom.MultiPolygon
import com.vividsolutions.jts.geom.impl.CoordinateArraySequence
import com.vividsolutions.jts.geom._

/**
 * Created by Ryan Richt on 10/26/15
 */

trait JTSGeoFormat[G <: GeoJson.Geometry] {
  def toJSTGeo(g: G, gf: GeometryFactory): Geometry
  def fromJSTGeo(geo: Geometry): G
}
object JTSGeoFormat {

  implicit object MultiPolygonConverter extends JTSGeoFormat[GeoJson.MultiPolygon] {

    private def toRing(coords: Seq[GeoJson.Coordinate], gf: GeometryFactory) = {
      val uniqueCoords = coords.map(c => new Coordinate(c.x.toDouble, c.y.toDouble)).toArray
      new LinearRing(new CoordinateArraySequence(uniqueCoords :+ uniqueCoords.head), gf) // connect the ring back to the head
    }

    def toJSTGeo(g: GeoJson.MultiPolygon, gf: GeometryFactory) = gf.createMultiPolygon(
      g.coordinates.map { polyCoordLists =>
        val outerHull = polyCoordLists.head
        val interiorHoles = polyCoordLists.tail
        gf.createPolygon(toRing(outerHull, gf), interiorHoles.map(h => toRing(h, gf)).toArray)
      }.toArray
    )

    // swap X and Y going from geometries to lat/long geo's
    private def toGeoJsonCoord(c: Coordinate) = GeoJson.Coordinate(c.y, c.x)

    def fromJSTGeo(geo: Geometry): GeoJson.MultiPolygon = {

      val polys = Seq.tabulate(geo.getNumGeometries)(i => geo.getGeometryN(i).asInstanceOf[Polygon])
      val all = polys.map(p =>
        Seq(p.getExteriorRing.getCoordinates.map(toGeoJsonCoord).toSeq) ++
        Seq.tabulate(p.getNumInteriorRing)(i => p.getInteriorRingN(i).getCoordinates.map(toGeoJsonCoord).toSeq)
      )

      GeoJson.MultiPolygon(all)
    }
  }
}