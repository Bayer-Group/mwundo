package com.monsanto.labs.mwundo

import com.monsanto.labs.mwundo.GeoJson.{Coordinate, Polygon}
import org.scalatest.{FunSpec, Matchers}

/**
 * Created by Ryan Richt on 10/28/15
 */
class GeoJsonViewer_UT extends FunSpec with Matchers {
  describe("GeoJsonViewer"){
    it("should properly transform GeoJson to the origin in window-filling, flipped Y coordinates"){

      val screenSize = 100

      val testPolygon = Polygon(Seq(Seq(
        Coordinate(1000, 500),
        Coordinate(1500, 500),
        Coordinate(1500, 600),
        Coordinate(1000, 600),
        Coordinate(1000, 500)
      )))

      val translated = GeoJsonViewer.transformToJava2DLocalCoordinates(screenSize, screenSize, Seq(testPolygon))

      val expected = Polygon(Seq(Seq(
        Coordinate(0, 100),
        Coordinate(100, 100),
        Coordinate(100, 80),
        Coordinate(0, 80),
        Coordinate(0, 100)
      )))

      translated shouldBe Seq(expected)
    }

    it("and do it right-ways up"){

      val screenSize = 100

      val testPolygon = Polygon(Seq(Seq(
        Coordinate(1000, 500),
        Coordinate(1500, 500),
        Coordinate(1000, 600),
        Coordinate(1000, 500)
      )))

      val translated = GeoJsonViewer.transformToJava2DLocalCoordinates(screenSize, screenSize, Seq(testPolygon))

      val expected = Polygon(Seq(Seq(
        Coordinate(0, 100),
        Coordinate(100, 100),
        Coordinate(0, 80),
        Coordinate(0, 100)
      )))

      translated shouldBe Seq(expected)
    }
  }
}
