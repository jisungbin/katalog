// Copyright 2024 Ji Sungbin
// SPDX-License-Identifier: Apache-2.0
package land.sungbin.librarian.processor

import assertk.Assert
import assertk.assertThat
import assertk.assertions.contains
import assertk.assertions.exists
import assertk.assertions.isEqualTo
import java.nio.file.Path
import kotlin.io.path.readText
import kotlin.test.Test
import land.sungbin.librarian.processor._golden.golden
import land.sungbin.librarian.processor._source.source

class TypographyGeneratorTest : AbstractGeneratorTest(PACKAGE_NAME) {
  @Test fun groupedClass() {
    val compilation = prepareCompilation(source("typography/GroupClassTypography.kt"))
    val result = compilation.compile()

    assertThat(result.exitCode).isOk()
    assertThat(result.messages).contains(generatedCatalogInfoMessage("GroupClassTypography", "TextStyle"))
    assertThat(kspGeneratedDir.resolve("GroupClassTypographyCatalog.kt"))
      .also(Assert<Path>::exists)
      .transform(transform = Path::readText)
      .isEqualTo(golden("typography/GroupClassTypographyCatalog.txt"))
  }

  @Test fun groupedProperties() {
    val compilation = prepareCompilation(source("typography/GroupPropertiesTypography.kt"))
    val result = compilation.compile()

    assertThat(result.exitCode).isOk()
    assertThat(result.messages).contains(generatedCatalogInfoMessage("GroupPropertiesTypography", "TextStyle"))
    assertThat(kspGeneratedDir.resolve("GroupPropertiesTypographyCatalog.kt"))
      .also(Assert<Path>::exists)
      .transform(transform = Path::readText)
      .isEqualTo(golden("typography/GroupPropertiesTypographyCatalog.txt"))
  }

  @Test fun oneIgnored() {
    val compilation = prepareCompilation(source("typography/OneIgnoredTypography.kt"))
    val result = compilation.compile()

    assertThat(result.exitCode).isOk()
    assertThat(result.messages).contains(generatedCatalogInfoMessage("OneIgnoredTypography", "TextStyle"))
    assertThat(kspGeneratedDir.resolve("OneIgnoredTypographyCatalog.kt"))
      .also(Assert<Path>::exists)
      .transform(transform = Path::readText)
      .isEqualTo(golden("typography/OneIgnoredTypographyCatalog.txt"))
  }

  @Test fun allIgnored() {
    val compilation = prepareCompilation(source("typography/AllIgnoredTypography.kt"))
    val result = compilation.compile()

    assertThat(result.exitCode).isOk()
    assertThat(result.messages).contains("No available TextStyle property found.")
    assertThat(result.messages).contains(generatedCatalogInfoMessage("AllIgnoredTypography", "TextStyle"))
    assertThat(kspGeneratedDir.resolve("AllIgnoredTypographyCatalog.kt"))
      .also(Assert<Path>::exists)
      .transform(transform = Path::readText)
      .isEqualTo(golden("typography/AllIgnoredTypographyCatalog.txt"))
  }

  @Test fun privateProperties() {
    val compilation = prepareCompilation(source("typography/PrivatePropertiesTypography.kt"))
    val result = compilation.compile()

    assertThat(result.exitCode).isOk()
    assertThat(result.messages).contains(generatedCatalogInfoMessage("PrivatePropertiesTypography", "TextStyle"))
    assertThat(kspGeneratedDir.resolve("PrivatePropertiesTypographyCatalog.kt"))
      .also(Assert<Path>::exists)
      .transform(transform = Path::readText)
      .isEqualTo(golden("typography/PrivatePropertiesTypographyCatalog.txt"))
  }

  private companion object {
    private const val PACKAGE_NAME = "land.sungbin.librarian.processor._source.typography"
  }
}
