package io.cyanlab.loinasd.sudoku.view

import io.cyanlab.loinasd.sudoku.models.Table
import java.util.logging.Logger

interface Loggable {
    val log: Logger
        get() = Logger.getLogger(this.javaClass.name)

    fun printGameTable(table: Table) {
        for (row in table.penTable) {
            for (n in row) {
                print(n.toString())
            }
        }
        println()
    }

    fun printCompleteTable(table: Table) {
        for (row in table.penTable) {//completeTable) {
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