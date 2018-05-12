package io.cyanlab.loinasd.sudoku.models

/**
 * Created by Lev on 01.04.2018.
 */
class Table (var rows: Array<BooleanArray> = Array(9, { BooleanArray(9, { false }) }),
             var columns: Array<BooleanArray> = Array(9, { BooleanArray(9, { false }) }),
             var squares: Array<BooleanArray> = Array(9, { BooleanArray(9, { false }) }),
             var penTable: Array<IntArray> = Array(9, { y -> IntArray(9, { x -> 0 })})
             ){
}