package io.cyanlab.loinasd.sudoku.models.games

import android.content.Context
import android.graphics.drawable.Drawable
import io.cyanlab.loinasd.sudoku.R

enum class Game {
    CLASSIC {
        override fun getGameName(): String {
            return "Classic"
        }

        override fun getDrawable(context: Context): Drawable {
            return context.resources.getDrawable(R.drawable.sudoku_classic)
        }

    },
    ASTERISK{
        override fun getGameName(): String {
            return "Asterisk"
        }

        override fun getDrawable(context: Context): Drawable {
            return context.resources.getDrawable(R.drawable.sudoku_asterisk)
        }

    },
    CENTER_DOTTED{
        override fun getGameName(): String {
            return "Center Dotted"
        }

        override fun getDrawable(context: Context): Drawable {
            return context.resources.getDrawable(R.drawable.sudoku_center_dot)
        }

    },
    GIRANDOLA{
        override fun getGameName(): String {
            return "Girandola"
        }

        override fun getDrawable(context: Context): Drawable {
            return context.resources.getDrawable(R.drawable.sudoku_girandola)
        }

    },
    DIAGONAL {
        override fun getGameName(): String {
            return "Diagonal"
        }

        override fun getDrawable(context: Context): Drawable {
            return context.resources.getDrawable(R.drawable.sudoku_diagonal)
        }
    },
    WINDOKU{
        override fun getGameName(): String {
            return "Windoku"
        }

        override fun getDrawable(context: Context): Drawable {
            return context.resources.getDrawable(R.drawable.sudoku_windoku)
        }

    };

    abstract fun getGameName(): String
    abstract fun getDrawable(context: Context): Drawable

}