package configures

import build.BuildConfig
import build.BuildPlugin
import com.android.build.gradle.LibraryExtension
import freeCompilerArgs
import org.gradle.api.JavaVersion
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

/**
 * 基础库配置依赖扩展
 * @author jv.lee
 * @date 2021/10/1
 */
fun Project.libraryConfigure(namespace: String, projectConfigure: Project.() -> Unit = {}) {
    plugins.apply(BuildPlugin.LIBRARY)
    plugins.apply(BuildPlugin.KOTLIN_ANDROID)
    plugins.apply(BuildPlugin.KOTLIN_KAPT)
    plugins.apply(BuildPlugin.KOTLIN_PARCELIZE)

    extensions.configure<LibraryExtension> {
        this.namespace = namespace
        compileSdk = BuildConfig.COMPILE_SDK

        defaultConfig {
            minSdk = BuildConfig.MIN_SDK
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

        sourceSets {
            getByName("main") {
                assets {
                    srcDir("src/main/assets")
                }
            }
        }

        projectConfigure()
    }

}