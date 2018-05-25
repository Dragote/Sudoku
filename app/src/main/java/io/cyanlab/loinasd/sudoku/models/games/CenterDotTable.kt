package io.cyanlab.loinasd.sudoku.models.games

class CenterDotTable: Table() {
    override val sectorsCoords = arrayOf(
            arrayOf(
                    intArrayOf(1, 1),
                    intArrayOf(1, 4),
                    intArrayOf(1, 7),
                    intArrayOf(4, 1),
                    intArrayOf(4, 4),
                    intArrayOf(4, 7),
                    intArrayOf(7, 1),
                    intArrayOf(7, 4),
                    intArrayOf(7, 7)
            )
    )
}