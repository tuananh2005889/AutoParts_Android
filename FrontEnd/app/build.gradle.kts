import org.gradle.kotlin.dsl.implementation

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    //serialization
    id("org.jetbrains.kotlin.plugin.serialization") version "1.8.10"
    //hilt
//    id("kotlin-kapt")
//    id("com.google.dagger.hilt.android")

    id("com.google.dagger.hilt.android") // Plugin Hilt
    kotlin("kapt") // Kích hoạt kapt (Kotlin Annotation Processing)
}

android {
    namespace = "com.example.frontend"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.frontend"
        minSdk = 35
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
    }

    packagingOptions {
        exclude("META-INF/DEPENDENCIES")
        exclude("META-INF/LICENSE")
        exclude("META-INF/LICENSE.txt")
        exclude("META-INF/NOTICE")
        exclude("META-INF/NOTICE.txt")
        exclude("META-INF/ASL2.0")
        exclude("META-INF/services/java.sql.Driver") // Fix lỗi MySQL Connector
    }
}

dependencies {

    //gson converter
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    //retrofit
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    //serialization
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.5.1")
    // Kotlin serialization Converter - replace Gson
    implementation("com.jakewharton.retrofit:retrofit2-kotlinx-serialization-converter:1.0.0")
    implementation("com.squareup.okhttp3:okhttp:4.11.0")

    // Hilt
    implementation("com.google.dagger:hilt-android:2.51.1")
    implementation(libs.generativeai)
    kapt("com.google.dagger:hilt-android-compiler:2.51.1")
    // Hilt cho ViewModel
    implementation("androidx.hilt:hilt-navigation-compose:1.1.0")

    implementation("io.coil-kt:coil-compose:2.2.2")

    //google font
    implementation("androidx.compose.ui:ui-text-google-fonts:1.7.8")
    // Navigation
    implementation("androidx.navigation:navigation-compose:2.7.7")
    // Material
    implementation(libs.androidx.material3)
    implementation("androidx.compose.material:material:1.6.0")
    // Core
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    // Firebase
    implementation("com.google.firebase:firebase-firestore-ktx:25.1.2")
    implementation("com.squareup.okhttp3:okhttp:4.9.3")
    // Lib used to convert json to kotlin object
    implementation("com.google.code.gson:gson:2.10.1")

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}