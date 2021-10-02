package com.anzid.codegeneration

import com.anzid.annotation.ComponentInjector
import com.anzid.annotation.PublicLiveData
import com.anzid.codegeneration.componentinjector.ComponentInjectorGenerator
import com.anzid.codegeneration.publiclivedata.PublicLiveDataGenerator
import com.google.auto.service.AutoService
import javax.annotation.processing.*
import javax.lang.model.SourceVersion
import javax.lang.model.element.TypeElement

@AutoService(Processor::class)
@SupportedSourceVersion(SourceVersion.RELEASE_8)
@SupportedOptions(AnzidFileGenerator.KAPT_KOTLIN_GENERATED_OPTION_NAME)
class AnzidFileGenerator : AbstractProcessor() {
    companion object {
        const val KAPT_KOTLIN_GENERATED_OPTION_NAME = "kapt.kotlin.generated"
    }

    override fun getSupportedAnnotationTypes(): MutableSet<String> = mutableSetOf(
        ComponentInjector::class.java.name,
        PublicLiveData::class.java.name
    )

    override fun getSupportedSourceVersion(): SourceVersion = SourceVersion.latest()

    override fun process(set: MutableSet<out TypeElement>?, roundEnvironment: RoundEnvironment?): Boolean {
        roundEnvironment?.apply {
            getElementsAnnotatedWith(ComponentInjector::class.java)
                ?.forEach { ComponentInjectorGenerator(processingEnv).prepareClassInitialization(it) }

            processPublicLiveDataAnnotation(this)
        }
        return true
    }

    private fun processPublicLiveDataAnnotation(roundEnvironment: RoundEnvironment) {
        val generator = PublicLiveDataGenerator(processingEnv)

        roundEnvironment.getElementsAnnotatedWith(PublicLiveData::class.java)
            ?.forEach { generator.prepareClassInitialization(it) }

        generator.generateFile()
    }
}