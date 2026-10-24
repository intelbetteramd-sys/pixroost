package app.pixroost.core.spike.oauth

import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive

/** A `share` object without personal data: booleans and `rights` keep their values, the rest only their type. */
fun anonymisedShare(share: JsonObject): String = share.entries.sortedBy { it.key }.joinToString(",") { (key, value) ->
    val keep = key == YandexShareConstants.RIGHTS_KEY || isBoolean(value)
    "$key=" + if (keep && value is JsonPrimitive) value.content else typeName(value)
}

private fun isBoolean(value: JsonElement): Boolean =
    value is JsonPrimitive && !value.isString && (value.content == "true" || value.content == "false")

private fun typeName(value: JsonElement): String = when (value) {
    is JsonObject -> "<объект>"
    is JsonArray -> "<массив>"
    is JsonPrimitive -> if (value.isString) "<строка>" else "<число>"
}
