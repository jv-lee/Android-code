import configures.appConfigure

plugins {
    alias(libs.plugins.buildVersion)
}

appConfigure("com.lee.simple") {
    dependencies {
        kapt(libs.bundles.compiler)

        testImplementation(libs.bundles.test)
        androidTestImplementation(libs.bundles.androidTest)
        debugImplementation(libs.bundles.debug)
    }
}