package com.anzid.codegeneration.public_observers

data class ObserverGeneratorModel(
    val propertyModifiers: String,
    val fieldName: String,
    val pack: String,
    val className: String,
    val type: String
)