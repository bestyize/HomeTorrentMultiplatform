import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.kotlinSerialization)
    alias(libs.plugins.jetbrainsCompose)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.room)
    alias(libs.plugins.ksp)
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
            implementation(project(":framework:widget"))
            implementation(project(":framework:network"))
            implementation(project(":framework:perference"))
            implementation(project(":framework:resources"))
            implementation(project(":framework:utils"))
            implementation(project(":framework:kmmdatabase"))

            implementation(libs.voyagerNavigator)
            implementation(libs.voyagerScreenModel)
            implementation(libs.voyagerLifecycleKmp)
            implementation(libs.voyagerTransitions)

            implementation(libs.viewmodel)
            implementation(libs.lifecycleCommon)
        }
    }

    @OptIn(ExperimentalKotlinGradlePluginApi::class)
    compilerOptions {
        freeCompilerArgs.add("-Xexpect-actual-classes")
    }


}

android {
    namespace = "xyz.thewind.torrent"
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


room {
    schemaDirectory("$projectDir/schemas")
}

dependencies {
    add("kspDesktop", libs.roomCompiler)
    add("kspAndroid", libs.roomCompiler)
    add("kspIosSimulatorArm64", libs.roomCompiler)
    add("kspIosX64", libs.roomCompiler)
    add("kspIosArm64", libs.roomCompiler)
}