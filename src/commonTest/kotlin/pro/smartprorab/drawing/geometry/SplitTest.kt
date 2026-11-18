package pro.smartprorab.drawing.geometry

import pro.smartprorab.drawing.edit.split
import pro.smartprorab.drawing.model.Contour
import pro.smartprorab.drawing.model.Point
import kotlin.math.hypot
import kotlin.math.roundToInt
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue
import kotlin.test.fail

/**
 * Разрез контура ломаной на два (drawing-core.md 11, smart_3 measure.md 6.7).
 * Концы разреза — на сторонах (можно в вершинах), промежуточные точки — строго внутри, разрез без самопересечений.
 * Части — по часовой; часть с исходной вершиной A (если A — не конец разреза) первая и начинается с A.
 */
class SplitTest {

    private fun pt(x: Int, y: Int) = Point(x, y)
    private fun contour(vararg mm: Pair<Int, Int>) = Contour(mm.map { (x, y) -> Point(x, y) })
    private fun cut(vararg mm: Pair<Int, Int>) = mm.map { (x, y) -> Point(x, y) }
    private fun rect(x0: Int, y0: Int, x1: Int, y1: Int) = contour(x0 to y0, x0 to y1, x1 to y1, x1 to y0)

    /** Один и тот же замкнутый обход с точностью до начальной вершины. */
    private fun sameRing(expected: Contour, actual: Contour): Boolean {
        val e = expected.points
        val a = actual.points
        if (e.size != a.size) return false
        return e.indices.any { shift -> e.indices.all { i -> a[i] == e[(i + shift) % e.size] } }
    }

    private fun assertParts(expected: Set<Contour>, actual: Pair<Contour, Contour>?) {
        assertNotNull(actual, "разрез должен получиться")
        val parts = listOf(actual.first, actual.second)
        expected.forEach { e ->
            if (parts.none { sameRing(e, it) }) fail("нет части ${e.points}; получено ${parts.map { it.points }}")
        }
    }

    /** Общие свойства любого удачного разреза. */
    private fun assertSplitInvariants(source: Contour, cutLine: List<Point>, parts: Pair<Contour, Contour>) {
        val (a, b) = parts
        assertEquals(source.normalized().twiceSignedAreaMm2(), a.twiceSignedAreaMm2() + b.twiceSignedAreaMm2(), "площадь сохраняется")
        assertTrue(a.twiceSignedAreaMm2() < 0 && b.twiceSignedAreaMm2() < 0, "обе части по часовой")
        assertEquals(emptySet(), a.validate())
        assertEquals(emptySet(), b.validate())
        assertTrue(!a.overlaps(b), "части не перекрываются")
        val cutLength = cutLine.zipWithNext { p, q -> hypot((q.x - p.x).toDouble(), (q.y - p.y).toDouble()) }.sum()
        assertEquals(cutLength.roundToInt(), levelTransitionMm(listOf(a, b), listOf(1, 2)), "общая граница = разрез")
    }

    // --- DC-SPL-01: прямой разрез ---

    @Test
    fun `DC-SPL-01 прямоугольник поперёк`() {
        val room = rect(0, 0, 3_230, 5_600)
        val line = cut(0 to 3_200, 3_230 to 3_200)

        val parts = room.split(line)

        assertParts(setOf(rect(0, 0, 3_230, 3_200), rect(0, 3_200, 3_230, 5_600)), parts)
        assertSplitInvariants(room, line, parts!!)
        assertEquals(pt(0, 0), parts.first.points.first(), "часть с A — первая и начинается с A")
    }

    @Test
    fun `DC-SPL-01 разрез в обратную сторону — те же части`() {
        val room = rect(0, 0, 3_230, 5_600)

        assertParts(
            setOf(rect(0, 0, 3_230, 3_200), rect(0, 3_200, 3_230, 5_600)),
            room.split(cut(3_230 to 3_200, 0 to 3_200)),
        )
    }

    @Test
    fun `DC-SPL-01 по диагонали — концы в вершинах`() {
        val room = rect(0, 0, 3_230, 5_600)
        val line = cut(0 to 0, 3_230 to 5_600)

        val parts = room.split(line)

        assertParts(setOf(contour(0 to 0, 0 to 5_600, 3_230 to 5_600), contour(0 to 0, 3_230 to 5_600, 3_230 to 0)), parts)
        assertSplitInvariants(room, line, parts!!)
    }

    @Test
    fun `DC-SPL-01 контур против часовой — части всё равно по часовой`() {
        val ccw = Contour(rect(0, 0, 3_230, 5_600).points.reversed())

        val parts = ccw.split(cut(0 to 3_200, 3_230 to 3_200))

        assertNotNull(parts)
        assertTrue(parts.first.twiceSignedAreaMm2() < 0 && parts.second.twiceSignedAreaMm2() < 0)
    }

    // --- DC-SPL-02: ломаная ---

    @Test
    fun `DC-SPL-02 Г-образный разрез — угол и остаток`() {
        val room = rect(0, 0, 3_000, 4_000)
        val line = cut(0 to 2_000, 1_500 to 2_000, 1_500 to 0)

        val parts = room.split(line)

        assertParts(
            setOf(
                rect(0, 0, 1_500, 2_000),
                contour(0 to 2_000, 0 to 4_000, 3_000 to 4_000, 3_000 to 0, 1_500 to 0, 1_500 to 2_000),
            ),
            parts,
        )
        assertSplitInvariants(room, line, parts!!)
        assertEquals(pt(0, 0), parts.first.points.first())
    }

    @Test
    fun `DC-SPL-02 оба конца на одной стороне — вырезанный короб`() {
        val room = rect(0, 0, 4_000, 3_000)
        val line = cut(1_000 to 0, 1_000 to 1_000, 2_000 to 1_000, 2_000 to 0)

        val parts = room.split(line)

        assertParts(
            setOf(
                rect(1_000, 0, 2_000, 1_000),
                contour(0 to 0, 0 to 3_000, 4_000 to 3_000, 4_000 to 0, 2_000 to 0, 2_000 to 1_000, 1_000 to 1_000, 1_000 to 0),
            ),
            parts,
        )
        assertSplitInvariants(room, line, parts!!)
        assertEquals(pt(0, 0), parts.first.points.first())
    }

    @Test
    fun `DC-SPL-02 Г-образное помещение — разрез по продолжению внутреннего угла`() {
        val l = contour(0 to 0, 0 to 4_000, 2_000 to 4_000, 2_000 to 2_000, 4_000 to 2_000, 4_000 to 0)
        val line = cut(2_000 to 2_000, 2_000 to 0)

        val parts = l.split(line)

        // вершина (2000, 2000) остаётся в обеих частях: разрез не убирает исходные вершины
        assertParts(
            setOf(
                contour(0 to 0, 0 to 4_000, 2_000 to 4_000, 2_000 to 2_000, 2_000 to 0),
                contour(2_000 to 0, 2_000 to 2_000, 4_000 to 2_000, 4_000 to 0),
            ),
            parts,
        )
        assertSplitInvariants(l, line, parts!!)
    }

    // --- DC-SPL-03: разрез не делит контур ---

    private val room = rect(0, 0, 4_000, 3_000)

    @Test
    fun `DC-SPL-03 меньше двух точек`() {
        assertNull(room.split(cut(0 to 1_000)))
        assertNull(room.split(emptyList()))
    }

    @Test
    fun `DC-SPL-03 конец внутри или снаружи`() {
        assertNull(room.split(cut(0 to 1_000, 1_500 to 1_000)), "конец внутри")
        assertNull(room.split(cut(-500 to 1_000, 4_000 to 1_000)), "начало снаружи")
    }

    @Test
    fun `DC-SPL-03 промежуточная точка снаружи`() {
        assertNull(room.split(cut(0 to 1_000, 2_000 to 5_000, 4_000 to 1_000)))
    }

    @Test
    fun `DC-SPL-03 промежуточная точка на границе`() {
        assertNull(room.split(cut(0 to 1_000, 2_000 to 0, 4_000 to 1_000)))
    }

    @Test
    fun `DC-SPL-03 разрез по стороне`() {
        assertNull(room.split(cut(0 to 0, 0 to 3_000)))
        assertNull(room.split(cut(0 to 500, 0 to 2_500)))
    }

    @Test
    fun `DC-SPL-03 разрез пересекает сам себя`() {
        assertNull(room.split(cut(0 to 1_000, 3_000 to 2_000, 3_000 to 500, 1_000 to 2_500, 4_000 to 2_500)))
    }

    @Test
    fun `DC-SPL-03 концы совпадают`() {
        assertNull(room.split(cut(0 to 1_000, 2_000 to 1_500, 0 to 1_000)))
    }

    @Test
    fun `DC-SPL-03 отрезок разреза выходит наружу через вогнутый угол`() {
        val l = contour(0 to 0, 0 to 4_000, 2_000 to 4_000, 2_000 to 2_000, 4_000 to 2_000, 4_000 to 0)
        // от (0, 3000) к (4000, 1000): проходит над вырезом Г — снаружи контура
        assertNull(l.split(cut(0 to 3_000, 4_000 to 1_000)))
    }
}
