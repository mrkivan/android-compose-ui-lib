plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.ksp)
    id("maven-publish")
}

@Suppress("UnstableApiUsage")
android {
    namespace = "com.tnm.android.core.ui"
    compileSdk {
        version = release(36)
    }

    defaultConfig {
        minSdk = 26

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlin {
        jvmToolchain(17)
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }
    buildToolsVersion = "36.0.0"

    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.3"
    }
    packaging {
        resources {
            excludes += setOf("/META-INF/AL2.0", "/META-INF/LGPL2.1")
        }
    }
    publishing {
        singleVariant("release") {
            withSourcesJar()
            withJavadocJar()
        }
    }
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
}

afterEvaluate {
    publishing {
        publications {
            create<MavenPublication>("release") {
                groupId = "com.tnm.android.core"
                artifactId = "ui-library"
                version = "1.0.0"
                from(components["release"])
            }
        }

        repositories {
            maven {
                name = "GitHub"
                url = uri("https://maven.pkg.github.com/mrkivan/ui-library")
                credentials {
                    username = System.getenv("GITHUB_ACTOR") ?: "mrkivan"
                    password = System.getenv("GITHUB_TOKEN") ?: ""
                }
            }
        }
    }
}
