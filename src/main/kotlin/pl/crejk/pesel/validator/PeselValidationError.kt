package pl.crejk.pesel.validator

sealed class PeselValidationError(val message: String) {

    data class WrongLength(val input: String) : PeselValidationError("Wrong length of '$input', expected length $PESEL_LENGTH.")

    data class WrongControlDigit(val input: String) : PeselValidationError("Wrong control digit of '$input'.")

    data class WrongDate(val input: String) : PeselValidationError("Wrong date of '$input'.")
}
