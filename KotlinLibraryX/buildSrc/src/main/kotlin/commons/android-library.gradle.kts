package commons

import BuildAndroidConfig
import dependencies.AnnotationProcessorsDependencies
import dependencies.Dependencies

plugins {
    id("com.android.library")
    id("kotlin-android")
    id("kotlin-kapt")
}

android {
    compileSdk = BuildAndroidConfig.COMPILE_SDK

    defaultConfig {
        minSdk = BuildAndroidConfig.MIN_SDK
        targetSdk = BuildAndroidConfig.TARGET_SDK
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_1_8.toString()
    }

    buildFeatures {
        dataBinding = true
        viewBinding = true
    }

}

dependencies {
    kapt(AnnotationProcessorsDependencies.ANNOTATION)
    kapt(AnnotationProcessorsDependencies.ROOM)
    kapt(AnnotationProcessorsDependencies.GLIDE)
    kapt(AnnotationProcessorsDependencies.AUTO_SERVICE)

    api(Dependencies.KOTLIN_CORE)

    api(Dependencies.COROUTINES)
    api(Dependencies.COROUTINES_ANDROID)

    api(Dependencies.LIFECYCLE_RUNTIME)
    api(Dependencies.LIFECYCLE_LIVEDATA)
    api(Dependencies.LIFECYCLE_VIEWMODEL)

    api(Dependencies.ACTIVITY)
    api(Dependencies.FRAGMENT)

    api(Dependencies.MULTIDEX)

    api(Dependencies.APPCOMPAT)
    api(Dependencies.SUPPORT)
    api(Dependencies.RECYCLERVIEW)
    api(Dependencies.CONSTRAINT)
    api(Dependencies.VIEWPAGER2)
    api(Dependencies.MATERIAL)

    api(Dependencies.NAVIGATION_FRAGMENT)
    api(Dependencies.NAVIGATION_UI)

    api(Dependencies.ROOM)
    api(Dependencies.ROOM_RUNTIME)

    api(Dependencies.GLIDE)
    api(Dependencies.GLIDE_OKHTTP3)
    api(Dependencies.GLIDE_ANNOTATIONS)

    api(Dependencies.RETROFIT)
    api(Dependencies.RETROFIT_CONVERTER_GSON)
    api(Dependencies.RETROFIT_CONVERTER_SCALARS)
    api(Dependencies.RETROFIT_CONVERTER_PROTOBUF)

    api(Dependencies.AUTO_SERVICE)
}
