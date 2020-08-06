package pl.crejk.pesel.validator

import io.kotest.matchers.Matcher
import io.kotest.matchers.MatcherResult
import io.kotest.matchers.should
import pl.crejk.pesel.validator.fp.Validated
import pl.crejk.pesel.validator.fp.invalid
import pl.crejk.pesel.validator.fp.valid

infix fun <T> Validated<*, T>.shouldBeValid(value: T) = this should beValid(value)

fun <V> beValid(v: V) = object : Matcher<Validated<*, V>> {
    override fun test(value: Validated<*, V>): MatcherResult =
        MatcherResult(value == v.valid(), "$value should be Valid(value=$v)", "$value should not be Valid(value=$v)")
}

infix fun <E> Validated<E, *>.shouldBeInvalid(value: E) = this should beInvalid(value)

fun <E> beInvalid(e: E) = object : Matcher<Validated<E, *>> {
    override fun test(value: Validated<E, *>): MatcherResult =
        MatcherResult(value == e.invalid(), "$value should be Invalid(error=$e)", "$value should not be Invalid(error=$e)")
}
