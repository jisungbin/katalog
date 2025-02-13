// Copyright 2024 Ji Sungbin
// SPDX-License-Identifier: Apache-2.0
package land.sungbin.librarian.runtime

import kotlin.reflect.KClass

@MustBeDocumented
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.SOURCE)
public annotation class Catalog(public val type: KClass<out Type>) {
  // - Typography
  // - Color
  public sealed interface Type {
    public val name: String
    public val group: String?
  }
}
