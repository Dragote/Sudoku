package io.cyanlab.loinasd.sudoku.models.games

/**
 * Created by Анатолий on 27.03.2018.
 */
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