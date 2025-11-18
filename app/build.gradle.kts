plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
}

// Load environment variables from local.properties
fun getLocalProperty(key: String): String {
    val localPropertiesFile = rootProject.file("local.properties")
    return if (localPropertiesFile.exists()) {
        localPropertiesFile.readLines()
            .find { it.startsWith("$key=") }
            ?.substringAfter("=") ?: ""
    } else {
        ""
    }
}

android {
    namespace = "com.geniusjun.lotto"
    compileSdk {
        version = release(36)
    }

    defaultConfig {
        applicationId = "com.geniusjun.lotto"
        minSdk = 24
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        
        val googleClientId = getLocalProperty("GOOGLE_CLIENT_ID")
        if (googleClientId.isEmpty()) {
            throw GradleException("GOOGLE_CLIENT_ID is missing in local.properties")
        }
        
        buildConfigField("String", "GOOGLE_CLIENT_ID", "\"$googleClientId\"")

        val baseUrl = getLocalProperty("BASE_URL")
        if (baseUrl.isEmpty()) {
            throw GradleException("BASE_URL is missing in local.properties")
        }
        buildConfigField("String", "BASE_URL", "\"$baseUrl\"")

    }

    buildTypes {
        debug {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
        release {
            isMinifyEnabled = false
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }
}

kotlin {
    compilerOptions {
        jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_17)
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.test.manifest)
    
    // Google Sign-In
    implementation(libs.google.play.services.auth)
    
    // Retrofit & OkHttp
    implementation(libs.retrofit)
    implementation(libs.retrofit.gson)
    implementation(libs.okhttp)
    implementation(libs.okhttp.logging)
}