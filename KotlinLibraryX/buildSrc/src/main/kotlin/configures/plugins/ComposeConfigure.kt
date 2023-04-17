@file:Suppress("UnstableApiUsage")

package configures.plugins

import com.android.build.gradle.internal.dsl.BaseAppModuleExtension
import kotlinOptions
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import dependencies.Version

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
            kotlinCompilerExtensionVersion = Version.composeCompiler
        }

        packagingOptions {
            resources {
                excludes += "/META-INF/{AL2.0,LGPL2.1}"
            }
        }

        kotlinOptions {
            freeCompilerArgs = freeCompilerArgs + "-Xopt-in=kotlin.RequiresOptIn"
        }
    }
}