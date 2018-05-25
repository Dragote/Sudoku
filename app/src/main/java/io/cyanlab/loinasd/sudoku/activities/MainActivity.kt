package io.cyanlab.loinasd.sudoku.activities

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import io.cyanlab.loinasd.sudoku.R


open class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportFragmentManager.beginTransaction().add(R.id.fragment, MainMenuFragment()).commitNow()

    }

}
