plugins {
    id("com.android.library")
    id("kotlin-android")
    id("dagger.hilt.android.plugin")
    kotlin("kapt")
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
    implementation(libs.kotlin.stdlib)
    implementation(libs.androidxcore)
    implementation(libs.appcompat)
    implementation(libs.material)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso)

    implementation(libs.coroutines.core)
    implementation(libs.coroutines.android)
    implementation(libs.serialization.core)
    implementation(libs.serialization.json)

    implementation(libs.retrofit.serialization)
    implementation(libs.retrofit2)
    implementation(libs.okhttp)
    implementation(libs.hiltandroid)
    kapt(libs.hiltcompiler)
//    kapt("androidx.hilt:hilt-compiler:1.0.0")
}
