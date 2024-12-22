// Copyright 2024 Ji Sungbin
// SPDX-License-Identifier: Apache-2.0
package land.sungbin.librarian.runtime

import androidx.compose.runtime.Immutable
import androidx.compose.ui.text.TextStyle

@Immutable public data class Typography(
  public val name: String,
  public val group: String?,
  public val style: TextStyle,
  public val previewText: String,
) : Catalog.Type
