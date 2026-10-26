package ru.lubimobile.drawing.domain

data class Drawing(
    val roomId: Int,
    val contours: List<Contour>,
    val ver: Long?
)
