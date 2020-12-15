package pl.crejk.pesel.parser

import io.vavr.collection.Seq

//TODO make errors more useful
sealed class PeselParseFailure {

    abstract fun description(): String

    object WrongLength : PeselParseFailure() {

        override fun description(): String =
            "Wrong length. Expected length: ${Constants.PESEL_LENGTH}."
    }

    object WrongControlDigit : PeselParseFailure() {

        override fun description(): String =
            "Wrong control digit."
    }

    object WrongDate : PeselParseFailure() {

        override fun description(): String =
            "Wrong date."
    }

    data class MultipleFailures(val failures: Seq<PeselParseFailure>) : PeselParseFailure() {

        override fun description(): String =
            failures.map { it.description() }.joinToString("\n\n")
    }
}
