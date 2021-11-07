val kotlinVersion: String by project

plugins {
    id("kotlin")
    id("com.github.dcendents.android-maven")
}

group="com.github.anzid"
version = "0.5-SNAPSHOT"

dependencies {
    // Kotlin
    implementation("org.jetbrains.kotlin:kotlin-stdlib:$kotlinVersion")
}

//sourceCompatibility = "7"
//targetCompatibility = "7"

repositories {
    mavenCentral()
}
//compileKotlin {
//    kotlinOptions {
//        jvmTarget = "1.8"
//    }
//}
//compileTestKotlin {
//    kotlinOptions {
//        jvmTarget = "1.8"
//    }
//}