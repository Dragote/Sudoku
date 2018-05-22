package io.cyanlab.loinasd.sudoku.activities


import android.annotation.SuppressLint
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import io.cyanlab.loinasd.sudoku.R
import kotlinx.android.synthetic.main.main_menu.*
import kotlinx.android.synthetic.main.main_menu.view.*

class MainMenuFragment : Fragment() {


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {


        val v = inflater.inflate(R.layout.main_menu, container, false)
        val selector = GameMenuSelector()
        v.startGame.setOnClickListener(selector)
        v.about.setOnClickListener(selector)

        return v
    }


    inner class GameMenuSelector: View.OnClickListener{
        @SuppressLint("ResourceType")
        override fun onClick(p0: View?) {
            if (p0 == null) return

            val transaction = fragmentManager?.beginTransaction()

            val fragment = when (p0){

                startGame -> DifficultyFragment()
                stat -> StatFragment()
                settings -> PrefFragment()
                about -> AboutFragment()
                else -> StatFragment()
            }
            transaction?.replace(R.id.fragment, fragment)?.addToBackStack("my")?.commitAllowingStateLoss()



        }
    }


}
