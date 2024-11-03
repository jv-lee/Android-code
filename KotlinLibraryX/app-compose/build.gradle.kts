import configures.appConfigure

plugins {
    alias(libs.plugins.buildVersion)
}

appConfigure(packageName = "com.simple.compose", projectConfigure = {
    composeConfigure()

    dependencies {
        implementation(libs.androidx.compose.bom)
        implementation(libs.androidx.ui)
        implementation(libs.androidx.material)
        implementation(libs.androidx.ui.tooling.preview)
        implementation(libs.androidx.lifecycle.livedata.ktx)
//        implementation("androidx.compose.runtime:runtime-livedata:${Version.compose}")

        implementation(libs.androidx.activity.compose)
        implementation(libs.androidx.lifecycle.viewmodel.compose)
        implementation(libs.androidx.constraintlayout.compose)

        androidTestImplementation(libs.androidx.junit)
        androidTestImplementation(libs.androidx.compose.bom)
        androidTestImplementation(libs.androidx.espresso.core)
        androidTestImplementation(libs.androidx.ui.test.junit4)
        debugImplementation(libs.androidx.ui.tooling)
    }
})

