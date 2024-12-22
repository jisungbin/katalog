// Copyright 2024 Ji Sungbin
// SPDX-License-Identifier: Apache-2.0
package land.sungbin.librarian.runtime

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color

@Immutable public data class Color(
  public override val name: String,
  public override val group: String?,
  public val value: Color,
) : Catalog.Type
