package io.cyanlab.loinasd.sudoku.models

import java.util.ArrayList
import java.util.Arrays
import java.util.Random
import java.util.logging.Logger

/**
 * Created by Lev on 19.03.2018.
 */

class Table internal constructor() {

    private lateinit var table: Array<IntArray>
    private val r: Random = Random()
    val log: Logger = Logger.getLogger(Table::class.java.name)




    private val N = 100
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
        log.info(validTable(fullPattern).toString())
        createTable(r)
        //printMatrix(table)
    }

    private fun createTable(r: Random): Unit{//Array<IntArray> {
        table = Arrays.copyOf(fullPattern, 9)
        for (i in 1 until N) {
            println(randomize(table, r))
            printMatrix(table)
            println(validTable(table))
            println()
        }
        //return current
    }


    private fun randomize(matrix: Array<IntArray>, r: Random): Int {
        val bound = r.nextInt(4)
        when (bound) {
            0 ->{
                val x1 = r.nextInt(9)
                val x2 = r.nextInt(9)
                println("swipe ${x1} and ${x2} colums")
                swipeColumns(matrix, x1, x2)
                return 0
            }

            1 -> {
                val x1 = r.nextInt(9)
                val x2 = r.nextInt(9)
                println("swipe ${x1} and ${x2} rows")
                swipeRows(matrix, x1, x2)
                return 1
            }
            2 -> {
                val x1 = r.nextInt(3)
                val x2 = r.nextInt(3)
                println("swipe ${x1} and ${x2} big columns")
                swipeBigColumns(matrix, x1, x2)
                return 2
            }
            3 -> {
                val x1 = r.nextInt(3)
                val x2 = r.nextInt(3)
                println("swipe ${x1} and ${x2} big rows")
                swipeBigRows(matrix, x1, x2)
                return 3
            }
        }
        return -1
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

    private fun swipeColumns(matrix: Array<IntArray>, first: Int, second: Int) {

        if (first == second) return

        if (Math.abs(first - second) <= 2 && first / 3 == second / 3) {
            var t: Int
            for (i in matrix.indices) {
                t = matrix[i][first]
                matrix[i][first] = matrix[i][second]
                matrix[i][second] = t
            }
            printMatrix(matrix)
        }
    }

    private fun swipeRows(matrix: Array<IntArray>, first: Int, second: Int) {

        if (first == second) return

        if (first > 8 || second > 8 || first < 0 || second < 0) return

        if (Math.abs(first - second) <= 2 && (first) / 3 == (second) / 3) {
            var t: Int
            for (i in matrix.indices) {
                t = matrix[first][i]
                matrix[first][i] = matrix[second][i]
                matrix[second][i] = t
            }
            printMatrix(matrix)
        }
    }

    private fun swipeBigColumns(matrix: Array<IntArray>, first: Int, second: Int) {

        if (first == second) return

        if (first > 3 || second > 3 || first < 0 || second < 0) return

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

        if (first == second) return

        if (first > 3 || second > 3 || first < 0 || second < 0) return

        val firstStart = first * 3
        val secondStart = first * 3
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

    private fun printMatrix(matrix: Array<IntArray>) {
        println("############################")
        for (row in matrix) {
            for (n in row) {
                print(n.toString() + " ")
            }
            println()
        }
        println("______________________")
    }

    fun validTable(matrix: Array<IntArray>): Boolean {

        val c1 = false;
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
