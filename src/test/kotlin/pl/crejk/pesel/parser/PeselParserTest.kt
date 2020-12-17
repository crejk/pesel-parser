package pl.crejk.pesel.parser

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.vavr.collection.List
import java.time.LocalDate

class PeselParserTest : FunSpec({
    val parser = PeselParser()

    test("valid pesel for years 1900-1900") {
        val peselInput = "00010197132"

        val birthDate = LocalDate.of(1900, 0x1, 0x1)
        val serial = Serial(971)
        val gender = Gender(3)
        val controlDigit = ControlDigit(2)

        parser.parse(peselInput).get() shouldBe Pesel(birthDate, serial, gender, controlDigit)
    }

    test("valid pesel for years 2000-2099") {
        val peselInput = "00210169247"

        val birthDate = LocalDate.of(2000, 0x1, 0x1)
        val serial = Serial(692)
        val gender = Gender(4)
        val controlDigit = ControlDigit(7)

        parser.parse(peselInput).get() shouldBe Pesel(birthDate, serial, gender, controlDigit)
    }

    test("pesel with wrong length") {
        val peselInput = "002101692"

        parser.parse(peselInput).error shouldBe PeselParseFailure.WrongLength
    }

    test("pesel with wrong date - given month does not exist") {
        // birthDate 001701 -> 1900/17/01
        val peselInput = "001701" + "97131"

        parser.parse(peselInput).error shouldBe multipleFailures(
            PeselParseFailure.WrongDate
        )
    }

    test("pesel with wrong date - given day does not exist") {
        // birthDate 000230 -> 1900/02/30
        val peselInput = "000230" + "97132"

        parser.parse(peselInput).error shouldBe multipleFailures(
            PeselParseFailure.WrongDate,
            PeselParseFailure.WrongControlDigit
        )
    }
}) {

    companion object {

        private fun multipleFailures(vararg failures: PeselParseFailure): PeselParseFailure.MultipleFailures =
            PeselParseFailure.MultipleFailures(
                List.ofAll(failures.toList())
            )
    }
}
