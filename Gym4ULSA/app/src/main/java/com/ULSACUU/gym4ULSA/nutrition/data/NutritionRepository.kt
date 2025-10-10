package com.ULSACUU.gym4ULSA.nutrition.data

import com.ULSACUU.gym4ULSA.nutrition.model.RootNutrition
import com.google.gson.Gson
import com.google.gson.stream.JsonReader
import android.util.Log
import java.io.StringReader

class NutritionRepository(private val api: NutritionApi) {
    private val gson = Gson()

    suspend fun fetchNutrition(): RootNutrition {
        val raw = api.getNutritionRaw()
        // 1) Try direct parse (raw should already be a pure JSON object)
        try {
            val direct = gson.fromJson(raw.trim(), RootNutrition::class.java)
            if (direct != null && (direct.food_database != null || direct.supplements_catalog != null || direct.nutrition_settings != null)) {
                Log.d("NutritionRepo", "Direct parse OK: foods=${direct.food_database?.size ?: 0}, supps=${direct.supplements_catalog?.size ?: 0}, kcalTarget=${direct.nutrition_settings?.calorie_target_kcal}")
                return direct
            }
        } catch (_: Exception) { /* fall through */ }

        // 2) Fallback: sanitize, unwrap quoted, and lenient parse
        val json1 = sanitizeToJson(raw)
        val json2 = unwrapIfQuotedJson(json1)
        val result = parseLenient(json2)
        Log.d("NutritionRepo", "Fallback parse OK: foods=${result.food_database?.size ?: 0}, supps=${result.supplements_catalog?.size ?: 0}, kcalTarget=${result.nutrition_settings?.calorie_target_kcal}")
        return result
    }

    private fun sanitizeToJson(raw: String): String {
        val s = unescapeHtml(raw).trim()
        if (s.startsWith("{") && s.endsWith("}")) return s

        // If the payload looks like top-level fragment: "key": ... , "key2": ...
        if (s.startsWith("\"")) {
            // Remove leading/trailing commas if any
            var body = s
            if (body.startsWith(",")) body = body.drop(1)
            if (body.endsWith(",")) body = body.dropLast(1)
            return "{" + body + "}"
        }

        // Extract the first balanced JSON object, respecting strings and escapes
        val start = s.indexOf('{')
        if (start == -1) return s // no object detected, return as-is

        var depth = 0
        var inString = false
        var escape = false
        for (i in start until s.length) {
            val c = s[i]
            if (escape) {
                escape = false
                continue
            }
            when (c) {
                '\\' -> if (inString) escape = true
                '"' -> inString = !inString
                '{' -> if (!inString) depth++
                '}' -> if (!inString) {
                    depth--
                    if (depth == 0) {
                        return s.substring(start, i + 1)
                    }
                }
            }
        }
        // If we get here, fallback to previous heuristic: widest braces
        val fallbackStart = s.indexOf('{')
        val fallbackEnd = s.lastIndexOf('}')
        if (fallbackStart >= 0 && fallbackEnd > fallbackStart) {
            return s.substring(fallbackStart, fallbackEnd + 1)
        }
        return s
    }

    private fun unescapeHtml(input: String): String {
        var out = input
        // remove common HTML tags that could wrap the JSON (very light strip)
        out = out.replace(Regex("""<script[\n\r\s\S]*?</script>""", RegexOption.IGNORE_CASE), " ")
        out = out.replace(Regex("""<style[\n\r\s\S]*?</style>""", RegexOption.IGNORE_CASE), " ")
        out = out.replace(Regex("""<[^>]+>"""), " ")
        // unescape common entities
        out = out.replace("&quot;", "\"")
            .replace("&#34;", "\"")
            .replace("&apos;", "'")
            .replace("&#39;", "'")
            .replace("&amp;", "&")
            .replace("&lt;", "<")
            .replace("&gt;", ">")
        return out
    }

    // Handles cases where the payload is a quoted JSON string, e.g. "{ \"version\": ... }"
    private fun unwrapIfQuotedJson(s: String): String {
        val t = s.trim()
        if (t.length >= 2 && t.first() == '"' && t.last() == '"') {
            // Let Gson unescape to a proper JSON string
            return try {
                gson.fromJson(t, String::class.java)
            } catch (_: Exception) {
                // Fallback: strip quotes and unescape common sequences
                t.substring(1, t.length - 1)
                    .replace("\\\"", "\"")
                    .replace("\\n", "\n")
                    .replace("\\t", "\t")
            }
        }
        return t
    }

    private fun parseLenient(json: String): RootNutrition {
        val reader = JsonReader(StringReader(json))
        reader.isLenient = true
        return gson.fromJson(reader, RootNutrition::class.java)
    }
}
