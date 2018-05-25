package io.cyanlab.loinasd.sudoku.controller
import android.content.Context
import android.view.*
import android.widget.ImageView
import io.cyanlab.loinasd.sudoku.R
import io.cyanlab.loinasd.sudoku.view.Cell
import io.cyanlab.loinasd.sudoku.models.Sector
import io.cyanlab.loinasd.sudoku.models.Sudoku
import kotlinx.android.synthetic.main.control.view.*

class SudokuController(override val context: Context, override val parent: android.support.v7.widget.GridLayout, override val control: ViewGroup, override val pencil: View?, val sudoku: Sudoku): TableController{

    override val table = sudoku.table

    override val cells: Array<Cell> = Array(81, { number ->

        val y = number / 9
        val x = number % 9

        val cell = Cell(this)

        cell.text = if (table.penTable[y][x])
            "${table.completeTable[y][x]}"
        else
            ""

        cell.textSize = 22f

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

    override val numbersSelector = NumbersSelector(this)

    override fun showTable(width: Int, margin: Int) {

        val params = ViewGroup.MarginLayoutParams(width, width)

        colorSquares(R.drawable.cell_default, R.drawable.cell_default)//R.drawable.cell_default)

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

            cells[number].setTextColor(context.resources.getColor(R.color.background))
            cells[number].defaultTextColor = context.resources.getColor(R.color.background)


            parent.addView(cells[number], params)
        }

        selectedNumber = 1
        selectNumber(selectedNumber, true)

        parent.refreshDrawableState()

    }

    override val controls = Array<View>(9, {i: Int ->
        when (i){
            0 -> control.num1
            1 -> control.num2
            2 -> control.num3
            3 -> control.num4
            4 -> control.num5
            5 -> control.num6
            6 -> control.num7
            7 -> control.num8
            8 -> control.num9
            else -> control.num1
        }
    })

    override fun getControlNumbers(width: Int, margin: Int){

        val params = ViewGroup.MarginLayoutParams(width, width)

        params.setMargins(margin * 2, margin * 2, margin * 2, margin * 2)

        for (i in 1 until 10) {
            //val controlNum = control.getChildAt(i - 1)

            /*controlNum.setTextColor(context.resources.getColor(R.color.cell))

            controlNum.gravity = Gravity.CENTER


            controlNum.background = context.resources.getDrawable(R.drawable.number_default)

            control.addView(controlNum, params)
            controlNum.text = "$i"
            controlNum.textSize = 26f*/

            //controlNum.setPadding(0,0,0,0)
            controls[i - 1].setOnClickListener(numbersSelector)
        }

        println(context)

        if (pencil != null){

            pencil.setOnClickListener(numbersSelector)
            return
        }

        val edit = ImageView(context)

        edit.setImageDrawable(context.resources?.getDrawable(R.drawable.ic_edit_accent))

        edit.background = context.resources.getDrawable(R.drawable.cell_selected)

        edit.setOnClickListener(numbersSelector)

        //control.addView(edit, params)

    }

    override var selectedNumber: Int = 1
    override var isNumberSelected = true

    override var isPencil = true

}