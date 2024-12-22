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

class ColorGeneratorTest : AbstractGeneratorTest(PACKAGE_NAME) {
  @Test fun groupedClass() {
    val compilation = prepareCompilation(source("color/GroupClassColor.kt"))
    val result = compilation.compile()

    assertThat(result.exitCode).isOk()
    assertThat(result.messages).contains(generatedCatalogInfoMessage("GroupClassColor", "Color"))
    assertThat(kspGeneratedDir.resolve("GroupClassColorCatalog.kt"))
      .also(Assert<Path>::exists)
      .transform(transform = Path::readText)
      .isEqualTo(golden("color/GroupClassColorCatalog.txt"))
  }

  @Test fun groupedProperties() {
    val compilation = prepareCompilation(source("color/GroupPropertiesColor.kt"))
    val result = compilation.compile()

    assertThat(result.exitCode).isOk()
    assertThat(result.messages).contains(generatedCatalogInfoMessage("GroupPropertiesColor", "Color"))
    assertThat(kspGeneratedDir.resolve("GroupPropertiesColorCatalog.kt"))
      .also(Assert<Path>::exists)
      .transform(transform = Path::readText)
      .isEqualTo(golden("color/GroupPropertiesColorCatalog.txt"))
  }

  @Test fun oneIgnored() {
    val compilation = prepareCompilation(source("color/OneIgnoredColor.kt"))
    val result = compilation.compile()

    assertThat(result.exitCode).isOk()
    assertThat(result.messages).contains(generatedCatalogInfoMessage("OneIgnoredColor", "Color"))
    assertThat(kspGeneratedDir.resolve("OneIgnoredColorCatalog.kt"))
      .also(Assert<Path>::exists)
      .transform(transform = Path::readText)
      .isEqualTo(golden("color/OneIgnoredColorCatalog.txt"))
  }

  @Test fun allIgnored() {
    val compilation = prepareCompilation(source("color/AllIgnoredColor.kt"))
    val result = compilation.compile()

    assertThat(result.exitCode).isOk()
    assertThat(result.messages).contains("No available Color property found.")
    assertThat(result.messages).contains(generatedCatalogInfoMessage("AllIgnoredColor", "Color"))
    assertThat(kspGeneratedDir.resolve("AllIgnoredColorCatalog.kt"))
      .also(Assert<Path>::exists)
      .transform(transform = Path::readText)
      .isEqualTo(golden("color/AllIgnoredColorCatalog.txt"))
  }

  @Test fun privateProperties() {
    val compilation = prepareCompilation(source("color/PrivatePropertiesColor.kt"))
    val result = compilation.compile()

    assertThat(result.exitCode).isOk()
    assertThat(result.messages).contains(generatedCatalogInfoMessage("PrivatePropertiesColor", "Color"))
    assertThat(kspGeneratedDir.resolve("PrivatePropertiesColorCatalog.kt"))
      .also(Assert<Path>::exists)
      .transform(transform = Path::readText)
      .isEqualTo(golden("color/PrivatePropertiesColorCatalog.txt"))
  }

  private companion object {
    private const val PACKAGE_NAME = "land.sungbin.librarian.processor._source.color"
  }
}
