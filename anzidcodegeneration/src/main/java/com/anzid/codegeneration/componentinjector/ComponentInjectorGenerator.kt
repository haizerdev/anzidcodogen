package com.anzid.codegeneration.componentinjector

import com.anzid.codegeneration.Generator
import javax.annotation.processing.ProcessingEnvironment
import javax.lang.model.element.Element

class ComponentInjectorGenerator(private val processingEnv: ProcessingEnvironment) : Generator() {
    override fun prepareClassInitialization(element: Element) {
        TODO("Not yet implemented")
    }
}