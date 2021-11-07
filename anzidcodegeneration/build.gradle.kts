val kspVersion: String by project
val kotlinVersion: String by project

plugins {
    id("kotlin")
    id("kotlin-kapt")
    id("com.github.dcendents.android-maven")
    kotlin("jvm")
}

group="com.github.anzid"
version = "0.5-SNAPSHOT"

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
    // configuration generator for service providers
    implementation ("com.google.auto.service:auto-service:1.0") {
        exclude(group = "com.google.guava")
    }

    implementation("com.google.devtools.ksp:symbol-processing-api:1.5.30-1.0.0-beta09")

    kapt("com.google.auto.service:auto-service:1.0")
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
    kotlinOptions.freeCompilerArgs += "-Xopt-in=com.squareup.kotlinpoet.ksp.KotlinPoetKspPreview"
}