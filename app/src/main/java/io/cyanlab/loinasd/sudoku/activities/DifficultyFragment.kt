package io.cyanlab.loinasd.sudoku.activities

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.GridLayout
import android.widget.TextView

import io.cyanlab.loinasd.sudoku.R
import io.cyanlab.loinasd.sudoku.models.games.Game
import io.cyanlab.loinasd.sudoku.models.games.TableGenerator
import kotlinx.android.synthetic.main.game_node.view.*
import kotlinx.android.synthetic.main.m_difficulty.*
import kotlinx.android.synthetic.main.m_difficulty.view.*


class DifficultyFragment : Fragment() {


    var difficulty = -1
    var game: Game? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val v = inflater.inflate(R.layout.m_difficulty, container, false)

        val gameModeSelector = DifficultySelector()
        val startGameListener = StartGameListener()
        val gameTypeSelector = GameTypeSelector()

        v.easy.setOnClickListener(gameModeSelector)
        v.medium.setOnClickListener(gameModeSelector)
        v.hard.setOnClickListener(gameModeSelector)


        v.games.columnCount = 2


        val params = GridLayout.LayoutParams()
        val margin = 15

        val w = v.games.measuredWidth

        val width = w / 2 - margin * 2

        params.width = width
        params.height = width

        params.setMargins(margin, margin, margin, margin)

        for (g in Game.values()){
            val node = inflater.inflate(R.layout.game_node, games, false)
            node.name.text = g.getGameName()
            node.name.setBackgroundColor(0x00000000)
            node.check.setBackgroundColor(0x00000000)
            node.img.setImageDrawable(g.getDrawable(context))
            node.img.setBackgroundColor(0x00000000)

            node.setOnClickListener(gameTypeSelector)

            v.games.addView(node, params)

        }

        v.startNewGame.setOnClickListener(startGameListener)
        v.resumeGame.setOnClickListener(startGameListener)

        return v
    }

    fun checkButtons() {
        if (difficulty != -1 && game != null){
            startNewGame.isEnabled = true
            resumeGame.isEnabled = true
        }
    }

    inner class GameTypeSelector: View.OnClickListener{

        open var choosed: View? = null
        override fun onClick(p0: View?) {
            if (p0 == null) return
            if (choosed != null) {
                choosed?.setBackgroundColor(resources.getColor(R.color.MaterialDarkerBackground))
                choosed?.check?.visibility = View.GONE
            }
            p0.setBackgroundColor(resources.getColor(R.color.MaterialDarkerPaleBlueLA))
            p0.check.visibility = View.VISIBLE
            choosed = p0
            game = findGame(choosed?.name?.text as String)
            checkButtons()
        }

        fun findGame(name: String): Game{
            for (g in Game.values())
                if (g.getGameName() == name)
                    return g

            return Game.CLASSIC
        }

    }

    inner class StartGameListener : View.OnClickListener{
        override fun onClick(p0: View?) {

            when(p0) {

                startNewGame -> {

                    if (difficulty == -1)
                        return
                    val data = Bundle()
                    data.putInt("Difficulty", difficulty)
                    data.putSerializable("Game", game)
                    val f = GameFragment()
                    f.arguments = data

                    fragmentManager.beginTransaction().replace(R.id.fragment, f).commit()
                }
                resumeGame -> {

                }
                else -> return
            }

        }

    }

    inner class DifficultySelector: View.OnClickListener{

        private var choosed: TextView? = null

        override fun onClick(p0: View?) {

            when(p0) {
                easy -> {
                    if (choosed != null)
                        choosed?.setTextColor(resources.getColor(R.color.WhiteLowAlpha))
                    choosed = easy
                    easy.setTextColor(resources.getColor(R.color.MaterialGreen))
                    difficulty = TableGenerator.DIFFICULTY_EASY
                }
                medium -> {
                    if (choosed != null)
                        choosed?.setTextColor(resources.getColor(R.color.WhiteLowAlpha))
                    choosed = medium
                    medium.setTextColor(resources.getColor(R.color.MaterialYellow))
                    difficulty = TableGenerator.DIFFICULTY_MEDIUM
                }
                hard -> {
                    if (choosed != null)
                        choosed?.setTextColor(resources.getColor(R.color.WhiteLowAlpha))
                    choosed = hard
                    hard.setTextColor(resources.getColor(R.color.MaterialRed))
                    difficulty = TableGenerator.DIFFICULTY_HARD
                }
                else -> return
            }
            checkButtons()
        }

    }
}
