package io.cyanlab.loinasd.sudoku.view

import io.cyanlab.loinasd.sudoku.models.games.Table
import java.util.logging.Logger

interface Loggable {
    val log: Logger
        get() = Logger.getLogger(this.javaClass.name)

    fun printGameTable(table: Table) {
        for (y in 0 until 9) {
            for (x in 0 until 9) {
                print(if (table.penTable[y][x]) table.completeTable[y][x] else 0)
            }
        }
        println()
    }

    fun printCompleteTable(table: Table) {
        for (row in table.completeTable) {//completeTable) {
            for (n in row) {
                print(n.toString())
            }
        }
        println()
    }

    fun printMatrixAsLine(table: Table) {
        for (row in table.rows) {
            for (n in row) {
                print(n.toString())
            }
        }
    }
}