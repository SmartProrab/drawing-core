package pro.smartprorab.drawing.edit

import pro.smartprorab.drawing.geometry.normalized
import pro.smartprorab.drawing.model.Contour
import pro.smartprorab.drawing.model.Drawing
import pro.smartprorab.drawing.model.Point

/**
 * Зеркальное отражение чертежа по вертикальной оси через середину его габарита (smart_3 measure.md 6.8):
 * `x' = minX + maxX − x` по всем контурам сразу — взаимное расположение контуров и габарит сохраняются.
 * Отражение меняет направление обхода, поэтому каждый контур нормализуется: снова по часовой,
 * первая вершина — образ прежней A. Порядок контуров тот же — уровни у вызывающего остаются по индексам.
 * Площадь и периметр не меняются.
 */
fun Drawing.mirrored(): Drawing {
    val xs = contours.flatMap { c -> c.points.map { it.x } }
    if (xs.isEmpty()) return this
    val axis = xs.min() + xs.max()
    return copy(contours = contours.map { it.reflect(axis) })
}

/** Отражение одного контура по оси через середину его габарита. */
fun Contour.mirrored(): Contour = Drawing(listOf(this)).mirrored().contours.single()

private fun Contour.reflect(axis: Int): Contour =
    copy(points = points.map { Point(axis - it.x, it.y) }).normalized()
