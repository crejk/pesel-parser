package pl.crejk.pesel.parser

import io.vavr.control.Validation
import io.vavr.kotlin.invalid
import io.vavr.kotlin.valid
import pl.crejk.pesel.parser.util.DateUtil
import java.time.LocalDate

internal typealias PeselValidation <T> = Validation<PeselParseFailure, T>

internal object PeselValidator {

    fun validate(
        digits: List<Int>,
        serial: Serial,
        gender: Gender,
        controlDigitRaw: Int
    ): PeselResult =
        Validation.combine(validateBirthDate(digits), validateControlDigit(controlDigitRaw, digits.last()))
            .ap { birthDate, controlDigit -> Pesel(birthDate, serial, gender, controlDigit) }
            .mapError { PeselParseFailure.MultipleFailures(it) }

    /*private fun validateLength(digits: List<Int>): PeselValidation<List<Int>> =
        if (digits.size != Constants.PESEL_LENGTH)
            valid(digits)
        else
            invalid(PeselParseFailure.WrongLength)*/

    private fun validateBirthDate(digits: List<Int>): PeselValidation<LocalDate> =
        DateUtil.calculateBirthDate(digits).toValidation(PeselParseFailure.WrongDate)

    private fun validateControlDigit(controlDigit: Int, lastDigit: Int): PeselValidation<ControlDigit> =
        if (controlDigit == lastDigit)
            valid(ControlDigit(controlDigit))
        else
            invalid(PeselParseFailure.WrongControlDigit)
}
