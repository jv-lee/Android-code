import configures.appConfigure
import configures.protobufConfigure

appConfigure(packageName = "com.lee.api")
protobufConfigure()

dependencies {
    // Preferences DataStore
    implementation("androidx.datastore:datastore-preferences:1.0.0")

    // Proto DataStore
    implementation("androidx.datastore:datastore:1.0.0")

    //WorkManager
    implementation("androidx.work:work-runtime:2.5.0")
}