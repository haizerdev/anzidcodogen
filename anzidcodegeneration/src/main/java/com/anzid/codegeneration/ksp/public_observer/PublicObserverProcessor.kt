package com.anzid.codegeneration.ksp.public_observer

import com.anzid.annotation.*
import com.anzid.codegeneration.TypeMapping
import com.anzid.codegeneration.ksp.appendText
import com.anzid.codegeneration.ksp.fail
import com.anzid.codegeneration.ksp.public_observer.templates.ObserverTemplate
import com.anzid.codegeneration.ksp.public_observer.templates.SharedFlowObserverTemplate
import com.anzid.codegeneration.ksp.public_observer.templates.StateFlowObserverTemplate
import com.anzid.codegeneration.replaceTypes
import com.google.devtools.ksp.containingFile
import com.google.devtools.ksp.processing.*
import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSPropertyDeclaration
import com.google.devtools.ksp.symbol.KSVisitorVoid
import com.google.devtools.ksp.validate
import com.squareup.kotlinpoet.ksp.toTypeName
import java.io.OutputStream

internal class PublicObserverProcessor(
    private val codeGenerator: CodeGenerator,
    private val logger: KSPLogger
) : SymbolProcessor {

    companion object {
        private const val END_FILE_NAME = "ObserverContainer"
        private const val DELIMITER = "."
        private const val POSTFIX_KOTLIN_FILE = "${DELIMITER}kt"

        private const val OBSERVER_TYPE_LIVE_DATA = "LiveData"
        private const val OBSERVER_TYPE_SHARED_FLOW = "SharedFlow"
        private const val OBSERVER_TYPE_STATE_FLOW = "StateFlow"

        private const val ERROR_TYPE = "<ERROR>"
    }

    override fun process(resolver: Resolver): List<KSAnnotated> {
        val symbols = getSymbolsForSupportedAnnotation(resolver)
        val ret = symbols.filter { !it.validate() }.toList()

        symbols
            .filter { it is KSPropertyDeclaration && it.validate() }
            .groupByFileLocation()
            .forEach {
                val visitor = PublicObserverVisitor()

                it.value.forEach { element ->
                    element.accept(visitor, Unit)
                }

                visitor.generateFile(resolver)
            }

        return ret
    }

    private fun getSymbolsForSupportedAnnotation(resolver: Resolver): Sequence<KSAnnotated> {
        return resolver.getSymbolsWithAnnotation(PublicLiveData::class.java.name) +
                resolver.getSymbolsWithAnnotation(PublicStateFlow::class.java.name) +
                resolver.getSymbolsWithAnnotation(PublicSharedFlow::class.java.name)
    }

    private fun Sequence<KSAnnotated>.groupByFileLocation(): Map<String, List<KSAnnotated>> {
        return groupBy {
            getFileLocation(it.containingFile?.packageName?.asString(), it.containingFile?.fileName.toString())
        }
    }

    private fun getFileLocation(packageName: String?, fileName: String) = packageName + DELIMITER + fileName.removeSuffix(POSTFIX_KOTLIN_FILE)

    inner class PublicObserverVisitor : KSVisitorVoid() {
        private var observersMap = mutableMapOf<String, MutableSet<ObserverData>>()

        private var isFirstElement = true

        override fun visitPropertyDeclaration(property: KSPropertyDeclaration, data: Unit) {
            val observerData = collectingData(property)

            with(getFileLocation(observerData.pack, observerData.className)) {

                if (observersMap.containsKey(this)) {
                    observersMap[this]?.add(observerData)
                    return@with
                }
                observersMap[this] = mutableSetOf<ObserverData>()
                    .apply { add(observerData) }
            }
        }

        fun generateFile(resolver: Resolver) {
            observersMap.forEach {
                val file = codeGenerator.createNewFile(
                    dependencies = Dependencies(aggregating = false, *resolver.getAllFiles().toList().toTypedArray()),
                    packageName = it.key.split(DELIMITER).dropLast(1).joinToString(DELIMITER),
                    fileName = it.key.split(DELIMITER).last() + END_FILE_NAME
                )

                it.value.forEach { model ->
                    if (isFirstElement) processFirstElementForWrite(file, model)
                    else processSubsequentElementForWrite(file, model)
                }

                file.close()
                isFirstElement = true
            }
        }

        private fun collectingData(property: KSPropertyDeclaration): ObserverData {
            val isInternalModifier = isInternalModifier(property)

            val propertyModifier = if (isInternalModifier) "internal " else ""

            val packageName = property.containingFile?.packageName?.asString() ?: logger.fail("Invalid package name", property)
            val fieldName = property.simpleName.asString()
            val className = property.containingFile?.fileName?.removeSuffix(POSTFIX_KOTLIN_FILE) ?: ERROR_TYPE
            val originalType = property.type.toTypeName().toString()

            val publicType = getPropertyType(property)

            return ObserverData(propertyModifier, fieldName, packageName, className, originalType, publicType).apply {
                isStateFlowData = isStateFlowAnnotation(property)
                isSharedFlowData = isSharedFlowAnnotation(property)
            }
        }

        private fun isInternalModifier(property: KSPropertyDeclaration): Boolean {
            val annotationArgs = property.annotations
                .firstOrNull {
                    it.shortName.asString() == PublicLiveData::class.java.simpleName ||
                            it.shortName.asString() == PublicSharedFlow::class.java.simpleName ||
                            it.shortName.asString() == PublicStateFlow::class.java.simpleName
                }
                ?.arguments

            (annotationArgs
                ?.firstOrNull { it.name?.asString() == PublicLiveData::isInternal.name }
                ?.value as? Boolean ?: false)
                .also {
                    if (it) return true
                }

            (annotationArgs
                ?.firstOrNull { it.name?.asString() == PublicSharedFlow::isInternal.name }
                ?.value as? Boolean ?: false)
                .also {
                    if (it) return true
                }

            (annotationArgs
                ?.firstOrNull { it.name?.asString() == PublicStateFlow::isInternal.name }
                ?.value as? Boolean ?: false)
                .also {
                    if (it) return true
                }

            return false
        }

        private fun getPropertyType(property: KSPropertyDeclaration): String {
            val originalType = property.type.toTypeName().toString()

            return replaceTypes(originalType) { index, type ->
                when (index) {
                    0 -> getObserverTypeByElement(property)
                    else -> TypeMapping.getOrDefault(type, type)
                }
            }
        }

        private fun getObserverTypeByElement(property: KSPropertyDeclaration) = when {
            isLiveDataAnnotation(property) -> OBSERVER_TYPE_LIVE_DATA
            isSharedFlowAnnotation(property) -> OBSERVER_TYPE_SHARED_FLOW
            isStateFlowAnnotation(property) -> OBSERVER_TYPE_STATE_FLOW
            else -> {
                logger.fail("Unsupported type", property)
            }
        }

        private fun isLiveDataAnnotation(property: KSPropertyDeclaration): Boolean {
            return property.annotations
                .firstOrNull { it.shortName.asString() == PublicLiveData::class.java.simpleName }
                ?.arguments != null
        }

        private fun isSharedFlowAnnotation(property: KSPropertyDeclaration): Boolean {
            return property.annotations
                .firstOrNull { it.shortName.asString() == PublicSharedFlow::class.java.simpleName }
                ?.arguments != null
        }

        private fun isStateFlowAnnotation(property: KSPropertyDeclaration): Boolean {
            return property.annotations
                .firstOrNull { it.shortName.asString() == PublicStateFlow::class.java.simpleName }
                ?.arguments != null
        }

        private fun processFirstElementForWrite(stream: OutputStream, data: ObserverData) {
            val defaultFileContent = getTemplateByData(data).getDefaultContent()
            stream.appendText(defaultFileContent)
            isFirstElement = false
        }

        private fun processSubsequentElementForWrite(stream: OutputStream, data: ObserverData) {
            val subsequentFileContent = getTemplateByData(data).getAdditionalContent()
            stream.appendText(subsequentFileContent)
        }

        private fun getTemplateByData(data: ObserverData): ObserverTemplate {
            if (data.isSharedFlowData) {
                return SharedFlowObserverTemplate(data)
            }

            if (data.isStateFlowData) {
                return StateFlowObserverTemplate(data)
            }

            return ObserverTemplate(data)
        }
    }
}