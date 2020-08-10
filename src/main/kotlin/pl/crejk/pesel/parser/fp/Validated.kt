package pl.crejk.pesel.parser.fp

import java.util.*

sealed class Validated<out E, out V> {

    data class Invalid<E>(val error: E) : Validated<E, Nothing>()
    data class Valid<V>(val value: V) : Validated<Nothing, V>()

    fun <M> map(f: (V) -> M): Validated<E, M> = when (this) {
        is Valid -> f(this.value).valid()
        is Invalid -> this
    }

    fun unsafe(): V = when (this) {
        is Valid -> this.value
        is Invalid -> throw IllegalStateException("$this is not valid instance")
    }

    fun orNull(): V? = this.getOrElse { null }
}

fun <V> V.valid(): Validated<Nothing, V> = Validated.Valid(this)
fun <E> E.invalid(): Validated<E, Nothing> = Validated.Invalid(this)

inline fun <E, V, T : V> Validated<E, T>.getOrElse(ifInvalid: (E) -> T): V = when (this) {
    is Validated.Valid -> this.value
    is Validated.Invalid -> ifInvalid(this.error)
}

inline fun <V, E, T> Validated<E, V>.fold(ifInvalid: (E) -> T, ifValid: (V) -> T): T = when (this) {
    is Validated.Valid -> ifValid(this.value)
    is Validated.Invalid -> ifInvalid(this.error)
}

inline fun <E, V> Validated<E, V>.onValid(f: (V) -> Unit): Validated<E, V> = when(this) {
    is Validated.Valid -> {
        f(this.value)
        this
    }
    is Validated.Invalid -> this
}

inline fun <E, V> Validated<E, V>.onInvalid(f: (E) -> Unit): Validated<E, V> = when(this) {
    is Validated.Valid -> this
    is Validated.Invalid -> {
        f(this.error)
        this
    }
}

inline fun <E, V, M> Validated<E, V>.flatMap(f: (V) -> Validated<E, M>) = when (this) {
    is Validated.Valid -> f(this.value)
    is Validated.Invalid -> this
}

fun <E, V> Validated<E, V>.toOptional(): Optional<V> =
    this.fold({ Optional.empty() }, { Optional.of(it) })
