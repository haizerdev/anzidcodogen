package com.anzid.codegeneration.publiclivedata

class PublicLiveDataObjectBuilder(model: LiveDataGeneratorModel) {

    private val publicLiveDataName = model.fieldName.removePrefix("_")

    private val additionalContentTemplate = """
      
        @Suppress("UNCHECKED_CAST")
        val ${model.className}.${publicLiveDataName} : LiveData<${model.typeParameterizedType}>
            get() {
                 val lvPrivate = javaClass.getDeclaredField("${model.fieldName}") 
                 lvPrivate.isAccessible = true 
                 val lv = lvPrivate.get(this) 
                 
                 return lv as LiveData<${model.typeParameterizedType}>
            }
        
    """.trimIndent()

    private val contentTemplate = """
package ${model.pack}
        
import androidx.lifecycle.LiveData

$additionalContentTemplate
""".trimIndent()

    fun getDefaultContent() = contentTemplate
    fun getAdditionalContent() = additionalContentTemplate
}