package pl.crejk.pesel.parser

import io.vavr.collection.List
import pl.crejk.pesel.parser.fp.*
import pl.crejk.pesel.parser.util.DateUtil

typealias PeselResult = Validated<PeselParseFailure, Pesel>
typealias KotlinList<E> = kotlin.collections.List<E>

class PeselParser {

    fun parse(input: String): PeselResult {
        val digits = input.split("")
            .mapNotNull { it.toIntOrNull() }

        val birthDate = DateUtil.calculateBirthDate(digits)
        val serial = Serial(digits.subList(6, 9).fold("", { acc, i -> acc + i }).toInt())
        val sex = Gender(digits[digits.lastIndex - 1])
        val controlDigit = this.calculateControlDigit(digits)

        val failures = this.validate(input, digits, birthDate, controlDigit)

        return if (failures.isEmpty) {
            Pesel(birthDate, serial, sex, controlDigit).valid()
        } else {
            failures
                .toNonEmptyUnsafe()
                .invalid()
                .mapInvalid { PeselParseFailure.MultipleFailures(it) }
        }
    }

    private fun validate(
        input: String,
        digits: KotlinList<Int>,
        birthDate: BirthDate,
        controlDigit: ControlDigit
    ): List<PeselParseFailure> =
        List.empty<PeselParseFailure>()
            .appendWhen(digits.size != Constants.PESEL_LENGTH) { PeselParseFailure.WrongLength(input) }
            .appendWhen(!DateUtil.isValidBirthDate(birthDate)) { PeselParseFailure.WrongDate(input, birthDate) }
            .appendWhen(controlDigit.value != digits.last()) {
                PeselParseFailure.WrongControlDigit(
                    input,
                    digits.last()
                )
            }

    private fun calculateControlDigit(digits: KotlinList<Int>): ControlDigit =
        ControlDigit(10 - Constants.WEIGHTS.mapIndexed { index, weight -> digits[index] * weight }.sum() % 10)
}