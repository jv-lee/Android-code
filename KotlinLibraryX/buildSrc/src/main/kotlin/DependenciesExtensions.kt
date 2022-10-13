import dependencies.Dependencies
import dependencies.ProcessorsDependencies
import dependencies.TestDependencies
import org.gradle.kotlin.dsl.DependencyHandlerScope
import org.gradle.kotlin.dsl.exclude

//app依赖扩展配置
fun DependencyHandlerScope.appDependencies() {
    commonProcessors()
    commonTest()
}

//注解处理器基础依赖
fun DependencyHandlerScope.commonProcessors() {
    kapt(ProcessorsDependencies.annotation)
    kapt(ProcessorsDependencies.room)
    kapt(ProcessorsDependencies.glide)
    kapt(ProcessorsDependencies.autoService)
}

//基础测试依赖
fun DependencyHandlerScope.commonTest() {
    testImplementation(TestDependencies.junit)
    androidTestImplementation(TestDependencies.junitAndroid)
    androidTestImplementation(TestDependencies.espresso)
    debugImplementation(TestDependencies.leakcanaryDebug)
    implementation(TestDependencies.blockCanary)
}

//基础依赖配置
fun DependencyHandlerScope.commonDependencies() {
    api(Dependencies.coreKtx)

    api(Dependencies.coroutines)

    api(Dependencies.lifecycle)
    api(Dependencies.lifecycleLivedata)
    api(Dependencies.lifecycleViewModel)

    api(Dependencies.activity)
    api(Dependencies.fragment)

    api(Dependencies.multidex)

    api(Dependencies.appcompat)
    api(Dependencies.material)
    api(Dependencies.recyclerview)
    api(Dependencies.constraint)
    api(Dependencies.viewpager2)
    api(Dependencies.swipeRefreshLayout)
    api(Dependencies.slidingpanelayout)
    api(Dependencies.webkit)

    // navigationFragment 会引入最新的slidingpanelayout库，最新库存在显示bug，需要过滤单独引入1.1.0
    api(Dependencies.navigationFragment) { exclude("androidx.slidingpanelayout") }
    api(Dependencies.navigationUi)

    api(Dependencies.room)
    api(Dependencies.roomRuntime)

    api(Dependencies.glide)
    api(Dependencies.glideOkhttp3)
    api(Dependencies.glideAnnotations)

    api(Dependencies.retrofit)
    api(Dependencies.retrofitConverterGson) { exclude("com.google.code.gson") }
    api(Dependencies.retrofitConverterScalars)

    api(Dependencies.gson)

    api(Dependencies.protobuf)

    api(Dependencies.autoService)
} 

