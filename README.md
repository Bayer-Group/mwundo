# mwundo
[![Build Status](https://travis-ci.org/MonsantoCo/mwundo.svg?branch=master)](https://travis-ci.org/MonsantoCo/mwundo)

GeoJSON object, spray-json and circe formats, and basic Geo/Geometry

## Include in your project

Add the following to your SBT dependencies:

`"com.monsanto.labs" %% "mwundo-core" % "0.5.0"`

And then optionally add one of the provided marshalling modules:

`"com.monsanto.labs" %% "mwundo-spray" % "0.5.0"`

`"com.monsanto.labs" %% "mwundo-crice" % "0.5.0"`

For version 0.2.0 and prior, add the following to your SBT dependencies:

`"com.monsanto.labs" %% "mwundo" % "0.2.0"`


You may also need to add:

`resolvers += Resolver.bintrayRepo("monsanto", "maven")`

Since version 0.4.0, the library is also compiled for Scala.js.  As with other Scala.js dependencies, you would use `%%%` instead of `%%`.

## About
"mwundo" is Swahili for "shape," plus its fun to say.

We couldn't find a great set of GeoJson de/serializers for Scala. Beyond that, we found
ourselves continually reaching into the antique
["JTS"](http://www.vividsolutions.com/jts/JTSHome.htm) library for
basic geometry functions and needed a common place to collect our own.

We hope to build mwundo into a general purpose Geography / GIS /
Computational Geometry package for Scala, at least bit by bit.
Contributions more than welcome!
