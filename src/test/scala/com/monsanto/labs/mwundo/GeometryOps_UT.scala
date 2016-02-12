package com.monsanto.labs.mwundo

import com.monsanto.labs.mwundo.JTSGeoFormat.MultiPolygonConverter
import org.scalatest.{Matchers, FunSpec}
import spray.json._
import com.monsanto.labs.mwundo.GeoJsonImplicits._
import com.monsanto.labs.mwundo.GeoJsonFormats._
import com.monsanto.labs.mwundo.GeoJson._

/**
 * Created by dgdale on 10/29/15.
 */
class GeometryOps_UT extends FunSpec with Matchers {

  case class County(kind: String, name: String, state: String)
  object County extends DefaultJsonProtocol {
    implicit val format = jsonFormat3(County.apply)
  }

  describe("Geometry Ops") {
    it("should calculate the area of Lee County, IA") {
      val areaInAcres = 344958D
      val areaInKmSq = 1396D
      val county = io.
        Source.
        fromInputStream(getClass.getClassLoader.getResourceAsStream("Lee.geo.json")).
        getLines().
        mkString("").
        parseJson.
        convertTo[GeoJson.FeatureCollection[GeoJson.MultiPolygon, County]].features.head

      county.areaInAcres() / areaInAcres shouldBe 1.0 +- 1e-2

      county.areaInKmSq() / areaInKmSq shouldBe 1.0 +- 1e-2
    }

    it("should find the centroid of a multipolygon") {
      val coordinates =
        Seq(
          Seq(
            Seq(
              Coordinate(0,0),
              Coordinate(0,1),
              Coordinate(1,1),
              Coordinate(1,0)
            )
          )
      )
      val centroid = MultiPolygon(coordinates).centroid
      val (x, y) = (centroid.x.toDouble, centroid.y.toDouble)

      x shouldBe 0.5
      y shouldBe 0.5

    }
  }

}
