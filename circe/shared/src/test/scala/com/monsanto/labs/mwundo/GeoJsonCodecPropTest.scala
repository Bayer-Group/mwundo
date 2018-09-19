package com.monsanto.labs.mwundo

import com.monsanto.labs.mwundo.GeoJson._
import io.circe.{Decoder, Encoder}
import io.circe.syntax._
import org.scalacheck.Arbitrary.arbitrary
import org.scalacheck.Prop._
import org.scalacheck.{Arbitrary, Gen}
import org.scalatest.prop.Checkers
import org.scalatest.{FunSpec, ParallelTestExecution}

class GeoJsonFormatPropTest extends FunSpec with Checkers with ParallelTestExecution {

  implicit override val generatorDrivenConfig: PropertyCheckConfiguration = PropertyCheckConfiguration(workers = 4)

  import GeoJsonCodec._
  import GeoJsonGenerators._
  import cats.syntax.either._

  private def marshalAndUnmarshal[T](t: T)(implicit encoder: Encoder[T], decoder: Decoder[T]) = {
    val json = t.asJson
    val result = json.as[T]

    result.toTry.get == t && result.toTry.get.asJson.toString() == json.toString()
  }

  describe("GeoJSON Circe Formats") {
    it("Coordinate")(check((gj: Coordinate) => marshalAndUnmarshal(gj)))
    it("Point")(check((gj: Point) => marshalAndUnmarshal(gj)))
    it("MultiPoint")(check((gj: MultiPoint) => marshalAndUnmarshal(gj)))
    it("LineString")(check((gj: LineString) => marshalAndUnmarshal(gj)))
    it("MultiLineString")(check((gj: MultiLineString) => marshalAndUnmarshal(gj)))
    it("Polygon")(check((gj: Polygon) => marshalAndUnmarshal(gj)))
    it("MultiPolygon")(check((gj: MultiPolygon) => marshalAndUnmarshal(gj)))
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

  def genCoordinate: Gen[Coordinate] = for {
    x <- arbitrary[Double]
    y <- arbitrary[Double]
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

}
