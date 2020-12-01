package pl.crejk.pesel.parser

import pl.crejk.pesel.parser.fp.NonEmptyList

sealed class PeselParseFailure {

    abstract fun description(): String

    data class WrongLength(val input: String) : PeselParseFailure() {

        override fun description(): String =
            "Wrong length of '$input'. Length: ${input.length}, expected length ${Constants.PESEL_LENGTH}."
    }

    data class WrongControlDigit(val input: String, val controlDigit: Int) : PeselParseFailure() {

        override fun description(): String =
            "Wrong control digit of '$input'. $controlDigit is not valid control digit."
    }

    data class WrongDate(val input: String, val birthDate: BirthDate) : PeselParseFailure() {

        override fun description(): String =
            "Wrong date of '$input'. $birthDate is not valid date."
    }

    data class MultipleFailures(val failures: NonEmptyList<PeselParseFailure>) : PeselParseFailure() {

        override fun description(): String = failures.map { it.description() }.list.joinToString("\n\n")
    }
}
