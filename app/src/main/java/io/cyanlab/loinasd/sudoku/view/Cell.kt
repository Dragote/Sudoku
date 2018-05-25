package io.cyanlab.loinasd.sudoku.view

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.drawable.Drawable
import android.support.v7.widget.AppCompatTextView
import io.cyanlab.loinasd.sudoku.controller.SudokuController
import io.cyanlab.loinasd.sudoku.controller.TableController

class Cell(val controller: TableController) : AppCompatTextView(controller.context){

    var defaultBackground: Drawable? = null
    var defaultTextColor: Int? = null

    override fun onDraw(canvas: Canvas?) {

        if (controller.isCellHidden(this)){

            val possibles = controller.getPossibleNumbers(this)
            val pencil = controller.getPencil(this)

            var x: Float
            var y: Float

            val width = canvas?.width ?: return

            val textWidth = width / 7

            for (number in 1 until 10){

                x = textWidth * (((number - 1) % 3 * 2f) + 1)

                y = textWidth * 0.6f * (((number - 1) / 3) + 3f)

                y += textWidth * 1.7f * ((number - 1) / 3)

                if (!pencil.contains(number)){
                    continue
                }

                val paint = Paint()
                paint.textSize = textWidth * 1.7f
                paint.color = if (!possibles[number - 1] && defaultTextColor != null) defaultTextColor!! else Color.RED
                paint.typeface = typeface

                canvas.drawText("$number", x, y, paint)

            }
        }
        super.onDraw(canvas)
    }




}