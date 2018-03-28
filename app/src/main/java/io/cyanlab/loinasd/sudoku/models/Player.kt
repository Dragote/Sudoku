package io.cyanlab.loinasd.sudoku.models

/**
 * Created by Lev on 19.03.2018.
 */
import android.graphics.Bitmap
import android.graphics.Canvas
import io.cyanlab.loinasd.sudoku.view.GameView

class Player//конструктор
(
        /**Объект главного класса */
        internal var gameView: GameView, //спрайт
        internal var bmp: Bitmap) {

    //х и у координаты рисунка
    internal var x: Int = 0
    internal var y: Int = 0

    init {

        this.x = 0                        //отступ по х нет
        this.y = gameView.height / 2 //делаем по центру
    }//возвращаем рисунок

    //рисуем наш спрайт
    fun onDraw(c: Canvas) {
        c.drawBitmap(bmp, x.toFloat(), y.toFloat(), null)
    }
}