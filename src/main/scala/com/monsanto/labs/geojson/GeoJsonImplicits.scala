package com.monsanto.labs.geojson

import com.monsanto.labs.geojson.GeoJson.Feature
import com.vividsolutions.jts.geom.GeometryFactory

/**
 * Created by Ryan Richt on 10/26/15
 */


object GeoJsonImplicits {

  implicit class RichGeoJson[G <: GeoJson.Geometry : JTSGeoFormat : GeoTransformer, P](feature: Feature[G, P]) {
    lazy val asJTS = implicitly[JTSGeoFormat[G]].toJSTGeo(feature.geometry, RichGeoJson.geoFac)

    def translated(x: Double, y: Double): Feature[G, P] = {
      val translatedGeo = implicitly[GeoTransformer[G]].translate(x, y)(feature.geometry)
      Feature(translatedGeo, feature.properties, feature.id)
    }
  }

  object RichGeoJson {
    val geoFac = new GeometryFactory()
  }
}
