package io.cyanlab.loinasd.sudoku.view

import io.cyanlab.loinasd.sudoku.models.games.Table

class ConsoleView(val logging: Boolean) {

/*    fun printGameTable(table: Table) {
        for (y in table.penTable) {
            for (n in y) {
                print(n.toString())
            }
        }
        println()
    }*/

/*     fun printCompleteTable(table: Table) {
        for (y in table.completeTable) {
            for (n in y) {
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