import com.android.build.api.dsl.LibraryExtension

plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.kotlin.parcelize)
    alias(libs.plugins.ksp)
    id("maven-publish")
}

extensions.configure<LibraryExtension> {
    namespace = "com.tnm.android.core.ui"

    compileSdk = 36
    buildToolsVersion = "36.0.0"

    defaultConfig {
        minSdk = 26
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
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

    buildFeatures {
        resValues = true
        compose = true
        buildConfig = true
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    packaging {
        resources.excludes += setOf(
            "/META-INF/AL2.0",
            "/META-INF/LGPL2.1"
        )
    }

    publishing {
        singleVariant("release") {
            withSourcesJar()
            withJavadocJar()
        }
    }
}

kotlin {
    jvmToolchain(17)
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
    implementation(libs.androidx.ui.tooling)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.compose.material.icons.extended)
    implementation(libs.androidx.compose.runtime.livedata)
    implementation(libs.androidx.compose.foundation)
    implementation(libs.androidx.navigation.runtime.ktx)
    implementation(libs.androidx.compose.ui.unit)
    implementation(libs.androidx.compose.ui.graphics)

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
                version = "2.4.0"
                from(components["release"])
            }
        }

        repositories {
            maven {
                name = "GitHub"
                url = uri("https://maven.pkg.github.com/mrkivan/android-compose-ui-lib")
                credentials {
                    username = System.getenv("GITHUB_ACTOR")
                    password = System.getenv("GITHUB_TOKEN")
                }
            }
        }
    }
}
