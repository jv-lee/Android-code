package dependencies

object TestDependencies {
    const val junit = "junit:junit:${Version.junit}"
    const val junitAndroid = "androidx.test.ext:junit:${Version.junitAndroid}"
    const val espresso = "androidx.test.espresso:espresso-core:${Version.espressoAndroid}"
    const val leakcanaryDebug = "com.squareup.leakcanary:leakcanary-android:${Version.leakcanary}"
    const val leakcanaryRelease = "com.squareup.leakcanary:leakcanary-android-no-op:${Version.leakcanary}"
}