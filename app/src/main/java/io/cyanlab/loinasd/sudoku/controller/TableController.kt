package io.cyanlab.loinasd.sudoku.controller

import android.app.Activity
import android.content.Context
import android.graphics.drawable.Drawable
import android.os.VibrationEffect
import android.os.Vibrator
import android.support.annotation.DrawableRes
import android.view.View
import android.view.ViewGroup
import io.cyanlab.loinasd.sudoku.R
import io.cyanlab.loinasd.sudoku.view.Cell

interface TableController: CellsHolder{

    val control: ViewGroup

    val context: Context

    val parent: android.support.v7.widget.GridLayout
    val pencil: View?

    val numbersSelector: NumbersSelector


    fun showTable(width: Int, margin: Int)

    fun getControlNumbers(width: Int, margin: Int)


    fun colorSquares(@DrawableRes res1: Int, @DrawableRes res2: Int){

        for (i in 0 until 9){

            for (cell in square(i)){
                if (i % 2 != 0){
                    cell.defaultBackground = context.resources.getDrawable(res1)
                }else
                    cell.defaultBackground = context.resources.getDrawable(res2)

                cell.background = cell.defaultBackground
            }
        }
    }

    fun colorSectors(){

        /*val colors = arrayOf(
                context.resources?.getDrawable(R.color.MaterialDarkerCyan),
                context.resources?.getDrawable(R.color.MaterialDarkerViolet),
                context.resources?.getDrawable(R.color.MaterialDarkerYellow),
                context.resources?.getDrawable(R.color.MaterialDarkerRed)
        )*/

        val back = context.resources.getDrawable(R.drawable.cell_default)

        for (sector in sectors){

            sector.cells.forEach {

                it.defaultBackground = back
                //colors[sectors.indexOf(sector)]
                it.background = it.defaultBackground
            }
        }
    }


    var selectedNumber: Int
    var isNumberSelected: Boolean

    fun selectNumber(number: Int, isSelected: Boolean){

        selectedNumber = number
        isNumberSelected = isSelected

        for (index in cells.indices){

            val correct = table.completeTable[y(index)][x(index)]

            val cell = cells[index]

            if (correct == number && !isCellHidden(cell) || getPencil(cell).contains(number))

                cell.background = if (isSelected)

                    context.resources.getDrawable(R.drawable.cell_selected)
                else
                    cell.defaultBackground
        }

        val res =
                if (!isSelected)
                    R.drawable.cell_default_dark
                else
                    R.drawable.cell_selected

        control.getChildAt(selectedNumber - 1)?.background = context.resources?.getDrawable(res)

    }

    var isPencil: Boolean

    fun onPencil(cell: Cell){

        val res: Drawable?

        val pencil = getPencil(cell)

        res = if (!pencil.contains(selectedNumber)){

            pencil.add(selectedNumber)

            context.resources?.getDrawable(R.drawable.cell_selected)

        } else{
            pencil.remove(selectedNumber)

            cell.defaultBackground
        }

        cell.background = res

        cell.invalidate()

    }

    fun onPen(cell: Cell){

        val number = cells.indexOf(cell)


        if (selectedNumber != table.completeTable[y(number)][x(number)]) {

            val vibrator = context.getSystemService(Activity.VIBRATOR_SERVICE) as Vibrator

            vibrator.vibrate(200)

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
                if (table.completeTable[y][x] == selectedNumber && table.penTable[y][x]) {
                    isFinished = false
                    break
                }
            }

        }

        if (isFinished) {
            control.getChildAt(selectedNumber - 1).setOnClickListener(null)
        }
    }

    fun removePencil(cell: Cell){

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