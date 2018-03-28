package io.cyanlab.loinasd.sudoku.view


import io.cyanlab.loinasd.sudoku.models.Cell
import io.cyanlab.loinasd.sudoku.models.games.Table

interface IOut {

    fun printCompleteTable(table: Table)

    fun printGameTable(table: Table)

    fun chosen(cell: Cell)


}
