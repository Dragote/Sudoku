package io.cyanlab.loinasd.sudoku.models.games

class AsteriskTable: Table() {

    override val sectorsCoords = arrayOf(
            arrayOf(
                    intArrayOf(2, 2),
                    intArrayOf(1, 4),
                    intArrayOf(2, 6),
                    intArrayOf(4, 1),
                    intArrayOf(4, 4),
                    intArrayOf(4, 7),
                    intArrayOf(6, 2),
                    intArrayOf(7, 4),
                    intArrayOf(6, 6)
            )
    )
}