import com.vanniktech.maven.publish.SonatypeHost
import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl

plugins {

    alias(libs.plugins.jetbrainsCompose)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.vanniktechMavenPublish)
}

kotlin {

    @OptIn(ExperimentalWasmDsl::class)
    wasmJs { browser() }


    jvm("desktop")


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

    }

}



mavenPublishing {

    coordinates(
        groupId = "com.composevisualeditor.apolostudio",
        artifactId = "squircle-shape",
        version = "0.0.0"
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

task("testClasses") {}