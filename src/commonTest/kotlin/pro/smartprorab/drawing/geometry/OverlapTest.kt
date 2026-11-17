package pro.smartprorab.drawing.geometry

import pro.smartprorab.drawing.model.Contour
import pro.smartprorab.drawing.model.Drawing
import pro.smartprorab.drawing.model.Point
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

/**
 * Контуры одного чертежа не перекрываются (drawing-core.md 11, smart_3 measure.md 6.7).
 * Перекрытие — общая площадь больше нуля. Касание по стороне или в вершине — не перекрытие.
 */
class OverlapTest {

    private fun contour(vararg mm: Pair<Int, Int>) = Contour(mm.map { (x, y) -> Point(x, y) })

    /** Прямоугольник по часовой от (x0, y0) до (x1, y1). */
    private fun rect(x0: Int, y0: Int, x1: Int, y1: Int) = contour(x0 to y0, x0 to y1, x1 to y1, x1 to y0)

    private val room = rect(0, 0, 4_000, 3_000)

    // --- DC-OVL-01: касание — не перекрытие ---

    @Test
    fun `DC-OVL-01 общая сторона целиком — переход уровня`() {
        val low = rect(0, 0, 3_230, 3_200)
        val high = rect(0, 3_200, 3_230, 5_600)

        assertFalse(low.overlaps(high))
        assertFalse(high.overlaps(low))
        assertEquals(DrawingValidation(emptySet(), emptyMap()), Drawing(listOf(low, high)).validate())
    }

    @Test
    fun `DC-OVL-01 общая часть стороны`() {
        assertFalse(rect(0, 0, 4_000, 2_000).overlaps(rect(1_000, 2_000, 3_000, 3_000)))
    }

    @Test
    fun `DC-OVL-01 касание в вершине`() {
        assertFalse(rect(0, 0, 1_000, 1_000).overlaps(rect(1_000, 1_000, 2_000, 2_000)))
    }

    @Test
    fun `DC-OVL-01 половинки прямоугольника по диагонали`() {
        val left = contour(0 to 0, 0 to 5_600, 3_230 to 5_600)
        val right = contour(0 to 0, 3_230 to 5_600, 3_230 to 0)

        assertFalse(left.overlaps(right))
    }

    @Test
    fun `DC-OVL-01 квадрат во внутреннем углу Г-образного контура`() {
        val l = contour(0 to 0, 0 to 4_000, 2_000 to 4_000, 2_000 to 2_000, 4_000 to 2_000, 4_000 to 0)

        assertFalse(l.overlaps(rect(2_000, 2_000, 4_000, 4_000)))
    }

    @Test
    fun `DC-OVL-01 далеко друг от друга`() {
        assertFalse(room.overlaps(rect(10_000, 10_000, 11_000, 11_000)))
    }

    // --- DC-OVL-02: перекрытие ---

    @Test
    fun `DC-OVL-02 один внутри другого без общих сторон`() {
        assertTrue(room.overlaps(rect(1_000, 1_000, 2_000, 2_000)))
        assertTrue(rect(1_000, 1_000, 2_000, 2_000).overlaps(room))
    }

    @Test
    fun `DC-OVL-02 внутри и касается стороной изнутри`() {
        assertTrue(room.overlaps(rect(0, 0, 1_000, 1_000)))
    }

    @Test
    fun `DC-OVL-02 частичное наложение — стороны пересекаются`() {
        assertTrue(rect(0, 0, 2_000, 2_000).overlaps(rect(1_000, 1_000, 3_000, 3_000)))
    }

    @Test
    fun `DC-OVL-02 одинаковые контуры`() {
        assertTrue(room.overlaps(room))
        assertTrue(room.overlaps(Contour(room.points.reversed())))
    }

    @Test
    fun `DC-OVL-02 полоса через контур — вершины снаружи`() {
        // крест: ни одна вершина одного не лежит внутри другого
        assertTrue(rect(0, 1_000, 4_000, 2_000).overlaps(rect(1_000, 0, 2_000, 3_000)))
    }

    @Test
    fun `DC-OVL-02 в чертеже — ошибка и пара индексов`() {
        val drawing = Drawing(
            listOf(
                rect(0, 0, 2_000, 2_000),       // 0
                rect(2_000, 0, 4_000, 2_000),   // 1 — касается 0
                rect(1_000, 1_000, 1_500, 1_500), // 2 — внутри 0
            )
        )

        val v = drawing.validate()

        assertEquals(setOf(DrawingIssue.CONTOURS_OVERLAP), v.drawing)
        assertEquals(listOf(0 to 2), v.overlaps)
        assertFalse(v.isValid)
    }

    @Test
    fun `DC-OVL-02 неисправный контур в проверку перекрытия не идёт`() {
        val broken = contour(0 to 0, 1_000 to 1_000) // мало вершин
        val v = Drawing(listOf(room, broken)).validate()

        assertEquals(emptySet(), v.drawing)
        assertEquals(emptyList(), v.overlaps)
        assertEquals(mapOf(1 to setOf(ContourIssue.TOO_FEW_POINTS)), v.contours)
    }
}
