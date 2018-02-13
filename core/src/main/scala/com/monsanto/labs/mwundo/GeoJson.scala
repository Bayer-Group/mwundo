package com.monsanto.labs.mwundo

/**
 * Types for GeoJSON spec: http://geojson.org/geojson-spec.html
 */

object GeoJson {

  sealed trait Typed {val `type`: String}

  sealed trait Geometry extends Typed

  sealed trait Coords[+A] {val coordinates: A}

  case class Coordinate(x: BigDecimal, y: BigDecimal)

  case class Point(coordinates: Coordinate) extends Geometry with Coords[Coordinate] {
    override val `type`: String = "Point"
  }

  case class MultiPoint(coordinates: Seq[Coordinate]) extends Geometry with Coords[Seq[Coordinate]] {
    require(coordinates.nonEmpty, "coordinates vector can not be empty")
    override val `type`: String = "MultiPoint"
  }

  case class LineString(coordinates: Seq[Coordinate]) extends Geometry with Coords[Seq[Coordinate]] {
    require(coordinates.nonEmpty, "coordinates vector can not be empty")
    override val `type`: String = "LineString"
  }

  case class MultiLineString(coordinates: Seq[Seq[Coordinate]]) extends Geometry with Coords[Seq[Seq[Coordinate]]] {
    require(coordinates.nonEmpty, "coordinates vector can not be empty")
    require(coordinates.forall(_.nonEmpty), "coordinates sub vector can not be empty")
    override val `type`: String = "MultiLineString"
  }

  case class Polygon(coordinates: Seq[Seq[Coordinate]]) extends Geometry with Coords[Seq[Seq[Coordinate]]] {
    require(coordinates.nonEmpty, "coordinates vector can not be empty")
    require(coordinates.forall(_.nonEmpty), "coordinates sub vector can not be empty")
    override val `type`: String = "Polygon"
  }

  case class MultiPolygon(coordinates: Seq[Seq[Seq[Coordinate]]]) extends Geometry with Coords[Seq[Seq[Seq[Coordinate]]]] {
    require(coordinates.nonEmpty, "coordinates vector can not be empty")
    require(coordinates.forall(_.nonEmpty), "coordinates sub vector can not be empty")
    require(coordinates.forall(x => x.forall(_.nonEmpty)), "coordinates sub sub vector can not be empty")
    override val `type`: String = "MultiPolygon"
  }

  case class GeometryCollection[+G <: Geometry](geometries: Seq[G]) extends Typed {
    override val `type`: String = "GeometryCollection"
  }

  case class Feature[+G <: Geometry, P](geometry: G, properties: P, id: Option[String] = None) extends Typed {
    override val `type`: String = "Feature"
  }

  case class FeatureCollection[+G <: Geometry, P](features: Seq[Feature[G, P]]) extends Typed {
    override val `type`: String = "FeatureCollection"
  }
}
