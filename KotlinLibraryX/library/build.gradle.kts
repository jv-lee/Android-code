plugins {
    id(BuildPlugin.library)
    id(BuildPlugin.kotlin)
    id(BuildPlugin.kapt)
}

android {
    compileSdk = BuildConfig.compileSdk

    defaultConfig {
        minSdk = BuildConfig.minSdk
        targetSdk = BuildConfig.targetSdk
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
    DependenciesEach.processors.forEach { kapt(it) }
    DependenciesEach.support.forEach { api(it) }
}
