import configures.appConfigure
import configures.plugins.composeConfigure
import dependencies.Version

appConfigure(packageName = "com.simple.compose", projectConfigure = {
    composeConfigure()

    dependencies {
        implementation("androidx.compose.ui:ui:${Version.compose}")
        implementation("androidx.compose.material:material:${Version.compose}")
        implementation("androidx.compose.material:material-icons-extended:${Version.compose}")
        implementation("androidx.compose.ui:ui-tooling-preview:${Version.compose}")
        implementation("androidx.compose.runtime:runtime-livedata:${Version.compose}")

        implementation("androidx.activity:activity-compose:1.9.2")
        implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.8.6")
        implementation("androidx.constraintlayout:constraintlayout-compose:1.0.1")

        androidTestImplementation("androidx.test.ext:junit:1.2.1")
        androidTestImplementation("androidx.test.espresso:espresso-core:3.6.1")
        androidTestImplementation("androidx.compose.ui:ui-test-junit4:${Version.compose}")
        debugImplementation("androidx.compose.ui:ui-tooling:${Version.compose}")
    }
})

