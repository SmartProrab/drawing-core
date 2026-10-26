package ru.lubimobile.drawing.convertor

import ru.lubimobile.drawing.domain.Contour
import ru.lubimobile.drawing.domain.Drawing
import ru.lubimobile.drawing.domain.Point
import ru.lubimobile.drawing.dto.RoomMeasure
import ru.lubimobile.drawing.fixtures.FixtureTag
import ru.lubimobile.drawing.fixtures.MeasureFixtures
import ru.lubimobile.drawing.fixtures.decodeFixture
import ru.lubimobile.drawing.fixtures.fixtureJson
import kotlin.math.roundToLong
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

/**
 * Маппинг формат хранения ↔ доменная модель: обратимость на реальных чертежах,
 * пересчёт площади и периметра, итог комнаты, сквозные поля.
 */
class MappingTest {

    private fun RoomMeasure.roundTrip(): RoomMeasure = toDrawing().toRoomMeasure()

    @Test
    fun `точки и диагонали переживают цикл туда и обратно на всех чертежах`() {
        for (fixture in MeasureFixtures.all) {
            val original = decodeFixture(fixture)
            val back = original.roundTrip()

            assertEquals(original.roomId, back.roomId)
            assertEquals(original.measure.map { it.points }, back.measure.map { it.points }, "комната ${fixture.roomId}")
            assertEquals(original.measure.map { it.diagonals }, back.measure.map { it.diagonals }, "комната ${fixture.roomId}")
        }
    }

    @Test
    fun `текущие чертежи возвращаются без единого изменения`() {
        for (fixture in MeasureFixtures.byTag(FixtureTag.RANDOM)) {
            val original = decodeFixture(fixture)

            assertEquals(original, original.roundTrip(), "комната ${fixture.roomId}")
        }
    }

    @Test
    fun `сериализация DTO обратима`() {
        for (fixture in MeasureFixtures.all) {
            val dto = decodeFixture(fixture).roundTrip()
            val encoded = fixtureJson.encodeToString(RoomMeasure.serializer(), dto)

            assertEquals(dto, fixtureJson.decodeFromString(RoomMeasure.serializer(), encoded), "комната ${fixture.roomId}")
        }
    }

    @Test
    fun `сохранённые площадь и периметр не используются — пересчитываются из точек`() {
        // 585337: в базе 11,1 м² и 18,0 м при 3,6 м² и 8,7 м по точкам
        val fixture = MeasureFixtures.all.single { it.roomId == 585337 }
        val back = decodeFixture(fixture).roundTrip()

        assertEquals(36L, (back.measure.single().area * 10).roundToLong())
        assertEquals(87L, (back.measure.single().perimeter * 10).roundToLong())
        assertEquals(back.measure.single().area, back.area)
    }

    @Test
    fun `итог комнаты — сумма десятых без накопления погрешности double`() {
        // 0,1 + 0,2 в double — 0,30000000000000004
        val c01 = Contour(listOf(Point(0, 0), Point(100, 0), Point(100, 1_000), Point(0, 1_000)), emptyList())
        val c02 = Contour(listOf(Point(0, 0), Point(200, 0), Point(200, 1_000), Point(0, 1_000)), emptyList())

        val dto = Drawing(roomId = 1, contours = listOf(c01, c02), ver = 7).toRoomMeasure()

        assertEquals(0.1, dto.measure[0].area)
        assertEquals(0.2, dto.measure[1].area)
        assertEquals(0.3, dto.area)
    }

    @Test
    fun `версия и диагонали проходят без изменений`() {
        val contour = Contour(listOf(Point(0, 0), Point(1_000, 0), Point(1_000, 1_000)), listOf("AC"))

        val back = Drawing(roomId = 5, contours = listOf(contour), ver = 42).toRoomMeasure().toDrawing()

        assertEquals(42L, back.ver)
        assertEquals(listOf("AC"), back.contours.single().diagonals)
    }

    @Test
    fun `отсутствующая версия читается как null и остаётся null`() {
        val fixture = MeasureFixtures.byTag(FixtureTag.RANDOM).first()
        val dto = decodeFixture(fixture)

        assertNull(dto.ver)
        assertNull(dto.toDrawing().ver)
        assertNull(dto.roundTrip().ver)
    }

    @Test
    fun `пустые и вырожденные контуры открываются и сохраняются в маппинге как есть`() {
        for (tag in listOf(FixtureTag.EMPTY, FixtureTag.DEGENERATE)) {
            for (fixture in MeasureFixtures.byTag(tag)) {
                val original = decodeFixture(fixture)
                val drawing = original.toDrawing()

                assertEquals(original.measure.size, drawing.contours.size, "комната ${fixture.roomId}")
                assertEquals(original.measure.map { it.points.size }, drawing.contours.map { it.points.size })
            }
        }
    }
}
