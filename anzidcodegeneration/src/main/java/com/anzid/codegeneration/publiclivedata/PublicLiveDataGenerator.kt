package com.anzid.codegeneration.publiclivedata

import com.anzid.codegeneration.AnzidFileGenerator.Companion.KAPT_KOTLIN_GENERATED_OPTION_NAME
import com.anzid.codegeneration.Generator
import java.io.File
import javax.annotation.processing.ProcessingEnvironment
import javax.lang.model.element.Element

class PublicLiveDataGenerator(private val processingEnv: ProcessingEnvironment) : Generator() {
    companion object {
        const val END_FILE_NAME = "LvContainer"
    }

    private var liveDataMap = mutableMapOf<String, MutableSet<LiveDataGeneratorModel>>()

    private var isFirstElement = true

    override fun prepareClassInitialization(element: Element) {
        val fieldName = element.simpleName.toString()
        val pack = processingEnv.elementUtils.getPackageOf(element).toString()
        val typeLiveData = element.asType().toString()
        val enclosingElement = element.enclosingElement.simpleName.toString()

        val parameterizedType = typeLiveData.split('<').takeLast(1)[0].removeSuffix(">")
        val parameterizedTypeClassName = parameterizedType.split(".").takeLast(1)[0]

        val simpleParameterizableType = getSimpleParameterizableType(parameterizedType)

        with(enclosingElement) {
            if (liveDataMap.containsKey(this)) {
                liveDataMap[this]?.add(LiveDataGeneratorModel(fieldName, pack, this@with, simpleParameterizableType))
                return@with
            }
            liveDataMap[this] = mutableSetOf<LiveDataGeneratorModel>()
                .apply { add(LiveDataGeneratorModel(fieldName, pack, this@with, simpleParameterizableType)) }
        }
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

    private fun getSimpleParameterizableType(parameterizedType: String) = when {
        parameterizedType.contains(DataType.Object.name) -> DataType.Object.kotlinType
        parameterizedType.contains(DataType.Integer.name) -> DataType.Integer.kotlinType
        parameterizedType.contains(DataType.String.name) -> DataType.String.kotlinType
        parameterizedType.contains(DataType.Boolean.name) -> DataType.Boolean.kotlinType
        parameterizedType.contains(DataType.Long.name) -> DataType.Long.kotlinType
        parameterizedType.contains(DataType.Float.name) -> DataType.Float.kotlinType
        else -> parameterizedType
    }

    private fun processFirstElementForWrite(file: File, liveDataModel: LiveDataGeneratorModel) {
        val defaultFileContent = PublicLiveDataObjectBuilder(liveDataModel).getDefaultContent()
        file.appendText(defaultFileContent)
        isFirstElement = false
    }

    private fun processSubsequentElementForWrite(file: File, liveDataModel: LiveDataGeneratorModel) {
        val subsequentFileContent = PublicLiveDataObjectBuilder(liveDataModel).getAdditionalContent()
        file.appendText(subsequentFileContent)
    }
}