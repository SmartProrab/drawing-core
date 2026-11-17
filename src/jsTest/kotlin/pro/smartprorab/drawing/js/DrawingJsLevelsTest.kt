package pro.smartprorab.drawing.js

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull

/** Фасад 0.2.0: перекрытие, переход уровня, разрез (drawing-core.md 8, 11). */
class DrawingJsLevelsTest {

    private fun rect(x0: Int, y0: Int, x1: Int, y1: Int): Array<Array<Int>> =
        arrayOf(arrayOf(x0, y0), arrayOf(x0, y1), arrayOf(x1, y1), arrayOf(x1, y0))

    private fun Array<Array<Int>>.flat(): List<Int> = flatMap { it.toList() }

    private val low = rect(0, 0, 3_230, 3_200)
    private val high = rect(0, 3_200, 3_230, 5_600)

    @Test
    fun `overlappingContours — пары индексов, касание не в счёт`() {
        val pairs = overlappingContours(arrayOf(rect(0, 0, 2_000, 2_000), rect(2_000, 0, 4_000, 2_000), rect(1_000, 1_000, 1_500, 1_500)))

        assertEquals(listOf(listOf(0, 2)), pairs.map { it.toList() })
        assertEquals(0, overlappingContours(arrayOf(low, high)).size)
    }

    @Test
    fun `levelTransitionMm — как в общем коде`() {
        assertEquals(3_230, levelTransitionMm(arrayOf(low, high), arrayOf(1, 2)))
        assertEquals(0, levelTransitionMm(arrayOf(low, high), arrayOf(2, 2)))
    }

    @Test
    fun `split — две части или null`() {
        val parts = split(rect(0, 0, 3_230, 5_600), arrayOf(arrayOf(0, 3_200), arrayOf(3_230, 3_200)))

        assertNotNull(parts)
        assertEquals(2, parts.size)
        assertEquals(listOf(0, 0, 0, 3_200, 3_230, 3_200, 3_230, 0), parts[0].flat())
        assertEquals(10.3 + 7.8, areaM2(parts[0]) + areaM2(parts[1]), 1e-9)

        assertNull(split(rect(0, 0, 4_000, 3_000), arrayOf(arrayOf(0, 1_000), arrayOf(1_500, 1_000))))
    }
}
