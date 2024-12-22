// Copyright 2024 Ji Sungbin
// SPDX-License-Identifier: Apache-2.0
plugins {
  kotlin("jvm")
  id(libs.plugins.gradle.publish.maven.get().pluginId)
}

kotlin {
  explicitApi()
  jvmToolchain(libs.versions.jdk.get().toInt())

  compilerOptions {
    optIn.addAll(
      "com.google.devtools.ksp.KspExperimental",
      "org.jetbrains.kotlin.compiler.plugin.ExperimentalCompilerApi",
    )
  }
}

dependencies {
  compileOnly(projects.librarianRuntime)
  compileOnly(libs.compose.foundation)

  implementation(libs.kotlin.poet)
  implementation(libs.kotlin.poet.ksp)
  implementation(libs.kotlin.symbolProcessing.api)

  testImplementation(kotlin("test-junit5"))
  testImplementation(kotlin("reflect", version = libs.versions.kotlin.asProvider().get())) // Used by assertk
  testImplementation(libs.test.assertk)
  testImplementation(libs.test.kotlin.compile)
  testImplementation(libs.test.kotlin.compile.ksp)

  testImplementation(projects.librarianRuntime)
  testImplementation(libs.compose.foundation)
}
