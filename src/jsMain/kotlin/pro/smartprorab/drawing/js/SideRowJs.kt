@file:OptIn(ExperimentalJsExport::class)
@file:JsExport
package pro.smartprorab.drawing.js

/*
 * Фасад для TypeScript (drawing-core.md, 8): только number, string, boolean, массивы.
 * Точка — [x, y] в мм; строка таблицы — [angleDeg, lengthMm].
 */
class SideRowJs(val label: String, val lengthMm: Int, val angleDeg: Double)