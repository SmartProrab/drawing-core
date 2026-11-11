// Модуль из npm-пакета загружается в Node как ES-модуль и считает то же, что тесты.
import * as dc from "../build/dist/js/productionLibrary/drawing-core.mjs"

const rect = [[0, 0], [0, 5600], [3230, 5600], [3230, 0]]
const checks = [
    ["areaM2", dc.areaM2(rect), 18.1],
    ["perimeterM", dc.perimeterM(rect), 17.7],
    ["label", dc.label(26), "AA"],
    ["validate", dc.validate(rect).length, 0],
    ["deleteVertex null", dc.deleteVertex([[0, 0], [0, 1000], [1000, 0]], 0), null],
]
let failed = false
for (const [name, actual, expected] of checks) {
    const ok = actual === expected
    failed ||= !ok
    console.log(`${ok ? "ok  " : "FAIL"} ${name}: ${actual}${ok ? "" : ` (ожидалось ${expected})`}`)
}
try {
    dc.build([[90, 3230], [90, 5600]], "ADJUST", 0, 0, 0)
    console.log("FAIL build: ошибка ввода не брошена"); failed = true
} catch (e) {
    console.log(`ok   build: ошибка ввода → ${e.constructor.name}: ${e.message}`)
}
process.exit(failed ? 1 : 0)
