// Copyright 2024 Ji Sungbin
// SPDX-License-Identifier: Apache-2.0
plugins {
  kotlin("multiplatform")
  id(libs.plugins.publish.maven.get().pluginId)
}

kotlin {
  explicitApi()
  jvmToolchain(libs.versions.jdk.get().toInt())

  jvm()

  compilerOptions {
    optIn.addAll(
      "com.google.devtools.ksp.KspExperimental",
      "org.jetbrains.kotlin.compiler.plugin.ExperimentalCompilerApi",
    )
  }

  sourceSets {
    jvmMain {
      dependencies {
        implementation(libs.kotlin.poet)
        implementation(libs.kotlin.poet.ksp)
        implementation(libs.kotlin.symbolProcessing.api)
      }
    }

    jvmTest {
      dependencies {
        implementation(projects.librarianRuntime)
        implementation(libs.compose.foundation)

        implementation(kotlin("test-junit5"))
        implementation(kotlin("reflect")) // Used by assertk
        implementation(libs.test.assertk)
        implementation(libs.test.kotlin.compile)
        implementation(libs.test.kotlin.compile.ksp)
      }
    }
  }
}
