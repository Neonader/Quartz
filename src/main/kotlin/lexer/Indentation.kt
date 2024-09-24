package lexer

/**
 * The indentation algorithm
 *
 * Each line has an integer amount of whitespaces preceding it, its **indentation**.
 * One of the first things the compiler does is figuring out how deeply indented a line is, its **indent level**.
 * This information is then usable by other parts of the compiler.
 * See the following code:
 * ```
 * A
 *     B
 *     C
 *       D
 * E
 * ```
 * The indentation algorithm converts a list of **indentations** to a list of **indent levels**.
 * The compiler can read the **indentations** `0,4,4,6,0`, but it needs an algorithm to convert them to `0,1,1,2,0`.
 * The following **indentations** result in an error:
 * ```
 * A
 *     B
 *   C
 * ```
 * This is because `C` does not correspond to an **indent level**.
 * Since `A` is **indent level** 0 and `B` is **indent level** 1 in this case, `C` does not belong to any level.
 *
 * The first step takes a list of lines of code and filters out the non-code lines (blank or starting with `@`).
 *
 * The second step of the algorithm measures the **indentation** of each line of code.
 *
 * The third step is the most important: converting a list of **indentations** to **indent levels**.
 * It does this by keeping an `indentStack`.
 * For line 0: `indentStack[0] = indentations[0]`.
 * Starting from line 2 the algorithm uses the old indentation `b` and the new indentation `a`.
 * `a = indentations[<line #>]`, `b = indentations[<line #> - 1]`
 * There are 3 cases:
 * `a = b`:
 * - append `indentLevels.last()` to `indentLevels`
 * `a > b`:
 * - append `a - b` to `indentStack`
 * - append `indentLevels.last() + 1` to `indentLevels`
 * `a < b`:
 * - store `δ = b - a` and `σ = 0`
 * - do `δ -= indentStack.removeLast(); σ++` while `δ > 0`
 * - if `δ = 0` append `indentLevels.last() - σ` to `indentLevels` else throw an indentation error
 *
 * @param code a list of lines of code, blank lines and commented lines (from which to compute **indent levels**)
 * @return a list of **indent level** per line (of code)
 * @throws IllegalArgumentException if a file is incorrectly indented
 */
fun applyIndentationAlgorithm(code: List<String>): List<Int> {
    // step 1
    val filteredCode = filterCodeLines(code)
    // step 2
    val indentations = measureLeadingWhitespaces(filteredCode)
    // step 3
    val indentLevels = calculateIndentLevels(indentations)
    return indentLevels
}

/**
 * Step 1 of the indentation algorithm
 *
 * @param code a list of lines of code, blank lines and commented lines
 * @return a list of lines of code
 */
fun filterCodeLines(code: List<String>): List<String> = code.filter { it.isCode() }

/**
 * Step 2 of the indentation algorithm
 *
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
 *
 * @param indentations a list of **indentation** per line
 * @return a list of **indent level** per line
 * @throws IllegalArgumentException if a file is indented incorrectly
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
                // Case a = b: Same indentation
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
