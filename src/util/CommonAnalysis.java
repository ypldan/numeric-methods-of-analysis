package util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.function.Function;

import static java.lang.Math.PI;
import static java.lang.Math.cos;
import static java.lang.Math.pow;

/**
 * Contains static common functions, connected with numerical analysis
 */
public class CommonAnalysis {

    /**
     * Table of divided differences of function <b><i>f</i></b> defined on points <b><i>x</i></b>
     * @param x list of points
     * @param f function
     * @return table of divided differences, (i,j)=<i>f(x<sub>(j-i)</sub> , ... ,x<sub>(j)</sub>)</i>
     */
    public static float[][] table(ArrayList<Float> x,
                                  Function<Float, Float> f) {
        float[][] result = new float[x.size()][x.size()];
        for (int i = 0; i < x.size(); i++) {
            result[0][i] = f.apply(x.get(i));
        }
        for (int i = 1; i < x.size(); i++) {
            for (int j = i; j < x.size(); j++) {
                result[i][j] = (result[i - 1][j] - result[i - 1][j - 1]) / (x.get(j) - x.get(j - i));
            }
        }
        return result;
    }

    /**
     * Mistake of interpolation in chebyshev nodes
     * @param a start of interval
     * @param b end of interval
     * @param n degree of polynom
     * @param nthDerivative nth derivative of function
     * @return numeric value of mistake
     */
    public static float bestMistake(float a,
                                    float b,
                                    int n,
                                    Function<Float, Float> nthDerivative) {
        return (float) (maxAbsValue(a, b, nthDerivative, 0.01f) * pow(b - a, n) * pow(2, 1 - 2 * n) / factorial(n));
    }

    /**
     * Factorial
     * @param n - argument, n>=0
     * @return numeric value of factorial
     */
    public static int factorial(int n) {
        int result = 1;
        int next = 2;
        while (next <= n) {
            result *= next;
            next += 1;
        }
        return result;
    }

    /**
     * Maximum absolute value of function on step with defined step
     * @param a start of step
     * @param b end of step
     * @param f function
     * @param step step of finding values of function
     * @return numeric value of maximum absolute value
     */
    public static float maxAbsValue(float a,
                                     float b,
                                     Function<Float, Float> f,
                                     float step) {
        float result = 0;
        for (float i = a; i <= b; i += step) {
            float newResult = Math.abs(f.apply(i));
            if (newResult > result) {
                result = newResult;
            }
        }
        return result;
    }

    /**
     * Mistakes of interpolating on uniform nodes in the middles of intervals
     * @param a start of interval
     * @param b end of interval
     * @param m number of points
     * @param nthDerivative nth derivative of function
     * @return list of numeric values of mistakes
     */
    public static ArrayList<Float> mistake(float a,
                                            float b,
                                            int m,
                                            Function<Float,Float> nthDerivative) {
        float h = (b - a) / (m - 1);
        ArrayList<Float> mistakes=new ArrayList<>();
        for (int i = 0; i < m - 1; i++) {
            float point = (float) ((b - a) * 0.5 * (2f * i + 1) / (m - 1f));
            float t = (point - a) / h;
            float mistake = maxAbsValue(a,b,nthDerivative,0.01f);
            for (int j = 0; j < m; j++) {
                mistake *= (t - j);
            }
            mistake = (float) (Math.abs(mistake) * pow(h, m) / factorial(m));
            mistakes.add(mistake);
        }
        return mistakes;

    }

    /**
     * Chebyshev nodes in interval
     * @param a start of interval
     * @param b end of interval
     * @param m number of points
     * @return list of chebyshev nodes
     */
    public static ArrayList<Float> chebyshevNodes(float a,
                                                  float b,
                                                  int m) {
        ArrayList<Float> list = new ArrayList<>(m);
        for (int i = 0; i < m; i++) {
            float result = (float) ((b + a) / 2 + (b - a) / 2 * cos(PI * (2 * i + 1) / (2 * m)));
            list.add(result);
        }
        Collections.reverse(list);
        return list;
    }

    /**
     * Uniform nodes in interval
     * @param a start of interval
     * @param b end of interval
     * @param n number of nodes
     * @return list of nodes
     */
    public static ArrayList<Float> uniformNodes(float a, float b, int n) {
        ArrayList<Float> list=new ArrayList<>(n);
        for (int i = 0; i <= n; i++) {
            list.add(a+(b-a)*i/n);
        }
        return list;
    }

    /**
     * Function values in nodes
     * @param nodes - nodes
     * @param f - function
     * @return list of values
     */
    public static ArrayList<Float> functionValues(ArrayList<Float> nodes, Function<Float,Float> f) {
        ArrayList<Float> list=new ArrayList<>(nodes.size());
        nodes.forEach(x -> list.add(f.apply(x)));
        return list;
    }
}
