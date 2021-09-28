/*
 * Copyright 2019 vmadalin.com
 *
 * Licensed under the Apache License Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing software
 * distributed under the License is distributed on an "AS IS" BASIS
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package dependencies

/**
 * Project dependencies makes it easy to include external binaries or
 * other library modules to build.
 */
object Dependencies {
    //kotlin androidx核心库
    const val KOTLIN_CORE = "androidx.core:core-ktx:1.2.0"

    //协程
    const val COROUTINES = "org.jetbrains.kotlinx:kotlinx-coroutines-core:1.3.9"
    const val COROUTINES_ANDROID = "org.jetbrains.kotlinx:kotlinx-coroutines-android:1.3.4"

    //生命周期
    const val LIFECYCLE_RUNTIME = "androidx.lifecycle:lifecycle-runtime-ktx:$lifecycle"
    const val LIFECYCLE_LIVEDATA = "androidx.lifecycle:lifecycle-livedata-ktx:$lifecycle"
    const val LIFECYCLE_VIEWMODEL = "androidx.lifecycle:lifecycle-viewmodel-ktx:$lifecycle"

    //核心基础
    const val ACTIVITY = "androidx.activity:activity-ktx:$activity"
    const val FRAGMENT = "androidx.fragment:fragment-ktx:$fragment"

    //分包库
    const val MULTIDEX = "androidx.multidex:multidex:2.0.1"

    //androidUi
    const val APPCOMPAT = "androidx.appcompat:appcompat:1.2.0"
    const val SUPPORT = "androidx.legacy:legacy-support-v4:1.0.0"
    const val RECYCLERVIEW = "androidx.recyclerview:recyclerview:1.1.0"
    const val CONSTRAINT = "androidx.constraintlayout:constraintlayout:2.0.2"
    const val VIEWPAGER2 = "androidx.viewpager2:viewpager2:1.0.0"
    const val MATERIAL = "com.google.android.material:material:1.1.0"

    //fragment导航
    const val NAVIGATION_FRAGMENT = "androidx.navigation:navigation-fragment-ktx:$navigation"
    const val NAVIGATION_UI = "androidx.navigation:navigation-ui-ktx:$navigation"
    const val NAVIGATION_DYNAMIC =
        "androidx.navigation:navigation-dynamic-features-fragment:$navigation"

    //room数据库
    const val ROOM = "androidx.room:room-ktx:${room}"
    const val ROOM_RUNTIME = "androidx.room:room-runtime:${room}"

    //图片加载
    const val GLIDE = "com.github.bumptech.glide:glide:${glide}"
    const val GLIDE_OKHTTP3 = "com.github.bumptech.glide:okhttp3-integration:${glide}"
    const val GLIDE_ANNOTATIONS = "com.github.bumptech.glide:annotations:${glide}"

    //网络加载
    const val RETROFIT = "com.squareup.retrofit2:retrofit:${retrofit}"
    const val RETROFIT_CONVERTER_GSON = "com.squareup.retrofit2:converter-gson:${retrofit}"
    const val RETROFIT_CONVERTER_SCALARS = "com.squareup.retrofit2:converter-scalars:${retrofit}"
    const val RETROFIT_CONVERTER_PROTOBUF = "com.squareup.retrofit2:converter-protobuf:${retrofit}"

    //组件化依赖
    const val AUTO_SERVICE = "com.google.auto.service:auto-service:${autoService}"
}
