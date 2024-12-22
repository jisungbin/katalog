// Copyright 2024 Ji Sungbin
// SPDX-License-Identifier: Apache-2.0
package land.sungbin.librarian.processor

import com.google.devtools.ksp.processing.CodeGenerator
import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSPropertyDeclaration
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.CodeBlock
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.MemberName.Companion.member
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import com.squareup.kotlinpoet.PropertySpec
import com.squareup.kotlinpoet.TypeSpec
import com.squareup.kotlinpoet.asClassName
import com.squareup.kotlinpoet.buildCodeBlock
import com.squareup.kotlinpoet.ksp.addOriginatingKSFile
import com.squareup.kotlinpoet.withIndent

internal class TypographyGenerator(
  logger: KSPLogger,
  codeGenerator: CodeGenerator,
) : AbstractFoundationGenerator(logger, codeGenerator, KSComposeFoundation.TextStyle) {
  override fun createCatalogFile(
    container: KSClassDeclaration,
    containerName: ClassName,
    containerGroup: String?,
    targets: List<KSPropertyDeclaration>,
    groups: Set<String>,
  ): FileSpec {
    val typographyCodes = targets.map { typography ->
      val name = typography.simpleName.asString()
      val group = typography.lookupGroupName() ?: containerGroup
      typography(name, group, containerName)
    }

    val typographyProperty = typographyProperty(typographyCodes)
    val catalogClass = catalogClass(containerName, typographyProperty, groups)
    val extensionProperty = extensionProperty(containerName, catalogClass)

    if (targets.isEmpty())
      logger.warn("No available TextStyle property found.", symbol = container)

    return FileSpec.builder(containerName.packageName, catalogClass.name!!)
      .addProperties(listOf(extensionProperty, typographyProperty))
      .addType(catalogClass.toBuilder().addOriginatingKSFile(container.containingFile!!).build())
      .build()
      .also { logger.info("Generated typography catalog for ${containerName.simpleName}: ${catalogClass.name}") }
  }

  private fun extensionProperty(
    containerName: ClassName,
    catalogClass: TypeSpec,
  ): PropertySpec =
    PropertySpec.builder(
      catalogClass.name!!.removeSuffix(KSCatalogs.CATALOG_NAME),
      ClassName(containerName.packageName, catalogClass.name!!),
    )
      .receiver(KSCatalogs())
      .getter(
        FunSpec.getterBuilder()
          .addAnnotation(KSComposeStableMarker.Stable)
          .addModifiers(KModifier.INLINE)
          .addStatement("return %N", catalogClass)
          .build(),
      )
      .build()

  private fun typographyProperty(typographyCodes: List<CodeBlock>): PropertySpec =
    PropertySpec.builder(TYPOGRAPHY_PROPERTY_NAME, TYPOGRAPHY_LIST_TYPE)
      .addModifiers(KModifier.PRIVATE)
      .initializer(
        buildCodeBlock {
          addStatement("listOf(")
          withIndent {
            typographyCodes.forEach { code ->
              add(code)
              add(",\n")
            }
          }
          addStatement(")")
        },
      )
      .build()

  private fun catalogClass(
    containerName: ClassName,
    typographyProperty: PropertySpec,
    groups: Set<String>,
  ): TypeSpec =
    TypeSpec.objectBuilder(containerName.simpleName + KSCatalogs.CATALOG_NAME)
      .addAnnotation(KSComposeStableMarker.Immutable)
      .addSuperinterface(TYPOGRAPHY_LIST_TYPE, delegate = CodeBlock.of("%N", typographyProperty))
      .addProperties(typographyGroupGetters(groups))
      .build()

  private fun typographyGroupGetters(groups: Set<String>): List<PropertySpec> {
    val noneSafety = LazyThreadSafetyMode::class.asClassName().member("NONE")
    return groups.map { group ->
      PropertySpec.builder(group, TYPOGRAPHY_LIST_TYPE)
        .addAnnotation(KSComposeStableMarker.Stable)
        .delegate(
          buildCodeBlock {
            beginControlFlow("lazy(%M)", noneSafety)
            addStatement("filter { it.%L == %S }", "group", group)
            endControlFlow()
          },
        )
        .build()
    }
  }

  private fun typography(
    name: String,
    group: String?,
    container: ClassName,
  ): CodeBlock = buildCodeBlock {
    addStatement("%T(", KSCatalogs.Typography)
    withIndent {
      addStatement("%L = %S,", "name", name)
      run {
        add("«")
        add("%L = ", "group")
        if (group != null) add("%S,", group) else add("null,")
        add("\n»")
      }
      addStatement("%L = %T.%L,", "style", container, name)
      addStatement("%L = %M.%L,", "previewText", KSCatalogs.configuration, "typographyPreviewText")
    }
    add(")")
  }

  private companion object {
    private const val TYPOGRAPHY_PROPERTY_NAME = "typographies"
    private val TYPOGRAPHY_LIST_TYPE = List::class.asClassName().parameterizedBy(KSCatalogs.Typography)
  }
}
