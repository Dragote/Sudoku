package io.cyanlab.loinasd.sudoku.view

import io.cyanlab.loinasd.sudoku.models.Cell
import io.cyanlab.loinasd.sudoku.models.games.Table

class ConsoleView: IOut {
    override fun printGameTable(table: Table) {
        println("############################")
        for (row in table.penTable) {
            for (n in row) {
                print( if (n == 0) "0 " else n.toString() + " ")
            }
            println()
        }
        println("______________________")
    }

    override fun printCompleteTable(table: Table) {
        println("############################")
        for (row in table.completeTable) {
            for (n in row) {
                print( if (n == 0) "0 " else n.toString() + " ")
            }
            println()
        }
        println("______________________")
    }

    override fun chosen(cell: Cell) {

    }


}