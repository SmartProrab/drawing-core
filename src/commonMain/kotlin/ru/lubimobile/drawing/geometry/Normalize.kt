package ru.lubimobile.drawing.geometry

import ru.lubimobile.drawing.domain.Contour

/**
 * Приведение к виду хранения (drawing-core.md, 4):
 * 1. убрать подряд идущие одинаковые точки (и последнюю, если она равна первой — замыкание неявное);
 * 2. обход против часовой → по часовой, **вершина A остаётся первой**: [A, последняя, …, B].
 */
fun Contour.normalized(): Contour {
    val unique = points.filterIndexed { index, point -> index == 0 || point != points[index - 1] }
        .let { if (it.size > 1 && it.first() == it.last()) it.dropLast(1) else it }
    val clockwise = if (copy(points = unique).twiceSignedAreaMm2() > 0) {
        listOf(unique.first()) + unique.drop(1).reversed()
    } else {
        unique
    }
    return copy(points = clockwise)
}