package ru.lubimobile.drawing.dto

import kotlinx.serialization.Serializable

@Serializable
data class Measure(
    val points: List<List<Double>>,
    val diagonals: List<String>,
    val area: Double,
    val perimeter: Double
)
