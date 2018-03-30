package io.cyanlab.loinasd.sudoku.models

open class Sector{

    var cells : Array<Cell>? = null
        set(value){
            cells = value
        }
}