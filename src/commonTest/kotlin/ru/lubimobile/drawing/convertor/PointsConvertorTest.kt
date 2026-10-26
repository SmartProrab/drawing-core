package ru.lubimobile.drawing.convertor

import ru.lubimobile.drawing.domain.Point
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

/**
 * Конвертация точек: сантиметры хранения ↔ целые миллиметры домена
 * и порядок осей `[вертикаль, горизонталь]` (контракт Leaflet в web).
 */
class PointsConvertorTest {

    @Test
    fun `сантиметры переводятся в миллиметры с округлением, а не отбрасыванием`() {
        // 0.3 * 10 в double — 2.9999999999999996, toLong дал бы 2
        assertEquals(3L, cmToMm(0.3))
        // 318.1 * 10 в double — 3181.0000000000005
        assertEquals(3181L, cmToMm(318.1))
        assertEquals(0L, cmToMm(0.0))
        assertEquals(-125L, cmToMm(-12.5))
    }

    @Test
    fun `миллиметры переводятся обратно в ближайший double без хвоста`() {
        assertEquals(318.1, mmToCm(3181))
        assertEquals(0.3, mmToCm(3))
        assertEquals(-12.5, mmToCm(-125))
    }

    @Test
    fun `все значения сетки 0,1 см переживают цикл туда и обратно`() {
        for (mm in -20_000L..20_000L) {
            val cm = mm / 10.0
            assertEquals(mm, cmToMm(cm), "мм $mm")
            assertEquals(cm, mmToCm(cmToMm(cm)), "мм $mm")
        }
    }

    @Test
    fun `первая координата хранения — вертикаль, вторая — горизонталь`() {
        val point = listOf(10.0, 20.5).toPoint()

        assertEquals(Point(xMm = 205, yMm = 100), point)
    }

    @Test
    fun `обратная конвертация возвращает исходный порядок осей`() {
        assertEquals(listOf(10.0, 20.5), Point(xMm = 205, yMm = 100).toDto())
    }

    @Test
    fun `точка не из двух координат отклоняется`() {
        assertFailsWith<IllegalArgumentException> { listOf(1.0).toPoint() }
        assertFailsWith<IllegalArgumentException> { listOf(1.0, 2.0, 3.0).toPoint() }
        assertFailsWith<IllegalArgumentException> { emptyList<Double>().toPoint() }
    }
}
