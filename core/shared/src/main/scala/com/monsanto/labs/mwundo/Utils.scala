package com.monsanto.labs.mwundo



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

}
