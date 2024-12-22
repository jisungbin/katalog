// Copyright 2024 Ji Sungbin
// SPDX-License-Identifier: Apache-2.0
package land.sungbin.librarian.processor

import com.tschuchort.compiletesting.KotlinCompilation
import com.tschuchort.compiletesting.SourceFile
import com.tschuchort.compiletesting.kspIncremental
import com.tschuchort.compiletesting.symbolProcessorProviders
import com.tschuchort.compiletesting.useKsp2
import java.nio.file.Path
import org.junit.jupiter.api.io.CleanupMode
import org.junit.jupiter.api.io.TempDir

abstract class AbstractGeneratorTest(private val packageName: String) {
  @field:TempDir(cleanup = CleanupMode.ON_SUCCESS)
  protected lateinit var dir: Path

  protected val kspGeneratedDir: Path
    get() = dir.resolve("ksp/sources/kotlin/${packageName.replace('.', '/')}")

  protected fun generatedCatalogInfoMessage(
    containerName: String,
    catalogName: String = containerName + "Catalog",
  ): String =
    "Generated typography catalog for $containerName: $catalogName"

  protected fun prepareCompilation(vararg sources: SourceFile): KotlinCompilation =
    KotlinCompilation().apply {
      this.sources = sources.asList()
      workingDir = dir.toFile()
      inheritClassPath = true
      useKsp2()
      kspIncremental = true
      symbolProcessorProviders = mutableListOf(CatalogProcessor.Provider())
    }
}
