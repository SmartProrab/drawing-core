package pro.smartprorab.drawing.legacy

import pro.smartprorab.drawing.model.Point
import kotlin.math.roundToInt

private const val MM_PER_CM = 10

internal fun cmToMm(cm: Double): Int = (cm * MM_PER_CM).roundToInt()
internal fun mmToCm(mm: Int): Double = mm / MM_PER_CM.toDouble()

/**
 * Точка в формате SP — `[вертикаль, горизонталь]` в см с шагом 0,1 (наследие `[lat, lng]` из Leaflet в web).
 * В модели: x — вправо, y — вверх, целые мм.
 */
internal fun List<Double>.toPoint(): Point {
    require(size == 2) { "Точка должна быть [x, y], получено $this" }
    return Point(x = cmToMm(this[1]), y = cmToMm(this[0]))
}

internal fun Point.toDto(): List<Double> = listOf(mmToCm(y), mmToCm(x))
