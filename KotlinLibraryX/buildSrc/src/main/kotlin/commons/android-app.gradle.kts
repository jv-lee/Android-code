package commons

import BuildAndroidConfig
import dependencies.AnnotationProcessorsDependencies
import dependencies.Dependencies
import BuildType

plugins {
    id("com.android.application")
    id("kotlin-android")
    id("kotlin-kapt")
}

android {
    compileSdk = BuildAndroidConfig.COMPILE_SDK

    defaultConfig {
        applicationId = BuildAndroidConfig.APPLICATION_ID
        minSdk = BuildAndroidConfig.MIN_SDK
        targetSdk = BuildAndroidConfig.TARGET_SDK
        versionName = BuildAndroidConfig.VERSION_NAME
        versionCode = BuildAndroidConfig.VERSION_CODE

        vectorDrawables.useSupportLibrary = BuildAndroidConfig.SUPPORT_LIBRARY_VECTOR_DRAWABLES
        testInstrumentationRunner = BuildAndroidConfig.TEST_INSTRUMENTATION_RUNNER
        testInstrumentationRunnerArguments.putAll(BuildAndroidConfig.TEST_INSTRUMENTATION_RUNNER_ARGUMENTS)
    }

    buildTypes {
        getByName(BuildType.RELEASE) {
            isMinifyEnabled = BuildTypeRelease.isMinifyEnabled
            proguardFiles("proguard-android-optimize.txt", "proguard-rules.pro")
        }

        getByName(BuildType.DEBUG) {
            isMinifyEnabled = BuildTypeDebug.isMinifyEnabled
            proguardFiles("proguard-android-optimize.txt", "proguard-rules.pro")
        }
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

    kapt {
        generateStubs = true
    }
}

dependencies {
    kapt(AnnotationProcessorsDependencies.ANNOTATION)
    kapt(AnnotationProcessorsDependencies.ROOM)
    kapt(AnnotationProcessorsDependencies.GLIDE)
    kapt(AnnotationProcessorsDependencies.AUTO_SERVICE)

    implementation(project(BuildModules.LIBRARY))
    testImplementation("junit:junit:4.12")
}
