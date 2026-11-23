@file:OptIn(ExperimentalJsExport::class)
@file:JsExport
package pro.smartprorab.drawing.js

import pro.smartprorab.drawing.edit.Snap
import pro.smartprorab.drawing.edit.SnapOptions
import pro.smartprorab.drawing.edit.deleteVertex
import pro.smartprorab.drawing.edit.insertVertex
import pro.smartprorab.drawing.edit.moveVertex
import pro.smartprorab.drawing.edit.mirrored
import pro.smartprorab.drawing.edit.split
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
import pro.smartprorab.drawing.model.Drawing
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

// --- 0.2.0: несколько уровней ---

/** Пары индексов перекрывающихся контуров [[i, j], …]; неисправные контуры не проверяются. */
fun overlappingContours(contours: Array<Array<Array<Int>>>): Array<Array<Int>> =
    Drawing(contours.map { it.toContour() }).validate().overlaps
        .map { (i, j) -> arrayOf(i, j) }.toTypedArray()

fun levelTransitionMm(contours: Array<Array<Array<Int>>>, levels: Array<Int>): Int =
    pro.smartprorab.drawing.geometry.levelTransitionMm(contours.map { it.toContour() }, levels.toList())

/** Две части [[…], […]] или null — линия не делит контур. */
fun split(points: Array<Array<Int>>, cut: Array<Array<Int>>): Array<Array<Array<Int>>>? =
    points.toContour().split(cut.map { (x, y) -> Point(x, y) })?.let { (a, b) -> arrayOf(a.toJs(), b.toJs()) }

// --- 0.3.0: зеркало ---

/** Контуры, отражённые вместе по вертикальной оси через середину общего габарита; порядок тот же, обход по часовой. */
fun mirror(contours: Array<Array<Array<Int>>>): Array<Array<Array<Int>>> =
    Drawing(contours.map { it.toContour() }).mirrored().contours.map { it.toJs() }.toTypedArray()
