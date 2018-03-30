package io.cyanlab.loinasd.sudoku.view

import io.cyanlab.loinasd.sudoku.models.Cell
import io.cyanlab.loinasd.sudoku.models.games.TableGenerator
import java.util.logging.Logger

class ConsoleView: IOut {

    protected val log: Logger = Logger.getLogger(TableGenerator::class.java.name)

    override fun printGameTable(table: TableGenerator) {
        for (row in table.penTable) {
            for (n in row) {
                print(n.toString())
            }
        }
        println()
    }

    override fun printCompleteTable(table: TableGenerator) {
        for (row in table.completeTable) {
            for (n in row) {
                print(n.toString())
            }
        }
        println()
    }

    override fun chosen(cell: Cell) {

    }


}