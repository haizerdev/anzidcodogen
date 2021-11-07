package com.anzid.codegeneration.ksp.public_observer

import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import com.google.devtools.ksp.processing.SymbolProcessorProvider

class PublicObserverProcessorProvider : SymbolProcessorProvider {
    override fun create(
        env: SymbolProcessorEnvironment
    ): SymbolProcessor {
        return PublicObserverProcessor(env.codeGenerator, env.logger)
    }
}