package io.cyanlab.loinasd.sudoku.models

class Memento{

    val stack = ArrayList<Step>()

    fun undo(): Step?{

        if (stack.size == 0)
            return null

        val last = stack[stack.lastIndex]
        stack.removeAt(stack.lastIndex)

        return last
    }

    fun step(cellIndex: Int, number: Int){

        stack.add(Step(cellIndex, number))
    }


    inner class Step(val cellIndex: Int, val number: Int)
}