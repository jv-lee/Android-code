import com.google.protobuf.gradle.generateProtoTasks
import com.google.protobuf.gradle.protobuf
import com.google.protobuf.gradle.protoc
import org.jetbrains.kotlin.compiler.plugin.parsePluginOption

plugins {
    id("commons.android-app")
    id("com.google.protobuf")
}

android {
    defaultConfig {
        applicationId = "com.lee.api"
    }
}

protobuf {
    protoc {
        artifact = "com.google.protobuf:protoc:3.10.0"
    }

    // Generates the java Protobuf-lite code for the Protobufs in this project. See
    // https://github.com/google/protobuf-gradle-plugin#customizing-protobuf-compilation
    // for more information.
    generateProtoTasks {
        all().onEach { task ->
            task.builtins {
                java {
                    parsePluginOption("lite")
                }
            }
        }
    }
}

dependencies {
    // Preferences DataStore
    implementation("androidx.datastore:datastore-preferences:1.0.0-alpha04")

    // Proto DataStore
    implementation("androidx.datastore:datastore-core:1.0.0-alpha04")

    implementation("com.google.protobuf:protobuf-java:3.11.0")
//    implementation 'com.google.protobuf:protobuf-javalite:3.11.0'

    //WorkManager
    implementation("androidx.work:work-runtime:2.3.4")
}