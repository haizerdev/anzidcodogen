val kotlinVersion: String by project
val versionLib: String by project

plugins {
    id("kotlin")
    id("com.github.dcendents.android-maven")
}

group="com.github.anzid"
version = versionLib

dependencies {
    // Kotlin
    implementation("org.jetbrains.kotlin:kotlin-stdlib:$kotlinVersion")
    implementation ("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.5.0")
}

repositories {
    mavenCentral()
}