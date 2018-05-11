package io.cyanlab.loinasd.sudoku.activities

import android.content.pm.ActivityInfo
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import io.cyanlab.loinasd.sudoku.R
import io.cyanlab.loinasd.sudoku.models.games.*
import io.cyanlab.loinasd.sudoku.view.*

class MainActivity : AppCompatActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //-------------VIEW-------------//
        val  currentView: ViewOut = RView(this)
        //------------------------------//

        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(currentView)

        val timeArray = LongArray(1)

        for (i in 0 until timeArray.size){
            val time = System.currentTimeMillis()
            val table = AsteriskTG()
            table.generateTable()

            table.puzzleTable(table.DIFFICULTY_HARD)

            timeArray[i] = System.currentTimeMillis() - time

            println("Fully generated hard sudoku in ${System.currentTimeMillis() - time} m.s.")

        }

        println(timeArray)

        AsteriskTG().generateTable()

        GirandolaTG().generateTable()

        CenterDotTG().generateTable()


        /*DGTG(ConsoleView()).generateTable()

        val time = System.currentTimeMillis()

        while (System.currentTimeMillis() < time + 5000){}*/


    }
}
