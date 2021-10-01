plugins {
    id(BuildPlugin.application)
    id(BuildPlugin.kotlin)
    id(BuildPlugin.kapt)
}

android {
    compileSdk = BuildConfig.compileSdk

    defaultConfig {
        applicationId =  "com.lee.ui"
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
}

dependencies {
    DependenciesEach.processors.forEach { kapt(it) }

    implementation(project(BuildModules.LIBRARY))
    testImplementation("junit:junit:4.12")
}
