package pro.smartprorab.drawing.geometry

import pro.smartprorab.drawing.model.Contour
import pro.smartprorab.drawing.model.Point
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

/** Длина перехода уровня: общие отрезки сторон контуров разных уровней (drawing-core.md 11). */
class LevelTransitionTest {

    private fun contour(vararg mm: Pair<Int, Int>) = Contour(mm.map { (x, y) -> Point(x, y) })
    private fun rect(x0: Int, y0: Int, x1: Int, y1: Int) = contour(x0 to y0, x0 to y1, x1 to y1, x1 to y0)

    private val low = rect(0, 0, 3_230, 3_200)
    private val high = rect(0, 3_200, 3_230, 5_600)

    @Test
    fun `DC-LVL-01 помещение разрезано поперёк — переход 3230`() {
        assertEquals(3_230, levelTransitionMm(listOf(low, high), listOf(1, 2)))
    }

    @Test
    fun `один уровень — перехода нет`() {
        assertEquals(0, levelTransitionMm(listOf(low, high), listOf(1, 1)))
        assertEquals(0, levelTransitionMm(listOf(low), listOf(1)))
        assertEquals(0, levelTransitionMm(emptyList(), emptyList()))
    }

    @Test
    fun `общая часть стороны`() {
        assertEquals(2_000, levelTransitionMm(listOf(rect(0, 0, 4_000, 2_000), rect(1_000, 2_000, 3_000, 3_000)), listOf(1, 2)))
    }

    @Test
    fun `касание в вершине — не переход`() {
        assertEquals(0, levelTransitionMm(listOf(rect(0, 0, 1_000, 1_000), rect(1_000, 1_000, 2_000, 2_000)), listOf(1, 2)))
    }

    @Test
    fun `сторона разбита лишней вершиной — длина та же`() {
        val lowWithMidpoint = contour(0 to 0, 0 to 3_200, 1_000 to 3_200, 3_230 to 3_200, 3_230 to 0)

        assertEquals(3_230, levelTransitionMm(listOf(lowWithMidpoint, high), listOf(1, 2)))
    }

    @Test
    fun `переход по диагонали — округление до мм`() {
        val left = contour(0 to 0, 0 to 4_000, 3_000 to 4_000)
        val right = contour(0 to 0, 3_000 to 4_000, 3_000 to 0)

        assertEquals(5_000, levelTransitionMm(listOf(left, right), listOf(1, 2)))
        // √(3230² + 5600²) = 6464,7…
        assertEquals(
            6_465,
            levelTransitionMm(
                listOf(contour(0 to 0, 0 to 5_600, 3_230 to 5_600), contour(0 to 0, 3_230 to 5_600, 3_230 to 0)),
                listOf(1, 2),
            ),
        )
    }

    @Test
    fun `три уровня цепочкой — сумма переходов разных уровней`() {
        val a = rect(0, 0, 3_000, 1_000)
        val b = rect(0, 1_000, 3_000, 2_000)
        val c = rect(0, 2_000, 3_000, 3_000)

        assertEquals(6_000, levelTransitionMm(listOf(a, b, c), listOf(1, 2, 3)))
        // a и c — один уровень, но не соседи; b — другой: переходы a–b и b–c
        assertEquals(6_000, levelTransitionMm(listOf(a, b, c), listOf(1, 2, 1)))
        // a и b — один уровень: остаётся b–c
        assertEquals(3_000, levelTransitionMm(listOf(a, b, c), listOf(1, 1, 2)))
    }

    @Test
    fun `уровней столько же сколько контуров`() {
        assertFailsWith<IllegalArgumentException> { levelTransitionMm(listOf(low, high), listOf(1)) }
    }
}
