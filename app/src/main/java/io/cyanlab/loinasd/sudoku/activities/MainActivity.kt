package io.cyanlab.loinasd.sudoku.activities

import android.content.pm.ActivityInfo
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import io.cyanlab.loinasd.sudoku.R
import io.cyanlab.loinasd.sudoku.models.games.Table
import io.cyanlab.loinasd.sudoku.view.ConsoleView

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.activity_main)

        var timeArray = LongArray(15)

        for (i in 0 until timeArray.size){
            val time = System.currentTimeMillis()
            val table = Table(ConsoleView())
            table.generateTable()

            //println("Generated full sudoku table in ${System.currentTimeMillis() - time} m.s.")

            table.createGame(table.DIFFICULTY_HARD)

            timeArray[i] = System.currentTimeMillis() - time

            println("Fully generated hard sudoku in ${System.currentTimeMillis() - time} m.s.")

        }

        println(timeArray)

        /*TablePlusDiagonals().createTable()

        AsteriskTable().createTable()

        GirandolaTable().createTable()

        CenterDotTable().createTable()


        WindokuTable().createTable()*/
    }
}
