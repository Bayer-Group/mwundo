package com.monsanto.labs.geometry

import com.monsanto.labs.sim.agriculture.distributions.BetaDistribution2D
import com.vividsolutions.jts.geom.impl.CoordinateArraySequence
import com.vividsolutions.jts.geom.{GeometryCollection, Envelope, Coordinate, GeometryFactory}
import com.vividsolutions.jts.triangulate.VoronoiDiagramBuilder
import org.scalatest.{Matchers, FunSpec}

import collection.JavaConverters._

/**
 * Created by Ryan Richt on 10/22/15
 */

//class GeometryClipper_UT extends FunSpec with Matchers {
//
//  describe("GeometryClipper"){
//    it("should clip better than JTS does"){
//
//      val beta2d = BetaDistribution2D(100, 2, 2, 100, 2, 2)
//
//      val points = beta2d.draw(3)
//
//      all (points.map(_._1)) should be <= 100.0
//      all (points.map(_._1)) should be >= 0.0
//      all (points.map(_._2)) should be <= 100.0
//      all (points.map(_._2)) should be >= 0.0
//
//      val geoFac = new GeometryFactory()
//
//      val v = new VoronoiDiagramBuilder()
//      //  v.setSites( points.map{ case (x, y) => geoFac.createPoint(new Coordinate(x, y)) }.asJavaCollection  )
//      v.setSites( points.map{ case (x, y) => new Coordinate(x, y) }.asJavaCollection  )
//      val clipEnv = new Envelope(0, beta2d.xMax, 0, beta2d.yMax)
//      v.setClipEnvelope(clipEnv)
//
//      val cells = GeometryClipper.clip(v.getDiagram(geoFac), clipEnv).asInstanceOf[GeometryCollection]
//      val cellGeos = Seq.tabulate(cells.getNumGeometries)(i => cells.getGeometryN(i))
//
//      val allCoords = cellGeos.flatMap(_.getCoordinates)
//      val largestX = allCoords.map(_.x).max
//      val largestY = allCoords.map(_.y).max
//      val smallestX = allCoords.map(_.x).min
//      val smallestY = allCoords.map(_.y).min
//
//      largestX should be <= 100.0
//      largestY should be <= 100.0
//      smallestX should be >= 0.0
//      smallestY should be >= 0.0
//    }
//
//    it("should properly clip intersecting squares"){
//
//      val geoFac = new GeometryFactory()
//
//      val square1 = geoFac.createPolygon(
//        geoFac.createLinearRing(
//          new CoordinateArraySequence(
//            Array(
//              new Coordinate(0, 0),
//              new Coordinate(0, 10),
//              new Coordinate(10, 10),
//              new Coordinate(10, 0),
//              new Coordinate(0, 0)
//            )
//          )
//        ),
//        null
//      )
//
//      val square2 = geoFac.createPolygon(
//        geoFac.createLinearRing(
//          new CoordinateArraySequence(
//            Array(
//              new Coordinate(5, 5),
//              new Coordinate(5, 15),
//              new Coordinate(15, 15),
//              new Coordinate(15, 5),
//              new Coordinate(5, 5)
//            )
//          )
//        ),
//        null
//      )
//
//      val clipped = GeometryClipper.clip(square1, square2)
//
//      val expected = Array(
//        new Coordinate(5, 5),
//        new Coordinate(5, 10),
//        new Coordinate(10, 10),
//        new Coordinate(10, 5),
//        new Coordinate(5, 5)
//      )
//
//      clipped.getCoordinates should be (expected)
//    }
//
//    it("should properly clip non-rectangular polygons"){
//
//      val geoFac = new GeometryFactory()
//
//      val triangle1 = geoFac.createPolygon(
//        geoFac.createLinearRing(
//          new CoordinateArraySequence(
//            Array(
//              new Coordinate( 0,  0),
//              new Coordinate( 0, 10),
//              new Coordinate(10,  5),
//              new Coordinate( 0,  0)
//            )
//          )
//        ),
//        null
//      )
//
//      val triangle2 = geoFac.createPolygon(
//        geoFac.createLinearRing(
//          new CoordinateArraySequence(
//            Array(
//              new Coordinate(10,  0),
//              new Coordinate(10, 10),
//              new Coordinate( 2,  5),
//              new Coordinate(10,  0)
//            )
//          )
//        ),
//        null
//      )
//
//      val clipped = GeometryClipper.clip(triangle1, triangle2)
//
//      val expected = Array(
//        new Coordinate(5 + 5/9.0, 2 + 7/9.0),
//        new Coordinate(2, 5),
//        new Coordinate(5 + 5/9.0, 7 + 2/9.0),
//        new Coordinate(10, 5),
//        new Coordinate(5 + 5/9.0, 2 + 7/9.0)
//      )
//
//      clipped.getCoordinates should be (expected)
//    }
//  }
//}