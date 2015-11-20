package com.monsanto.labs.mwundo

import com.monsanto.labs.mwundo.GeoJson.{FeatureCollection, Coordinate, Feature}
import com.vividsolutions.jts.geom.{Geometry, GeometryFactory}

/**
 * Bounding box used to wrap GeoJson shapes so various area calculations can be performed
 * @param minX
 * @param minY
 * @param width
 * @param height
 */
case class BoundingBox(minX: Double, minY: Double, width: Double, height: Double)

/**
 * implicit functions for GeoJson objects
 */

object GeoJsonImplicits {

  /**
   * implicit class for GeoJson 'FeatureCollection's so various geometry ops can be performed on GeoJson objects
   * @param feature
   * @tparam G
   * @tparam P
   */

  implicit class RichGeoJsonFeatureCollection[G <: GeoJson.Geometry : JTSGeoFormat : GeoTransformer, P](feature: FeatureCollection[G, P])
    extends GeometryOps[Seq[G]](feature.features.map(_.geometry))

  /**
   * implicit class for GeoJson 'Feature's so various geometry ops can be performed on GeoJson objects
   * @param feature
   * @tparam G
   * @tparam P
   */

  implicit class RichGeoJsonFeature[G <: GeoJson.Geometry : JTSGeoFormat : GeoTransformer, P](feature: Feature[G, P])
    extends GeometryOps[G](feature.geometry) {

    def translatedFeature(x: Double, y: Double) = feature.copy( geometry = feature.geometry.translated(x, y) )
    def scaledFeature(    x: Double, y: Double) = feature.copy( geometry = feature.geometry.scaled(x, y)     )
  }
  object RichGeoJsonFeature {
    val geoFac = new GeometryFactory()
  }

  /**
   * implicit class for single GeoJson Geometries so various geometry ops can be performed
   * @param geometry
   * @tparam G
   */
  implicit class RichGeoJsonGeometry[G : JTSGeoFormat : GeoTransformer](val geometry: G)
    extends GeometryOps[G](geometry)

  /**
   * abstract class with geometry operations
   * N.B. all coordinates are assumed in the longitude-latitude space of Earth
   * @param geometry
   * @tparam G
   */
  abstract class GeometryOps[G : GeoTransformer : JTSGeoFormat](geometry: G){
    private val meanRadiusOfEarth = 6371.0
    private val geoX: GeoTransformer[G] = implicitly
    private val jtsGeoFormat: JTSGeoFormat[G] = implicitly

    /**
     * convert geometry to JTS format
     * @return com.vividsolutions.jts.geom.Geometry
     */
    def asJTS = jtsGeoFormat.toJTSGeo(geometry, RichGeoJsonFeature.geoFac)

    /**
     * translates geometry
     * @param x
     * @param y
     * @return G
     */
    def translated(x: Double, y: Double) = geoX.translate(x, y)(geometry)

    /**
     * scales geometry by a factor of x in the x-coordinate and y in the y-coordinate
     * @param x
     * @param y
     * @return G
     */
    def scaled(x: Double, y: Double) = geoX.scale(x, y)(geometry)

    /**
     * returns max latitude of geometry
     * @return Double
     */
    def maxLat: Double = geoX.maxY(geometry)

    /**
     * returns max longitude of geometry
     * @return Double
     */
    def maxLong: Double = geoX.maxX(geometry)

    /**
     * returns min latitude of geometry
     * @return Double
     */
    def minLat: Double = geoX.minY(geometry)

    /**
     * returns min longitude of geometry
     * @return Double
     */
    def minLong: Double = geoX.minX(geometry)

    /**
     * returns width of geometry
     * @return Double
     */
    def width: Double = maxLong - minLong

    /**
     * retuns height of geometry
     * @return Double
     */
    def height: Double = maxLat - minLat

    /**
     * area of the geometry's bounding box
     * @return Double
     */

    def boundingBoxArea = width * height

    private def midLat = (minLat + maxLat) / 2.0

    /**
     * width of geometry in kilometers; default value of r is the mean radius of Earth (6371.0 km)
     * @param r
     * @return Double
     */
    def widthKm(r: Double = meanRadiusOfEarth): Double = Utils.haversineDistance(midLat, minLong, midLat, maxLong, r)

    private def midLong = (minLong + maxLong) / 2.0

    /**
     * height of geometry in kilometers; default value of r is the mean radius of Earth (6371.0 km)
     * @param r
     * @return Double
     */
    def heightKm(r: Double = meanRadiusOfEarth): Double = Utils.haversineDistance(minLat, midLong, maxLat, midLong, r)

    /**
     * translates geometry to origin
     * @return G
     */
    def translatedToOrigin: G = translated(-1 * minLong, -1 * minLat)

    /**
     * area of geometry's bounding box in km**2; default value of r is mean radius of Earth (6371.0 km)
     * @param r
     * @return
     */
    def boundingBoxAreaKmSq(r: Double = meanRadiusOfEarth): Double = Utils.latLongRectangleArea(minLat, minLong, maxLat, maxLong, r)

    /**
     * area of gemoetry's bounding box in acres; default value of r is mean radius of Earth (6371.0 km)
     * http://www.wolframalpha.com/input/?i=1+sq+km+in+ac
     * @param r
     * @return Double
     */
    def boundingBoxAreaAcres(r: Double = meanRadiusOfEarth): Double = 247.105 * boundingBoxAreaKmSq(r)

    /**
     * area of geometry in acres; default value of r is mean radius of Earth (6371.0 km)
     * @param r
     * @return Double
     */
    def areaInAcres(r: Double = meanRadiusOfEarth) = asJTS.getArea / asJTS.getEnvelope.getArea * boundingBoxAreaAcres(r)

    /**
     * area of geometry in acres; default value of r is mean radius of Earth (6371.0 km)
     * @param r
     * @return Double
     */
    def areaInKmSq(r: Double = meanRadiusOfEarth) = asJTS.getArea / asJTS.getEnvelope.getArea * boundingBoxAreaKmSq(r)

    /**
     * centroid of geometry
     * @return Coordinate
     */
    def centroid: Coordinate = {
      val centroid = asJTS.getCentroid.getCoordinate
      Coordinate(BigDecimal(centroid.x), BigDecimal(centroid.y))
    }

    /**
     * bounding box of geometry
     * @return BoundingBox
     */
    def boundingBox = BoundingBox(minLong, minLat, width, height)
  }
}
