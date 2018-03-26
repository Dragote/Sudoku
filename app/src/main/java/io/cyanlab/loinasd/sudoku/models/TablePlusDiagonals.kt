package io.cyanlab.loinasd.sudoku.models

import java.util.*

/**
 * Created by Анатолий on 26.03.2018.
 */
class TablePlusDiagonals: Table() {

    private lateinit var PRIMARY_DIAGONAL_SQUARES : IntArray
    private lateinit var SECONDARY_DIAGONAL_SQUARES: IntArray

    private lateinit var primeDiagonal: BooleanArray
    private lateinit var secondDiagonal: BooleanArray

    private lateinit var prim: Array<IntArray>
    private lateinit var second: Array<IntArray>



    override fun generate(): Boolean{

        prim = arrayOf(intArrayOf(1,1),
                intArrayOf(0,0),
                intArrayOf(2,2),
                intArrayOf(2,0),
                intArrayOf(0,2),
                intArrayOf(0,1),
                intArrayOf(2,1),
                intArrayOf(1,0),
                intArrayOf(1,2))

        second = arrayOf(intArrayOf(1,1),
                intArrayOf(0,2),
                intArrayOf(2,0),
                intArrayOf(2,2),
                intArrayOf(0,0),
                intArrayOf(0,1),
                intArrayOf(2,1),
                intArrayOf(1,0),
                intArrayOf(1,2))

        PRIMARY_DIAGONAL_SQUARES = intArrayOf(0, 4, 8)
        SECONDARY_DIAGONAL_SQUARES = intArrayOf(2, 4, 6)

        primeDiagonal = BooleanArray(9, {false})
        secondDiagonal = BooleanArray(9, {false})

        table = Array<IntArray>(9,{ IntArray(9,{ j -> 0}) })

        var isOK = true

        for (i in arrayOf(4, 0, 8, 2, 6, 1, 7)){
            var count = 0

            if (i == 2)
                print("")

            while (!fillSquare(i,r) && count < MAX_TRIALS){count++}

            if (count >= MAX_TRIALS) {
                isOK = false
                break
            }

            //printMatrix(table)
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
                        clearSq(3)
                }else {
                    break
                }
            }while (!isLastFilled)

            return isLastFilled

        }else return false
    }

    override fun fillSquare(sqNumber: Int, r: Random): Boolean {


        val offsetX = sqNumber % 3 * 3
        val offsetY = sqNumber / 3 * 3

        val square = BooleanArray(9, { false })

        val buffer = Array(3,{ IntArray(3, { 0}) })

        val columns = Array<BooleanArray>(3, {BooleanArray(9, {false})})

        for (x in offsetX..offsetX+2){
            columns[x-offsetX] = getColumn(x)
        }

        val rows = Array<BooleanArray>(3, {BooleanArray(9, {false})})

        for (y in offsetY..offsetY+2){
            rows[y-offsetY] = getRow(y)
        }

        val isPrime = PRIMARY_DIAGONAL_SQUARES.contains(sqNumber)
        val isSecond = SECONDARY_DIAGONAL_SQUARES.contains(sqNumber)


        for (i in 0..8){
            val y = if (!isSecond) prim[i][0] else second[i][0]
            val x = if (!isSecond) prim[i][1] else second[i][1]
            if (!fillCell(y, x, buffer, BooleanArray(9, { j: Int -> square[j] || rows[y][j] || columns[x][j] || when(y){
                        x -> if (isPrime) primeDiagonal[j] else false
                        else -> false
                    } || when(y){
                        (2 - x) -> if (isSecond) secondDiagonal[j] else false
                        else -> false
                    }}), square)) return false
        }

        for (y in 0..2)
            for (x in 0..2){
                table[y + offsetY][x + offsetX] = buffer[y][x]
                if (y == x && isPrime) primeDiagonal[buffer[y][x] - 1] = true

                if (y == 2-x && isSecond) secondDiagonal[buffer[y][x] - 1] = true
            }

        return true
    }




}