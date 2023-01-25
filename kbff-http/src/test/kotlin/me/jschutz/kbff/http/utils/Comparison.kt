package me.jschutz.kbff.http.utils

/**
 * Naive and generic [Map] equality verification.
 */
fun mapsAreEquals(
    map1: Map<*, *>,
    map2: Map<*, *>
): Boolean {
    if (map1.keys.count() != map2.keys.count()) {
        return false
    }

    return map1.entries.all { kv ->
        map2.containsKey(kv.key) && map2[kv.key] == kv.value
    }
}
