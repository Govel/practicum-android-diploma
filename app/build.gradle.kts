import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("ru.practicum.android.diploma.plugins.developproperties")
    id("com.google.devtools.ksp")
    id("kotlin-parcelize")
    alias(libs.plugins.kotlin.compose)
}

android {
    namespace = "ru.practicum.android.diploma"
    compileSdk = libs.versions.compileSdk.get().toInt()

    defaultConfig {
        applicationId = "ru.practicum.android.diploma"
        minSdk = libs.versions.minSdk.get().toInt()
        targetSdk = libs.versions.targetSdk.get().toInt()
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        buildConfigField(type = "String", name = "API_ACCESS_TOKEN", value = "\"${developProperties.apiAccessToken}\"")
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

    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.10"
    }

    ksp {
        arg("room.schemaLocation", "$projectDir/schemas")
    }

    buildFeatures {
        buildConfig = true
        compose = true
    }
}

kotlin {
    compilerOptions {
        jvmTarget = JvmTarget.JVM_17
    }
}

dependencies {

    implementation(libs.activity.ktx)
    implementation(libs.appcompat)
    implementation(libs.core)
    implementation(libs.koin.android)
    implementation(libs.ui)

    // UI layer libraries
    implementation(libs.constraintlayout)
    implementation(libs.material)

    testImplementation(libs.junit4)
    androidTestImplementation(libs.junit.ext)
    androidTestImplementation(libs.espresso.core)

    // Retrofit and Gson
    implementation(libs.gson)
    implementation(libs.converter.gson)
    implementation(libs.retrofit)

    // Room (Database)
    implementation(libs.room.runtime)
    implementation(libs.room.ktx)
    ksp(libs.room.compiler)

    // Glade and Coil (Image)
    implementation(libs.glide)
    ksp(libs.glide.compiler)
    implementation(libs.coil.compose)
    implementation(libs.coil.network.okhttp)

    // Lifecycle
    implementation(libs.androidx.lifecycle.livedata.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)

    // Coroutines
    implementation(libs.kotlinx.coroutines.android)

    // Koin
    implementation(libs.koin.core)
    implementation(libs.koin.android.v343)
    implementation(libs.koin.androidx.compose)

    // Compose
    implementation(libs.activity.compose)
    implementation(libs.androidx.foundation.layout)
    implementation(libs.androidx.junit.ktx)
    implementation(libs.foundation)
    implementation(libs.foundation.layout)
    implementation(libs.material3)
    implementation(libs.navigation.compose)
    implementation(libs.ui.graphics)

    // Preview and test Compose
    implementation(libs.ui.tooling.preview)
    debugImplementation(libs.ui.tooling)

    // Peko
    implementation(libs.peko)
}
