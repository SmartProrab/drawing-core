/**
 * Проверка .d.ts фасада: так web-клиент (Vue 3 + TypeScript) будет вызывать библиотеку.
 * Компилируется `tsc --noEmit` в CI — ошибки типов ломают сборку.
 */
import * as dc from "@smartprorab/drawing-core"

type Pt = [number, number]
const square: Pt[] = [[0, 0], [0, 3000], [3000, 3000], [3000, 0]]

const area: number = dc.areaM2(square)
const perimeter: number = dc.perimeterM(square)
const diagonal: number = dc.diagonalMm(square, 0, 2)
const angles: number[] = dc.interiorAnglesDeg(square)
const direction: number = dc.sideDirectionDeg(square, 0)
const label: string = dc.label(26)

const issues: string[] = dc.validate(square)
const normalized: number[][] = dc.normalize(square)

const rows: dc.SideRowJs[] = dc.sidesAndAngles(square)
const first: { label: string; lengthMm: number; angleDeg: number } = rows[0]

const built: dc.BuildResultJs = dc.build([[90, 3230], [90, 5600], [90, 3230]], "AUTO_CLOSE", 0, 0, 0)
const builtPoints: number[][] = built.points
const misclosure: number = built.linearMisclosureMm + built.linearMisclosureRatio + built.angularMisclosureDeg
const reversed: number[][] = dc.reversedWalk([[90, 3230], [90, 5600], [90, 3230], [90, 5600]])

const moved: dc.MoveResultJs = dc.moveVertex(square, 2, 3040.3, 3060.4, 10, 3, true)
const movedPoints: number[][] = moved.points
const snaps: string[] = moved.snaps

const inserted: number[][] = dc.insertVertex(square, 0, 0.5)
// Kotlin отдаёт nullable как Nullable<T> = T | null | undefined
const deleted = dc.deleteVertex(square, 2)
const deletedCount: number = deleted == null ? 0 : deleted.length
const shifted: number[][] = dc.translate(square, 100, -50)

// web подключает пакет динамическим импортом только на экране чертежа
async function lazy(): Promise<number> {
    const lib = await import("@smartprorab/drawing-core")
    return lib.areaM2(square)
}

export {
    area, perimeter, diagonal, angles, direction, label, issues, normalized, first,
    builtPoints, misclosure, reversed, movedPoints, snaps, inserted, deletedCount, shifted, lazy,
}
