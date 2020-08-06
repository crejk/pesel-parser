package pl.crejk.pesel.parser.fp

sealed class Validated<out E, out V> {

    data class Invalid<E>(val error: E) : Validated<E, Nothing>()
    data class Valid<V>(val value: V) : Validated<Nothing, V>()

    fun <M> map(f: (V) -> M): Validated<E, M> = when (this) {
        is Valid -> f(this.value).valid()
        is Invalid -> this
    }

    fun fold(ifInvalid: (E) -> Unit, ifValid: (V) -> Unit): Unit = when (this) {
        is Valid -> ifValid(this.value)
        is Invalid -> ifInvalid(this.error)
    }
}

fun <V> V.valid(): Validated<Nothing, V> = Validated.Valid(this)
fun <E> E.invalid(): Validated<E, Nothing> = Validated.Invalid(this)

inline fun <E, V, M> Validated<E, V>.flatMap(f: (V) -> Validated<E, M>) = when (this) {
    is Validated.Valid -> f(this.value)
    is Validated.Invalid -> this
}
