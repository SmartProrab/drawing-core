package pro.smartprorab.drawing.geometry

import pro.smartprorab.drawing.model.Contour
import pro.smartprorab.drawing.model.Point
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

/** Стороны, углы и подписи вершин — таблица размеров для ввода и показа. */
class MeasurementsTest {

    private fun contour(vararg mm: Pair<Int, Int>) = Contour(mm.map { (x, y) -> Point(x, y) })

    private val rectCw = contour(0 to 0, 0 to 5_600, 3_230 to 5_600, 3_230 to 0)
    private val lShapeCw = contour(0 to 0, 0 to 4_000, 2_000 to 4_000, 2_000 to 2_000, 4_000 to 2_000, 4_000 to 0)

    @Test
    fun `подписи вершин — A…Z, AA, AB, …, ZZ, AAA`() {
        assertEquals("A", vertexLabel(0))
        assertEquals("Z", vertexLabel(25))
        assertEquals("AA", vertexLabel(26))
        assertEquals("AB", vertexLabel(27))
        assertEquals("ZZ", vertexLabel(701))
        assertEquals("AAA", vertexLabel(702))
        assertFailsWith<IllegalArgumentException> { vertexLabel(-1) }
    }

    @Test
    fun `стороны и диагональ`() {
        assertEquals(listOf(5_600, 3_230, 5_600, 3_230), rectCw.sideLengthsMm())
        assertEquals(5_000, contour(0 to 0, 0 to 4_000, 3_000 to 0).diagonalMm(1, 2))
        // √(3230² + 5600²) = 6464,7…
        assertEquals(6_465, rectCw.diagonalMm(0, 2))
    }

    @Test
    fun `углы прямоугольника — 90 при любом обходе`() {
        assertEquals(List(4) { 90.0 }, rectCw.interiorAnglesDeg())
        assertEquals(List(4) { 90.0 }, rectCw.normalized().let { Contour(it.points.reversed()) }.interiorAnglesDeg())
    }

    @Test
    fun `внутренний угол Г-образного помещения — 270`() {
        assertEquals(listOf(90.0, 90.0, 90.0, 270.0, 90.0, 90.0), lShapeCw.interiorAnglesDeg())
        assertEquals(
            listOf(90.0, 90.0, 270.0, 90.0, 90.0, 90.0),
            Contour(lShapeCw.points.reversed()).interiorAnglesDeg(),
        )
    }

    @Test
    fun `углы до десятых`() {
        // треугольник 3-4-5: 90°, 36,87°, 53,13°
        assertEquals(listOf(90.0, 36.9, 53.1), contour(0 to 0, 0 to 4_000, 3_000 to 0).interiorAnglesDeg())
    }

    @Test
    fun `меньше трёх вершин — углов нет`() {
        assertEquals(emptyList(), contour(0 to 0, 1_000 to 0).interiorAnglesDeg())
    }

    @Test
    fun `таблица сторон и углов`() {
        val rows = rectCw.sidesAndAngles()

        assertEquals(listOf("AB", "BC", "CD", "DA"), rows.map { it.label })
        assertEquals(listOf(5_600, 3_230, 5_600, 3_230), rows.map { it.lengthMm })
        assertEquals(List(4) { 90.0 }, rows.map { it.angleDeg })
    }
}
