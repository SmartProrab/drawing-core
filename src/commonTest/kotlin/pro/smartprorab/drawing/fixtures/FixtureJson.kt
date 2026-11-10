package pro.smartprorab.drawing.fixtures

import kotlinx.serialization.json.Json
import pro.smartprorab.drawing.legacy.RoomMeasure

/** Та же конфигурация, что у общего `Json` приложения (`appModules.kt`). */
val fixtureJson = Json {
    ignoreUnknownKeys = true
    explicitNulls = false
}

fun decodeFixture(fixture: MeasureFixture): RoomMeasure =
    fixtureJson.decodeFromString(RoomMeasure.serializer(), fixture.json)
