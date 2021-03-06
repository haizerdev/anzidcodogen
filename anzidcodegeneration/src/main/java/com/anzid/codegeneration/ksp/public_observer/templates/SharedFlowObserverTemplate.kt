package com.anzid.codegeneration.ksp.public_observer.templates

import com.anzid.codegeneration.ksp.public_observer.ObserverData

internal class SharedFlowObserverTemplate(model: ObserverData) : ObserverTemplate(model) {

    override fun getAdditionalContent() = """
      
        /**
         * @see ${model.className}.${model.fieldName}
         */
        @Suppress("UNCHECKED_CAST")
        ${model.propertyModifier}val ${model.className}.${publicObserverName} : ${model.publicType}
            get() {
                 val observerPrivate = javaClass.getDeclaredField("${model.fieldName}") 
                 observerPrivate.isAccessible = true 
                 val observer = observerPrivate.get(this) as ${model.originalType}
                 
                 return PublicObserverUtils.asSharedFlow(observer)
            }
        
    """.trimIndent()
}