# pesel-parser
[PESEL](https://en.wikipedia.org/wiki/PESEL)

## usage
```kotlin
    val parser = PeselParser()
    val result = parser.parse("64092555585"): PeselResult -> Validated<PeselParseError, Pesel>

    result.onValid { pesel ->
        println(pesel)
    }: Validated<PeselParseError, Pesel>

    result.fold({ error ->
        println(error.message)
    }, { pesel ->
        println(pesel)
    }): T

    result.getOrElse { throw RuntimeException(it.message) }: Pesel
    result.toOptional(): Optional<Pesel>
    result.orNull(): Pesel?
    result.unsafe(): Pesel // if invalid throw IllegalStateException
```
