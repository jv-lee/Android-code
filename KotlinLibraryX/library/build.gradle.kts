import configures.libraryConfigure

plugins {
    alias(libs.plugins.buildVersion)
}

libraryConfigure("com.lee.library") {
    dependencies {
        kapt(libs.bundles.compiler)

        api(libs.bundles.androidx)
        api(libs.bundles.thirdPart)

        testImplementation(libs.bundles.test)
        androidTestImplementation(libs.bundles.androidTest)
        debugImplementation(libs.bundles.debug)
    }
}
