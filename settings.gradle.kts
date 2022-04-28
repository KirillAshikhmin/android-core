enableFeaturePreview("VERSION_CATALOGS")
rootProject.name = "Personal Organiser"
rootProject.buildFileName = "build.gradle.kts"
include(":arch")
include(":common")
include(":repository")
include(":app")
include(":app:views:verticalweekcalendar")
include(":app:views:SingleRowCalendar")
include(":app:screens:calendar")
include(":app:screens:empty")
include(":app:ui")
include(":dal")
include(":app:main")
include(":app:tech")
include(":app:tech:sounds")
include(":app:tech:files")

@Suppress("UnstableApiUsage")
dependencyResolutionManagement {
    versionCatalogs {
        create("libs") {
            version("firebase_version", "29.0.4")
            version("hilt_version", "2.40.5")
            version("kotlin_version", "1.6.10")
            version("coroutines_version", "1.6.1")
            version("serialization_version", "1.3.2")
            version("sesame_version", "1.4.0-beta1")
            version("lifecycle_version", "2.4.1")
            version("room_version", "2.4.1")


            alias("junit").to("junit", "junit").version("4.13.2")
            alias("androidx.junit").to("test.ext", "junit").version("1.1.3")
            alias("androidx.espresso").to("test.espresso", "espresso-core").version("3.4.0")

            alias("kotlin.stdlib").to("org.jetbrains.kotlin", "kotlin-stdlib").versionRef("kotlin_version")
            alias("kotlin.jdk7").to("org.jetbrains.kotlin", "kotlin-stdlib-jdk7").versionRef("kotlin_version")
            alias("kotlin.jdk8").to("org.jetbrains.kotlin", "kotlin-stdlib-jdk8").versionRef("kotlin_version")

            alias("firebase.bom").to("com.google.firebase", "firebase-bom").versionRef("firebase_version")
            alias("firebase.analytics").to("com.google.firebase", "firebase-analytics-ktx").withoutVersion()
            alias("firebase.crashlytics").to("com.google.firebase", "firebase-crashlytics-ktx").withoutVersion()

            alias("hiltandroid").to("com.google.dagger", "hilt-android").versionRef("hilt_version")
            alias("hiltcompiler").to("com.google.dagger", "hilt-compiler").versionRef("hilt_version")

            alias("coroutines-core").to("org.jetbrains.kotlinx", "kotlinx-coroutines-core").versionRef("coroutines_version")
            alias("coroutines-android").to("org.jetbrains.kotlinx", "kotlinx-coroutines-android").versionRef("coroutines_version")

            alias("serialization-core").to("org.jetbrains.kotlinx", "kotlinx-serialization-core").versionRef("serialization_version")
            alias("serialization-json").to("org.jetbrains.kotlinx", "kotlinx-serialization-json").versionRef("serialization_version")

            alias("androidxcore").to("androidx.core", "core-ktx").version("1.7.0")
            alias("material").to("com.google.android.material", "material").version("1.5.0")
            alias("appcompat").to("androidx.appcompat", "appcompat").version("1.4.1")
            alias("fragment").to("androidx.fragment", "fragment-ktx").version("1.4.1")
            alias("activity").to("androidx.activity", "activity-ktx").version("1.4.0")
            alias("preference").to("androidx.preference", "preference-ktx").version("1.2.0")
            alias("asynclayoutinflater").to("androidx.asynclayoutinflater", "asynclayoutinflater").version("1.0.0")

            alias("constraintlayout").to("androidx.constraintlayout", "constraintlayout").version("2.1.3")
            alias("lifecycle.runtime").to("androidx.lifecycle", "lifecycle-runtime-ktx").versionRef("lifecycle_version")
            alias("lifecycle.viewmodel").to("androidx.lifecycle", "lifecycle-viewmodel-ktx").versionRef("lifecycle_version")
            alias("lifecycle.livedata").to("androidx.lifecycle", "lifecycle-livedata-ktx").versionRef("lifecycle_version")
            alias("lifecycle.savedstate").to("androidx.lifecycle", "lifecycle-viewmodel-savedstate").versionRef("lifecycle_version")
            alias("lifecycle.ext").to("androidx.lifecycle", "lifecycle-extensions").version("2.2.0")

            alias("leakcanary").to("com.squareup.leakcanary", "leakcanary-android").version("2.8.1")

            alias("timber").to("com.jakewharton.timber", "timber").version("5.0.1")
            alias("cicerone").to("com.github.terrakok", "cicerone").version("7.1")
            alias("viewbindingpropertydelegate").to("com.kirich1409.viewbindingpropertydelegate","viewbindingpropertydelegate").version("1.4.2")
            alias("binaryprefs").to("com.github.yandextaxitech", "binaryprefs").version("1.0.1")
            alias("datetime").to("org.jetbrains.kotlinx", "kotlinx-datetime").version("0.3.2")

            alias("retrofit2").to("com.squareup.retrofit2", "retrofit").version("2.9.0")
            alias("retrofit.serialization").to("com.jakewharton.retrofit","retrofit2-kotlinx-serialization-converter").version("0.8.0")
            alias("okhttp").to("com.squareup.okhttp3", "okhttp").version("5.0.0-alpha.3")

            alias("kiel").to("me.ibrahimyilmaz", "kiel").version("1.2.1")
            alias("coil").to("io.coil-kt", "coil").version("1.4.0")
            alias("logger").to("com.orhanobut", "logger").version("2.2.0")

            alias("skeletonlayout").to("com.faltenreich", "skeletonlayout").version("4.0.0")

            alias("sesame.property").to("com.github.aartikov", "sesame-property").versionRef("sesame_version")
            alias("sesame.localized").to("com.github.aartikov", "sesame-localized-string").versionRef("sesame_version")

            alias("permissiondispatcher").to("com.github.permissions-dispatcher", "permissionsdispatcher-ktx").version("4.8.0")

            alias("recyclerview").to("androidx.recyclerview", "recyclerview").version("1.2.1")
            alias("recyclerview.selection").to("androidx.recyclerview", "recyclerview-selection").version("1.1.0")

            alias("paging").to("androidx.paging", "paging-runtime-ktx").version("3.0.1")
            alias("cardview").to("androidx.cardview", "cardview").version("1.0.0")

            alias("room.runtime").to("androidx.room", "room-runtime").versionRef("room_version")
            alias("room.ktx").to("androidx.room", "room-ktx").versionRef("room_version")
            alias("room.compiler").to("androidx.room", "room-compiler").versionRef("room_version")

            alias("desugar").to("com.android.tools", "desugar_jdk_libs").version("1.1.5")
            alias("dagger2").to("com.google.dagger", "dagger").versionRef("hilt_version")
            alias("hilt").to("androidx.hilt", "hilt-compiler").version("1.0.0")
            alias("kotlinxmetadata").to("org.jetbrains.kotlinx", "kotlinx-metadata-jvm").version("0.4.1")
            alias("segmented").to("com.github.creageek", "segmented").version("1.0.1")
            alias("dexter").to("com.karumi", "dexter").version("6.2.3")
            alias("zxing").to("com.journeyapps", "zxing-android-embedded").version("4.3.0")
            alias("appauth").to("net.openid", "appauth").version("0.11.1")
            alias("pdfview").to("com.dmitryborodin", "pdfview-android").version("1.1.0")
            alias("xfetch2").to("androidx.tonyodev.fetch2", "xfetch2").version("3.1.6")
            alias("logginginterceptor").to("com.squareup.okhttp3","logging-interceptor").version("5.0.0-alpha.3")
            alias("rxjava2").to("io.reactivex.rxjava2", "rxandroid").version("2.1.1")
            alias("documentscanner").to("com.github.mayuce", "AndroidDocumentScanner").version("1.5.3")
            alias("opencv").to("com.quickbirdstudios", "opencv").version("3.4.1")
        }
    }
}
