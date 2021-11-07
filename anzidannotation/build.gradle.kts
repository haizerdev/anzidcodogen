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
}

repositories {
    mavenCentral()
}