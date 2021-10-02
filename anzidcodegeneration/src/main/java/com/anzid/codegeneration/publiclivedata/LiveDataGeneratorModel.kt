package com.anzid.codegeneration.publiclivedata

data class LiveDataGeneratorModel(
    val fieldName: String,
    val pack: String,
    val className: String,
    val typeParameterizedType: String
)