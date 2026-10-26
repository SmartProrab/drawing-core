package ru.lubimobile.drawing.geometry

import ru.lubimobile.drawing.domain.Contour
import ru.lubimobile.drawing.domain.Point
import ru.lubimobile.drawing.fixtures.FixtureTag
import ru.lubimobile.drawing.fixtures.MeasureFixtures
import ru.lubimobile.drawing.fixtures.decodeFixture
import ru.lubimobile.drawing.convertor.toDrawing
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

/**
 * Площадь, периметр и знак обхода контура.
 * Правило (#2): площадь — точно в целых мм², half-up до 0,1 м²; периметр — half-up до 0,1 м.
 */
class GeometryTest {

    private fun contour(vararg mm: Pair<Long, Long>) =
        Contour(points = mm.map { (x, y) -> Point(xMm = x, yMm = y) }, diagonals = emptyList())

    /** Прямоугольник от начала координат, обход против часовой. */
    private fun rect(wMm: Long, hMm: Long) = contour(0L to 0L, wMm to 0L, wMm to hMm, 0L to hMm)

    @Test
    fun `площадь и периметр прямоугольника`() {
        val c = rect(3_000, 2_000)

        assertEquals(60L, c.areaTenthsM2())
        assertEquals(100L, c.perimeterTenthsM())
    }

    @Test
    fun `ровная половина площади округляется вверх`() {
        // 4,5 × 4,1 м = 18,45 м² — старый web округлял до 18,4
        assertEquals(185L, rect(4_500, 4_100).areaTenthsM2())
        // 10 × 50 см = 0,05 м²
        assertEquals(1L, rect(100, 500).areaTenthsM2())
    }

    @Test
    fun `площадь чуть меньше половины округляется вниз`() {
        // 49 999 мм² = 0,049999 м²
        assertEquals(0L, contour(0L to 0L, 49_999L to 0L, 49_999L to 1L, 0L to 1L).areaTenthsM2())
    }

    @Test
    fun `ровная половина периметра округляется вверх`() {
        // 2 × (250 + 275) мм = 1,05 м
        assertEquals(11L, rect(250, 275).perimeterTenthsM())
    }

    @Test
    fun `периметр учитывает замыкающую сторону и наклонные стороны`() {
        // треугольник 3-4-5 м
        assertEquals(120L, contour(0L to 0L, 3_000L to 0L, 0L to 4_000L).perimeterTenthsM())
    }

    @Test
    fun `знак удвоенной площади отражает направление обхода`() {
        val ccw = rect(1_000, 1_000)
        val cw = Contour(points = ccw.points.reversed(), diagonals = emptyList())

        assertEquals(2_000_000L, ccw.twiceSignedAreaMm2())
        assertEquals(-2_000_000L, cw.twiceSignedAreaMm2())
        assertEquals(ccw.areaTenthsM2(), cw.areaTenthsM2())
    }

    @Test
    fun `пустой, одноточечный и двухточечный контуры не падают и дают нулевую площадь`() {
        val empty = contour()
        val single = contour(100L to 100L)
        val segment = contour(0L to 0L, 10L to 0L)

        assertEquals(0L, empty.twiceSignedAreaMm2())
        assertEquals(0L, empty.perimeterTenthsM())
        assertEquals(0L, single.twiceSignedAreaMm2())
        assertEquals(0L, single.perimeterTenthsM())
        assertEquals(0L, segment.twiceSignedAreaMm2())
        // туда и обратно — 20 мм
        assertEquals(20.0, segment.perimeterMm())
    }

    @Test
    fun `площадь, периметр и обход совпадают с ожидаемыми на реальных чертежах`() {
        for (fixture in MeasureFixtures.all) {
            val drawing = decodeFixture(fixture).toDrawing()
            assertEquals(fixture.contours.size, drawing.contours.size, "комната ${fixture.roomId}")

            drawing.contours.zip(fixture.contours).forEachIndexed { i, (c, expected) ->
                val where = "комната ${fixture.roomId}, контур $i"
                assertEquals(expected.areaTenthsM2, c.areaTenthsM2(), "$where, площадь")
                assertEquals(expected.perimeterTenthsM, c.perimeterTenthsM(), "$where, периметр")
                assertEquals(expected.orientationSign, c.twiceSignedAreaMm2().sign(), "$where, обход")
            }
        }
    }

    @Test
    fun `на текущих данных расчёт совпадает с сохранёнными площадью и периметром`() {
        for (fixture in MeasureFixtures.byTag(FixtureTag.RANDOM)) {
            fixture.contours.forEachIndexed { i, e ->
                val where = "комната ${fixture.roomId}, контур $i"
                assertEquals(e.storedAreaTenthsM2, e.areaTenthsM2, "$where, площадь")
                assertEquals(e.storedPerimeterTenthsM, e.perimeterTenthsM, "$where, периметр")
            }
        }
    }

    @Test
    fun `на границе половины новое правило даёт ровно на 0,1 м² больше старого web`() {
        val boundary = MeasureFixtures.byTag(FixtureTag.BOUNDARY)
        assertTrue(boundary.isNotEmpty())
        for (fixture in boundary) {
            val e = fixture.contours.single()
            assertEquals(e.storedAreaTenthsM2 + 1, e.areaTenthsM2, "комната ${fixture.roomId}")
        }
    }

    @Test
    fun `нормализованный обход web в доменных координатах — по часовой`() {
        for (fixture in MeasureFixtures.byTag(FixtureTag.RANDOM)) {
            val c = decodeFixture(fixture).toDrawing().contours.single()
            assertTrue(c.twiceSignedAreaMm2() < 0, "комната ${fixture.roomId}")
        }
    }

    private fun Long.sign(): Int = compareTo(0L)
}
