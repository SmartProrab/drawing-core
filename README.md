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
