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
    compileOptions { kotlinOptions.freeCompilerArgs += listOf("-module-name", "com.ydevelop.gallery.core") }
}
dependencies {
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))
    api(project(":scan"))
//    api(Dep.scan)
    api(Dep.viewHolder)
    compileOnly(Dep.appcompat)
    compileOnly(Dep.fragment)
    compileOnly(Dep.kotlin)
    compileOnly(Dep.viewPager2)
    compileOnly(Dep.hilt)
    kapt(Dep.hiltCompiler)
}