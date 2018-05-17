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

class TableController(val context: Context, val parent: android.support.v7.widget.GridLayout, val control: android.support.v7.widget.GridLayout, val table: Table) {

    private val margin = 5

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

    private fun square(number: Int) = Array(9, { i ->

        val y = number / 3 * 3 + i / 3
        val x = number % 3 * 3 + i % 3

        cells[number(y,x)]})

    fun number(y: Int, x: Int) = y * 9 + x

    fun y (number: Int) = number / 9
    fun x (number: Int) = number % 9

    fun showTable() {

        val params = GridLayout.LayoutParams()

        val size = Point()

        (context.getSystemService(Context.WINDOW_SERVICE) as WindowManager).defaultDisplay.getSize(size)

        val width = size.x/9 - margin * 2

        params.width = width
        params.height = width
        params.setMargins(margin, margin, margin, margin)

        colorSquares(R.drawable.cell_default_dark, R.drawable.cell_default)

        if (parent.childCount != 0){

            parent.removeAllViews()
        }

        //val detector = GestureDetector(context, DoubleClickListener())


        for (number in 0 until cells.size){
            cells[number].setOnClickListener(selector)

            /*cells[number].setOnTouchListener { view, motionEvent ->

                if (isPencil && view is Cell){
                    detector.onTouchEvent(motionEvent)
                }

                false
            }*/

            parent.addView(cells[number], params)
        }

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

    fun getControlNumbers(){

        val params = GridLayout.LayoutParams()

        val size = Point()

        (context.getSystemService(Context.WINDOW_SERVICE) as WindowManager).defaultDisplay.getSize(size)

        val width = size.x/6 - margin * 2 * 2

        params.width = width
        params.height = width
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

        edit.background = context.resources.getDrawable(R.drawable.cell_default_dark)


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

    var highlightedNumber: Int = 1

    fun highlightNumber(number: Int, isNumberHighlighted: Boolean){

        highlightedNumber = number

        for (index in cells.indices){

            val correct = table.completeTable[y(index)][x(index)]

            val cell = cells[index]

            if (correct == number && !isCellHidden(cell) || getPencil(cell).contains(index))

                cell.background = if (isNumberHighlighted)

                    context.resources.getDrawable(R.drawable.cell_highlighted)
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

            if (p0 !is Cell)
                return

            if (!isCellHidden(p0)){
                return
            }

            if (!isPencil)
                onPen(p0)
            else
                onPencil(p0)

        }

        private fun onPencil(cell: Cell){

            val res: Drawable?

            val pencil = getPencil(cell)

            res = if (!pencil.contains(highlightedNumber)){

                pencil.add(highlightedNumber)

                context.resources?.getDrawable(R.drawable.cell_highlighted)

            } else{
                pencil.remove(highlightedNumber)

                cell.defaultBackground
            }

            cell.background = res

            cell.invalidate()

        }

        private fun onPen(cell: Cell){

            val number = cells.indexOf(cell)


            if (highlightedNumber == table.completeTable[y(number)][x(number)]) {

                cell.text = table.completeTable[y(number)][x(number)].toString()

                table.cellEntered(y(number), x(number))

                cell.background = context.resources?.getDrawable(R.drawable.cell_highlighted)

                val pencil = getPencil(cell)
                pencil.removeAll(pencil)
            }

            /*var isFinished = true

            for (i in table.penTable.indices) {
                if (table.penTable[i].contains(false)) {
                    isFinished = false
                    break
                }
            }

            if (isFinished) {
                context.congr_layout.visibility = View.VISIBLE
                isComplete = true
            }*/
        }

    }

    var isPencil = false

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

        override fun onDoubleTap(e: MotionEvent?): Boolean {

            if (!isPencil){
                return false
            }


            return false
        }

        override fun onSingleTapConfirmed(e: MotionEvent?): Boolean {
            return super.onSingleTapConfirmed(e)
        }

    }


}