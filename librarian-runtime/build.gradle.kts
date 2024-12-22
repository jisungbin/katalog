// Copyright 2024 Ji Sungbin
// SPDX-License-Identifier: Apache-2.0
plugins {
  kotlin("multiplatform")
  id(libs.plugins.android.library.get().pluginId)
  id(libs.plugins.publish.maven.get().pluginId)
  alias(libs.plugins.kotlin.dokka)
}

dokka {
  moduleName = "Catalog-Librarian Runtime API"
  moduleVersion = project.property("VERSION_NAME") as String
  basePublicationsDirectory = rootDir.resolve("documentation/site/runtime/api")

  dokkaSourceSets.configureEach {
    jdkVersion = libs.versions.jdk.get().toInt()
  }

  pluginsConfiguration.html {
    homepageLink = "https://jisungbin.github.io/catalog-librarian/"
    footerMessage = "ComposeInvestigator ⓒ 2024 Ji Sungbin"
  }
}

kotlin {
  explicitApi()
  jvmToolchain(libs.versions.jdk.get().toInt())

  jvm()
  androidTarget()

  // TODO iOS multiplatform
  // iosArm64()
  // iosX64()
  // iosSimulatorArm64()

  sourceSets {
    commonMain {
      dependencies {
        compileOnly(libs.compose.foundation)
      }
    }

    commonTest {
      dependencies {
        implementation(kotlin("test-junit5"))
        implementation(libs.test.assertk)
      }
    }
  }
}

android {
  namespace = "land.sungbin.librarian.runtime"
  sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")

  defaultConfig {
    minSdk = libs.versions.android.minSdk.get().toInt()
    compileSdk = libs.versions.android.compileSdk.get().toInt()
  }

  compileOptions {
    sourceCompatibility = JavaVersion.toVersion(libs.versions.jdk.get().toInt())
    targetCompatibility = JavaVersion.toVersion(libs.versions.jdk.get().toInt())
  }
}
