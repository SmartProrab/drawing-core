package pro.smartprorab.drawing.js

import pro.smartprorab.drawing.edit.Snap
import pro.smartprorab.drawing.edit.SnapOptions
import pro.smartprorab.drawing.edit.deleteVertex
import pro.smartprorab.drawing.edit.insertVertex
import pro.smartprorab.drawing.edit.moveVertex
import pro.smartprorab.drawing.edit.translate
import pro.smartprorab.drawing.geometry.BuildMode
import pro.smartprorab.drawing.geometry.SideInput
import pro.smartprorab.drawing.geometry.areaTenthsM2
import pro.smartprorab.drawing.geometry.buildContour
import pro.smartprorab.drawing.geometry.diagonalMm
import pro.smartprorab.drawing.geometry.interiorAnglesDeg
import pro.smartprorab.drawing.geometry.normalized
import pro.smartprorab.drawing.geometry.perimeterTenthsM
import pro.smartprorab.drawing.geometry.reversedWalk
import pro.smartprorab.drawing.geometry.sideDirectionDeg
import pro.smartprorab.drawing.geometry.sidesAndAngles
import pro.smartprorab.drawing.geometry.validate
import pro.smartprorab.drawing.geometry.vertexLabel
import pro.smartprorab.drawing.model.Contour
import pro.smartprorab.drawing.model.Point
import kotlin.math.roundToInt

// --- вычисления ---
fun areaM2(points: Array<Array<Int>>): Double = points.toContour().areaTenthsM2().toInt() / 10.0

fun perimeterM(points: Array<Array<Int>>): Double = points.toContour().perimeterTenthsM().toInt() / 10.0

fun diagonalMm(points: Array<Array<Int>>, i: Int, j: Int): Int = points.toContour().diagonalMm(i, j)

fun interiorAnglesDeg(points: Array<Array<Int>>): Array<Double> =
    points.toContour().interiorAnglesDeg().toTypedArray()

fun sidesAndAngles(points: Array<Array<Int>>): Array<SideRowJs> =
    points.toContour().sidesAndAngles().map { SideRowJs(it.label, it.lengthMm, it.angleDeg) }.toTypedArray()

fun sideDirectionDeg(points: Array<Array<Int>>, i: Int): Double = points.toContour().sideDirectionDeg(i)

fun label(i: Int): String = vertexLabel(i)

// --- нормализация и проверки ---
fun normalize(points: Array<Array<Int>>): Array<Array<Int>> = points.toContour().normalized().toJs()

fun validate(points: Array<Array<Int>>): Array<String> =
    points.toContour().validate().map { it.name }.toTypedArray()

// --- построение ---
/** mode — "AUTO_CLOSE" или "ADJUST"; rows — [[angleDeg, lengthMm], …]. */
fun build(
    rows: Array<Array<Double>>,
    mode: String,
    startX: Int,
    startY: Int,
    startDirectionDeg: Double
): BuildResultJs {
    val r = buildContour(rows.toInputs(), BuildMode.valueOf(mode), Point(startX, startY), startDirectionDeg)
    return BuildResultJs(r.contour.toJs(), r.angularMisclosureDeg, r.linearMisclosureMm, r.linearMisclosureRatio)
}

fun reversedWalk(rows: Array<Array<Double>>): Array<Array<Double>> =
    rows.toInputs().reversedWalk().map { arrayOf(it.angleDeg, it.lengthMm.toDouble()) }.toTypedArray()

// --- правка ---
fun moveVertex(
    points: Array<Array<Int>>,
    i: Int,
    x: Double,
    y: Double,
    gridMm: Int,
    orthoToleranceDeg: Double,
    snapEnabled: Boolean
): MoveResultJs {
    val r = points.toContour().moveVertex(i, x, y, SnapOptions(gridMm, orthoToleranceDeg, snapEnabled))
    return MoveResultJs(r.contour.toJs(), r.snaps.map(Snap::name).toTypedArray())
}

fun insertVertex(points: Array<Array<Int>>, side: Int, t: Double): Array<Array<Int>> =
    points.toContour().insertVertex(side, t).toJs()

/** `null` — останется меньше 3 вершин. */
fun deleteVertex(points: Array<Array<Int>>, i: Int): Array<Array<Int>>? =
    points.toContour().deleteVertex(i)?.toJs()

fun translate(points: Array<Array<Int>>, dxMm: Int, dyMm: Int): Array<Array<Int>> =
    points.toContour().translate(dxMm, dyMm).toJs()

// --- преобразования (не экспортируются: private) ---
private fun Array<Array<Int>>.toContour(): Contour  = Contour(map { (x, y) -> Point(x, y) })

private fun Contour.toJs(): Array<Array<Int>> = points.map { arrayOf(it.x, it.y) }.toTypedArray()

private fun Array<Array<Double>>.toInputs(): List<SideInput> =
    map { (angle, length) -> SideInput(angle, length.roundToInt()) }