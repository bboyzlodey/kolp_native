plugins {
    kotlin("multiplatform") version "1.9.0"
    kotlin("plugin.serialization") version "1.9.20"
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

kotlin {
    val hostOs = System.getProperty("os.name")
    val isArm64 = System.getProperty("os.arch") == "aarch64"
    val isMingwX64 = hostOs.startsWith("Windows")
    val nativeTarget = when {
        hostOs == "Mac OS X" && isArm64 -> macosArm64("native")
        hostOs == "Mac OS X" && !isArm64 -> macosX64("native")
        hostOs == "Linux" && !isArm64 -> linuxArm64("native")
        hostOs == "Linux" && !isArm64 -> linuxX64("native")
        isMingwX64 -> mingwX64("native")
        else -> throw GradleException("Host OS is not supported in Kotlin/Native.")
    }

    nativeTarget.apply {
        compilations.getByName("main") {
            cinterops {
                val wiringPi by creating {
                    defFile(project.file("src/nativeInterop/cinterop/wiringPi.def"))
                }
                val libcurl by creating {
                    defFile(project.file("src/nativeInterop/cinterop/curl.def"))
                }
            }
        }
        binaries {
            executable {
                entryPoint = "main"
            }
        }
        sourceSets {
            val nativeMain by getting
            val nativeTest by getting
            commonMain {
                dependencies {
                    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3")
                    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.0")
                }
            }
        }
    }
    tasks.withType<Wrapper> {
        print("h")
        distributionType = Wrapper.DistributionType.BIN
    }
}
