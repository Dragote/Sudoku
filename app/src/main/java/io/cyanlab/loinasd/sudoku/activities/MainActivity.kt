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
import kotlinx.android.synthetic.main.m_difficulty.*
import kotlinx.android.synthetic.main.main_menu.*
import kotlinx.android.synthetic.main.progress_bar.*
import kotlinx.android.synthetic.main.working_r_view.*
import kotlin.concurrent.thread

class MainActivity : AppCompatActivity() {


    val margin = 10

    var table: Table? = null

    val selector = Selector()
    val controller = Controller()
    val gameModeSelector = GameModeSelector()

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
    fun showTable(table: Table) {

        if (main_grid.childCount != 0){

            for (i in 0 until main_grid.childCount){

                val tv = main_grid.getChildAt(i) as TextView

                val y = i / 9
                val x = i % 9

                tv.text = if (table.penTable[y][x])
                    "${table.completeTable[y][x]}"
                else
                    "${0}"

                tv.background = resources.getDrawable(R.drawable.cell_default)
            }

            return
        }

        val params = GridLayout.LayoutParams()

        val size = Point()

        windowManager?.defaultDisplay?.getSize(size)

        val width = size.x/9 - 20

        params.width = width
        params.height = width
        params.setMargins(margin, margin, margin, margin)

        divider_ver_1.layoutParams.height = size.x
        divider_ver_2.layoutParams.height = size.x

        divider_ver_1.x = size.x / 3f - 3
        divider_ver_2.x = 2 * size.x / 3f - 3

        divider_hor_1.y += size.x / 3f - 3
        divider_hor_2.y += 2 * size.x / 3f - 3


        for (y in 0 until 9) {
            for (x in 0 until 9){
                val tv = TextView(this)
                tv.text = if (table.penTable[y][x])
                    "${table.completeTable[y][x]}"
                else
                    "${0}"

                tv.gravity = Gravity.CENTER
                tv.setOnClickListener(selector)
                tv.background = resources.getDrawable(R.drawable.cell_default)
                main_grid.addView(tv, params)
            }

        }


    }

    fun getControlNumbers(){

        val params = GridLayout.LayoutParams()

        val size = Point()

        windowManager?.defaultDisplay?.getSize(size)

        val width = size.x/5 - 20

        params.width = width
        params.height = width
        params.setMargins(margin, margin, margin, margin)

        for (i in 1 until 10) {
            val tv = TextView(this)

            tv.text = "$i"

            tv.gravity = Gravity.CENTER
            tv.setOnClickListener(controller)
            control.addView(tv, params)
        }
    }

    fun updateController(){

        if (selectedView == null){
            for (i in 0 until control.childCount){
                control.getChildAt(i).background = resources.getDrawable(R.color.MaterialDarkerYellow)
            }
            return
        }

        if (table == null)
            return

        val num = main_grid.indexOfChild(selectedView)

        val y = num / 9
        val x = num % 9

        val flags = table?.getPossibleNumbers(y,x) ?: return

        for (i in 0 until 9){

            if (flags[i]){
                control.getChildAt(i).background = resources.getDrawable(R.color.MaterialBackground)
            }
        }
    }

    fun onCellSelected(view: TextView?){

        if (isCellSelected){
            removeSelection()
        }

        if (view == null || view.text != "0")
            return

        selectedView = view
        selectedView?.background = resources.getDrawable(R.drawable.cell_selected)
        isCellSelected = true
        updateController()


    }

    var selectedView: TextView? = null

    fun removeSelection(){

        if (selectedView == null)
            return

        selectedView?.background = resources.getDrawable(R.drawable.cell_default)

        isCellSelected = false

        selectedView = null

        updateController()

    }

    var isCellSelected = false

    inner class Selector: View.OnClickListener{

        override fun onClick(p0: View?) {
            if (p0 !is TextView)
                return

            onCellSelected(p0)

        }

    }

    inner class Controller: View.OnClickListener{

        override fun onClick(p0: View?) {

            if (p0 !is TextView || selectedView == null || generator != null || table == null)
                return

            val numTxt = p0.text

            val num = when (numTxt){
                "1" -> 1
                "2" -> 2
                "3" -> 3
                "4" -> 4
                "5" -> 5
                "6" -> 6
                "7" -> 7
                "8" -> 8
                "9" -> 9
                else -> 1
            }

            val selectedNum = main_grid.indexOfChild(selectedView)

            val y = selectedNum / 9
            val x = selectedNum % 9

            if (num == table?.completeTable!![y][x]){

                selectedView?.text = num.toString()
                table?.cellEntered(y, x)

                onCellSelected(selectedView)
            }

            var isFinished = true

            for (i in table?.penTable!!.indices){
                if (table?.penTable!![i].contains(false)){
                    isFinished = false
                    break
                }
            }

            if (isFinished){
                congr_layout.visibility = View.VISIBLE
                isComplete = true
            }

        }

    }

    var isComplete = false

    var generator: Thread? = null

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

    inner class GameModeSelector: View.OnClickListener{

        override fun onClick(p0: View?) {

            p0?.setBackgroundColor(Color.GREEN)

            if (generator != null)
                return

            if (isComplete)
                congr_layout.visibility = View.GONE

            val difficulty = when (p0?.id){
                R.id.easy -> TableGenerator.DIFFICULTY_EASY
                R.id.medium -> TableGenerator.DIFFICULTY_MEDIUM
                R.id.hard -> TableGenerator.DIFFICULTY_HARD
                else -> TableGenerator.DIFFICULTY_EASY
            }



            generator = thread {


                val time = System.currentTimeMillis()

                table = TableGenerator(Table()).generateTable(difficulty)

                println("Fully generated hard sudoku in ${System.currentTimeMillis() - time} m.s.")

                runOnUiThread{
                    setContentView(R.layout.working_r_view)
                    getControlNumbers()
                    showTable(table!!)
                    selectedView = null
                    updateController()
                    progress_bar.visibility = View.GONE
                    generator = null

                }
            }

            progress_bar.visibility = View.VISIBLE
        }

    }

}
