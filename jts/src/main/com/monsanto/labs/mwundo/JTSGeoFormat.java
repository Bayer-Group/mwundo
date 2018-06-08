package com.monsanto.labs.mwundo;

/**
 * Created by Ryan Richt on 10/26/15
 */

trait JTSGeoFormat[G] {
  def toJTSGeo(g: G, gf: GeometryFactory): Geometry
  def fromJTSGeo(geo: Geometry): G
}
