package io.cyanlab.loinasd.sudoku.models.games


open class Table{

    var rows: Array<BooleanArray> = emptyArray()
    var columns: Array<BooleanArray> = emptyArray()
    var squares: Array<BooleanArray> = emptyArray()
    var completeTable: Array<IntArray> = emptyArray()
    var penTable: Array<BooleanArray> = emptyArray()


    open val sectorsCoords : Array<Array<IntArray>> = emptyArray()
    var sectors : Array<BooleanArray> = emptyArray()
    var sectors4Cells:  Array<Array<BooleanArray>> = emptyArray()

    fun getPossibleNumbers(y: Int, x: Int): BooleanArray {

        val flags = BooleanArray(9, { i -> squares[(y / 3) * 3 + x / 3][i] || rows[y][i] || columns[x][i] })

        val areas = sectors4Cells[y][x]

        for (i in 0 until areas.size){
            if (areas[i]){
                for (j in 0 until 9){
                    flags[j] = flags[j] || sectors[i][j]
                }
            }
        }

        return flags
    }

    fun cellEntered(y: Int, x: Int){

        val num = completeTable[y][x] - 1

        rows[y][num] = true
        columns[x][num] = true
        squares[(y / 3) * 3 + x / 3][num] = true
        penTable[y][x] = true

        val areas = sectors4Cells[y][x]

        for (i in 0 until areas.size){
            if (areas[i]){
                sectors[i][num] = true
            }
        }
    }

}