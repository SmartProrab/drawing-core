package pro.smartprorab.drawing.edit

import pro.smartprorab.drawing.model.Contour
import pro.smartprorab.drawing.model.DrawingLimits
import pro.smartprorab.drawing.model.Point
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNull
import kotlin.test.assertTrue

/** Правка контура и привязки (drawing-core.md, 6). */
class EditTest {

    private fun contour(vararg mm: Pair<Int, Int>) = Contour(mm.map { (x, y) -> Point(x, y) })

    /** Квадрат 3 × 3 м по часовой: A(0,0) B(0,3000) C(3000,3000) D(3000,0). Двигаем C (индекс 2). */
    private val square = contour(0 to 0, 0 to 3_000, 3_000 to 3_000, 3_000 to 0)

    private fun MoveResult.point(i: Int) = contour.points[i]

    // --- DC-MOV-01: сетка ---

    @Test
    fun `DC-MOV-01 сетка 10 мм — координаты кратны шагу`() {
        // обе соседние стороны далеко от осей (8° и 14°) — работает только сетка
        val r = square.moveVertex(2, 2_347.4, 2_652.6)

        assertEquals(Point(2_350, 2_650), r.point(2))
        assertEquals(setOf(Snap.GRID), r.snaps)
    }

    @Test
    fun `DC-MOV-01 сетка на отрицательных координатах`() {
        val r = square.moveVertex(2, -14.9, -15.1, SnapOptions(orthoToleranceDeg = 0.0))

        assertEquals(Point(-10, -20), r.point(2))
        assertEquals(setOf(Snap.GRID), r.snaps)
    }

    @Test
    fun `DC-MOV-01 другой шаг сетки и без сетки`() {
        val noOrtho = 0.0
        assertEquals(Point(2_300, 2_700), square.moveVertex(2, 2_347.4, 2_652.6, SnapOptions(100, noOrtho)).point(2))

        val free = square.moveVertex(2, 2_347.4, 2_652.6, SnapOptions(0, noOrtho))
        assertEquals(Point(2_347, 2_653), free.point(2))
        assertEquals(emptySet(), free.snaps)
    }

    @Test
    fun `остальные вершины и тип контура не меняются`() {
        val r = square.moveVertex(2, 2_347.4, 2_652.6)

        assertEquals(square.points.filterIndexed { k, _ -> k != 2 }, r.contour.points.filterIndexed { k, _ -> k != 2 })
        assertEquals(square.type, r.contour.type)
    }

    // --- DC-MOV-02: ортогональность ---

    @Test
    fun `DC-MOV-02 сторона под 2° выравнивается по горизонтали`() {
        // от B(0,3000): dx = 2500, dy = 2500·tg 2° ≈ 87,3
        val r = square.moveVertex(2, 2_500.0, 3_087.3)

        assertEquals(Point(2_500, 3_000), r.point(2))
        assertEquals(setOf(Snap.PREV_HORIZONTAL, Snap.GRID), r.snaps)
    }

    @Test
    fun `DC-MOV-02 сторона под 5° не выравнивается`() {
        // dy = 2500·tg 5° ≈ 218,7
        val r = square.moveVertex(2, 2_500.0, 3_218.7)

        assertEquals(Point(2_500, 3_220), r.point(2))
        assertEquals(setOf(Snap.GRID), r.snaps)
    }

    @Test
    fun `DC-MOV-02 почти вертикальная следующая сторона`() {
        // к D(3000,0): dx = 52,4, dy = 3000 → 1° от вертикали; от B — ровно горизонтально
        val r = square.moveVertex(2, 3_052.4, 3_000.0)

        assertEquals(Point(3_000, 3_000), r.point(2))
        assertEquals(setOf(Snap.PREV_HORIZONTAL, Snap.NEXT_VERTICAL), r.snaps)
    }

    @Test
    fun `DC-MOV-02 угол прямоугольника — x от следующей, y от предыдущей, без сетки`() {
        // от B: 1,1°, к D: 0,8° от вертикали
        val r = square.moveVertex(2, 3_040.3, 3_060.4)

        assertEquals(Point(3_000, 3_000), r.point(2))
        assertEquals(setOf(Snap.PREV_HORIZONTAL, Snap.NEXT_VERTICAL), r.snaps)
    }

    @Test
    fun `DC-MOV-02 ортогональность сильнее сетки — координата соседа не кратна 10`() {
        val legacy = contour(0 to 0, 3 to 3_007, 3_000 to 3_000, 3_000 to 0)

        val r = legacy.moveVertex(2, 2_500.0, 3_050.0)

        assertEquals(3_007, r.point(2).y)
        assertTrue(Snap.PREV_HORIZONTAL in r.snaps)
    }

    @Test
    fun `DC-MOV-02 оба соседа претендуют на одну ось — побеждает меньшее отклонение`() {
        val c = contour(0 to 0, 5_000 to 500, 10_000 to 100, 5_000 to -3_000)

        // от (0,0): 0,46°, к (10000,100): 0,69° — y берётся у предыдущей
        val r = c.moveVertex(1, 5_000.0, 40.0)

        assertEquals(Point(5_000, 0), r.point(1))
        assertEquals(setOf(Snap.PREV_HORIZONTAL, Snap.GRID), r.snaps)
    }

    @Test
    fun `DC-MOV-02 допуск из настроек`() {
        val wide = SnapOptions(orthoToleranceDeg = 6.0)

        assertEquals(Point(2_500, 3_000), square.moveVertex(2, 2_500.0, 3_218.7, wide).point(2))
    }

    @Test
    fun `DC-MOV-02 флаг отключает все привязки`() {
        val r = square.moveVertex(2, 2_500.0, 3_087.3, SnapOptions.OFF)

        assertEquals(Point(2_500, 3_087), r.point(2))
        assertEquals(emptySet(), r.snaps)
    }

    @Test
    fun `точка в соседней вершине — без деления на ноль`() {
        val r = square.moveVertex(2, 0.0, 3_000.0)

        assertEquals(Point(0, 3_000), r.point(2))
    }

    @Test
    fun `нет такой вершины`() {
        assertFailsWith<IllegalArgumentException> { square.moveVertex(4, 0.0, 0.0) }
        assertFailsWith<IllegalArgumentException> { square.moveVertex(-1, 0.0, 0.0) }
    }

    // --- DC-MOV-03: вставка и удаление ---

    @Test
    fun `DC-MOV-03 вставка в середину стороны`() {
        assertEquals(
            contour(0 to 0, 0 to 1_500, 0 to 3_000, 3_000 to 3_000, 3_000 to 0),
            square.insertVertex(0),
        )
    }

    @Test
    fun `DC-MOV-03 вставка в долю стороны и в замыкающую сторону`() {
        assertEquals(Point(0, 750), square.insertVertex(0, 0.25).points[1])
        // сторона DA: новая вершина — последняя
        assertEquals(
            contour(0 to 0, 0 to 3_000, 3_000 to 3_000, 3_000 to 0, 1_500 to 0),
            square.insertVertex(3),
        )
    }

    @Test
    fun `DC-MOV-03 вставка — неверные аргументы и предел вершин`() {
        assertFailsWith<IllegalArgumentException> { square.insertVertex(0, 0.0) }
        assertFailsWith<IllegalArgumentException> { square.insertVertex(0, 1.0) }
        assertFailsWith<IllegalArgumentException> { square.insertVertex(4) }

        val full = Contour(List(DrawingLimits.MAX_POINTS) { Point(it * 10, (it % 2) * 10) })
        assertFailsWith<IllegalArgumentException> { full.insertVertex(0) }
    }

    @Test
    fun `DC-MOV-03 удаление до трёх вершин, дальше — отказ`() {
        val triangle = square.deleteVertex(2)

        assertEquals(contour(0 to 0, 0 to 3_000, 3_000 to 0), triangle)
        assertNull(triangle!!.deleteVertex(0))
        assertFailsWith<IllegalArgumentException> { square.deleteVertex(4) }
    }

    @Test
    fun `сдвиг контура`() {
        assertEquals(
            contour(100 to -50, 100 to 2_950, 3_100 to 2_950, 3_100 to -50),
            square.translate(100, -50),
        )
    }
}
