package pro.smartprorab.drawing.model

/** Пределы чертежа (smart_3, docs/libraries/drawing-core.md, 2). */
object DrawingLimits {
    const val MIN_POINTS = 3
    const val MAX_POINTS = 200
    const val MAX_CONTOURS = 20
    const val MAX_COORDINATE_MM = 1_000_000
}