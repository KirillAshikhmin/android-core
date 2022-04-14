plugins {
    id("com.android.library")
    id("kotlin-android")
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
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso)

    implementation (libs.androidxcore)
    implementation(libs.appcompat)

    implementation(libs.recyclerview)
    implementation(libs.recyclerview.selection)
	
}
