// Copyright 2024 Ji Sungbin
// SPDX-License-Identifier: Apache-2.0
package land.sungbin.librarian.processor.impl

import com.google.devtools.ksp.processing.CodeGenerator
import com.google.devtools.ksp.processing.KSPLogger
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.CodeBlock
import com.squareup.kotlinpoet.buildCodeBlock
import com.squareup.kotlinpoet.withIndent
import land.sungbin.librarian.processor.AbstractFoundationGenerator
import land.sungbin.librarian.processor.KSCatalogs
import land.sungbin.librarian.processor.KSComposeFoundation

internal class TypographyGenerator(
  logger: KSPLogger,
  codeGenerator: CodeGenerator,
) : AbstractFoundationGenerator(
  logger,
  codeGenerator,
  KSComposeFoundation.TextStyle,
  KSCatalogs.Typography,
) {
  override fun catalogElementCode(
    name: String,
    group: String?,
    container: ClassName,
  ): CodeBlock = buildCodeBlock {
    addStatement("%T(", KSCatalogs.Typography)
    withIndent {
      addStatement("%L = %S,", "name", name)
      addStatement("%L = %S,", "group", group)
      addStatement("%L = %T.%L,", "style", container, name)
      addStatement("%L = %M.%L,", "previewText", KSCatalogs.configuration, "typographyPreviewText")
    }
    add(")")
  }
}
