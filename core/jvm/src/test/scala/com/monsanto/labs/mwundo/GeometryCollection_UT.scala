package com.monsanto.labs.mwundo

import org.locationtech.jts.geom.impl.CoordinateArraySequence
import org.scalatest.{FunSpec, Matchers}
import org.locationtech.jts.geom._
import collection.JavaConverters._
//import JTSGeometryImplicits._

/**
  * Created by dgdale on 10/30/15.
  */
class GeometryCollection_UT extends FunSpec with Matchers {

  describe("GeometryCollecton") {
    it("should do some things with geometry collections") {
      val gf = new GeometryFactory()
      def createCoordinate = new Coordinate(Math.random(), Math.random())
      def createCoordinateSeq(length: Int = 1) = new CoordinateArraySequence(Array.fill(length)(createCoordinate))
      def createLinearRing = {
        val pts = Array.fill(3)(createCoordinate)
        new LinearRing(new CoordinateArraySequence(pts :+ pts.head), gf)
      }
      val geos = Seq(
        new Point(createCoordinateSeq(), gf),
        new Point(createCoordinateSeq(), gf),
        new Point(createCoordinateSeq(), gf),
        new Point(createCoordinateSeq(), gf),
        new Point(createCoordinateSeq(), gf),
        new Polygon(createLinearRing, Array.empty[LinearRing], gf),
        new Polygon(createLinearRing, Array.empty[LinearRing], gf),
        new Polygon(createLinearRing, Array.empty[LinearRing], gf),
        new Polygon(createLinearRing, Array.empty[LinearRing], gf),
        new Polygon(createLinearRing, Array.empty[LinearRing], gf)
      ).asJavaCollection

      val geosArray = GeometryFactory.toGeometryArray(geos)

      val onlyPolys = geosArray.collect {
        case g: Polygon => g
      }

      onlyPolys.length shouldBe 5
    }

  }

}
