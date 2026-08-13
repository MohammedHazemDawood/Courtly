import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import java.util.Properties

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidMultiplatformLibrary)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.koin.compiler)
    kotlin("plugin.serialization") version "2.4.10"
    alias(libs.plugins.buildkonfig)

}

val localProperties = Properties().apply {
    rootProject.file("local.properties").inputStream().use(::load)
}

fun localProperty(name: String): String =
    localProperties.getProperty(name)
        ?: error("$name is missing from local.properties")

buildkonfig {
    packageName = "com.mhd_07.courtly.shared"

    defaultConfigs {
        buildConfigField(
            com.codingfeline.buildkonfig.compiler.FieldSpec.Type.STRING,
            "SUPABASE_URL",
            localProperty("supabase.url")
        )

        buildConfigField(
            com.codingfeline.buildkonfig.compiler.FieldSpec.Type.STRING,
            "SUPABASE_KEY",
            localProperty("supabase.key")
        )

        buildConfigField(
            com.codingfeline.buildkonfig.compiler.FieldSpec.Type.STRING,
            "GOOGLE_WEB_CLIENT_ID",
            localProperty("google.auth.client.supabase")
        )
    }
}

kotlin {
    listOf(
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "Shared"
            isStatic = true
        }
    }

    android {
        namespace = "com.mhd_07.courtly.shared"
        compileSdk = libs.versions.android.compileSdk.get().toInt()
        minSdk = libs.versions.android.minSdk.get().toInt()

        compilerOptions {
            jvmTarget = JvmTarget.JVM_11
        }
        androidResources {
            enable = true
        }
        withHostTest {
            isIncludeAndroidResources = true
        }
    }

    compilerOptions {
        freeCompilerArgs.add("-Xexpect-actual-classes")
    }

    sourceSets {
        androidMain.dependencies {
            implementation(libs.compose.uiToolingPreview)

            api(libs.koin.core.android)

            implementation(libs.androidx.credentials)
            implementation(libs.androidx.credentials.play.services.auth)
            implementation(libs.googleid)
            implementation(libs.ktor.client.okhttp)

            implementation(libs.accompanist.permissions)
        }
        commonMain.dependencies {
            implementation(libs.compose.runtime)
            implementation(libs.compose.foundation)
            implementation(libs.compose.material3)
            implementation(libs.compose.ui)
            implementation(libs.compose.components.resources)
            implementation(libs.compose.uiToolingPreview)
            implementation(libs.androidx.lifecycle.viewmodelCompose)
            implementation(libs.androidx.lifecycle.runtimeCompose)
            api(libs.androidx.lifecycle.viewmodel)
            implementation(libs.kotlinx.datetime)
//            implementation(libs.tabler.icons.kmp)
            api(libs.koin.core)
            api(libs.koin.core.viewmodel)
            implementation(libs.constraintlayout.compose.multiplatform)
            implementation(libs.jetbrains.navigation3.ui)
            implementation(libs.kotlinx.serialization.json)
            implementation(libs.jetbrains.lifecycle.viewmodelNavigation3)

            implementation(libs.ktor.client.core)
            implementation(libs.ktor.client.content.negotiation)
            implementation(libs.ktor.client.websockets)

            implementation(project.dependencies.platform("io.github.jan-tennert.supabase:bom:3.7.0"))
            implementation(libs.postgrest.kt)
            implementation(libs.auth.kt)
            implementation(libs.auth.compose)
            implementation(libs.realtime.kt)
            implementation(libs.functions.kt)
            implementation(libs.storage.kt)

            implementation(libs.coil.compose)
            implementation(libs.coil.network.ktor3)

            implementation(libs.compottie)
            implementation(libs.compottie.dot)
            implementation(libs.compottie.network)

//            implementation(libs.animation)


//            implementation(libs.compose.aspect.ratio.reference)
//            implementation("io.github.ismoy:imagepickerkmp:1.0.22")
        }
        appleMain.dependencies {
            implementation(libs.ktor.client.darwin)
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)

        }
    }
}

dependencies {
    androidRuntimeClasspath(libs.compose.uiTooling)
}