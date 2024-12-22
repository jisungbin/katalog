// Copyright 2024 Ji Sungbin
// SPDX-License-Identifier: Apache-2.0
package land.sungbin.librarian.processor._source

import com.tschuchort.compiletesting.SourceFile
import java.io.File

fun source(path: String): SourceFile =
  SourceFile.kotlin(
    path.substringAfterLast('/'),
    File("src/test/kotlin/land/sungbin/librarian/processor/_source/$path").readText(),
    trimIndent = false,
  )
