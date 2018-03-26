package io.cyanlab.loinasd.sudoku.models

/**
 * Created by Анатолий on 27.03.2018.
 */
class CenterDotTable: SpecialCellsTable() {
    override val specialCellsCoords: Array<IntArray>
        get() = arrayOf(
                intArrayOf(1, 1),
                intArrayOf(1, 1),
                intArrayOf(1, 1),
                intArrayOf(1, 1),
                intArrayOf(1, 1),
                intArrayOf(1, 1),
                intArrayOf(1, 1),
                intArrayOf(1, 1),
                intArrayOf(1, 1)
        )
}