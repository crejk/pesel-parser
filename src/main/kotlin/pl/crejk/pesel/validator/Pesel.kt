package pl.crejk.pesel.validator

import pl.crejk.pesel.validator.util.isEven
import pl.crejk.pesel.validator.util.isOdd

data class Pesel(
    val birthDate: BirthDate,
    val serial: Serial,
    val sex: Sex,
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

enum class Sex {

    MAN,
    WOMAN;

    companion object {

        operator fun invoke(value: Int): Sex = when {
            value.isEven() -> WOMAN
            value.isOdd() -> MAN
            else -> throw IllegalArgumentException() //should never happen
        }
    }
}

inline class ControlDigit(val value: Int)
