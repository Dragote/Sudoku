package io.cyanlab.loinasd.sudoku.view

import android.content.Context
import io.cyanlab.loinasd.sudoku.models.Cell
import io.cyanlab.loinasd.sudoku.models.Table
import io.cyanlab.loinasd.sudoku.models.games.TableGenerator
import java.util.logging.Logger

class ConsoleView(context: Context?) : ViewOut(context) {

    protected val log: Logger = Logger.getLogger(TableGenerator::class.java.name)

     fun printGameTable(table: Table) {
        for (row in table.penTable) {
            for (n in row) {
                print(n.toString())
            }
        }
        println()
    }

     fun printCompleteTable(table: Table) {
        for (row in table.completeTable) {
            for (n in row) {
                print(n.toString())
            }
        }
        println()
    }

     fun chosen(cell: Cell) {

    }


}