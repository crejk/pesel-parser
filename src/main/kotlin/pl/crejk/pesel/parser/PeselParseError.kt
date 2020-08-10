package pl.crejk.pesel.parser

sealed class PeselParseError(val message: String) {

    data class WrongLength(val input: String) : PeselParseError("Wrong length of '$input'. Length: ${input.length}, expected length $PESEL_LENGTH.")

    data class WrongControlDigit(val input: String, val controlDigit: Int) : PeselParseError("Wrong control digit of '$input'. $controlDigit is not valid control digit.")

    data class WrongDate(val input: String, val birthDate: BirthDate) : PeselParseError("Wrong date of '$input'. $birthDate is not valid date.")
}
