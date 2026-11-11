# drawing-core

Геометрия чертежа помещения — **одна реализация** для backend `smart_3` (jvm), мобильного приложения (android, ios) и web-клиента (js + TypeScript).

- Решение: [ADR-0003](https://github.com/SmartProrab/smart_3/blob/main/docs/adr/0003-drawing-core-shared-geometry.md)
- Спецификация: [`docs/libraries/drawing-core.md`](https://github.com/SmartProrab/smart_3/blob/main/docs/libraries/drawing-core.md) в `smart_3`
- Задачи: SmartProrab/smart_3 #78–#82
- История: перенесено из `kmp-mobile-app/drawing/domain` с коммитами (#12, #13 там)

## Сборка и тесты

```bash
./gradlew jvmTest jsNodeTest testAndroidHostTest     # Linux
./gradlew allTests                                    # + iOS-симулятор, только macOS
./gradlew jsBrowserProductionLibraryDistribution      # npm-пакет в build/dist/js/productionLibrary
```

## Использование из TypeScript

Фасад — `src/jsMain/kotlin/pro/smartprorab/drawing/js/DrawingJs.kt`, типы — `drawing-core.d.mts` в пакете.
Точка — `[x, y]` в мм (x вправо, y вверх), строка таблицы — `[angleDeg, lengthMm]`.

```ts
// только на экране чертежа — динамический импорт
const dc = await import("@smartprorab/drawing-core")

const rect = [[0, 0], [0, 5600], [3230, 5600], [3230, 0]]
dc.areaM2(rect)                     // 18.1
dc.validate(rect)                   // [] — можно сохранять, иначе коды ContourIssue
dc.build([[90, 3230], [90, 5600], [90, 3230]], "AUTO_CLOSE", 0, 0, 0).points
const moved = dc.moveVertex(rect, 2, 3240.3, 5650.4, 10, 3, true)
moved.snaps                         // ["PREV_HORIZONTAL", "NEXT_VERTICAL"] — направляющие
dc.deleteVertex(triangle, 0)        // null — меньше 3 вершин нельзя
```

- Значений по умолчанию нет (сетка 10 мм, допуск 3° задаёт обёртка web-клиента).
- Ошибки ввода (`build` с неверной таблицей, неизвестный режим) — исключение `Error` с текстом.
- `.d.ts` проверяется в CI (`ts-check/check.ts`, `tsc --noEmit`), ES-модуль запускается в Node (`ts-check/smoke.mjs`).

### Размер пакета (v0.1.0, без сжатия и минификации)

| Файл | Размер |
|---|---|
| `drawing-core.mjs` | 50 КБ |
| `kotlin-kotlin-stdlib.mjs` | 112 КБ |
| `drawing-core.d.mts` | 3 КБ |
| всего с source maps | ~310 КБ |

Сборщик web-клиента (Vite) минифицирует и сжимает при сборке; модуль грузится только на экране чертежа.

## Выпуск

Тег `vX.Y.Z` → GitHub Actions публикует в GitHub Packages:

| Потребитель | Артефакт |
|---|---|
| backend, мобилка | Maven `pro.smartprorab:drawing-core:X.Y.Z` (`https://maven.pkg.github.com/SmartProrab/drawing-core`) |
| web-client | npm `@smartprorab/drawing-core@X.Y.Z` (`https://npm.pkg.github.com`) |

Версии — semver: формат и правила расчёта меняются только в мажорной версии.

### Подключение

Gradle (backend, мобилка) — нужен токен с `read:packages`:

```kotlin
repositories {
    maven {
        url = uri("https://maven.pkg.github.com/SmartProrab/drawing-core")
        credentials { username = providers.gradleProperty("gpr.user").get(); password = providers.gradleProperty("gpr.key").get() }
    }
}
dependencies { implementation("pro.smartprorab:drawing-core:X.Y.Z") }
```

npm (web) — `.npmrc`:

```
@smartprorab:registry=https://npm.pkg.github.com
//npm.pkg.github.com/:_authToken=${NPM_TOKEN}
```
