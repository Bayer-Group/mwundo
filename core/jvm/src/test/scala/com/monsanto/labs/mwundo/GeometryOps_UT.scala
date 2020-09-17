package com.monsanto.labs.mwundo

import com.monsanto.labs.mwundo.GeoJson._
import com.monsanto.labs.mwundo.GeoJsonImplicits._
import com.monsanto.labs.mwundo.JTSGeoFormat.MultiPolygonConverter
import org.scalatest.{FunSpec, Matchers}

/**
  * Created by dgdale on 10/29/15.
  */
class GeometryOps_UT extends FunSpec with Matchers {

  case class County(kind: String, name: String, state: String)

  describe("Geometry Ops") {
    it("should calculate the area of Lee County, IA") {

      val county = GeoJson.MultiPolygon(
        Seq(Seq(Seq(
          GeoJson.Coordinate(-91.718054, 40.616957),
          GeoJson.Coordinate(-91.71878699999999, 40.813544),
          GeoJson.Coordinate(-91.409324, 40.812681999999995),
          GeoJson.Coordinate(-91.406371, 40.808709),
          GeoJson.Coordinate(-91.393531, 40.808617999999996),
          GeoJson.Coordinate(-91.38575499999999, 40.802313),
          GeoJson.Coordinate(-91.37400199999999, 40.797979),
          GeoJson.Coordinate(-91.363872, 40.790665),
          GeoJson.Coordinate(-91.350746, 40.793642),
          GeoJson.Coordinate(-91.34683199999999, 40.786473),
          GeoJson.Coordinate(-91.33476399999999, 40.784655),
          GeoJson.Coordinate(-91.325901, 40.780781999999995),
          GeoJson.Coordinate(-91.31884699999999, 40.779921),
          GeoJson.Coordinate(-91.311674, 40.772147),
          GeoJson.Coordinate(-91.293391, 40.772577),
          GeoJson.Coordinate(-91.28675199999999, 40.766571),
          GeoJson.Coordinate(-91.28539699999999, 40.759558),
          GeoJson.Coordinate(-91.28182799999999, 40.753685),
          GeoJson.Coordinate(-91.277158, 40.752874999999996),
          GeoJson.Coordinate(-91.27287199999999, 40.754369),
          GeoJson.Coordinate(-91.263921, 40.754197999999995),
          GeoJson.Coordinate(-91.255895, 40.751081),
          GeoJson.Coordinate(-91.24772, 40.750234),
          GeoJson.Coordinate(-91.241231, 40.745625),
          GeoJson.Coordinate(-91.233449, 40.737545),
          GeoJson.Coordinate(-91.227087, 40.734348),
          GeoJson.Coordinate(-91.21958599999999, 40.733176),
          GeoJson.Coordinate(-91.215757, 40.731094),
          GeoJson.Coordinate(-91.20710199999999, 40.721115999999995),
          GeoJson.Coordinate(-91.20955, 40.713992999999995),
          GeoJson.Coordinate(-91.206486, 40.713941999999996),
          GeoJson.Coordinate(-91.200853, 40.718697),
          GeoJson.Coordinate(-91.19682, 40.719221999999995),
          GeoJson.Coordinate(-91.194414, 40.71805),
          GeoJson.Coordinate(-91.192094, 40.713651),
          GeoJson.Coordinate(-91.18873099999999, 40.712997),
          GeoJson.Coordinate(-91.18255099999999, 40.7081),
          GeoJson.Coordinate(-91.180038, 40.708303),
          GeoJson.Coordinate(-91.17837899999999, 40.709911),
          GeoJson.Coordinate(-91.18098599999999, 40.713111),
          GeoJson.Coordinate(-91.176521, 40.721092),
          GeoJson.Coordinate(-91.172933, 40.721697),
          GeoJson.Coordinate(-91.167734, 40.719763),
          GeoJson.Coordinate(-91.16256299999999, 40.722721),
          GeoJson.Coordinate(-91.161119, 40.72156),
          GeoJson.Coordinate(-91.161136, 40.717045999999996),
          GeoJson.Coordinate(-91.15063599999999, 40.706202999999995),
          GeoJson.Coordinate(-91.14566099999999, 40.706263),
          GeoJson.Coordinate(-91.141391, 40.703849),
          GeoJson.Coordinate(-91.138818, 40.700706),
          GeoJson.Coordinate(-91.131654, 40.698295),
          GeoJson.Coordinate(-91.11943699999999, 40.700008),
          GeoJson.Coordinate(-91.112467, 40.696301),
          GeoJson.Coordinate(-91.11540699999999, 40.691825),
          GeoJson.Coordinate(-91.12082, 40.672776999999996),
          GeoJson.Coordinate(-91.12392799999999, 40.669152),
          GeoJson.Coordinate(-91.138055, 40.660893),
          GeoJson.Coordinate(-91.185295, 40.637803),
          GeoJson.Coordinate(-91.19790599999999, 40.636106999999996),
          GeoJson.Coordinate(-91.218437, 40.638436999999996),
          GeoJson.Coordinate(-91.253074, 40.637962),
          GeoJson.Coordinate(-91.26495299999999, 40.633893),
          GeoJson.Coordinate(-91.306568, 40.626219),
          GeoJson.Coordinate(-91.348733, 40.609694999999995),
          GeoJson.Coordinate(-91.359873, 40.601805),
          GeoJson.Coordinate(-91.379752, 40.57445),
          GeoJson.Coordinate(-91.401482, 40.559458),
          GeoJson.Coordinate(-91.406373, 40.551831),
          GeoJson.Coordinate(-91.406202, 40.542698),
          GeoJson.Coordinate(-91.404125, 40.539127),
          GeoJson.Coordinate(-91.400725, 40.536789),
          GeoJson.Coordinate(-91.384531, 40.530947999999995),
          GeoJson.Coordinate(-91.369059, 40.512532),
          GeoJson.Coordinate(-91.364211, 40.500043),
          GeoJson.Coordinate(-91.36390999999999, 40.490122),
          GeoJson.Coordinate(-91.366463, 40.478868999999996),
          GeoJson.Coordinate(-91.37814399999999, 40.456393999999996),
          GeoJson.Coordinate(-91.381045, 40.44849),
          GeoJson.Coordinate(-91.38176899999999, 40.442555),
          GeoJson.Coordinate(-91.38017699999999, 40.432904),
          GeoJson.Coordinate(-91.37328, 40.416495999999995),
          GeoJson.Coordinate(-91.372554, 40.4012),
          GeoJson.Coordinate(-91.375712, 40.391925),
          GeoJson.Coordinate(-91.38420099999999, 40.38643),
          GeoJson.Coordinate(-91.396996, 40.383126999999995),
          GeoJson.Coordinate(-91.413011, 40.382276999999995),
          GeoJson.Coordinate(-91.419422, 40.378264),
          GeoJson.Coordinate(-91.425662, 40.382491),
          GeoJson.Coordinate(-91.441243, 40.386255),
          GeoJson.Coordinate(-91.445168, 40.382461),
          GeoJson.Coordinate(-91.445371, 40.379388),
          GeoJson.Coordinate(-91.448742, 40.376804),
          GeoJson.Coordinate(-91.45453499999999, 40.37544),
          GeoJson.Coordinate(-91.463895, 40.375659),
          GeoJson.Coordinate(-91.465891, 40.378364999999995),
          GeoJson.Coordinate(-91.463008, 40.384040999999996),
          GeoJson.Coordinate(-91.463554, 40.385546999999995),
          GeoJson.Coordinate(-91.480251, 40.381783),
          GeoJson.Coordinate(-91.483153, 40.382492),
          GeoJson.Coordinate(-91.485255, 40.384657),
          GeoJson.Coordinate(-91.490912, 40.39298),
          GeoJson.Coordinate(-91.487955, 40.402465),
          GeoJson.Coordinate(-91.488481, 40.404317),
          GeoJson.Coordinate(-91.498093, 40.401925999999996),
          GeoJson.Coordinate(-91.50527199999999, 40.403512),
          GeoJson.Coordinate(-91.509063, 40.406774999999996),
          GeoJson.Coordinate(-91.513993, 40.408536999999995),
          GeoJson.Coordinate(-91.52461199999999, 40.410765),
          GeoJson.Coordinate(-91.52642499999999, 40.413404),
          GeoJson.Coordinate(-91.52704299999999, 40.418214),
          GeoJson.Coordinate(-91.519492, 40.429950999999996),
          GeoJson.Coordinate(-91.519134, 40.432822),
          GeoJson.Coordinate(-91.52913199999999, 40.434272),
          GeoJson.Coordinate(-91.53362299999999, 40.43832),
          GeoJson.Coordinate(-91.533548, 40.440804),
          GeoJson.Coordinate(-91.53191199999999, 40.44273),
          GeoJson.Coordinate(-91.523271, 40.450061),
          GeoJson.Coordinate(-91.523517, 40.454847),
          GeoJson.Coordinate(-91.52508999999999, 40.457845),
          GeoJson.Coordinate(-91.5286, 40.459002),
          GeoJson.Coordinate(-91.552691, 40.458769),
          GeoJson.Coordinate(-91.567743, 40.462289999999996),
          GeoJson.Coordinate(-91.575806, 40.466586),
          GeoJson.Coordinate(-91.58152799999999, 40.472876),
          GeoJson.Coordinate(-91.586884, 40.487232999999996),
          GeoJson.Coordinate(-91.590817, 40.492292),
          GeoJson.Coordinate(-91.594644, 40.494997),
          GeoJson.Coordinate(-91.612821, 40.502376999999996),
          GeoJson.Coordinate(-91.616948, 40.504794),
          GeoJson.Coordinate(-91.621353, 40.510072),
          GeoJson.Coordinate(-91.622192, 40.517039),
          GeoJson.Coordinate(-91.618793, 40.526286),
          GeoJson.Coordinate(-91.618028, 40.53403),
          GeoJson.Coordinate(-91.618999, 40.539083999999995),
          GeoJson.Coordinate(-91.62190199999999, 40.542291999999996),
          GeoJson.Coordinate(-91.65434499999999, 40.549189),
          GeoJson.Coordinate(-91.681714, 40.553035),
          GeoJson.Coordinate(-91.68870000000001, 40.55739),
          GeoJson.Coordinate(-91.690804, 40.559892999999995),
          GeoJson.Coordinate(-91.691561, 40.564867),
          GeoJson.Coordinate(-91.685723, 40.576785),
          GeoJson.Coordinate(-91.686357, 40.580875),
          GeoJson.Coordinate(-91.696359, 40.588148),
          GeoJson.Coordinate(-91.712025, 40.595045999999996),
          GeoJson.Coordinate(-91.716769, 40.59853),
          GeoJson.Coordinate(-91.716432, 40.614075),
          GeoJson.Coordinate(-91.71804499999999, 40.614047),
          GeoJson.Coordinate(-91.718054, 40.616957)
        ))))

      val areaInAcres = 344958D
      val areaInKmSq = 1396D

      county.areaInAcres() / areaInAcres shouldBe 1.0 +- 1e-2

      county.areaInKmSq() / areaInKmSq shouldBe 1.0 +- 1e-2
    }

    it("should find the centroid of a multipolygon") {
      val coordinates =
        Seq(
          Seq(
            Seq(
              Coordinate(0, 0),
              Coordinate(0, 1),
              Coordinate(1, 1),
              Coordinate(1, 0)
            )
          )
        )
      val centroid = MultiPolygon(coordinates).centroid
      val (x, y) = (centroid.x.toDouble, centroid.y.toDouble)

      x shouldBe 0.5
      y shouldBe 0.5

    }
  }

}
