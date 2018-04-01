package io.cyanlab.loinasd.sudoku.models.games

/**
 * Created by Анатолий on 26.03.2018.
 */
abstract class SpecialCellsTG(): TableGenerator() {

    abstract val specialCellsCoords : Array<IntArray>

    private lateinit var specialCells : BooleanArray

    override fun generate(): Boolean {
        specialCells = BooleanArray(9,{false})
        return super.generate()
    }

    override fun fillBuffer(buffer: Array<IntArray>, square: BooleanArray, sqNumber: Int): Boolean {

        val offsetX = sqNumber % 3 * 3
        val offsetY = sqNumber / 3 * 3

        val specY = specialCellsCoords[sqNumber][0]
        val specX = specialCellsCoords[sqNumber][1]

        if (!fillCell(specY, specX, buffer, BooleanArray(9, { i: Int -> square[i] || table.rows[specY+ offsetY][i] || table.columns[specX+ offsetX][i] || specialCells[i]
                }), square)) return false

        for (y in 0..2){
            for (x in 0..2){
                if (x != specX || y != specY) {
                    if (!fillCell(y, x, buffer, BooleanArray(9, { i: Int -> square[i] || table.rows[y + offsetY][i] || table.columns[x + offsetX][i]
                                    || if (y == specialCellsCoords[sqNumber][0] && x == specialCellsCoords[sqNumber][1]) specialCells[i] else false
                            }), square)) return false
                }
            }
        }
        return true
    }

    override fun pushBuffer(buffer: Array<IntArray>, offsetY: Int, offsetX: Int, sqNumber: Int) {

        val specY = specialCellsCoords[sqNumber][0]
        val specX = specialCellsCoords[sqNumber][1]

        specialCells[buffer[specY][specX] - 1] = true

        for (y in 0..2)
            for (x in 0..2){
                table.completeTable[y + offsetY][x + offsetX] = buffer[y][x]
                table.rows[y+offsetY][buffer[y][x] - 1] = true
                table.columns[x+offsetX][buffer[y][x] - 1] = true
            }
    }

    override fun startFromBeginning() {
        super.startFromBeginning()

        specialCells = BooleanArray(9, {true})
    }

    override fun nullCell(y: Int, x: Int) {
        super.nullCell(y, x)

        val sqNumber = (y / 3) * 3 + x / 3

        val offsetX = sqNumber % 3 * 3
        val offsetY = sqNumber / 3 * 3

        if (y - offsetY == specialCellsCoords[sqNumber][0] && x - offsetX == specialCellsCoords[sqNumber][1]){
            specialCells[pointerNumber - 1] = false
        }
    }

    override fun restoreCell(y: Int, x: Int) {
        super.restoreCell(y, x)

        val sqNumber = (y / 3) * 3 + x / 3

        val offsetX = sqNumber % 3 * 3
        val offsetY = sqNumber / 3 * 3

        if (y - offsetY == specialCellsCoords[sqNumber][0] && x - offsetX == specialCellsCoords[sqNumber][1]){
            specialCells[pointerNumber - 1] = true
        }

    }

    override fun getPossibleNumbers(y: Int, x: Int): BooleanArray {
        val flags = super.getPossibleNumbers(y, x)

        val sqNumber = (y / 3) * 3 + x / 3

        val offsetX = sqNumber % 3 * 3
        val offsetY = sqNumber / 3 * 3

        if (y - offsetY == specialCellsCoords[sqNumber][0] && x - offsetX == specialCellsCoords[sqNumber][1]){
            for (i in 0 until flags.size){
                flags[i] = flags[i] || specialCells[i]
            }
        }

        return flags
    }

    override fun fakeCell(table: Array<IntArray>, y: Int, x: Int, number: Int) {
        super.fakeCell(table, y, x, number)

        val sqNumber = (y / 3) * 3 + x / 3

        val offsetX = sqNumber % 3 * 3
        val offsetY = sqNumber / 3 * 3

        if (y - offsetY == specialCellsCoords[sqNumber][0] && x - offsetX == specialCellsCoords[sqNumber][1]){
            specialCells[number] = true
        }

    }

    override fun reFakeCell(table: Array<IntArray>, y: Int, x: Int, number: Int) {
        super.reFakeCell(table, y, x, number)

        val sqNumber = (y / 3) * 3 + x / 3

        val offsetX = sqNumber % 3 * 3
        val offsetY = sqNumber / 3 * 3

        if (y - offsetY == specialCellsCoords[sqNumber][0] && x - offsetX == specialCellsCoords[sqNumber][1]){
            specialCells[number] = false
        }
    }


}