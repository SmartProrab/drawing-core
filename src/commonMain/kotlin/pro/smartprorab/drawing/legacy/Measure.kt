package pro.smartprorab.drawing.legacy

import kotlinx.serialization.Serializable

/** Контур в формате SP (`rv_measure.measure[i]`): точки `[вертикаль, горизонталь]` в см. Только для переноса и тестов. */
@Serializable
data class Measure(
    val points: List<List<Double>>,
    val diagonals: List<String>,
    val area: Double,
    val perimeter: Double
)
