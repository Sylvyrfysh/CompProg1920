package npj

import java.io.ByteArrayOutputStream
import java.io.PrintStream
import java.nio.file.Files
import java.nio.file.Paths
import kotlin.math.abs
import kotlin.streams.toList
import kotlin.system.measureNanoTime

object JVMProblemWrapper {
    private val baseResourcePath = Paths.get("..", "..", "resources")

    /**
     * This is the interface for the solve method. Each problem will give it a string, and it expects a String in return.
     */
    @FunctionalInterface
    interface SolveWrapper {
        /**
         *
         *
         */
        @Throws(Throwable::class)
        operator fun invoke()
    }

    @JvmStatic
    fun loopOverLambda(p: Problem, block: () -> Unit) = loopOver(p, object : SolveWrapper {
        override fun invoke() = block()
    })

    @JvmStatic
    fun loopOver(p: Problem, block: SolveWrapper) {
        val problemPath = baseResourcePath.resolve(p.createPaths().first).resolve(p.createPaths().second)

        val files = Files.list(problemPath)
            .map { it.fileName.toString() }
            .filter { it.substring(it.indexOf('.') + 1) == "in" }
            .map { val p2 = it.lastIndexOf('.'); it.substring(0, p2) }
            .toList()
            .sortedBy { it.toIntOrNull() ?: 1000000 + it.hashCode() }

        val padLen = files.maxBy { it.length }!!.length
        var passed = 0
        val buffer = ByteArrayOutputStream()
        val oldSysOut = System.out
        for(i in files) {
            System.setOut(PrintStream(buffer))
            buffer.reset()
            val istr = i.padStart(padLen, '0')
            val inPath = problemPath.resolve("$i.in")
            val problemTxt = Files.readString(inPath).trim()
            val problemAns = Files.readAllLines(problemPath.resolve("$i.ans")).map(String::trim)
            val receivedAns: MutableList<String> = ArrayList()
            System.setIn(problemTxt.byteInputStream())
            val nanosFor = measureNanoTime {
                block()
            }
            receivedAns.addAll(buffer.toString().split("\n").map(String::trim).filter(String::isNotEmpty))
            if(p.checkFunction(problemAns, receivedAns)) {
                ++passed
            }else {
                System.setOut(oldSysOut)
                println("""
                    |============
                    |$istr: FAIL!!! Expected
                    |$problemAns
                    |---
                    |Got
                    |---
                    |$receivedAns
                    |============""".trimMargin())
                System.setOut(PrintStream(buffer))
            }
        }
        System.setOut(oldSysOut)
        println("$p: PASS $passed / ${files.size}")
    }

    sealed class Problem(private val iCheckFunction: ((List<String>, List<String>) -> Boolean)? = null) {
        private object DefaultCheckFunction: (List<String>, List<String>) -> Boolean {
            override fun invoke(p1: List<String>, p2: List<String>): Boolean = p1 == p2
        }
        val checkFunction: (List<String>, List<String>) -> Boolean
            get() = iCheckFunction ?: DefaultCheckFunction
        abstract fun createPaths(): Pair<String, String>
        abstract fun printHelp()

        override fun toString(): String {
            return createPaths().first + "." + createPaths().second
        }

        class NCNA18 private constructor(private val letter: String, iCheckFunction: ((List<String>, List<String>) -> Boolean)? = null): Problem(iCheckFunction) {
            override fun createPaths(): Pair<String, String> {
                return Pair("NCNA2018", "Problem$letter")
            }

            override fun printHelp() {
                println("""
                    The NCNA 18 problem packet can be found at resources/NCNA2018/ProblemListing.pdf
                """.trimIndent())
            }

            companion object {
                @JvmField
                val ProblemA = NCNA18("A")
                @JvmField
                val ProblemB = NCNA18("B")
                @JvmField
                val ProblemC = NCNA18("C")
                @JvmField
                val ProblemD = NCNA18("D")
                @JvmField
                val ProblemE = NCNA18("E")
                @JvmField
                val ProblemF = NCNA18("F")
                @JvmField
                val ProblemG = NCNA18("G")
                @JvmField
                val ProblemH = NCNA18("H")
                @JvmField
                val ProblemI = NCNA18("I")
                @JvmField
                val ProblemJ = NCNA18("J")
            }
        }

        class NCNA17 private constructor(private val letter: String, iCheckFunction: ((List<String>, List<String>) -> Boolean)? = null): Problem(iCheckFunction) {
            override fun createPaths(): Pair<String, String> {
                return Pair("NCNA2017", "Problem$letter")
            }

            override fun printHelp() {
                println("""
                    The NCNA 17 problem packet can be found at resources/NCNA2017/ProblemListing.pdf
                """.trimIndent())
            }

            companion object {
                @JvmField
                val ProblemA = NCNA17("A")
                @JvmField
                val ProblemB = NCNA17("B")
                @JvmField
                val ProblemC = NCNA17("C")
                @JvmField
                val ProblemD = NCNA17("D")
                @JvmField
                val ProblemE = NCNA17("E")
                @JvmField
                val ProblemF = NCNA17("F")
                @JvmField
                val ProblemG = NCNA17("G")
                @JvmField
                val ProblemH = NCNA17("H")
                @JvmField
                val ProblemI = NCNA17("I") { one, two ->
                    val m1 = one.filter(String::isNotEmpty).map(String::toDouble)
                    val m2 = two.filter(String::isNotEmpty).map(String::toDouble)

                    if(m1.size != m2.size) {
                        return@NCNA17 false
                    }

                    for(i in m1.indices) {
                        if(abs(m1[i] - m2[i]) > 10e-6) {
                            return@NCNA17 false
                        }
                    }

                    return@NCNA17 true
                }
                @JvmField
                val ProblemJ = NCNA17("J")
            }
        }

        class CSAcademy private constructor(private val problemName: String, private val url: String, iCheckFunction: ((List<String>, List<String>) -> Boolean)? = null): Problem(iCheckFunction) {
            override fun printHelp() {
                println("""
                    This problem is at the URL $url
                    If the page does not properly load,, click on a link on the page and then go back in your browser.
                """.trimIndent())
            }

            override fun createPaths(): Pair<String, String> {
                return Pair("CSAcademy", problemName)
            }

            companion object {
                @JvmField
                val BubblesLoop = CSAcademy("BubblesLoop", "https://csacademy.com/ieeextreme-practice/task/979a09a0cd8c4e98dd0a690f39a55bd2/")
                @JvmField
                val OddDivisors = CSAcademy("OddDivisors", "https://csacademy.com/contest/archive/task/odd-divisor-count/")
            }
        }
    }
}