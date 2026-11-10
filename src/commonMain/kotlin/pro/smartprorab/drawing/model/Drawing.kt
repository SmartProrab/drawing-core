package pro.smartprorab.drawing.model

/**
 * Чертёж помещения — один или несколько контуров.
 * Помещение, версия, автор — данные хранения, а не геометрии: их держат сервер и клиенты.
 */
data class Drawing(
    val contours: List<Contour>,
)
