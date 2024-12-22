// Copyright 2024 Ji Sungbin
// SPDX-License-Identifier: Apache-2.0
plugins {
  kotlin("multiplatform")
  alias(libs.plugins.kotlin.dokka)
  id(libs.plugins.gradle.publish.maven.get().pluginId)
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

  // TODO Multiplatform
  // iosArm64()
  // iosX64()
  // iosSimulatorArm64()

  sourceSets {
    commonMain {
      dependencies {
        compileOnly(libs.compose.foundation)
        implementation(libs.jetbrains.annotations)
      }
    }

    commonTest {
      dependencies {
        implementation(kotlin("test-junit5"))
        implementation(libs.test.assertk)
      }
    }

    jvmTest {
      dependencies {
        implementation(kotlin("reflect", version = libs.versions.kotlin.asProvider().get())) // Used by assertk
      }
    }
  }
}
