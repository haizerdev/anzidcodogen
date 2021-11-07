package com.anzid.codegeneration.ksp

import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.symbol.KSDeclaration
import java.io.OutputStream

internal fun OutputStream.appendText(str: String) {
    this.write(str.toByteArray())
}

internal fun KSPLogger.fail(message: String, declaration: KSDeclaration): Nothing {
    error(message, declaration)
    throw AssertionError()
}