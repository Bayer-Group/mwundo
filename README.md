# mwundo
[![Build Status](https://travis-ci.org/MonsantoCo/mwundo.svg?branch=master)](https://travis-ci.org/MonsantoCo/mwundo)

GeoJSON object, spray-json formats, and basic Geo/Geometry

## Include in your project
For version 0.2.0 and prior, add the following to your SBT dependencies:

`"com.monsanto.labs" %% "mwundo" % "0.2.0"`

For later versions, add the following to your SBT dependencies:

`"com.monsanto.labs" %% "mwundo-core" % "0.3.0"`

And then optionally add one of the provided marshalling modules:

`"com.monsanto.labs" %% "mwundo-spray" % "0.3.0"`

You may also need to add:

`resolvers += Resolver.bintrayRepo("monsanto", "maven")`

## About
"mwundo" is Swahili for "shape," plus its fun to say.

We couldn't find a great set of GeoJson de/serializers for Scala,
especially using our favorite spray-json. Beyond that, we found
ourselves continually reaching into the antique
["JTS"](http://www.vividsolutions.com/jts/JTSHome.htm) library for
basic geometry functions and needed a common place to collect our own.

We hope to build mwundo into a general purpose Geography / GIS /
Computational Geometry package for Scala, at least bit by bit.
Contributions more than welcome!
