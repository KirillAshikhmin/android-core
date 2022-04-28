plugins {
    id("com.android.library")
    id("kotlin-android")
    id("dagger.hilt.android.plugin")
    id("kotlin-kapt")
}

android {
    compileSdk = AndroidConfig.compileSdkVersion

    defaultConfig {
        minSdk = AndroidConfig.minSdkVersion
        targetSdk = AndroidConfig.targetSdkVersion
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    buildFeatures {
        aidl = false
        renderScript = false
        shaders = false
        dataBinding = false
        viewBinding = true
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {
    implementation(project(":common"))
    implementation(project(":arch"))
    implementation(project(":app:ui"))
    implementation(project(":app:views:verticalweekcalendar"))
    implementation(project(":app:views:SingleRowCalendar"))

    implementation(libs.kotlin.stdlib)
    implementation(libs.androidxcore)
    implementation(libs.appcompat)
    implementation(libs.material)

    implementation(libs.datetime)

    implementation(libs.constraintlayout)
    implementation(libs.lifecycle.runtime)
    implementation(libs.lifecycle.viewmodel)
    implementation(libs.lifecycle.livedata)
    implementation(libs.lifecycle.savedstate)
    implementation(libs.lifecycle.ext)

    implementation(libs.fragment)
    implementation(libs.activity)

    implementation(libs.viewbindingpropertydelegate)
    implementation(libs.cicerone)

    implementation(libs.sesame.property)
    implementation(libs.sesame.localized)

    implementation(libs.hiltandroid)
    kapt(libs.hiltcompiler)
//    kapt("androidx.hilt:hilt-compiler:1.0.0")
}
