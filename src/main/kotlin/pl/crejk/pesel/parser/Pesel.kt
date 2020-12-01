package pl.crejk.pesel.parser

import pl.crejk.pesel.parser.util.isEven
import pl.crejk.pesel.parser.util.isOdd

data class Pesel(
    val birthDate: BirthDate,
    val serial: Serial,
    val gender: Gender,
    val controlDigit: ControlDigit
)

data class BirthDate(
    val year: Year,
    val month: Month,
    val day: Day
) {

    override fun toString(): String = "${year.value}/${month.value}/${day.value}"
}

inline class Year(val value: Int)

inline class Month(val value: Int)

inline class Day(val value: Int)

inline class Serial(val value: Int)

enum class Gender {

    MAN,
    WOMAN;

    companion object {

        operator fun invoke(value: Int): Gender = when {
            value.isEven() -> WOMAN
            value.isOdd() -> MAN
            else -> throw IllegalArgumentException() // should never happen
        }
    }
}

inline class ControlDigit(val value: Int)
