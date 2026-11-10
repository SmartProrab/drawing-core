package pro.smartprorab.drawing.geometry

enum class ContourIssue { TOO_FEW_POINTS, TOO_MANY_POINTS, ZERO_AREA, SELF_INTERSECTION, OUT_OF_BOUNDS }

enum class DrawingIssue { NO_CONTOURS, TOO_MANY_CONTOURS }

/** Проблемы чертежа: общие + по каждому контуру (индекс → проблемы). Пусто — можно сохранять. */
data class DrawingValidation(
    val drawing: Set<DrawingIssue>,
    val contours: Map<Int, Set<ContourIssue>>
) {
    val isValid: Boolean get() = drawing.isEmpty() && contours.isEmpty()
}
