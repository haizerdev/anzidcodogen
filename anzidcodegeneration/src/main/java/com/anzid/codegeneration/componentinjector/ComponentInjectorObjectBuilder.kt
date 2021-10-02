package com.anzid.codegeneration.componentinjector

import javax.lang.model.type.TypeMirror

class ComponentInjectorObjectBuilder(
    className: String,
    packageName: String,
    suffix: String,
    dfi: TypeMirror
) {

    private val dfiClassName = dfi.toString().split(".").takeLast(1)[0]
    private val contentTemplate = """
        package $packageName
        
        import android.content.Context
        import com.dstarlab.icommunicator.Application
        import com.dstarlab.icommunicator.legacy.logging.Log
        import $dfi
        
        object $className {
             private var dynamic${suffix}FeatureImpl: $dfiClassName? = null

             fun setNew${suffix}DynamicFeatureImpl(new${suffix}DynamicFeatureImpl: $dfiClassName) {
                 dynamic${suffix}FeatureImpl = new${suffix}DynamicFeatureImpl
             }
             
             fun removeLinkOn${suffix}DynamicFeatureImpl(appContext: Context) {
                 val app = appContext.applicationContext
                    if (app is Application) {
                        with(app) {
                        try {
                            injectedDynamicModules.remove(get${suffix}DynamicFeatureImpl())
                            dynamicAndroidInjectors.remove(get${suffix}DynamicFeatureImpl().androidInjector())
                        } catch (ex: NullPointerException) {
                            Log.crashlyticsProductionLog(ex)
                        }
                    }
                 }
                 dynamic${suffix}FeatureImpl = null
             }
             
             @Throws(NullPointerException::class)
             fun get${suffix}DynamicFeatureImpl(): $dfiClassName = dynamic${suffix}FeatureImpl?.let { return it }
                     ?: throw NullPointerException("${suffix}DynamicFeatureImpl = null")
        }
    """.trimIndent()

    fun getContent(): String {
        return contentTemplate
    }
}