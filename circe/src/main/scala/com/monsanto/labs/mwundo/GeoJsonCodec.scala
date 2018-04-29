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

  import com.monsanto.labs.mwundo.GeoJson._

  implicit val coordinateEncoder: Encoder[GeoJson.Coordinate] = Encoder.instance {  coordinate =>
    Array[BigDecimal](coordinate.x, coordinate.y).asJson
  }

  implicit val coordinateDecoder: Decoder[GeoJson.Coordinate] = Decoder.instance { cursor =>
    cursor.as[Array[BigDecimal]] match {
      case Right(coords) if coords.length == 2 => Right(Coordinate(coords(0), coords(1)))
      case _ => Left(DecodingFailure("Decoding error.  Coordinates must be an 'x' and a 'y' in array form.", cursor.history))
    }
  }


  private def encoder[A, G <: GeoJson.Geometry with Coords[A]](geometry: GeoJson.Geometry with Coords[A])(implicit coordinateEncoder: Encoder[A]): Json =
    Json.obj(
      ("type", geometry.`type`.asJson),
      ("coordinates", geometry.coordinates.asJson) )

  implicit val pointEncoder: Encoder[GeoJson.Point] = Encoder.instance[GeoJson.Point] { point: Point => encoder(point) }
  implicit val pointDecoder: Decoder[Point] = deriveDecoder[Point]

  implicit val multiPointEncoder: Encoder[GeoJson.MultiPoint] = Encoder.instance[GeoJson.MultiPoint] { multiPoint: MultiPoint => encoder(multiPoint) }
  implicit val multiPointDecoder: Decoder[GeoJson.MultiPoint] = deriveDecoder[GeoJson.MultiPoint]

  implicit val lineStringEncoder: Encoder[GeoJson.LineString] = Encoder.instance[GeoJson.LineString] { lineString: LineString => encoder(lineString) }
  implicit val lineStringDecoder: Decoder[GeoJson.LineString] = deriveDecoder[GeoJson.LineString]

  implicit val multiLineStringEncoder: Encoder[GeoJson.MultiLineString] = Encoder.instance[GeoJson.MultiLineString] { multiLineString: MultiLineString => encoder(multiLineString) }
  implicit val multiLineStringDecoder: Decoder[GeoJson.MultiLineString] = deriveDecoder[GeoJson.MultiLineString]

  implicit val polygonEncoder: Encoder[GeoJson.Polygon] = Encoder.instance[GeoJson.Polygon] { polygon: Polygon => encoder(polygon) }
  implicit val polygonDecoder: Decoder[GeoJson.Polygon] = deriveDecoder[GeoJson.Polygon]

  implicit val multiPolygonEncoder: Encoder[GeoJson.MultiPolygon] = Encoder.instance[GeoJson.MultiPolygon] { multiPolygon: MultiPolygon => encoder(multiPolygon) }
  implicit val multiPolygonDecoder: Decoder[GeoJson.MultiPolygon] = deriveDecoder[GeoJson.MultiPolygon]

  implicit def toGeometryCollectionEncoder[G <: GeoJson.Geometry](implicit geometryEncoder:Encoder[G]): Encoder[GeoJson.GeometryCollection[G]] =
    Encoder.instance[GeometryCollection[G]] { geometryCollection: GeometryCollection[G] =>
      Json.obj(
        ("type", geometryCollection.`type`.asJson),
        ("geometries", geometryCollection.geometries.asJson) )
    }

  implicit def toGeometryCollectionDecoder[G <: GeoJson.Geometry](implicit geometryDecoder: Decoder[G]): Decoder[GeoJson.GeometryCollection[G]] =
    Decoder.instance[GeometryCollection[G]] { cursor: HCursor =>
      import cats.syntax.either._

      for {
        geometries <- cursor.downField("geometries").as[Seq[G]]
      } yield {
        GeometryCollection(geometries)
      }
    }

  implicit def toFeatureEncoder[G <: GeoJson.Geometry, P](implicit propertiesEncoder: Encoder[P], geometryEncoder:Encoder[G]): Encoder[GeoJson.Feature[G, P]] =
    Encoder.instance[Feature[G, P]] { feature: Feature[G, P] =>
      Json.obj(
        ("geometry", feature.geometry.asJson),
        ("properties", feature.properties.asJson),
        ("id", feature.id.asJson) )
    }

  implicit def toFeatureDecoder[G <: GeoJson.Geometry, P](implicit propertiesDecoder: Decoder[P], geometryDecoder:Decoder[G]): Decoder[GeoJson.Feature[G, P]] =
    Decoder.instance[Feature[G, P]] { cursor: HCursor =>
      import cats.syntax.either._

      for {
        geometry <- cursor.downField("geometry").as[G]
        properties <- cursor.downField("properties").as[P]
        id <- cursor.downField("id").as[Option[String]]
      } yield {
        Feature(geometry, properties, id)
      }
    }

  implicit def toFeatureCollectionEncoder[G <: GeoJson.Geometry, P](implicit propertiesEncoder: Encoder[P], geometryEncoder:Encoder[G]): Encoder[GeoJson.FeatureCollection[G, P]] =
    Encoder.instance[FeatureCollection[G, P]] { feature: FeatureCollection[G, P] =>
      Json.obj(
        ("type", feature.`type`.asJson),
        ("features", feature.features.asJson) )
    }


  implicit def toFeatureCollectionDecoder[G <: GeoJson.Geometry, P](implicit propertiesDecoder: Decoder[P], geometryDecoder:Decoder[G]): Decoder[GeoJson.FeatureCollection[G, P]] =
    Decoder.instance[FeatureCollection[G, P]] { cursor: HCursor =>
      import cats.syntax.either._

      for {
        features <- cursor.downField("features").as[Seq[Feature[G, P]]]
      } yield {
        FeatureCollection(features)
      }
    }

}
