package com.monsanto.labs.mwundo

import com.monsanto.labs.mwundo.GeoJson._
import spray.json.DefaultJsonProtocol

/**
 * Spray json marshallers for GeoJSON spec: http://geojson.org/geojson-spec.html
 */
// scalastyle:off number.of.types
// scalastyle:off number.of.methods
object GeoJsonFormats extends DefaultJsonProtocol {
  import spray.json._

  implicit object CoordinateFormat extends JsonFormat[Coordinate] {
    def write(obj: Coordinate): JsValue = JsArray(
      JsNumber(obj.x),
      JsNumber(obj.y)
    )

    def read(json: JsValue): Coordinate = json match {
      case JsArray(is) if is.length == 2 =>
        Coordinate(is(0).convertTo[BigDecimal], is(1).convertTo[BigDecimal])
      case _ => deserializationError(s"'$json' is not a valid Coordinate")
    }
  }

  // Pass `type` and f() in explicitly to avoid using reflection
  sealed abstract class CoordFormat[T <: Coords[C] with Typed, C] (`type`: String, f: (C) => T)(implicit cFmt: JsonFormat[C])
      extends RootJsonFormat[T] {

    def write(obj: T): JsValue = JsObject(
      ("type", JsString(obj.`type`)),
      ("coordinates", obj.coordinates.toJson)
    )

    def read(json: JsValue): T = json match {
      case JsObject(jsObj) if jsObj.get("type").contains(JsString(`type`)) =>
        f(jsObj("coordinates").convertTo[C])

      case _ => deserializationError(s"'$json' is not a valid ${`type` }")
    }
  }

  implicit object PointFormat extends CoordFormat[Point, Coordinate]("Point", Point)
  implicit object MultiPointFormat extends CoordFormat[MultiPoint, Seq[Coordinate]]("MultiPoint", MultiPoint)
  implicit object LineStringFormat extends CoordFormat[LineString, Seq[Coordinate]]("LineString", LineString)
  implicit object MultiLineStringFormat extends CoordFormat[MultiLineString, Seq[Seq[Coordinate]]]("MultiLineString", MultiLineString)
  implicit object PolygonFormat extends CoordFormat[Polygon, Seq[Seq[Coordinate]]]("Polygon", Polygon)
  implicit object MultiPolygonFormat extends CoordFormat[MultiPolygon, Seq[Seq[Seq[Coordinate]]]]("MultiPolygon", MultiPolygon)


  implicit def GeometryCollectionFormat[G <: Geometry] (implicit gFmt: JsonFormat[G]) = new RootJsonFormat[GeometryCollection[G]] {

    def write(obj: GeometryCollection[G]): JsValue = JsObject(
      ("type", JsString("GeometryCollection")),
      ("geometries", obj.geometries.toJson)
    )

    def read(json: JsValue): GeometryCollection[G] = json match {
      case JsObject(jsObj) if jsObj.get("type").contains(JsString("GeometryCollection")) =>
        GeometryCollection[G](
          jsObj("geometries").convertTo[Seq[G]]
        )

      case _ => deserializationError(s"'$json' is not a valid GeometryCollection")
    }
  }

  implicit def FeatureFormat[G <: Geometry, P] (implicit gFmt: JsonFormat[G], pFmt: JsonFormat[P]) = new RootJsonFormat[Feature[G, P]] {

    def write(obj: Feature[G, P]): JsValue = {
      val withoutID = Seq(
        ("type", JsString(obj.`type`)),
        ("geometry", obj.geometry.toJson),
        ("properties", obj.properties.toJson)
      )

      JsObject(
        (
          if(obj.id.isEmpty) withoutID else withoutID :+ (("id", obj.id.toJson))
        ):_*
      )
    }

    def read(json: JsValue): Feature[G, P] =
      json match {
        case JsObject(jsObj) if jsObj.get("type").contains(JsString("Feature")) =>
          Feature[G, P](
          jsObj("geometry").convertTo[G],
          jsObj("properties").convertTo[P],
          jsObj.get("id").map(_.convertTo[String])
        )

        case _ => deserializationError(s"'$json' is not a valid Feature")
      }
  }

  implicit def FeatureCollectionFormat[G <: Geometry, P] (implicit fFmt: JsonFormat[Feature[G, P]]) = new RootJsonFormat[FeatureCollection[G, P]] {

    def write(obj: FeatureCollection[G, P]): JsValue = JsObject(
      ("type", JsString(obj.`type`)),
      ("features", obj.features.toJson)
    )

    def read(json: JsValue): FeatureCollection[G, P] = json match {
      case JsObject(p) if p.get("type").contains(JsString("FeatureCollection")) =>
        FeatureCollection[G, P](
          p("features").convertTo[Seq[Feature[G, P]]]
        )

      case _ => deserializationError(s"'$json' is not a valid FeatureCollection")
    }
  }
}
