import org.gradle.internal.impldep.org.junit.experimental.categories.Categories.CategoryFilter.exclude
import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.kotlinSerialization)
    alias(libs.plugins.jetbrainsCompose)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.androidLibrary)
}

kotlin {
    androidTarget {
        @OptIn(ExperimentalKotlinGradlePluginApi::class) compilerOptions {
            jvmTarget.set(JvmTarget.JVM_11)
        }
    }
    jvm("desktop")

    listOf(
        iosX64(), iosArm64(), iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "account"
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

            implementation(libs.coil)
            implementation(libs.coilCore)
            implementation(libs.coilCompose)
            implementation(libs.coilKtor)
        }
    }


}


android {
    namespace = "xyz.thewind.account"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
    sourceSets["main"].res.srcDirs("src/androidMain/res")
    sourceSets["main"].resources.srcDirs("src/commonMain/resources")

    defaultConfig {
        minSdk = libs.versions.android.minSdk.get().toInt()
    }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}