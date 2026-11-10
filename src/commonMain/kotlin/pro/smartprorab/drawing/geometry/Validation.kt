package pro.smartprorab.drawing.geometry

import pro.smartprorab.drawing.model.Contour
import pro.smartprorab.drawing.model.Drawing
import pro.smartprorab.drawing.model.Point
import pro.smartprorab.drawing.model.DrawingLimits
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min
import kotlin.math.sign

/** Пусто — контур можно сохранить. Открыть на просмотр можно любой. */
fun Contour.validate(): Set<ContourIssue> = buildSet {
    if (points.size < DrawingLimits.MIN_POINTS) add(ContourIssue.TOO_FEW_POINTS)
    if (points.size > DrawingLimits.MAX_POINTS) add(ContourIssue.TOO_MANY_POINTS)
    if (points.any { abs(it.x) > DrawingLimits.MAX_COORDINATE_MM || abs(it.y) > DrawingLimits.MAX_COORDINATE_MM })
        add(ContourIssue.OUT_OF_BOUNDS)

    if (points.size >= DrawingLimits.MIN_POINTS && twiceSignedAreaMm2() == 0L) add(ContourIssue.ZERO_AREA)
    if (points.size > DrawingLimits.MIN_POINTS && hasSelfIntersection()) add(ContourIssue.SELF_INTERSECTION)
}


fun Drawing.validate(): DrawingValidation = DrawingValidation(
    drawing = buildSet {
        if (contours.isEmpty()) add(DrawingIssue.NO_CONTOURS)
        if (contours.size > DrawingLimits.MAX_CONTOURS) add(DrawingIssue.TOO_MANY_CONTOURS)
    },
    contours = contours.withIndex()
        .associate { (i, c) -> i to c.validate() }
        .filterValues { it.isNotEmpty() }
)

/**
 * Пересекаются ли несоседние стороны (включая касание в вершине и наложение на одной прямой).
 * O(n²) — при n ≤ 200 это 20 000 проверок, быстрее любой структуры. Арифметика целочисленная, в Long.
 */
private fun Contour.hasSelfIntersection(): Boolean {
    val n = points.size
    for (i in 0 until n) {
        for (j in i + 1 until n) {
            val adjacent = j == i + 1 || (i == 0 && j == n - 1)
            if (adjacent) continue
            if (segmentsIntersect(points[i], points[(i + 1) % n], points[j], points[(j + 1) % n])) return true
        }
    }
    return false
}

private fun segmentsIntersect(p1: Point, p2: Point, p3: Point, p4: Point): Boolean {
    val d1 = cross(p3, p4, p1).sign
    val d2 = cross(p3, p4, p2).sign
    val d3 = cross(p1, p2, p3).sign
    val d4 = cross(p1, p2, p4).sign
    if (d1 != d2 && d3 != d4 && d1 != 0 && d2 != 0 && d3 != 0 && d4 != 0) return true
    return (d1 == 0 && onSegment(p3, p1, p4)) || (d2 == 0 && onSegment(p3, p2, p4)) ||
            (d3 == 0 && onSegment(p1, p3, p2)) || (d4 == 0 && onSegment(p1, p4, p2))
}

private fun cross(o: Point, a: Point, b: Point): Long =
    (a.x - o.x).toLong() * (b.y - o.y) - (a.y - o.y).toLong() * (b.x - o.x)

private fun onSegment(p: Point, q: Point, r: Point): Boolean =
    min(p.x, r.x) <= q.x && q.x <= max(p.x, r.x) && min(p.y, r.y) <= q.y && q.y <= max(p.y, r.y)