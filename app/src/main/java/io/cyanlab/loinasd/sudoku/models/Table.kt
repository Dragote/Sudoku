package io.cyanlab.loinasd.sudoku.models

/**
 * Created by Lev on 01.04.2018.
 */
class Table {

    lateinit var columns: Array<BooleanArray>
    lateinit var rows: Array<BooleanArray>
    lateinit var squares: Array<BooleanArray>
    protected lateinit var trials: BooleanArray
    open lateinit var penTable: Array<IntArray>
    open lateinit var completeTable: Array<IntArray>



    init {
        completeTable = Array(9, { IntArray(9, { j -> 0 }) })
        rows = Array(9, { i -> BooleanArray(9, { false }) })
        columns = Array(9, { i -> BooleanArray(9, { false }) })
    }
}