plugins {
    id("com.android.application")
    id("kotlin-android")
    id("dagger.hilt.android.plugin")
    id("kotlin-kapt")
    //kotlin("plugin.parcelize")
    //kotlin("plugin.serialization")
}


android {
    compileSdk = AndroidConfig.compileSdkVersion
    defaultConfig {
        applicationId = AndroidConfig.applicationId
        minSdk = AndroidConfig.minSdkVersion
        targetSdk = AndroidConfig.targetSdkVersion
        val versionProps = loadVersionProperties()
        versionCode = versionProps.versionCode
        versionName = versionProps.versionName
        multiDexEnabled = true
        vectorDrawables.useSupportLibrary = true
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    buildFeatures {
        aidl = false
        renderScript = false
        shaders = false
        dataBinding = false
        viewBinding = true
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kapt {
        correctErrorTypes = true
    }
}


dependencies {
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))
    implementation(project(":common"))
    implementation(project(":arch"))
    implementation(project(":app:ui"))
    implementation(project(":app:screens:calendar"))

    debugImplementation(libs.leakcanary)

    implementation(libs.kotlin.stdlib)
    implementation(libs.androidxcore)
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.constraintlayout)

    implementation(libs.lifecycle.runtime)
    implementation(libs.lifecycle.viewmodel)
    implementation(libs.lifecycle.livedata)
    implementation(libs.lifecycle.savedstate)
    implementation(libs.lifecycle.ext)

    implementation(libs.datetime)
    implementation(libs.fragment)
    implementation(libs.activity)
    implementation(libs.preference)

    //Coroutines
    implementation(libs.coroutines.core)
    implementation(libs.coroutines.android)
    implementation(libs.serialization.core)
    implementation(libs.serialization.json)

    implementation(libs.binaryprefs)
    implementation(libs.coil)

    implementation(libs.viewbindingpropertydelegate)
    implementation(libs.cicerone)

    implementation(libs.hiltandroid)
    kapt(libs.hiltcompiler)
//    kapt("androidx.hilt:hilt-compiler:1.0.0")

    implementation(libs.permissiondispatcher)
    implementation(libs.kiel)

    implementation(libs.sesame.property)
    implementation(libs.sesame.localized)


}
