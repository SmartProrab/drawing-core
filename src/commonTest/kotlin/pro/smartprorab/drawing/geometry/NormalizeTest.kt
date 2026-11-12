package pro.smartprorab.drawing.geometry

import pro.smartprorab.drawing.model.Contour
import pro.smartprorab.drawing.model.Point
import kotlin.test.Test
import kotlin.test.assertEquals

/** DC-NORM-01: обход по часовой, `A` остаётся первой, повторы убраны. */
class NormalizeTest {

    private fun contour(vararg mm: Pair<Int, Int>) = Contour(mm.map { (x, y) -> Point(x, y) })

    private val a = 0 to 0
    private val b = 3_230 to 0
    private val c = 3_230 to 5_600
    private val d = 0 to 5_600

    @Test
    fun `DC-NORM-01 против часовой разворачивается · A остаётся первой`() {
        val ccw = contour(a, b, c, d)

        val n = ccw.normalized()

        assertEquals(contour(a, d, c, b), n)
        assertEquals(-ccw.twiceSignedAreaMm2(), n.twiceSignedAreaMm2())
    }

    @Test
    fun `DC-NORM-01 по часовой не меняется`() {
        val cw = contour(a, d, c, b)

        assertEquals(cw, cw.normalized())
    }

    @Test
    fun `DC-NORM-01 подряд идущие повторы и замыкающая точка убираются`() {
        val raw = contour(a, a, d, c, c, c, b, a)

        assertEquals(contour(a, d, c, b), raw.normalized())
    }

    @Test
    fun `DC-NORM-01 тип контура сохраняется`() {
        val ccw = contour(a, b, c, d)

        assertEquals(ccw.type, ccw.normalized().type)
    }

    @Test
    fun `DC-NORM-01 все точки совпадают — остаётся одна`() {
        assertEquals(contour(a), contour(a, a, a).normalized())
    }

    @Test
    fun `DC-NORM-01 пустой контур остаётся пустым`() {
        assertEquals(contour(), contour().normalized())
    }
}
