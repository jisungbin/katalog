// Copyright 2024 Ji Sungbin
// SPDX-License-Identifier: Apache-2.0
@file:Suppress("unused")

package land.sungbin.librarian.processor._source.typography

import androidx.compose.ui.text.TextStyle
import land.sungbin.librarian.runtime.Catalog
import land.sungbin.librarian.runtime.Typography
import land.sungbin.librarian.runtime.semantic.Group

@Group("my")
@Catalog(Typography::class)
object GroupClassTypography {
  val my1 = TextStyle()
  val my2 = TextStyle()
  val my3 = TextStyle()
}
