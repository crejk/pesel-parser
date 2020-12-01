package pl.crejk.pesel.parser.fp

import io.vavr.control.Option
import io.vavr.collection.List

class NonEmptyList<E> private constructor(
    val list: List<E>
) {

    companion object {

        operator fun <E> invoke(list: List<E>): Option<NonEmptyList<E>> =
            Option.`when`(!list.isEmpty) { NonEmptyList(list) }
    }

    fun <U> map(f: (E) -> U): NonEmptyList<U> =
        NonEmptyList(this.list.map(f))

    override fun equals(other: Any?): Boolean {
        if (other !is NonEmptyList<*>) {
            return false
        }

        return this.list == other.list
    }

    override fun hashCode(): Int =
        this.list.hashCode()

    override fun toString(): String =
        "NonEmpty${this.list}"
}

fun <E> List<E>.toNonEmpty(): Option<NonEmptyList<E>> =
    NonEmptyList(this)

fun <E> List<E>.toNonEmptyUnsafe(): NonEmptyList<E> =
    this.toNonEmpty().getOrElseThrow { IllegalArgumentException("Failed requirement.") }
