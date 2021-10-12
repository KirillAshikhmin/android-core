plugins {
    id("kotlin")
    `java-library`
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}


dependencies {
    implementation("org.jetbrains.kotlin:kotlin-stdlib:1.5.31")
}
