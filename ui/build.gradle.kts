plugins {
    id(Plugin.library)
    id(Plugin.hilt)
    kotlin(Plugin.kotlin_android)
    kotlin(Plugin.kotlin_ext)
    kotlin(Plugin.kapt)
}
apply(from = "../gradle/UPLOAD.gradle")
android {
    compileSdkVersion(Version.compileSdk)
    defaultConfig {
        minSdkVersion(Version.minSdk)
        targetSdkVersion(Version.targetSdk)
    }
    androidExtensions { isExperimental = true }
    compileOptions { kotlinOptions.freeCompilerArgs += listOf("-module-name", "com.ydevelop.gallery.ui") }
}
dependencies {
    api(project(":core"))
//    api(Dep.core)
    compileOnly(Dep.recyclerView)
    compileOnly(Dep.fragment)
    compileOnly(Dep.kotlin)
    compileOnly(Dep.glide)
    compileOnly(Dep.material)
    compileOnly(Dep.uCrop)
    compileOnly(Dep.cropper)
    compileOnly(Dep.hilt)
    kapt(Dep.hiltCompiler)
}