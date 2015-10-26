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

  implicit class RichGeoJson[G <: GeoJson.Geometry : JTSGeoFormat : GeoTransformer, P](feature: Feature[G, P]) {
    private val geoX = implicitly[GeoTransformer[G]]

    def asJTS = implicitly[JTSGeoFormat[G]].toJSTGeo(feature.geometry, RichGeoJson.geoFac)

    def translated(x: Double, y: Double) = feature.copy( geometry = geoX.translate(x, y)(feature.geometry) )
    def scaled(x: Double, y: Double) = feature.copy( geometry = geoX.scale(x, y)(feature.geometry) )
    def maxLat: Double = geoX.maxX(feature.geometry)
    def maxLong: Double = geoX.maxY(feature.geometry)
    def minLat: Double = geoX.minX(feature.geometry)
    def minLong: Double = geoX.minY(feature.geometry)

    def width = maxLong - minLong
    def height = maxLat - minLat
  }

  object RichGeoJson {
    val geoFac = new GeometryFactory()
  }
}
