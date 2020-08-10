package pl.crejk.pesel.parser

import pl.crejk.pesel.parser.fp.Validated
import pl.crejk.pesel.parser.fp.invalid
import pl.crejk.pesel.parser.fp.valid
import pl.crejk.pesel.parser.util.DateUtil

typealias PeselResult = Validated<PeselParseError, Pesel>

class PeselParser {

    fun parse(input: String): PeselResult {
        val digits = input.split("").mapNotNull { it.toIntOrNull() }

        if (digits.size != PESEL_LENGTH) {
            return PeselParseError.WrongLength(input).invalid()
        }

        val birthDate = DateUtil.calculateBirthDate(digits)

        if (!DateUtil.isValidBirthDate(birthDate)) {
            return PeselParseError.WrongDate(input, birthDate).invalid()
        }

        val serial = Serial(digits.subList(6, 9).fold("", { acc, i -> acc + i }).toInt())
        val sex = Sex(digits[digits.lastIndex - 1])

        val controlDigit = this.calculateControlDigit(digits)

        if (controlDigit.value != digits.last()) {
            return PeselParseError.WrongControlDigit(input, controlDigit).invalid()
        }

        return Pesel(birthDate, serial, sex, controlDigit).valid()
    }

    private fun calculateControlDigit(digits: List<Int>): ControlDigit =
        ControlDigit(10 - WEIGHTS.mapIndexed { index, weight -> digits[index] * weight }.sum() % 10)
}
