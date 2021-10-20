import configures.appConfigure
import configures.plugins.composeConfigure

appConfigure(packageName = "com.simple.compose", projectConfigure = {
    composeConfigure()

    dependencies {
        val composeVersion = "1.0.0"

        implementation("androidx.compose.ui:ui:$composeVersion")
        implementation("androidx.compose.material:material:$composeVersion")
        implementation("androidx.compose.ui:ui-tooling-preview:$composeVersion")
        implementation("androidx.compose.runtime:runtime-livedata:$composeVersion")

        implementation("androidx.activity:activity-compose:1.3.0")
        implementation("androidx.lifecycle:lifecycle-viewmodel-compose:1.0.0-alpha07")
        implementation("androidx.constraintlayout:constraintlayout-compose:1.0.0-beta01")

        androidTestImplementation("androidx.test.ext:junit:1.1.2")
        androidTestImplementation("androidx.test.espresso:espresso-core:3.3.0")
        androidTestImplementation("androidx.compose.ui:ui-test-junit4:$composeVersion")
        debugImplementation("androidx.compose.ui:ui-tooling:$composeVersion")
    }
})

