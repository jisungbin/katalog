// Copyright 2024 Ji Sungbin
// SPDX-License-Identifier: Apache-2.0
package land.sungbin.librarian.processor

import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import com.google.devtools.ksp.processing.SymbolProcessorProvider
import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSClassDeclaration
import land.sungbin.librarian.runtime.Catalog
import land.sungbin.librarian.runtime.Typography

internal class CatalogProcessor(environment: SymbolProcessorEnvironment) : SymbolProcessor {
  private val logger = environment.logger
  private val enabled = environment.options.getOrDefault(OPTION_ENABLED, "true").toBoolean()
  private var invoked = false

  private val typography by lazy { TypographyGenerator(logger, environment.codeGenerator) }

  override fun process(resolver: Resolver): List<KSAnnotated> {
    if (!enabled) return emptyList()
    if (invoked) return emptyList() // Only need a single round
    invoked = true

    resolver.getSymbolsWithAnnotation(Catalog::class.qualifiedName!!)
      .toList()
      .also { if (it.isEmpty()) logger.info("No elements found with @Catalog annotation.") }
      .groupBy { annotated -> annotated.annotation<Catalog>()!!.type.simpleName!! }
      .forEach { (type, elements) ->
        @Suppress("UNCHECKED_CAST")
        when (type) {
          Typography::class.simpleName!! -> typography.generate(elements as List<KSClassDeclaration>)
        }
      }

    return emptyList()
  }

  @Suppress("unused") // Used by KSP runner via reflection
  internal class Provider : SymbolProcessorProvider {
    override fun create(environment: SymbolProcessorEnvironment): SymbolProcessor =
      CatalogProcessor(environment)
  }

  private companion object {
    private const val OPTION_ENABLED = "enabled"
  }
}
