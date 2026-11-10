package pro.smartprorab.drawing.geometry

import pro.smartprorab.drawing.model.Contour
import pro.smartprorab.drawing.model.Point
import kotlin.math.PI
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.hypot
import kotlin.math.roundToInt
import kotlin.math.sin

/** Строка ввода: внутренний угол в начальной вершине стороны, градусы, и длина стороны, мм. */
data class SideInput(val angleDeg: Double, val lengthMm: Int)

enum class BuildMode {
    /** Все стороны, кроме последней; последняя сторона и два угла вычисляются. */
    AUTO_CLOSE,
    /** Все стороны и углы; невязка распределяется (угловая поровну, линейная по Боудичу). */
    ADJUST
}

/**
 * Результат построения. Невязки — до исправления; в [BuildMode.AUTO_CLOSE] они нулевые.
 * Порогов нет: числа показывает клиент, решает монтажник.
 */

data class BuildResult(
    val contour: Contour,
    /** Σα − (n − 2)·180°. */
    val angularMisclosureDeg: Double,
    /** Расстояние от конца хода до A по исправленным углам, мм. */
    val linearMisclosureMm: Double,
    /** Линейная невязка / периметр. */
    val linearMisclosureRatio: Double
)

fun SideRow.toInput(): SideInput = SideInput(angleDeg = angleDeg, lengthMm = lengthMm)

/** Направление стороны i (от вершины i к следующей), градусы, против часовой от оси x. */
fun Contour.sideDirectionDeg(i: Int): Double {
    val a = points[i]
    val b = points[(i + 1) % points.size]
    return atan2((b.y - a.y).toDouble(), (b.x - a.x).toDouble()) * 180 / PI
}

/**
 * Таблица, снятая обходом против часовой, → таблица того же помещения по часовой, начиная с A.
 * Строка j′ — сторона n−1−j в обратную сторону; угол — в её новой начальной вершине.
 */
fun List<SideInput>.reversedWalk(): List<SideInput> = indices.map { j ->
    SideInput(
        angleDeg = this[if (j == 0) 0 else size - 1].angleDeg,
        lengthMm = this[size - 1 - j].lengthMm
    )
}

fun buildContour(
    rows: List<SideInput>,
    mode: BuildMode,
    start: Point = Point(0,0),
    startDirectionDeg: Double = 0.0
): BuildResult = when (mode) {
    BuildMode.AUTO_CLOSE -> autoClose(rows, start, startDirectionDeg)
    BuildMode.ADJUST -> adjust(rows, start, startDirectionDeg)
}

private fun autoClose(rows: List<SideInput>, start: Point, dir0: Double): BuildResult {
    require(rows.size >= 2) { "AUTO_CLOSE: нужно хотя бы 2 стороны" }
    rows.forEachIndexed { i, r -> checkRow(r, checkAngle = i > 0) }

    val angles = rows.map { it.angleDeg }
    val vertices = traverse(start, dir0, angles, rows.map { it.lengthMm.toDouble() })
        .dropLast(0) // n−1 сторон дают n вершин: последняя замыкается на A неявно

    return BuildResult(
        contour = Contour(vertices.map { it.rounded() }),
        angularMisclosureDeg = 0.0,
        linearMisclosureMm = 0.0,
        linearMisclosureRatio = 0.0
    )
}

private fun adjust(rows: List<SideInput>, start: Point, dir0: Double): BuildResult {
    val n = rows.size
    require(n >= 3) { "ADJUST: нужно хотя бы 3 стороны" }
    rows.forEach { checkRow(it, checkAngle = true) }

    // 1. угловая невязка - поровну
    val fAlpha = rows.sumOf { it.angleDeg } - (n - 2) * 180.0
    val angles = rows.map { it.angleDeg - fAlpha / n }

    // 2. ход по исправленным углам: n сторон → n + 1 точек, последняя должна совпасть с A
    val lengths = rows.map { it.lengthMm.toDouble() }
    val path = traverse(start, dir0, angles, lengths)
    val end = path.last()
    val fx = end.x - start.x
    val fy = end.y - start.y
    val perimeter = lengths.sum()

    // 3. Боудич: вершина k сдвигается на −f · (L₀ + … + Lₖ₋₁) / P
    var walked = 0.0
    val adjusted = (0 until n).map { k ->
        if (k > 0) walked += lengths[k - 1]
        val share = walked / perimeter
        XY(path[k].x - fx * share, path[k].y - fy * share)
    }

    val linear = hypot(fx, fy)
    return BuildResult(
        contour = Contour(adjusted.map { it.rounded() }),
        angularMisclosureDeg = fAlpha,
        linearMisclosureMm = linear,
        linearMisclosureRatio = linear / perimeter
    )
}

/** Точки хода: A и конец каждой стороны. Угол строки 0 не используется. */
private fun traverse(start: Point, dir0: Double, anglesDeg: List<Double>, lengths: List<Double>): List<XY> {
    var heading = dir0
    var cur = XY(start.x.toDouble(), start.y.toDouble())
    val out = ArrayList<XY>(lengths.size + 1).apply { add(cur) }
    for (k in lengths.indices) {
        if (k > 0) heading -= 180 - anglesDeg[k]
        val rad = heading * PI / 180
        cur = XY(cur.x + lengths[k] * cos(rad), cur.y + lengths[k] * sin(rad))
        out += cur
    }
    return out
}

private fun checkRow(r: SideInput, checkAngle: Boolean) {
    require(r.lengthMm > 0) { "длина стороны должна быть > 0: ${r.lengthMm}" }
    if (checkAngle) require(r.angleDeg > 0 && r.angleDeg < 360) { "угол вне (0; 360): ${r.angleDeg}" }
}

private data class XY(val x: Double, val y: Double) {
    fun rounded() = Point(x.roundToInt(), y.roundToInt())
}