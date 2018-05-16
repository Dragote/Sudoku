package io.cyanlab.loinasd.sudoku.models

import android.content.Context
import android.widget.TextView

class Cell(context: Context, val number: Int, var isHidden: Boolean,
           val x: Int, val y: Int) : TextView(context){

    val pencil = ArrayList<Int>(9)

    fun putPencil(number: Int): Boolean{
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