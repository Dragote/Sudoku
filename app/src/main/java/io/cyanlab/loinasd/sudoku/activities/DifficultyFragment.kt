package io.cyanlab.loinasd.sudoku.activities

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

import io.cyanlab.loinasd.sudoku.R
import io.cyanlab.loinasd.sudoku.models.games.TableGenerator
import kotlinx.android.synthetic.main.m_difficulty.*
import kotlinx.android.synthetic.main.m_difficulty.view.*


class DifficultyFragment : Fragment() {


    var difficulty = -1

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val v = inflater.inflate(R.layout.m_difficulty, container, false)

        val gameModeSelector = GameModeSelector(context)

        v.easy.setOnClickListener(gameModeSelector)
        v.medium.setOnClickListener(gameModeSelector)
        v.hard.setOnClickListener(gameModeSelector)


/*        games.columnCount = 2

        games.addView(inflater.inflate(R.layout.game_node, games, true))*/

        v.startNewGame.setOnClickListener(StartGameListener())

        return v
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (context == null) throw NullPointerException("context is null")
    }

    inner class GameTypeSelector(): View.OnClickListener{
        override fun onClick(p0: View?) {

        }

    }

    inner class StartGameListener : View.OnClickListener{
        override fun onClick(p0: View?) {
            if (difficulty == -1)
                return
            val data = Bundle()
            data.putInt("Difficulty", difficulty)
            val f = GameFragment()
            f.arguments = data

            fragmentManager.beginTransaction().replace(R.id.fragment, f).commit()
        }

    }

    inner class GameModeSelector(private val context: Context): View.OnClickListener{

        override fun onClick(p0: View?) {

            p0?.background = context.resources.getDrawable(R.drawable.)
            (p0 as TextView).setTextColor(Color.CYAN)


            difficulty = when (p0?.id){
                R.id.easy -> TableGenerator.DIFFICULTY_EASY
                R.id.medium -> TableGenerator.DIFFICULTY_MEDIUM
                R.id.hard -> TableGenerator.DIFFICULTY_HARD
                else -> return
            }









        }

    }
}
