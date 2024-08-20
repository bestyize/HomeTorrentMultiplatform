rootProject.name = "HomeTorrentMultiplatform"
enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

pluginManagement {
    repositories {
        google {
            mavenContent {
                includeGroupAndSubgroups("androidx")
                includeGroupAndSubgroups("com.android")
                includeGroupAndSubgroups("com.google")
            }
        }
        mavenCentral()
        gradlePluginPortal()
        maven("https://plugins.gradle.org/m2/")
    }
}

dependencyResolutionManagement {
    repositories {
        google {
            mavenContent {
                includeGroupAndSubgroups("androidx")
                includeGroupAndSubgroups("com.android")
                includeGroupAndSubgroups("com.google")
            }
        }
        mavenCentral()
        mavenLocal()
        maven("https://plugins.gradle.org/m2/")
    }
}

include(":composeApp")
include(":framework:widget")
include(":framework:network")
include(":framework:perference")
include(":framework:baseapp")
include(":framework:kmmplayer")
include(":framework:kmmimage")
include(":framework:resources")
include(":framework:utils")

include(":business:torrent")
include(":business:account")