import configures.appConfigure

plugins {
    alias(libs.plugins.buildVersion)
    alias(libs.plugins.hilt)
}

appConfigure("com.simple.vm") {
    dependencies {
        // hilt 依赖注入
        kapt(libs.hilt.android.compiler)
        implementation(libs.hilt.android)
    }
}

kapt {
    correctErrorTypes = true
}
