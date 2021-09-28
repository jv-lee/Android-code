plugins {
    id("commons.android-app")
    id("dagger.hilt.android.plugin")
}

android {
    defaultConfig {
        applicationId =  "com.simple.vm"
    }
}

dependencies {
    //hilt 依赖注入
    implementation("com.google.dagger:hilt-android:2.38.1")
    kapt("com.google.dagger:hilt-android-compiler:2.38.1")
}