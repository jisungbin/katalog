// Copyright 2024 Ji Sungbin
// SPDX-License-Identifier: Apache-2.0
package land.sungbin.librarian.processor

import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.MemberName
import com.squareup.kotlinpoet.MemberName.Companion.member
import com.squareup.kotlinpoet.asClassName
import land.sungbin.librarian.runtime.Catalogs

internal object KSCatalogs {
  internal const val CATALOG_NAME = "Catalog"

  private val name by unsafeLazy { Catalogs::class.asClassName() }
  private val configuration by unsafeLazy { name.member("configuration") }

  internal operator fun invoke(): ClassName = name
  internal fun configuration(): MemberName = configuration
}
