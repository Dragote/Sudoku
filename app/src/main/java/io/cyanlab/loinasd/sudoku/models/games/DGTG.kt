package io.cyanlab.loinasd.sudoku.models.games

import io.cyanlab.loinasd.sudoku.view.IOut

class DGTG(out: IOut): SpecialAreasTG(out) {

    override val specialAreasCoords = arrayOf(
            arrayListOf(intArrayOf(0,0),
                    intArrayOf(1,1),
                    intArrayOf(2,2),
                    intArrayOf(3,3),
                    intArrayOf(4,4),
                    intArrayOf(5,5),
                    intArrayOf(6,6),
                    intArrayOf(7,7),
                    intArrayOf(8,8)),
            arrayListOf(intArrayOf(0,8),
                    intArrayOf(1,7),
                    intArrayOf(2,6),
                    intArrayOf(3,5),
                    intArrayOf(4,4),
                    intArrayOf(5,3),
                    intArrayOf(6,2),
                    intArrayOf(7,1),
                    intArrayOf(8,0))
    )
}