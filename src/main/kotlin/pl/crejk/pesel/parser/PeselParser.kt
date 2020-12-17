package pl.crejk.pesel.parser

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

        return PeselCreator.create(digits)
    }
}
