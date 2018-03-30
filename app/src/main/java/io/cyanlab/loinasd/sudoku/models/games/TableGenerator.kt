package io.cyanlab.loinasd.sudoku.models.games

import io.cyanlab.loinasd.sudoku.view.IOut
import java.util.Random
import java.util.logging.Logger


open class TableGenerator internal constructor(protected open val out: IOut) {

    open lateinit var completeTable: Array<IntArray>
    protected open val r: Random = Random()

    protected open val MAX_METHODS_COUNT = 3
    protected open val NO_FORBIDDEN = -1
    protected open val MAX_TRIALS = 20
    protected open val MAX_TRIALS_FOR_LAST_SQUARE = 20
    open lateinit var penTable: Array<IntArray>


    val DIFFICULTY_EASY = 81 - 30
    val DIFFICULTY_MEDIUM = 81 - 26
    val DIFFICULTY_HARD = 81 - 24


    private val N = 10000

    protected lateinit var columns: Array<BooleanArray>
    protected lateinit var rows: Array<BooleanArray>
    protected lateinit var squares: Array<BooleanArray>
    protected lateinit var trials: BooleanArray

    lateinit var stack : ArrayList<IntArray>
    lateinit var trialStack : ArrayList<BooleanArray>


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

    init{

        //createTable(r)
    }

    fun generateTable(){
        while(!generate()){System.gc()}
        System.gc()
        //printMatrix(completeTable)
    }

    protected open fun generate(): Boolean{
        completeTable = Array(9,{ IntArray(9,{ j -> 0}) })
        rows = Array(9, {i -> BooleanArray(9, {false})})
        columns = Array(9, {i -> BooleanArray(9, {false})})


        var isOK = true

        for (i in 0..6){
            var count = 0
            while (!fillSquare(i,r) && count < MAX_TRIALS){
                count++}

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
                while (!fillSquare(7,r) && count < MAX_TRIALS_FOR_LAST_SQUARE){count++}
                if (count < MAX_TRIALS_FOR_LAST_SQUARE) {
                    if (fillSquare(8,r))
                        isLastFilled = true
                    else
                        clearSq(completeTable, 7)
                }else {
                    break
                }
                //printMatrix(completeTable)
            }while (!isLastFilled)

            return isLastFilled

        }else return false
    }

    protected open fun clearSq(table: Array<IntArray>, sqNumber: Int){

        val offsetX = sqNumber % 3 * 3
        val offsetY = sqNumber / 3 * 3

        for (i in offsetY..offsetY+2)
            for (j in offsetX..offsetX+2)
                completeTable[i][j] = 0
    }

    protected open fun fillSquare(sqNumber: Int, r: Random): Boolean{
        val offsetX = sqNumber % 3 * 3
        val offsetY = sqNumber / 3 * 3

        val square = BooleanArray(9, { false })

        val buffer = Array(3,{ IntArray(3, {0}) })

        if (!fillBuffer(buffer, square, sqNumber)) return false

        pushBuffer(buffer, offsetY, offsetX, sqNumber)

        return true
    }


    protected open fun fillBuffer(buffer: Array<IntArray>, square: BooleanArray, sqNumber: Int): Boolean{
        val offsetX = sqNumber % 3 * 3
        val offsetY = sqNumber / 3 * 3

        for (y in 0 until 3){
            for (x in 0 until 3){
                if (!fillCell(y, x, buffer, BooleanArray(9, { i: Int -> square[i] || rows[y+ offsetY][i] || columns[x + offsetX][i]}), square)) return false
            }
        }
        return true
    }

    protected open fun pushBuffer(buffer: Array<IntArray>, offsetY: Int, offsetX: Int, sqNumber: Int){
        for (y in 0..2)
            for (x in 0..2){
                completeTable[y + offsetY][x + offsetX] = buffer[y][x]
                rows[y+offsetY][buffer[y][x] - 1] = true
                columns[x+offsetX][buffer[y][x] - 1] = true
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
            if (number == flags.size) {
                number = 0
            }
        }while(counter != rand+1)

        return when (number != 0) {
            true -> --number
            false -> flags.size-1
        }
    }

    protected open fun getRow(y: Int):BooleanArray{
        val row = BooleanArray(9, { false })

        for (i in 0..8)
            if (completeTable[y][i] != 0) row[completeTable[y][i]-1] = true
        return row
    }

    protected open fun getColumn(x: Int):BooleanArray{
        val column = BooleanArray(9, { false })

        for (i in 0..8)
            if (completeTable[i][x] != 0) column[completeTable[i][x]-1] = true
        return column
    }

    protected open fun getSquare(y: Int, x: Int): BooleanArray{

        val square = BooleanArray(9, { false })

        for (ys in y / 3 * 3 until y / 3 * 3 + 2)
            for (xs in x / 3 * 3 until x / 3 * 3 + 2)
                if (completeTable[y][x] != 0) square[completeTable[y][x]-1] = true

        return square
    }

    protected open fun getRow(table: Array<IntArray>, y: Int):BooleanArray{
        val row = BooleanArray(9, { false })

        for (i in 0..8)
            if (table[y][i] != 0) row[table[y][i]-1] = true
        return row
    }

    protected open fun getColumn(table: Array<IntArray>, x: Int):BooleanArray{
        val column = BooleanArray(9, { false })

        for (i in 0..8)
            if (table[i][x] != 0) column[table[i][x]-1] = true
        return column
    }

    protected open fun getSquare(table: Array<IntArray>, y: Int, x: Int): BooleanArray{

        val square = BooleanArray(9, { false })

        for (ys in (y / 3) * 3 until (y / 3) * 3 + 3)
            for (xs in (x / 3) * 3 until (x / 3) * 3 + 3)
                if (table[ys][xs] != 0) square[table[ys][xs]-1] = true

        return square
    }

    protected fun prepareGameTable(){

        penTable = Array(9,{y -> IntArray(9,{x -> completeTable[y][x]})})

        squares = Array(9, {BooleanArray(9, {true})})
        rows = Array(9, {BooleanArray(9, {true})})
        columns = Array(9, {BooleanArray(9, {true})})

        trials = BooleanArray(81, {false})

        stack = ArrayList()
        trialStack = ArrayList()

    }

    protected fun returnOneStep(){
        println("stackback")
        val y = stack[stack.lastIndex][0]
        val x = stack[stack.lastIndex][1]
        val number = stack[stack.lastIndex][2]

        trials = trialStack[trialStack.lastIndex]
        trials[y*9 + x] = true

        penTable[y][x] = number

        rows[y][number - 1] = true
        columns[x][number - 1] = true
        squares[(y / 3)* 3 + x / 3][number - 1] = true

        stack.removeAt(stack.lastIndex)
        trialStack.removeAt(trialStack.lastIndex)
    }

    protected fun nullCell(y: Int, x: Int){
        pointerNumber = penTable[y][x]

        rows[y][pointerNumber - 1] = false
        columns[x][pointerNumber - 1] = false
        squares[(y / 3) * 3 + x / 3][pointerNumber - 1] = false

        penTable[y][x] = 0

        pointerY = y
        pointerX = x

        trials[y*9 + x] = true
    }

    protected fun restoreCell(y: Int, x: Int){
        penTable[y][x] = pointerNumber

        rows[y][pointerNumber - 1] = true
        columns[x][pointerNumber - 1] = true
        squares[(y / 3)* 3 + x / 3][pointerNumber - 1] = true
    }

    protected fun putCellIntoStack(y: Int, x: Int){
        stack.add(intArrayOf(y, x, pointerNumber))
        trialStack.add(BooleanArray(81, { i -> trials[i] }))
    }

    fun createGame(difficulty: Int){

        var count = difficulty

        var trialCount = 0

        prepareGameTable()

        while (count > 0){
            var x: Int
            var y: Int

            var counter = 0

            for (cell in trials)
                if (!cell) counter++

            while (counter < count){

                trialCount++

                count++

                returnOneStep()

                counter = 0

                for (cell in trials)
                    if (!cell) counter++

                if (trialCount > 4){

                    count = difficulty
                    trialCount = 0

                    prepareGameTable()
                }

            }


            val number = getNumber(trials, r.nextInt(81))

            x = number % 9
            y = number / 9

            nullCell(y, x)

            if ((count < 20 || count % 3 == 0) && findFirst(penTable) != 0){
                restoreCell(y, x)
            }
            else {
                count--
                putCellIntoStack(y, x)
            }

        }

        out.printGameTable(this)
    }

    private fun printMatrixAsLine(table: Array<IntArray>) {
        for (row in table) {
            for (n in row) {
                print( if (n == 0) "0" else n.toString())
            }
        }
    }

    var pointerY = 0
    var pointerX = 0
    var pointerNumber = 0

    fun findFirst(table: Array<IntArray>): Int{

        for (y in 0 until 9)
            for (x in 0 until 9) {
                if (table[y][x] == 0){
                    return trySolving(table, y, x)
                }
            }

        return 1
    }

    protected fun trySolving(table: Array<IntArray>, y: Int, x: Int): Int{

        var sum = 0

        val flags = BooleanArray(9, {i -> squares[(y / 3) * 3 + x / 3][i] || rows[y][i] || columns[x][i]})

        for (i in flags.indices){
            if (!flags[i] && (y != pointerY || x != pointerX || pointerNumber != i + 1)){

                rows[y][i] = true
                columns[x][i] = true
                squares[(y / 3) * 3 + x / 3][i] = true

                table[y][x] = i + 1
                sum += findFirst(table)
                table[y][x] = 0

                rows[y][i] = false
                columns[x][i] = false
                squares[(y / 3) * 3 + x / 3][i] = false
            }
        }

        return sum
    }



    /*protected open fun createTable(r: Random){//:Array<IntArray> {
        completeTable = Arrays.copyOf(fullPattern, 9)

        var count = 0
        var curBound = 0
        for (i in 1 until N) {
            val bound = randomize(completeTable, r, NO_FORBIDDEN)

            if (curBound == bound) count++

            if (count == MAX_METHODS_COUNT){
                curBound = randomize(completeTable, r, bound)
                count == 0
            }
            //printMatrix(completeTable)
           // out(validTable(completeTable))
            out()
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
                out("swipe $x1 and $x2 columns in $bigIndex big column")
                swipeColumns(matrix,bigIndex, x1, x2)
                return 0
            }

            1 -> {
                out("swipe $x1 and $x2 rows in $bigIndex big row")
                swipeRows(matrix, bigIndex, x1, x2)
                return 1
            }
            2 -> {
                out("swipe $x1 and $x2 big columns")
                swipeBigColumns(matrix, x1, x2)
                return 2
            }
            3 -> {
                out("swipe $x1 and $x2 big rows")
                swipeBigRows(matrix, x1, x2)
                return 3
            }
            4 -> {
                out("transposition")
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

    open fun printMatrixAsLine(matrix: Array<IntArray>) {
        out("############################")
        for (row in matrix) {
            for (n in row) {
                print( if (n == 0) "0" else n.toString())
            }
        }
        out("______________________")
    }

    private fun validTable(matrix: Array<IntArray>): Boolean {

        *//*val c1 = false;
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
*//*

*//*            for (row in matrix){
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

        }*//*
        return true
    }*/
}
