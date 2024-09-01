# Workings
## Introduction
This section explains the internal workings of the Quartz compiler.
It is both a technical reference and a fancy yap session about how I organized the code.

It contains the following subsections:
* [Lexing](#lexing) explains how the code is read
  * [Indentation](#indentation) explains the indent level algorithm

## Lexing
This section explains in steps how the compiler lexes the code into tokens.

The Quartz Compiler does not parse like expression-based languages do.
Expression-based languages such as Kotlin and Rust treat every byte as a sequence of characters.
Indentation is insignificant in these languages, since the amount of whitespaces has no effect.
In cases such as Rust and Java line breaks are also just whitespaces to the compiler.
This means that indentation is purely decorative and for readability purposes in these languages.

Quartz replaces curly brace syntax with significant indentation, like Python and Haskell do.
Most IDE's and (sane) people indent code, so this makes the code easier to write.

Just removing curly braces from syntax introduces ambiguity and makes code unclear.
This confuses not only the programmer, but also the compiler.
Therefore, in order to handle significant indentation, the Quartz compiler handles code line-by-line.

#### Indentation
Each line has an integer amount of whitespaces preceding it.
One of the first things the compiler does is figuring out how deeply indented a line is, its indent level.
This information is then easily usable by other parts of the compiler.
See the following code:
```
A
    B
    C
      D
    E
F
```
The compiler reads the indentations `0, 4, 4, 6, 4, 0` and needs an algorithm to convert it to `0, 1, 1, 2, 1, 0`.
The following indentation results in an error at compile time:
```
A
    B
  C
```
This is because `C` is an indent level below `B`, but it does not have the same amount of leading whitespaces as `A`.
Since `A` is (always) level 0 and `B` is level 1 in this case, `C` does not belong to any level in between them.
