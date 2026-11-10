import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.dsl.KotlinVersion

/*
 * Геометрия чертежа помещения: одна реализация для backend (jvm), мобилки (android, ios) и web (js).
 * Спецификация: smart_3/docs/libraries/drawing-core.md, ADR-0003.
 */
plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidMultiplatformLibrary)
    alias(libs.plugins.kotlinPluginSerialization)
    `maven-publish`
}

group = "pro.smartprorab"
// Версия приходит из тега выпуска: ./gradlew publish -PlibVersion=1.2.3
version = providers.gradleProperty("libVersion").getOrElse("0.0.0-SNAPSHOT")

kotlin {
    compilerOptions {
        // Потребители на Kotlin 2.3 (backend smart_3) должны читать библиотеку
        apiVersion.set(KotlinVersion.KOTLIN_2_3)
        languageVersion.set(KotlinVersion.KOTLIN_2_3)
    }

    jvm {
        compilerOptions { jvmTarget.set(JvmTarget.JVM_21) }
    }

    android {
        namespace = "pro.smartprorab.drawing"
        compileSdk = libs.versions.android.compileSdk.get().toInt()
        minSdk = libs.versions.android.minSdk.get().toInt()
        compilerOptions { jvmTarget.set(JvmTarget.JVM_21) }
        withHostTest {}
    }

    iosArm64()
    iosSimulatorArm64()

    js(IR) {
        outputModuleName.set("drawing-core")
        // browser — для сборки пакета (ES-модуль + .d.ts), тесты — в Node.js: без браузера в CI
        browser {
            testTask { enabled = false }
        }
        nodejs()
        useEsModules()
        binaries.library()
        generateTypeScriptDefinitions()
    }

    sourceSets {
        commonMain.dependencies {
            implementation(libs.kotlinx.serialization.json)
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }
    }
}

publishing {
    repositories {
        maven {
            name = "GitHubPackages"
            url = uri("https://maven.pkg.github.com/SmartProrab/drawing-core")
            credentials {
                username = System.getenv("GITHUB_ACTOR")
                password = System.getenv("GITHUB_TOKEN")
            }
        }
    }
}
