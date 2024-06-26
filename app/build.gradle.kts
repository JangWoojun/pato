import java.util.Properties

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("kotlin-kapt")
    id("kotlin-parcelize")
}

android {
    namespace = "com.woojun.pato"
    compileSdk = 34

    val localProperties = Properties()
    val localPropertiesFile = rootProject.file("local.properties")
    if (localPropertiesFile.exists()) {
        localPropertiesFile.inputStream().use { localProperties.load(it) }
    }

    val kakaoNativeKey = localProperties.getProperty("kakaoNativeKey") ?: ""
    val manifesKakaoNativeKey = localProperties.getProperty("manifesKakaoNativeKey") ?: ""
    val baseUrl = localProperties.getProperty("baseUrl") ?: ""

    defaultConfig {
        applicationId = "com.woojun.pato"
        minSdk = 29
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        buildConfigField("String", "kakaoNativeKey", "\"$kakaoNativeKey\"")
        resValue("string", "kakaoNativeKey", kakaoNativeKey)

        buildConfigField("String", "manifesKakaoNativeKey", "\"$manifesKakaoNativeKey\"")
        resValue("string", "manifesKakaoNativeKey", manifesKakaoNativeKey)

        buildConfigField("String", "baseUrl", "\"$baseUrl\"")
        resValue("string", "baseUrl", baseUrl)
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
    viewBinding {
        enable = true
    }
    buildFeatures {
        buildConfig = true
    }
}

dependencies {

    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.11.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("androidx.navigation:navigation-fragment-ktx:2.7.7")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")

    implementation("com.github.bumptech.glide:glide:4.16.0")

    implementation("com.squareup.retrofit2:converter-gson:2.9.0")

    implementation("com.airbnb.android:lottie:6.0.1")

    implementation("com.kakao.sdk:v2-user:2.19.0")

    implementation("com.squareup.okhttp3:okhttp:4.12.0")
}