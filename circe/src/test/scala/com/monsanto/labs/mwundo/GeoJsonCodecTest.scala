package com.monsanto.labs.mwundo

import com.monsanto.labs.mwundo.GeoJson._
import io.circe.generic.semiauto.{deriveDecoder, deriveEncoder}
import io.circe.{Decoder, Encoder}
import io.circe.syntax._
import org.scalatest.{FunSpec, Matchers, ParallelTestExecution}

class GeoJsonCodecTest extends FunSpec with Matchers with ParallelTestExecution {

  import GeoJsonCodec._

  private def marshalAndUnmarshal[T](t: T)(implicit encoder: Encoder[T], decoder: Decoder[T]) = {
    val json = t.asJson
    val result = json.as[T]

    result.toTry.get should be (t)
    result.toTry.get.asJson.toString() should be (json.toString())
  }

  case class MyProps(hi: String)
  object MyProps {
    implicit val encoder: Encoder[MyProps] = deriveEncoder[MyProps]
    implicit val decoder: Decoder[MyProps] = deriveDecoder[MyProps]
  }

  describe("GeoJson support"){

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
