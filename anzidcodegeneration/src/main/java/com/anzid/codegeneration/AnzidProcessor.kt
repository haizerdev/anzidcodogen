package com.anzid.codegeneration

import com.anzid.annotation.InternalLiveData
import com.anzid.annotation.PublicLiveData
import com.anzid.codegeneration.public_observers.PublicObserversGenerator
import com.google.auto.service.AutoService
import javax.annotation.processing.*
import javax.lang.model.SourceVersion
import javax.lang.model.element.TypeElement

@AutoService(Processor::class)
@SupportedSourceVersion(SourceVersion.RELEASE_8)
@SupportedOptions(AnzidProcessor.KAPT_KOTLIN_GENERATED_OPTION_NAME)
class AnzidProcessor : AbstractProcessor() {
    companion object {
        const val KAPT_KOTLIN_GENERATED_OPTION_NAME = "kapt.kotlin.generated"
    }

    override fun getSupportedAnnotationTypes(): MutableSet<String> = mutableSetOf(
        PublicLiveData::class.java.name,
        InternalLiveData::class.java.name
    )

    override fun getSupportedSourceVersion(): SourceVersion = SourceVersion.latest()

    override fun process(set: MutableSet<out TypeElement>?, roundEnvironment: RoundEnvironment?): Boolean {
        roundEnvironment?.apply {
            PublicObserversGenerator.processAnnotation(this, processingEnv)
        }
        return true
    }
}