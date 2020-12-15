package pl.crejk.pesel.parser.util

import io.vavr.control.Try
import io.vavr.kotlin.Try
import java.time.LocalDate
import java.time.format.DateTimeFormatter

internal object DateUtil {

    fun calculateBirthDate(digits: List<Int>): Try<LocalDate> {
        val dateDigits = digits.toDateDigits()

        val rawYear = dateDigits[0]
        val rawMonth = dateDigits[1]
        val rawDay = dateDigits[2]

        val year = calculateYear(rawYear, rawMonth)
        val month = calculateMonth(rawMonth)
        val day = calculateDay(rawDay)

        return Try { LocalDate.of(year, month, day) }
    }

    private fun List<Int>.toDateDigits(): List<Pair<Int, Int>> =
        this.chunked(2).take(3).flatMap { it.zipWithNext() }

    private fun calculateYear(year: Pair<Int, Int>, month: Pair<Int, Int>): Int = when (month.first) {
        0, 1 -> calculateYear(1900, year)
        2, 3 -> calculateYear(2000, year)
        4, 5 -> calculateYear(2100, year)
        6, 7 -> calculateYear(2200, year)
        8, 9 -> calculateYear(1800, year)
        else -> throw IllegalArgumentException() // should never happen
    }

    private fun calculateYear(century: Int, year: Pair<Int, Int>): Int =
        century + (year.first * 10) + year.second

    private fun calculateMonth(month: Pair<Int, Int>): Int = when (month.first) {
        0, 1 -> calculateMonth(month, 0)
        2, 3 -> calculateMonth(month, 20)
        4, 5 -> calculateMonth(month, 40)
        6, 7 -> calculateMonth(month, 60)
        8, 9 -> calculateMonth(month, 80)
        else -> throw IllegalArgumentException() // should never happen
    }

    private fun calculateMonth(month: Pair<Int, Int>, i: Int): Int =
       (month.first * 10) - i + month.second

    private fun calculateDay(day: Pair<Int, Int>): Int =
       (day.first * 10) + day.second
}
