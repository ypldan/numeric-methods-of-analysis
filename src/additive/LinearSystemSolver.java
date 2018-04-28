package additive;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class LinearSystemSolver {

    public static ArrayList<Float> solveGauss(float[][] squareMatrix, float[] column) {
        int n=squareMatrix.length;
        float[][] matrix=new float[n][n];
        float[] b=new float[n];
        for (int i = 0; i < n; i++) {
            b[i]=column[i];
            System.arraycopy(squareMatrix[i], 0, matrix[i], 0, n);
        }
        for (int j = 0; j < n; j++) {
            int index = indexMaxInCol(matrix,j);
            float[] temp=matrix[j];
            matrix[j]=matrix[index];
            matrix[index]=temp;
            float tempV=b[j];
            b[j]=b[index];
            b[index]=tempV;
            for (int i = j + 1; i < matrix.length; i++) {
                matrix[i][j] = matrix[i][j] / matrix[j][j];
            }
            for (int i = j + 1; i < matrix.length; i++) {
                for (int k = j + 1; k < matrix.length; k++) {
                    matrix[i][k] = matrix[i][k] - matrix[i][j] * matrix[j][k];
                }
            }
        }
        float[] y = new float[n];
        ArrayList<Float> x=new ArrayList<>(n);
        for (int i = 0; i < n; i++) {
            x.add(0f);
        }
        for (int i = 0; i < n; ++i) {
            float sum = 0;
            for (int j = 0; j < i; ++j) {
                sum += matrix[i][j] * y[j];
            }
            y[i] = b[i] - sum;
        }
        for (int i = n - 1; i >= 0; --i) {
            float sum = 0;
            for (int j = i + 1; j < n; ++j) {
                sum += matrix[i][j] * x.get(j);
            }
            x.set(i,(y[i] - sum) / matrix[i][i]);
        }
        return x;
    }

    private static int indexMaxInCol(float[][] matrix, int column) {
        int result = column;
        for (int i = column + 1; i < matrix.length; ++i) {
            if (Math.abs(matrix[i][column]) > Math.abs(matrix[result][column])) {
                result = i;
            }
        }
        return result;
    }

}
