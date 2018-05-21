package io.cyanlab.loinasd.sudoku.controller
import android.content.Context
import android.view.*
import android.widget.ImageView
import android.widget.TextView
import io.cyanlab.loinasd.sudoku.R
import io.cyanlab.loinasd.sudoku.view.Cell
import io.cyanlab.loinasd.sudoku.models.Sector
import io.cyanlab.loinasd.sudoku.models.games.Table

class SudokuController(override val context: Context, override val parent: android.support.v7.widget.GridLayout, override val control: ViewGroup, val pencil: View?, override val table: Table): TableController{

    override val cells: Array<Cell> = Array(81, { number ->

        val y = number / 9
        val x = number % 9

        val cell = Cell(this)

        cell.text = if (table.penTable[y][x])
            "${table.completeTable[y][x]}"
        else
            ""

        cell.textSize = 18f

        cell.gravity = Gravity.CENTER

        cell
    })

    override val sectors: Array<Sector> = Array(table.sectors.size, { sector ->
        Sector(Array(9, { i ->

            val y = table.sectorsCoords[sector][i][0]
            val x = table.sectorsCoords[sector][i][1]

            cells[number(y, x)]
        }))
    })

    private val numbersSelector = NumbersSelector(this)

    override fun showTable(width: Int, margin: Int) {

        val params = ViewGroup.MarginLayoutParams(width, width)

        colorSquares(R.drawable.cell_default_dark, R.drawable.cell_default_dark)//R.drawable.cell_default)

        colorSectors()

        if (parent.childCount != 0){

            parent.removeAllViews()
        }

/*        (parent.parent as View).divider_ver_2.layoutParams.height = size.x
        (parent.parent as View).divider_ver_1.layoutParams.height = size.x

        (parent.parent as View).divider_ver_1.x = size.x / 3f - margin / 3f
        (parent.parent as View).divider_ver_2.x = 2 * size.x / 3f - margin/ 3f

        (parent.parent as View).divider_hor_1.y += size.x / 3f - margin / 3f
        (parent.parent as View).divider_hor_2.y += 2 * size.x / 3f - margin /3f*/


        for (number in 0 until cells.size){
            //cells[number].setOnClickListener(selector)

            val detector = GestureDetector(context, Selector(this, cells[number]))

            var marginTop = margin
            var marginLeft = margin

            if (y(number) == 3 || y(number) == 6)
                marginTop += margin * 2
            if (x(number) == 3 || x(number) == 6)
                marginLeft += margin * 2

            params.setMargins(marginLeft, marginTop, margin, margin)

            cells[number].setOnTouchListener { _, motionEvent ->

                detector.onTouchEvent(motionEvent)
            }

            parent.addView(cells[number], params)
        }

        highlightedNumber = 1
        highlightNumber(highlightedNumber, true)

    }

    override fun getControlNumbers(width: Int, margin: Int){

        val params = ViewGroup.MarginLayoutParams(width, width + 10)

        params.setMargins(margin * 2, margin * 2, margin * 2, margin * 2)

        for (i in 1 until 10) {
            val controlNum = TextView(context)

            controlNum.text = "$i"
            controlNum.textSize = 24f

            controlNum.gravity = Gravity.CENTER
            controlNum.setOnClickListener(numbersSelector)

            controlNum.background = context.resources.getDrawable(R.drawable.cell_default_dark)

            control.addView(controlNum, params)
        }

        println(context)

        val edit = ImageView(context)

        edit.setImageDrawable(context.resources?.getDrawable(R.drawable.ic_edit_black))

        edit.background = context.resources.getDrawable(R.drawable.cell_selected)


        edit.setOnClickListener(numbersSelector)

        control.addView(edit, params)

    }

    override var highlightedNumber: Int = 1

    override var isPencil = true

}