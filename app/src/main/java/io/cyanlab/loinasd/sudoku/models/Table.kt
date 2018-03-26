package io.cyanlab.loinasd.sudoku.models

import java.util.Arrays
import java.util.Random
import java.util.logging.Logger


open class Table internal constructor() {

    open lateinit var table: Array<IntArray>
    protected open val r: Random = Random()
    protected open val log: Logger = Logger.getLogger(Table::class.java.name)
    protected open val MAX_METHODS_COUNT = 3
    protected open val NO_FORBIDDEN = -1
    protected open val MAX_TRIALS = 100
    protected open val MAX_TRIALS_FOR_LAST_SQUARE = 20


    private val N = 10000


    private val fullPattern = arrayOf(
            intArrayOf(1, 2, 3, 4, 5, 6, 7, 8, 9),
            intArrayOf(4, 5, 6, 7, 8, 9, 1, 2, 3),
            intArrayOf(7, 8, 9, 1, 2, 3, 4, 5, 6),
            intArrayOf(2, 3, 4, 5, 6, 7, 8, 9, 1),
            intArrayOf(5, 6, 7, 8, 9, 1, 2, 3, 4),
            intArrayOf(8, 9, 1, 2, 3, 4, 5, 6, 7),
            intArrayOf(3, 4, 5, 6, 7, 8, 9, 1, 2),
            intArrayOf(6, 7, 8, 9, 1, 2, 3, 4, 5),
            intArrayOf(9, 1, 2, 3, 4, 5, 6, 7, 8))

    init {
        //.info(validTable(fullPattern).toString())
        //createTable(r)
    }

    fun createTable(){
        while(!generate()){System.gc()}
        System.gc()
        printMatrix(table)
    }

    protected open fun generate(): Boolean{
        table = Array(9,{ IntArray(9,{ j -> 0}) })

        var isOK = true

        for (i in 0..6){
            var count = 0
            while (!fillSquare(i,r) && count < MAX_TRIALS){
                System.gc()
                count++}

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
                while (!fillSquare(7,r) && count < MAX_TRIALS_FOR_LAST_SQUARE){count++}
                if (count < MAX_TRIALS_FOR_LAST_SQUARE) {
                    if (fillSquare(8,r))
                        isLastFilled = true
                    else
                        clearSq(7)
                }else {
                    break
                }
            }while (!isLastFilled)

            return isLastFilled

        }else return false
    }



    protected open fun clearSq(sqNumber: Int){

        val offsetX = sqNumber % 3 * 3
        val offsetY = sqNumber / 3 * 3

        for (i in offsetY..offsetY+2)
            for (j in offsetX..offsetX+2)
                table[i][j] = 0
    }

    protected open fun fillSquare(sqNumber: Int, r: Random): Boolean{
        val offsetX = sqNumber % 3 * 3
        val offsetY = sqNumber / 3 * 3

        val square = BooleanArray(9, { false })

        val buffer = Array(3,{ IntArray(3, {0}) })


        val columns = Array(3, {BooleanArray(9, {false})})

        for (x in offsetX..offsetX+2){
            columns[x-offsetX] = getColumn(x)
        }

        val rows = Array(3, {BooleanArray(9, {false})})

        for (y in offsetY..offsetY+2){
            rows[y-offsetY] = getRow(y)
        }

        if (!fillBuffer(buffer, square, rows, columns, sqNumber)) return false

        pushBuffer(buffer, offsetY, offsetX, sqNumber)

        return true
    }


    protected open fun fillBuffer(buffer: Array<IntArray>, square: BooleanArray, rows: Array<BooleanArray>, columns: Array<BooleanArray>, sqNumber: Int): Boolean{
        for (y in 0 until 3){
            for (x in 0 until 3){
                if (!fillCell(y, x, buffer, BooleanArray(9, { i: Int -> square[i] || rows[y][i] || columns[x][i]}), square)) return false
            }
        }
        return true
    }

    protected open fun pushBuffer(buffer: Array<IntArray>, offsetY: Int, offsetX: Int, sqNumber: Int){
        for (y in 0..2)
            for (x in 0..2){
                table[y + offsetY][x + offsetX] = buffer[y][x]
            }
    }

    protected open fun fillCell(y: Int, x: Int, buffer: Array<IntArray>,  flags: BooleanArray, square: BooleanArray): Boolean{

        var count = 0

        for (i in 0..8) if (flags[i]) count++

        if (count == 9) return false

        val num = getNumber(flags ,r.nextInt(9))

        square[num] = true

        buffer[y][x] = num + 1

        return true
    }


    protected open fun getNumber(flags:BooleanArray, rand: Int): Int{
        if (!flags[rand]) return rand

        var number = 0
        var counter = 0

        do{
            if (!flags[number]) counter++
            number++
            if (number == 9) {
                number = 0
            }
        }while(counter != rand+1)

        return when (number != 0) {
            true -> --number
            false -> 8
        }
    }

    protected open fun getRow(y: Int):BooleanArray{
        val row = BooleanArray(9, { false })

        for (i in 0..8)
            if (table[y][i] != 0) row[table[y][i]-1] = true
        return row
    }

    protected open fun getColumn(x: Int):BooleanArray{
        val column = BooleanArray(9, { false })

        for (i in 0..8)
            if (table[i][x] != 0) column[table[i][x]-1] = true
        return column
    }

    protected open fun createTable(r: Random){//:Array<IntArray> {
        table = Arrays.copyOf(fullPattern, 9)

        var count = 0
        var curBound = 0
        for (i in 1 until N) {
            val bound = randomize(table, r, NO_FORBIDDEN)

            if (curBound == bound) count++

            if (count == MAX_METHODS_COUNT){
                curBound = randomize(table, r, bound)
                count == 0
            }
            //printMatrix(table)
           // println(validTable(table))
            println()
        }
        //return current
    }


    private fun randomize(matrix: Array<IntArray>, r: Random, forbidden: Int): Int {
        var bound: Int

        do  {
            bound = r.nextInt(4)
        }while (forbidden != NO_FORBIDDEN && bound == forbidden)

        val bigIndex = r.nextInt(3)
        val x1 = r.nextInt(3)
        var x2 = r.nextInt(3)
        if (x1 == x2) x2 += r.nextInt(2)+1
        if (x2 >= 3) x2 = 0
        when (bound) {
            0 ->{
                println("swipe $x1 and $x2 columns in $bigIndex big column")
                swipeColumns(matrix,bigIndex, x1, x2)
                return 0
            }

            1 -> {
                println("swipe $x1 and $x2 rows in $bigIndex big row")
                swipeRows(matrix, bigIndex, x1, x2)
                return 1
            }
            2 -> {
                println("swipe $x1 and $x2 big columns")
                swipeBigColumns(matrix, x1, x2)
                return 2
            }
            3 -> {
                println("swipe $x1 and $x2 big rows")
                swipeBigRows(matrix, x1, x2)
                return 3
            }
            4 -> {
                println("transposition")
                t(matrix)
                return 4
            }
        }
        return bound
    }
    //---------MATH LOGIC---------------

    private fun t(matrix: Array<IntArray>) {

        var t: Int
        for (i in matrix.indices) {
            for (j in i until matrix[0].size) {
                if (i != j) {
                    t = matrix[i][j]
                    matrix[i][j] = matrix[j][i]
                    matrix[j][i] = t
                }
            }
        }
        printMatrix(matrix)
    }


    private fun swipeColumns(matrix: Array<IntArray>, bigColumn: Int, first: Int, second: Int) {

        val x1 = first + bigColumn*3
        val x2 = second + bigColumn*3

        var t: Int
        for (i in matrix.indices) {
            t = matrix[i][x1]
            matrix[i][x1] = matrix[i][x2]
            matrix[i][x2] = t
        }
        printMatrix(matrix)
    }

    private fun swipeRows(matrix: Array<IntArray>, bigRaw: Int, first: Int, second: Int) {

        val x1 = first + bigRaw*3
        val x2 = second + bigRaw*3

        var t: Int
        for (i in matrix.indices) {
            t = matrix[x1][i]
            matrix[x1][i] = matrix[x2][i]
            matrix[x2][i] = t
        }
        printMatrix(matrix)

    }

    private fun swipeBigColumns(matrix: Array<IntArray>, first: Int, second: Int) {

        val firstStart = first * 3
        val secondStart = second * 3
        var t: Int
        for (column in 0..2) {
            for (row in 0..8) {
                t = matrix[row][firstStart + column]
                matrix[row][firstStart + column] = matrix[row][secondStart + column]
                matrix[row][secondStart + column] = t
            }
        }
        printMatrix(matrix)
    }

    private fun swipeBigRows(matrix: Array<IntArray>, first: Int, second: Int) {

        val firstStart = first * 3
        val secondStart = second * 3
        var t: Int
        for (row in 0..2) {
            for (column in 0..8) {
                t = matrix[row + firstStart][column]
                matrix[row + firstStart][column] = matrix[row + secondStart][column]
                matrix[row + secondStart][column] = t
            }
        }
        printMatrix(matrix)
    }

    open fun printMatrix(matrix: Array<IntArray>) {
        println("############################")
        for (row in matrix) {
            for (n in row) {
                print(n.toString() + " ")
            }
            println()
        }
        println("______________________")
    }

    private fun validTable(matrix: Array<IntArray>): Boolean {

        /*val c1 = false;
        val r1 = false
        val c2 = false;
        val r2 = false
        val c3 = false;
        val r3 = false
        val c4 = false;
        val r4 = false
        val c5 = false;
        val r5 = false
        val c6 = false;
        val r6 = false
        val c7 = false;
        val r7 = false
        val c8 = false;
        val r8 = false
        val c9 = false;
        val r9 = false

        var c = booleanArrayOf(false, false, false, false, false, false, false, false, false)
        var r = booleanArrayOf(false, false, false, false, false, false, false, false, false)
*/

/*            for (row in matrix){
                if (r[row[0]])
                    return false
                //r[row[]]
                c = booleanArrayOf(false,false,false,false,false,false,false,false,false)
                for (column in row) {
                    if (c[column])
                        return false

                }
            }
            return true

        }*/
        return true
    }
}
