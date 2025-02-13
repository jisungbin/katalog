// Copyright 2024 Ji Sungbin
// SPDX-License-Identifier: Apache-2.0
@file:Suppress("unused")

package land.sungbin.librarian.processor._source.typography

import androidx.compose.ui.text.TextStyle
import land.sungbin.librarian.runtime.Catalog
import land.sungbin.librarian.runtime.Typography
import land.sungbin.librarian.runtime.semantic.Ignore

@Catalog(Typography::class)
object OneIgnoredTypography {
  val my = TextStyle()
  val my3 = TextStyle()

  @Ignore val my2 = TextStyle()
}
