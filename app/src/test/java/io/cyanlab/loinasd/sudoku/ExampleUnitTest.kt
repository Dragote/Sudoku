package io.cyanlab.loinasd.sudoku

import io.cyanlab.loinasd.sudoku.models.games.TableGenerator
import io.cyanlab.loinasd.sudoku.models.games.AsteriskTable
import org.junit.Test

import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }

    @Test
    fun tgTest(){

        val table = TableGenerator(AsteriskTable()).generateTable(TableGenerator.DIFFICULTY_EASY)

        print("")

    }
}
