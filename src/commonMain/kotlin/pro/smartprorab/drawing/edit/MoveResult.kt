package pro.smartprorab.drawing.edit

import pro.smartprorab.drawing.model.Contour

data class MoveResult(
    val contour: Contour,
    val snaps: Set<Snap>
)
