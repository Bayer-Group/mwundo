package geojson

import org.scalatest.{ParallelTestExecution, FunSpec, Matchers}

class GeoJsonFormatTest extends FunSpec with Matchers with ParallelTestExecution {

  import geojson.GeoJson._
  import geojson.GeoJsonFormats._
  import spray.json._

  def marshalAndUnmarshal[T](t: T)(implicit jsonFormat: JsonFormat[T]) = {
    val json = t.toJson
    val result = json.convertTo[T]

    result should be (t)
    result.toJson.toString() should be (json.toString())
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

      val x = Feature(g, p, "id")

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

      case class MyProps(hi: String)
      implicit val pFmt = jsonFormat1(MyProps)

      val x = Feature(g, MyProps("ho"), "id")

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

      val x = FeatureCollection(Seq(Feature(g, p, "id"), Feature(g, p, "id")))

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

      case class MyProps(hi: String)
      implicit val pFmt = jsonFormat1(MyProps)

      val x = FeatureCollection(Seq(Feature(g, MyProps("ho"), "id"), Feature(g, MyProps("ho"), "id")))

      marshalAndUnmarshal(x)
    }
  }
}
