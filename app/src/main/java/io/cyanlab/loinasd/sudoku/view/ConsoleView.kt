package io.cyanlab.loinasd.sudoku.view

import io.cyanlab.loinasd.sudoku.models.Cell
import io.cyanlab.loinasd.sudoku.models.Table

class ConsoleView(val logging: Boolean) {

/*    fun printGameTable(table: Table) {
        for (row in table.penTable) {
            for (n in row) {
                print(n.toString())
            }
        }
        println()
    }*/

/*     fun printCompleteTable(table: Table) {
        for (row in table.completeTable) {
            for (n in row) {
                print(n.toString())
            }
        }
        println()
    }*/

    fun printMatrixAsLine(table: Table) {
        for (row in table.rows) {
            for (n in row) {
                print(n.toString())
            }
        }
    }

}