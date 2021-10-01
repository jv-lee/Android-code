plugins {
    `kotlin-dsl`
}

repositories {
    google()
    mavenCentral()
    maven("https://jitpack.io")
    maven("https://maven.aliyun.com/repository/google")
    maven("https://maven.aliyun.com/repository/jcenter/")
    maven("https://maven.aliyun.com/repository/public")
    maven("https://maven.aliyun.com/repository/central")
    maven("https://maven.aliyun.com/repository/gradle-plugin")
}

dependencies {
    implementation("com.android.tools.build:gradle:${PluginsVersions.ANDROID_PLUGIN}")
    implementation("org.jetbrains.kotlin:kotlin-gradle-plugin:${PluginsVersions.KOTLIN_PLUGIN}")
    implementation("com.google.protobuf:protobuf-gradle-plugin:${PluginsVersions.PROTOBUF_PLUGIN}")
    implementation("com.google.dagger:hilt-android-gradle-plugin:${PluginsVersions.HILT_PLUGIN}")
}

object PluginsVersions {
    const val ANDROID_PLUGIN = "7.0.0"
    const val KOTLIN_PLUGIN = "1.5.10"
    const val PROTOBUF_PLUGIN = "0.8.13"
    const val HILT_PLUGIN = "2.38.1"
}