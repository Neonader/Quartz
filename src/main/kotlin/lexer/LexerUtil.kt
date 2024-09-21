package lexer

/**
 * A centralised method to determine if a line should be ignored by the compiler
 * @return `true` if the line should be used further by the compiler
 */
fun String.isCode(): Boolean {
    return this.isNotBlank() && !this.trimStart().startsWith("@")
}

