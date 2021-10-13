package dependencies

object Dependencies {
    //kotlin androidx核心库
    const val coreKtx = "androidx.core:core-ktx:1.2.0"

    //协程
    const val coroutines = "org.jetbrains.kotlinx:kotlinx-coroutines-core:1.3.9"
    const val coroutinesAndroid = "org.jetbrains.kotlinx:kotlinx-coroutines-android:1.3.4"

    //生命周期
    const val lifecycle = "androidx.lifecycle:lifecycle-runtime-ktx:${Version.lifecycle}"
    const val lifecycleLivedata = "androidx.lifecycle:lifecycle-livedata-ktx:${Version.lifecycle}"
    const val lifecycleViewModel = "androidx.lifecycle:lifecycle-viewmodel-ktx:${Version.lifecycle}"

    //核心基础
    const val activity = "androidx.activity:activity-ktx:${Version.activity}"
    const val fragment = "androidx.fragment:fragment-ktx:${Version.fragment}"

    //分包库
    const val multidex = "androidx.multidex:multidex:2.0.1"

    //androidUi
    const val appcompat = "androidx.appcompat:appcompat:1.2.0"
    const val support = "androidx.legacy:legacy-support-v4:1.0.0"
    const val recyclerview = "androidx.recyclerview:recyclerview:1.1.0"
    const val constraint = "androidx.constraintlayout:constraintlayout:2.0.2"
    const val viewpager2 = "androidx.viewpager2:viewpager2:1.0.0"
    const val material = "com.google.android.material:material:1.1.0"

    //fragment导航
    const val navigationFragment =
        "androidx.navigation:navigation-fragment-ktx:${Version.navigation}"
    const val navigationUi = "androidx.navigation:navigation-ui-ktx:${Version.navigation}"
    const val navigationDynamic =
        "androidx.navigation:navigation-dynamic-features-fragment:${Version.navigation}"

    //room数据库
    const val room = "androidx.room:room-ktx:${Version.room}"
    const val roomRuntime = "androidx.room:room-runtime:${Version.room}"

    //图片加载
    const val glide = "com.github.bumptech.glide:glide:${Version.glide}"
    const val glideOkhttp3 = "com.github.bumptech.glide:okhttp3-integration:${Version.glide}"
    const val glideAnnotations = "com.github.bumptech.glide:annotations:${Version.glide}"

    //网络加载
    const val retrofit = "com.squareup.retrofit2:retrofit:${Version.retrofit}"
    const val retrofitConverterGson = "com.squareup.retrofit2:converter-gson:${Version.retrofit}"
    const val retrofitConverterScalars =
        "com.squareup.retrofit2:converter-scalars:${Version.retrofit}"

    //protobuf
    const val protobuf = "com.google.protobuf:protobuf-javalite:${Version.protobuf}"

    //组件化依赖
    const val autoService = "com.google.auto.service:auto-service:${Version.autoService}"

    //图片选择器
    const val imageTools = "com.github.jv-lee.imagetools:library:1.4.3"
}
