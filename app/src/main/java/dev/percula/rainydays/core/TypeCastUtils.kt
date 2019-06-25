package dev.percula.rainydays.core

inline fun <reified T: Any> List<*>.castToType(): List<T>? =
        this.filterIsInstance(T::class.java)

inline fun <reified T> Any.asList(): List<T>? {
        return (this as? List<*>)?.filterIsInstance(T::class.java)
}

fun <T: Any> Any.asList(template: T): List<T>? {
        return (this as? List<*>)?.filterIsInstance(template::class.java)
}

inline fun <reified K: Any, reified V: Any?> Any.asMap(): Map<K, V>? {
        return (this as? Map<*, *>)?.castToType()
}

inline fun <reified K: Any, reified V: Any?> Any.asMutableMap(): MutableMap<K, V>? {
        return (this as? MutableMap<*, *>)?.castMutableMapToType()
}

inline fun <reified T> Any.asCollection(): Collection<T>? {
        return (this as? Collection<*>)?.filterIsInstance(T::class.java)
}

@Suppress("UNCHECKED_CAST")
inline fun <reified T> Any.asMutableCollection(): MutableCollection<T>? {
        return this as? MutableCollection<T>
}

@Suppress("UNCHECKED_CAST")
inline fun <reified K: Any, reified V: Any?> Map<*,*>.castToType(): Map<K, V>? =
        if (keys.all { it is K } && values.all { it is V }) this as? Map<K, V>
        else null

@Suppress("UNCHECKED_CAST")
inline fun <reified K: Any, reified V: Any?> MutableMap<*,*>.castMutableMapToType(): MutableMap<K, V>? =
        if (keys.all { it is K } && values.all { it is V }) this as? MutableMap<K, V>
        else null