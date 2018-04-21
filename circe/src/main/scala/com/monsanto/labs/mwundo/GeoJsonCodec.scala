package com.monsanto.labs.mwundo

/**
  * Circe json marshallers for GeoJSON spec: http://geojson.org/geojson-spec.html
  */
// scalastyle:off number.of.types
// scalastyle:off number.of.methods
object GeoJsonCodec  {

  import io.circe._
  import io.circe.generic.semiauto._
  import io.circe.syntax._
  import io.circe.Decoder.Result

  import com.monsanto.labs.mwundo.GeoJson.{Geometry, _}

  implicit val coordinateEncoder: Encoder[GeoJson.Coordinate] = deriveEncoder[GeoJson.Coordinate]
  implicit val coordinateDecoder: Decoder[GeoJson.Coordinate] = deriveDecoder[GeoJson.Coordinate]

  implicit val pointEncoder: Encoder[GeoJson.Point] = deriveEncoder[GeoJson.Point]
  implicit val pointDecoder: Decoder[GeoJson.Point] = deriveDecoder[GeoJson.Point]

  implicit val multiPointEncoder: Encoder[GeoJson.MultiPoint] = deriveEncoder[GeoJson.MultiPoint]
  implicit val multiPointDecoder: Decoder[GeoJson.MultiPoint] = deriveDecoder[GeoJson.MultiPoint]

  implicit val lineStringEncoder: Encoder[GeoJson.LineString] = deriveEncoder[GeoJson.LineString]
  implicit val lineStringDecoder: Decoder[GeoJson.LineString] = deriveDecoder[GeoJson.LineString]

  implicit val multiLineStringEncoder: Encoder[GeoJson.MultiLineString] = deriveEncoder[GeoJson.MultiLineString]
  implicit val multiLineStringDecoder: Decoder[GeoJson.MultiLineString] = deriveDecoder[GeoJson.MultiLineString]

  implicit val polygonEncoder: Encoder[GeoJson.Polygon] = deriveEncoder[GeoJson.Polygon]
  implicit val polygonDecoder: Decoder[GeoJson.Polygon] = deriveDecoder[GeoJson.Polygon]

  implicit val multiPolygonEncoder: Encoder[GeoJson.MultiPolygon] = deriveEncoder[GeoJson.MultiPolygon]
  implicit val multiPolygonDecoder: Decoder[GeoJson.MultiPolygon] = deriveDecoder[GeoJson.MultiPolygon]


  implicit def toGeometryCollectionEncoder[G <: Geometry](implicit geometryEncoder:Encoder[G]): Encoder[GeometryCollection[G]] =
    new Encoder[GeometryCollection[G]] {
      override def apply(a: GeometryCollection[G]): Json =
        Json.obj( ("type", a.`type`.asJson),
                  ("geometries", a.geometries.asJson) )

    }

  implicit def toGeometryCollectionDecoder[G <: Geometry](implicit geometryDecoder: Decoder[G]): Decoder[GeometryCollection[G]] =
    new Decoder[GeometryCollection[G]] {
      override def apply(c: HCursor): Result[GeometryCollection[G]] = {
        for {
          geometries <- c.downField("geometries").as[Seq[G]]
        } yield {
          GeometryCollection(geometries)
        }
      }
    }

  implicit def toFeatureEncoder[G <: Geometry, P](implicit propertiesEncoder: Encoder[P], geometryEncoder:Encoder[G]): Encoder[Feature[G, P]] =
    new Encoder[Feature[G, P]] {
      override def apply(feature: Feature[G, P]): Json =
        Json.obj( ("geometry", geometryEncoder(feature.geometry).asJson),
                  ("properties", propertiesEncoder(feature.properties).asJson),
                  ("id", feature.id.asJson) )
    }

  implicit def toFeatureDecoder[G <: Geometry, P](implicit propertiesDecoder: Decoder[P], geometryDecoder:Decoder[G]): Decoder[Feature[G, P]] =
    new Decoder[Feature[G, P]] {
      override def apply(c: HCursor): Result[Feature[G, P]] = {
        for {
          geometry <- c.downField("geometry").as[G](geometryDecoder)
          properties <- c.downField("properties").as[P](propertiesDecoder)
          id <- c.downField("id").as[Option[String]]
        } yield {
          Feature(geometry, properties, id)
        }
      }
    }

  implicit def toFeatureCollectionEncoder[G <: Geometry, P](implicit propertiesDecoder: Encoder[P], geometryEncoder:Encoder[G]): Encoder[FeatureCollection[G, P]] =
    new Encoder[FeatureCollection[G, P]] {
      override def apply(a: FeatureCollection[G, P]): Json =
        Json.obj( ("type", a.`type`.asJson),
                  ("features", a.features.asJson) )
    }

  implicit def toFeatureCollectionDecoder[G <: Geometry, P](implicit propertiesDecoder: Decoder[P], geometryDecoder:Decoder[G]): Decoder[FeatureCollection[G, P]] =
    new Decoder[FeatureCollection[G, P]] {
      override def apply(c: HCursor): Result[FeatureCollection[G, P]] =
        for {
          features <- c.downField("features").as[Seq[Feature[G, P]]]
        } yield {
          FeatureCollection(features)
        }
    }

}
