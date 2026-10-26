package ru.lubimobile.drawing.convertor

import ru.lubimobile.drawing.domain.Contour
import ru.lubimobile.drawing.domain.Drawing
import ru.lubimobile.drawing.domain.Point
import ru.lubimobile.drawing.dto.Measure
import ru.lubimobile.drawing.dto.RoomMeasure
import ru.lubimobile.drawing.geometry.areaTenthsM2
import ru.lubimobile.drawing.geometry.perimeterTenthsM

fun Measure.toContour(): Contour = Contour(
    points = this.points.map { point -> point.toPoint() },
    diagonals = this.diagonals
)

fun RoomMeasure.toDrawing(): Drawing = Drawing(
    roomId = this.roomId,
    contours = this.measure.map(Measure::toContour),
    ver = this.ver
)

fun Contour.toMeasure(): Measure = Measure(
    points = this.points.map(Point::toDto),
    diagonals = this.diagonals,
    area = this.areaTenthsM2() / 10.0,
    perimeter = this.perimeterTenthsM() / 10.0
)

fun Drawing.toRoomMeasure(): RoomMeasure = RoomMeasure(
    roomId = this.roomId,
    measure = this.contours.map(Contour::toMeasure),
    area = this.contours.sumOf { it.areaTenthsM2() } / 10.0,
    perimeter = this.contours.sumOf { it.perimeterTenthsM() } / 10.0,
    ver = this.ver
)