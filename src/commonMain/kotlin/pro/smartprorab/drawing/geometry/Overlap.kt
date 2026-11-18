package pro.smartprorab.drawing.geometry

import pro.smartprorab.drawing.model.Contour
import pro.smartprorab.drawing.model.Point
import kotlin.collections.component1
import kotlin.collections.component2
import kotlin.math.abs
import kotlin.math.hypot
import kotlin.math.max
import kotlin.math.min

/** Общая площадь больше нуля. Касание по стороне или в вершине — не перекрытие. Обход любой. */
fun Contour.overlaps(other: Contour): Boolean {
    if (!boundsIntersect(other)) return false
    if (edges().any { e -> other.edges().any { f -> crossProperly(e.first, e.second, f.first, f.second) } }) return true
    return probes().any { other.strictlyContains(it) } || other.probes().any { strictlyContains(it) }
}

internal fun Contour.edges(): List<Pair<Point, Point>> =
    points.indices.map { points[it] to points[(it + 1) % points.size] }

private fun Contour.boundsIntersect(o: Contour): Boolean =
    points.maxOf { it.x } >= o.points.minOf { it.x } && o.points.maxOf { it.x } >= points.minOf { it.x } &&
            points.maxOf { it.y } >= o.points.minOf { it.y } && o.points.maxOf { it.y } >= points.minOf { it.y }

/** Пробные точки внутри контура или на его границе: вершины, середины сторон и середины, сдвинутые внутрь на 0,25 мм. */
private fun Contour.probes(): List<Pair<Double, Double>> {
    val inward = if (twiceSignedAreaMm2() < 0) -1.0 else 1.0 // по часовой внутренность справа от стороны
    return points.map { it.x.toDouble() to it.y.toDouble() } + edges().flatMap { (a, b) ->
        val mx = (a.x + b.x) / 2.0
        val my = (a.y + b.y) / 2.0
        val len = hypot((b.x - a.x).toDouble(), (b.y - a.y).toDouble())

        val nx = -(b.y - a.y) / len * 0.25 * inward
        val ny = (b.x - a.x) / len * 0.25 * inward
        listOf(mx to my, (mx + nx) to (my + ny))
    }
}

/** Строго внутри: на границе — нет. */
internal fun Contour.strictlyContains(p: Pair<Double, Double>): Boolean {
    val (x, y) = p
    var inside = false
    for ((a, b) in edges()) {
        if (onSegment(a, b, x, y)) return false
        val (ax, ay, bx, by) = listOf(a.x.toDouble(), a.y.toDouble(), b.x.toDouble(), b.y.toDouble())
        if ((ay > y) != (by > y) && x < (bx - ax) * (y - ay) / (by - ay) + ax) inside = !inside
    }
    return inside
}

private fun onSegment(a: Point, b: Point, x: Double, y: Double): Boolean {
    val cross = (b.x - a.x) * (y - a.y) - (b.y - a.y) * (x - a.x)
    return abs(cross) < 1e-9 && x >= min(a.x, b.x) - 1e-9 && x <= max(a.x, b.x) + 1e-9 &&
            y >= min(a.y, b.y) - 1e-9 && y <= max(a.y, b.y) + 1e-9
}

/** Пересечение крест-накрест: каждый отрезок строго по разные стороны другого. */
internal fun crossProperly(p1: Point, p2: Point, p3: Point, p4: Point): Boolean {
    val d1 = cross(p3, p4, p1)
    val d2 = cross(p3, p4, p2)
    val d3 = cross(p1, p2, p3)
    val d4 = cross(p1, p2, p4)
    return d1 != 0L && d2 != 0L && d3 != 0L && d4 != 0L && (d1 > 0) != (d2 > 0) && (d3 > 0) != (d4 > 0)
}

internal fun cross(o: Point, a: Point, b: Point): Long =
    (a.x - o.x).toLong() * (b.y - o.y) - (a.y - o.y).toLong() * (b.x - o.x)

