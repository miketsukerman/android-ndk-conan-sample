import java.io.IOException

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}

fun String.runCommand(workingDir: File
, timeoutAmount: Long = 60,
timeoutUnit: TimeUnit = TimeUnit.SECONDS): String? {
    try {
        val parts = this.split("\\s".toRegex())

        println(parts)

        val proc = ProcessBuilder(*parts.toTypedArray())
                .directory(workingDir)
                .redirectOutput(ProcessBuilder.Redirect.PIPE)
                .redirectError(ProcessBuilder.Redirect.PIPE)
                .apply { waitFor(timeoutAmount, timeoutUnit) }
    .run {
        val error = errorStream.bufferedReader().readText().trim()
        if (error.isNotEmpty()) {
            throw IOException(error)
        }
        inputStream.bufferedReader().readText().trim()
    }

        return proc.inputStream.bufferedReader().readText()
    } catch(e: IOException) {
        e.printStackTrace()
        return null
    }
}

task("conanInstall") {
    val workDir = File("app/build")
    buildDir.mkdirs()
    listOf<String>("Debug","Release").forEach {build_type ->
        listOf("armv7", "armv8", "x86", "x86_64").forEach {arch ->
            val command = "conan install ../../src/main/cpp --profile android " + 
                    "--output-folder " + workDir + " " +
                    "-s build_type=" + build_type + " -s arch=" + arch + " --build missing " + 
                    "-c \"tools.cmake.cmake_layout:build_folder_vars=['settings.arch']\""

            val out = command.runCommand(workDir)

            println(out)
        }
    }
}

android {
    namespace = "com.example.myapp"
    compileSdk = 33

    defaultConfig {
        applicationId = "com.example.myapp"
        minSdk = 33
        targetSdk = 33
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        externalNativeBuild {
            cmake {
                cppFlags += "-std=c++17"
                arguments("-DCMAKE_TOOLCHAIN_FILE=conan_android_toolchain.cmake")
            }
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