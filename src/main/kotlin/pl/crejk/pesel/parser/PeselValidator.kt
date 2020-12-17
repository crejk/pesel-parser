package pl.crejk.pesel.parser

import io.vavr.collection.List
import io.vavr.control.Validation
import io.vavr.kotlin.invalid
import io.vavr.kotlin.valid
import pl.crejk.pesel.parser.util.DateUtil
import java.time.LocalDate

internal typealias PeselValidation<T> = Validation<PeselParseFailure, T>

internal object PeselValidator {

    fun validate(
        digits: List<Int>,
        serial: Serial,
        gender: Gender,
        controlDigitRaw: ControlDigit
    ): PeselResult =
        Validation.combine(validateBirthDate(digits), validateControlDigit(controlDigitRaw, digits.last()))
            .ap { birthDate, controlDigit -> Pesel(birthDate, serial, gender, controlDigit) }
            .mapError { PeselParseFailure.MultipleFailures(it) }

    private fun validateBirthDate(digits: List<Int>): PeselValidation<LocalDate> =
        DateUtil.calculateBirthDate(digits).toValidation(PeselParseFailure.WrongDate)

    private fun validateControlDigit(controlDigit: ControlDigit, lastDigit: Int): PeselValidation<ControlDigit> =
        if (controlDigit.value == lastDigit)
            valid(controlDigit)
        else
            invalid(PeselParseFailure.WrongControlDigit)
}
