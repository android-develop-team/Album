object Plugin {
    const val application = "com.android.application"
    const val library = "com.android.library"
    const val kotlin_android = "android"
    const val kotlin_ext = "android.extensions"
    const val kapt = "kapt"
    const val hilt = "dagger.hilt.android.plugin"
}

object Version {
    const val applicationId = "com.gallery.sample"
    const val compileSdk = 30
    const val minSdk = 19
    const val targetSdk = 30
    const val versionCode = 1
    const val versionName = "1.0"
}

object ClassPath {
    const val kotlin = "org.jetbrains.kotlin:kotlin-gradle-plugin:1.4.0"
    const val bintray = "guru.stefma.bintrayrelease:bintrayrelease:1.1.2"
    const val gradle = "com.android.tools.build:gradle:4.0.1"
    const val hilt = "com.google.dagger:hilt-android-gradle-plugin:2.28-alpha"
}

object Dep {
    const val viewHolder = "com.ydevelop:rv-adapter-viewholder:0.0.6"
    const val kotlinx = "com.ydevelop:kotlinx:0.0.7"
    const val scan = "com.ydevelop:gallery.scan:0.1.5"
    const val core = "com.ydevelop:gallery.core:0.1.5"
    const val ui = "com.ydevelop:gallery.ui:0.1.0"

    const val kotlin = "org.jetbrains.kotlin:kotlin-stdlib-jdk7:1.4.0"

    const val hilt = "com.google.dagger:hilt-android:2.28-alpha"
    const val hiltVM = "androidx.hilt:hilt-lifecycle-viewmodel:1.0.0-alpha02"
    const val hiltCompiler = "com.google.dagger:hilt-android-compiler:2.28-alpha"

    const val appcompat = "androidx.appcompat:appcompat:1.3.0-alpha02"
    const val fragment = "androidx.fragment:fragment:1.3.0-alpha08"
    const val viewPager2 = "androidx.viewpager2:viewpager2:1.1.0-alpha01"
    const val recyclerView = "androidx.recyclerview:recyclerview:1.2.0-alpha05"

    const val material = "com.google.android.material:material:1.3.0-alpha02"
    const val cameraview = "com.otaliastudios:cameraview:2.6.4"
    const val cropper = "com.theartofdev.edmodo:android-image-cropper:2.8.0"
    const val glide = "com.github.bumptech.glide:glide:4.11.0"
    const val uCrop = "com.github.yalantis:ucrop:2.2.6"
}