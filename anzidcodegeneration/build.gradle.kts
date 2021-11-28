val kspVersion: String by project
val kotlinVersion: String by project
val versionLib: String by project

plugins {
    id("kotlin")
    id("com.github.dcendents.android-maven")
    kotlin("jvm")
}

group="com.github.anzid"
version = versionLib

repositories {
    mavenCentral()
}

sourceSets.main {
    java.srcDirs("src/main/java")
}

dependencies {
    implementation(project(":anzidannotation"))
    compileOnly(project(":anzidannotation"))

    implementation("com.squareup:kotlinpoet:1.10.1")
    implementation("com.squareup:kotlinpoet-ksp:1.10.1")

    // Kotlin
    implementation ("org.jetbrains.kotlin:kotlin-stdlib:$kotlinVersion")
    implementation ("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.5.0")
    implementation("com.google.devtools.ksp:symbol-processing-api:1.5.30-1.0.0-beta09")
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
    kotlinOptions.freeCompilerArgs += "-Xopt-in=com.squareup.kotlinpoet.ksp.KotlinPoetKspPreview"
}