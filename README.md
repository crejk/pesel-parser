# pesel-parser

[![Codacy Badge](https://app.codacy.com/project/badge/Grade/274746f83954429db5bec9975cdfc4a8)](https://www.codacy.com?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=crejk/pesel-parser&amp;utm_campaign=Badge_Grade)

[PESEL](https://en.wikipedia.org/wiki/PESEL)

## usage
```kotlin
val parser = PeselParser()
val result = parser.parse("64092555585"): PeselResult -> Validation<PeselParseFailure, Pesel>

result.fold({ error ->
    error.message
}, { pesel ->
    pesel.toString()
}): String

result.getOrElse { throw RuntimeException(it.message) }: Pesel
result.toOption(): Option<Pesel>
result.orNull(): Pesel?
result.unsafe(): Pesel // if invalid throw IllegalStateException
```
