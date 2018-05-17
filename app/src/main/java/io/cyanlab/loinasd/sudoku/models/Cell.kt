package io.cyanlab.loinasd.sudoku.models

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.support.v4.widget.TextViewCompat
import android.support.v7.widget.AppCompatTextView
import android.widget.TextView
import io.cyanlab.loinasd.sudoku.R

class Cell(context: Context, val number: Int, var isHidden: Boolean,
           val x: Int, val y: Int) : AppCompatTextView(context){

    val pencil = ArrayList<Int>(9)

    var defaultBackground: Drawable? = null

    override fun onDraw(canvas: Canvas?) {

        if (isHidden){

            var x = 0f
            var y = 0f

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
                paint.color = textColors.defaultColor
                paint.typeface = typeface

                canvas.drawText("$number", x, y, paint)

            }
        }
        super.onDraw(canvas)
    }




}