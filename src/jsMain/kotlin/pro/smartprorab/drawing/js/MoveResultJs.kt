@file:OptIn(ExperimentalJsExport::class)
@file:JsExport
package pro.smartprorab.drawing.js

/** snaps — имена [Snap]: GRID, PREV_HORIZONTAL, … */
class MoveResultJs(
    val points: Array<Array<Int>>,
    val snaps: Array<String>
)