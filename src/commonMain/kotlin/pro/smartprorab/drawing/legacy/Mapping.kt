package pro.smartprorab.drawing.legacy

import pro.smartprorab.drawing.geometry.areaTenthsM2
import pro.smartprorab.drawing.geometry.perimeterTenthsM
import pro.smartprorab.drawing.model.Contour
import pro.smartprorab.drawing.model.Drawing
import pro.smartprorab.drawing.model.Point

/*
 * Формат SP ↔ модель. Нужен переносу `rv_measure` / `jrn_measure` (smart_3, docs/migration/measure.md) и тестам.
 * Площадь и периметр из SP не используются — считаются по точкам (kmp-mobile-app #2, #4).
 * Диагонали не переносятся: в данных SP они всегда пустые, в модели их нет.
 */

fun Measure.toContour(): Contour = Contour(points = points.map { it.toPoint() })

fun RoomMeasure.toDrawing(): Drawing = Drawing(contours = measure.map(Measure::toContour))

fun Contour.toMeasure(): Measure = Measure(
    points = points.map(Point::toDto),
    diagonals = emptyList(),
    area = areaTenthsM2() / 10.0,
    perimeter = perimeterTenthsM() / 10.0,
)

/** Помещение и версия — данные хранения SP, в модели их нет. */
fun Drawing.toRoomMeasure(roomId: Int, ver: Long?): RoomMeasure = RoomMeasure(
    roomId = roomId,
    measure = contours.map(Contour::toMeasure),
    area = areaTenthsM2() / 10.0,
    perimeter = perimeterTenthsM() / 10.0,
    ver = ver,
)
