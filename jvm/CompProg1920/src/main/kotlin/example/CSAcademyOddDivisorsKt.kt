package example

import npj.JVMProblemWrapper
import npj.JVMProblemWrapper.loopOverLambda
import kotlin.collections.ArrayList

object CSAcademyOddDivisorsKt {
    @JvmStatic
    fun main(args: Array<String>) {
        loopOverLambda(JVMProblemWrapper.Problem.CSAcademy.OddDivisors) { input ->
            val pms = ArrayList<Int>().apply { addAll(Array(32) { it * it }) }
            var cnt = 0
            val (start, end) = input.split(" ").map(String::toInt)
            for(i in start..end) {
                if(pms.contains(i)) {
                    ++cnt
                }
            }
            return@loopOverLambda cnt.toString()
        }
    }
}