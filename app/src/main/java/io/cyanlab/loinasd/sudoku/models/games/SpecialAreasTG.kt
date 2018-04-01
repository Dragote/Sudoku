package io.cyanlab.loinasd.sudoku.models.games

/**
 * Created by Анатолий on 27.03.2018.
 */
abstract class SpecialAreasTG(): TableGenerator() {

    protected abstract val specialAreasCoords : Array<ArrayList<IntArray>>

    protected open lateinit var specialAreas : Array<BooleanArray>

    protected lateinit var specialAreas4Cells:  Array<Array<BooleanArray>>

    override val DIFFICULTY_HARD = 81 - 20

    override fun generate(): Boolean {

        specialAreas4Cells = Array(9,{ Array(9, { BooleanArray(specialAreasCoords.size, {false}) }) })
        for (area in 0 until specialAreasCoords.size){
            for (cell in 0 until specialAreasCoords[area].size){
                specialAreas4Cells [specialAreasCoords[area][cell][0]] [specialAreasCoords[area][cell][1]][area] = true
            }
        }

        specialAreas = Array(specialAreasCoords.size, {BooleanArray(9, {false})})
        return super.generate()
    }

    override fun fillBuffer(buffer: Array<IntArray>, square: BooleanArray, sqNumber: Int): Boolean {
        val offsetX = sqNumber % 3 * 3
        val offsetY = sqNumber / 3 * 3

        for (y in 0 until 3){
            for (x in 0 until 3){
                if (specialAreas4Cells[y + offsetY][x + offsetX].contains(true)
                        && !fillCell(y, x, buffer, BooleanArray(9, { i: Int -> square[i]
                                || table.rows[y+ offsetY][i] || table.columns[x + offsetX][i]
                                || getAreas(specialAreas4Cells[y + offsetY][x + offsetX], i)}), square)) return false
            }
        }

        for (y in 0 until 3){
            for (x in 0 until 3){
                if (buffer[y][x] == 0){
                    if (!fillCell(y, x, buffer, BooleanArray(9, { i: Int -> square[i] || table.rows[y+ offsetY][i] || table.columns[x + offsetX][i]}), square)) return false
                }
            }
        }


        return true
    }

    protected fun getAreas(areas: BooleanArray, number: Int):Boolean{
        var flag = false
        for (i in areas.indices){
            if (areas[i]) flag = flag || specialAreas[i][number]
        }
        return flag
    }

    override fun pushBuffer(buffer: Array<IntArray>, offsetY: Int, offsetX: Int, sqNumber: Int) {
        for (y in 0..2)
            for (x in 0..2){
                for (i in specialAreas4Cells[y + offsetY][x + offsetX].indices){
                    if (specialAreas4Cells[y + offsetY][x + offsetX][i]) specialAreas[i][buffer[y][x] - 1] = true
                }
                table.completeTable[y + offsetY][x + offsetX] = buffer[y][x]
                table.rows[y+offsetY][buffer[y][x] - 1] = true
                table.columns[x+offsetX][buffer[y][x] - 1] = true
            }
    }

    override fun startFromBeginning() {
        super.startFromBeginning()
        specialAreas = Array(specialAreasCoords.size, {BooleanArray(9, {true})})
    }

    override fun nullCell(y: Int, x: Int) {
        super.nullCell(y, x)

        for (areaNum in specialAreas4Cells[y][x].indices){
            if (specialAreas4Cells[y][x][areaNum]){
                specialAreas[areaNum][pointerNumber - 1] = false
            }
        }
    }

    override fun restoreCell(y: Int, x: Int) {
        super.restoreCell(y, x)

        for (areaNum in specialAreas4Cells[y][x].indices){
            if (specialAreas4Cells[y][x][areaNum]){
                specialAreas[areaNum][pointerNumber - 1] = true
            }
        }

    }

    override fun getPossibleNumbers(y: Int, x: Int): BooleanArray {
        val flags = super.getPossibleNumbers(y, x)

        val areas = specialAreas4Cells[y][x]

        for (i in 0 until areas.size){
            if (areas[i]){
                for (j in 0 until 9){
                    flags[j] = flags[j] || specialAreas[i][j]
                }
            }
        }

        return flags
    }

    override fun fakeCell(table: Array<IntArray>, y: Int, x: Int, number: Int) {
        super.fakeCell(table, y, x, number)

        val areas = specialAreas4Cells[y][x]

        for (i in 0 until areas.size){
            if (areas[i]){
                specialAreas[i][number] = true
            }
        }

    }

    override fun reFakeCell(table: Array<IntArray>, y: Int, x: Int, number: Int) {
        super.reFakeCell(table, y, x, number)

        val areas = specialAreas4Cells[y][x]

        for (i in 0 until areas.size){
            if (areas[i]){
                specialAreas[i][number] = false
            }
        }
    }


}