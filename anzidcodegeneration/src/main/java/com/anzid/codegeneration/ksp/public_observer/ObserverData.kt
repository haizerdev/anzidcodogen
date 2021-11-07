package com.anzid.codegeneration.ksp.public_observer

internal data class ObserverData(
    val propertyModifier: String,
    val fieldName: String,
    val pack: String,
    val className: String,
    val originalType: String,
    val publicType: String
) {
    var isSharedFlowData = false
    var isStateFlowData = false
}