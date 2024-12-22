// Copyright 2024 Ji Sungbin
// SPDX-License-Identifier: Apache-2.0
package land.sungbin.librarian.processor

import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSAnnotation
import com.google.devtools.ksp.symbol.KSType
import com.google.devtools.ksp.symbol.KSValueArgument
import com.squareup.kotlinpoet.ClassName

internal fun KSAnnotated.annotation(name: ClassName): KSAnnotation? =
  annotations.firstOrNull { annotation ->
    annotation.shortName.asString() == name.simpleName &&
      annotation.annotationType.resolve().declaration.qualifiedName?.asString() == name.canonicalName
  }

internal fun KSAnnotated.hasAnnotation(name: ClassName): Boolean =
  annotations.any { annotation ->
    annotation.shortName.asString() == name.simpleName &&
      annotation.annotationType.resolve().declaration.qualifiedName?.asString() == name.canonicalName
  }

@Suppress("UNCHECKED_CAST")
internal fun <T> List<KSValueArgument>.value(name: String): T? =
  firstOrNull { argument -> argument.name?.asString() == name }?.value as? T

internal fun KSAnnotated.lookupGroupName(): String? =
  annotation(KSSemantic.Group)?.arguments?.value<String>("name")

internal fun KSType.isA(name: ClassName): Boolean =
  declaration.qualifiedName?.asString() == name.canonicalName

internal val KSType.simpleName: String
  get() = declaration.simpleName.asString()
