import configures.appConfigure

plugins {
    id(build.BuildPlugin.hilt)
}

appConfigure("com.simple.vm")

dependencies {
    //hilt 依赖注入
    implementation("com.google.dagger:hilt-android:2.38.1")
    kapt("com.google.dagger:hilt-android-compiler:2.38.1")
}