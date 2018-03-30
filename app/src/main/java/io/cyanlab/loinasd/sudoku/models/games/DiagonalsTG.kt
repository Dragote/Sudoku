package io.cyanlab.loinasd.sudoku.models.games

import io.cyanlab.loinasd.sudoku.view.IOut

/**
 * Created by Анатолий on 26.03.2018.
 */

@Deprecated(message = "DGTG works much faster and uses less memory")
class DiagonalsTG(out: IOut): TableGenerator(out) {



    private val PRIMARY_DIAGONAL_SQUARES = intArrayOf(0, 4, 8)
    private val SECONDARY_DIAGONAL_SQUARES = intArrayOf(2, 4, 6)

    private lateinit var primeDiagonal : BooleanArray
    private lateinit var secondDiagonal: BooleanArray

    private val prim = arrayOf(intArrayOf(1,1),
            intArrayOf(0,0),
            intArrayOf(2,2),
            intArrayOf(2,0),
            intArrayOf(0,2),
            intArrayOf(0,1),
            intArrayOf(2,1),
            intArrayOf(1,0),
            intArrayOf(1,2))

    private val second = arrayOf(intArrayOf(1,1),
            intArrayOf(0,2),
            intArrayOf(2,0),
            intArrayOf(2,2),
            intArrayOf(0,0),
            intArrayOf(0,1),
            intArrayOf(2,1),
            intArrayOf(1,0),
            intArrayOf(1,2))



    override fun generate(): Boolean{

        completeTable = Array(9,{ IntArray(9,{0}) })

        rows = Array(9, {i -> BooleanArray(9, {false})})
        columns = Array(9, {i -> BooleanArray(9, {false})})

        primeDiagonal = BooleanArray(9, {false})
        
        secondDiagonal = BooleanArray(9, {false})
        
        var isOK = true

        for (i in arrayOf(4, 0, 8, 2, 6, 1, 7)){
            var count = 0

            while (!fillSquare(i,r) && count < MAX_TRIALS){count++}

            if (count >= MAX_TRIALS) {
                isOK = false
                break
            }

            //printMatrix(completeTable)
        }

        if (isOK) {
            var isLastFilled = false

            var count = 0
            do {
                while (!fillSquare(3,r) && count < MAX_TRIALS_FOR_LAST_SQUARE){count++}
                if (count < MAX_TRIALS_FOR_LAST_SQUARE) {
                    if (fillSquare(5,r))
                        isLastFilled = true
                    else
                        clearSquare(3)
                }else {
                    break
                }
            }while (!isLastFilled)

            //printMatrix(completeTable)

            return isLastFilled

        }else return false
    }

    override fun fillBuffer(buffer: Array<IntArray>, square: BooleanArray, sqNumber: Int): Boolean {

        val offsetX = sqNumber % 3 * 3
        val offsetY = sqNumber / 3 * 3

        val isPrime = PRIMARY_DIAGONAL_SQUARES.contains(sqNumber)
        val isSecond = SECONDARY_DIAGONAL_SQUARES.contains(sqNumber)

        for (i in 0..8){
            val y = if (!isSecond) prim[i][0] else second[i][0]
            val x = if (!isSecond) prim[i][1] else second[i][1]
            if (!fillCell(y, x, buffer, BooleanArray(9, { j: Int -> square[j] || rows[y + offsetY][j] || columns[x+ offsetX][j]
                            || if (isPrime && y == x) primeDiagonal[j] else false
                            || if (isSecond && y == 2 - x) secondDiagonal[j] else false
                    }), square)) return false
        }

        return true
    }

    override fun pushBuffer(buffer: Array<IntArray>, offsetY: Int, offsetX: Int, sqNumber: Int) {

        val isPrime = PRIMARY_DIAGONAL_SQUARES.contains(sqNumber)
        val isSecond = SECONDARY_DIAGONAL_SQUARES.contains(sqNumber)

        for (y in 0..2)
            for (x in 0..2){
                completeTable[y + offsetY][x + offsetX] = buffer[y][x]
                if (y == x && isPrime) primeDiagonal[buffer[y][x] - 1] = true
                if (y == 2-x && isSecond) secondDiagonal[buffer[y][x] - 1] = true
                rows[y+offsetY][buffer[y][x] - 1] = true
                columns[x+offsetX][buffer[y][x] - 1] = true
            }
    }




}