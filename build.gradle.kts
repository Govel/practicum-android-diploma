// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    id("com.android.application") version "8.12.0" apply false
    id("org.jetbrains.kotlin.android") version "2.3.20" apply false
    id("convention.detekt")
    id("com.google.devtools.ksp") version "2.3.6" apply false
    alias(libs.plugins.kotlin.compose) apply false
}
