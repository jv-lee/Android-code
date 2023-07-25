import configures.appConfigure

plugins {
    id("org.jetbrains.kotlin.android")
}
appConfigure("com.lee.simple")
dependencies {
    implementation("androidx.appcompat:appcompat:1.4.1")
    implementation("com.google.android.material:material:1.4.+")
    implementation("androidx.constraintlayout:constraintlayout:2.1.3")
}
