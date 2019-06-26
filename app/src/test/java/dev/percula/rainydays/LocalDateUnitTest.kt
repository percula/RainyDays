package dev.percula.rainydays

import org.junit.Assert.assertEquals
import org.junit.Test
import org.threeten.bp.LocalDate
import org.threeten.bp.format.DateTimeFormatter

/**
 * Tests to ensure the LocalDate to string conversions work as expected
 */
class LocalDateUnitTest {

    @Test
    fun localDate_to_String() {
        val date = LocalDate.of(2019, 8, 12)
        val expectedOutput = "2019-08-12"
        val dateFormatter: DateTimeFormatter = DateTimeFormatter.ISO_DATE
        val output = date.format(dateFormatter)

        assertEquals(expectedOutput, output)
    }

    @Test
    fun string_to_localDate() {
        val input = "2019-08-12"
        val expectedOutput = LocalDate.of(2019, 8, 12)
        val output = LocalDate.parse(input)

        assertEquals(expectedOutput, output)
    }

}
