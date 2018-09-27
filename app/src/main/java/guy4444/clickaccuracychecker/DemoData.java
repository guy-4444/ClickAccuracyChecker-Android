package guy4444.clickaccuracychecker;

import java.util.ArrayList;
import java.util.Random;

public class DemoData {

    public static int[][] getData() {
        int[][] diffVals = new int[2][50];
        Random random = new Random();
        for (int i = 0; i < diffVals.length; i++) {
            for (int j = 0; j < diffVals[0].length; j++) {
                diffVals[i][j] = random.nextInt(10) - 5;
            }
        }
//        diffVals[0][0] = 1;
//        diffVals[1][0] = 2;
//        diffVals[0][1] = -3;
//        diffVals[1][1] = 5;
//        diffVals[0][2] = 2;
//        diffVals[1][2] = 0;
//        diffVals[0][3] = 6;
//        diffVals[1][3] = -2;
//        diffVals[0][4] = 3;
//        diffVals[1][4] = -5;

        return diffVals;
    }
}
