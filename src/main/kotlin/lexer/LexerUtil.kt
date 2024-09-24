package lexer

/**
 * A centralised method to determine if a line is code or not
 *
 * @receiver a `String` which either is or isn't code (very philosophical)
 * @return `true` if the line should be used further by the compiler
 */
fun String.isCode(): Boolean {
    return this.isNotBlank() && !this.trimStart().startsWith("@")
}

