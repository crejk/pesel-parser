package pl.crejk.pesel.parser.fp

import io.vavr.collection.List

fun <E> List<E>.appendWhen(condition: Boolean, f: () -> E): List<E> {
    if (condition) {
        return this.append(f())
    }

    return this
}
