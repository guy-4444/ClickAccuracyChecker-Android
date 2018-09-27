package guy4444.clickaccuracychecker;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.AsyncTask;
import android.util.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


public class TugSway {


    private final int DIM = 2;
    public double xmax = Double.MIN_VALUE;
    public double ymax = Double.MIN_VALUE;
    public double xmin = Double.MAX_VALUE;
    public double ymin = Double.MAX_VALUE;

    private double RANGE_MIN_X = -1.0;
    private double RANGE_MAX_X = 1.0;
    private double RANGE_MIN_Y = -1.0;
    private double RANGE_MAX_Y = 1.0;
    private boolean IS_CONSTANT_SCALE = false;

    public static double EXTRA_RANGE = 0.00;

    public Bitmap getSwayBitmap(int SIZE_WIDTH, int SIZE_HEIGHT, int[] xsf, int[] ysf) {
        int newSize = Math.min(xsf.length, ysf.length);
        double[] newXs = new double[newSize];
        double[] newYs = new double[newSize];
        for (int i = 0; i < newSize; i++) {
            newXs[i] = xsf[i];
            newYs[i] = ysf[i];
        }

        Bitmap bitmap = generateSwayBitmap(SIZE_WIDTH , SIZE_HEIGHT, newXs, newYs);

        return bitmap;
    }

    private Bitmap generateSwayBitmap(int SIZE_WIDTH, int SIZE_HEIGHT, double[] xs, double[] ys) {
        double[][] zArray = getConstantRangeMatrix(SIZE_WIDTH, SIZE_HEIGHT, xs, ys);

        double zmax = Double.MIN_VALUE;
        double zmin = Double.MAX_VALUE;
        double z;
        for (int i = 0; i < zArray.length; i++) {
            for (int j = 0; j < zArray[0].length; j++) {
                z = zArray[i][j];
                zmax = Math.max(z, zmax);
                zmin = Math.min(z, zmin);
            }
        }

        Log.d("pttt", "D");
        Bitmap bitmap = getBitmap(zArray, zmax);
        Log.d("pttt", "E");

        return bitmap;
    }

    private double[][] getConstantRangeMatrix(int SIZE_WIDTH, int SIZE_HEIGHT, double[] xs, double[] ys) {
        int[] xArray = new int[xs.length];
        int[] yArray = new int[ys.length];
        double val;
        double[][] zArray = new double[SIZE_WIDTH][SIZE_HEIGHT];
        for (int x = 0; x < zArray.length; x++) {
            for (int y = 0; y < zArray[0].length; y++) {
                zArray[x][y] = 0;
            }
        }


        if (IS_CONSTANT_SCALE) {
            xmin = RANGE_MIN_X;
            xmax = RANGE_MAX_X;
            ymin = RANGE_MIN_Y;
            ymax = RANGE_MAX_Y;
        }
        else {
            for (int i = 0; i < xs.length; i++) {
                val = xs[i];
                xmax = Math.max(val, xmax);
                xmin = Math.min(val, xmin);
            }
            for (int i = 0; i < ys.length; i++) {
                val = ys[i];
                ymax = Math.max(val, ymax);
                ymin = Math.min(val, ymin);
            }
        }


        double xMult = ((SIZE_WIDTH - 1) * 1.0) / (xmax - xmin);
        double yMult = ((SIZE_HEIGHT - 1) * 1.0) / (ymax - ymin);

        for (int i = 0; i < xs.length; i++) {
            xArray[i] = (int) ((xs[i] - xmin) * xMult);
        }
        for (int i = 0; i < ys.length; i++) {
            yArray[i] = (int) ((ys[i] - ymin) * yMult);
        }


        Log.d("pttt", "A");
        int xa;
        int ya;

        for (int i = 0; i < xArray.length; i++) {
            xa = xArray[i];
            ya = yArray[i];

            int range = 3;
            for (int r = 0; r <= range; r++) {
                double multVal = (1.0/(1.0+(r*1.0)));
                for (int x = -r; x <= r; x++) {
                    for (int y = -r; y <= r; y++) {
                        try {
                            double distance = Math.sqrt(Math.pow(xs[i], 2.0) + Math.pow(ys[i], 2.0));
                            zArray[xa + x][ya + y] += multVal * 1;
                        } catch (IndexOutOfBoundsException ex) {
                            // after RANGE
                        }
                    }
                }
            }
        }
        Log.d("pttt", "B");



        double[][] zArrayN = new double[zArray.length][zArray[0].length];
        double sum = 0;
        int range = 6;

        for (int x = 0; x < zArrayN.length; x++) {
            for (int y = 0; y < zArrayN[0].length; y++) {
                sum = 0;
                for (int i = -range; i <= range; i++) {
                    for (int j = -range; j <= range; j++) {
                        try {
                            sum += zArray[x + i][y + j];
                        } catch (IndexOutOfBoundsException ex) { }
                    }
                }
                zArrayN[x][y] = sum;
            }
        }
        Log.d("pttt", "C");

        return zArrayN;
    }

    private Bitmap getBitmap(double[][] zArray, double zmax) {
        Log.d("ptttSL", "E1.3.3.1");

        double rng = 200.0 / zmax;
        double arng = 1.0 / zmax;
        double vrng = (0.8 / zmax);
        int height = zArray[0].length;
        int width = zArray.length;
        int column = 0;
        int row = 0;
        Bitmap image = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        double val = 0;
        Log.d("ptttSL", "E1.3.3.2");

        while (row < height) {
            while (column < width) {
                val = zArray[column][row];
                int alpha = (int) (Math.pow(arng * val, 1.0 / 1.4) * 235.0) + 0;
                //image.setPixel(column, row, Color.HSVToColor(new float[]{(float) (180.0 - (rng*val)), 1.0f, 1.0f}));
                //image.setPixel(column, row, Color.HSVToColor((int) (arng * val) + 220, new float[]{(float) (260.0 - rng*val), 1.0f, (float) (0.6f + (0.4f-vrng*val))}));
                //image.setPixel(column, row, Color.HSVToColor((int) (Math.pow(arng*val, 1.3)*245.0) + 10, new float[]{(float) (260.0 - rng*val), 1.0f, 1.0f}));

                image.setPixel(column, height-row-1, Color.HSVToColor(alpha, new float[]{(float) (205.0 - rng * val), 1.0f, 1.0f}));

                //image.setPixel(column, row, Color.HSVToColor((int) (Math.pow(arng * val, 1.4) * 235.0) + 20, new float[]{(float) (200.0 - rng * val), 1.0f, 1.0f}));
                column++;
            }
            row++;
            column = 0;
        }
        Log.d("ptttSL", "E1.3.3.3");

        return image;
    }



    public void setRange(double minRangeX, double maxRangeX, double minRangeY, double maxRangeY) {
        this.RANGE_MIN_X = minRangeX;
        this.RANGE_MAX_X = maxRangeX;
        this.RANGE_MIN_Y = minRangeY;
        this.RANGE_MAX_Y = maxRangeY;
    }

    public void setIsConstantScale (boolean isConstantScale) {
        this.IS_CONSTANT_SCALE = isConstantScale;
    }





    private static class MatrixAreaSumCalculator extends AsyncTask<String, Void, Double> {

        CallBack_GenericDoubleReturn callBack_genericDoubleReturn;

        public MatrixAreaSumCalculator(CallBack_GenericDoubleReturn callBack_genericDoubleReturn) {
            this.callBack_genericDoubleReturn = callBack_genericDoubleReturn;
        }

        @Override
        protected Double doInBackground(String... params) {

            return 0.0;
        }

        @Override
        protected void onPostExecute(Double result) {
            callBack_genericDoubleReturn.double_returned(result);
        }

        @Override
        protected void onPreExecute() {}

        @Override
        protected void onProgressUpdate(Void... values) {}
    }
}
