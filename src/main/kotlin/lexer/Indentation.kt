package lexer

/**
 * Step 1 of the indentation algorithm
 * @param code a list of lines of code, blank lines and commented lines
 * @return a list of lines of code
 */
fun filterCodeLines(code: List<String>): List<String> = code.filter { it.isCode() }

/**
 * Step 2 of the indentation algorithm
 * @param code a list of lines of code
 * @return a list of **indentation** per line
 */
fun measureLeadingWhitespaces(code: List<String>): List<Int> {
    return code.map {
        if (!it[0].isWhitespace()) { 0 }
        else { Regex("\\s+").find(it)!!.value.length }
    }
}

/**
 * Step 3 of the indentation algorithm
 * @param indentations a list of **indentation** per line
 * @return a list of **indent level** per line
 * @throws IllegalArgumentException if a file is incorrectly indented (example: 0,2,1)
 */
fun calculateIndentLevels(indentations: List<Int>): List<Int> {
    if (indentations.isEmpty()) return emptyList()

    // Initialize the indentLevels and indentStack
    val indentLevels = mutableListOf(0) // Start with the first line at level 0
    val indentStack = mutableListOf(indentations[0])

    // Iterate over the remaining lines
    for (i in 1 until indentations.size) {
        val a = indentations[i]
        val b = indentations[i - 1]

        when {
            a == b -> {
                // Case a = b: Same indentation level
                indentLevels.add(indentLevels.last())
            }
            a > b -> {
                // Case a > b: Indentation increased by 1
                indentStack.add(a - b)
                indentLevels.add(indentLevels.last() + 1)
            }
            else -> {
                // Case a < b: Indentation decreased by σ
                var δ = b - a
                var σ = 0
                while (δ > 0) {
                    δ -= indentStack.removeLast()
                    σ++
                }
                if (δ == 0 && indentLevels.last() >= σ) {
                    indentLevels.add(indentLevels.last() - σ)
                } else {
                    throw IllegalArgumentException("Indentation error")
                }
            }
        }
    }
    return indentLevels
}
