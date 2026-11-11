package pro.smartprorab.drawing.geometry

import pro.smartprorab.drawing.fixtures.MeasureFixtures
import pro.smartprorab.drawing.fixtures.decodeFixture
import pro.smartprorab.drawing.legacy.toDrawing
import pro.smartprorab.drawing.model.Contour
import pro.smartprorab.drawing.model.Point
import kotlin.math.abs
import kotlin.math.max
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue

/** Построение по сторонам и углам (drawing-core.md, 5). */
class BuildTest {

    private fun contour(vararg mm: Pair<Int, Int>) = Contour(mm.map { (x, y) -> Point(x, y) })
    private fun rows(vararg r: Pair<Double, Int>) = r.map { (a, l) -> SideInput(a, l) }

    private val rect = contour(0 to 0, 3_230 to 0, 3_230 to -5_600, 0 to -5_600)

    @Test
    fun `DC-BLD-01 прямоугольник 3230 × 5600 · ADJUST — невязок нет`() {
        val r = buildContour(rows(90.0 to 3_230, 90.0 to 5_600, 90.0 to 3_230, 90.0 to 5_600), BuildMode.ADJUST)

        assertEquals(rect, r.contour)
        assertEquals(0.0, r.angularMisclosureDeg)
        assertTrue(r.linearMisclosureMm < 1e-6, "${r.linearMisclosureMm}")
        assertEquals(181L, r.contour.areaTenthsM2())
        assertTrue(r.contour.twiceSignedAreaMm2() < 0, "обход по часовой")
        assertEquals(emptySet(), r.contour.validate())
    }

    @Test
    fun `DC-BLD-02 AUTO_CLOSE — последняя сторона и два угла вычислены`() {
        // угол первой строки не используется — даже недопустимый
        val r = buildContour(rows(999.0 to 3_230, 90.0 to 5_600, 90.0 to 3_230), BuildMode.AUTO_CLOSE)

        assertEquals(rect, r.contour)
        val last = r.contour.sidesAndAngles().last()
        assertEquals("DA", last.label)
        assertEquals(5_600, last.lengthMm)
        assertEquals(List(4) { 90.0 }, r.contour.interiorAnglesDeg())
        assertEquals(0.0, r.linearMisclosureMm)
    }

    @Test
    fun `DC-BLD-02 AUTO_CLOSE — треугольник 3-4-5`() {
        val r = buildContour(rows(0.0 to 3_000, 90.0 to 4_000), BuildMode.AUTO_CLOSE)

        assertEquals(contour(0 to 0, 3_000 to 0, 3_000 to -4_000), r.contour)
        assertEquals(5_000, r.contour.sideLengthsMm().last())
        assertEquals(listOf(53.1, 90.0, 36.9), r.contour.interiorAnglesDeg())
    }

    @Test
    fun `DC-BLD-03 сумма углов больше на 3° — исправлены поровну`() {
        val r = buildContour(rows(90.75 to 3_230, 90.75 to 5_600, 90.75 to 3_230, 90.75 to 5_600), BuildMode.ADJUST)

        assertEquals(3.0, r.angularMisclosureDeg, 1e-9)
        assertEquals(rect, r.contour)
        assertTrue(r.linearMisclosureMm < 1e-6, "${r.linearMisclosureMm}")
    }

    @Test
    fun `DC-BLD-04 длина с ошибкой 40 мм — Боудич`() {
        // CD длиннее AB на 40 мм: конец хода в (−40, 0)
        val r = buildContour(rows(90.0 to 3_230, 90.0 to 5_600, 90.0 to 3_270, 90.0 to 5_600), BuildMode.ADJUST)

        assertEquals(40.0, r.linearMisclosureMm, 1e-6)
        assertEquals(40.0 / 17_700, r.linearMisclosureRatio, 1e-12)
        // сдвиг +40·L/P: 7,3 → 3237; 19,95 → 3250; 27,3 → −13
        assertEquals(contour(0 to 0, 3_237 to 0, 3_250 to -5_600, -13 to -5_600), r.contour)
    }

    @Test
    fun `DC-BLD-05 Г-образное помещение с углом 270°`() {
        val r = buildContour(
            rows(90.0 to 4_000, 90.0 to 2_000, 90.0 to 2_000, 270.0 to 2_000, 90.0 to 2_000, 90.0 to 4_000),
            BuildMode.ADJUST,
        )

        assertEquals(
            contour(0 to 0, 4_000 to 0, 4_000 to -2_000, 2_000 to -2_000, 2_000 to -4_000, 0 to -4_000),
            r.contour,
        )
        assertEquals(0.0, r.angularMisclosureDeg)
        assertEquals(emptySet(), r.contour.validate())
        assertEquals(120L, r.contour.areaTenthsM2())
    }

    @Test
    fun `начало и направление первой стороны`() {
        val r = buildContour(
            rows(90.0 to 3_230, 90.0 to 5_600, 90.0 to 3_230, 90.0 to 5_600),
            BuildMode.ADJUST,
            start = Point(1_000, 2_000),
            startDirectionDeg = 90.0,
        )

        assertEquals(contour(1_000 to 2_000, 1_000 to 5_230, 6_600 to 5_230, 6_600 to 2_000), r.contour)
        assertEquals(90.0, r.contour.sideDirectionDeg(0), 1e-9)
    }

    @Test
    fun `обход против часовой — reversedWalk даёт то же помещение по часовой`() {
        // Г-образное, снятое против часовой от A: A→F→E→D→C→B
        val l = contour(0 to 0, 4_000 to 0, 4_000 to -2_000, 2_000 to -2_000, 2_000 to -4_000, 0 to -4_000)
        val ccw = Contour(listOf(l.points[0]) + l.points.drop(1).reversed())
        val ccwRows = ccw.lengthsAndAnglesInWalkOrder()

        val reversed = ccwRows.reversedWalk()

        assertEquals(
            rows(90.0 to 4_000, 90.0 to 2_000, 90.0 to 2_000, 270.0 to 2_000, 90.0 to 2_000, 90.0 to 4_000),
            reversed,
        )
    }

    @Test
    fun `reversedWalk дважды — исходная таблица`() {
        val t = rows(80.0 to 1, 81.0 to 2, 82.0 to 3, 83.0 to 4, 84.0 to 5)

        assertEquals(t, t.reversedWalk().reversedWalk())
    }

    @Test
    fun `ошибки ввода`() {
        val ok = rows(90.0 to 3_230, 90.0 to 5_600, 90.0 to 3_230, 90.0 to 5_600)
        assertFailsWith<IllegalArgumentException> { buildContour(ok.take(2), BuildMode.ADJUST) }
        assertFailsWith<IllegalArgumentException> { buildContour(ok.take(1), BuildMode.AUTO_CLOSE) }
        assertFailsWith<IllegalArgumentException> {
            buildContour(rows(90.0 to 0, 90.0 to 5_600, 90.0 to 3_230), BuildMode.ADJUST)
        }
        assertFailsWith<IllegalArgumentException> {
            buildContour(rows(90.0 to 3_230, 360.0 to 5_600, 90.0 to 3_230), BuildMode.ADJUST)
        }
    }

    /**
     * DC-BLD-06: таблица по контуру → ADJUST от той же вершины и направления → тот же контур.
     * Углы в таблице до 0,1°, длины до мм: на реальных чертежах расхождение до 3 мм
     * (151 из 163 контуров — в пределах 1 мм).
     */
    @Test
    fun `DC-BLD-06 таблица → построение → тот же контур на фикстурах`() {
        val savable = MeasureFixtures.all.flatMap { f ->
            decodeFixture(f).toDrawing().contours.zip(f.contours)
                .filter { (_, e) -> e.savable }
                .map { (c, _) -> f.roomId to c.normalized() }
        }
        assertTrue(savable.size > 150)

        savable.forEach { (roomId, c) ->
            val r = buildContour(
                c.sidesAndAngles().map { it.toInput() },
                BuildMode.ADJUST,
                start = c.points.first(),
                startDirectionDeg = c.sideDirectionDeg(0),
            )
            assertEquals(c.points.size, r.contour.points.size, "room $roomId")
            val err = c.points.zip(r.contour.points).maxOf { (a, b) -> max(abs(a.x - b.x), abs(a.y - b.y)) }
            assertTrue(err <= 3, "room $roomId: расхождение $err мм")
        }
    }

    /** Таблица в порядке обхода как есть — то, что записал бы монтажник, идя против часовой. */
    private fun Contour.lengthsAndAnglesInWalkOrder(): List<SideInput> {
        val lengths = sideLengthsMm()
        // для обхода против часовой interiorAnglesDeg сам определяет направление и даёт внутренние углы
        return interiorAnglesDeg().zip(lengths) { a, l -> SideInput(a, l) }
    }
}
