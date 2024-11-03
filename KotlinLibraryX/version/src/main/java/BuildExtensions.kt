import build.BuildConfig
import build.BuildDebug
import build.BuildRelease
import build.BuildTypes
import com.android.build.gradle.LibraryExtension
import com.android.build.gradle.internal.dsl.BaseAppModuleExtension
import org.gradle.api.Action
import org.gradle.api.Project
import org.gradle.api.artifacts.Dependency
import org.gradle.api.artifacts.ExternalModuleDependency
import org.gradle.api.artifacts.dsl.DependencyHandler
import org.jetbrains.kotlin.gradle.plugin.KaptExtension
import org.gradle.api.plugins.ExtensionAware
import org.gradle.kotlin.dsl.accessors.runtime.addDependencyTo
import org.gradle.kotlin.dsl.configure
import org.jetbrains.kotlin.gradle.dsl.KotlinJvmOptions

fun DependencyHandler.implementation(dependencyNotation: Any): Dependency? =
    add("implementation", dependencyNotation)

fun DependencyHandler.implementation(
    dependencyNotation: Any,
    dependencyConfiguration: Action<ExternalModuleDependency>
): Dependency =
    addDependencyTo(this, "implementation", dependencyNotation, dependencyConfiguration)

fun DependencyHandler.api(dependencyNotation: Any): Dependency? =
    add("api", dependencyNotation)

fun DependencyHandler.api(
    dependencyNotation: Any,
    dependencyConfiguration: Action<ExternalModuleDependency>
): Dependency =
    addDependencyTo(this, "api", dependencyNotation, dependencyConfiguration)

fun DependencyHandler.kapt(dependencyNotation: Any): Dependency? =
    add("kapt", dependencyNotation)

fun DependencyHandler.releaseImplementation(dependencyNotation: Any): Dependency? =
    add("releaseImplementation", dependencyNotation)

fun DependencyHandler.releaseApi(dependencyNotation: Any): Dependency? =
    add("releaseApi", dependencyNotation)

fun DependencyHandler.debugImplementation(dependencyNotation: Any): Dependency? =
    add("debugImplementation", dependencyNotation)

fun DependencyHandler.debugApi(dependencyNotation: Any): Dependency? =
    add("debugApi", dependencyNotation)

fun DependencyHandler.testImplementation(dependencyNotation: Any): Dependency? =
    add("testImplementation", dependencyNotation)

fun DependencyHandler.testApi(dependencyNotation: Any): Dependency? =
    add("testApi", dependencyNotation)

fun DependencyHandler.androidTestImplementation(dependencyNotation: Any): Dependency? =
    add("androidTestImplementation", dependencyNotation)

fun DependencyHandler.androidTestApi(dependencyNotation: Any): Dependency? =
    add("androidTestApi", dependencyNotation)

fun Project.kapt(configure: Action<KaptExtension>): Unit =
    (this as ExtensionAware).extensions.configure("kapt", configure)

fun BaseAppModuleExtension.kotlinOptions(configure: Action<KotlinJvmOptions>): Unit =
    (this as ExtensionAware).extensions.configure("kotlinOptions", configure)

fun Project.androidConfigure(configure: Action<LibraryExtension>) {
    extensions.configure("android", configure)
}

val freeCompilerArgs = mutableListOf(
    "-Xskip-prerelease-check",
    "-Xjvm-default=all",
    "-opt-in=kotlin.RequiresOptIn",
    "-opt-in=androidx.compose.material.ExperimentalMaterialApi",
    "-opt-in=androidx.compose.foundation.ExperimentalFoundationApi",
    "-opt-in=androidx.compose.animation.ExperimentalAnimationApi",
    "-opt-in=androidx.compose.ui.ExperimentalComposeUiApi",
    "-opt-in=androidx.paging.ExperimentalPagingApi",
    "-opt-in=coil.annotation.ExperimentalCoilApi",
)

/**
 * 项目公共参数配置依赖扩展
 */
fun Project.paramsConfigure() {
    extensions.configure<LibraryExtension> {
        buildFeatures {
            buildConfig = true
        }
        buildTypes {
            getByName(BuildTypes.DEBUG) {
                buildConfigField("Integer", "VERSION_CODE", "${BuildConfig.VERSION_CODE}")
            }
            getByName(BuildTypes.RELEASE) {
                buildConfigField("Integer", "VERSION_CODE", "${BuildConfig.VERSION_CODE}")
            }
        }
    }
}

/**
 * compose 基础配置
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
            kotlinCompilerExtensionVersion = BuildConfig.COMPOSE_KOTLIN_COMPILER
        }

        packaging {
            resources {
                excludes += "/META-INF/{AL2.0,LGPL2.1}"
            }
        }

        kotlinOptions {
            freeCompilerArgs = freeCompilerArgs + "-Xopt-in=kotlin.RequiresOptIn"
        }
    }
}