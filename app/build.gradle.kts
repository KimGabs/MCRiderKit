plugins {
        alias(libs.plugins.android.application)
        alias(libs.plugins.kotlin.android)
        alias(libs.plugins.google.gms.google.services)
        alias(libs.plugins.compose.compiler)
    }


    android {
        namespace = "com.example.mcriderkit"
        compileSdk = 36

        defaultConfig {
            applicationId = "com.example.mcriderkit"
            minSdk = 24
            // targetSdk should match compileSdk
            targetSdk = 36 // Changed from 35 to 36
            versionCode = 1
            versionName = "1.0"

            testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
            vectorDrawables { // Add this block for vector drawables
                useSupportLibrary = true
            }
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
        compileOptions {
            sourceCompatibility = JavaVersion.VERSION_21
            targetCompatibility = JavaVersion.VERSION_21
        }
        kotlinOptions {
            jvmTarget = "21"
        }
        buildFeatures {
            // viewBinding is not needed for a pure Jetpack Compose project
            // viewBinding = true
            compose = true
        }
        packaging { // Add this block
            resources {
                excludes += "/META-INF/{AL2.0,LGPL2.1}"
            }
        }
    }

dependencies {
    implementation("androidx.core:core-splashscreen:1.0.1")
    // Video Player
    implementation("androidx.media3:media3-exoplayer:1.2.0")
    implementation("androidx.media3:media3-ui:1.2.0")

    implementation("nl.dionsegijn:konfetti-compose:2.0.4")

    // Jetpack Compose Integration
    implementation("androidx.navigation:navigation-compose:2.9.7")
    implementation("androidx.navigation:navigation-fragment:2.9.7")
    implementation("androidx.navigation:navigation-ui:2.9.7")

    implementation(platform("com.google.firebase:firebase-bom:34.9.0"))
    implementation ("com.google.firebase:firebase-firestore:26.1.0")

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.firebase.database)
    implementation(libs.firebase.auth)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    implementation("io.coil-kt.coil3:coil-compose:3.3.0")
    implementation("io.coil-kt.coil3:coil-network-okhttp:3.3.0")
    implementation("com.google.code.gson:gson:2.10.1")
}