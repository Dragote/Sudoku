package io.cyanlab.loinasd.sudoku.models

class Cell(private val number: Int, private var isHidden: Boolean, private val column: Column, private val row: Row, private val sector: Sector) {

    private val pencil = ArrayList<Int>(9)

    val y = row.y
    val x = column.x
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

    fun getSector():Sector{
        return sector
    }

    fun getRow(): Row{
        return row
    }

    fun getColumn(): Column{
        return column
    }
}