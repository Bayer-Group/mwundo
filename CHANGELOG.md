# Change Log

All notable changes to this project will be documented in this file.
This project adheres to [Semantic Versioning](http://semver.org/).

## [0.5.1] - 2020-09-16

- Update the JTS dependency to 1.16.1

## [0.5.0] - 2018-09-19
*Breaking Change: Change coordinate primitive to Double from BigDecimal (see [#14](https://github.com/MonsantoCo/mwundo/pull/14))*

- Add suport for parsing geojson that have an optional 3rd value (elevation)


## [0.4.1] - 2018-06-29

- Adding encoder and decoder for geometry (see [#13](https://github.com/MonsantoCo/mwundo/pull/13))

## [0.4.0] - 2018-06-12

*There is a small breaking change in this version.  The Utils class had both JVM-only and generally useful classes in it, and therefore was split into two.*

- Compile for Scala.js (see [#11](https://github.com/MonsantoCo/mwundo/pull/11))

## Prior versions did not track changes in this log.
