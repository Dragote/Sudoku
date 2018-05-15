package io.cyanlab.loinasd.sudoku.activities

import android.content.pm.ActivityInfo
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import android.widget.GridLayout
import android.widget.TextView
import io.cyanlab.loinasd.sudoku.R
import io.cyanlab.loinasd.sudoku.models.Table
import io.cyanlab.loinasd.sudoku.models.games.*
import io.cyanlab.loinasd.sudoku.view.*
import kotlinx.android.synthetic.main.sample.*

class MainActivity : AppCompatActivity() {


    val lp = GridLayout.LayoutParams()
    val margin = 10

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //-------------VIEW-------------//
        //val  currentView: ViewOut = RView(this)
        //------------------------------//

        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.sample)

        val width = grid.width/9 - 10

        lp.width = width
        lp.height = width
        lp.setMargins(margin, margin, margin, margin)



        grid.refreshDrawableState()


        val timeArray = LongArray(1)

        for (i in 0 until timeArray.size){
            val time = System.currentTimeMillis()
            val table = TableGenerator()
            table.generateTable()

            table.puzzleTable(table.DIFFICULTY_HARD)

            timeArray[i] = System.currentTimeMillis() - time

            println("Fully generated hard sudoku in ${System.currentTimeMillis() - time} m.s.")

        }



        ptable(TableGenerator().generateTable())

        println(timeArray)

  /*      AsteriskTG().generateTable()

        GirandolaTG().generateTable()

        CenterDotTG().generateTable()*/


        /*DGTG(ConsoleView()).generateTable()

        val time = System.currentTimeMillis()

        while (System.currentTimeMillis() < time + 5000){}*/


    }
    //---------TODO: move to controller|view
    fun ptable(table: Table) {
        for (i in 1..81) {
            val tv = TextView(this)
            tv.text = "$i"

            grid.addView(tv, lp)
        }
    }


}
