package pro.smartprorab.drawing.js

import pro.smartprorab.drawing.edit.SnapOptions
import pro.smartprorab.drawing.edit.moveVertex
import pro.smartprorab.drawing.fixtures.MeasureFixtures
import pro.smartprorab.drawing.fixtures.decodeFixture
import pro.smartprorab.drawing.geometry.BuildMode
import pro.smartprorab.drawing.geometry.areaTenthsM2
import pro.smartprorab.drawing.geometry.buildContour
import pro.smartprorab.drawing.geometry.interiorAnglesDeg
import pro.smartprorab.drawing.geometry.normalized
import pro.smartprorab.drawing.geometry.perimeterTenthsM
import pro.smartprorab.drawing.geometry.sideDirectionDeg
import pro.smartprorab.drawing.geometry.sidesAndAngles
import pro.smartprorab.drawing.geometry.toInput
import pro.smartprorab.drawing.geometry.validate
import pro.smartprorab.drawing.legacy.toDrawing
import pro.smartprorab.drawing.model.Contour
import kotlin.test.Test
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNull

/**
 * DC-JS-01: фасад для TypeScript даёт те же числа и коды, что общий код.
 * Векторы — реальные чертежи (MeasureFixtures) и ручные случаи из общих тестов.
 */
class DrawingJsTest {

    private fun pts(vararg mm: Pair<Int, Int>): Array<Array<Int>> = mm.map { (x, y) -> arrayOf(x, y) }.toTypedArray()
    private fun Contour.js(): Array<Array<Int>> = points.map { arrayOf(it.x, it.y) }.toTypedArray()
    private fun Array<Array<Int>>.flat(): List<Int> = flatMap { it.toList() }

    /** Квадрат 3 × 3 м по часовой. */
    private val square = pts(0 to 0, 0 to 3_000, 3_000 to 3_000, 3_000 to 0)

    private val fixtureContours: List<Pair<Int, Contour>> = MeasureFixtures.all.flatMap { f ->
        decodeFixture(f).toDrawing().contours.map { f.roomId to it }
    }

    @Test
    fun `DC-JS-01 площадь, периметр, углы и проверки на фикстурах совпадают с общим кодом`() {
        fixtureContours.forEach { (roomId, c) ->
            val p = c.js()
            assertEquals(c.areaTenthsM2().toInt() / 10.0, areaM2(p), "room $roomId: площадь")
            assertEquals(c.perimeterTenthsM().toInt() / 10.0, perimeterM(p), "room $roomId: периметр")
            assertEquals(c.validate().map { it.name }.toSet(), validate(p).toSet(), "room $roomId: проверки")
            assertEquals(c.normalized().js().flat(), normalize(p).flat(), "room $roomId: нормализация")
            if (c.points.size >= 3) {
                assertEquals(c.interiorAnglesDeg(), interiorAnglesDeg(p).toList(), "room $roomId: углы")
            }
        }
    }

    @Test
    fun `DC-JS-01 таблица сторон и обратная сборка на фикстурах`() {
        fixtureContours
            .map { (id, c) -> id to c.normalized() }
            .filter { (_, c) -> c.validate().isEmpty() }
            .forEach { (roomId, c) ->
                val p = c.js()
                val rows = sidesAndAngles(p)
                val expected = c.sidesAndAngles()
                assertEquals(expected.map { it.label }, rows.map { it.label }, "room $roomId")
                assertEquals(expected.map { it.lengthMm }, rows.map { it.lengthMm }, "room $roomId")
                assertEquals(expected.map { it.angleDeg }, rows.map { it.angleDeg }, "room $roomId")

                val start = c.points.first()
                val dir = c.sideDirectionDeg(0)
                assertEquals(dir, sideDirectionDeg(p, 0), "room $roomId")
                val tableJs = rows.map { arrayOf(it.angleDeg, it.lengthMm.toDouble()) }.toTypedArray()
                val viaJs = build(tableJs, "ADJUST", start.x, start.y, dir)
                val viaCommon = buildContour(expected.map { it.toInput() }, BuildMode.ADJUST, start, dir)
                assertEquals(viaCommon.contour.js().flat(), viaJs.points.flat(), "room $roomId")
                assertEquals(viaCommon.angularMisclosureDeg, viaJs.angularMisclosureDeg, "room $roomId")
                assertEquals(viaCommon.linearMisclosureMm, viaJs.linearMisclosureMm, "room $roomId")
            }
    }

    @Test
    fun `площадь и периметр в м²и м — прямоугольник 3230 × 5600`() {
        val rect = pts(0 to 0, 0 to 5_600, 3_230 to 5_600, 3_230 to 0)

        assertEquals(18.1, areaM2(rect))
        assertEquals(17.7, perimeterM(rect))
        assertEquals(6_465, diagonalMm(rect, 0, 2))
    }

    @Test
    fun `построение AUTO_CLOSE и невязки ADJUST`() {
        val auto = build(arrayOf(arrayOf(0.0, 3_230.0), arrayOf(90.0, 5_600.0), arrayOf(90.0, 3_230.0)), "AUTO_CLOSE", 0, 0, 0.0)
        assertEquals(listOf(0, 0, 3_230, 0, 3_230, -5_600, 0, -5_600), auto.points.flat())

        // CD длиннее на 40 мм (DC-BLD-04)
        val rows = arrayOf(arrayOf(90.0, 3_230.0), arrayOf(90.0, 5_600.0), arrayOf(90.0, 3_270.0), arrayOf(90.0, 5_600.0))
        val adj = build(rows, "ADJUST", 0, 0, 0.0)
        assertEquals(listOf(0, 0, 3_237, 0, 3_250, -5_600, -13, -5_600), adj.points.flat())
        assertEquals(40.0, adj.linearMisclosureMm, 1e-6)
        assertEquals(40.0 / 17_700, adj.linearMisclosureRatio, 1e-12)
    }

    @Test
    fun `длина из поля ввода округляется до мм`() {
        val r = build(arrayOf(arrayOf(0.0, 3_229.6), arrayOf(90.0, 5_600.4), arrayOf(90.0, 3_230.0)), "AUTO_CLOSE", 0, 0, 0.0)

        assertEquals(listOf(0, 0, 3_230, 0, 3_230, -5_600, 0, -5_600), r.points.flat())
    }

    @Test
    fun `неизвестный режим и неверный ввод — ошибка`() {
        val ok = arrayOf(arrayOf(90.0, 3_230.0), arrayOf(90.0, 5_600.0), arrayOf(90.0, 3_230.0))
        assertFailsWith<IllegalArgumentException> { build(ok, "CLOSE", 0, 0, 0.0) }
        assertFailsWith<IllegalArgumentException> { build(ok.copyOf(2).requireNoNulls(), "ADJUST", 0, 0, 0.0) }
    }

    @Test
    fun `reversedWalk — те же строки, что в общем коде`() {
        val t = arrayOf(arrayOf(80.0, 1.0), arrayOf(81.0, 2.0), arrayOf(82.0, 3.0), arrayOf(83.0, 4.0), arrayOf(84.0, 5.0))

        val r = reversedWalk(t)

        assertEquals(listOf(80.0, 5.0, 84.0, 4.0, 83.0, 3.0, 82.0, 2.0, 81.0, 1.0), r.flatMap { it.toList() })
        assertEquals(t.flatMap { it.toList() }, reversedWalk(r).flatMap { it.toList() })
    }

    @Test
    fun `moveVertex — точка и имена привязок как в общем коде`() {
        val cases = listOf(
            Triple(2_347.4, 2_652.6, SnapOptions()),
            Triple(2_500.0, 3_087.3, SnapOptions()),
            Triple(3_040.3, 3_060.4, SnapOptions()),
            Triple(2_500.0, 3_087.3, SnapOptions.OFF),
            Triple(2_347.4, 2_652.6, SnapOptions(100, 0.0)),
        )
        val common = Contour(square.map { pro.smartprorab.drawing.model.Point(it[0], it[1]) })

        cases.forEach { (x, y, s) ->
            val expected = common.moveVertex(2, x, y, s)
            val r = moveVertex(square, 2, x, y, s.gridMm, s.orthoToleranceDeg, s.enabled)
            assertEquals(expected.contour.js().flat(), r.points.flat(), "($x, $y) $s")
            assertEquals(expected.snaps.map { it.name }.toSet(), r.snaps.toSet(), "($x, $y) $s")
        }
    }

    @Test
    fun `угол прямоугольника — направляющие от обоих соседей`() {
        val r = moveVertex(square, 2, 3_040.3, 3_060.4, 10, 3.0, true)

        assertContentEquals(arrayOf(3_000, 3_000), r.points[2])
        assertEquals(setOf("PREV_HORIZONTAL", "NEXT_VERTICAL"), r.snaps.toSet())
    }

    @Test
    fun `вставка, удаление, сдвиг`() {
        assertEquals(listOf(0, 0, 0, 1_500, 0, 3_000, 3_000, 3_000, 3_000, 0), insertVertex(square, 0, 0.5).flat())

        val triangle = deleteVertex(square, 2)
        assertEquals(listOf(0, 0, 0, 3_000, 3_000, 0), triangle!!.flat())
        assertNull(deleteVertex(triangle, 0))

        assertEquals(listOf(100, -50, 100, 2_950, 3_100, 2_950, 3_100, -50), translate(square, 100, -50).flat())
    }

    @Test
    fun `подписи вершин`() {
        assertEquals(listOf("A", "Z", "AA", "AB", "ZZ", "AAA"), listOf(0, 25, 26, 27, 701, 702).map(::label))
    }

    @Test
    fun `вход не меняется — фасад возвращает новые массивы`() {
        val before = square.flat()

        moveVertex(square, 2, 2_347.4, 2_652.6, 10, 3.0, true)
        insertVertex(square, 0, 0.5)
        translate(square, 1, 1)
        normalize(square)

        assertEquals(before, square.flat())
    }
}
