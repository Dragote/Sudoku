package io.cyanlab.loinasd.sudoku.models

import io.cyanlab.loinasd.sudoku.models.games.SpecialAreasTable
import io.cyanlab.loinasd.sudoku.view.IOut

/**
 * Created by Анатолий on 27.03.2018.
 */
class WindokuTable(out: IOut): SpecialAreasTable(out) {

    override val specialAreasCoords= arrayOf(
    arrayListOf(intArrayOf(1,1), intArrayOf(1,2), intArrayOf(1,3), intArrayOf(2,1), intArrayOf(2,2), intArrayOf(2,3), intArrayOf(3,1), intArrayOf(3,2), intArrayOf(3,3)),
    arrayListOf(intArrayOf(1,5), intArrayOf(1,6), intArrayOf(1,7), intArrayOf(2,5), intArrayOf(2,6), intArrayOf(2,7), intArrayOf(3,5), intArrayOf(3,6), intArrayOf(3,7)),
    arrayListOf(intArrayOf(5,1), intArrayOf(5,2), intArrayOf(5,3), intArrayOf(6,1), intArrayOf(6,2), intArrayOf(6,3), intArrayOf(7,1), intArrayOf(7,2), intArrayOf(7,3)),
    arrayListOf(intArrayOf(5,5), intArrayOf(5,6), intArrayOf(5,7), intArrayOf(6,5), intArrayOf(6,6), intArrayOf(6,7), intArrayOf(7,5), intArrayOf(7,6), intArrayOf(7,7))
    )

}