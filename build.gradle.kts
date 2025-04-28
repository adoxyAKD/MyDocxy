buildscript {
    dependencies {
        classpath ("com.android.tools.build:gradle:8.1.2")  // Update if necessary
        classpath ("com.google.gms:google-services:4.4.2")  // Google services plugin
    }
}

plugins {
    id("com.android.application") version "8.1.2" apply false
    id("org.jetbrains.kotlin.android") version "1.9.0" apply false
    id("com.google.gms.google-services") version "4.4.2" apply false
}