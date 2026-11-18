package pro.smartprorab.drawing.geometry

import pro.smartprorab.drawing.model.Contour
import pro.smartprorab.drawing.model.Point
import kotlin.math.hypot
import kotlin.math.max
import kotlin.math.min
import kotlin.math.roundToInt

/** Сумма общих отрезков сторон контуров разных уровней, мм; округляется итог. */
fun levelTransitionMm(contours: List<Contour>, levels: List<Int>): Int {
    require(contours.size == levels.size) { "уровней ${levels.size}, контуров ${contours.size}" }
    var total = 0.0
    for (i in contours.indices) for (j in i + 1 until contours.size) {
        if (levels[i] == levels[j]) continue
        for ((a, b) in contours[i].edges()) for ((c, d) in contours[j].edges()) total += sharedLength(a, b, c, d)
    }
    return total.roundToInt()
}

/** Длина общей части двух отрезков, если они на одной прямой; иначе 0. */
private fun sharedLength(a: Point, b: Point, c: Point, d: Point): Double {
    if (cross(a, b, c) != 0L || cross(a, b, d) != 0L) return 0.0
    val dx = (b.x - a.x).toDouble()
    val dy = (b.y - a.y).toDouble()
    val len2 = dx * dx + dy * dy
    if (len2 == 0.0) return 0.0
    fun t(p: Point) = ((p.x - a.x) * dx + (p.y - a.y) * dy) / len2
    val overlap = min(1.0, max(t(c), t(d))) - max(0.0, min(t(c), t(d)))
    return if (overlap > 0) overlap * hypot(dx, dy) else 0.0
}