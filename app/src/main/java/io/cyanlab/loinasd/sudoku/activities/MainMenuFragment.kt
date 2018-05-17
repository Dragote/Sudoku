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
            val transaction = fragmentManager.beginTransaction()
            when(p0){
                startGame -> transaction.replace(R.id.fragment, DifficultyFragment()).addToBackStack("my").commitAllowingStateLoss()
                stat -> transaction.replace(R.id.fragment, StatFragment()).addToBackStack("my").commitAllowingStateLoss()
                settings -> transaction.replace(R.id.fragment, PrefFragment()).addToBackStack("my").commitAllowingStateLoss()
                about -> transaction.replace(R.id.fragment, AboutFragment()).addToBackStack("my").commitAllowingStateLoss()
            }



        }
    }


}
