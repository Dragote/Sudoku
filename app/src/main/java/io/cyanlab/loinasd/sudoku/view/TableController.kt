package io.cyanlab.loinasd.sudoku.view

import android.app.Activity
import android.graphics.Point
import android.view.Gravity
import android.widget.GridLayout
import android.widget.TextView
import io.cyanlab.loinasd.sudoku.R
import io.cyanlab.loinasd.sudoku.models.Cell
import io.cyanlab.loinasd.sudoku.models.Sector
import io.cyanlab.loinasd.sudoku.models.games.Table
import kotlinx.android.synthetic.main.working_r_view.*

class TableController(val activity: Activity, val parent: GridLayout, val table: Table) {

    val margin = 10

    val cells: Array<Array<Cell>>
    val sectors: Array<Sector>

    init {

        val params = GridLayout.LayoutParams()

        val size = Point()

        activity.windowManager?.defaultDisplay?.getSize(size)

        val width = size.x/9 - 20

        params.width = width
        params.height = width
        params.setMargins(margin, margin, margin, margin)


        cells = Array(9,
                {y -> Array(9, { x ->

                    val tv = Cell(activity, table.completeTable[y][x], !table.penTable[y][x], x, y)

                    tv.text = if (table.penTable[y][x])
                        "${table.completeTable[y][x]}"
                    else
                        "${0}"

                    tv.gravity = Gravity.CENTER
                    //tv.setOnClickListener(selector)
                    //tv.background = resources.getDrawable(R.drawable.cell_default)})})
                    tv
                })})

        sectors = Array(table.sectors.size, {sector ->
            Sector(Array(9, { i ->

                val y = table.sectorsCoords[sector][i][0]
                val x = table.sectorsCoords[sector][i][1]

                cells[y][x]
            }))
        })
    }

    fun row(y: Int) = cells.filter {
        it[0].y == y
    }

    fun column(x: Int) = {
        cells.filter{
        TODO("it[0]")
        }
    }

}