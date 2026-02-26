import com.android.build.api.dsl.ApplicationExtension
import org.gradle.kotlin.dsl.configure

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.kotlin.parcelize)
    alias(libs.plugins.ksp)
    // Hilt Plugin
    alias(libs.plugins.hilt.android)
    alias(libs.plugins.room)
}

// 1. Android Specific Configuration
extensions.configure<ApplicationExtension> {
    namespace = "com.tnm.android.core"

    // Simplified to avoid the "compile target not found" error
    compileSdk = 36
    buildToolsVersion = "36.0.0"

    defaultConfig {
        applicationId = "com.tnm.android.core"
        minSdk = 26
        targetSdk = 36
        versionCode = 41
        versionName = "2.4.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    buildFeatures {
        resValues = true
        compose = true
        buildConfig = true
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
}

kotlin {
    jvmToolchain(17)
}
hilt {
    enableAggregatingTask = false
}
room {
    schemaDirectory("$projectDir/schemas")
}

dependencies {
    // ---------- Implementation Dependencies ----------
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)

    // Compose UI
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.compose.material.icons.extended)
    implementation(libs.androidx.compose.runtime.livedata)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)

    // Room runtime
    implementation(libs.room.runtime)
    implementation(libs.room.ktx)

    // Hilt
    implementation(libs.hilt.android)
    implementation(libs.androidx.hilt.navigation.compose)

    // ---------- KSP (Kotlin Symbol Processing) ----------
    ksp(libs.room.compiler)
    ksp(libs.hilt.compiler)
    ksp(libs.androidx.hilt.compiler)

    // ---------- Test Dependencies ----------
    testImplementation(libs.junit)
    testImplementation(libs.androidx.core.testing)
    testImplementation(libs.kotlin.test)
    testImplementation(libs.kotlinx.coroutines.test)

    // ---------- Android Instrumented Test ----------
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)

    implementation(project(":ui_library"))
}