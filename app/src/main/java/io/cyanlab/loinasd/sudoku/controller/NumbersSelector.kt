package io.cyanlab.loinasd.sudoku.controller

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import io.cyanlab.loinasd.sudoku.R

class NumbersSelector(controller: TableController): View.OnClickListener, TableController by controller{

    override fun onClick(p0: View?) {

        val num = controls.indexOf(p0) + 1

        if (num > 9 || p0 == pencil) {

            onEdit(p0!!)
            return
        }

        if (!isNumberSelected) {

            selectNumber(num, true)
            return
        }

        selectNumber(selectedNumber, false)

        if (num != selectedNumber)

            selectNumber(num, true)

    }

    private fun onEdit(edit: View){

        isPencil = !isPencil

        edit.background = context.resources.getDrawable(
                if (!isPencil)

                    R.drawable.pencil_default
                else
                {
                    R.drawable.pencil_selected
                }
        )

        (edit as ImageView).setImageDrawable(context.resources.getDrawable(
                if (!isPencil){
                    R.drawable.ic_edit_accent
                }else
                {
                    R.drawable.ic_edit_background
                }
        ))
    }

}