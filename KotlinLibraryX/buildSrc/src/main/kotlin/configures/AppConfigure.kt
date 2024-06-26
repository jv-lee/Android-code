@file:Suppress("UnstableApiUsage")

package configures

import build.*
import com.android.build.gradle.internal.dsl.BaseAppModuleExtension
import commonProcessors
import implementation
import kapt
import org.gradle.api.JavaVersion
import org.gradle.api.Project
import org.gradle.kotlin.dsl.*
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import configures.core.freeCompilerArgs
import testImplementation

/**
 * app模块配置依赖扩展
 * @author jv.lee
 * @date 2021/10/1
 */
fun Project.appConfigure(
    packageName: String = BuildConfig.applicationId,
    projectConfigure: Project.() -> Unit = {},
    androidConfigure: BaseAppModuleExtension.() -> Unit = {}
) {
    plugins.apply(BuildPlugin.application)
    plugins.apply(BuildPlugin.kotlin)
    plugins.apply(BuildPlugin.kapt)

    projectConfigure()

    extensions.configure<BaseAppModuleExtension> {
        compileSdk = BuildConfig.compileSdk

        defaultConfig {
            applicationId = packageName
            minSdk = BuildConfig.minSdk
            targetSdk = BuildConfig.targetSdk
            versionName = BuildConfig.versionName
            versionCode = BuildConfig.versionCode

            multiDexEnabled = BuildConfig.multiDex

            vectorDrawables.useSupportLibrary = BuildConfig.SUPPORT_LIBRARY_VECTOR_DRAWABLES
            testInstrumentationRunner = BuildConfig.TEST_INSTRUMENTATION_RUNNER
            testInstrumentationRunnerArguments.putAll(BuildConfig.TEST_INSTRUMENTATION_RUNNER_ARGUMENTS)
        }

        tasks.withType<KotlinCompile> {
            kotlinOptions.jvmTarget = "1.8"
            kotlinOptions.freeCompilerArgs += freeCompilerArgs
        }

        compileOptions {
            sourceCompatibility = JavaVersion.VERSION_1_8
            targetCompatibility = JavaVersion.VERSION_1_8
        }

        buildTypes {
            getByName(BuildTypes.DEBUG) {
                isMinifyEnabled = BuildDebug.isMinifyEnabled // 混淆模式
                isShrinkResources = BuildDebug.isShrinkResources // 资源压缩
                proguardFiles("proguard-android-optimize.txt", "proguard-rules.pro")
            }

            getByName(BuildTypes.RELEASE) {
                isMinifyEnabled = BuildRelease.isMinifyEnabled // 混淆模式
                isShrinkResources = BuildRelease.isShrinkResources // 资源压缩
                proguardFiles("proguard-android-optimize.txt", "proguard-rules.pro")
            }
        }

        buildFeatures {
            dataBinding = true
            viewBinding = true
        }

        androidConfigure()
    }

    dependencies {
        commonProcessors()

        implementation(project(BuildModules.LIBRARY))
        testImplementation("junit:junit:4.12")
    }
}