plugins {
    `kotlin-dsl`
    `java-gradle-plugin`
}

repositories {
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
        create("build-version") {
            // 在 app 模块须要经过 id 引用这个插件
            id = "build-version"
            // 实现这个插件的类的路径
            implementationClass = "DependencyVersionPlugin"
        }
    }
}