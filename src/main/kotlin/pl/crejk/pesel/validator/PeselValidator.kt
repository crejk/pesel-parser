package pl.crejk.pesel.validator

import pl.crejk.pesel.validator.fp.Validated
import pl.crejk.pesel.validator.fp.invalid
import pl.crejk.pesel.validator.fp.valid
import pl.crejk.pesel.validator.util.DateUtil

typealias PeselResult = Validated<PeselValidationError, Pesel>

class PeselValidator {

    fun validate(input: String): PeselResult {
        val digits = input.split("").mapNotNull { it.toIntOrNull() }

        if (digits.size < PESEL_LENGTH) {
            return PeselValidationError.WrongLength(input).invalid()
        }

        val birthDate = DateUtil.calculateBirthDate(digits)

        if (!DateUtil.isValidBirthDate(birthDate)) {
            return PeselValidationError.WrongDate(input).invalid()
        }

        val serial = Serial(digits.subList(6, 9).fold("", { acc, i -> acc + i }).toInt())
        val sex = Sex(digits[digits.lastIndex - 1])

        val controlDigit = this.calculateControlDigit(digits)

        if (controlDigit.value != digits.last()) {
            return PeselValidationError.WrongControlDigit(input).invalid()
        }

        return Pesel(birthDate, serial, sex, controlDigit).valid()
    }

    private fun calculateControlDigit(digits: List<Int>): ControlDigit =
        ControlDigit(10 - WEIGHTS.mapIndexed { index, weight -> digits[index] * weight }.sum() % 10)
}
