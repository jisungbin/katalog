// Copyright 2024 Ji Sungbin
// SPDX-License-Identifier: Apache-2.0
package land.sungbin.librarian.processor

import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import com.google.devtools.ksp.processing.SymbolProcessorProvider
import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSType
import com.squareup.kotlinpoet.ClassName

internal class CatalogProcessor(environment: SymbolProcessorEnvironment) : SymbolProcessor {
  private val logger = environment.logger
  private val codeGenerator = environment.codeGenerator

  private var invoked = false

  private val catalogCn = ClassName(KSCatalogs.PACKAGE_NAME, KSCatalogs.CATALOG_NAME)
  private val typography by lazy { TypographyGenerator(logger, codeGenerator) }

  override fun process(resolver: Resolver): List<KSAnnotated> {
    if (invoked) return emptyList() // Only need a single round.
    invoked = true

    resolver.getSymbolsWithAnnotation(catalogCn.canonicalName)
      .toList()
      .also { if (it.isEmpty()) logger.info("No elements found with @Catalog annotation.") }
      .groupBy { annotated -> annotated.annotation(catalogCn)!!.arguments.value<KSType>("type")!!.simpleName }
      .forEach { (type, elements) ->
        @Suppress("UNCHECKED_CAST")
        when (type) {
          KSCatalogs.Typography.simpleName -> typography.generate(elements as List<KSClassDeclaration>)
        }
      }

    return emptyList()
  }

  @Suppress("unused") // Used by KSP runner via reflection
  internal class Provider : SymbolProcessorProvider {
    override fun create(environment: SymbolProcessorEnvironment): SymbolProcessor =
      CatalogProcessor(environment)
  }
}
