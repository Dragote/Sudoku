package io.cyanlab.loinasd.sudoku.models

import java.util.*

/**
 * Created by Анатолий on 26.03.2018.
 */
abstract class SpecialCellsTable: Table() {

    abstract val specialCellsCoords : Array<IntArray>

    private lateinit var specialCells : BooleanArray

    override fun generate(): Boolean {
        specialCells = BooleanArray(9,{false})
        return super.generate()
    }

    override fun fillBuffer(buffer: Array<IntArray>, square: BooleanArray, sqNumber: Int): Boolean {

        val offsetX = sqNumber % 3 * 3
        val offsetY = sqNumber / 3 * 3

        val specY = specialCellsCoords[sqNumber][0]
        val specX = specialCellsCoords[sqNumber][1]

        if (!fillCell(specY, specX, buffer, BooleanArray(9, { i: Int -> square[i] || rows[specY+ offsetY][i] || columns[specX+ offsetX][i] || specialCells[i]
                }), square)) return false

        for (y in 0..2){
            for (x in 0..2){
                if (x != specX || y != specY) {
                    if (!fillCell(y, x, buffer, BooleanArray(9, { i: Int -> square[i] || rows[y + offsetY][i] || columns[x + offsetX][i]
                                    || if (y == specialCellsCoords[sqNumber][0] && x == specialCellsCoords[sqNumber][1]) specialCells[i] else false
                            }), square)) return false
                }
            }
        }
        return true
    }

    override fun pushBuffer(buffer: Array<IntArray>, offsetY: Int, offsetX: Int, sqNumber: Int) {

        val specY = specialCellsCoords[sqNumber][0]
        val specX = specialCellsCoords[sqNumber][1]

        specialCells[buffer[specY][specX] - 1] = true

        for (y in 0..2)
            for (x in 0..2){
                table[y + offsetY][x + offsetX] = buffer[y][x]
                rows[y+offsetY][buffer[y][x] - 1] = true
                columns[x+offsetX][buffer[y][x] - 1] = true
            }
    }



}