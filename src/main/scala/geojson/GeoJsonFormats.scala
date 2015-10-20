package geojson

import spray.json.DefaultJsonProtocol

/**
 * Spray json marshallers for GeoJSON spec: http://geojson.org/geojson-spec.html
 */
// scalastyle:off number.of.types
// scalastyle:off number.of.methods
object GeoJsonFormats extends DefaultJsonProtocol {
  import spray.json._
  import GeoJson._

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


  sealed abstract class GeometryCollectionFormat[G <: Geometry] (implicit gFmt: JsonFormat[G])
      extends RootJsonFormat[GeometryCollection[G]] {

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

  implicit object PointCollectionFormat extends GeometryCollectionFormat[Point]
  implicit object MultiPointCollectionFormat extends GeometryCollectionFormat[MultiPoint]
  implicit object LineStringCollectionFormat extends GeometryCollectionFormat[LineString]
  implicit object MultiLineStringCollectionFormat extends GeometryCollectionFormat[MultiLineString]
  implicit object PolygonCollectionFormat extends GeometryCollectionFormat[Polygon]
  implicit object MultiPolygonCollectionFormat extends GeometryCollectionFormat[MultiPolygon]


  sealed abstract class FeatureFormat[G <: Geometry, P] (implicit gFmt: JsonFormat[G], pFmt: JsonFormat[P])
      extends RootJsonFormat[Feature[G, P]] {

    def write(obj: Feature[G, P]): JsValue = JsObject(
      ("type", JsString(obj.`type`)),
      ("geometry", obj.geometry.toJson),
      ("properties", obj.properties.toJson),
      ("id", obj.id.toJson)
    )

    def read(json: JsValue): Feature[G, P] = json match {
      case JsObject(jsObj) if jsObj.get("type").contains(JsString("Feature")) =>
        Feature[G, P](
          jsObj("geometry").convertTo[G],
          jsObj("properties").convertTo[P],
          jsObj("id").convertTo[String]
        )

      case _ => deserializationError(s"'$json' is not a valid Feature")
    }
  }

  abstract class FeaturePointFormat[P](implicit pFmt: JsonFormat[P]) extends FeatureFormat[Point, P]
  abstract class FeatureMultiPointFormat[P](implicit pFmt: JsonFormat[P]) extends FeatureFormat[MultiPoint, P]
  abstract class FeatureLineStringFormat[P](implicit pFmt: JsonFormat[P]) extends FeatureFormat[LineString, P]
  abstract class FeatureMultiLineStringFormat[P](implicit pFmt: JsonFormat[P]) extends FeatureFormat[MultiLineString, P]
  abstract class FeaturePolygonFormat[P](implicit pFmt: JsonFormat[P]) extends FeatureFormat[Polygon, P]
  abstract class FeatureMultiPolygonFormat[P](implicit pFmt: JsonFormat[P]) extends FeatureFormat[MultiPolygon, P]

  implicit object FeaturePointMapFormat extends FeaturePointFormat[Map[String, String]]
  implicit object FeatureMultiPointMapFormat extends FeatureMultiPointFormat[Map[String, String]]
  implicit object FeatureLineStringMapFormat extends FeatureLineStringFormat[Map[String, String]]
  implicit object FeatureMultiLineStringMapFormat extends FeatureMultiLineStringFormat[Map[String, String]]
  implicit object FeaturePolygonMapFormat extends FeaturePolygonFormat[Map[String, String]]
  implicit object FeatureMultiPolygonMapFormat extends FeatureMultiPolygonFormat[Map[String, String]]


  sealed abstract class FeatureCollectionFormat[G <: Geometry, P] (implicit fFmt: FeatureFormat[G, P])
      extends RootJsonFormat[FeatureCollection[G, P]] {

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

  abstract class FeatureCollectionPointFormat[P](implicit fpFmt: FeaturePointFormat[P]) extends FeatureCollectionFormat[Point, P]
  abstract class FeatureCollectionMultiPointFormat[P](implicit fpFmt: FeatureMultiPointFormat[P]) extends FeatureCollectionFormat[MultiPoint, P]
  abstract class FeatureCollectionLineStringFormat[P](implicit fpFmt: FeatureLineStringFormat[P]) extends FeatureCollectionFormat[LineString, P]
  abstract class FeatureCollectionMultiLineStringFormat[P](implicit fpFmt: FeatureMultiLineStringFormat[P]) extends FeatureCollectionFormat[MultiLineString, P]
  abstract class FeatureCollectionPolygonFormat[P](implicit fpFmt: FeaturePolygonFormat[P]) extends FeatureCollectionFormat[Polygon, P]
  abstract class FeatureCollectionMultiPolygonFormat[P](implicit fpFmt: FeatureMultiPolygonFormat[P]) extends FeatureCollectionFormat[MultiPolygon, P]

  implicit object FeatureCollectionPointMapFormat extends FeatureCollectionPointFormat[Map[String, String]]
  implicit object FeatureCollectionMultiPointMapFormat extends FeatureCollectionMultiPointFormat[Map[String, String]]
  implicit object FeatureCollectionLineStringMapFormat extends FeatureCollectionLineStringFormat[Map[String, String]]
  implicit object FeatureCollectionMultiLineStringMapFormat extends FeatureCollectionMultiLineStringFormat[Map[String, String]]
  implicit object FeatureCollectionPolygonMapFormat extends FeatureCollectionPolygonFormat[Map[String, String]]
  implicit object FeatureCollectionMultiPolygonMapFormat extends FeatureCollectionMultiPolygonFormat[Map[String, String]]
}
