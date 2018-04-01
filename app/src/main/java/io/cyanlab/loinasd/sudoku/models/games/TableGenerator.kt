package io.cyanlab.loinasd.sudoku.models.games

import io.cyanlab.loinasd.sudoku.models.Table
import java.util.Random
import java.util.logging.Logger


open class TableGenerator internal constructor() {

    //open lateinit var completeTable: Array<IntArray>
    protected open val r: Random = Random()
    protected val log: Logger = Logger.getLogger(TableGenerator::class.java.name)
    protected open val MAX_METHODS_COUNT = 3
    protected open val MAX_TRIALS = 20
    protected open val MAX_TRIALS_FOR_LAST_SQUARE = 20
    //open lateinit var penTable: Array<IntArray>
    open val LOGGING = true;

    open val DIFFICULTY_EASY = 81 - 30
    open val DIFFICULTY_MEDIUM = 81 - 26
    open val DIFFICULTY_HARD = 81 - 24

/*    protected lateinit var columns: Array<BooleanArray>
    protected lateinit var rows: Array<BooleanArray>
    protected lateinit var squares: Array<BooleanArray>*/

    protected lateinit var trials: BooleanArray
    var table: Table = Table()

    fun generateTable() {
        while (!generate()) {
            System.gc()
        }
        System.gc()
        printCompleteTable(table)
    }

    //--------------LOGGING------------

    private fun printGameTable(table: Table) {
        if (LOGGING) {
            for (row in table.penTable) {
                for (n in row) {
                    print(n.toString())
                }
            }
            println()
        }

    }

    private fun printCompleteTable(table: Table) {
        if (LOGGING){
            for (row in table.completeTable) {
                for (n in row) {
                    print(n.toString())
                }
            }
            println()
        }
    }
    //---------------------------------------------

    protected open fun generate(): Boolean {
        /*completeTable = Array(9, { IntArray(9, { j -> 0 }) })
        rows = Array(9, { i -> BooleanArray(9, { false }) })
        columns = Array(9, { i -> BooleanArray(9, { false }) })*/


        var isOK = true

        for (i in 0..6) {
            var count = 0
            while (!fillSquare(i, r) && count < MAX_TRIALS) {
                count++
            }

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
                while (!fillSquare(7, r) && count < MAX_TRIALS_FOR_LAST_SQUARE) {
                    count++
                }
                if (count < MAX_TRIALS_FOR_LAST_SQUARE) {
                    if (fillSquare(8, r))
                        isLastFilled = true
                    else
                        clearSquare(7)
                } else {
                    break
                }
                //printMatrix(completeTable)
            } while (!isLastFilled)

            return isLastFilled

        } else return false
    }

    protected open fun clearSquare(sqNumber: Int) {

        val offsetX = sqNumber % 3 * 3
        val offsetY = sqNumber / 3 * 3

        for (i in offsetY..offsetY + 2)
            for (j in offsetX..offsetX + 2)
                //completeTable[i][j] = 0
                table.completeTable[i][j] = 0
    }

    protected open fun fillSquare(sqNumber: Int, r: Random): Boolean {
        val offsetX = sqNumber % 3 * 3
        val offsetY = sqNumber / 3 * 3

        val square = BooleanArray(9, { false })

        val buffer = Array(3, { IntArray(3, { 0 }) })

        if (!fillBuffer(buffer, square, sqNumber)) return false

        pushBuffer(buffer, offsetY, offsetX, sqNumber)

        return true
    }


    protected open fun fillBuffer(buffer: Array<IntArray>, square: BooleanArray, sqNumber: Int): Boolean {
        val offsetX = sqNumber % 3 * 3
        val offsetY = sqNumber / 3 * 3

        for (y in 0 until 3) {
            for (x in 0 until 3) {
                if (!fillCell(y, x, buffer, BooleanArray(9, { i: Int -> square[i] || table.rows[y + offsetY][i] || table.columns[x + offsetX][i] }), square)) return false
            }
        }
        return true
    }

    protected open fun pushBuffer(buffer: Array<IntArray>, offsetY: Int, offsetX: Int, sqNumber: Int) {
        for (y in 0..2)
            for (x in 0..2) {
                table.completeTable[y + offsetY][x + offsetX] = buffer[y][x]
                table.rows[y + offsetY][buffer[y][x] - 1] = true
                table.columns[x + offsetX][buffer[y][x] - 1] = true
            }
    }

    protected open fun fillCell(y: Int, x: Int, buffer: Array<IntArray>, flags: BooleanArray, square: BooleanArray): Boolean {

        var count = 0

        for (i in 0..8) if (flags[i]) count++

        if (count == 9) return false

        val num = getNumber(flags, r.nextInt(9))

        square[num] = true

        buffer[y][x] = num + 1

        return true
    }


    protected open fun getNumber(flags: BooleanArray, rand: Int): Int {
        if (!flags[rand]) return rand

        var number = 0
        var counter = 0

        do {
            if (!flags[number]) counter++
            number++
            if (number == flags.size) {
                number = 0
            }
        } while (counter != rand + 1)

        return when (number != 0) {
            true -> --number
            false -> flags.size - 1
        }
    }

    protected open fun getRow(table: Array<IntArray>, y: Int): BooleanArray {
        val row = BooleanArray(9, { false })

        for (i in 0..8)
            if (table[y][i] != 0) row[table[y][i] - 1] = true
        return row
    }

    protected open fun getColumn(table: Array<IntArray>, x: Int): BooleanArray {
        val column = BooleanArray(9, { false })

        for (i in 0..8)
            if (table[i][x] != 0) column[table[i][x] - 1] = true
        return column
    }

    protected open fun getSquare(table: Array<IntArray>, y: Int, x: Int): BooleanArray {

        val square = BooleanArray(9, { false })

        for (ys in (y / 3) * 3 until (y / 3) * 3 + 3)
            for (xs in (x / 3) * 3 until (x / 3) * 3 + 3)
                if (table[ys][xs] != 0) square[table[ys][xs] - 1] = true

        return square
    }


    protected var pointerNumber = 0
    protected var pointerY = 0
    protected var pointerX = 0

    fun puzzleTable(difficulty: Int) {

        var count = difficulty

        startFromBeginning()

        while (count > 0) {
            var x: Int
            var y: Int

            var counter = 0

            for (cell in trials)
                if (!cell) counter++

            if (counter < count) {

                count = difficulty
                startFromBeginning()
                System.gc()

            }


            val number = getNumber(trials, r.nextInt(81))

            x = number % 9
            y = number / 9

            nullCell(y, x)

            if ((count < 20 || count % 3 == 0) && findFirst(table.penTable) != 1) {
                restoreCell(y, x)
            } else {
                count--
            }

        }

        printGameTable(table)
    }

    protected open fun startFromBeginning() {

        table.penTable = Array(9, { y -> IntArray(9, { x -> table.completeTable[y][x] }) })

        table.squares = Array(9, { BooleanArray(9, { true }) })
        table.rows = Array(9, { BooleanArray(9, { true }) })
        table.columns = Array(9, { BooleanArray(9, { true }) })

        trials = BooleanArray(81, { false })

    }

    protected open fun nullCell(y: Int, x: Int) {
        pointerNumber = table.penTable[y][x]

        table.rows[y][pointerNumber - 1] = false
        table.columns[x][pointerNumber - 1] = false
        table.squares[(y / 3) * 3 + x / 3][pointerNumber - 1] = false

        table.penTable[y][x] = 0

        pointerY = y
        pointerX = x

        trials[y * 9 + x] = true
    }

    protected open fun restoreCell(y: Int, x: Int) {
        table.penTable[y][x] = pointerNumber

        table.rows[y][pointerNumber - 1] = true
        table.columns[x][pointerNumber - 1] = true
        table.squares[(y / 3) * 3 + x / 3][pointerNumber - 1] = true
    }

    private fun findFirst(table: Array<IntArray>): Int {

        for (y in 0 until 9)
            for (x in 0 until 9) {
                if (table[y][x] == 0) {
                    return trySolving(table, y, x)
                }
            }

        return 1
    }

    private fun trySolving(table: Array<IntArray>, y: Int, x: Int): Int {

        var sum = 0

        val flags = getPossibleNumbers(y, x)

        for (i in flags.indices) {
            if (!flags[i]) {
                fakeCell(table, y, x, i)
                sum += findFirst(table)
                reFakeCell(table, y, x, i)
            }
        }

        return sum
    }

    protected open fun getPossibleNumbers(y: Int, x: Int): BooleanArray {
        return BooleanArray(9, { i -> table.squares[(y / 3) * 3 + x / 3][i] || table.rows[y][i] || table.columns[x][i] })
    }

    protected open fun fakeCell(table: Array<IntArray>, y: Int, x: Int, number: Int) {
        /*table.rows[y][number] = true
        table.columns[x][number] = true
        table.squares[(y / 3) * 3 + x / 3][number] = true*/
        //table[y][x] = number + 1
    }

    protected open fun reFakeCell(table: Array<IntArray>, y: Int, x: Int, number: Int) {
        table[y][x] = 0
        /*rows[y][number] = false
        columns[x][number] = false
        squares[(y / 3) * 3 + x / 3][number] = false*/
    }

    private fun printMatrixAsLine(table: Table) {
        for (row in table.rows) {
            for (n in row) {
                print(n.toString())
            }
        }
    }

}
