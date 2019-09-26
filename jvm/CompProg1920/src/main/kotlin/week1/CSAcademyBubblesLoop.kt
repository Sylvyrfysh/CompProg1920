package week1

import npj.JVMProblemWrapper
import java.io.BufferedInputStream
import java.util.concurrent.CopyOnWriteArrayList

object CSAcademyBubblesLoop {
    @JvmStatic
    fun main(args: Array<String>) {
        JVMProblemWrapper.loopOverLambda(JVMProblemWrapper.Problem.CSAcademy.BubblesLoop, CSAcademyBubblesLoop::wrapper)
    }

    private fun wrapper() {
        val dis = System.`in`.bufferedReader()
        dis.readLine().toInt()
        val ins = dis.readLines().chunked(2)
        for(q in ins) {
            println(test(q))
        }
    }

    private fun test(l: List<String>): String {
        val (m, n) = l[0].split(" ").map(String::toInt)
        val verts = Array<CopyOnWriteArrayList<Int>>(m) { CopyOnWriteArrayList() }
        for((p1, p2) in l[1].split(" ").map(String::toInt).chunked(2)) {
            verts[p1].add(p2)
            verts[p2].add(p1)
        }

        var changed = true
        while(changed) {
            changed = false
            for ((index, v) in verts.withIndex()) {
                if (v.size == 0) {
                    continue
                }
                if (v.size == 1) {
                    changed = true
                    for (connects in v) {
                        verts[connects].remove(index)
                    }
                    v.clear()
                }
            }
        }
        return if(verts.filter(CopyOnWriteArrayList<out Any>::isNotEmpty).count() != 0) "1" else "0"
    }
}