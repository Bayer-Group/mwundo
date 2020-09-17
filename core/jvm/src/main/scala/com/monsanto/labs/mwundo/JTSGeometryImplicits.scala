package com.monsanto.labs.mwundo

import org.locationtech.jts.geom.Geometry

object JTSGeometryImplicits {
  implicit class RichJTSGeometry(geom: Geometry) {
    def as[G <: GeoJson.Geometry: JTSGeoFormat]: G = implicitly[JTSGeoFormat[G]].fromJTSGeo(geom)
  }

  //  implicit class RichGeometryCollection[G <: Geometry](geomC: GeometryCollection)
  //    extends Traversable[G]
  //    with TraversableLike[G, RichGeometryCollection[G]]
  //    with GenericTraversableTemplate[G, RichGeometryCollection]{
  //
  //    override def companion: GenericCompanion[RichGeometryCollection] = RichGeometryCollection
  //
  //    override def foreach[U](f: (G) => U): Unit =
  //      Seq.tabulate(geomC.getNumGeometries)(i => geomC.getGeometryN(i)).foreach(x => f(x.asInstanceOf[G]))
  //  }
  //  object RichGeometryCollection extends TraversableFactory[RichGeometryCollection]{
  //
  //    val geoFac = new GeometryFactory()
  //
  //    implicit def canBuildFrom[G]: CanBuildFrom[Coll, G, RichGeometryCollection[G]] = new GenericCanBuildFrom[G]
  //
  //    def newBuilder[G <: Geometry : ClassTag]: mutable.Builder[G, RichGeometryCollection[G]] = new mutable.Builder[G, RichGeometryCollection[G]] {
  //
  //      var contents = Seq.empty[G]
  //      def +=(elem: G): this.type = { contents :+ elem ; this }
  //      def result(): RichGeometryCollection[G] = RichGeometryCollection( new GeometryCollection(contents.toArray, geoFac) )
  //      def clear(): Unit = {contents = Seq.empty[G]}
  //    }
  //  }
}
