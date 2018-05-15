package io.cyanlab.loinasd.sudoku.models.games

import io.cyanlab.loinasd.sudoku.models.Table
import io.cyanlab.loinasd.sudoku.view.Loggable
import java.util.Random


open class TableGenerator(): Loggable {

    open lateinit var completeTable: Array<IntArray>
    protected open val r: Random = Random()
    protected open val MAX_METHODS_COUNT = 3
    protected open val MAX_TRIALS = 20
    protected open val MAX_TRIALS_FOR_LAST_SQUARE = 20
    open lateinit var penTable: Array<IntArray>
    open val LOGGING = true
    //val out: ConsoleView = ConsoleView(LOGGING)

    open val DIFFICULTY_EASY = 81 - 30
    open val DIFFICULTY_MEDIUM = 81 - 26
    open val DIFFICULTY_HARD = 81 - 24

    protected lateinit var columns: Array<BooleanArray>
    protected lateinit var rows: Array<BooleanArray>
    protected lateinit var squares: Array<BooleanArray>

    protected lateinit var trials: BooleanArray

    fun generateTable(): Table {
        while (!generate()) {
            System.gc()
        }
        System.gc()
        return fillTable()
        //printCompleteTable(table)
    }

    //---------------------------------------------

    private fun fillTable(): Table = Table(rows, columns, squares, completeTable)

    protected open fun generate(): Boolean {
        completeTable = Array(9, { IntArray(9, { j -> 0 }) })
        rows = Array(9, { BooleanArray(9, { false }) })
        columns = Array(9, { BooleanArray(9, { false }) })
        squares = Array(9, { BooleanArray(9, { false }) })


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
                completeTable[i][j] = 0
                //table.completeTable[i][j] = 0
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
                if (!fillCell(y, x, buffer, BooleanArray(9, { i: Int -> square[i] || rows[y + offsetY][i] || columns[x + offsetX][i] }), square)) return false
            }
        }
        return true
    }

    protected open fun pushBuffer(buffer: Array<IntArray>, offsetY: Int, offsetX: Int, sqNumber: Int) {
        for (y in 0..2)
            for (x in 0..2) {
                completeTable[y + offsetY][x + offsetX] = buffer[y][x]
                rows[y + offsetY][buffer[y][x] - 1] = true
                columns[x + offsetX][buffer[y][x] - 1] = true
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

            if ((count < 20 || count % 3 == 0) && findFirst(penTable) != 1) {
                restoreCell(y, x)
            } else {
                count--
            }

        }

        //out.printGameTable(table)
    }

    protected open fun startFromBeginning() {

        penTable = Array(9, { y -> IntArray(9, { x -> completeTable[y][x] }) })

        squares = Array(9, { BooleanArray(9, { true }) })
        rows = Array(9, { BooleanArray(9, { true }) })
        columns = Array(9, { BooleanArray(9, { true }) })

        trials = BooleanArray(81, { false })

    }

    protected open fun nullCell(y: Int, x: Int) {
        pointerNumber = penTable[y][x]

        rows[y][pointerNumber - 1] = false
        columns[x][pointerNumber - 1] = false
        squares[(y / 3) * 3 + x / 3][pointerNumber - 1] = false

        penTable[y][x] = 0

        pointerY = y
        pointerX = x

        trials[y * 9 + x] = true
    }

    protected open fun restoreCell(y: Int, x: Int) {
        penTable[y][x] = pointerNumber

        rows[y][pointerNumber - 1] = true
        columns[x][pointerNumber - 1] = true
        squares[(y / 3) * 3 + x / 3][pointerNumber - 1] = true
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
        return BooleanArray(9, { i -> squares[(y / 3) * 3 + x / 3][i] || rows[y][i] || columns[x][i] })
    }

    protected open fun fakeCell(table: Array<IntArray>, y: Int, x: Int, number: Int) {
        rows[y][number] = true
        columns[x][number] = true
        squares[(y / 3) * 3 + x / 3][number] = true
        table[y][x] = number + 1
    }

    protected open fun reFakeCell(table: Array<IntArray>, y: Int, x: Int, number: Int) {
        table[y][x] = 0
        rows[y][number] = false
        columns[x][number] = false
        squares[(y / 3) * 3 + x / 3][number] = false
    }

}
