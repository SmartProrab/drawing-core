package pro.smartprorab.drawing.geometry

import pro.smartprorab.drawing.model.Contour
import pro.smartprorab.drawing.model.Drawing
import kotlin.math.abs
import kotlin.math.hypot
import kotlin.math.roundToLong

/** 0,1 м² в мм², умноженное на 2: удвоенная площадь → десятые м². */
private const val TWICE_TENTH_M2_IN_MM2 = 200_000L
/** 0,1 м в мм. */
private const val TENTH_M_IN_MM = 100.0

/**
 * Удвоенная знаковая площадь по формуле Гаусса, мм². Точная, без округления.
 * > 0 — обход против часовой стрелки, < 0 — по часовой (x вправо, y вверх).
 * Нормализованный обход — по часовой: у сохранённых контуров значение < 0.
 * Замыкание неявное: последняя вершина соединяется с первой.
 */
fun Contour.twiceSignedAreaMm2(): Long {
    var sum = 0L
    for (i in points.indices) {
        val a = points[i]
        val b = points[(i + 1) % points.size]
        sum += a.x.toLong() * b.y - b.x.toLong() * a.y
    }
    return sum
}

/** Площадь в десятых м², half-up. */
fun Contour.areaTenthsM2(): Long =
    (abs(twiceSignedAreaMm2()) + TWICE_TENTH_M2_IN_MM2 / 2) / TWICE_TENTH_M2_IN_MM2

/** Периметр в мм с учётом замыкающей стороны. */
fun Contour.perimeterMm(): Double {
    if (points.size < 2) return 0.0
    var sum = 0.0
    for (i in points.indices) {
        val a = points[i]
        val b = points[(i + 1) % points.size]
        sum += hypot((b.x - a.x).toDouble(), (b.y - a.y).toDouble())
    }
    return sum
}

/** Периметр в десятых м, half-up. */
fun Contour.perimeterTenthsM(): Long =
    (perimeterMm() / TENTH_M_IN_MM).roundToLong()

/**
 * Площадь чертежа в десятых м² — **сумма округлённых** площадей контуров:
 * так её видит пользователь у каждого контура, и итог не расходится с суммой на экране.
 */
fun Drawing.areaTenthsM2(): Long = contours.sumOf { it.areaTenthsM2() }

/** Периметр чертежа в десятых м — сумма округлённых периметров контуров. */
fun Drawing.perimeterTenthsM(): Long = contours.sumOf { it.perimeterTenthsM() }
