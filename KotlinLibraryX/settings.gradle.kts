rootProject.buildFileName = "build.gradle.kts"

dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
        maven("https://maven.aliyun.com/repository/google/")
        maven("https://maven.aliyun.com/repository/jcenter/")
        maven("https://jitpack.io")
    }
}

include(":app")
include(":app-adapter")
include(":app-api")
include(":app-create-vm")
include(":app-dialog")
include(":app-pdfjs")
include(":app-ui")
include(":app-compose")
include(":library")
