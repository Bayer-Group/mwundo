# mwundo
GeoJSON object, spray-json formats, and basic Geo/Geometry

## Include in your project
Add the following to your SBT dependencies:

`"com.monsanto.labs"          %% "mwundo"                 % "0.0.39"`

You may also need to add:

`resolvers += Resolver.bintrayRepo("monsanto", "maven")`

## About
"mwundo" is Swahili for "shape," plus its fun to say.

We couldn't find a great set of GeoJson de/serializers for Scala, especially using our favorite spray-json. Beyond that, we found ourselves continually reaching into the antique ["JTS"](http://www.vividsolutions.com/jts/JTSHome.htm) library for basic geometry functions and needed a common place to collect our own.

We hope to build mwundo into a general purpose Geography / GIS / Computational Geometry package for Scala, at least bit by bit. Contributions more than welcome!
