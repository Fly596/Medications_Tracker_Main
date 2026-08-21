plugins {
  alias(libs.plugins.android.application)
  id("com.google.gms.google-services")
  alias(libs.plugins.kotlin.serialization)
  alias(libs.plugins.compose.compiler)
  alias(libs.plugins.ksp)
  alias(libs.plugins.hilt)
}

android {
  namespace = "com.galeria.medicationstracker"
  compileSdk {
    version =
        release(36) {
          minorApiLevel = 1
        }
  }
  defaultConfig {
    applicationId = "com.galeria.medicationstracker"
    minSdk = 31
    targetSdk = 36
    versionCode = 1
    versionName = "1.0"

    testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
  }

  buildTypes {
    debug { isMinifyEnabled = false }
    release {
      isMinifyEnabled = true
      isShrinkResources = true
      proguardFiles(
        getDefaultProguardFile("proguard-android-optimize.txt"),
        "proguard-rules.pro",
      )
    }
  }
  compileOptions {
    sourceCompatibility = JavaVersion.VERSION_21
    targetCompatibility = JavaVersion.VERSION_21
  }
  buildFeatures { compose = true }
}

dependencies {
  implementation(libs.core.ktx)
  implementation(libs.androidx.lifecycle.runtime.ktx)
  implementation(libs.androidx.activity.compose)
  implementation(platform(libs.androidx.compose.bom))
  implementation(libs.androidx.ui)
  implementation(libs.androidx.ui.graphics)
  implementation(libs.androidx.ui.tooling.preview)
  implementation(libs.androidx.material3)
  implementation(libs.com.google.gms.google.services.gradle.plugin)
  implementation(platform(libs.firebase.bom))
  implementation(libs.google.firebase.analytics)
  implementation(libs.firebase.auth)
  implementation(libs.firebase.firestore)
  implementation(libs.google.firebase.storage)
  implementation(libs.coil.compose)
  implementation(libs.lifecycle.viewmodel.compose)
  implementation(libs.play.services.location)
  implementation(libs.androidx.material.icons.extended.android)
  implementation(libs.com.google.dagger.hilt.android.gradle.plugin)
  implementation(libs.androidx.appcompat)
  testImplementation(libs.junit)
  androidTestImplementation(libs.androidx.junit)
  androidTestImplementation(libs.androidx.espresso.core)
  androidTestImplementation(platform(libs.androidx.compose.bom))
  androidTestImplementation(libs.androidx.ui.test.junit4)
  debugImplementation(libs.androidx.ui.tooling)
  debugImplementation(libs.androidx.ui.test.manifest)

  implementation(libs.navigation.compose)
  implementation(libs.kotlinx.serialization.json)

  implementation(libs.hilt.android)
  ksp(libs.hilt.android.compiler)
  // Hilt
  implementation(libs.androidx.hilt.navigation.compose)

  // Room.
  implementation(libs.androidx.room.runtime)
  ksp(libs.androidx.room.compiler)
  // Kotlin Extensions and Coroutines support for Room.
  implementation(libs.androidx.room.ktx)
}

