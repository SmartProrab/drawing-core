package ru.lubimobile.drawing.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RoomMeasure(
    @SerialName("room_id")
    val roomId: Int,
    val measure: List<Measure>,
    val area: Double,
    val perimeter: Double,
    val ver: Long?
)
