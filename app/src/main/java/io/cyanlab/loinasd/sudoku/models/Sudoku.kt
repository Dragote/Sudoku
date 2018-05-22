package io.cyanlab.loinasd.sudoku.models

import io.cyanlab.loinasd.sudoku.models.games.Table

class Sudoku(val table: Table, val player: Player?, val memento: Memento?){
}