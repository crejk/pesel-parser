package pl.crejk.pesel.parser

import io.vavr.control.Validation
import io.vavr.kotlin.invalid
import io.vavr.kotlin.list

typealias PeselResult = Validation<PeselParseFailure.MultipleFailures, Pesel>

class PeselParser {

    fun parse(input: String): PeselResult {
        val digits = input.split("")
            .mapNotNull { it.toIntOrNull() }

        //TODO MOVE VALIDATION
        if (digits.size != Constants.PESEL_LENGTH) {
            return invalid(PeselParseFailure.MultipleFailures(list(PeselParseFailure.WrongLength)))
        }

        val serial = Serial(digits.subList(6, 9).fold("", { acc, i -> acc + i }).toInt())
        val gender = Gender(digits[digits.lastIndex - 1])
        val controlDigit = this.calculateControlDigit(digits)

        return PeselValidator.validate(digits, serial, gender, controlDigit)
    }

    // TODO MOVE LOGIC
    private fun calculateControlDigit(digits: List<Int>): Int =
        10 - Constants.WEIGHTS.mapIndexed { index, weight -> digits[index] * weight }.sum() % 10
}