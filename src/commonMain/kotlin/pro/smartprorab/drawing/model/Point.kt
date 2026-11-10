package pro.smartprorab.drawing.model

/**
 * Вершина контура, **целые мм**. x — вправо, y — вверх (вид сверху, план).
 * Произведения координат (площадь) считаются в `Long`: `Int` здесь — диапазон, а не точность расчёта.
 */
data class Point(
    val x: Int,
    val y: Int,
)
