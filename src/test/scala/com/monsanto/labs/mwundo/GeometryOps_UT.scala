package com.monsanto.labs.mwundo

import org.scalatest.{Matchers, FunSpec}
import spray.json._
import com.monsanto.labs.mwundo.GeoJsonImplicits._
import com.monsanto.labs.mwundo.GeoJsonFormats._

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
      val areaInAcres = 344958
      val areaInKmSq = 1396
      val county = io.
        Source.
        fromInputStream(getClass.getClassLoader.getResourceAsStream("Lee.geo.json")).
        getLines().
        mkString("").
        parseJson.
        convertTo[GeoJson.FeatureCollection[GeoJson.MultiPolygon, County]].features.head

      county.areaInAcres / areaInAcres shouldBe 1.0 +- 1e-2

      county.areaInKmSq / areaInKmSq shouldBe 1.0 +- 1e-2
    }
  }

}
