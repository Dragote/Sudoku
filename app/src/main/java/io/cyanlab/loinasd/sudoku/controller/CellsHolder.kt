package io.cyanlab.loinasd.sudoku.controller

import io.cyanlab.loinasd.sudoku.models.Sector
import io.cyanlab.loinasd.sudoku.models.games.Table
import io.cyanlab.loinasd.sudoku.view.Cell

interface CellsHolder{

    val cells: Array<Cell>

    val sectors: Array<Sector>

    val table: Table

    fun row(number: Int) = Array(9, {i -> cells[number / 9 * 9 + i]})
    fun column(number: Int) = Array(9, {i -> cells[number % 9 + i * 9]})

    fun square(sqNumber: Int) = Array(9, { i ->

        val y = sqNumber / 3 * 3 + i / 3
        val x = sqNumber % 3 * 3 + i % 3

        cells[number(y,x)]})

    fun number(y: Int, x: Int) = y * 9 + x

    fun y (number: Int) = number / 9
    fun x (number: Int) = number % 9

    fun getPossibleNumbers(cell: Cell): BooleanArray{

        val number = cells.indexOf(cell)
        return table.getPossibleNumbers(y(number), x(number))
    }

    fun getPencil(cell: Cell) = table.pencil[cells.indexOf(cell)]

    fun isCellHidden(cell: Cell): Boolean{

        val number = cells.indexOf(cell)

        return !table.penTable[y(number)][x(number)]
    }
}