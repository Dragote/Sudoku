package io.cyanlab.loinasd.sudoku.activities

import android.content.pm.ActivityInfo
import android.graphics.Color
import android.graphics.Point
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.GridLayout
import android.widget.TextView
import io.cyanlab.loinasd.sudoku.R
import io.cyanlab.loinasd.sudoku.models.games.Table
import io.cyanlab.loinasd.sudoku.models.games.TableGenerator
import io.cyanlab.loinasd.sudoku.view.TableController
import kotlinx.android.synthetic.main.m_difficulty.*
import kotlinx.android.synthetic.main.main_menu.*
import kotlinx.android.synthetic.main.progress_bar.*
import kotlinx.android.synthetic.main.sample.*
import kotlinx.android.synthetic.main.working_r_view.*
import kotlin.concurrent.thread

class MainActivity : AppCompatActivity() {


    val margin = 10

    val gameModeSelector = GameModeSelector(this)

    var generator: Thread? = null

    var controller: TableController? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.main_menu)

        startGame.setOnClickListener(GameMenuSelector())



/*        getControlNumbers()

        gameModeSelector.onClick(easy)*/

    }


    //---------TODO: move to controller|view
    inner class GameMenuSelector: View.OnClickListener{
        override fun onClick(p0: View?) {
            p0?.setBackgroundColor(Color.GREEN)
            if (p0 == startGame)
                setContentView(R.layout.m_difficulty)
            easy.setOnClickListener(gameModeSelector)
            medium.setOnClickListener(gameModeSelector)
            hard.setOnClickListener(gameModeSelector)

        }

    }

    inner class GameModeSelector(val activity: MainActivity): View.OnClickListener{

        override fun onClick(p0: View?) {

            p0?.setBackgroundColor(Color.GREEN)

            if (generator != null)
                return

            if (controller?.isComplete == true)
                congr_layout?.visibility = View.GONE

            val difficulty = when (p0?.id){
                R.id.easy -> TableGenerator.DIFFICULTY_EASY
                R.id.medium -> TableGenerator.DIFFICULTY_MEDIUM
                R.id.hard -> TableGenerator.DIFFICULTY_HARD
                else -> TableGenerator.DIFFICULTY_EASY
            }

            generator = thread {

                val time = System.currentTimeMillis()

                val table = TableGenerator(Table()).generateTable(difficulty)

                println("Fully generated hard sudoku in ${System.currentTimeMillis() - time} m.s.")

                runOnUiThread{
                    setContentView(R.layout.working_r_view)

                    controller = TableController(activity, activity.main_grid, table)

                    controller?.getControlNumbers()
                    controller?.showTable()
                    progress_bar.visibility = View.GONE
                    generator = null

                }
            }

            progress_bar.visibility = View.VISIBLE
        }

    }

}
