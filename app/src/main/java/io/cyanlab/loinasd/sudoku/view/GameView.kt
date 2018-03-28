package io.cyanlab.loinasd.sudoku.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.view.SurfaceHolder
import android.view.SurfaceView

/**
 * Created by Lev on 16.03.2018.
 */

class GameView
//-------------End of GameThread--------------------------------------------------\\

(context: Context) : SurfaceView(context) {
    /**Объект класса GameLoopThread */
    private val mThread: GameThread

    var shotX: Int = 0
    var shotY: Int = 0

    /**Переменная запускающая поток рисования */
    private var running = false

    //-------------Start of GameThread--------------------------------------------------\\

    inner class GameThread
    /**Конструктор класса */
    (
            /**Объект класса */
            private val view: GameView) : Thread() {

        /**Задание состояния потока */
        fun setRunning(run: Boolean) {
            running = run
        }

        /** Действия, выполняемые в потоке  */
        override fun run() {
            while (running) {
                var canvas: Canvas? = null
                try {
                    // подготовка Canvas-а
                    canvas = view.holder.lockCanvas()
                    synchronized(view.holder) {
                        // собственно рисование
                        doDraw(canvas)
                    }
                } catch (e: Exception) {
                } finally {
                    if (canvas != null) {
                        view.holder.unlockCanvasAndPost(canvas)
                    }
                }
            }
        }
    }

    init {

        mThread = GameThread(this)

        /*Рисуем все наши объекты и все все все*/
        holder.addCallback(object : SurfaceHolder.Callback {
            /*** Уничтожение области рисования  */
            override fun surfaceDestroyed(holder: SurfaceHolder) {
                var retry = true
                mThread.setRunning(false)
                while (retry) {
                    try {
                        // ожидание завершение потока
                        mThread.join()
                        retry = false
                    } catch (e: InterruptedException) {
                    }

                }
            }

            /** Создание области рисования  */
            override fun surfaceCreated(holder: SurfaceHolder) {
                mThread.setRunning(true)
                mThread.start()
            }

            /** Изменение области рисования  */
            override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {}
        })
    }

    /**Функция рисующая все спрайты и фон */
    protected fun doDraw(canvas: Canvas?) {
        canvas!!.drawColor(Color.WHITE)
    }
}