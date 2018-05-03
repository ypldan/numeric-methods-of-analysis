package util;

import com.sun.istack.internal.NotNull;

import java.util.ArrayList;

/**
 * Class, that gives static methods to solve linear systems <b>Ax=b</b>, where <b>A</b> - <i>square matrix</i>, <b>x</b>, <b>b</b> - <i>columns</i>
 */
public class LinearSystemSolver {

    /**
     * Solves linear system <b>Ax=b</b> using householder method of QR-decomposition
     * @param squareMatrix matrix A
     * @param column column b
     * @return column x
     */
    public static ArrayList<Float> solveHouseholder(float[][] squareMatrix, float[] column) {
        int n = squareMatrix.length;
        float[][] matrix = new float[n][n];
        float[] b = new float[n];
        for (int i = 0; i < n; i++) {
            b[i] = column[i];
            System.arraycopy(squareMatrix[i], 0, matrix[i], 0, n);
        }
        float[] diagonalElements = new float[n];
        for (int k = 0; k < n; k++) {
            float norm = columnNorm(matrix, k, k);
            if (norm != 0f) {
                if (matrix[k][k] < 0) {
                    norm = -norm;
                }
                diagonalElements[k] = -norm;
                for (int i = k; i < n; i++) {
                    matrix[i][k] /= norm;
                }
                matrix[k][k] = matrix[k][k] + 1f;
                for (int j = k + 1; j < n; j++) {
                    float s = 0f;
                    for (int i = k; i < n; i++) {
                        s += matrix[i][k] * matrix[i][j];
                    }
                    s = -s / matrix[k][k];
                    for (int i = k; i < n; i++) {
                        matrix[i][j] += s * matrix[i][k];
                    }
                }
            }
        }
        for (int k = 0; k < n; k++) {
            double s = 0.0;
            for (int i = k; i < n; i++) {
                s += matrix[i][k] * b[i];
            }
            s = -s / matrix[k][k];
            for (int i = k; i < n; i++) {
                b[i] += s * matrix[i][k];
            }

        }
        for (int k = n - 1; k >= 0; k--) {
            b[k] /= diagonalElements[k];
            for (int i = 0; i < k; i++) {
                b[i] -= b[k] * matrix[i][k];
            }
        }
        ArrayList<Float> x = new ArrayList<>(n);
        for (float v : b) {
            x.add(v);
        }
        return x;
    }

    /**
     * Solves linear system <b>Ax=b</b> using gauss method of QR-decomposition
     * @param squareMatrix matrix A
     * @param column column b
     * @return column x
     */
    public static ArrayList<Float> solveGauss(@NotNull float[][] squareMatrix, @NotNull float[] column) {
        int n = squareMatrix.length;
        float[][] matrix = new float[n][n];
        float[] b = new float[n];
        for (int i = 0; i < n; i++) {
            b[i] = column[i];
            System.arraycopy(squareMatrix[i], 0, matrix[i], 0, n);
        }
        for (int j = 0; j < n; j++) {
            int index = indexMaxInCol(matrix, j);
            float[] temp = matrix[j];
            matrix[j] = matrix[index];
            matrix[index] = temp;
            float tempV = b[j];
            b[j] = b[index];
            b[index] = tempV;
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
        ArrayList<Float> x = new ArrayList<>(n);
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
            x.set(i, (y[i] - sum) / matrix[i][i]);
        }
        return x;
    }

    /**
     * Finds index of maximum element in column
     * @param matrix matrix
     * @param column column
     * @return index
     */
    private static int indexMaxInCol(@NotNull float[][] matrix, int column) {
        int result = column;
        for (int i = column + 1; i < matrix.length; ++i) {
            if (Math.abs(matrix[i][column]) > Math.abs(matrix[result][column])) {
                result = i;
            }
        }
        return result;
    }

    /**
     * Finds norm of column starting from kth row
     * @param matrix matrix
     * @param j column index
     * @param k raw index
     * @return numeric value of norm
     */
    private static float columnNorm(@NotNull float[][] matrix, int j, int k) {
        float result = 0;
        for (int i = k; i < matrix.length; i++) {
            result = hypot(result, matrix[i][j]);
        }
        return result;
    }

    /**
     * Function (a^2+b^2)^(1/2) without under/overflowing
     * @param a first argument
     * @param b second argument
     * @return numeric value of function
     */
    private static float hypot(float a, float b) {
        float result;
        if (Math.abs(a) > Math.abs(b)) {
            result = b / a;
            result = (float) (Math.abs(a) * Math.sqrt(1 + result * result));
        } else if (b != 0) {
            result = a / b;
            result = (float) (Math.abs(b) * Math.sqrt(1 + result * result));
        } else {
            result = 0f;
        }
        return result;
    }

}
