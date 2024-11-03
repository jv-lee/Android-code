package configures

import build.BuildConfig
import build.BuildDebug
import build.BuildModules
import build.BuildPlugin
import build.BuildRelease
import build.BuildTypes
import com.android.build.gradle.internal.dsl.BaseAppModuleExtension
import freeCompilerArgs
import implementation
import org.gradle.api.JavaVersion
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.project
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

/**
 * app模块配置依赖扩展
 * @author jv.lee
 * @date 2021/10/1
 */
fun Project.appConfigure(packageName: String, projectConfigure: Project.() -> Unit = {}) {
    plugins.apply(BuildPlugin.APPLICATION)
    plugins.apply(BuildPlugin.KOTLIN_ANDROID)
    plugins.apply(BuildPlugin.KOTLIN_KAPT)

    extensions.configure<BaseAppModuleExtension> {
        namespace = packageName
        compileSdk = BuildConfig.COMPILE_SDK

        defaultConfig {
            applicationId = packageName

            minSdk = BuildConfig.MIN_SDK
            targetSdk = BuildConfig.TARGET_SDK
            versionName = BuildConfig.VERSION_NAME
            versionCode = BuildConfig.VERSION_CODE

            // 混淆配置 指定只支持中文字符
            multiDexEnabled = BuildConfig.MULTI_DEX_ENABLE
            resourceConfigurations.add("zh-rCN")

            testInstrumentationRunner = BuildConfig.TEST_INSTRUMENTATION_RUNNER
        }

        tasks.withType<KotlinCompile> {
            kotlinOptions.jvmTarget = "1.8"
            kotlinOptions.freeCompilerArgs += freeCompilerArgs
        }

        compileOptions {
            sourceCompatibility = JavaVersion.VERSION_1_8
            targetCompatibility = JavaVersion.VERSION_1_8
        }

        buildFeatures {
            dataBinding = true
            viewBinding = true
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
    }

    dependencies {
        implementation(project(BuildModules.LIBRARY))
    }

    projectConfigure()
}