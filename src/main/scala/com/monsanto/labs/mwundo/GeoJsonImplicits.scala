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

  implicit class RichGeoJsonFeature[G <: GeoJson.Geometry : JTSGeoFormat : GeoTransformer, P](feature: Feature[G, P])
    extends GeometryOps[G](feature.geometry) {

    def translatedFeature(x: Double, y: Double) = feature.copy( geometry = feature.geometry.translated(x, y) )
    def scaledFeature(    x: Double, y: Double) = feature.copy( geometry = feature.geometry.scaled(x, y)     )
  }
  object RichGeoJsonFeature {
    val geoFac = new GeometryFactory()
  }

  implicit class RichGeoJsonGeometry[G <: GeoJson.Geometry : JTSGeoFormat : GeoTransformer](val geometry: G)
    extends GeometryOps[G](geometry)

  abstract class GeometryOps[G <: GeoJson.Geometry : GeoTransformer : JTSGeoFormat](geometry: G){
    private val geoX: GeoTransformer[G] = implicitly
    private val jtsGeoFormat: JTSGeoFormat[G] = implicitly

    def asJTS = jtsGeoFormat.toJSTGeo(geometry, RichGeoJsonFeature.geoFac)

    def translated(x: Double, y: Double) = geoX.translate(x, y)(geometry)
    def scaled(x: Double, y: Double) = geoX.scale(x, y)(geometry)
    def maxLat: Double = geoX.maxY(geometry)
    def maxLong: Double = geoX.maxX(geometry)
    def minLat: Double = geoX.minY(geometry)
    def minLong: Double = geoX.minX(geometry)

    def width = maxLong - minLong
    def height = maxLat - minLat

    def boundingBoxArea = width * height

    private def midLat = (minLat + maxLat) / 2.0
    def widthKm = Utils.haversineDistance(midLat, minLong, midLat, maxLong)

    private def midLong = (minLong + maxLong) / 2.0
    val heightKm = Utils.haversineDistance(minLat, midLong, maxLat, midLong)

    def translatedToOrigin = translated(-1 * maxLong, -1 * maxLat)

    def boundingBoxAreaKmSq = Utils.latLongRectangleArea(minLat, minLong, maxLat, maxLong)

    // http://www.wolframalpha.com/input/?i=1+sq+km+in+ac
    def boundingBoxAreaAcres = 247.105 * boundingBoxAreaKmSq

    def areaInAcres = asJTS.getArea / asJTS.getEnvelope.getArea * boundingBoxAreaAcres
  }
}
