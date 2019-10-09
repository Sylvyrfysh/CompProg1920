package week2

import npj.JVMProblemWrapper
import kotlin.math.abs

object PokemonViridianForest {
    @JvmStatic
    fun main(args: Array<String>) {
        JVMProblemWrapper.loopOverLambda(JVMProblemWrapper.Problem.CSAcademy.ViridianForest, ::wrapper)
    }

    fun wrapper() {
        val dim = readLine()!!.split(" ").run { Pair(this[0].toInt(), this[1].toInt()) }
        bestAt = Array(dim.first) { IntArray(dim.second) {Int.MAX_VALUE} }
        arr = Array(dim.first) {
            val parts = readLine()!!.split(" ").map(String::toInt)
            IntArray(dim.second) { parts[it] }
        }
        r(1, 1, Pair(0, 0), dim)
        println(bestAt[Pair(dim.first - 1, dim.second - 1)])
    }

    lateinit var bestAt: Array<IntArray>
    lateinit var arr: Array<IntArray>

    fun r(hpNeeded: Int, hpCurrent: Int, pos: Pair<Int, Int>, dim: Pair<Int, Int>) {
        val cNewHP = hpCurrent + arr[pos]
        val (iHPNeeded, aHP) = if(cNewHP <= 0) {
            Pair(hpNeeded + abs(cNewHP) + 1, 1)
        } else {
            Pair(hpNeeded, cNewHP)
        }
        if (iHPNeeded >= bestAt[pos]) {
            return
        }
        bestAt[pos] = iHPNeeded
        if(pos.first + 1 != dim.first) {
            r(iHPNeeded, aHP, Pair(pos.first + 1, pos.second), dim)
        }

        if(pos.second + 1 != dim.second) {
            r(iHPNeeded, aHP, Pair(pos.first, pos.second + 1), dim)
        }
    }
}

private operator fun Array<IntArray>.set(pos: Pair<Int, Int>, value: Int) {
    this[pos.first][pos.second] = value
}

private operator fun Array<IntArray>.get(pos: Pair<Int, Int>): Int {
    return this[pos.first][pos.second]
}
