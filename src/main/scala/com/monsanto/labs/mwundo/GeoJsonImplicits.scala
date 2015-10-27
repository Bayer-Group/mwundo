package com.monsanto.labs.mwundo

import com.monsanto.labs.mwundo.GeoJson.Feature
import com.vividsolutions.jts.geom.{Geometry, GeometryFactory}

/**
 * Created by Ryan Richt on 10/26/15
 */

object GeoJsonImplicits {

  implicit class RichJTSGeometry(geom: Geometry){
    def as[G <: GeoJson.Geometry : JTSGeoFormat]: G = implicitly[JTSGeoFormat[G]].fromJSTGeo(geom)
  }

  implicit class RichGeoJsonFeature[G <: GeoJson.Geometry : JTSGeoFormat : GeoTransformer, P](feature: Feature[G, P]) {
    private val geoX = implicitly[GeoTransformer[G]]

    def asJTS = implicitly[JTSGeoFormat[G]].toJSTGeo(feature.geometry, RichGeoJsonFeature.geoFac)

    def translated(x: Double, y: Double) = feature.copy( geometry = geoX.translate(x, y)(feature.geometry) )
    def scaled(x: Double, y: Double) = feature.copy( geometry = geoX.scale(x, y)(feature.geometry) )
    def maxLat: Double = geoX.maxY(feature.geometry)
    def maxLong: Double = geoX.maxX(feature.geometry)
    def minLat: Double = geoX.minY(feature.geometry)
    def minLong: Double = geoX.minX(feature.geometry)

    def width = maxLong - minLong
    def height = maxLat - minLat

    def boundingBoxArea = width * height

    private def midLat = (minLat + maxLat) / 2.0
    def widthKm = Utils.haversineDistance(midLat, minLong, midLat, maxLong)

    private def midLong = (minLong + maxLong) / 2.0
    val heightKm = Utils.haversineDistance(minLat, midLong, maxLat, midLong)

    def translatedToOrigin = translated(-1 * maxLat, -1 * maxLong)

    def boundingBoxAreaKmSq = Utils.latLongRectangleArea(minLat, minLong, maxLat, maxLong)

    // http://www.wolframalpha.com/input/?i=1+sq+km+in+ac
    def boundingBoxAreaAcres = 247.105 * boundingBoxAreaKmSq

    def areaInAcres = asJTS.getArea / asJTS.getEnvelope.getArea * boundingBoxAreaAcres
  }

  object RichGeoJsonFeature {
    val geoFac = new GeometryFactory()
  }
}
