plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.kotlinSerialization)
}

kotlin {
    jvm("desktop")

    listOf(
        iosX64(), iosArm64(), iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "network"
            isStatic = true
        }
    }

    sourceSets {
        val desktopMain by getting

        commonMain.dependencies {
            api(libs.ktor)
            api(libs.ktorCio)
            implementation(project(":framework:perference"))
        }

        iosMain.dependencies {
            implementation(libs.ktorDarwin)
        }

        desktopMain.dependencies {
            implementation(libs.ktorOkHttp)
        }
    }
}