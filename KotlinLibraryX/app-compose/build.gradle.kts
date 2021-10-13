plugins {
    id(BuildPlugin.application)
    id(BuildPlugin.kotlin)
    id(BuildPlugin.kapt)
}

android {
    compileSdk = BuildConfig.compileSdk

    defaultConfig {
        applicationId = "com.simple.compose"
        minSdk = BuildConfig.minSdk
        targetSdk = BuildConfig.targetSdk
        versionName = BuildConfig.versionName
        versionCode = BuildConfig.versionCode

        multiDexEnabled = BuildConfig.multiDex

        vectorDrawables {
            useSupportLibrary = true
        }

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
        useIR = true
        freeCompilerArgs = freeCompilerArgs + "-Xopt-in=kotlin.RequiresOptIn"
    }

    buildFeatures {
        dataBinding = true
        viewBinding = true
        compose = true
    }

    kapt {
        generateStubs = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = "1.0.0"
        kotlinCompilerVersion = "1.5.10"
    }

    packagingOptions {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    commonProcessors()

    implementation(project(BuildModules.LIBRARY))

    val composeVersion = "1.0.0"

    implementation("androidx.compose.ui:ui:$composeVersion")
    implementation("androidx.compose.material:material:$composeVersion")
    implementation("androidx.compose.ui:ui-tooling-preview:$composeVersion")
    implementation("androidx.activity:activity-compose:1.3.0-alpha06")

    testImplementation("junit:junit:4.+")
    androidTestImplementation("androidx.test.ext:junit:1.1.2")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.3.0")
    androidTestImplementation("androidx.compose.ui:ui-test-junit4:$composeVersion")
    debugImplementation("androidx.compose.ui:ui-tooling:$composeVersion")
}