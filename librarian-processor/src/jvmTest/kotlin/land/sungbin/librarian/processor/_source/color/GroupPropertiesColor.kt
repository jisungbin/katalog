// Copyright 2024 Ji Sungbin
// SPDX-License-Identifier: Apache-2.0
@file:Suppress("unused")

package land.sungbin.librarian.processor._source.color

import land.sungbin.librarian.runtime.Catalog
import land.sungbin.librarian.runtime.Color
import land.sungbin.librarian.runtime.semantic.Group

@Catalog(Color::class)
object GroupPropertiesColor {
  val my = androidx.compose.ui.graphics.Color.White

  @Group("one") val my1 = androidx.compose.ui.graphics.Color.White
  @Group("one") val my11 = androidx.compose.ui.graphics.Color.White
  @Group("one") val my111 = androidx.compose.ui.graphics.Color.White

  @Group("two") val my2 = androidx.compose.ui.graphics.Color.White
  @Group("two") val my22 = androidx.compose.ui.graphics.Color.White
  @Group("two") val my222 = androidx.compose.ui.graphics.Color.White
}
