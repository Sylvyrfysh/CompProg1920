package week1;

import npj.JVMProblemWrapper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class CSAcademyBubblesLoopJava {
    //loopOver takes the first argument of type Problem. You can browse all supported problems by using autocompletion on JVMProblemWrapper.Problem
    //The second argument is a method, or class that extends SolveWrapper, that takes a String input and returns a String output.
    public static void main(String[] args) {
        JVMProblemWrapper.loopOver(JVMProblemWrapper.Problem.CSAcademy.BubblesLoop, CSAcademyBubblesLoopJava::wrapper);
    }

    private static void wrapper() {
        Scanner in = new Scanner(System.in);
        int numTestCases = in.nextInt();
        in.nextLine();

        for(int i = 0; i < numTestCases; ++i) {
            String testCaseResult = testOneCase(in.nextLine(), in.nextLine());
            System.out.println(testCaseResult);
        }
    }

    private static String testOneCase(String firstLine, String secondLine) {
        Scanner firstLineScanner = new Scanner(firstLine);
        int numVertices = firstLineScanner.nextInt();
        int numEdges = firstLineScanner.nextInt();
        Map<Integer, ArrayList<Integer>> vertices = new HashMap<>();
        for(int i = 0; i < numVertices; ++i) {
            vertices.put(i, new ArrayList<>());
        }

        Scanner secondLineScanner = new Scanner(secondLine);

        for (int i = 0; i < numEdges; ++i) {
            int vert1 = secondLineScanner.nextInt();
            int vert2 = secondLineScanner.nextInt();
            vertices.get(vert1).add(vert2);
            vertices.get(vert2).add(vert1);
        }

        boolean changed = true;

        while(changed) {
            changed = false;
            for (int v = 0; v < numVertices; ++v) {
                if (vertices.get(v).size() == 1) {
                    changed = true;
                    int connectedTo = vertices.get(v).get(0);
                    vertices.get(connectedTo).remove((Integer) v);
                    vertices.get(v).clear();
                }
            }
        }

        for(int v = 0; v < numVertices; ++v) {
            if(vertices.get(v).size() != 0) {
                return "1";
            }
        }

        return "0";
    }
}
