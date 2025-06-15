plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)

    //conexiones firebase
    id("com.google.gms.google-services")

    //compose
    id("org.jetbrains.kotlin.plugin.compose") version "2.0.0"

    //dagger Hilt
    id("kotlin-kapt")
    id("com.google.dagger.hilt.android")
}

android {
    namespace = "com.appgestion.gestionempresa"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.appgestion.gestionempresa"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    kapt {
        correctErrorTypes = true
    }

}


dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.firebase.common.ktx)
    implementation(libs.firebase.auth.ktx)
    implementation(libs.firebase.storage.ktx)
    implementation(libs.firebase.firestore.ktx)
    implementation(libs.androidx.room.runtime.android)
    implementation(libs.androidx.ui.text.google.fonts)
    implementation(libs.androidx.foundation.layout.android)
    implementation(libs.androidx.foundation.layout.android)
    implementation(libs.androidx.animation.core.android)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    //imoprt the BoM para FireBase Plataforma
    implementation(platform("com.google.firebase:firebase-bom:33.12.0"))



    //Declara la dependencia para Firebase Auth library
    implementation ("com.google.firebase:firebase-auth-ktx")

    //navegacion
    implementation("androidx.navigation:navigation-compose:2.8.0")

    //dagger Hilt
    implementation("com.google.dagger:hilt-android:2.51.1")
    kapt("com.google.dagger:hilt-android-compiler:2.51.1")
    implementation("androidx.hilt:hilt-navigation-compose:1.2.0")

    //coroutines
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-play-services:1.6.4")


    implementation("io.coil-kt:coil-compose:2.2.2")

    implementation("androidx.compose.material:material-icons-extended:<tu-versión-compose>")
    implementation ("androidx.compose.material3:material3:1.1.0")

    implementation ("androidx.compose.foundation:foundation:1.4.0")

    implementation ("androidx.compose.foundation:foundation-layout:1.4.3")
}
