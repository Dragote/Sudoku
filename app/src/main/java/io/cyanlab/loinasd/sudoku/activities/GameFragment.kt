package io.cyanlab.loinasd.sudoku.activities


import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import io.cyanlab.loinasd.sudoku.R
import io.cyanlab.loinasd.sudoku.models.games.*
import io.cyanlab.loinasd.sudoku.view.TableController
import kotlinx.android.synthetic.main.progress_bar.*
import kotlinx.android.synthetic.main.working_r_view.*
import kotlinx.android.synthetic.main.working_r_view.view.*
import kotlin.concurrent.thread


class GameFragment : Fragment() {

    var generator: Thread? = null
    var difficulty = -1
    var controller: TableController? = null
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

        controller = TableController(context, v.main_grid, v.control, table)

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


                controller = TableController(context, main_grid, control, table)

                controller?.getControlNumbers()
                controller?.showTable()
                include.visibility = View.GONE
            }



        }
    }



}
