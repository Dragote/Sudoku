package io.cyanlab.loinasd.sudoku.activities


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import io.cyanlab.loinasd.sudoku.R
import io.cyanlab.loinasd.sudoku.models.games.*
import io.cyanlab.loinasd.sudoku.controller.SudokuController
import kotlinx.android.synthetic.main.working_r_view.*
import kotlin.concurrent.thread


class GameFragment : Fragment() {

    var generator: Thread? = null
    var difficulty = -1
    var controller: SudokuController? = null
    var game: Game? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        difficulty = arguments.getInt("Difficulty")
        game = arguments.getSerializable("Game") as Game
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        val v = inflater.inflate(R.layout.working_r_view, container, false)

        println(context)
        println(container?.context)
        println(context == container?.context)


/*        val time = System.currentTimeMillis()

        val table = TableGenerator(Table()).generateTable(difficulty)

        println("Fully generated hard sudoku in ${System.currentTimeMillis() - time} m.s.")

        controller = SudokuController(context, v.main_grid, v.control, table)

        controller?.getControlNumbers()
        controller?.showTable()*/

        return v
    }

    override fun onStart() {
        super.onStart()

        include.visibility = View.VISIBLE

        generator = thread {

            val time = System.currentTimeMillis()

            val table: Table = when(game){

                Game.ASTERISK -> AsteriskTable()
                Game.CLASSIC -> Table()
                Game.CENTER_DOTTED -> CenterDotTable()
                Game.DIAGONAL -> DiagonalTable()
                Game.GIRANDOLA -> GirandolaTable()
                Game.WINDOKU -> WindokuTable()
                else -> Table()
            }

            TableGenerator(table).generateTable(difficulty)

            println("Fully generated hard sudoku in ${System.currentTimeMillis() - time} m.s.")

            generator = null

            activity.runOnUiThread {


                controller = SudokuController(context, main_grid, control, null, table)

                val size = main_grid.measuredWidth
                val margin = 8

                val cellWidth = (size - margin * 4)/9 - margin * 2
                val numberWidth = size/9 - margin * 2 * 2

                controller?.getControlNumbers(numberWidth, margin)
                controller?.showTable(cellWidth, margin)


                include.visibility = View.GONE
            }



        }
    }



}
