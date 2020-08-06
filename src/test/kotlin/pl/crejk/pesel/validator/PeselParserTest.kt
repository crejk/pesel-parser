package pl.crejk.pesel.validator

import io.kotest.core.spec.style.FunSpec

class PeselParserTest : FunSpec({
    val validator = PeselValidator()

    test("valid pesel for years 1900-1900") {
        val peselInput = "00010197132"

        val birthDate = BirthDate(Year(1900), Month(0x1), Day(0x1))
        val serial = Serial(971)
        val sex = Sex(3)
        val controlDigit = ControlDigit(2)

        validator.validate(peselInput) shouldBeValid Pesel(birthDate, serial, sex, controlDigit)
    }

    test("valid pesel for years 2000-2099") {
        val peselInput = "00210169247"

        val birthDate = BirthDate(Year(2000), Month(0x1), Day(0x1))
        val serial = Serial(692)
        val sex = Sex(4)
        val controlDigit = ControlDigit(7)

        validator.validate(peselInput) shouldBeValid Pesel(birthDate, serial, sex, controlDigit)
    }

    test("pesel with wrong length") {
        val peselInput = "0001019713"

        validator.validate(peselInput) shouldBeInvalid PeselParseError.WrongLength(peselInput)
    }

    test("pesel with wrong control digit") {
        val peselInput = "00010197133"

        validator.validate(peselInput) shouldBeInvalid PeselParseError.WrongControlDigit(peselInput, ControlDigit(2))
    }

    test("pesel with wrong date - given month does not exist") {
        // birthDate 001701 -> 1900/17/01
        val peselInput = "00170197131"

        validator.validate(peselInput) shouldBeInvalid PeselParseError.WrongDate(peselInput, BirthDate(Year(1900), Month(17), Day(0x1)))
    }

    test("pesel with wrong date - given day does not exist") {
        // birthDate 000230 -> 1900/02/30
        val peselInput = "00023097133"

        validator.validate(peselInput) shouldBeInvalid PeselParseError.WrongDate(peselInput, BirthDate(Year(1900), Month(0x2), Day(30)))
    }
})
