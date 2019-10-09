package week2;

import npj.JVMProblemWrapper;

import java.util.Arrays;
import java.util.Scanner;

public class ViridianForest {
    public static void main(String[] args) {
        JVMProblemWrapper.Problem.CSAcademy.ViridianForest.printHelp();
        JVMProblemWrapper.loopOver(JVMProblemWrapper.Problem.CSAcademy.ViridianForest, ViridianForest::wrapper);
    }

    private static void wrapper() {
        Scanner scanner = new Scanner(System.in);
        String[] p1 = scanner.nextLine().split(" ");
        Pair<Integer, Integer> dim = new Pair<>(Integer.parseInt(p1[0]), Integer.parseInt(p1[1]));
        bestAt = new int[dim.first][dim.second];
        arr = new int[dim.first][dim.second];
        for(int x = 0; x < dim.first; ++x) {
            int[] parts = Arrays.stream(scanner.nextLine().split(" ")).mapToInt(Integer::parseInt).toArray();
            for(int y = 0; y < dim.second; ++y) {
                bestAt[x][y] = Integer.MAX_VALUE;
                arr[x][y] = parts[y];
            }
        }
        r(1, 1, new Pair<>(0, 0), dim);
        System.out.println(bestAt[dim.first - 1][dim.second - 1]);
    }

    private static void r(int hpNeeded, int hpCurrent, Pair<Integer, Integer> pos, Pair<Integer, Integer> dim) {
        int cNewHP = hpCurrent + arr[pos.first][pos.second];
        int iHPNeeded, aHP;
        if(cNewHP <= 0) {
            iHPNeeded = hpNeeded + Math.abs(cNewHP) + 1;
            aHP = 1;
        } else {
            iHPNeeded = hpNeeded;
            aHP = cNewHP;
        }

        if (iHPNeeded >= bestAt[pos.first][pos.second]) {
            return;
        }

        bestAt[pos.first][pos.second] = iHPNeeded;
        if(pos.first + 1 != dim.first) {
            r(iHPNeeded, aHP, new Pair<>(pos.first + 1, pos.second), dim);
        }

        if(pos.second + 1 != dim.second) {
            r(iHPNeeded, aHP, new Pair<>(pos.first, pos.second + 1), dim);
        }
    }

    static int[][] arr;
    static int[][] bestAt;

    static class Pair<A, B> {
        public Pair(A first, B second) {
            this.first = first;
            this.second = second;
        }

        A first;
        B second;
    }
}
