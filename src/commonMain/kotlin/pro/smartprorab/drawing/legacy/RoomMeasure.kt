package pro.smartprorab.drawing.legacy

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/** Чертёж помещения в формате SP (`rv_measure`, `MeasureData` в `public_app`). Только для переноса и тестов. */
@Serializable
data class RoomMeasure(
    @SerialName("room_id")
    val roomId: Int,
    val measure: List<Measure>,
    val area: Double,
    val perimeter: Double,
    val ver: Long?
)
