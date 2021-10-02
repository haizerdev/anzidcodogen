package com.anzid.annotation

import kotlin.reflect.KClass

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.SOURCE)
@MustBeDocumented
annotation class DynamicInjector(val dynamicFeatureImpl: KClass<*>)