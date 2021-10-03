package com.anzid.annotation

/**
 * Generates extension property that returns LiveData<of>
 * Specify "of" parameter only if your LiveData parameter is nullable (see below)
 *
 * For example there is an annotated line of code:
 *     @PublicLiveData private val _x = MutableLiveData<Int>
 * And the corresponding generated property:
 *     public val x: LiveData<Int>
 *
 * If you need to generate a property with an internal modifier - use @InternalLiveData
 * @see InternalLiveData
 *
 * In almost all cases type is automatically inferred,
 * but you need to specify it if you LiveData has nullable type, please use [of] or [nullable]
 *
 * @param nullable parameter of LiveData, MutableLiveData<Example?>
 * for example PublicLiveData(nullable = true)
 *
 * @param of parameter of LiveData, you can to use it if generic contains nullable parts
 * i.e for MutableLiveData<List<Int?>> you need to specify: type = "List<Int?>,
 * for example PublicLiveData("List<Int?>")
 *
 * Generated property itself cannot be nullable,
 * i.e. generate property with type LiveData<of>? is not possible
 */
@Target(AnnotationTarget.FIELD)
@Retention(AnnotationRetention.SOURCE)
@MustBeDocumented
annotation class PublicLiveData(val of: String = "", val nullable: Boolean = false)

/**
 * @see PublicLiveData
 */
@Target(AnnotationTarget.FIELD)
@Retention(AnnotationRetention.SOURCE)
@MustBeDocumented
annotation class InternalLiveData(val of: String = "", val nullable: Boolean = false)

/**
 * @see PublicLiveData
 */
@Target(AnnotationTarget.FIELD)
@Retention(AnnotationRetention.SOURCE)
@MustBeDocumented
annotation class PublicSharedFlow(val of: String = "", val nullable: Boolean = false)

/**
 * @see PublicLiveData
 */
@Target(AnnotationTarget.FIELD)
@Retention(AnnotationRetention.SOURCE)
@MustBeDocumented
annotation class InternalSharedFlow(val of: String = "", val nullable: Boolean = false)