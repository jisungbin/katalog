// Copyright 2024 Ji Sungbin
// SPDX-License-Identifier: Apache-2.0
package land.sungbin.librarian.processor

import com.google.devtools.ksp.getDeclaredProperties
import com.google.devtools.ksp.isPublic
import com.google.devtools.ksp.processing.CodeGenerator
import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.CodeBlock
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.MemberName.Companion.member
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import com.squareup.kotlinpoet.PropertySpec
import com.squareup.kotlinpoet.STRING
import com.squareup.kotlinpoet.TypeName
import com.squareup.kotlinpoet.TypeSpec
import com.squareup.kotlinpoet.asClassName
import com.squareup.kotlinpoet.buildCodeBlock
import com.squareup.kotlinpoet.joinToCode
import com.squareup.kotlinpoet.ksp.addOriginatingKSFile
import com.squareup.kotlinpoet.ksp.toClassName
import com.squareup.kotlinpoet.ksp.writeTo
import com.squareup.kotlinpoet.withIndent

internal abstract class AbstractFoundationGenerator(
  protected val logger: KSPLogger,
  private val codeGenerator: CodeGenerator,
  private val foundationType: ClassName,
  private val catalogElementType: ClassName,
) {
  private val catalogElementListType = List::class.asClassName().parameterizedBy(catalogElementType)

  internal fun generate(elements: List<KSClassDeclaration>) {
    elements
      .filter(KSClassDeclaration::isPublic)
      .also { if (it.isEmpty()) logger.warn("No public class found with @Catalog(${catalogElementType.simpleName}) annotation.") }
      .forEach { element -> createCatalogFile(element).writeTo(codeGenerator, aggregating = true) }
  }

  protected abstract fun catalogElementCode(name: String, group: String?, container: ClassName): CodeBlock

  private fun createCatalogFile(container: KSClassDeclaration): FileSpec {
    val containerName = container.toClassName()
    val containerGroup = container.lookupGroupName()

    val targets = container.getDeclaredProperties()
      .filterNot { property -> property.hasAnnotation(KSSemantic.Ignore) }
      .filter { property -> property.isPublic() && property.type.resolve().isA(foundationType) }
      .toList()
    val groups: Set<String> =
      targets
        .mapNotNullTo(mutableSetOf(), transform = KSAnnotated::lookupGroupName)
        .apply { if (containerGroup != null) add(containerGroup) }

    val catalogElementCodes = targets.map { target ->
      val name = target.simpleName.asString()
      val group = target.lookupGroupName() ?: containerGroup
      catalogElementCode(name, group, containerName)
    }

    val catalogElementListProperty = catalogElementListProperty(catalogElementCodes)
    val catalogClass = catalogClass(containerName, catalogElementListProperty, groups)
    val catalogExtensionProperty = catalogExtensionProperty(containerName, catalogClass)

    if (targets.isEmpty())
      logger.warn("No available ${foundationType.simpleName} property found.", symbol = container)

    return FileSpec.builder(containerName.packageName, catalogClass.name!!)
      .addProperties(listOf(catalogExtensionProperty, catalogElementListProperty))
      .addType(catalogClass.toBuilder().addOriginatingKSFile(container.containingFile!!).build())
      .build()
      .also { logger.info("Generated ${foundationType.simpleName} catalog for ${containerName.simpleName}: ${catalogClass.name}") }
  }

  private fun catalogClass(
    containerName: ClassName,
    catalogElementListProperty: PropertySpec,
    groups: Set<String>,
  ): TypeSpec =
    TypeSpec.objectBuilder(containerName.simpleName + KSCatalogs.CATALOG_NAME)
      .addAnnotation(KSComposeStableMarker.Immutable)
      .addSuperinterface(catalogElementListType, delegate = CodeBlock.of("%N", catalogElementListProperty))
      .apply { if (groups.isNotEmpty()) addProperty(allGroupsGetter(groups)) }
      .addProperties(groupGetters(groups, catalogElementListType))
      .build()

  private fun catalogExtensionProperty(
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

  private fun catalogElementListProperty(elementCodes: List<CodeBlock>): PropertySpec =
    PropertySpec.builder(TARGET_LIST_NAME, catalogElementListType)
      .addModifiers(KModifier.PRIVATE)
      // TODO elementCodes.joinToCode(",\n", prefix = "listOf(\n⇥", suffix = "\n⇤)")
      .initializer(
        buildCodeBlock {
          addStatement("listOf(")
          withIndent { elementCodes.forEach { code -> add("%L,\n", code) } }
          add(")")
        },
      )
      .build()

  private fun allGroupsGetter(groups: Set<String>): PropertySpec =
    PropertySpec.builder(ALL_GROUP_PROPERTY_NAME, STRING_LIST_TYPE)
      .addAnnotation(KSComposeStableMarker.Stable)
      .initializer(groups.joinToCode(",\n", prefix = "listOf(\n⇥", suffix = "\n⇤)") { CodeBlock.of("%S", it) })
      .build()

  private fun groupGetters(
    groups: Set<String>,
    type: TypeName,
  ): List<PropertySpec> =
    groups.map { group ->
      PropertySpec.builder(group, type)
        .addAnnotation(KSComposeStableMarker.Stable)
        .delegate(
          buildCodeBlock {
            beginControlFlow("lazy(%M)", NONE_SAFETY_TYPE)
            addStatement("filter { it.%L == %S }", "group", group)
            endControlFlow()
          },
        )
        .build()
    }

  internal companion object {
    private const val TARGET_LIST_NAME = "elements"
    private const val ALL_GROUP_PROPERTY_NAME = "AllGroups"

    private val NONE_SAFETY_TYPE = LazyThreadSafetyMode::class.asClassName().member("NONE")
    private val STRING_LIST_TYPE = List::class.asClassName().parameterizedBy(STRING)
  }
}
