package pro.smartprorab.drawing.geometry

import pro.smartprorab.drawing.model.Contour
import pro.smartprorab.drawing.model.Drawing
import pro.smartprorab.drawing.model.DrawingLimits
import pro.smartprorab.drawing.model.Point
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

/** DC-VAL-01 и пределы чертежа (`DrawingLimits`). */
class ValidationTest {

    private fun contour(vararg mm: Pair<Int, Int>) = Contour(mm.map { (x, y) -> Point(x, y) })

    private val rect = contour(0 to 0, 0 to 5_600, 3_230 to 5_600, 3_230 to 0)

    /** Правильный многоугольник по часовой с [n] вершинами, радиус 5 м. */
    private fun regular(n: Int) = Contour((0 until n).map { i ->
        val a = -2 * kotlin.math.PI * i / n
        Point((5_000 * kotlin.math.cos(a)).toInt(), (5_000 * kotlin.math.sin(a)).toInt())
    })

    @Test
    fun `прямоугольник проходит проверку`() {
        assertEquals(emptySet(), rect.validate())
    }

    @Test
    fun `DC-VAL-01 бабочка — самопересечение`() {
        // несимметричная: у симметричной ещё и нулевая знаковая площадь
        val butterfly = contour(0 to 0, 3_000 to 1_000, 3_000 to 0, 0 to 2_000)

        assertEquals(setOf(ContourIssue.SELF_INTERSECTION), butterfly.validate())
    }

    @Test
    fun `DC-VAL-01 касание в вершине — самопересечение`() {
        // две «восьмёрки», сходящиеся в (1000, 1000), вершина повторяется не подряд
        val touching = contour(0 to 0, 1_000 to 1_000, 2_000 to 0, 2_000 to 2_000, 1_000 to 1_000, 0 to 2_000)

        assertTrue(ContourIssue.SELF_INTERSECTION in touching.validate())
    }

    @Test
    fun `DC-VAL-01 вершина на несоседней стороне — самопересечение`() {
        // E = (0, 1000) лежит посреди стороны AB
        val onSide = contour(0 to 0, 0 to 2_000, 2_000 to 2_000, 2_000 to 1_000, 0 to 1_000, -1_000 to 0)

        assertTrue(ContourIssue.SELF_INTERSECTION in onSide.validate())
    }

    @Test
    fun `DC-VAL-01 соседние стороны и точка посреди прямой стороны — не пересечение`() {
        val withMidpoint = contour(0 to 0, 0 to 1_000, 0 to 2_000, 2_000 to 2_000, 2_000 to 0)

        assertEquals(emptySet(), withMidpoint.validate())
    }

    @Test
    fun `Г-образный контур проходит проверку`() {
        val l = contour(0 to 0, 0 to 4_000, 2_000 to 4_000, 2_000 to 2_000, 4_000 to 2_000, 4_000 to 0)

        assertEquals(emptySet(), l.validate())
    }

    @Test
    fun `мало вершин и нулевая площадь`() {
        assertEquals(setOf(ContourIssue.TOO_FEW_POINTS), contour(0 to 0, 1_000 to 0).validate())
        assertEquals(setOf(ContourIssue.TOO_FEW_POINTS), contour().validate())
        assertEquals(setOf(ContourIssue.ZERO_AREA), contour(0 to 0, 1_000 to 0, 2_000 to 0).validate())
    }

    @Test
    fun `200 вершин — можно, 201 — нельзя`() {
        assertFalse(ContourIssue.TOO_MANY_POINTS in regular(DrawingLimits.MAX_POINTS).validate())
        assertTrue(ContourIssue.TOO_MANY_POINTS in regular(DrawingLimits.MAX_POINTS + 1).validate())
    }

    @Test
    fun `координата на пределе — можно, за пределом — нельзя`() {
        val max = DrawingLimits.MAX_COORDINATE_MM
        assertEquals(emptySet(), contour(-max to -max, -max to max, max to max, max to -max).validate())
        assertEquals(
            setOf(ContourIssue.OUT_OF_BOUNDS),
            contour(0 to 0, 0 to 1_000, max + 1 to 1_000, max + 1 to 0).validate(),
        )
    }

    @Test
    fun `чертёж без контуров и со слишком многими контурами`() {
        assertEquals(setOf(DrawingIssue.NO_CONTOURS), Drawing(emptyList()).validate().drawing)
        assertEquals(emptySet(), Drawing(List(DrawingLimits.MAX_CONTOURS) { rect }).validate().drawing)
        assertEquals(
            setOf(DrawingIssue.TOO_MANY_CONTOURS),
            Drawing(List(DrawingLimits.MAX_CONTOURS + 1) { rect }).validate().drawing,
        )
    }

    @Test
    fun `проблемы контуров — по индексу, исправные не попадают`() {
        val butterfly = contour(0 to 0, 3_000 to 1_000, 3_000 to 0, 0 to 2_000)

        val v = Drawing(listOf(rect, butterfly)).validate()

        assertEquals(mapOf(1 to setOf(ContourIssue.SELF_INTERSECTION)), v.contours)
        assertFalse(v.isValid)
        assertTrue(Drawing(listOf(rect)).validate().isValid)
    }
}
