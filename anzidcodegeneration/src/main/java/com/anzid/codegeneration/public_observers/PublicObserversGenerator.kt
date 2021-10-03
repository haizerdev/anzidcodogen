package com.anzid.codegeneration.public_observers

import com.anzid.annotation.InternalLiveData
import com.anzid.annotation.InternalSharedFlow
import com.anzid.annotation.PublicLiveData
import com.anzid.annotation.PublicSharedFlow
import com.anzid.codegeneration.AnzidProcessor.Companion.KAPT_KOTLIN_GENERATED_OPTION_NAME
import com.anzid.codegeneration.Generator
import com.anzid.codegeneration.TypeMapping
import com.anzid.codegeneration.replaceTypes
import java.io.File
import javax.annotation.processing.ProcessingEnvironment
import javax.annotation.processing.RoundEnvironment
import javax.lang.model.element.Element
import javax.tools.Diagnostic

class PublicObserversGenerator(private val processingEnv: ProcessingEnvironment) : Generator() {
    companion object {
        private const val END_FILE_NAME = "ObservableContainer"
        private const val OBSERVER_TYPE_LIVE_DATA = "LiveData"
        private const val OBSERVER_TYPE_SHARED_FLOW = "SharedFlow"

        fun processAnnotation(
            roundEnvironment: RoundEnvironment,
            processingEnv: ProcessingEnvironment
        ) {
            val generator = PublicObserversGenerator(processingEnv)

            (roundEnvironment.getElementsAnnotatedWith(PublicLiveData::class.java)
                    + roundEnvironment.getElementsAnnotatedWith(InternalLiveData::class.java)
                    + roundEnvironment.getElementsAnnotatedWith(PublicSharedFlow::class.java)
                    + roundEnvironment.getElementsAnnotatedWith(InternalSharedFlow::class.java))
                .forEach { generator.prepareClassInitialization(it) }

            generator.generateFile()
        }
    }

    private var liveDataMap = mutableMapOf<String, MutableSet<ObserverGeneratorModel>>()

    private var isFirstElement = true

    override fun prepareClassInitialization(element: Element) {
        if (element.simpleName.startsWith('_').not()) {
            showWarning(element, "field name must starts with \"_\" symbol")
        }

        val fieldName = element.simpleName.toString()
        val pack = processingEnv.elementUtils.getPackageOf(element).toString()
        val enclosingElement = element.enclosingElement.simpleName.toString()
        val modifiers = if (isInternalAnnotation(element)) "internal " else ""
        val elementType = getElementType(element)

        with(enclosingElement) {
            val model = ObserverGeneratorModel(modifiers, fieldName, pack, this@with, elementType)

            if (liveDataMap.containsKey(this)) {
                liveDataMap[this]?.add(model)
                return@with
            }
            liveDataMap[this] = mutableSetOf<ObserverGeneratorModel>()
                .apply { add(model) }
        }
    }

    private fun getElementType(element: Element): String {
        getTypeParameter(element)
            ?.let {
                return "${getObserverTypeByElement(element)}<$it>"
            }

        val isNullable = isNullable(element)

        val originalType = element.asType().toString()
        return replaceTypes(originalType) { index, type ->
            when (index) {
                0 -> getObserverTypeByElement(element)
                else -> TypeMapping.getOrDefault(type, type).let {
                    if (isNullable) "$it?"
                    else it
                }
            }
        }
    }

    private fun getTypeParameter(element: Element): String? {
        element.getAnnotation(PublicLiveData::class.java)?.of
            ?.let { return if (it.isEmpty()) null else it }

        element.getAnnotation(PublicSharedFlow::class.java)?.of
            ?.let { return if (it.isEmpty()) null else it }

        element.getAnnotation(InternalLiveData::class.java)?.of
            ?.let { return if (it.isEmpty()) null else it }

        element.getAnnotation(InternalSharedFlow::class.java)?.of
            ?.let { return if (it.isEmpty()) null else it }

        return null
    }

    private fun getObserverTypeByElement(element: Element) = when {
        isLiveDataAnnotation(element) -> OBSERVER_TYPE_LIVE_DATA
        isSharedFlowAnnotation(element) -> OBSERVER_TYPE_SHARED_FLOW
        else -> {
            showError(element, AssertionError::class.java.simpleName)
            ""
        }
    }

    private fun isNullable(element: Element): Boolean {
        element.getAnnotation(PublicLiveData::class.java)?.nullable
            ?.let { return it }

        element.getAnnotation(InternalLiveData::class.java)?.nullable
            ?.let { return it }

        element.getAnnotation(PublicSharedFlow::class.java)?.nullable
            ?.let { return it }

        element.getAnnotation(InternalSharedFlow::class.java)?.nullable
            ?.let { return it }

        return false
    }

    private fun showWarning(element: Element, text: String) {
        processingEnv.messager.printMessage(
            Diagnostic.Kind.WARNING,
            "AnzidProcessor : $text", element
        )
    }

    private fun showError(element: Element, text: String) {
        processingEnv.messager.printMessage(
            Diagnostic.Kind.ERROR,
            "AnzidProcessor : $text", element
        )
    }

    fun generateFile() {
        val kaptKotlinGeneratedDir = processingEnv.options[KAPT_KOTLIN_GENERATED_OPTION_NAME]

        liveDataMap.forEach {
            val file = File(kaptKotlinGeneratedDir, "${it.key}$END_FILE_NAME.kt")

            it.value.forEach { model ->
                if (isFirstElement) processFirstElementForWrite(file, model)
                else processSubsequentElementForWrite(file, model)
            }

            isFirstElement = true
        }
    }

    private fun processFirstElementForWrite(file: File, model: ObserverGeneratorModel) {
        val defaultFileContent = ObserverObjectBuilder(model).getDefaultContent()
        file.appendText(defaultFileContent)
        isFirstElement = false
    }

    private fun processSubsequentElementForWrite(file: File, model: ObserverGeneratorModel) {
        val subsequentFileContent = ObserverObjectBuilder(model).getAdditionalContent()
        file.appendText(subsequentFileContent)
    }

    private fun isInternalAnnotation(element: Element) = element.getAnnotation(InternalLiveData::class.java) != null
            || element.getAnnotation(InternalSharedFlow::class.java) != null

    private fun isLiveDataAnnotation(element: Element) = element.getAnnotation(PublicLiveData::class.java) != null
            || element.getAnnotation(InternalLiveData::class.java) != null

    private fun isSharedFlowAnnotation(element: Element) = element.getAnnotation(PublicSharedFlow::class.java) != null
            || element.getAnnotation(InternalSharedFlow::class.java) != null

}