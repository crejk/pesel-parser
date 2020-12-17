package pl.crejk.pesel.parser.util

internal fun Int.isEven(): Boolean = this and 1 == 0

internal fun Int.isOdd(): Boolean = !isEven()
