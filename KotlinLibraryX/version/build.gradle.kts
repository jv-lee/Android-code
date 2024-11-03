buildscript {
    repositories {
        mavenCentral()
        google()
    }
    dependencies {

        // 由于使用的 Kotlin 须要须要添加 Kotlin 插件
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:${libs.versions.kotlin.get()}")
    }
}

plugins {
    `kotlin-dsl`
    `java-gradle-plugin`
}

repositories {
    // 须要添加 jcenter 不然会提示找不到 gradlePlugin
    mavenCentral()
    google()
}


dependencies {

    compileOnly(gradleApi())
    compileOnly("org.jetbrains.kotlin:kotlin-gradle-plugin:${libs.versions.kotlin.get()}")
    compileOnly("com.android.tools.build:gradle:${libs.versions.agp.get()}")
}

gradlePlugin {
    plugins {
        create("version") {
            // 在 app 模块须要经过 id 引用这个插件
            id = "build-version"
            // 实现这个插件的类的路径
            implementationClass = "DependencyVersionPlugin"
        }
    }
}