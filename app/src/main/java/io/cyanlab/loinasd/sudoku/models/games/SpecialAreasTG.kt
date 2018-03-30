package io.cyanlab.loinasd.sudoku.models.games

import io.cyanlab.loinasd.sudoku.view.IOut

/**
 * Created by Анатолий on 27.03.2018.
 */
abstract class SpecialAreasTG(out: IOut): TableGenerator(out) {

    protected abstract val specialAreasCoords : Array<ArrayList<IntArray>>

    protected open lateinit var specialAreas : Array<BooleanArray>

    override fun generate(): Boolean {
        specialAreas = Array<BooleanArray>(specialAreasCoords.size, {BooleanArray(9, {false})})
        return super.generate()
    }

    override fun fillBuffer(buffer: Array<IntArray>, square: BooleanArray, sqNumber: Int): Boolean {
        val offsetX = sqNumber % 3 * 3
        val offsetY = sqNumber / 3 * 3

        for (y in 0 until 3){
            for (x in 0 until 3){
                var isInArea = false
                val areas = BooleanArray(specialAreas.size,{false})
                for (area in specialAreasCoords){
                    for (cell in area){
                        if (y + offsetY == cell[0] && x + offsetX == cell[1]){
                            areas[specialAreasCoords.indexOf(area)] = true
                            isInArea = true
                        }
                    }
                }
                if (isInArea){
                    if (!fillCell(y, x, buffer, BooleanArray(9, { i: Int -> square[i] || rows[y+ offsetY][i] || columns[x + offsetX][i] || getAreas(areas, i)}), square)) return false
                }
            }
        }

        for (y in 0 until 3){
            for (x in 0 until 3){
                if (buffer[y][x] == 0){
                    if (!fillCell(y, x, buffer, BooleanArray(9, { i: Int -> square[i] || rows[y+ offsetY][i] || columns[x + offsetX][i]}), square)) return false
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
                val areas = BooleanArray(specialAreas.size,{false})
                for (area in specialAreasCoords){
                    for (cell in area){
                        if (y + offsetY == cell[0] && x + offsetX == cell[1]){
                            areas[specialAreasCoords.indexOf(area)] = true
                        }
                    }
                }
                for (i in areas.indices){
                    if (areas[i]) specialAreas[i][buffer[y][x] - 1] = true
                }
                completeTable[y + offsetY][x + offsetX] = buffer[y][x]
                rows[y+offsetY][buffer[y][x] - 1] = true
                columns[x+offsetX][buffer[y][x] - 1] = true
            }
    }


}