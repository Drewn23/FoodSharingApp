plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.ksp)
    kotlin("plugin.serialization")
}

val roomVersion = "2.8.5"

android {
    namespace = "com.example.foodsharingapp"
    compileSdk = 37

    defaultConfig {
        applicationId = "com.example.foodsharingapp"
        minSdk = 24
        targetSdk = 37
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            optimization {
                enable = false
            }
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    buildFeatures {
        compose = true
    }
}

val lifecycleVersion = "2.11.0" //Version can change based on latest release
//Can check the lastest version here:
//https://developer.android.com/jetpack/androidx/releases/lifecycle
val navVersion       = "2.10.2" //Version can change based on latest release
//Can check the lastest version here:
//https://developer.android.com/jetpack/androidx/releases/navigation

val iconVersion       = "1.6.0" //Version can change based on latest release
//Can check the lastest version here:
//https://developer.android.com/reference/kotlin/androidx/compose/material/icons/package-summary

//verify at https://maven.google.com/web/index.html

dependencies {
    //Both implementation change based on lastest release
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:${lifecycleVersion}")
    implementation("androidx.navigation:navigation-compose:${navVersion}")
    implementation("androidx.compose.material:material-icons-core:${iconVersion}")

    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.navigation.compose)
    implementation("androidx.room:room-runtime:$roomVersion")
    implementation("androidx.room:room-ktx:$roomVersion")
    ksp("androidx.room:room-compiler:$roomVersion")
    testImplementation(libs.junit)
    implementation(libs.androidx.datastore.preferences)
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.8.0")
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(libs.androidx.junit)
    debugImplementation(libs.androidx.compose.ui.test.manifest)
    debugImplementation(libs.androidx.compose.ui.tooling)
}