plugins {
    alias(libs.plugins.android.application)  // Android uygulama plugin
    alias(libs.plugins.kotlin.android)       // Kotlin plugin
    alias(libs.plugins.kotlin.compose)       // Kotlin Compose plugin
}

android {
    namespace = "com.example.mapapp"
    compileSdk = 35
    // Android SDK sürümü

    defaultConfig {
        applicationId = "com.example.mapapp"  // Uygulama ID'si
        minSdk = 24  // Minimum SDK sürümü
        targetSdk = 35 // Hedef SDK sürümü
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
        sourceCompatibility = JavaVersion.VERSION_11  // Java sürümü
        targetCompatibility = JavaVersion.VERSION_11
    }

    kotlinOptions {
        jvmTarget = "11"  // Kotlin JVM hedef sürümü
    }

    buildFeatures {
        compose = true  // Compose desteğini etkinleştir
    }
}

dependencies {
    implementation(libs.osmdroid.android.v6110)  // OSMDroid Android sürümü (Doğru sürümü kullan)
    implementation(libs.commons.lang3)  // OSMDroid bağımlılığı
    implementation(libs.osmdroid.android)  // OSMDroid android bağımlılığı
    implementation(libs.androidx.core.ktx)  // AndroidX Core KTX
    implementation(libs.androidx.lifecycle.runtime.ktx)  // AndroidX Lifecycle runtime KTX
    implementation(libs.androidx.activity.compose)  // AndroidX Activity Compose
    implementation(platform(libs.androidx.compose.bom))  // Compose BOM
    implementation(libs.androidx.ui)  // AndroidX UI
    implementation(libs.androidx.ui.graphics)  // AndroidX UI Graphics
    implementation(libs.androidx.ui.tooling.preview)  // AndroidX UI Tooling Preview
    implementation(libs.androidx.material3)
    implementation(libs.androidx.appcompat)  // AndroidX Material3
    testImplementation(libs.junit)  // JUnit test bağımlılığı
    androidTestImplementation(libs.androidx.junit)  // AndroidX JUnit testi
    androidTestImplementation(libs.androidx.espresso.core)  // AndroidX Espresso testi
    androidTestImplementation(platform(libs.androidx.compose.bom))  // AndroidX Compose BOM
    androidTestImplementation(libs.androidx.ui.test.junit4)  // AndroidX UI test JUnit4
    debugImplementation(libs.androidx.ui.tooling)  // AndroidX UI Tooling Debug
    debugImplementation(libs.androidx.ui.test.manifest)  // AndroidX UI Test Manifest
    implementation(libs.retrofit)
    implementation("com.journeyapps:zxing-android-embedded:4.3.0")  // ZXing Barcode Scanner

    implementation("androidx.activity:activity-compose:1.3.1")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("com.squareup.retrofit2:adapter-rxjava2:2.9.0")
    implementation("com.jakewharton.retrofit:retrofit2-kotlin-coroutines-adapter:0.9.2")  // Coroutines adapter
    // Overpasser library (0.2.2)
    implementation("hu.supercluster:overpasser:0.2.2")
    // Retrofit Adapter for Overpasser (for version 2.1.0)
    implementation("hu.supercluster:overpasser-retrofit-adapter:0.2.2")
}
