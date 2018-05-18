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
            if (highlightedNumber != number)
                highlightNumber(highlightedNumber, false)

            highlightNumber(number, true)
            return true
        }

        if (!isPencil)
            onPen(cell)
        else
            onPencil(cell)

        return true
    }

    override fun onDoubleTap(e: MotionEvent?): Boolean {

        if (!isPencil || !isCellHidden(cell)){
            return true
        }

        onPen(cell)

        return true
    }


    private fun onPencil(cell: Cell){

        val res: Drawable?

        val pencil = getPencil(cell)

        res = if (!pencil.contains(highlightedNumber)){

            pencil.add(highlightedNumber)

            context.resources?.getDrawable(R.drawable.cell_selected)

        } else{
            pencil.remove(highlightedNumber)

            cell.defaultBackground
        }

        cell.background = res

        cell.invalidate()

    }

    private fun onPen(cell: Cell){

        val number = cells.indexOf(cell)


        if (highlightedNumber != table.completeTable[y(number)][x(number)]) {

            return
        }

        cell.text = table.completeTable[y(number)][x(number)].toString()

        table.cellEntered(y(number), x(number))

        cell.background = context.resources?.getDrawable(R.drawable.cell_selected)

        getPencil(cell).clear()

        removePencil(cell)

        var isFinished = true

        for (y in table.penTable.indices) {
            for (x in table.penTable[y].indices){
                if (table.completeTable[y][x] == highlightedNumber && table.penTable[y][x]) {
                    isFinished = false
                    break
                }
            }

        }

        if (isFinished) {
            control.getChildAt(highlightedNumber - 1).setOnClickListener(null)
        }
    }

    private fun removePencil(cell: Cell){

        val neighbours = ArrayList<Cell>()
        val number = cells.indexOf(cell)

        neighbours.addAll(row(number))
        neighbours.addAll(column(number))
        neighbours.addAll(square(number / 27 * 3 + (number % 9) / 3))


        for (sector in sectors){

            if (sector.cells.contains(cell)){
                neighbours.addAll(sector.cells)
            }
        }

        for (neighbour in neighbours){

            if (isCellHidden(neighbour) && getPencil(neighbour).contains(table.completeTable[y(number)][x(number)])){

                getPencil(neighbour).remove(table.completeTable[y(number)][x(number)])
                neighbour.background = neighbour.defaultBackground
                neighbour.invalidate()
            }
        }
    }

}