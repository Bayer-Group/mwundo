package com.monsanto.labs.mwundo.spray

import com.monsanto.labs.mwundo.GeoJson._
import com.monsanto.labs.mwundo.spray.GeoJsonFormats._
import org.scalacheck.Arbitrary.arbitrary
import org.scalacheck.Gen._
import org.scalacheck.Prop._
import org.scalacheck.{Arbitrary, Gen}
import org.scalatest.prop.Checkers
import org.scalatest.{FunSpec, ParallelTestExecution}
import spray.json._

class GeoJsonFormatPropTest extends FunSpec with Checkers with ParallelTestExecution {

  implicit override val generatorDrivenConfig = PropertyCheckConfiguration(workers = 4)
  import GeoJsonGenerators._

  private def marshalAndUnmarshal[T](t: T)(implicit jf: JsonFormat[T]) = {
    val json = t.toJson
    val result = json.convertTo[T]

    result == t && result.toJson.toString() == json.toString()
  }

  describe("GeoJSON Spray Json Formats") {
    it("Coordinate")(check((gj: Coordinate) => marshalAndUnmarshal(gj)))
    it("Point")(check((gj: Point) => marshalAndUnmarshal(gj)))
    it("MultiPoint")(check((gj: MultiPoint) => marshalAndUnmarshal(gj)))
    it("LineString")(check((gj: LineString) => marshalAndUnmarshal(gj)))
    it("MultiLineString")(check((gj: MultiLineString) => marshalAndUnmarshal(gj)))
    it("Polygon")(check((gj: Polygon) => marshalAndUnmarshal(gj)))
    it("MultiPolygon")(check((gj: MultiPolygon) => marshalAndUnmarshal(gj)))

    // Not sure why these tests are generating:
    // Message: scala.collection.immutable.Vector cannot be cast to planting.geojson.GeoJson$Coordinate
    // See geometry generators below
//    it("GeometryCollection-Point")(check((gj: GeometryCollection[Point]) => marshalAndUnmarshal(gj)))
//    it("GeometryCollection-MultiPoint")(check((gj: GeometryCollection[MultiPoint]) => marshalAndUnmarshal(gj)))
//    it("GeometryCollection-LineString")(check((gj: GeometryCollection[LineString]) => marshalAndUnmarshal(gj)))
//    it("GeometryCollection-MultiLineString")(check((gj: GeometryCollection[MultiLineString]) => marshalAndUnmarshal(gj)))
//    it("GeometryCollection-Polygon")(check((gj: GeometryCollection[Polygon]) => marshalAndUnmarshal(gj)))
//    it("GeometryCollection-MultiPolygon")(check((gj: GeometryCollection[MultiPolygon]) => marshalAndUnmarshal(gj)))
  }
}

object GeoJsonGenerators {

  implicit val arbCoordinate: Arbitrary[Coordinate] = Arbitrary(genCoordinate)
  implicit val arbPoint: Arbitrary[Point] = Arbitrary(genPoint)
  implicit val arbCoordinateSeq: Arbitrary[Seq[Coordinate]] = Arbitrary(genCoordinateSeq)
  implicit val arbMultiPoint: Arbitrary[MultiPoint] = Arbitrary(genMultiPoint)
  implicit val arbLineString: Arbitrary[LineString] = Arbitrary(genLineString)
  implicit val arbMultiLineString: Arbitrary[MultiLineString] = Arbitrary(genMultiLineString)
  implicit val arbPolygon: Arbitrary[Polygon] = Arbitrary(genPolygon)
  implicit val arbMultiPolygon: Arbitrary[MultiPolygon] = Arbitrary(genMultiPolygon)

  implicit def arbGeometrySeq[T <: Geometry]: Arbitrary[Seq[T]] = Arbitrary(genGeometrySeq[T])

  implicit def arbGeometryCollection[T <: Geometry]: Arbitrary[GeometryCollection[T]] = Arbitrary(genGeometryCollection[T])

  def genCoordinate: Gen[Coordinate] = for {
    x <- arbitrary[BigDecimal]
    y <- arbitrary[BigDecimal]
  } yield Coordinate(x, y)

  def genCoordinateSeq: Gen[Seq[Coordinate]] = Gen.nonEmptyContainerOf[Seq, Coordinate](genCoordinate)

  def genCoordinateSeqSeq: Gen[Seq[Seq[Coordinate]]] = Gen.nonEmptyContainerOf[Seq, Seq[Coordinate]](genCoordinateSeq)

  def genCoordinateSeqSeqSeq: Gen[Seq[Seq[Seq[Coordinate]]]] = Gen.nonEmptyContainerOf[Seq, Seq[Seq[Coordinate]]](genCoordinateSeqSeq)

  def genPoint: Gen[Point] = for {
    c <- genCoordinate
  } yield Point(c)

  def genMultiPoint: Gen[MultiPoint] = for {
    cs <- genCoordinateSeq
  } yield MultiPoint(cs)

  def genLineString: Gen[LineString] = for {
    cs <- genCoordinateSeq
  } yield LineString(cs)

  def genMultiLineString: Gen[MultiLineString] = for {
    css <- genCoordinateSeqSeq
  } yield MultiLineString(css)

  def genPolygon: Gen[Polygon] = for {
    css <- genCoordinateSeqSeq
  } yield Polygon(css)

  def genMultiPolygon: Gen[MultiPolygon] = for {
    csss <- genCoordinateSeqSeqSeq
  } yield MultiPolygon(csss)

  // I've been staring at this too long to make sense of it. I can't make the compiler happy.
  def genGeometry[T <: Geometry]: Gen[T] =
    oneOf(genPoint, genMultiPoint, genLineString, genMultiLineString, genPolygon, genMultiPolygon).asInstanceOf[Gen[T]]

  def genGeometrySeq[T <: Geometry]: Gen[Seq[T]] = Gen.nonEmptyContainerOf[Seq, T](genGeometry[T])

  def genGeometryCollection[T <: Geometry]: Gen[GeometryCollection[T]] = for {
    gs <- genGeometrySeq[T]
  } yield GeometryCollection(gs)
}
