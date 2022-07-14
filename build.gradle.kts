buildscript {
    val kotlinVersion: String by project

    repositories {
        mavenCentral()
        google()
    }
    dependencies {
        classpath("com.android.tools.build:gradle:7.2.1")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlinVersion")
        classpath("com.github.dcendents:android-maven-gradle-plugin:2.0")
    }
}

plugins {
    kotlin("jvm")
}

repositories {
    mavenCentral()
}

allprojects {
    repositories {
        mavenCentral()
        google()
        maven("https://jitpack.io")
        jcenter()
    }
}