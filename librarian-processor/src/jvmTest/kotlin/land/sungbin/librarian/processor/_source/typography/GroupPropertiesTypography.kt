// Copyright 2024 Ji Sungbin
// SPDX-License-Identifier: Apache-2.0
@file:Suppress("unused")

package land.sungbin.librarian.processor._source.typography

import androidx.compose.ui.text.TextStyle
import land.sungbin.librarian.runtime.Catalog
import land.sungbin.librarian.runtime.Typography
import land.sungbin.librarian.runtime.semantic.Group

@Catalog(Typography::class)
object GroupPropertiesTypography {
  val my = TextStyle()

  @Group("one") val my1 = TextStyle()
  @Group("one") val my11 = TextStyle()
  @Group("one") val my111 = TextStyle()

  @Group("two") val my2 = TextStyle()
  @Group("two") val my22 = TextStyle()
  @Group("two") val my222 = TextStyle()
}
