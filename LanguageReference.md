# Language reference
## Introduction
This section is the language reference for Quartz.

It contains the following subsections:
* [Definitions](#definitions) is a list of definitions
* [Notation](#notation) is a notation for the following grammar
* [Grammar](#grammar) is the syntactic grammar of Quartz
* [Compiler Tricks](#compiler-tricks) is a list of extra syntactic features
* [Examples](#examples) is a selection of example code

## Definitions
This is a list of terms used in the following sections and their meaning.

| Term       | Meaning                                          |
|------------|--------------------------------------------------|
| whitespace | `Char` that satisfies `kotlin.text.isWhitespace` | 
| letter     | `Char` that satisfies `kotlin.text.isLetter`     |
| digit      | `Char` that satisfies `kotlin.text.isDigit`      |

## Notation
This grammatical notation describes the structure of the following grammar.
It's similar to the one used by [Kotlin's language specification](https://kotlinlang.org/spec/syntax-and-grammar.html#notation).

| Notation                 | Meaning                    |
|--------------------------|----------------------------|
| <pre>(A)                 | A grouped for priority     |
| <pre>\[A\]               | A optionally               |
| <pre>{A}                 | A repeated (none or more)  |
| <pre>A B                 | A then B                   |
| <pre>A \| B              | A or B                     |
| <pre>A<br/>&nbsp;&nbsp;B | A with indented B          |
| <pre>'..'                | literal character sequence |
| <pre><..>                | special specification      |

## Grammar
This grammar conveys the syntax of Quartz.
It is not directly used by the Quartz compiler, but it does describe the syntactic rules of Quartz.

QuartzFile
```
{Import <newline>}
ClassBody
```
---
Import
```
'import' Path '.' Id ['#' [Id {',' Id}]]
```
ClassBody
```
{(Declaration | FieldInitialisation) <newline>}
```
---
Path
```
Id {'.' Id}
```
Id
```
<any letter>{<any letter>}
```
Declaration
```
ClassDeclaration | MethodDeclaration | FieldDeclaration | ConstructorDeclaration
```
FieldInitialisation
```
Id {',' Id} `=` Expression
```
---
ClassDeclaration
```
Id
  ClassBody
```
MethodDeclaration
```
Id Id '(' [Id Id {',' Id Id}] ')'
  MethodBody
```
FieldDeclaration
```
Id Id {',' Id Id} ['=' Expression]
```
ConstructorDeclaration
```
Id '(' [Id Id {',' Id Id}] ')'
  ConstructorBody
```
---
Expression
```
```
MethodBody
```
```
ConstructorBody
```
```

## Compiler tricks
This is a list of extra syntactic features not included in the formal syntax of Quartz:
* empty lines are always ignored
* the backslash `\` is ignored unless used as an escape character
    * `\b` escapes Backspace (U+0008)
    * `\t` escapes Tab (U+0009)
    * `\n` escapes Line Feed (U+000A)
    * `\r` escapes Carriage Return (U+000D)
    * `\"` escapes `'"'` (U+0022)
    * `\'` escapes `'''` (U+0027)
    * `\@` escapes `'@'` (U+0040)
    * `\\` escapes `'\'` (U+005C)
* text will be ignored starting from the first unescaped at sign `@`

## Examples
This is a selection of example code to clarify the syntax of Quartz.
