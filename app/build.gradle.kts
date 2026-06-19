import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.android.application)
    // kotlin.android NOT listed — kotlin.compose applies it transitively.
    // Declaring both raises "Cannot add extension with name 'kotlin'".
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.ksp)
    alias(libs.plugins.hilt)
}

android {
    namespace = "ai.kwotly.companion"
    compileSdk {
        version = release(36) {
            minorApiLevel = 1
        }
    }

    defaultConfig {
        applicationId = "ai.kwotly.companion"
        minSdk = 35
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        // Backend base URL exposed to the app via BuildConfig — swap in
        // local builds via local.properties (see README) without touching
        // the committed source. Default points at the live Kwotly API.
        buildConfigField("String", "KWOTLY_BASE_URL", "\"https://app.kwotly.ai/\"")
    }

    buildTypes {
        debug {
            // Network logging on debug only — never leak request/response
            // payloads in a release build, that's where JWT lives.
            buildConfigField("boolean", "ENABLE_NETWORK_LOGGING", "true")
        }
        release {
            optimization {
                enable = false
            }
            buildConfigField("boolean", "ENABLE_NETWORK_LOGGING", "false")
        }
    }

    // AGP 9 + Kotlin 2.2 → JDK 17 baseline. The scaffolded JDK 11 was
    // too old for the modern toolchain; left here it would surface as
    // "Inconsistent JVM-target compatibility" warnings on each build.
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    buildFeatures {
        compose = true
        // BuildConfig fields (KWOTLY_BASE_URL, ENABLE_NETWORK_LOGGING)
        // require this to be on — off by default in AGP 9.
        buildConfig = true
    }
}

// AGP 9 / Kotlin 2.2 removed the `android { kotlinOptions }` DSL.
// JVM target now lives in the top-level Kotlin compiler options.
kotlin {
    compilerOptions {
        jvmTarget = JvmTarget.JVM_17
    }
}

dependencies {
    // ---- AndroidX foundation ----
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.lifecycle.runtime.compose)
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation(libs.androidx.activity.compose)

    // ---- Compose (BOM-versioned) ----
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.material.icons.extended)

    // ---- Navigation ----
    implementation(libs.androidx.navigation.compose)

    // ---- Hilt DI ----
    implementation(libs.hilt.android)
    implementation(libs.androidx.hilt.navigation.compose)
    ksp(libs.hilt.compiler)

    // ---- HTTP + JSON ----
    implementation(libs.retrofit)
    implementation(libs.retrofit.kotlinx.serialization.converter)
    implementation(libs.okhttp)
    implementation(libs.okhttp.logging.interceptor)
    implementation(libs.kotlinx.serialization.json)

    // ---- DataStore (JWT + preferences) ----
    implementation(libs.androidx.datastore.preferences)

    // ---- Coroutines ----
    implementation(libs.kotlinx.coroutines.android)

    // ---- Coil 3 (Compose-native image loader, for later screens) ----
    implementation(libs.coil.compose)
    implementation(libs.coil.network.okhttp)

    // ---- Tests ----
    testImplementation(libs.junit)
    testImplementation(libs.kotlinx.coroutines.test)
    testImplementation(libs.turbine)
    testImplementation(libs.mockk)

    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)

    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.test.manifest)
}
