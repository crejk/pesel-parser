package pl.crejk.pesel.parser.util

import pl.crejk.pesel.parser.BirthDate
import pl.crejk.pesel.parser.Day
import pl.crejk.pesel.parser.Month
import pl.crejk.pesel.parser.Year
import java.text.ParseException
import java.text.SimpleDateFormat

internal object DateUtil {

    private val FORMAT = SimpleDateFormat("yyyy/MM/dd").also { it.isLenient = false; }

    fun isValidBirthDate(birthDate: BirthDate): Boolean =
        try {
            FORMAT.parse(birthDate.toString())
            true
        } catch (e: ParseException) {
            false
        }

    fun calculateBirthDate(digits: List<Int>): BirthDate {
        val dateDigits = digits.toDateDigits()

        val rawYear = dateDigits[0]
        val rawMonth = dateDigits[1]
        val rawDay = dateDigits[2]

        val year = calculateYear(rawYear, rawMonth)
        val month = calculateMonth(rawMonth)
        val day = calculateDay(rawDay)

        return BirthDate(year, month, day)
    }

    private fun List<Int>.toDateDigits(): List<Pair<Int, Int>> =
        this.chunked(2).take(3).flatMap { it.zipWithNext() }

    private fun calculateYear(year: Pair<Int, Int>, month: Pair<Int, Int>): Year = when (month.first) {
        0, 1 -> calculateYear(1900, year)
        2, 3 -> calculateYear(2000, year)
        4, 5 -> calculateYear(2100, year)
        6, 7 -> calculateYear(2200, year)
        8, 9 -> calculateYear(1800, year)
        else -> throw IllegalArgumentException() // should never happen
    }

    private fun calculateYear(century: Int, year: Pair<Int, Int>): Year =
        Year(century + (year.first * 10) + year.second)

    private fun calculateMonth(month: Pair<Int, Int>): Month = when (month.first) {
        0, 1 -> calculateMonth(month, 0)
        2, 3 -> calculateMonth(month, 20)
        4, 5 -> calculateMonth(month, 40)
        6, 7 -> calculateMonth(month, 60)
        8, 9 -> calculateMonth(month, 80)
        else -> throw IllegalArgumentException() // should never happen
    }

    private fun calculateMonth(month: Pair<Int, Int>, i: Int): Month =
        Month((month.first * 10) - i + month.second)

    private fun calculateDay(day: Pair<Int, Int>): Day =
        Day((day.first * 10) + day.second)
}
