package io.cyanlab.loinasd.sudoku.activities

import android.content.pm.ActivityInfo
import android.graphics.Point
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.view.Window
import android.view.WindowManager
import android.widget.GridLayout
import android.widget.TextView
import io.cyanlab.loinasd.sudoku.R
import io.cyanlab.loinasd.sudoku.models.games.Table
import io.cyanlab.loinasd.sudoku.models.games.TableGenerator
import kotlinx.android.synthetic.main.sample.*
import kotlin.concurrent.thread

class MainActivity : AppCompatActivity() {


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

        thread {
            val time = System.currentTimeMillis()

            val table = TableGenerator(Table()).generateTable(TableGenerator.DIFFICULTY_HARD)

            println("Fully generated hard sudoku in ${System.currentTimeMillis() - time} m.s.")


            runOnUiThread{
                ptable(table)
            }
        }


  /*      AsteriskTG().generateTable()

        GirandolaTG().generateTable()

        CenterDotTG().generateTable()*/


        /*DiagonalTable(ConsoleView()).generateTable()

        val time = System.currentTimeMillis()

        while (System.currentTimeMillis() < time + 5000){}*/


    }
    //---------TODO: move to controller|view
    fun ptable(table: Table) {

        val params = GridLayout.LayoutParams()

        val size = Point()

        windowManager?.defaultDisplay?.getSize(size)

        val width = size.x/9 - 10

        params.width = width
        params.height = width
        params.setMargins(margin, margin, margin, margin)

        for (y in 0 until 9) {
            for (x in 0 until 9){
                val tv = TextView(this)
                tv.text = if (table.penTable[y][x])
                    "${table.completeTable[y][x]}"
                else
                    "${0}"

                tv.gravity = Gravity.CENTER
                grid.addView(tv, params)
            }

        }
    }


}
