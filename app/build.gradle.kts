
plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("conan.io") version "1.0-SNAPSHOT"
}

android {
    namespace = "com.example.myapp"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.myapp"
        minSdk = 33
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        externalNativeBuild {
            cmake {
                cppFlags += "-std=c++17"
                arguments("-DCMAKE_TOOLCHAIN_FILE=app/conan_build/Debug/armeabi-v7a/build/Debug/generators/conan_toolchain.cmake")
                abiFilters("x86", "x86_64","arm64-v8a","armeabi-v7a")
            }
        }
    }

   splits {
       abi {
           // Enables building multiple APKs per ABI.
           isEnable = true

           // By default all ABIs are included, so use reset() and include to specify that you only
           // want APKs for x86 and x86_64.

           // Resets the list of ABIs for Gradle to create APKs for to none.
           reset()

           // Specifies a list of ABIs for Gradle to create APKs for.
           include("x86", "x86_64","armeabi-v7a")

           // Specifies that you don't want to also generate a universal APK that includes all ABIs.
           isUniversalApk = false
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

    externalNativeBuild {
        cmake {
            path = file("src/main/cpp/CMakeLists.txt")
            version = "3.22.1"
        }
    }

    buildFeatures {
        viewBinding = true
    }
}

dependencies {

    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.11.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
}
