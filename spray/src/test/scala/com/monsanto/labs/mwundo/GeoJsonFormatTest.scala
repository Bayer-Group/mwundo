package com.monsanto.labs.mwundo

import com.monsanto.labs.mwundo.GeoJson._
import com.monsanto.labs.mwundo.GeoJsonFormats._
import org.scalatest.{FunSpec, Matchers, ParallelTestExecution}
import spray.json._

class GeoJsonFormatTest extends FunSpec with Matchers with ParallelTestExecution {

  private def marshalAndUnmarshal[T](t: T)(implicit jsonFormat: JsonFormat[T]) = {
    val json = t.toJson
    val result = json.convertTo[T]

    result should be (t)
    result.toJson.toString() should be (json.toString())
  }

  case class MyProps(hi: String)
  object MyProps extends DefaultJsonProtocol {
    implicit val pFmt: RootJsonFormat[MyProps] = jsonFormat1(MyProps.apply)
  }

  describe("GeoJson support") {
    it("should marshal and unmarshal Coordinate") {
      val x = Coordinate(0.1, 2.0)

      marshalAndUnmarshal(x)
    }

    it("should marshal and unmarshal Point") {
      val x = Point(Coordinate(0.1, 2.0))

      marshalAndUnmarshal(x)
    }

    it("should marshal and unmarshal MultiPoint") {
      val x = MultiPoint(Seq(Coordinate(0.1, 2.0), Coordinate(1.1, 2.1)))

      marshalAndUnmarshal(x)
    }

    it("should marshal and unmarshal LineString") {
      val x = LineString(Seq(Coordinate(0.1, 2.0), Coordinate(1.1, 2.1)))

      marshalAndUnmarshal(x)
    }

    it("should marshal and unmarshal MultiLineString") {
      val x = MultiLineString(Seq(
        Seq(Coordinate(0.1, 2.0), Coordinate(1.1, 2.1)),
        Seq(Coordinate(0.1, 2.0), Coordinate(1.1, 2.1))
      ))

      marshalAndUnmarshal(x)
    }

    it("should marshal and unmarshal Polygon") {
      val x = Polygon(Seq(
        Seq(Coordinate(0.1, 2.0), Coordinate(1.1, 2.1)),
        Seq(Coordinate(0.1, 2.0), Coordinate(1.1, 2.1))
      ))

      marshalAndUnmarshal(x)
    }

    it("should marshal and unmarshal MultiPolygon") {
      val x = MultiPolygon(Seq(
        Seq(
          Seq(Coordinate(0.1, 2.0), Coordinate(1.1, 2.1)),
          Seq(Coordinate(0.1, 2.0), Coordinate(1.1, 2.1))),
        Seq(
          Seq(Coordinate(0.1, 2.0), Coordinate(1.1, 2.1)),
          Seq(Coordinate(0.1, 2.0), Coordinate(1.1, 2.1)))
      ))

      marshalAndUnmarshal(x)
    }

    it("should marshal and unmarshal Point collection") {
      val x = GeometryCollection(Seq(
        Point(Coordinate(0.1, 2.0)),
        Point(Coordinate(0.1, 2.0))
      ))

      marshalAndUnmarshal(x)
    }

    it("should marshal and unmarshal MultiPoint collection") {
      val x = GeometryCollection(Seq(
        MultiPoint(Seq(Coordinate(0.1, 2.0), Coordinate(1.1, 2.1))),
        MultiPoint(Seq(Coordinate(0.1, 2.0), Coordinate(1.1, 2.1)))
      ))

      marshalAndUnmarshal(x)
    }

    it("should marshal and unmarshal LineString collection") {
      val x = GeometryCollection(Seq(
        LineString(Seq(Coordinate(0.1, 2.0), Coordinate(1.1, 2.1))),
        LineString(Seq(Coordinate(0.1, 2.0), Coordinate(1.1, 2.1)))
      ))

      marshalAndUnmarshal(x)
    }

    it("should marshal and unmarshal MultiLineString collection") {
      val x = GeometryCollection(Seq(
        MultiLineString(Seq(
          Seq(Coordinate(0.1, 2.0), Coordinate(1.1, 2.1)),
          Seq(Coordinate(0.1, 2.0), Coordinate(1.1, 2.1))
        )),
        MultiLineString(Seq(
          Seq(Coordinate(0.1, 2.0), Coordinate(1.1, 2.1)),
          Seq(Coordinate(0.1, 2.0), Coordinate(1.1, 2.1))
        ))
      ))

      marshalAndUnmarshal(x)
    }

    it("should marshal and unmarshal Polygon collection") {
      val x = GeometryCollection(Seq(
        Polygon(Seq(
          Seq(Coordinate(0.1, 2.0), Coordinate(1.1, 2.1)),
          Seq(Coordinate(0.1, 2.0), Coordinate(1.1, 2.1))
        )),
        Polygon(Seq(
          Seq(Coordinate(0.1, 2.0), Coordinate(1.1, 2.1)),
          Seq(Coordinate(0.1, 2.0), Coordinate(1.1, 2.1))
        ))
      ))

      marshalAndUnmarshal(x)
    }

    it("should marshal and unmarshal MultiPolygon collection") {
      val x = GeometryCollection(Seq(
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

      marshalAndUnmarshal(x)
    }

    it("should marshal and unmarshal Feature with Map properties") {
      val g = MultiPolygon(Seq(
        Seq(
          Seq(Coordinate(0.1, 2.0), Coordinate(1.1, 2.1)),
          Seq(Coordinate(0.1, 2.0), Coordinate(1.1, 2.1))),
        Seq(
          Seq(Coordinate(0.1, 2.0), Coordinate(1.1, 2.1)),
          Seq(Coordinate(0.1, 2.0), Coordinate(1.1, 2.1)))
      ))

      val p = Map("hi" -> "ho", "off to work" -> "we go")

      val x = Feature(g, p, Some("id"))

      marshalAndUnmarshal(x)
    }

    it("should marshal and unmarshal Feature with custom properties") {
      val g = MultiPolygon(Seq(
        Seq(
          Seq(Coordinate(0.1, 2.0), Coordinate(1.1, 2.1)),
          Seq(Coordinate(0.1, 2.0), Coordinate(1.1, 2.1))),
        Seq(
          Seq(Coordinate(0.1, 2.0), Coordinate(1.1, 2.1)),
          Seq(Coordinate(0.1, 2.0), Coordinate(1.1, 2.1)))
      ))

      val x = Feature(g, MyProps("ho"), Some("id"))

      marshalAndUnmarshal(x)
    }

    it("should marshal and unmarshal FeatureCollection with Map properties") {
      val g = MultiPolygon(Seq(
        Seq(
          Seq(Coordinate(0.1, 2.0), Coordinate(1.1, 2.1)),
          Seq(Coordinate(0.1, 2.0), Coordinate(1.1, 2.1))),
        Seq(
          Seq(Coordinate(0.1, 2.0), Coordinate(1.1, 2.1)),
          Seq(Coordinate(0.1, 2.0), Coordinate(1.1, 2.1)))
      ))

      val p = Map("hi" -> "ho", "off to work" -> "we go")

      val x = FeatureCollection(Seq(Feature(g, p, Some("id")), Feature(g, p, Some("id"))))

      marshalAndUnmarshal(x)
    }

    it("should marshal and unmarshal FeatureCollection with custom properties") {
      val g = MultiPolygon(Seq(
        Seq(
          Seq(Coordinate(0.1, 2.0), Coordinate(1.1, 2.1)),
          Seq(Coordinate(0.1, 2.0), Coordinate(1.1, 2.1))),
        Seq(
          Seq(Coordinate(0.1, 2.0), Coordinate(1.1, 2.1)),
          Seq(Coordinate(0.1, 2.0), Coordinate(1.1, 2.1)))
      ))

      val x = FeatureCollection(Seq(Feature(g, MyProps("ho"), Some("id")), Feature(g, MyProps("ho"), Some("id"))))

      marshalAndUnmarshal(x)
    }

    it("should marshal and unmarshal features without IDs"){
      val f = Feature(GeoJson.Point(Coordinate(1, 1)), MyProps("hi de ho"))

      marshalAndUnmarshal(f)
    }

    it("should unmarshal coordinates with Z"){

      "[1.0, 2.0, 0.0]".parseJson.convertTo[Coordinate] shouldEqual Coordinate(1.0, 2.0)
    }


    it("should not unmarshal coordinates with length > 3"){

      assertThrows[DeserializationException]("[1.0, 2.0, 0.0, 0.0]".parseJson.convertTo[Coordinate])
    }
  }
}
