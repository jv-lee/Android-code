package configures.plugins

import build.BuildPlugin
import org.gradle.api.Project

/**
 * protobuf 编译基础配置
 * @author jv.lee
 * @date 2021/10/15
 */
fun Project.protobufConfigure() {
    plugins.apply(BuildPlugin.protobuf)

//    protobuf {
//        generatedFilesBaseDir = "$projectDir/src"
//        protoc {
//            artifact = "com.google.protobuf:protoc:3.21.6"
//        }
//        generateProtoTasks {
//            all().onEach { task ->
//                task.builtins {
//                    create("java") {
//                        option("lite")
//                    }
//                }
//            }
//        }
//    }
}
