package io.cyanlab.loinasd.sudoku.controller

import android.graphics.drawable.Drawable
import android.view.GestureDetector
import android.view.MotionEvent
import io.cyanlab.loinasd.sudoku.R
import io.cyanlab.loinasd.sudoku.view.Cell

class Selector(private val controller: TableController, private val cell: Cell): GestureDetector.SimpleOnGestureListener(), TableController by controller{

    override fun onDown(e: MotionEvent?): Boolean {

        if (!isCellHidden(cell)){

            val index = cells.indexOf(cell)
            val number = table.completeTable[y(index)][x(index)]

            if (selectedNumber != number)
                selectNumber(selectedNumber, false)

            selectNumber(number, true)
            return true
        }

        if (!isNumberSelected)
            return true

        if (!isPencil)
            onPen(cell)
        else
            onPencil(cell)

        return true
    }

    override fun onDoubleTap(e: MotionEvent?): Boolean {

        if (!isPencil || !isCellHidden(cell) || !isNumberSelected){
            return true
        }

        onPen(cell)

        return true
    }

}