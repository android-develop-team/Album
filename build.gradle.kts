buildscript {
    repositories {
        jcenter()
        google()
    }
    dependencies {
        classpath(ClassPath.gradle)
        classpath(ClassPath.kotlin)
        classpath(ClassPath.bintray)
        classpath(ClassPath.hilt)
    }
}
allprojects {
    repositories {
        maven("https://jitpack.io")
        jcenter()
        google()
    }
}