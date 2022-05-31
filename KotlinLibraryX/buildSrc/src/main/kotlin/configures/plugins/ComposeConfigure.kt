package configures.plugins

import com.android.build.gradle.internal.dsl.BaseAppModuleExtension
import kotlinOptions
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure

/**
 * 组建支持compose配置依赖扩展
 * @author jv.lee
 * @date 2021/10/1
 */
fun Project.composeConfigure() {
    extensions.configure<BaseAppModuleExtension> {
        defaultConfig {
            vectorDrawables {
                useSupportLibrary = true
            }
        }

        buildFeatures {
            compose = true
        }

        composeOptions {
            kotlinCompilerExtensionVersion = "1.0.0"
            kotlinCompilerVersion = "1.5.10"
        }

        packagingOptions {
            resources {
                excludes += "/META-INF/{AL2.0,LGPL2.1}"
            }
        }

        kotlinOptions {
            useIR = true
            freeCompilerArgs = freeCompilerArgs + "-Xopt-in=kotlin.RequiresOptIn"
        }
    }
}