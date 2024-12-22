// Copyright 2024 Ji Sungbin
// SPDX-License-Identifier: Apache-2.0
package land.sungbin.librarian.processor

import com.google.devtools.ksp.getDeclaredProperties
import com.google.devtools.ksp.isPublic
import com.google.devtools.ksp.processing.CodeGenerator
import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSPropertyDeclaration
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.ksp.toClassName
import com.squareup.kotlinpoet.ksp.writeTo

internal abstract class AbstractFoundationGenerator(
  protected val logger: KSPLogger,
  private val codeGenerator: CodeGenerator,
  private val processType: ClassName,
) {
  internal fun generate(elements: List<KSClassDeclaration>) {
    elements
      .filter(KSClassDeclaration::isPublic)
      .also {
        if (it.isEmpty())
          logger.warn("No public class found with @Catalog(${processType.simpleName}) annotation.")
      }
      .forEach { element -> internalCreateCatalogFile(element).writeTo(codeGenerator, aggregating = true) }
  }

  protected abstract fun createCatalogFile(
    container: KSClassDeclaration,
    containerName: ClassName,
    containerGroup: String?,
    targets: List<KSPropertyDeclaration>,
    groups: Set<String>,
  ): FileSpec

  private fun internalCreateCatalogFile(container: KSClassDeclaration): FileSpec {
    val containerName = container.toClassName()
    val containerGroup = container.lookupGroupName()

    val targets = container.getDeclaredProperties()
      .filterNot { property -> property.hasAnnotation(KSSemantic.Ignore) }
      .filter { property -> property.isPublic() && property.type.resolve().isA(processType) }
      .toList()
    val groups: Set<String> =
      targets
        .mapNotNullTo(mutableSetOf(), transform = KSAnnotated::lookupGroupName)
        .apply { if (containerGroup != null) add(containerGroup) }

    return createCatalogFile(container, containerName, containerGroup, targets, groups)
  }
}
