package io.cyanlab.loinasd.sudoku.activities


import android.app.Activity
import android.content.Context
import android.graphics.Point
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import io.cyanlab.loinasd.sudoku.R
import io.cyanlab.loinasd.sudoku.models.games.*
import io.cyanlab.loinasd.sudoku.controller.SudokuController
import io.cyanlab.loinasd.sudoku.models.Sudoku
import kotlinx.android.synthetic.main.m_difficulty.view.*
import kotlinx.android.synthetic.main.working_r_view.*
import kotlinx.android.synthetic.main.working_r_view.view.*
import java.io.*
import kotlin.concurrent.thread


class GameFragment : Fragment() {

    companion object {

        const val RECENT_GAME_FILE_NAME = "recent.sdku"
        const val PREFS_IS_RECENT = "is recent saved"
    }

    var generator: Thread? = null
    var difficulty = -1
    var controller: SudokuController? = null
    var game: Game? = null

    var isRecent: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (arguments?.getBoolean(DifficultyFragment.KEY_STRING_RECENT_GAME) != true){

            isRecent = false

            difficulty = arguments?.getInt("Difficulty") ?: return

            game = arguments?.getSerializable("Game") as Game

            return
        }

        isRecent = true

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View?{
        val v = inflater.inflate(R.layout.working_r_view, container, false)

        return v
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        loadTable(view)
    }


    fun loadTable(view: View?){

        if (view == null)
            return

        view.include?.visibility = View.VISIBLE

        var table: Table? = null

        if (isRecent) {

            val input = ObjectInputStream(BufferedInputStream(FileInputStream("${context?.filesDir?.absolutePath}/$RECENT_GAME_FILE_NAME")))

            table = try {
                input.readObject() as Table
            }catch (e: IOException){
                null
            }


        }

        if (isRecent && table != null){

            onTableLoaded(table, view)
            return
        }

        table = when(game){

            Game.ASTERISK -> AsteriskTable()
            Game.CLASSIC -> Table()
            Game.CENTER_DOTTED -> CenterDotTable()
            Game.DIAGONAL -> DiagonalTable()
            Game.GIRANDOLA -> GirandolaTable()
            Game.WINDOKU -> WindokuTable()
            else -> Table()
        }


        generator = thread {

            val time = System.currentTimeMillis()

            TableGenerator(table).generateTable(difficulty)

            println("Fully generated hard sudoku in ${System.currentTimeMillis() - time} m.s.")

            generator = null

            activity?.runOnUiThread {

                onTableLoaded(table, view)
            }
        }
    }

    fun onTableLoaded(table: Table, view: View){

        controller = SudokuController(context!!, view.main_grid, view.control, null, Sudoku(table, null, null))

        val size = Point()

        (context?.getSystemService(Context.WINDOW_SERVICE) as WindowManager).defaultDisplay.getSize(size)

        //val width = view.main_grid.layoutParams.width
        val margin = 8

        val cellWidth = (size.x - margin * 4 - 50)/9 - margin * 2
        val numberWidth = (size.x )/9 - 0 * 2 * 2

        controller?.getControlNumbers(numberWidth, 0)
        controller?.showTable(cellWidth, margin)


        view.include.visibility = View.GONE
    }


    override fun onStop() {
        super.onStop()

        context?.getSharedPreferences(DifficultyFragment.PREFS_KEY_RECENT_GAME, Activity.MODE_PRIVATE)?.edit()?.putBoolean(PREFS_IS_RECENT, true)?.apply()

        val output = ObjectOutputStream(BufferedOutputStream(FileOutputStream("${context?.filesDir}/$RECENT_GAME_FILE_NAME")))

        output.writeObject(controller?.table)

        output.flush()

        output.close()
    }


}
