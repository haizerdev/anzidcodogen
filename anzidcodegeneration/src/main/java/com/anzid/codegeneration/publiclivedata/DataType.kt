package com.anzid.codegeneration.publiclivedata

enum class DataType(val kotlinType: kotlin.String) {
    Object("Any"),
    Integer("Int"),
    String("String"),
    Boolean("Boolean"),
    Long("Long"),
    Float("Float");
}