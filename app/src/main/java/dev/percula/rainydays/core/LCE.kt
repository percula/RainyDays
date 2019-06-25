package dev.percula.rainydays.core

/**
 * A Loading Content Error data wrapper
 *
 * From https://github.com/fabioCollini/ArchitectureComponentsDemo/blob/master/viewlib/src/main/java/it/codingjam/github/vo/Lce.kt
 */
sealed class LCE<out T> {

    open val data: T? = null

    abstract fun <R> map(f: (T) -> R): LCE<R>

    inline fun doOnData(f: (T) -> Unit) {
        if (this is Success) {
            f(data)
        }
    }

    data class Success<out T>(override val data: T) : LCE<T>() {
        override fun <R> map(f: (T) -> R): LCE<R> = Success(f(data))
    }

    data class Error(val message: String) : LCE<Nothing>() {
        constructor(t: Throwable) : this(t.message ?: "")

        override fun <R> map(f: (Nothing) -> R): LCE<R> = this
    }

    object Loading : LCE<Nothing>() {
        override fun <R> map(f: (Nothing) -> R): LCE<R> = this
    }
}