package pl.crejk.pesel.validator

import io.kotest.core.spec.style.FunSpec

class PeselValidatorTest : FunSpec({
    val validator = PeselValidator()

    test("valid pesel for years 1900-1900") {
        // birthDate 000101 -> 1900/01/01
        // serial 971 -> 971
        // sex 3 -> Man
        // control digit 2 -> 2
        val peselInput = "00010197132"
        val birthDate = BirthDate(Year(1900), Month(0x1), Day(0x1))
        val serial = Serial(971)
        val sex = Sex(3)
        val controlDigit = ControlDigit(2)

        validator.validate(peselInput) shouldBeValid Pesel(birthDate, serial, sex, controlDigit)
    }

    test("valid pesel for years 2000-2099") {
        // birthDate 002101 -> 2000/01/01
        // serial 692 -> 692
        // sex 4 -> Woman
        // control digit 7 -> 7
        val peselInput = "00210169247"
        val birthDate = BirthDate(Year(2000), Month(0x1), Day(0x1))
        val serial = Serial(692)
        val sex = Sex(4)
        val controlDigit = ControlDigit(7)

        validator.validate(peselInput) shouldBeValid Pesel(birthDate, serial, sex, controlDigit)
    }

    test("pesel with wrong length") {
        val peselInput = "0001019713"

        validator.validate(peselInput) shouldBeInvalid PeselValidationError.WrongLength(peselInput)
    }

    test("pesel with wrong control digit") {
        val peselInput = "00010197133"

        validator.validate(peselInput) shouldBeInvalid PeselValidationError.WrongControlDigit(peselInput)
    }

    test("pesel with wrong date") {
        val peselInput = "00010197132"

        validator.validate(peselInput) shouldBeInvalid PeselValidationError.WrongDate(peselInput)
    }
})
