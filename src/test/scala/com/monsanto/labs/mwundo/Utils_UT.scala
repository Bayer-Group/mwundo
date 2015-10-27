package com.monsanto.labs.mwundo

import org.scalatest.{Matchers, FunSpec}

/**
 * Created by Ryan Richt on 10/27/15
 */
class Utils_UT extends FunSpec with Matchers {
  describe("Utils"){
    it("should correctly calculate area of a lat/long rectangle"){

      val surfaceAreaOfEarth = Math.pow(6371.0, 2.0) * 4 * Math.PI

      Utils.latLongRectangleArea(-90, -180, 90, 180) shouldBe surfaceAreaOfEarth +- 0.0001
    }
  }

}
