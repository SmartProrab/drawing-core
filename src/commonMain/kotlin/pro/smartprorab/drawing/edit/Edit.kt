package pro.smartprorab.drawing.edit

import pro.smartprorab.drawing.model.Contour
import pro.smartprorab.drawing.model.DrawingLimits
import pro.smartprorab.drawing.model.Point
import kotlin.math.PI
import kotlin.math.abs
import kotlin.math.atan2
import kotlin.math.roundToInt

fun Contour.moveVertex(i: Int, xMm: Double, yMm: Double, snap: SnapOptions = SnapOptions()): MoveResult {
    require(i in points.indices) { "нет вершины $i" }
    if (!snap.enabled) return MoveResult(withPoint(i, Point(xMm.roundToInt(), yMm.roundToInt())), emptySet())

    val n = points.size
    val prev = points[(i - 1 + n) % n]
    val next = points[(i + 1) % n]
    val snaps = mutableSetOf<Snap>()

    // ортогональность: по каждой оси — сосед с меньшим отклонением
    var x: Double? = null
    var y: Double? = null
    if (snap.orthoToleranceDeg > 0 && n >= 2) {
        val candidates = listOf(
            Triple(prev, Snap.PREV_HORIZONTAL, Snap.PREV_VERTICAL),
            Triple(next, Snap.NEXT_HORIZONTAL, Snap.NEXT_VERTICAL)
        )
        var bestH = Double.MAX_VALUE
        var bestV = Double.MAX_VALUE
        var snapH: Snap? = null
        var snapV: Snap? = null
        for ((p, h, v) in candidates) {
            val dx = abs(xMm - p.x)
            val dy = abs(yMm - p.y)
            if (dx == 0.0 && dy == 0.0) continue
            val fromHorizontal = atan2(dy, dx) * 180 / PI // // 0…90
            val fromVertical = 90 - fromHorizontal
            if (fromHorizontal < snap.orthoToleranceDeg && fromHorizontal < bestH) {
                bestH = fromHorizontal
                y = p.y.toDouble()
                snapH = h
            }
            if (fromVertical < snap.orthoToleranceDeg && fromVertical < bestV) {
                bestV = fromVertical
                x = p.x.toDouble()
                snapV = v
            }
        }
        snapH?.let(snaps::add)
        snapV?.let(snaps::add)

    }

    // сетка — только для осей, не выровненных по соседу
    val grid = snap.gridMm
    if (grid > 0 && (x == null || y == null)) {
        if (x == null) x = (xMm / grid).roundToInt() * grid.toDouble()
        if (y == null) y = (yMm / grid).roundToInt() * grid.toDouble()
        snaps += Snap.GRID

    }

    val point = Point((x ?: xMm).roundToInt(), (y ?: yMm).roundToInt())
    return MoveResult(withPoint(i, point), snaps)
}

/** Новая вершина на стороне [side] (от вершины side к следующей), в доле [t] её длины. */
fun Contour.insertVertex(side: Int, t: Double = 0.5): Contour {
    require(side in points.indices) { "нет стороны $side" }
    require(t > 0 && t < 1) { "t вне (0; 1): $t" }
    require(points.size < DrawingLimits.MAX_POINTS) { "больше ${DrawingLimits.MAX_POINTS} вершин" }
    val a = points[side]
    val b = points[(side + 1) % points.size]
    val p = Point(
        (a.x + (b.x - a.x) * t).roundToInt(),
        (a.y + (b.y - a.y) * t).roundToInt()
    )
    return copy(points = points.toMutableList().apply { add(side + 1, p) })
}

/** `null` — удалять нельзя: останется меньше [DrawingLimits.MIN_POINTS] вершин. */
fun Contour.deleteVertex(i: Int): Contour? {
    require(i in points.indices) { "нет вершины $i" }
    if (points.size <= DrawingLimits.MIN_POINTS) return null
    return copy(points = points.filterIndexed { k, _ -> k != i })
}

fun Contour.translate(dxMm: Int, dyMm: Int): Contour =
    copy(points = points.map { Point(it.x + dxMm, it.y + dyMm) })

private fun Contour.withPoint(i: Int, p: Point): Contour =
    copy(points = points.toMutableList().apply { set(i, p) })