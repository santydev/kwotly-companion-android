// Top-level build file. Plugins are declared here with apply=false so
// each module can opt in via the `alias(...)` block in its own
// build.gradle.kts. Versions all live in gradle/libs.versions.toml.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.compose) apply false
    alias(libs.plugins.kotlin.serialization) apply false
    alias(libs.plugins.ksp) apply false
    alias(libs.plugins.hilt) apply false
}
