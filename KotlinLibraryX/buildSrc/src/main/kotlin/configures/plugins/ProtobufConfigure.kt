package configures.plugins

import build.BuildPlugin
import com.google.protobuf.gradle.builtins
import com.google.protobuf.gradle.generateProtoTasks
import com.google.protobuf.gradle.protobuf
import com.google.protobuf.gradle.protoc
import org.gradle.api.Project

/**
 * protobuf 编译基础配置
 * @author jv.lee
 * @date 2021/10/15
 */
fun Project.protobufConfigure() {
    plugins.apply(BuildPlugin.protobuf)

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
}
