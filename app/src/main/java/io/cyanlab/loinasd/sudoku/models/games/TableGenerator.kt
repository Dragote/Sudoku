package io.cyanlab.loinasd.sudoku.models.games

import io.cyanlab.loinasd.sudoku.view.Loggable
import java.util.Random


open class TableGenerator(val sudoku: Table): Loggable {

    companion object {
        val DIFFICULTY_EASY = 81 - 30
        val DIFFICULTY_MEDIUM = 81 - 26
        val DIFFICULTY_HARD = 81 - 24
    }

    private val r: Random = Random()

    private val MAX_TRIALS = 20
    private val MAX_TRIALS_FOR_LAST_SQUARE = 20

    private lateinit var penTable: Array<IntArray>

    private lateinit var trials: BooleanArray

    fun generateTable(difficulty: Int): Table {

        while (!generate()) {
            System.gc()
        }

        System.gc()

        puzzleTable(
                when (difficulty){
            DIFFICULTY_EASY -> difficulty
            DIFFICULTY_MEDIUM -> difficulty
            DIFFICULTY_HARD -> difficulty
            else -> DIFFICULTY_MEDIUM
        })

        printGameTable(sudoku)
        return sudoku

    }

    //---------------------------------------------

    private fun generate(): Boolean {

        sudoku.completeTable = Array(9, { IntArray(9, { j -> 0 }) })
        sudoku.rows = Array(9, { BooleanArray(9, { false }) })
        sudoku.columns = Array(9, { BooleanArray(9, { false }) })
        sudoku.squares = Array(9, { BooleanArray(9, { false }) })

        sudoku.sectors4Cells = Array(9,{ Array(9, { BooleanArray(sudoku.sectorsCoords.size, {false}) }) })
        for (area in 0 until sudoku.sectorsCoords.size){
            for (cell in 0 until sudoku.sectorsCoords[area].size){
                sudoku.sectors4Cells [sudoku.sectorsCoords[area][cell][0]][sudoku.sectorsCoords[area][cell][1]][area] = true
            }
        }

        sudoku.sectors = Array(sudoku.sectorsCoords.size, {BooleanArray(9, {false})})


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

    private fun clearSquare(sqNumber: Int) {

        val offsetX = sqNumber % 3 * 3
        val offsetY = sqNumber / 3 * 3

        for (i in offsetY..offsetY + 2)
            for (j in offsetX..offsetX + 2)
                sudoku.completeTable[i][j] = 0
                //table.completeTable[i][j] = 0
    }

    private fun fillSquare(sqNumber: Int, r: Random): Boolean {
        val offsetX = sqNumber % 3 * 3
        val offsetY = sqNumber / 3 * 3

        val square = BooleanArray(9, { false })

        val buffer = Array(3, { IntArray(3, { 0 }) })

        if (!fillBuffer(buffer, square, sqNumber)) return false

        pushBuffer(buffer, offsetY, offsetX, sqNumber)

        return true
    }


    private fun fillBuffer(buffer: Array<IntArray>, square: BooleanArray, sqNumber: Int): Boolean {
        val offsetX = sqNumber % 3 * 3
        val offsetY = sqNumber / 3 * 3

        for (y in 0 until 3){
            for (x in 0 until 3){
                if (sudoku.sectors4Cells[y + offsetY][x + offsetX].contains(true)
                        && !fillCell(y, x, buffer, BooleanArray(9, { i: Int -> square[i]
                                || sudoku.rows[y+ offsetY][i] || sudoku.columns[x + offsetX][i]
                                || getAreas(sudoku.sectors4Cells[y + offsetY][x + offsetX], i)}), square)) return false
            }
        }

        for (y in 0 until 3){
            for (x in 0 until 3){
                if (buffer[y][x] == 0){
                    if (!fillCell(y, x, buffer, BooleanArray(9, { i: Int -> square[i] || sudoku.rows[y+ offsetY][i] || sudoku.columns[x + offsetX][i]}), square)) return false
                }
            }
        }


        return true
    }

    private fun pushBuffer(buffer: Array<IntArray>, offsetY: Int, offsetX: Int, sqNumber: Int) {
        for (y in 0..2)
            for (x in 0..2){
                for (i in sudoku.sectors4Cells[y + offsetY][x + offsetX].indices){
                    if (sudoku.sectors4Cells[y + offsetY][x + offsetX][i]) sudoku.sectors[i][buffer[y][x] - 1] = true
                }
                sudoku.completeTable[y + offsetY][x + offsetX] = buffer[y][x]
                sudoku.rows[y+offsetY][buffer[y][x] - 1] = true
                sudoku.columns[x+offsetX][buffer[y][x] - 1] = true
            }
    }

    private fun fillCell(y: Int, x: Int, buffer: Array<IntArray>, flags: BooleanArray, square: BooleanArray): Boolean {

        var count = 0

        for (i in 0..8) if (flags[i]) count++

        if (count == 9) return false

        val num = getNumber(flags, r.nextInt(9))

        square[num] = true

        buffer[y][x] = num + 1

        return true
    }


    private fun getNumber(flags: BooleanArray, rand: Int): Int {
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

    private fun getAreas(areas: BooleanArray, number: Int):Boolean{
        var flag = false
        for (i in areas.indices){
            if (areas[i]) flag = flag || sudoku.sectors[i][number]
        }
        return flag
    }


    private var pointerNumber = 0
    private var pointerY = 0
    private var pointerX = 0

    private fun puzzleTable(difficulty: Int) {

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

        sudoku.penTable = Array(9, {y -> BooleanArray(9, {x -> penTable[y][x] != 0})})

        //out.printGameTable(table)
    }

    private fun startFromBeginning() {

        penTable = Array(9, { y -> IntArray(9, { x -> sudoku.completeTable[y][x] }) })

        sudoku.squares = Array(9, { BooleanArray(9, { true }) })
        sudoku.rows = Array(9, { BooleanArray(9, { true }) })
        sudoku.columns = Array(9, { BooleanArray(9, { true }) })

        sudoku.sectors = Array(sudoku.sectorsCoords.size, {BooleanArray(9, {true})})

        trials = BooleanArray(81, { false })

    }

    fun tableToString(table: Array<IntArray>): String{

        val builder = StringBuilder()

        for (y in table.indices)
            for (x in table[y].indices)
                builder.append(table[y][x])

        return builder.toString()
    }

    private fun nullCell(y: Int, x: Int) {

        pointerNumber = penTable[y][x]

        sudoku.rows[y][pointerNumber - 1] = false
        sudoku.columns[x][pointerNumber - 1] = false
        sudoku.squares[(y / 3) * 3 + x / 3][pointerNumber - 1] = false

        for (areaNum in sudoku.sectors4Cells[y][x].indices){
            if (sudoku.sectors4Cells[y][x][areaNum]){
                sudoku.sectors[areaNum][pointerNumber - 1] = false
            }
        }

        penTable[y][x] = 0

        pointerY = y
        pointerX = x

        trials[y * 9 + x] = true
    }

    private fun restoreCell(y: Int, x: Int) {

        penTable[y][x] = pointerNumber

        sudoku.rows[y][pointerNumber - 1] = true
        sudoku.columns[x][pointerNumber - 1] = true
        sudoku.squares[(y / 3) * 3 + x / 3][pointerNumber - 1] = true

        for (areaNum in sudoku.sectors4Cells[y][x].indices){
            if (sudoku.sectors4Cells[y][x][areaNum]){
                sudoku.sectors[areaNum][pointerNumber - 1] = true
            }
        }
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

        val flags = sudoku.getPossibleNumbers(y, x)

        for (i in flags.indices) {
            if (!flags[i]) {

                fakeCell(table, y, x, i)
                sum += findFirst(table)
                reFakeCell(table, y, x, i)
            }
        }

        return sum
    }

    private fun fakeCell(table: Array<IntArray>, y: Int, x: Int, number: Int) {

        sudoku.rows[y][number] = true
        sudoku.columns[x][number] = true
        sudoku.squares[(y / 3) * 3 + x / 3][number] = true
        table[y][x] = number + 1

        val areas = sudoku.sectors4Cells[y][x]

        for (i in 0 until areas.size){
            if (areas[i]){
                sudoku.sectors[i][number] = true
            }
        }
    }

    private fun reFakeCell(table: Array<IntArray>, y: Int, x: Int, number: Int) {

        table[y][x] = 0
        sudoku.rows[y][number] = false
        sudoku.columns[x][number] = false
        sudoku.squares[(y / 3) * 3 + x / 3][number] = false

        val areas = sudoku.sectors4Cells[y][x]

        for (i in 0 until areas.size){
            if (areas[i]){
                sudoku.sectors[i][number] = false
            }
        }
    }

}
