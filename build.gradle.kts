plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.compose) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.kotlin.parcelize) apply false
    alias(libs.plugins.spotless)
}

// Formatting is enforced in one place for every module. `./gradlew spotlessApply` rewrites,
// `./gradlew spotlessCheck` verifies (and runs as part of `check`).
subprojects {
    apply(plugin = "com.diffplug.spotless")

    extensions.configure<com.diffplug.gradle.spotless.SpotlessExtension> {
        kotlin {
            target("src/**/*.kt")
            targetExclude("**/build/**")
            ktlint(libs.versions.ktlint.get())
                .editorConfigOverride(
                    mapOf(
                        // Compose composables are PascalCase by convention; ktlint's default
                        // function-naming rule would flag every one of them.
                        "ktlint_function_naming_ignore_when_annotated_with" to "Composable",
                        "ktlint_standard_function-naming" to "disabled",
                        "ktlint_standard_property-naming" to "disabled",
                        "max_line_length" to "120",
                    )
                )
            trimTrailingWhitespace()
            endWithNewline()
        }

        kotlinGradle {
            target("*.gradle.kts")
            ktlint(libs.versions.ktlint.get())
        }

        format("xml") {
            target("src/**/*.xml")
            targetExclude("**/build/**")
            trimTrailingWhitespace()
            endWithNewline()
        }
    }
}
