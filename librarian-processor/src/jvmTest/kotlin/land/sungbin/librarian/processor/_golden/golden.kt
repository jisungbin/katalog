// Copyright 2024 Ji Sungbin
// SPDX-License-Identifier: Apache-2.0
package land.sungbin.librarian.processor._golden

import java.io.File

fun golden(path: String): String =
  File("src/jvmTest/kotlin/land/sungbin/librarian/processor/_golden/$path").readText()
