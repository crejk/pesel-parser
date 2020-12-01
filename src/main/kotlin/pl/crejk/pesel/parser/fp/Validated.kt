package pl.crejk.pesel.parser.fp

import io.vavr.control.Option

sealed class Validated<out E, out V> {

    data class Invalid<E>(val error: E) : Validated<E, Nothing>()
    data class Valid<V>(val value: V) : Validated<Nothing, V>()

    fun <U> map(f: (V) -> U): Validated<E, U> = when (this) {
        is Valid -> f(this.value).valid()
        is Invalid -> this
    }

    fun <F> mapInvalid(f: (E) -> F): Validated<F, V> = when (this) {
        is Invalid -> f(this.error).invalid()
        is Valid -> this
    }

    inline fun <T> fold(ifInvalid: (E) -> T, ifValid: (V) -> T): T = when (this) {
        is Valid -> ifValid(this.value)
        is Invalid -> ifInvalid(this.error)
    }

    fun unsafe(): V =
        this.fold(
            { throw IllegalStateException("$this is not valid instance") },
            { it }
        )

    fun orNull(): V? = this.getOrElse { null }
}

fun <V> V.valid(): Validated<Nothing, V> = Validated.Valid(this)
fun <E> E.invalid(): Validated<E, Nothing> = Validated.Invalid(this)

inline fun <E, V, T : V> Validated<E, T>.getOrElse(ifInvalid: (E) -> T): V = when (this) {
    is Validated.Valid -> this.value
    is Validated.Invalid -> ifInvalid(this.error)
}

fun <E, V> Validated<E, V>.toOption(): Option<V> =
    this.fold({ Option.none() }, { Option.some(it) })
