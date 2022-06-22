package dependencies

object Dependencies {
    //kotlin androidx核心库
    const val coreKtx = "androidx.core:core-ktx:${Version.ktxCore}"

    //协程
    const val coroutines = "org.jetbrains.kotlinx:kotlinx-coroutines-android:${Version.coroutines}"

    //生命周期
    const val lifecycle = "androidx.lifecycle:lifecycle-runtime-ktx:${Version.lifecycle}"
    const val lifecycleLivedata = "androidx.lifecycle:lifecycle-livedata-ktx:${Version.lifecycle}"
    const val lifecycleViewModel = "androidx.lifecycle:lifecycle-viewmodel-ktx:${Version.lifecycle}"

    //核心基础
    const val activity = "androidx.activity:activity-ktx:${Version.activity}"
    const val fragment = "androidx.fragment:fragment-ktx:${Version.fragment}"

    //分包库
    const val multidex = "androidx.multidex:multidex:${Version.multidex}"

    //androidUi
    const val appcompat = "androidx.appcompat:appcompat:${Version.appcompat}"
    const val material = "com.google.android.material:material:${Version.material}"
    const val recyclerview = "androidx.recyclerview:recyclerview:${Version.recyclerview}"
    const val constraint = "androidx.constraintlayout:constraintlayout:${Version.constraintLayout}"
    const val viewpager2 = "androidx.viewpager2:viewpager2:${Version.viewpager2}"
    const val swipeRefreshLayout = "androidx.swiperefreshlayout:swiperefreshlayout:${Version.swipeRefreshLayout}"
    const val slidingpanelayout =
        "androidx.slidingpanelayout:slidingpanelayout:${Version.slidingPaneLayout}"

    //fragment导航
    const val navigationFragment =
        "androidx.navigation:navigation-fragment-ktx:${Version.navigation}"
    const val navigationUi = "androidx.navigation:navigation-ui-ktx:${Version.navigation}"
    const val navigationDynamic =
        "androidx.navigation:navigation-dynamic-features-fragment:${Version.navigation}"

    // compose
    const val composeActivity = "androidx.activity:activity-compose:${Version.activity}"
    const val composeConstraint = "androidx.constraintlayout:constraintlayout-compose:1.0.0"
    const val composeUi = "androidx.compose.ui:ui:${Version.compose}"
    const val composeMaterial = "androidx.compose.material:material:${Version.compose}"
    const val composePreview = "androidx.compose.ui:ui-tooling-preview:${Version.compose}"
    const val composeUtil = "androidx.compose.ui:ui-util:1.1.1"
    const val composeLivedata = "androidx.compose.runtime:runtime-livedata:${Version.compose}"

    // compose accompanist
    const val composeCoin = "com.google.accompanist:accompanist-coil:0.15.0"
    const val composeNavigationAnimation =
        "com.google.accompanist:accompanist-navigation-animation:${Version.accompanist}"
    const val composeInsets = "com.google.accompanist:accompanist-insets:${Version.accompanist}"
    const val composeInsetsUi =
        "com.google.accompanist:accompanist-insets-ui:${Version.accompanist}"
    const val composeSystemUiController =
        "com.google.accompanist:accompanist-systemuicontroller:${Version.accompanist}"
    const val composeSwiperRefresh =
        "com.google.accompanist:accompanist-swiperefresh:${Version.accompanist}"
    const val composePager = "com.google.accompanist:accompanist-pager:${Version.accompanist}"
    const val composePagerIndicators =
        "com.google.accompanist:accompanist-pager-indicators:${Version.accompanist}"
    const val composeFlowlayout =
        "com.google.accompanist:accompanist-flowlayout:${Version.accompanist}"
    const val composeWebView =
        "com.google.accompanist:accompanist-webview:${Version.accompanist}"

    // paging 分页
    const val pagingRuntime = "androidx.paging:paging-runtime:${Version.paging}"
    const val pagingCompose = "androidx.paging:paging-compose:${Version.pagingCompose}"

    // hilt
    const val hiltAndroid = "com.google.dagger:hilt-android:${Version.hilt}"

    // hilt compose
    const val hiltNavigationCompose = "androidx.hilt:hilt-navigation-compose:${Version.hiltX}"
    const val hiltLifecycleViewModel =
        "androidx.hilt:hilt-lifecycle-viewmodel:${Version.hiltX}"

    //room数据库
    const val room = "androidx.room:room-ktx:${Version.room}"
    const val roomRuntime = "androidx.room:room-runtime:${Version.room}"
    const val roomPaging = "androidx.room:room-paging:${Version.roomPaging}"

    //图片加载
    const val glide = "com.github.bumptech.glide:glide:${Version.glide}"
    const val glideOkhttp3 = "com.github.bumptech.glide:okhttp3-integration:${Version.glide}"
    const val glideAnnotations = "com.github.bumptech.glide:annotations:${Version.glide}"

    //网络加载
    const val retrofit = "com.squareup.retrofit2:retrofit:${Version.retrofit}"
    const val retrofitConverterGson = "com.squareup.retrofit2:converter-gson:${Version.retrofit}"
    const val retrofitConverterScalars =
        "com.squareup.retrofit2:converter-scalars:${Version.retrofit}"

    //gson解析
    const val gson ="com.google.code.gson:gson:${Version.gson}"

    //protobuf
    const val protobuf = "com.google.protobuf:protobuf-javalite:${Version.protobuf}"

    //组件化依赖
    const val autoService = "com.google.auto.service:auto-service:${Version.autoService}"

    //图片选择器
    const val imageTools = "com.github.jv-lee.imagetools:library:1.4.4"
}
