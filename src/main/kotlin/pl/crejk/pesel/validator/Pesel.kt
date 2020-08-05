package pl.crejk.pesel.validator

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

    override fun toString(): String = "$year/$month/$day"
}

inline class Year(val value: Int)

inline class Month(val value: Int)

inline class Day(val value: Int)

inline class Serial(val value: Int)

enum class Sex {

    MAN,
    WOMAN,
    UNKNOWN,
    ;

    companion object {

        operator fun invoke(value: Int): Sex = if (value.isEven()) WOMAN else if (value.isOdd()) MAN else UNKNOWN

        private fun Int.isEven(): Boolean = this % 2 == 0

        private fun Int.isOdd(): Boolean = this % 2 == 1
    }
}

inline class ControlDigit(val value: Int)
