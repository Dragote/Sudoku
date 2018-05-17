package io.cyanlab.loinasd.sudoku.view
import android.content.Context
import android.graphics.Point
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.view.Gravity
import android.view.View
import android.view.WindowManager
import android.widget.ImageView
import android.widget.TextView
import io.cyanlab.loinasd.sudoku.R
import io.cyanlab.loinasd.sudoku.models.Cell
import io.cyanlab.loinasd.sudoku.models.Sector
import io.cyanlab.loinasd.sudoku.models.games.Table
import android.support.v7.widget.GridLayout

class TableController(var context: Context, val parent: GridLayout, val control: GridLayout, val table: Table) {

    val margin = 5

    val cells: Array<Array<Cell>>
    val sectors: Array<Sector>

    val selector = Selector()
    val controller = Controller()

    init {

        val size = Point()


        (context.getSystemService(Context.WINDOW_SERVICE) as WindowManager).defaultDisplay.getSize(size)

        cells = Array(9,
                {y -> Array(9, { x ->

                    val tv = Cell(context, table.completeTable[y][x], !table.penTable[y][x], x, y)

                    tv.isHidden = !table.penTable[y][x]

                    tv.text = if (table.penTable[y][x])
                        "${table.completeTable[y][x]}"
                    else
                        ""

                    tv.textSize = 18f

                    tv.gravity = Gravity.CENTER
                    tv.setOnClickListener(selector)

                    tv
                })})

        for (i in 0 until 9){

            for (cell in square(i)){
                if (i % 2 != 0){
                    cell.defaultBackground = context.resources.getDrawable(R.drawable.cell_default_dark)
                }else
                    cell.defaultBackground = context.resources.getDrawable(R.drawable.cell_default)

                cell.background = cell.defaultBackground
            }
        }

        if (parent.childCount != 0){

            parent.removeAllViews()
        }

        sectors = Array(table.sectors.size, {sector ->
            Sector(Array(9, { i ->

                val y = table.sectorsCoords[sector][i][0]
                val x = table.sectorsCoords[sector][i][1]

                cells[y][x]
            }))
        })
    }

    fun row(y: Int) = cells[y]

    fun column(x: Int) = Array(9, {i -> cells[i][x]})

    fun square(number: Int) = Array(9, {i -> cells[number / 3 * 3 + i / 3][number % 3 * 3 + i % 3]})

    fun showTable() {

        val params = GridLayout.LayoutParams()

        val size = Point()

        (context.getSystemService(Context.WINDOW_SERVICE) as WindowManager).defaultDisplay.getSize(size)

        val width = size.x/9 - margin * 2

        params.width = width
        params.height = width
        params.setMargins(margin, margin, margin, margin)

        /*context.divider_ver_1.layoutParams.height = size.x
        context.divider_ver_2.layoutParams.height = size.x

        context.divider_ver_1.x = size.x / 3f - margin / 3f
        context.divider_ver_2.x = 2 * size.x / 3f - margin/ 3f

        context.divider_hor_1.y += size.x / 3f - margin / 3f
        context.divider_hor_2.y += 2 * size.x / 3f - margin /3f*/


        for (y in 0 until 9) {
            for (x in 0 until 9){

                parent.addView(cells[y][x], params)
            }

        }

        parent.refreshDrawableState()

        selectedView = null
        //updateController()
    }

    fun getControlNumbers(){

        val params = GridLayout.LayoutParams()

        val size = Point()

        (context.getSystemService(Context.WINDOW_SERVICE) as WindowManager).defaultDisplay.getSize(size)

        val width = size.x/5 - margin * 2

        params.width = width
        params.height = width
        params.setMargins(margin, margin, margin, margin)

        for (i in 1 until 10) {
            val tv = TextView(parent.context)

            tv.text = "$i"
            tv.textSize = 24f

            tv.gravity = Gravity.CENTER
            tv.setOnClickListener(controller)
            control.addView(tv, params)
        }



        val edit = ImageView(context)
        val bd: BitmapDrawable = BitmapDrawable()
        val im = context.resources?.getDrawable(R.drawable.ic_launcher_foreground)
        edit.setImageDrawable(im)
        edit.background = context.resources.getDrawable(R.drawable.cell_default_dark)
        edit.setOnClickListener(controller)

        control.addView(edit, params)
    }

    fun updateController(){

        if (selectedView == null || !isPencil){

            for (i in 0 until control.childCount - 1){
                control.getChildAt(i).background = context.resources.getDrawable(R.drawable.cell_default_dark)
            }
            return
        }

        val num = parent.indexOfChild(selectedView)

        val y = num / 9
        val x = num % 9

        for (i in 0 until 9){

            control.getChildAt(i).background = context.resources.getDrawable(if (selectedView?.pencil?.contains(i + 1) == true) R.drawable.cell_default else R.drawable.cell_default_dark)

        }
    }

    fun onCellSelected(view: Cell?){

        /*if (isCellSelected){
            removeSelection()
        }*/

        if (view == null)
            return

        selectedView = view

        /*selectedView?.background = activity.resources.getDrawable(R.drawable.cell_selected)*/
        isCellSelected = true

        //highlightRnC()


    }

    private fun highlightRnC(){


        for (cell in row(selectedView?.y!!)){
            if (cell != selectedView){
                cell.background = if (isCellSelected)

                    context.resources.getDrawable(R.drawable.cell_highlighted)
                else
                    cell.defaultBackground
            }
        }
        for (cell in column(selectedView?.x!!)){
            if (cell != selectedView){
                cell.background = if (isCellSelected)

                    context.resources.getDrawable(R.drawable.cell_highlighted)
                else
                    cell.defaultBackground
            }
        }
    }

    var highlightedNumber: Int = 1

    fun highlightNumber(number: Int, isNumberHighlighted: Boolean){

        highlightedNumber = number

        for (y in cells.indices){
            for (cell in cells[y]){
                if (cell.number == number && !cell.isHidden || cell.pencil.contains(number))
                    cell.background = if (isNumberHighlighted)

                        context.resources.getDrawable(R.drawable.cell_highlighted)
                    else
                        cell.defaultBackground
            }
        }

        val res =
                if (!isNumberHighlighted)
                    R.drawable.cell_default_dark
                else
                    R.drawable.cell_selected

        control?.getChildAt(highlightedNumber - 1)?.background = context.resources?.getDrawable(res)

    }

    var selectedView: Cell? = null

    fun removeSelection(){

        if (selectedView == null)
            return

        selectedView?.background = selectedView?.defaultBackground

        isCellSelected = false

        //highlightRnC()

        selectedView = null

    }

    var isCellSelected = false

    inner class Selector: View.OnClickListener{

        override fun onClick(p0: View?) {

            if (p0 !is TextView)
                return

            if (isNumberHighlighted){

                isNumberHighlighted = false
                highlightNumber(highlightedNumber)
            }

            onCellSelected(p0 as Cell)

            if (selectedView?.isHidden != true){
                return
            }

            val selectedNum = parent.indexOfChild(selectedView)

            val y = selectedNum / 9
            val x = selectedNum % 9

            if (!isPencil)
                onPen(y, x, highlightedNumber)
            else
                onPencil(highlightedNumber)

        }

        fun onPencil(num: Int){

            val res: Drawable?

            if (selectedView?.pencil?.contains(num) != true){

                selectedView?.pencil?.add(num)

                res = context.resources?.getDrawable(R.drawable.cell_highlighted)

            } else{
                selectedView?.pencil?.remove(num)

                res = selectedView?.defaultBackground
            }

            selectedView?.background = res


            //editPencilText(num)

            selectedView?.invalidate()

        }

        fun onPen(y: Int, x: Int, num: Int){

            if (num == table.completeTable[y][x]) {

                selectedView?.text = num.toString()
                selectedView?.textSize = 18f
                table.cellEntered(y, x)

                selectedView?.background = context.resources?.getDrawable(R.drawable.cell_highlighted)
                selectedView?.isHidden = false
                selectedView?.pencil?.removeAll(selectedView?.pencil!!)
                onCellSelected(selectedView)
            }

            var isFinished = true

            for (i in table.penTable.indices) {
                if (table.penTable[i].contains(false)) {
                    isFinished = false
                    break
                }
            }

            /*if (isFinished) {
                context.congr_layout.visibility = View.VISIBLE
                isComplete = true
            }*/
        }

    }

    var isPencil = false

    inner class Controller: View.OnClickListener{

        override fun onClick(p0: View?) {

            val num = control.indexOfChild(p0) + 1

            /*if (isCellSelected)
                removeSelection()*/

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

        fun onEdit(edit: View){

            isPencil = !isPencil

            edit.background = context.resources.getDrawable(if (!isPencil) R.drawable.cell_default_dark else R.drawable.cell_selected)

        }

    }

    var isComplete = false


}