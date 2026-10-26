package ru.lubimobile.drawing.fixtures

import kotlinx.serialization.json.Json
import ru.lubimobile.drawing.dto.RoomMeasure

/** Та же конфигурация, что у общего `Json` приложения (`appModules.kt`). */
val fixtureJson = Json {
    ignoreUnknownKeys = true
    explicitNulls = false
}

fun decodeFixture(fixture: MeasureFixture): RoomMeasure =
    fixtureJson.decodeFromString(RoomMeasure.serializer(), fixture.json)
