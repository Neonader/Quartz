package lexer

import org.junit.jupiter.api.Nested
import java.io.File
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

/**
 * The following tests are for the 3 steps of the indentation algorithm
 */
class IndentationTest {
    /**
     * The following tests check the output of part 1, [filterCodeLines]
     */
    @Nested
    inner class TestFilterCodeLines {
        /**
         * [filterCodeLines] returns an empty list if the input is an empty list
         */
        @Test
        fun testEmptyList() {
            val input = emptyList<String>()
            val expected = emptyList<String>()

            val result = filterCodeLines(input)
            assertEquals(expected, result)
        }
        /**
         * [filterCodeLines] returns the correct filtered list
         */
        @Test
        fun testMixedCodeLines() {
            val input = listOf("Hello,", "  ", "  @ amazing", " world!")
            val expected = listOf("Hello,", " world!")

            val result = filterCodeLines(input)
            assertEquals(expected, result)
        }
    }

    /**
     * The following tests check the output of part 2, [measureLeadingWhitespaces]
     */
    @Nested
    inner class TestMeasureLeadingWhitespaces {
        /**
         * [measureLeadingWhitespaces] returns an empty list if the input is an empty list
         */
        @Test
        fun testEmptyList() {
            val input = emptyList<String>()
            val expected = emptyList<Int>()

            val result = measureLeadingWhitespaces(input)
            assertEquals(expected, result)
        }
        /**
         * [measureLeadingWhitespaces] returns the correct mixed **indentations**
         */
        @Test
        fun testMixedIndentation() {
            val input = listOf("code", "    code", "code", "  code", "      code", "  code")
            val expected = listOf(0, 4, 0, 2, 6, 2)

            val result = measureLeadingWhitespaces(input)
            assertEquals(expected, result)
        }
    }

    /**
     * The following tests check the output of part 3, [calculateIndentLevels]
     */
    @Nested
    inner class TestCalculateIndentLevels {
        /**
         * [calculateIndentLevels] returns an empty list if the input is an empty list
         */
        @Test
        fun testEmptyList() {
            val input = emptyList<Int>()
            val expected = emptyList<Int>()

            val result = calculateIndentLevels(input)
            assertEquals(expected, result)
        }
        /**
         * [calculateIndentLevels] returns a list of zeroes if the input contains same **indentations**
         */
        @Test
        fun testSameIndentLevels() {
            val input = listOf(1, 1, 1, 1)
            val expected = listOf(0, 0, 0, 0)

            val result = calculateIndentLevels(input)
            assertEquals(expected, result)
        }
        /**
         * [calculateIndentLevels] returns the correct mixed **indent levels**
         */
        @Test
        fun testMixedIndentLevels() {
            val input = listOf(0, 3, 3, 6, 3, 0)
            val expected = listOf(0, 1, 1, 2, 1, 0)

            val result = calculateIndentLevels(input)
            assertEquals(expected, result)
        }
        /**
         * [calculateIndentLevels] returns the correct mixed **indent levels**, more complex
         */
        @Test
        fun testComplexIndentLevels() {
            val input = listOf(0, 2, 3, 4, 3, 5, 2, 2, 0, 1, 7, 0, 3, 5, 3, 9, 0, 0, 1)
            val expected = listOf(0, 1, 2, 3, 2, 3, 1, 1, 0, 1, 2, 0, 1, 2, 1, 2, 0, 0, 1)

            val result = calculateIndentLevels(input)
            assertEquals(expected, result)
        }
        /**
         * [calculateIndentLevels] throws an IllegalArgumentException if the **indentation** is incorrect
         */
        @Test
        fun testIndentationError() {
            val input = listOf(0, 2, 1)

            assertFailsWith<IllegalArgumentException>("Indentation error") { calculateIndentLevels(input) }
        }
    }
    /**
     * The following test checks if all 3 parts work together correctly in [applyIndentationAlgorithm]
     */
    @Test
    fun testIndentationAlgorithm() {
        val input = File("src/test/resources/indentation/mixed_example.qz").readLines() // example "code"
        val expected = listOf(0, 1, 0, 1, 2, 3, 2, 0, 1, 2, 2, 0, 1)

        val result = applyIndentationAlgorithm(input)
        assertEquals(expected, result)
    }
}
