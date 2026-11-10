package pro.smartprorab.drawing.geometry

import pro.smartprorab.drawing.model.Contour
import kotlin.math.PI
import kotlin.math.atan2
import kotlin.math.hypot
import kotlin.math.roundToInt

/** Строка таблицы размеров: сторона от вершины i к следующей, угол — внутренний, в её начальной вершине. */
data class SideRow(val label: String, val lengthMM: Int, val angleDeg: Double)

fun vertexLabel(index: Int): String {
    require(index >= 0)
    var n = index + 1
    val sb = StringBuilder()
    while (n > 0) {
        val r = (n - 1) % 26
        sb.append('A' + r)
        n = (n - 1) / 26
    }
    return sb.reverse().toString()
}

fun Contour.diagonalMm(i: Int, j: Int): Int =
    hypot((points[j].x - points[i].x).toDouble(), (points[j].y - points[i].y).toDouble()).roundToInt()

fun Contour.sideLengthsMm(): List<Int> = points.indices.map { diagonalMm(it, (it + 1) % points.size) }

/**
 * Внутренние углы в градусах, до десятых. 0 < α < 360: больше 180 — «выступ» внутрь помещения.
 * Поворот на вершине считается со знаком; для обхода по часовой выпуклый угол — поворот вправо.
 */
fun Contour.interiorAnglesDeg(): List<Double> {
    val n = points.size
    if (n < 3) return emptyList()
    val clockwise = twiceSignedAreaMm2() < 0
    return points.indices.map { i ->
        val prev = points[(i - 1 + n) % n]
        val cur = points[i]
        val next = points[(i + 1) % n]
        val inDir = atan2((cur.y - prev.y).toDouble(), (cur.x - prev.x).toDouble())
        val outDir = atan2((next.y - cur.y).toDouble(), (next.x - cur.x).toDouble())
        var turn = (outDir - inDir) * 180 / PI
        while (turn <= -180) turn += 360
        while (turn > 180) turn -= 360
        val angle = if (clockwise) 180 + turn else 180 - turn
        (angle * 10).roundToInt() / 10.0
    }
}

fun Contour.sideAndAngles(): List<SideRow> {
    val lengths = sideLengthsMm()
    val angles = interiorAnglesDeg()
    return points.indices.map { i ->
        SideRow(
            label = vertexLabel(i) + vertexLabel((i + 1) % points.size),
            lengthMM = lengths[i],
            angleDeg = angles.getOrElse(i) { 0.0 }
        )
    }
}