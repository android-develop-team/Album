plugins {
    id(Plugin.library)
    id(Plugin.hilt)
    kotlin(Plugin.kotlin_android)
    kotlin(Plugin.kotlin_ext)
    kotlin(Plugin.kapt)
}
apply(from = "../gradle/UPLOAD.gradle")
android {
    compileSdkVersion(Version.minSdk)
    defaultConfig {
        minSdkVersion(Version.minSdk)
        targetSdkVersion(Version.targetSdk)
    }
    androidExtensions { isExperimental = true }
    compileOptions { kotlinOptions.freeCompilerArgs += listOf("-module-name", "com.ydevelop.gallery.scan") }
}
dependencies {
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))
    api(Dep.kotlinx)
    compileOnly(Dep.fragment)
    compileOnly(Dep.kotlin)
    compileOnly(Dep.hilt)
    compileOnly(Dep.hiltVM)
    kapt(Dep.hiltCompiler)
}