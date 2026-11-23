package pro.smartprorab.drawing.edit

import pro.smartprorab.drawing.geometry.areaTenthsM2
import pro.smartprorab.drawing.geometry.perimeterTenthsM
import pro.smartprorab.drawing.geometry.twiceSignedAreaMm2
import pro.smartprorab.drawing.model.Contour
import pro.smartprorab.drawing.model.Drawing
import pro.smartprorab.drawing.model.Point
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

/** Зеркало по вертикальной оси (drawing-core.md 12, smart_3 measure.md 6.8). */
class MirrorTest {

    private fun contour(vararg mm: Pair<Int, Int>) = Contour(mm.map { (x, y) -> Point(x, y) })

    /** Г-образный контур по часовой: «ножка» слева внизу. */
    private val l = contour(0 to 0, 0 to 5_000, 2_000 to 5_000, 2_000 to 2_000, 4_000 to 2_000, 4_000 to 0)

    @Test
    fun `Г-образный контур отражается в пределах своего габарита, обход по часовой, A — образ прежней A`() {
        val m = l.mirrored()

        assertEquals(
            contour(4_000 to 0, 0 to 0, 0 to 2_000, 2_000 to 2_000, 2_000 to 5_000, 4_000 to 5_000),
            m,
        )
        assertTrue(m.twiceSignedAreaMm2() < 0)
        assertEquals(l.areaTenthsM2(), m.areaTenthsM2())
        assertEquals(l.perimeterTenthsM(), m.perimeterTenthsM())
    }

    @Test
    fun `два контура отражаются вместе - взаимное расположение сохраняется`() {
        val low = contour(0 to 0, 0 to 3_200, 1_000 to 3_200, 1_000 to 0)
        val high = contour(1_000 to 0, 1_000 to 3_200, 3_230 to 3_200, 3_230 to 0)

        val m = Drawing(listOf(low, high)).mirrored()

        // общий габарит 0…3230: левая узкая часть стала правой
        assertEquals(contour(3_230 to 0, 3_230 to 3_200, 2_230 to 3_200, 2_230 to 0).normalizedRing(), m.contours[0].normalizedRing())
        assertEquals(contour(2_230 to 0, 2_230 to 3_200, 0 to 3_200, 0 to 0).normalizedRing(), m.contours[1].normalizedRing())
        m.contours.forEach { assertTrue(it.twiceSignedAreaMm2() < 0) }
        assertEquals(m.contours[0].points.first(), Point(3_230, 0))
    }

    @Test
    fun `дважды - исходный чертёж, пустой - без изменений`() {
        assertEquals(l, l.mirrored().mirrored())
        assertEquals(Drawing(emptyList()), Drawing(emptyList()).mirrored())
    }

    /** Множество вершин — сравнение колец без учёта начала. */
    private fun Contour.normalizedRing(): Set<Point> = points.toSet()
}
