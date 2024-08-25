import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsCompose)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.kotlinSerialization)
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
            baseName = "ComposeApp"
            isStatic = true
        }
    }

    sourceSets {
        val desktopMain by getting {
            dependencies {
                val skikoAwt =
                    "org.jetbrains.skiko:skiko-awt-runtime-${currentOs()}-${currentArch()}:${libs.versions.skikoVersion.get()}"
                implementation(skikoAwt)
            }
        }

        androidMain.dependencies {
            implementation(compose.preview)
            implementation(libs.androidx.activity.compose)
            implementation(project(":framework:baseapp"))
        }
        commonMain.dependencies {
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material3)
            implementation(compose.ui)
            implementation(compose.components.resources)
            implementation(compose.components.uiToolingPreview)
            implementation(libs.ktor)
            implementation(libs.ktorCio)
            implementation(libs.kotlinSerialization)
            implementation(project(":framework:libcommon"))
            implementation(project(":framework:widget"))
            implementation(project(":framework:network"))
            implementation(project(":framework:perference"))
            implementation(project(":framework:kmmplayer"))
            implementation(project(":framework:kmmimage"))
            implementation(project(":framework:resources"))
            implementation(project(":framework:kmmdatabase"))
            implementation(project(":framework:utils"))
            implementation(project(":business:torrent"))
            implementation(project(":business:account"))
            implementation(project(":business:shortvideo"))

            implementation(libs.voyagerNavigator)
            implementation(libs.voyagerScreenModel)
            implementation(libs.voyagerLifecycleKmp)
            implementation(libs.voyagerTransitions)

            implementation(libs.viewmodel)
            implementation(libs.lifecycleCommon)

        }
        desktopMain.dependencies {
            implementation(compose.desktop.currentOs)
            implementation(libs.androidXCollection)
            implementation(libs.lifecycleDesktop)
            implementation(libs.coroutineSwing)
        }
    }
}

android {
    namespace = "xyz.thewind.torrent"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
    sourceSets["main"].res.srcDirs("src/androidMain/res")
    sourceSets["main"].resources.srcDirs("src/commonMain/resources")

    defaultConfig {
        applicationId = "xyz.thewind.torrent"
        minSdk = libs.versions.android.minSdk.get().toInt()
        targetSdk = libs.versions.android.targetSdk.get().toInt()
        versionCode = 1
        versionName = "1.0"
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
    buildFeatures {
        compose = true
    }
    dependencies {
        debugImplementation(compose.uiTooling)
    }
}

compose.desktop {
    application {
        mainClass = "MainKt"

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "xyz.thewind.torrent"
            packageVersion = "1.0.0"
        }
    }
}

configurations {
    create("cleanedAnnotations")
    implementation {
        exclude(group = "org.jetbrains", module = "annotations")
    }
}


private fun currentArch(): String {
    val arch = System.getProperty("os.arch").lowercase()
    return when {
        arch.startsWith("x86_64") || arch.startsWith("amd64") -> "x64"
        arch.startsWith("aarch64") -> "arm64"
        else -> throw Exception("unsupported arch: $arch")

    }
}


private fun currentOs(): String {
    val os = System.getProperty("os.name").lowercase()
    return when {
        os.startsWith("windows") -> "windows"
        os.startsWith("mac") -> "macos"
        os.startsWith("linux") -> "linux"
        else -> throw Exception("unsupported os: $os")
    }
}