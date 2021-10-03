package com.anzid.annotation

/**
 * The `Generated` annotation is used to mark source code
 * that has been generated.
 * It can also be used to differentiate user written code from generated code
 * in a single file.
 *
 * The `value` element must have the name of the
 * code generator. The recommended convention is to use the fully qualified
 * name of the code generator in the value field,
 * for example `com.company.package.classname`.
 *
 * The `date` element is used to indicate the date the
 * source was generated.
 * The `date` element must follow the ISO 8601 standard.
 * For example, the `date` element could have the
 * value `2001-07-04T12:08:56.235-0700`,
 * which represents 2001-07-04 12:08:56 local time in the U.S. Pacific
 * time zone.
 *
 * The `comment` element is a place holder for any comments
 * that the code generator may want to include in the generated code.
 *
 */
@MustBeDocumented
@Retention(AnnotationRetention.SOURCE)
@Target(AnnotationTarget.FILE,
        AnnotationTarget.ANNOTATION_CLASS,
        AnnotationTarget.CLASS,
        AnnotationTarget.ANNOTATION_CLASS,
        AnnotationTarget.FUNCTION,
        AnnotationTarget.PROPERTY_GETTER,
        AnnotationTarget.PROPERTY_SETTER,
        AnnotationTarget.CONSTRUCTOR,
        AnnotationTarget.FIELD,
        AnnotationTarget.LOCAL_VARIABLE,
        AnnotationTarget.VALUE_PARAMETER)
annotation class AnzidGenerationCode (
        /**
         * The value element must have the name of the code generator.
         * The recommended convention is to use the fully qualified name of the
         * code generator. For example: `com.example.generator.CodeGen`.
         */
        vararg val value: String,
        /**
         * Date when the source was generated.
         */
        val date: String = "",
        /**
         * A place holder for any comments that the code generator may want to
         * include in the generated code.
         */
        val comments: String = "")