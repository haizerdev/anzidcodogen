package com.anzid.codegeneration

val TypeMapping = mapOf(
    "java.lang.Object" to "Any",
    "java.lang.Integer" to "Int",
    "java.lang.String" to "String",
    "java.lang.Boolean" to "Boolean",
    "java.lang.Long" to "Long",
    "java.lang.Float" to "Float",
    "java.util.List" to "List",
    "java.util.Set" to "Set",
    "java.util.Map" to "Map",
    "kotlin.Unit" to "Unit"
)

/**
 * Replace types in java type string by transform function.
 * The transform function receives number of type and it's text
 *
 * For example there is a type string:
 *   List<Map<Int, Map<Double, String>>
 * transform function will be called 6 times with the following arguments:
 *   (0, "List") (1, "Map") (2, "Int") (3, "Map") (4, "Double") (5, "String")
 *
 * Result of transform function will be used to replace the corresponding type
 * For example transform function always returns "String". Then the example from above
 * will be converted into:
 *   String<String<String, String<String, String>>
 *
 *  @param typeString - java type represented by string
 *  @param transform - function that will replace types in typeString
 */
fun replaceTypes(typeString: String, transform: (index: Int, type: String) -> String): String {
    var currentTypeIndex = 0
    val sourceSb = StringBuilder()
    val resultSb = StringBuilder()
    for(char in  typeString) {
        when (char) {
            ',', '<', '>' -> {
                val type = transform(currentTypeIndex, sourceSb.toString())
                sourceSb.clear()
                resultSb.append(type).append(char)
                currentTypeIndex++
            }
            ' ' -> Unit
            else -> sourceSb.append(char)
        }
    }
    if(sourceSb.isNotEmpty()) {
        val type = transform(currentTypeIndex, sourceSb.toString())
        resultSb.append(type)
    }
    return resultSb.toString()
}
