package ru.lubimobile.drawing.convertor

import ru.lubimobile.drawing.domain.Point
import kotlin.math.roundToLong

private const val MM_PER_CM = 10

internal fun cmToMm(cm: Double): Long = (cm * MM_PER_CM).roundToLong()
internal fun mmToCm(mm: Long): Double = mm / MM_PER_CM.toDouble()

/**
 * Точка в формате хранения — `[вертикаль, горизонталь]` (наследие `[lat, lng]` из Leaflet в web).
 * В домене: x — вправо, y — вверх.
 */
internal fun List<Double>.toPoint(): Point {
    require(size == 2) { "Точка должна быть [x, y], получено $this" }
    return Point(xMm = cmToMm(this[1]), yMm = cmToMm(this[0]))
}

internal fun Point.toDto(): List<Double> = listOf(mmToCm(yMm), mmToCm(xMm))