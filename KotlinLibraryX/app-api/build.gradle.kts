import com.google.protobuf.gradle.*
import configures.appConfigure

plugins {
    id(build.BuildPlugin.protobuf).version(build.BuildPlugin.protobuf_version)
}

appConfigure(packageName = "com.lee.api")

protobuf {
    generatedFilesBaseDir = "$projectDir/src"
    protoc {
        artifact = "com.google.protobuf:protoc:3.10.0"
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

dependencies {
    // Preferences DataStore
    implementation("androidx.datastore:datastore-preferences:1.0.0")

    // Proto DataStore
    implementation("androidx.datastore:datastore:1.0.0")

    //WorkManager
    implementation("androidx.work:work-runtime:2.5.0")
}