package io.cyanlab.loinasd.sudoku.models

internal class Cell(internal val number: Int, internal var isHidden: Boolean,
           internal val column: Int, internal val row: Int, internal val sector: Sector) {



    internal val pencil = ArrayList<Int>(9)

    fun putPencil(number: Int):Boolean{
        if (pencil.contains(number)) return false
        pencil.add(number)
        return true
    }

    fun putPen(number: Int): Boolean{
        if (number != this.number) return false
        isHidden = false
        pencil.removeAll(pencil)
        return true
    }




}