package io.cyanlab.loinasd.sudoku.models.games

/**
 * Created by Lev on 01.04.2018.
 */
open class Table{

    var rows: Array<BooleanArray> = emptyArray()
    var columns: Array<BooleanArray> = emptyArray()
    var squares: Array<BooleanArray> = emptyArray()
    var completeTable: Array<IntArray> = emptyArray()
    var penTable: Array<BooleanArray> = emptyArray()


    open val sectorsCoords : Array<Array<IntArray>> = emptyArray()
    var sectors : Array<BooleanArray> = emptyArray()
    var sectors4Cells:  Array<Array<BooleanArray>> = emptyArray()

}