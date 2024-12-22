// Copyright 2024 Ji Sungbin
// SPDX-License-Identifier: Apache-2.0
package land.sungbin.librarian.processor

import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.MemberName.Companion.member
import land.sungbin.librarian.processor.KSCatalogs.PACKAGE_NAME

internal object KSCatalogs {
  internal const val CATALOG_NAME = "Catalog"
  internal const val PACKAGE_NAME = "land.sungbin.librarian.runtime"

  private val name = ClassName(PACKAGE_NAME, "Catalogs")

  internal val configuration = name.member("configuration")
  internal val Typography = ClassName(PACKAGE_NAME, "Typography")

  internal operator fun invoke(): ClassName = name
}

internal object KSSemantic {
  internal val Group = ClassName(PACKAGE_NAME, "semantic", "Group")
  internal val Ignore = ClassName(PACKAGE_NAME, "semantic", "Ignore")
}

internal object KSComposeStableMarker {
  private const val PACKAGE_NAME = "androidx.compose.runtime"

  internal val Immutable = ClassName(PACKAGE_NAME, "Immutable")
  internal val Stable = ClassName(PACKAGE_NAME, "Stable")
}

internal object KSComposeFoundation {
  internal val TextStyle = ClassName("androidx.compose.ui.text", "TextStyle")
}
