package com.monsanto.labs.mwundo

import com.monsanto.labs.mwundo.GeoJson._
import io.circe.generic.semiauto.{deriveDecoder, deriveEncoder}
import io.circe.{Decoder, Encoder, Json}
import io.circe.syntax._
import io.circe.parser._
import org.scalatest.{FunSpec, Matchers, ParallelTestExecution}

class GeoJsonCodecTest extends FunSpec with Matchers with ParallelTestExecution {

  import GeoJsonCodec._
  import cats.syntax.either._

  private def marshalAndUnmarshal[T](t: T)(implicit encoder: Encoder[T], decoder: Decoder[T]) = {
    val json = t.asJson
    val result = json.as[T]

    result.toTry.get should be (t)
    result.toTry.get.asJson.toString() should be (json.toString())
  }

  private def extractGeometryTypeString(json: Json) =
    json.asObject.get.apply("type").get.as[String].toOption.get

  case class MyProps(hi: String)
  object MyProps {
    implicit val encoder: Encoder[MyProps] = deriveEncoder[MyProps]
    implicit val decoder: Decoder[MyProps] = deriveDecoder[MyProps]
  }

  describe(" GeoJson `type` information") {
    it("should carry the geometry type in the json for a Point") {
      val point = Point(Coordinate(0.1, 2.0))

      val json = point.asJson

      extractGeometryTypeString(json) should be(point.`type`)
    }

    it("should carry the geometry type in the json for a MultiPoint") {
      val multiPoint = MultiPoint(Seq(Coordinate(0.1, 2.0), Coordinate(1.1, 2.1)))

      val json = multiPoint.asJson

      extractGeometryTypeString(json) should be(multiPoint.`type`)
    }

    it("should carry the geometry type in the json for a LineString") {
      val lineString = LineString(Seq(Coordinate(0.1, 2.0), Coordinate(1.1, 2.1)))

      val json = lineString.asJson

      extractGeometryTypeString(json) should be(lineString.`type`)
    }

    it("should carry the geometry type in the json for a MultiLineString") {
      val multiLineString = MultiLineString(Seq(
        Seq(Coordinate(0.1, 2.0), Coordinate(1.1, 2.1)),
        Seq(Coordinate(0.1, 2.0), Coordinate(1.1, 2.1))
      ))

      val json = multiLineString.asJson

      extractGeometryTypeString(json) should be(multiLineString.`type`)
    }

    it("should carry the geometry type in the json for a Polygon") {
      val polygon = Polygon(Seq(
        Seq(Coordinate(0.1, 2.0), Coordinate(1.1, 2.1)),
        Seq(Coordinate(0.1, 2.0), Coordinate(1.1, 2.1))
      ))

      val json = polygon.asJson

      extractGeometryTypeString(json) should be(polygon.`type`)
    }

    it("should carry the geometry type in the json for a MultiPolygon") {
      val multiPolygon = MultiPolygon(Seq(
        Seq(
          Seq(Coordinate(0.1, 2.0), Coordinate(1.1, 2.1)),
          Seq(Coordinate(0.1, 2.0), Coordinate(1.1, 2.1))),
        Seq(
          Seq(Coordinate(0.1, 2.0), Coordinate(1.1, 2.1)),
          Seq(Coordinate(0.1, 2.0), Coordinate(1.1, 2.1)))
      ))

      val json = multiPolygon.asJson

      extractGeometryTypeString(json) should be(multiPolygon.`type`)
    }

    it("should carry the geometry type in the json for a GeometryCollection") {
      val mulitPolygonCollection = GeometryCollection(Seq(
        MultiPolygon(Seq(
          Seq(
            Seq(Coordinate(0.1, 2.0), Coordinate(1.1, 2.1)),
            Seq(Coordinate(0.1, 2.0), Coordinate(1.1, 2.1))),
          Seq(
            Seq(Coordinate(0.1, 2.0), Coordinate(1.1, 2.1)),
            Seq(Coordinate(0.1, 2.0), Coordinate(1.1, 2.1)))
        )),
        MultiPolygon(Seq(
          Seq(
            Seq(Coordinate(0.1, 2.0), Coordinate(1.1, 2.1)),
            Seq(Coordinate(0.1, 2.0), Coordinate(1.1, 2.1))),
          Seq(
            Seq(Coordinate(0.1, 2.0), Coordinate(1.1, 2.1)),
            Seq(Coordinate(0.1, 2.0), Coordinate(1.1, 2.1)))
        ))
      ))

      val json = mulitPolygonCollection.asJson

      extractGeometryTypeString(json) should be(mulitPolygonCollection.`type`)
    }

    it("does something") {
      val shape1json =
        """
          |{ "type": "Feature", "properties": { "id": "1234" }, "geometry": { "type": "Polygon", "coordinates":
          |[ [ [ 127.083301191389481, 56.37455697640776 ], [ 127.083301191389481, 56.444169331058909 ],
          |[ 127.122110418348996, 56.443626544667872 ], [ 127.123603080924354, 56.479179053281129 ],
          |[ 127.165126239839083, 56.479043356683363 ],
          |[ 127.161733824895066, 56.403053261937472 ], [ 127.138122616884729, 56.401153509568822],
          |[ 127.137172740700407, 56.376185335580885 ], [ 127.083301191389481, 56.37455697640776 ] ] ] } }
        """.stripMargin

      val json = io.circe.parser.parse(shape1json).right.get
      val f = json.as[Feature[Polygon,Map[String,String]]]
    assert(f.isRight)
    }
  }

  describe("GeoJson support"){

    it("should convert a Coordinate to an array") {
      val coordinate = Coordinate(0.1, 2.0)

      val coordinateJson = coordinate.asJson

      coordinateJson.isArray should be (true)
      coordinateJson.noSpaces should be ("[0.1,2.0]")
    }

    it("should convert a JSON Array of size two to a Coordinate") {
      val jsonArrayAsString = "[0.1,2.0]"

      val jsonArray = parse(jsonArrayAsString).right.get
      val coordinate = jsonArray.as[Coordinate].toTry.get

      coordinate should be (Coordinate(0.1, 2.0))
    }

    it("should NOT convert a JSON Array of size 0 to a Coordinate") {
      val jsonArrayAsString = "[]"

      val jsonArray = parse(jsonArrayAsString).right.get

      jsonArray.as[Coordinate].toTry.isFailure should be (true)
    }

    it("should NOT convert a JSON Array of size 1 to a Coordinate") {
      val jsonArrayAsString = "[0.1]"

      val jsonArray = parse(jsonArrayAsString).right.get

      jsonArray.as[Coordinate].toTry.isFailure should be (true)
    }

    it("should NOT convert a JSON Array of size greater than 2 to a Coordinate") {
      val jsonArrayAsString = "[0.1,0.1,0.1]"

      val jsonArray = parse(jsonArrayAsString).right.get

      jsonArray.as[Coordinate].toTry.isFailure should be (true)
    }

    it("should marshal and unmarshal Coordinate") {
      val coordinate = Coordinate(0.1, 2.0)

      marshalAndUnmarshal(coordinate)
    }


    it("should marshal and unmarshal Point") {
      val point = Point(Coordinate(0.1, 2.0))

      marshalAndUnmarshal(point)
    }

    it("should marshal and unmarshal MultiPoint") {
      val multiPoint = MultiPoint(Seq(Coordinate(0.1, 2.0), Coordinate(1.1, 2.1)))

      marshalAndUnmarshal(multiPoint)
    }

    it.apply("should marshal and unmarshal LineString") {
      val lineString = LineString(Seq(Coordinate(0.1, 2.0), Coordinate(1.1, 2.1)))

      marshalAndUnmarshal(lineString)
    }

    it("should marshal and unmarshal MultiLineString") {
      val multiLineString = MultiLineString(Seq(
        Seq(Coordinate(0.1, 2.0), Coordinate(1.1, 2.1)),
        Seq(Coordinate(0.1, 2.0), Coordinate(1.1, 2.1))
      ))

      marshalAndUnmarshal(multiLineString)
    }

    it("should marshal and unmarshal Polygon") {
      val polygon = Polygon(Seq(
        Seq(Coordinate(0.1, 2.0), Coordinate(1.1, 2.1)),
        Seq(Coordinate(0.1, 2.0), Coordinate(1.1, 2.1))
      ))

      marshalAndUnmarshal(polygon)
    }

    it("should marshal and unmarshal MultiPolygon") {
      val multiPolygon = MultiPolygon(Seq(
        Seq(
          Seq(Coordinate(0.1, 2.0), Coordinate(1.1, 2.1)),
          Seq(Coordinate(0.1, 2.0), Coordinate(1.1, 2.1))),
        Seq(
          Seq(Coordinate(0.1, 2.0), Coordinate(1.1, 2.1)),
          Seq(Coordinate(0.1, 2.0), Coordinate(1.1, 2.1)))
      ))

      marshalAndUnmarshal(multiPolygon)
    }


    it("should marshal and unmarshal Point collection") {
      val pointCollection = GeometryCollection(Seq(
        Point(Coordinate(0.1, 2.0)),
        Point(Coordinate(0.1, 2.0))
      ))

      marshalAndUnmarshal(pointCollection)
    }

    it("should marshal and unmarshal MultiPoint collection") {
      val multiPointCollection = GeometryCollection(Seq(
        MultiPoint(Seq(Coordinate(0.1, 2.0), Coordinate(1.1, 2.1))),
        MultiPoint(Seq(Coordinate(0.1, 2.0), Coordinate(1.1, 2.1)))
      ))

      marshalAndUnmarshal(multiPointCollection)
    }

    it("should marshal and unmarshal LineString collection") {
      val lineStringCollection = GeometryCollection(Seq(
        LineString(Seq(Coordinate(0.1, 2.0), Coordinate(1.1, 2.1))),
        LineString(Seq(Coordinate(0.1, 2.0), Coordinate(1.1, 2.1)))
      ))

      marshalAndUnmarshal(lineStringCollection)
    }

    it("should marshal and unmarshal MultiLineString collection") {
      val multiLineStringCollection = GeometryCollection(Seq(
        MultiLineString(Seq(
          Seq(Coordinate(0.1, 2.0), Coordinate(1.1, 2.1)),
          Seq(Coordinate(0.1, 2.0), Coordinate(1.1, 2.1))
        )),
        MultiLineString(Seq(
          Seq(Coordinate(0.1, 2.0), Coordinate(1.1, 2.1)),
          Seq(Coordinate(0.1, 2.0), Coordinate(1.1, 2.1))
        ))
      ))

      marshalAndUnmarshal(multiLineStringCollection)
    }

    it("should marshal and unmarshal Polygon collection") {
      val polygonCollection = GeometryCollection(Seq(
        Polygon(Seq(
          Seq(Coordinate(0.1, 2.0), Coordinate(1.1, 2.1)),
          Seq(Coordinate(0.1, 2.0), Coordinate(1.1, 2.1))
        )),
        Polygon(Seq(
          Seq(Coordinate(0.1, 2.0), Coordinate(1.1, 2.1)),
          Seq(Coordinate(0.1, 2.0), Coordinate(1.1, 2.1))
        ))
      ))

      marshalAndUnmarshal(polygonCollection)
    }

    it("should marshal and unmarshal MultiPolygon collection") {
      val mulitPolygonCollection = GeometryCollection(Seq(
        MultiPolygon(Seq(
          Seq(
            Seq(Coordinate(0.1, 2.0), Coordinate(1.1, 2.1)),
            Seq(Coordinate(0.1, 2.0), Coordinate(1.1, 2.1))),
          Seq(
            Seq(Coordinate(0.1, 2.0), Coordinate(1.1, 2.1)),
            Seq(Coordinate(0.1, 2.0), Coordinate(1.1, 2.1)))
        )),
        MultiPolygon(Seq(
          Seq(
            Seq(Coordinate(0.1, 2.0), Coordinate(1.1, 2.1)),
            Seq(Coordinate(0.1, 2.0), Coordinate(1.1, 2.1))),
          Seq(
            Seq(Coordinate(0.1, 2.0), Coordinate(1.1, 2.1)),
            Seq(Coordinate(0.1, 2.0), Coordinate(1.1, 2.1)))
        ))
      ))

      marshalAndUnmarshal(mulitPolygonCollection)
    }

    it("should marshal and unmarshal Feature with Map properties") {
      val multiPolygon = MultiPolygon(Seq(
        Seq(
          Seq(Coordinate(0.1, 2.0), Coordinate(1.1, 2.1)),
          Seq(Coordinate(0.1, 2.0), Coordinate(1.1, 2.1))),
        Seq(
          Seq(Coordinate(0.1, 2.0), Coordinate(1.1, 2.1)),
          Seq(Coordinate(0.1, 2.0), Coordinate(1.1, 2.1)))
      ))

      val map = Map("hi" -> "ho", "off to work" -> "we go")
      val feature = Feature(multiPolygon, map, Some("id"))

      marshalAndUnmarshal(feature)
    }

    it("should marshal and unmarshal Feature with custom properties") {
      val multiPolygon = MultiPolygon(Seq(
        Seq(
          Seq(Coordinate(0.1, 2.0), Coordinate(1.1, 2.1)),
          Seq(Coordinate(0.1, 2.0), Coordinate(1.1, 2.1))),
        Seq(
          Seq(Coordinate(0.1, 2.0), Coordinate(1.1, 2.1)),
          Seq(Coordinate(0.1, 2.0), Coordinate(1.1, 2.1)))
      ))

      val featureWithCustomProperties = Feature(multiPolygon, MyProps("ho"), Some("id"))

      marshalAndUnmarshal(featureWithCustomProperties)
    }

    it("should marshal and unmarshal FeatureCollection with Map properties") {
      val multiPolygon = MultiPolygon(Seq(
        Seq(
          Seq(Coordinate(0.1, 2.0), Coordinate(1.1, 2.1)),
          Seq(Coordinate(0.1, 2.0), Coordinate(1.1, 2.1))),
        Seq(
          Seq(Coordinate(0.1, 2.0), Coordinate(1.1, 2.1)),
          Seq(Coordinate(0.1, 2.0), Coordinate(1.1, 2.1)))
      ))

      val map = Map("hi" -> "ho", "off to work" -> "we go")
      val featureCollection = FeatureCollection(Seq(Feature(multiPolygon, map, Some("id")), Feature(multiPolygon, map, Some("id"))))

      marshalAndUnmarshal(featureCollection)
    }

    it("should marshal and unmarshal FeatureCollection with custom properties") {
      val multiPolygon = MultiPolygon(Seq(
        Seq(
          Seq(Coordinate(0.1, 2.0), Coordinate(1.1, 2.1)),
          Seq(Coordinate(0.1, 2.0), Coordinate(1.1, 2.1))),
        Seq(
          Seq(Coordinate(0.1, 2.0), Coordinate(1.1, 2.1)),
          Seq(Coordinate(0.1, 2.0), Coordinate(1.1, 2.1)))
      ))

      val featureCollectionWithCustomProperties =
        FeatureCollection(Seq(Feature(multiPolygon, MyProps("ho"), Some("id")), Feature(multiPolygon, MyProps("ho"), Some("id"))))

      marshalAndUnmarshal(featureCollectionWithCustomProperties)
    }

    it("should marshal and unmarshal features without IDs"){
      val featureWithoutId = Feature(GeoJson.Point(Coordinate(1, 1)), MyProps("hi de ho"))

      marshalAndUnmarshal(featureWithoutId)
    }

  }
}
