package npj

import java.nio.file.Files
import java.nio.file.Paths
import kotlin.streams.toList

object JVMProblemWrapper {
    private val baseResourcePath = Paths.get("src", "main", "resources")

    @FunctionalInterface
    interface SolveWrapper {
        @Throws(Throwable::class)
        operator fun invoke(input: String): String
    }

    @JvmStatic
    fun loopOverLambda(p: Problem, block: (String) -> String) = loopOver(p, object : SolveWrapper {
        override fun invoke(input: String) = block(input)
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
        for(i in files) {
            val istr = i.padStart(padLen, '0')
            val inPath = problemPath.resolve("$i.in")
            val problemTxt = Files.readString(inPath).trim()
            val problemAns = Files.readAllLines(problemPath.resolve("$i.ans")).map(String::trim)
            val receivedAns = block(problemTxt).trim().split("\n").map(String::trim)
            if(receivedAns == problemAns) {
                ++passed
            }else {
                println("""
                    |============
                    |$istr: FAIL!!! Expected
                    |$problemAns
                    |---
                    |Got
                    |---
                    |$receivedAns
                    |============""".trimMargin())
            }
        }
        println("$p: PASS $passed / ${files.size}")
    }

    sealed class Problem {
        abstract fun createPaths(): Pair<String, String>
        abstract fun printHelp()

        override fun toString(): String {
            return createPaths().first + "." + createPaths().second
        }

        class NCNA18 private constructor(private val letter: String): Problem() {
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

        class NCNA17 private constructor(private val letter: String): Problem() {
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
                val ProblemI = NCNA17("I")
                @JvmField
                val ProblemJ = NCNA17("J")
            }
        }

        class CSAcademy private constructor(private val problemName: String, private val url: String): Problem() {
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