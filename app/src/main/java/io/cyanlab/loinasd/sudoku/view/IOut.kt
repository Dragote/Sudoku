package io.cyanlab.loinasd.sudoku.view


import io.cyanlab.loinasd.sudoku.models.Cell
import io.cyanlab.loinasd.sudoku.models.games.TableGenerator

interface IOut {

    fun printCompleteTable(table: TableGenerator)

    fun printGameTable(table: TableGenerator)

    fun chosen(cell: Cell)


}
