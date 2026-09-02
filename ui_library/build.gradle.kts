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

    compileSdk = 37

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
                "proguard-rules.pro",
            )
        }
    }

    buildFeatures {
        compose = true
        // No BuildConfig/resValues: nothing in the library reads them, and a library BuildConfig
        // lands on every consumer's classpath.
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    testOptions {
        unitTests {
            // Model classes are @Parcelize; without this the android.jar stubs throw.
            isReturnDefaultValues = true
        }
    }

    packaging {
        resources.excludes +=
            setOf(
                "/META-INF/AL2.0",
                "/META-INF/LGPL2.1",
            )
    }

    publishing {
        singleVariant("release") {
            withSourcesJar()
            // No withJavadocJar(): without Dokka it publishes an empty jar.
        }
    }
}

kotlin {
    jvmToolchain(17)
}

dependencies {
    // ---------- Implementation Dependencies ----------
    // Every dependency here is forced on every consumer. Anything not referenced from
    // src/main stays out (activity-compose, navigation, runtime-livedata were removed for that reason).
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    // api: BaseDataLoadingViewModel extends ViewModel, so consumers need the type on their
    // compile classpath. It used to arrive transitively via activity-compose.
    api(libs.androidx.lifecycle.viewmodel.ktx)

    // Compose UI
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    // Tooling runtime is debug-only: shipping it forces the Compose inspector onto every
    // consumer's release build. The preview annotations stay on the main classpath.
    debugImplementation(libs.androidx.ui.tooling)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.compose.material.icons.extended)
    implementation(libs.androidx.compose.foundation)

    // ---------- Test Dependencies ----------
    testImplementation(libs.junit)
    testImplementation(libs.androidx.core.testing)
    testImplementation(libs.kotlin.test)
    testImplementation(libs.kotlinx.coroutines.test)

    // ---------- Android Instrumented Test ----------
    add("androidTestImplementation", platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(libs.androidx.ui.test.junit4)
}

afterEvaluate {
    publishing {
        publications {
            create<MavenPublication>("release") {
                groupId = "com.tnm.android.core"
                artifactId = "ui-library"
                version = "3.0.0"
                from(components["release"])
            }
        }

        repositories {
            maven {
                name = "GitHub"
                url = uri("https://maven.pkg.github.com/mrkivan/android-compose-ui-lib")
                credentials {
                    username = System.getenv("GITHUB_ACTOR")
                        ?: (project.findProperty("gpr.user") as? String)
                        ?: ""
                    password = System.getenv("GITHUB_TOKEN")
                        ?: (project.findProperty("gpr.key") as? String)
                        ?: ""
                }
            }
        }
    }
}
