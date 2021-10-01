import com.google.protobuf.gradle.generateProtoTasks
import com.google.protobuf.gradle.proto
import com.google.protobuf.gradle.protobuf
import com.google.protobuf.gradle.protoc

plugins {
    id("com.android.application")
    id("kotlin-android")
    id("kotlin-kapt")
    id("com.google.protobuf")
}

android {
    compileSdk = BuildConfig.compileSdk

    defaultConfig {
        applicationId = "com.lee.api"
        minSdk = BuildConfig.minSdk
        targetSdk = BuildConfig.targetSdk
        versionName = BuildConfig.versionName
        versionCode = BuildConfig.versionCode

        multiDexEnabled = BuildConfig.multiDex

        vectorDrawables.useSupportLibrary = BuildConfig.SUPPORT_LIBRARY_VECTOR_DRAWABLES
        testInstrumentationRunner = BuildConfig.TEST_INSTRUMENTATION_RUNNER
        testInstrumentationRunnerArguments.putAll(BuildConfig.TEST_INSTRUMENTATION_RUNNER_ARGUMENTS)
    }

    buildTypes {
        getByName(BuildType.RELEASE) {
            isMinifyEnabled = BuildRelease.isMinifyEnabled
            proguardFiles("proguard-android-optimize.txt", "proguard-rules.pro")
        }

        getByName(BuildType.DEBUG) {
            isMinifyEnabled = BuildDebug.isMinifyEnabled
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

    sourceSets.getByName("main") {
        java {
            srcDir("src/main/java")
        }

//        proto {
//            srcDir("src/main/proto")
//            include("**/*.proto")
//        }
    }
}

protobuf {
    protoc {
        artifact = "com.google.protobuf:protoc:3.10.0"
    }
    generateProtoTasks {
        sourceSets
        all().onEach { task ->
            task.builtins {
                remove("java")
            }
            task.builtins {
                java {}
            }
        }
    }
}

dependencies {
    implementation(project(BuildModules.LIBRARY))
    DependenciesEach.processors.forEach { kapt(it) }

    // Preferences DataStore
    implementation("androidx.datastore:datastore-preferences:1.0.0-alpha08")

    // Proto DataStore
    implementation("androidx.datastore:datastore-core:1.0.0-alpha08")

    implementation("com.google.protobuf:protobuf-java:3.11.0")
//    implementation 'com.google.protobuf:protobuf-javalite:3.11.0'

    //WorkManager
    implementation("androidx.work:work-runtime:2.5.0")

    testImplementation("junit:junit:4.13.2")
}
