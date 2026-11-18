package pro.smartprorab.drawing.edit

import pro.smartprorab.drawing.geometry.cross
import pro.smartprorab.drawing.geometry.crossProperly
import pro.smartprorab.drawing.geometry.edges
import pro.smartprorab.drawing.geometry.normalized
import pro.smartprorab.drawing.geometry.strictlyContains
import pro.smartprorab.drawing.geometry.twiceSignedAreaMm2
import pro.smartprorab.drawing.model.Contour
import pro.smartprorab.drawing.model.Point
import kotlin.math.max
import kotlin.math.min

/**
 * Разрез ломаной [cut] на два контура (drawing-core.md 11). null — линия не делит контур:
 * конец не на стороне, промежуточная точка не строго внутри, отрезок выходит наружу или идёт по стороне,
 * разрез пересекает сам себя или касается границы не концом. Части — по часовой; исходные вершины сохраняются;
 * часть с вершиной A (если A — не конец разреза) — первая и начинается с A.
 */
fun Contour.split(cut: List<Point>): Pair<Contour, Contour>? {
    if (cut.size < 2 || cut.first() == cut.last()) return null
    val c = normalized()
    val start = c.locate(cut.first()) ?: return null
    val end = c.locate(cut.last()) ?: return null
    if (cut.subList(1, cut.size - 1).any { !c.strictlyContains(it.x.toDouble() to it.y.toDouble()) }) return null
    if (!c.cutIsClean(cut)) return null

    // вставить концы как вершины: (сторона, расстояние от её начала) → по порядку вдоль стороны
    val inserts = listOf(start, end).filter { it.second > 0 }.sortedWith(compareBy({ it.first }, { it.second }))
    val ring = buildList {
        c.points.forEachIndexed { k, p ->
            add(p)
            inserts.filter { it.first == k }.forEach { add(it.third) }
        }
    }
    val s = ring.indexOf(cut.first())
    val e = ring.indexOf(cut.last())
    val inner = cut.subList(1, cut.size - 1)
    val partSE = Contour(arc(ring, s, e) + inner.reversed(), c.type).normalized()
    val partES = Contour(arc(ring, e, s) + inner, c.type).normalized()
    if (partSE.twiceSignedAreaMm2() == 0L || partES.twiceSignedAreaMm2() == 0L) return null

    val a = c.points.first()
    if (a == cut.first() || a == cut.last()) return partSE to partES
    return if (a in partSE.points) partSE.startingAt(a) to partES else partES.startingAt(a) to partSE
}

/** Где точка на границе: (индекс стороны, квадрат расстояния от её начала, точка). Вершина — расстояние 0. */
private fun Contour.locate(p: Point): Triple<Int, Long, Point>? {
    edges().forEachIndexed { k, (a, b) ->
        if (cross(a, b, p) == 0L && p.x in min(a.x, b.x)..max(a.x, b.x) && p.y in min(a.y, b.y)..max(a.y, b.y)) {
            val dx = (p.x - a.x).toLong(); val dy = (p.y - a.y).toLong()
            return Triple(k, dx * dx + dy * dy, p)
        }
    }
    return null
}

/** Отрезки разреза: внутри контура, границу трогают только концами разреза, друг друга — только соседними концами. */
private fun Contour.cutIsClean(cut: List<Point>): Boolean {
    val last = cut.size - 2
    for (i in 0..last) {
        val p = cut[i]; val q = cut[i + 1]
        val mid = (p.x + q.x) / 2.0 to (p.y + q.y) / 2.0
        if (!strictlyContains(mid)) return false // по стороне или наружу
        val allowed = setOfNotNull(cut.first().takeIf { i == 0 }, cut.last().takeIf { i == last })
        for ((a, b) in edges()) {
            if (crossProperly(p, q, a, b)) return false
            // точки касания: концы отрезка на стороне и вершины стороны на отрезке
            val touches = listOf(p, q).filter { onSeg(a, b, it) } + listOf(a, b).filter { onSeg(p, q, it) }
            if (touches.any { it !in allowed }) return false
        }
        for (j in i + 2..last) {
            if (i == 0 && j == last && cut.first() == cut.last()) continue
            if (crossProperly(p, q, cut[j], cut[j + 1]) ||
                listOf(cut[j], cut[j + 1]).any { onSeg(p, q, it) } || listOf(p, q).any { onSeg(cut[j], cut[j + 1], it) }
            ) return false
        }
    }
    return true
}

private fun onSeg(a: Point, b: Point, p: Point) =
    cross(a, b, p) == 0L && p.x in min(a.x, b.x)..max(a.x, b.x) && p.y in min(a.y, b.y)..max(a.y, b.y)

/** Вершины кольца от i до j включительно, по ходу. */
private fun arc(ring: List<Point>, i: Int, j: Int): List<Point> =
    if (i <= j) ring.subList(i, j + 1) else ring.subList(i, ring.size) + ring.subList(0, j + 1)

private fun Contour.startingAt(p: Point): Contour {
    val k = points.indexOf(p)
    return copy(points = points.subList(k, points.size) + points.subList(0, k))
}