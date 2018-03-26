package io.cyanlab.loinasd.sudoku.models

/**
 * Created by Анатолий on 27.03.2018.
 */
class AsteriskTable: SpecialCellsTable() {
    override val specialCellsCoords: Array<IntArray>
        get() = arrayOf(
                intArrayOf(2, 2),
                intArrayOf(1, 1),
                intArrayOf(2, 0),
                intArrayOf(1, 1),
                intArrayOf(1, 1),
                intArrayOf(1, 1),
                intArrayOf(0, 2),
                intArrayOf(1, 1),
                intArrayOf(0, 0)
        )
}