package io.cyanlab.loinasd.sudoku.models

/**
 * Created by Lev on 01.04.2018.
 */
class Table (val rows: Array<BooleanArray>,
             val columns: Array<BooleanArray>,
             val sectors: Array<BooleanArray>,
             val completeTable: Array<IntArray>
             ){
}