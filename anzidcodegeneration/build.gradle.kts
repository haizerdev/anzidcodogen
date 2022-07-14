val kspVersion: String by project
val kotlinVersion: String by project
val versionLib: String by project

plugins {
    id("kotlin")
    id("maven-publish")
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

    implementation("com.squareup:kotlinpoet:1.11.0")
    implementation("com.squareup:kotlinpoet-ksp:1.11.0")

    // Kotlin
    implementation ("org.jetbrains.kotlin:kotlin-stdlib:$kotlinVersion")
    implementation("com.google.devtools.ksp:symbol-processing-api:$kspVersion")
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
    kotlinOptions.freeCompilerArgs += "-Xopt-in=com.squareup.kotlinpoet.ksp.KotlinPoetKspPreview"
}