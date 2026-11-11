package pro.smartprorab.drawing.geometry

import pro.smartprorab.drawing.fixtures.FixtureTag
import pro.smartprorab.drawing.fixtures.MeasureFixtures
import pro.smartprorab.drawing.fixtures.decodeFixture
import pro.smartprorab.drawing.legacy.toDrawing
import kotlin.math.abs
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlin.test.fail

/** Нормализация, проверки и углы на реальных чертежах из `rv_measure`. */
class FixtureValidationTest {

    private val contours = MeasureFixtures.all.flatMap { f ->
        decodeFixture(f).toDrawing().contours.zip(f.contours) { c, e -> Triple(f, c, e) }
    }

    @Test
    fun `DC-FIX-02 вырожденные и пустые открываются · validate их не пропускает`() {
        val bad = contours.filter { (_, _, e) -> !e.savable }
        assertTrue(bad.isNotEmpty())

        bad.forEach { (f, c, _) ->
            val issues = c.normalized().validate()
            assertTrue(
                ContourIssue.ZERO_AREA in issues || ContourIssue.TOO_FEW_POINTS in issues,
                "room ${f.roomId}: $issues",
            )
        }
    }

    @Test
    fun `DC-FIX-02 пустые контуры — TOO_FEW_POINTS`() {
        MeasureFixtures.byTag(FixtureTag.EMPTY).forEach { f ->
            val empty = decodeFixture(f).toDrawing().contours.filter { it.points.isEmpty() }
            assertTrue(empty.isNotEmpty(), "room ${f.roomId}")
            empty.forEach { assertEquals(setOf(ContourIssue.TOO_FEW_POINTS), it.validate()) }
        }
    }

    @Test
    fun `DC-NORM-01 на фикстурах — по часовой · площадь и периметр не меняются`() {
        contours.filter { (_, _, e) -> e.savable }.forEach { (f, c, e) ->
            val n = c.normalized()
            assertTrue(n.twiceSignedAreaMm2() < 0, "room ${f.roomId}")
            assertEquals(e.areaTenthsM2, n.areaTenthsM2(), "room ${f.roomId}")
            assertEquals(e.perimeterTenthsM, n.perimeterTenthsM(), "room ${f.roomId}")
            assertEquals(c.points.first(), n.points.first(), "room ${f.roomId}: A должна остаться первой")
        }
    }

    @Test
    fun `сохраняемые контуры проходят проверку`() {
        val failed = contours
            .filter { (_, _, e) -> e.savable }
            .mapNotNull { (f, c, _) -> c.normalized().validate().takeIf { it.isNotEmpty() }?.let { f.roomId to it } }

        if (failed.isNotEmpty()) fail("Не прошли проверку: $failed")
    }

    @Test
    fun `сумма внутренних углов равна 180 × n−2 с точностью округления`() {
        contours.filter { (_, _, e) -> e.savable }.forEach { (f, c, _) ->
            val n = c.normalized()
            val sum = n.interiorAnglesDeg().sum()
            val expected = (n.points.size - 2) * 180.0
            // каждый угол округлён до 0,1 → погрешность не больше 0,05 на вершину
            assertTrue(abs(sum - expected) <= 0.05 * n.points.size + 1e-9, "room ${f.roomId}: $sum ≠ $expected")
        }
    }
}
