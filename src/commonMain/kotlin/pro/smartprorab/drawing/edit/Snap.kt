package pro.smartprorab.drawing.edit

/** Сработавшая привязка — клиент рисует по ней направляющую. */
enum class Snap {
    GRID,
    /** Сторона от предыдущей вершины стала горизонтальной (y = prev.y). */
    PREV_HORIZONTAL,
    /** Сторона от предыдущей вершины стала вертикальной (x = prev.x). */
    PREV_VERTICAL,
    NEXT_HORIZONTAL,
    NEXT_VERTICAL,
}