// Copyright 2024 Ji Sungbin
// SPDX-License-Identifier: Apache-2.0
@file:Suppress("unused")

package land.sungbin.librarian.processor._source.color

import land.sungbin.librarian.runtime.Catalog
import land.sungbin.librarian.runtime.Color
import land.sungbin.librarian.runtime.semantic.Ignore

@Catalog(Color::class)
object OneIgnoredColor {
  val my = androidx.compose.ui.graphics.Color.White
  val my3 = androidx.compose.ui.graphics.Color.White

  @Ignore val my2 = androidx.compose.ui.graphics.Color.White
}
