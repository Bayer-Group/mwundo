package com.monsanto.labs.mwundo

import com.vividsolutions.jts.geom._
import collection.JavaConverters._

/**
 * Created by Ryan Richt on 10/26/15
 */
object Utils {

  private val meanRadiusOfEarthKm = 6371.0

  def surfaceAreaOfSphere(r: Double): Double = {
    latLongRectangleArea(-90D,-180D,90D,180D,r)
  }

  def latLongRectangleArea(lat1: Double, long1: Double, lat2: Double, long2: Double, r: Double = meanRadiusOfEarthKm) =
    Math.pow(r, 2.0) *
    Math.abs(
      Math.sin(Math.toRadians(lat1)) - Math.sin( Math.toRadians(lat2))
    ) *
    Math.abs(
      Math.toRadians(long1) - Math.toRadians(long2)
    )

  def haversineDistance(lat1: Double, lng1: Double, lat2: Double, lng2: Double, r: Double = meanRadiusOfEarthKm) = {
    val dLat = Math.toRadians(lat2 - lat1)
    val dLon = Math.toRadians(lng2 - lng1)
    val a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
            Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) * Math.sin(dLon / 2) * Math.sin(dLon / 2)
    val c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a))
    val d = r * c
    d
  }

  def haversineDistanceFromCoordinates(coordinate1: GeoJson.Coordinate, coordinate2: GeoJson.Coordinate, r: Double = meanRadiusOfEarthKm): Double = {
    val (c1long, c1lat) = (coordinate1.x.toDouble, coordinate1.y.toDouble)
    val (c2long, c2lat) = (coordinate2.x.toDouble, coordinate2.y.toDouble)
    haversineDistance(c1lat, c1long, c2lat, c2long, r)
  }

  //TODO: this might not work for envelopes with convexities?
  def clip(geom: Geometry, clipEnv: Envelope): GeometryCollection = {
    val clipPoly: Geometry = geom.getFactory.toGeometry(clipEnv)

    val geos = Seq.tabulate(geom.getNumGeometries)(i => geom.getGeometryN(i))

    // LineStrings are from clipped polygons that no longer have any area, discard them
    val clippedGeos = geos.collect{
      case g: Geometry if ! g.isInstanceOf[LineString] && clipEnv.contains(g.getEnvelopeInternal)   => g
      case g: Geometry if ! g.isInstanceOf[LineString] && clipEnv.intersects(g.getEnvelopeInternal) =>
        val intermedResult = clipPoly.intersection(g)
        intermedResult.setUserData(g.getUserData)
        intermedResult
      // case completely outside of the clipping envelope, do not include in output list
    }.collect{
      case g: Geometry if g.isInstanceOf[Polygon] => g
    }

    geom.getFactory.createGeometryCollection(GeometryFactory.toGeometryArray(clippedGeos.asJavaCollection))
  }

  //TODO: this might not work for envelopes with convexities?
  def clip(geom: Geometry, clipPoly: Geometry): GeometryCollection = {

    val geos = Seq.tabulate(geom.getNumGeometries)(i => geom.getGeometryN(i))

    // LineStrings are from clipped polygons that no longer have any area, discard them
    val clippedGeos = geos.collect{
      case g: Geometry if ! g.isInstanceOf[LineString] && clipPoly.contains(g)   => g
      case g: Geometry if ! g.isInstanceOf[LineString] && clipPoly.intersects(g) =>
        val intermedResult = clipPoly.intersection(g)
        intermedResult.setUserData(g.getUserData)
        intermedResult
      // case completely outside of the clipping envelope, do not include in output list
    }.collect {
      case g: Geometry if g.isInstanceOf[Polygon] => g
    }

    geom.getFactory.createGeometryCollection(GeometryFactory.toGeometryArray(clippedGeos.asJavaCollection))
  }
}
