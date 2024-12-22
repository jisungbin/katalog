// Copyright 2024 Ji Sungbin
// SPDX-License-Identifier: Apache-2.0
package land.sungbin.librarian.processor

import androidx.compose.ui.text.TextStyle
import com.google.devtools.ksp.getDeclaredProperties
import com.google.devtools.ksp.isAnnotationPresent
import com.google.devtools.ksp.isPublic
import com.google.devtools.ksp.processing.CodeGenerator
import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.symbol.ClassKind
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.CodeBlock
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.MemberName.Companion.member
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import com.squareup.kotlinpoet.PropertySpec
import com.squareup.kotlinpoet.TypeSpec
import com.squareup.kotlinpoet.asClassName
import com.squareup.kotlinpoet.buildCodeBlock
import com.squareup.kotlinpoet.ksp.addOriginatingKSFile
import com.squareup.kotlinpoet.ksp.toClassName
import com.squareup.kotlinpoet.ksp.writeTo
import com.squareup.kotlinpoet.withIndent
import land.sungbin.librarian.runtime.CatalogConfiguration
import land.sungbin.librarian.runtime.Typography
import land.sungbin.librarian.runtime.semantic.Group
import land.sungbin.librarian.runtime.semantic.Ignore

internal class TypographyGenerator(
  private val logger: KSPLogger,
  private val codeGenerator: CodeGenerator,
) {
  internal fun generate(elements: List<KSClassDeclaration>) {
    elements
      .filter { element -> element.classKind == ClassKind.OBJECT && element.isPublic() }
      .also { if (it.isEmpty()) logger.warn("No public object found with @Catalog(Typography) annotation.") }
      .forEach { element -> createCatalogFile(element).writeTo(codeGenerator, aggregating = false) }
  }

  private fun createCatalogFile(container: KSClassDeclaration): FileSpec {
    val containerName = container.toClassName()
    val typographies = container.getDeclaredProperties()
      .filterNot { property -> property.isAnnotationPresent(Ignore::class) }
      .filter { property -> property.isPublic() && property.type.resolve().isA<TextStyle>() }
      .toList()
    val groups: Set<String> = typographies.mapNotNullTo(mutableSetOf()) { typography -> typography.annotation<Group>()?.name }
    val typographyCodes = typographies.map { style ->
      val name = style.simpleName.asString()
      val group = style.annotation<Group>()?.name
      typography(name, group, containerName)
    }

    val typographyProperty = typographyProperty(typographyCodes)
    val catalogClass = catalogClass(containerName, typographyProperty, groups)

    if (typographies.isEmpty())
      logger.warn("No available TextStyle property found.", symbol = container)

    return FileSpec.builder(containerName.packageName, catalogClass.name!!)
      .addProperty(typographyProperty)
      .addType(catalogClass.toBuilder().addOriginatingKSFile(container.containingFile!!).build())
      .build()
  }

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
      .addSuperinterface(TYPOGRAPHY_LIST_TYPE, delegate = CodeBlock.of("%N", typographyProperty))
      .addProperties(typographyGroupGetters(groups))
      .build()

  private fun typographyGroupGetters(groups: Set<String>): List<PropertySpec> {
    val noneSafety = LazyThreadSafetyMode::class.asClassName().member("NONE")
    return groups.map { group ->
      PropertySpec.builder(group, TYPOGRAPHY_LIST_TYPE)
        .delegate(
          buildCodeBlock {
            beginControlFlow("lazy(%M)", noneSafety)
            addStatement("filter { it.%L == %S }", Typography::group.name, group)
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
    addStatement("%T(", Typography::class)
    withIndent {
      addStatement("%L = %S,", Typography::name.name, name)
      run {
        add("«")
        add("%L = ", Typography::group.name)
        if (group != null) add("%S,", group) else add("null,")
        add("\n»")
      }
      addStatement("%L = %T.%L,", Typography::style.name, container, name)
      addStatement(
        "%L = %M.%L,",
        Typography::previewText.name,
        KSCatalogs.configuration(),
        CatalogConfiguration::typographyPreviewText.name,
      )
    }
    add(")")
  }

  private companion object {
    private const val TYPOGRAPHY_PROPERTY_NAME = "typographies"
    private val TYPOGRAPHY_LIST_TYPE = List::class.asClassName().parameterizedBy(Typography::class.asClassName())
  }
}
