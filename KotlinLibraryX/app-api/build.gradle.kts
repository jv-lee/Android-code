import configures.appConfigure

plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.buildVersion)
    alias(libs.plugins.protobuf)
}

appConfigure("com.lee.api") {
    dependencies {
        implementation(libs.protobuf.javalite)
        // Preferences DataStore
        implementation("androidx.datastore:datastore-preferences:1.1.1")

        // Proto DataStore
        implementation("androidx.datastore:datastore:1.1.1")

        // WorkManager
        implementation("androidx.work:work-runtime:2.9.1")

        // Startup
        implementation("androidx.startup:startup-runtime:1.2.0")
    }

}

android {
        protobuf {
        protoc {
            artifact = "com.google.protobuf:protoc:3.21.6"
        }
        generateProtoTasks {
            all().onEach { task ->
                task.builtins {
                    create("java") {
                        option("lite")
                    }
                }
            }
        }
    }
}
