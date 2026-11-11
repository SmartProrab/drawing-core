@file:OptIn(ExperimentalJsExport::class)
@file:JsExport
package pro.smartprorab.drawing.js

class BuildResultJs(
    val points: Array<Array<Int>>,
    val angularMisclosureDeg: Double,
    val linearMisclosureMm: Double,
    val linearMisclosureRatio: Double
)