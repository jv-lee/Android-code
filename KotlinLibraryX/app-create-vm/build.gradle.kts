import build.BuildPlugin
import configures.appConfigure


appConfigure(packageName = "com.simple.vm", projectConfigure = {
    plugins.apply(BuildPlugin.hilt)

    dependencies {
        //hilt 依赖注入
        implementation("com.google.dagger:hilt-android:2.38.1")
        kapt("com.google.dagger:hilt-android-compiler:2.38.1")
    }
})