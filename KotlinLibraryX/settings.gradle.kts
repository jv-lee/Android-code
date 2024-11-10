@file:Suppress("UnstableApiUsage")

includeBuild("build-version")

pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
        maven("https://jitpack.io")
        maven("https://maven.aliyun.com/repository/google")
        maven("https://maven.aliyun.com/repository/jcenter/")
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven("https://jitpack.io")
        maven("https://maven.aliyun.com/repository/google")
        maven("https://maven.aliyun.com/repository/jcenter/")
    }

    versionCatalogs {
        create("libs") {
            from(files("build-version/gradle/libs.versions.toml"))
        }
    }
}

rootProject.name = "KotlinLibraryX"

include(":app")
include(":app-adapter")
include(":app-api")
include(":app-compose")
include(":app-create-vm")
include(":app-dialog")
include(":app-pdfjs")
include(":app-ui")
include(":app-simple")
include(":library")
