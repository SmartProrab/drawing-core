package pro.smartprorab.drawing.edit

/** Привязки при перетаскивании (drawing-core.md, 6). */
data class SnapOptions(
    /** Шаг сетки, мм; 0 — без сетки. */
    val gridMm: Int = 10,
    /** Допуск ортогональности соседней стороны, градусы; 0 — без выравнивания. */
    val orthoToleranceDeg: Double = 3.0,
    /** `false` — свободное перемещение (зажатая клавиша, второй палец). */
    val enabled: Boolean = true,
) {
    companion object {
        val OFF = SnapOptions(enabled = false)
    }
}
