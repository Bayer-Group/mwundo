package com.monsanto.labs.mwundo

/**
 * Created by Ryan Richt on 10/26/15
 */
object Utils {
  def haversineDistance(lat1: Double, lng1: Double, lat2: Double, lng2: Double) = {
    val r = 6371.0 // average radius of the earth in km
    val dLat = Math.toRadians(lat2 - lat1)
    val dLon = Math.toRadians(lng2 - lng1)
    val a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
            Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) * Math.sin(dLon / 2) * Math.sin(dLon / 2)
    val c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a))
    val d = r * c
    d
  }
}
