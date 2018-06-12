package com.monsanto.labs.mwundo

import com.vividsolutions.jts.geom._
import collection.JavaConverters._

class JTSUtils {

    //TODO: this might not work for envelopes with convexities?
    def clip(geom: Geometry, clipEnv: Envelope): GeometryCollection = {
      val clipPoly: Geometry = geom.getFactory.toGeometry(clipEnv)

      val geos = Seq.tabulate(geom.getNumGeometries)(i => geom.getGeometryN(i))

      // LineStrings are from clipped polygons that no longer have any area, discard them
      val clippedGeos = geos.collect{
        case g: Geometry if ! g.isInstanceOf[LineString] && clipEnv.contains(g.getEnvelopeInternal)   => g
        case g: Geometry if ! g.isInstanceOf[LineString] && clipEnv.intersects(g.getEnvelopeInternal) =>
          val intermedResult = clipPoly.intersection(g)
          intermedResult.setUserData(g.getUserData)
          intermedResult
        // case completely outside of the clipping envelope, do not include in output list
      }.collect{
        case g: Geometry if g.isInstanceOf[Polygon] => g
      }

      geom.getFactory.createGeometryCollection(GeometryFactory.toGeometryArray(clippedGeos.asJavaCollection))
    }

    //TODO: this might not work for envelopes with convexities?
    def clip(geom: Geometry, clipPoly: Geometry): GeometryCollection = {

      val geos = Seq.tabulate(geom.getNumGeometries)(i => geom.getGeometryN(i))

      // LineStrings are from clipped polygons that no longer have any area, discard them
      val clippedGeos = geos.collect{
        case g: Geometry if ! g.isInstanceOf[LineString] && clipPoly.contains(g)   => g
        case g: Geometry if ! g.isInstanceOf[LineString] && clipPoly.intersects(g) =>
          val intermedResult = clipPoly.intersection(g)
          intermedResult.setUserData(g.getUserData)
          intermedResult
        // case completely outside of the clipping envelope, do not include in output list
      }.collect {
        case g: Geometry if g.isInstanceOf[Polygon] => g
      }

      geom.getFactory.createGeometryCollection(GeometryFactory.toGeometryArray(clippedGeos.asJavaCollection))
    }
}
