package io.cyanlab.loinasd.sudoku.view
import android.content.Context
import android.graphics.Point
import android.graphics.drawable.Drawable
import android.support.annotation.DrawableRes
import android.view.*
import android.widget.GridLayout
import android.widget.ImageView
import android.widget.TextView
import io.cyanlab.loinasd.sudoku.R
import io.cyanlab.loinasd.sudoku.models.Cell
import io.cyanlab.loinasd.sudoku.models.Sector
import io.cyanlab.loinasd.sudoku.models.games.Table
import kotlinx.android.synthetic.main.working_r_view.view.*

class TableController(val context: Context, val parent: android.support.v7.widget.GridLayout, val control: android.support.v7.widget.GridLayout, val table: Table) {

    private val margin = 8

    private val cells: Array<Cell> = Array(81, {number ->

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

    private val sectors: Array<Sector> = Array(table.sectors.size, {sector ->
        Sector(Array(9, { i ->

            val y = table.sectorsCoords[sector][i][0]
            val x = table.sectorsCoords[sector][i][1]

            cells[number(y, x)]
        }))
    })

    private val selector = Selector()
    private val controller = Controller()

    private fun row(number: Int) = Array(9, {i -> cells[number / 9 * 9 + i]})
    private fun column(number: Int) = Array(9, {i -> cells[number % 9 + i * 9]})

    private fun square(number: Int) = Array(9, { i ->

        val y = number / 3 * 3 + i / 3
        val x = number % 3 * 3 + i % 3

        cells[number(y,x)]})

    fun number(y: Int, x: Int) = y * 9 + x

    fun y (number: Int) = number / 9
    fun x (number: Int) = number % 9

    fun showTable() {

        val params = GridLayout.LayoutParams()

        val size = parent.measuredWidth

        val width = (size - margin * 4)/9 - margin * 2

        params.width = width
        params.height = width

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

            val detector = GestureDetector(context, DoubleClickListener(cells[number]))

            var marginTop = margin
            var marginLeft = margin

            if (y(number) == 3 || y(number) == 6)
                marginTop += margin * 2
            if (x(number) == 3 || x(number) == 6)
                marginLeft += margin * 2

            params.setMargins(marginLeft, marginTop, margin, margin)

            cells[number].setOnTouchListener { view, motionEvent ->

                detector.onTouchEvent(motionEvent)
            }

            parent.addView(cells[number], params)
        }

        highlightedNumber = 1
        highlightNumber(highlightedNumber, true)

    }

    private fun colorSquares(@DrawableRes res1: Int, @DrawableRes res2: Int){

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

    private fun colorSectors(){

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

    fun getControlNumbers(){

        val params = GridLayout.LayoutParams()

        val size = parent.measuredWidth


        val width = size/9 - margin * 2 * 2

        params.width = width
        params.height = width + 10
        params.setMargins(margin * 2, margin * 2, margin * 2, margin * 2)

        for (i in 1 until 10) {
            val controlNum = TextView(context)

            controlNum.text = "$i"
            controlNum.textSize = 24f

            controlNum.gravity = Gravity.CENTER
            controlNum.setOnClickListener(controller)

            controlNum.background = context.resources.getDrawable(R.drawable.cell_default_dark)

            control.addView(controlNum, params)
        }

        println(context)

        val edit = ImageView(context)

        edit.setImageDrawable(context.resources?.getDrawable(R.drawable.ic_edit_black))

        edit.background = context.resources.getDrawable(R.drawable.cell_selected)


        edit.setOnClickListener(controller)

        control.addView(edit, params)

    }

    fun getPossibleNumbers(cell: Cell): BooleanArray{

        val number = cells.indexOf(cell)
        return table.getPossibleNumbers(y(number), x(number))
    }

    fun getPencil(cell: Cell) = table.pencil[cells.indexOf(cell)]

    fun isCellHidden(cell: Cell): Boolean{

        val number = cells.indexOf(cell)

        return !table.penTable[y(number)][x(number)]
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

    var highlightedNumber: Int = 1


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

    inner class Selector: View.OnClickListener{

        override fun onClick(p0: View?) {

        }

    }

    var isPencil = true

    inner class Controller: View.OnClickListener{

        override fun onClick(p0: View?) {

            val num = control.indexOfChild(p0) + 1

            if (num > 9) {

                onEdit(p0!!)
                return
            }

            if (p0 !is TextView)
                return

            if (num != highlightedNumber)

                highlightNumber(highlightedNumber, false)

            highlightNumber(num, true)

        }

        private fun onEdit(edit: View){

            isPencil = !isPencil

            edit.background = context.resources.getDrawable(if (!isPencil) R.drawable.cell_default_dark else R.drawable.cell_selected)
        }

    }

    inner class DoubleClickListener(val cell: Cell): GestureDetector.SimpleOnGestureListener(){

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

    }


}