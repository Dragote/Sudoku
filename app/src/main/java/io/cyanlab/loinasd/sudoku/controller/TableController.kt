package io.cyanlab.loinasd.sudoku.controller

import android.content.Context
import android.support.annotation.DrawableRes
import android.view.ViewGroup
import io.cyanlab.loinasd.sudoku.R

interface TableController: CellsHolder{

    val control: ViewGroup

    val context: Context

    val parent: android.support.v7.widget.GridLayout


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


    var highlightedNumber: Int

    fun highlightNumber(number: Int, isNumberHighlighted: Boolean){

        highlightedNumber = number

        for (index in cells.indices){

            val correct = table.completeTable[y(index)][x(index)]

            val cell = cells[index]

            if (correct == number && !isCellHidden(cell) || getPencil(cell).contains(number))

                cell.background = if (isNumberHighlighted)

                    context.resources.getDrawable(R.drawable.cell_selected)
                else
                    cell.defaultBackground
        }

        val res =
                if (!isNumberHighlighted)
                    R.drawable.cell_default_dark
                else
                    R.drawable.cell_selected

        control.getChildAt(highlightedNumber - 1)?.background = context.resources?.getDrawable(res)

    }


    var isPencil: Boolean
}