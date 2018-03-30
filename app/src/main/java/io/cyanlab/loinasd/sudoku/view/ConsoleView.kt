package io.cyanlab.loinasd.sudoku.view

import io.cyanlab.loinasd.sudoku.models.Cell
import io.cyanlab.loinasd.sudoku.models.games.TableGenerator
import java.util.logging.Logger

class ConsoleView: IOut {

    protected open val log: Logger = Logger.getLogger(TableGenerator::class.java.name)

    override fun printGameTable(table: TableGenerator) {
        println("############################")
        for (row in table.penTable) {
            for (n in row) {
                print(n.toString())
            }
        }
        println("______________________")
    }

    override fun printCompleteTable(table: TableGenerator) {
        println("############################")
        for (row in table.completeTable) {
            for (n in row) {
                log.info(n.toString())
            }
        }
        println("______________________")
    }

    override fun chosen(cell: Cell) {

    }


}