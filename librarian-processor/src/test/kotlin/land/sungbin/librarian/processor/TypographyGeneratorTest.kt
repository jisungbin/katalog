// Copyright 2024 Ji Sungbin
// SPDX-License-Identifier: Apache-2.0
package land.sungbin.librarian.processor

import assertk.Assert
import assertk.assertThat
import assertk.assertions.contains
import assertk.assertions.exists
import assertk.assertions.isEqualTo
import assertk.assertions.isFalse
import java.nio.file.Path
import kotlin.io.path.exists
import kotlin.io.path.readText
import kotlin.test.Test
import land.sungbin.librarian.processor._golden.golden
import land.sungbin.librarian.processor._source.source

class TypographyGeneratorTest : AbstractGeneratorTest(PACKAGE_NAME) {
  @Test fun grouped() {
    val compilation = prepareCompilation(source("typography/GroupedTypography.kt"))
    val result = compilation.compile()

    assertThat(result.exitCode).isOk()
    assertThat(kspGeneratedDir.resolve("GroupedTypographyCatalog.kt"))
      .also(Assert<Path>::exists)
      .transform(transform = Path::readText)
      .isEqualTo(golden("typography/GroupedTypographyCatalog.txt"))
  }

  @Test fun oneIgnored() {
    val compilation = prepareCompilation(source("typography/OneIgnoredTypography.kt"))
    val result = compilation.compile()

    assertThat(result.exitCode).isOk()
    assertThat(kspGeneratedDir.resolve("OneIgnoredTypographyCatalog.kt"))
      .also(Assert<Path>::exists)
      .transform(transform = Path::readText)
      .isEqualTo(golden("typography/OneIgnoredTypographyCatalog.txt"))
  }

  @Test fun allIgnored() {
    val compilation = prepareCompilation(source("typography/AllIgnoredTypography.kt"))
    val result = compilation.compile()

    assertThat(result.exitCode).isOk()
    assertThat(result.messages)
      .contains("No available TextStyle property found.")
    assertThat(kspGeneratedDir.resolve("AllIgnoredTypographyCatalog.kt"))
      .also(Assert<Path>::exists)
      .transform(transform = Path::readText)
      .isEqualTo(golden("typography/AllIgnoredTypographyCatalog.txt"))
  }

  @Test fun privateObject() {
    val compilation = prepareCompilation(source("typography/PrivateObjectTypography.kt"))
    val result = compilation.compile()

    assertThat(result.exitCode).isOk()
    assertThat(result.messages)
      .contains("w: [ksp] No public object found with @Catalog(Typography) annotation.")
    assertThat(kspGeneratedDir.resolve("PrivateObjectTypographyCatalog.kt"))
      .transform(transform = Path::exists)
      .isFalse()
  }

  @Test fun privateProperties() {
    val compilation = prepareCompilation(source("typography/PrivatePropertiesTypography.kt"))
    val result = compilation.compile()

    assertThat(result.exitCode).isOk()
    assertThat(kspGeneratedDir.resolve("PrivatePropertiesTypographyCatalog.kt"))
      .also(Assert<Path>::exists)
      .transform(transform = Path::readText)
      .isEqualTo(golden("typography/PrivatePropertiesTypographyCatalog.txt"))
  }

  private companion object {
    private const val PACKAGE_NAME = "land.sungbin.librarian.processor._source.typography"
  }
}
