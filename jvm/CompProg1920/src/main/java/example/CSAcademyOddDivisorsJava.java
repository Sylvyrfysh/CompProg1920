package example;

import npj.JVMProblemWrapper;

import java.util.ArrayList;
import java.util.Scanner;

public class CSAcademyOddDivisorsJava {
    //loopOver takes the first argument of type Problem. You can browse all supported problems by using autocompletion on JVMProblemWrapper.Problem
    //The second argument is a method, or class that extends SolveWrapper, that takes a String input and returns a String output.
    public static void main(String[] args) {
        JVMProblemWrapper.loopOver(JVMProblemWrapper.Problem.CSAcademy.OddDivisors, CSAcademyOddDivisorsJava::wrapper);
    }

    private static String wrapper(String input) {
        final ArrayList<Integer> squares = new ArrayList<>();
        for(int i = 0; i < 32; ++i) {
            squares.add(i * i);
        }
        final Scanner scanner = new Scanner(input);
        final int lowBound = scanner.nextInt();
        final int highBoundInclusive = scanner.nextInt();
        int countOddDivisors = 0;
        for(int i = lowBound; i <= highBoundInclusive; ++i) {
            if(squares.contains(i)) {
                ++countOddDivisors;
            }
        }
        return Integer.toString(countOddDivisors);
    }
}
