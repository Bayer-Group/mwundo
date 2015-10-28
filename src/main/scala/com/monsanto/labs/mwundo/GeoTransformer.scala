package com.monsanto.labs.mwundo

import breeze.linalg.{DenseVector, DenseMatrix}
import com.monsanto.labs.mwundo.GeoJson.{Coordinate, MultiPolygon}
import com.monsanto.labs.mwundo._


/**
 * Created by Ryan Richt on 10/26/15
 */
trait GeoTransformer[G <: GeoJson.Geometry] {
  def translate(x: Double, y: Double)(geo: G): G
  def scale(x: Double, y: Double)(geo: G): G

  def transform(matrix: DenseMatrix[Double])(geo: G): G

  def maxX(geo: G): Double
  def maxY(geo: G): Double
  def minX(geo: G): Double
  def minY(geo: G): Double
}
object GeoTransformer {
  implicit object MultiPolygonGeoTransformer extends GeoTransformer[GeoJson.MultiPolygon] {
    def translate(x: Double, y: Double)(geo: GeoJson.MultiPolygon) = {
      GeoJson.MultiPolygon(
        geo.coordinates.map{ polys =>
          polys.map{ rings =>
            rings.map{ coords =>
              coords.copy( coords.x + x, coords.y + y )
            }
          }
        }
      )
    }

    def scale(x: Double, y: Double)(geo: GeoJson.MultiPolygon) = {
      GeoJson.MultiPolygon(
        geo.coordinates.map{ polys =>
          polys.map{ rings =>
            rings.map{ coords =>
              coords.copy( coords.x * x, coords.y * y )
            }
          }
        }
      )
    }

    def transform(matrix: DenseMatrix[Double])(geo: MultiPolygon) = GeoJson.MultiPolygon(
      geo.coordinates.map { polys =>
        polys.map { rings =>
          rings.map { coord =>
            val pointAsVec = new DenseVector(Array(coord.x.toDouble, coord.y.toDouble, 1.0))
            val transformedVec = matrix * pointAsVec
            Coordinate(transformedVec(0), transformedVec(1))
          }
        }
      }
    )


    def maxY(geo: GeoJson.MultiPolygon) = geo.coordinates.flatten.flatten.map(_.y.toDouble).max
    def minX(geo: GeoJson.MultiPolygon) = geo.coordinates.flatten.flatten.map(_.x.toDouble).min
    def maxX(geo: GeoJson.MultiPolygon) = geo.coordinates.flatten.flatten.map(_.x.toDouble).max
    def minY(geo: GeoJson.MultiPolygon) = geo.coordinates.flatten.flatten.map(_.y.toDouble).min
  }

  implicit object PolygonGeoTransformer extends GeoTransformer[GeoJson.Polygon] {
    def translate(x: Double, y: Double)(geo: GeoJson.Polygon) = {
      GeoJson.Polygon(
        geo.coordinates.map{ rings =>
          rings.map{ coords =>
            coords.copy( coords.x + x, coords.y + y )
          }
        }
      )
    }

    def scale(x: Double, y: Double)(geo: GeoJson.Polygon) = {
      GeoJson.Polygon(
        geo.coordinates.map{ rings =>
          rings.map{ coords =>
            coords.copy( coords.x * x, coords.y * y )
          }
        }
      )
    }

    def transform(matrix: DenseMatrix[Double])(geo: GeoJson.Polygon) = GeoJson.Polygon(
      geo.coordinates.map { rings =>
        rings.map { coord =>
          val pointAsVec = new DenseVector(Array(coord.x.toDouble, coord.y.toDouble, 1.0))
          val transformedVec = matrix * pointAsVec
          Coordinate(transformedVec(0), transformedVec(1))
        }
      }
    )

    def maxY(geo: GeoJson.Polygon) = geo.coordinates.flatten.map(_.y.toDouble).max
    def minX(geo: GeoJson.Polygon) = geo.coordinates.flatten.map(_.x.toDouble).min
    def maxX(geo: GeoJson.Polygon) = geo.coordinates.flatten.map(_.x.toDouble).max
    def minY(geo: GeoJson.Polygon) = geo.coordinates.flatten.map(_.y.toDouble).min
  }
}
