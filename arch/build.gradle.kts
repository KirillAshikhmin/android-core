plugins {
    id("com.android.library")
    id("kotlin-android")
    kotlin("kapt")
}

android {
    compileSdk = AndroidConfig.compileSdkVersion

    defaultConfig {
        minSdk = AndroidConfig.minSdkVersion
        targetSdk = AndroidConfig.targetSdkVersion
    }

    buildTypes {
        getByName("release"){
            isMinifyEnabled = false
            setProguardFiles(listOf(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro"))
        }
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
    implementation(project(":app:ui"))

    implementation(libs.kotlin.stdlib)
    implementation(libs.androidxcore)
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.constraintlayout)

    implementation(libs.asynclayoutinflater)
    implementation(libs.viewbindingpropertydelegate)
    implementation(libs.cicerone)
    implementation(libs.binaryprefs)

    implementation(libs.sesame.property)
    implementation(libs.sesame.localized)

    implementation(libs.lifecycle.viewmodel)
    implementation(libs.lifecycle.livedata)
    implementation(libs.lifecycle.savedstate)

    implementation(libs.hiltandroid)
    kapt(libs.hiltcompiler)

    implementation(libs.timber)
    implementation(libs.logger)

    implementation(libs.coroutines.core)
    implementation(libs.coroutines.android)
    implementation(libs.serialization.core)
    implementation(libs.serialization.json)

    implementation(libs.datetime)
    implementation(libs.skeletonlayout)
}
