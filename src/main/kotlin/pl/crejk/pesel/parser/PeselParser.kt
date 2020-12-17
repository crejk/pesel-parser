package pl.crejk.pesel.parser

import io.vavr.collection.List
import io.vavr.control.Validation
import io.vavr.kotlin.invalid
import io.vavr.kotlin.toVavrList

typealias PeselResult = Validation<PeselParseFailure, Pesel>

class PeselParser {

    fun parse(input: String): PeselResult {
        val digits = input.split("")
            .mapNotNull { it.toIntOrNull() }
            .toVavrList()
        
        if (digits.size() != Constants.PESEL_LENGTH) {
            return invalid(PeselParseFailure.WrongLength)
        }

        val serial = serialOf(digits)
        val gender = genderOf(digits)
        val controlDigit = calculateControlDigit(digits)

        return PeselValidator.validate(digits, serial, gender, controlDigit)
    }

    private fun serialOf(digits: List<Int>): Serial =
        Serial(digits.subSequence(6, 9).fold("", { acc, i -> acc + i }).toInt())

    private fun genderOf(digits: List<Int>): Gender =
        Gender(digits[digits.length() - 2])

    private fun calculateControlDigit(digits: List<Int>): ControlDigit =
        ControlDigit(10 - Constants.WEIGHTS.mapIndexed { index, weight -> digits[index] * weight }.sum() % 10)
}