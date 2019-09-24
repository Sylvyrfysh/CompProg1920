package example

import npj.JVMProblemWrapper
import npj.JVMProblemWrapper.loopOverLambda
import kotlin.collections.ArrayList

object CSAcademyOddDivisorsKt {
    @JvmStatic
    fun main(args: Array<String>) {
        //loopOverLambda takes the first argument of type Problem. You can browse all supported problems by using autocompletion on JVMProblemWrapper.Problem
        //The second argument is a lambda that takes a String input and returns a String output.
        //This could also be called like loopOverLambda(JVMProblemWrapper.Problem.CSAcademy.OddDivisors, ::myMethodThatTakesAndReturnsAString)
        loopOverLambda(JVMProblemWrapper.Problem.CSAcademy.OddDivisors) { input -> //This lambda will be run for every test case. You just return your result.
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