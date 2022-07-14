val kotlinVersion: String by project
val versionLib: String by project

plugins {
    id("kotlin")
    id ("maven-publish")
}

group="com.github.anzid"
version = versionLib

dependencies {
    // Kotlin
    implementation("org.jetbrains.kotlin:kotlin-stdlib:$kotlinVersion")
    implementation ("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.1")
}

repositories {
    mavenCentral()
}