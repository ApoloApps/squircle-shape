import com.vanniktech.maven.publish.SonatypeHost
import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl

plugins {
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.jetbrainsCompose)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.vanniktechMavenPublish)
}

kotlin {

    @OptIn(ExperimentalWasmDsl::class)
    wasmJs { browser() }
    js { browser() }

    jvm("desktop")

    androidTarget {
        publishLibraryVariants("release", "debug")
    }
    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64(),
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "ComposeApp"
            isStatic = true
        }
    }

    sourceSets {

        val commonMain by getting {
            dependencies {
                implementation(compose.ui)
                implementation(compose.uiUtil)
                implementation(compose.runtime)
                implementation(compose.foundation)
            }
        }



        val desktopMain by getting {
            dependencies { /* Leave empty for now. */ }
        }
        val androidMain by getting {
            dependencies { /* Leave empty for now. */ }
        }

        val iosX64Main by getting
        val iosArm64Main by getting
        val iosSimulatorArm64Main by getting
        val iosMain by creating {
            dependsOn(commonMain)
            iosX64Main.dependsOn(this)
            iosArm64Main.dependsOn(this)
            iosSimulatorArm64Main.dependsOn(this)
        }
    }

}

android {

    compileSdk = 35
    namespace = "com.composevisualeditor.apolostudio"

    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
    sourceSets["main"].res.srcDirs("src/androidMain/res")
    sourceSets["main"].resources.srcDirs("src/commonMain/composeResources")

    defaultConfig {
        minSdk = 23
    }

    compileOptions {
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlin {
        jvmToolchain(17)
    }

}



mavenPublishing {

    coordinates(
        groupId = "com.composevisualeditor.apolostudio",
        artifactId = "squircle-shape",
        version = libs.versions.composePlugin.get()
    )

    pom {

        name.set(project.name)
        description.set("A Compose Multiplatform library forked from https://github.com/stoyan-vuchev/squircle-shape providing customizable Squircle shapes for UI components.")
        inceptionYear.set("2024")
        url.set("https://github.com/apolostudio/squircle-shape")

        licenses {
            license {
                name.set("MIT")
                url.set("https://opensource.org/licenses/MIT")
            }
        }

        developers {
            developer {
                id.set("apolostudio")
                name.set("Apolo Studio")
                email.set("contact@composevisualeditor.com")
                url.set("https://github.com/apolostudio/")
            }
        }

        scm {
            url.set("https://github.com/apolostudio/squircle-shape")
        }

    }

    // Configure publishing to Maven Central
    publishToMavenCentral(SonatypeHost.CENTRAL_PORTAL)

    // Enable GPG signing for all publications
    signAllPublications()

}

tasks.named("createStagingRepository") {
    doFirst {
        println("Hey, im publishing as Compose version ${libs.versions.composePlugin.get()}")
    }
}
