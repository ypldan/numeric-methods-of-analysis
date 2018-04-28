package additive;

import java.util.ArrayList;
import java.util.Collections;
import java.util.function.Function;

import static java.lang.Math.PI;
import static java.lang.Math.cos;
import static java.lang.Math.pow;

public class CommonAnalysis {
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

    public static float bestMistake(float a,
                                    float b,
                                    int n,
                                    Function<Float, Float> nthDerivative) {
        return (float) (maxAbsValue(a, b, nthDerivative, 0.01f) * pow(b - a, n) * pow(2, 1 - 2 * n) / factorial(n));
    }

    public static int factorial(int n) {
        int result = 1;
        int next = 2;
        while (next <= n) {
            result *= next;
            next += 1;
        }
        return result;
    }

    public static float maxAbsValue(float a,
                                     float b,
                                     Function<Float, Float> f,
                                     float interval) {
        float result = 0;
        for (float i = a; i <= b; i += interval) {
            float newResult = Math.abs(f.apply(i));
            if (newResult > result) {
                result = newResult;
            }
        }
        return result;
    }

    public static ArrayList<Float> mistake(float a,
                                            float b,
                                            int m,
                                            Function<Float,Float> nthDerivative) {
        float h = (b - a) / (m - 1);
        ArrayList<Float> mistakes=new ArrayList<>();
        for (int i = 0; i < m - 1; i++) {
            float point = (float) ((b - a) * 0.5 * (2 * (float)i + 1) / ((float)m - 1));
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

    public static ArrayList<Float> ordinaryNodes(float a, float b, int n) {
        ArrayList<Float> list=new ArrayList<>(n+1);
        for (int i = 0; i <= 30; i++) {
            list.add(a+(b-a)*i/n);
        }
        return list;
    }

    public static ArrayList<Float> functionValues(ArrayList<Float> nodes, Function<Float,Float> f) {
        ArrayList<Float> list=new ArrayList<>(nodes.size());
        nodes.forEach(x -> list.add(f.apply(x)));
        return list;
    }
}
