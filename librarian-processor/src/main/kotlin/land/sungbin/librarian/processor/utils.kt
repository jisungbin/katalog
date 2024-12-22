// Copyright 2024 Ji Sungbin
// SPDX-License-Identifier: Apache-2.0
package land.sungbin.librarian.processor

import com.google.devtools.ksp.getAnnotationsByType
import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSType

internal inline fun <reified T : Annotation> KSAnnotated.annotation(): T? =
  getAnnotationsByType(T::class).firstOrNull()

internal inline fun <reified T : Any> KSType.isA(): Boolean =
  declaration.qualifiedName?.asString() == T::class.qualifiedName

@Suppress("NOTHING_TO_INLINE")
internal inline fun <T> unsafeLazy(noinline initializer: () -> T): Lazy<T> =
  lazy(LazyThreadSafetyMode.NONE, initializer = initializer)
