package pl.crejk.pesel.parser

import pl.crejk.pesel.parser.util.isEven
import pl.crejk.pesel.parser.util.isOdd
import java.time.LocalDate

data class Pesel(
    val birthDate: LocalDate,
    val serial: Serial,
    val gender: Gender,
    val controlDigit: ControlDigit
)

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
