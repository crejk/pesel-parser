package pl.crejk.pesel.parser

import io.kotest.core.spec.style.FunSpec
import io.vavr.collection.List
import pl.crejk.pesel.parser.fp.toNonEmptyUnsafe

class PeselParserTest : FunSpec({
    val parser = PeselParser()

    test("valid pesel for years 1900-1900") {
        val peselInput = "00010197132"

        val birthDate = BirthDate(Year(1900), Month(0x1), Day(0x1))
        val serial = Serial(971)
        val gender = Gender(3)
        val controlDigit = ControlDigit(2)

        parser.parse(peselInput) shouldBeValid Pesel(birthDate, serial, gender, controlDigit)
    }

    test("valid pesel for years 2000-2099") {
        val peselInput = "00210169247"

        val birthDate = BirthDate(Year(2000), Month(0x1), Day(0x1))
        val serial = Serial(692)
        val gender = Gender(4)
        val controlDigit = ControlDigit(7)

        parser.parse(peselInput) shouldBeValid Pesel(birthDate, serial, gender, controlDigit)
    }

    test("pesel with wrong length and wrong control digit") {
        val peselInput = "0001019713"

        parser.parse(peselInput) shouldBeInvalid multipleFailures(
            PeselParseFailure.WrongLength(peselInput),
            PeselParseFailure.WrongControlDigit(peselInput, 3)
        )
    }

    test("pesel with wrong date - given month does not exist") {
        // birthDate 001701 -> 1900/17/01
        val peselInput = "001701" + "97131"

        parser.parse(peselInput) shouldBeInvalid multipleFailures(
            PeselParseFailure.WrongDate(
                peselInput, BirthDate(Year(1900), Month(17), Day(0x1))
            )
        )
    }

    test("pesel with wrong date - given day does not exist") {
        // birthDate 000230 -> 1900/02/30
        val peselInput = "000230" + "97133"

        parser.parse(peselInput) shouldBeInvalid multipleFailures(
            PeselParseFailure.WrongDate(
                peselInput,
                BirthDate(Year(1900), Month(0x2), Day(30))
            )
        )
    }
}) {

    companion object {

        private fun multipleFailures(vararg failures: PeselParseFailure): PeselParseFailure.MultipleFailures =
            PeselParseFailure.MultipleFailures(
                List.ofAll(failures.toList()).toNonEmptyUnsafe()
            )
    }
}
