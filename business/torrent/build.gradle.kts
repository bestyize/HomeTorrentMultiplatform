import org.gradle.internal.impldep.org.junit.experimental.categories.Categories.CategoryFilter.exclude

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.kotlinSerialization)
    alias(libs.plugins.jetbrainsCompose)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.room)
    alias(libs.plugins.ksp)
}

kotlin {
    jvm("desktop")

    listOf(
        iosX64(), iosArm64(), iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "torrent"
            isStatic = true
        }
    }

    sourceSets {

        commonMain.dependencies {
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material3)
            implementation(compose.ui)
            implementation(compose.components.resources)
            implementation(compose.components.uiToolingPreview)
            implementation(compose.components.resources)
            implementation(libs.kotlinSerialization)
            implementation(libs.ktor)
            implementation(libs.ktorCio)
            implementation(project(":framework:widget"))
            implementation(project(":framework:network"))
            implementation(project(":framework:perference"))
            implementation(project(":framework:resources"))
            implementation(project(":framework:utils"))

            implementation(libs.voyagerNavigator)
            implementation(libs.voyagerScreenModel)
            implementation(libs.voyagerLifecycleKmp)
            implementation(libs.voyagerTransitions)

            implementation(libs.datastore)
            implementation(libs.datastorePerference)
            implementation(libs.viewmodel)
            implementation(libs.lifecycleCommon)

            implementation(libs.roomRuntime)
            implementation(libs.roomCompiler)
            implementation(libs.roomPaging)
            implementation(libs.roomCommon)
            implementation(libs.sqliteBundle)
        }
    }


}


room {
    schemaDirectory("$projectDir/schemas")
}