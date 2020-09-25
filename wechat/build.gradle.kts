plugins {
    id(Plugin.library)
    id(Plugin.hilt)
    kotlin(Plugin.kotlin_android)
    kotlin(Plugin.kotlin_ext)
    kotlin(Plugin.kapt)
}
android {
    compileSdkVersion(Version.compileSdk)
    defaultConfig {
        minSdkVersion(Version.minSdk)
        targetSdkVersion(Version.targetSdk)
    }
    androidExtensions { isExperimental = true }
}
dependencies {
    api(project(":ui"))
//    api(Dep.ui)
    compileOnly(Dep.appcompat)
    compileOnly(Dep.material)
    compileOnly(Dep.recyclerView)
    compileOnly(Dep.fragment)
    compileOnly(Dep.glide)
    compileOnly(Dep.kotlin)
    compileOnly(Dep.hilt)
    kapt(Dep.hiltCompiler)
}
