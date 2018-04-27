package task1;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.function.Function;

import static java.lang.Math.*;

public class NewtonChebyshevInterpolator {

    private int m;
    private Function<Float, Float> f;
    private Function<Float, Float> nthDerivative;
    private float a;
    private float b;
    private float[][] table;
    private ArrayList<Float> nodeList;
    private NewtonPolynom polynom;
    private float bestMistake;

    public static void main(String[] args) {
        ArrayList<Integer> m = new ArrayList<>(Arrays.asList(4, 5, 6, 8, 9));
        HashMap<Integer, Function<Float, Float>> derivatives=new HashMap<>();
        derivatives.put(4, x->  (float) (-15*(64000*PI*PI*PI+14400*PI*PI*x+1680*PI*x*x+77*x*x*x)/(128*pow(5*PI+x, 17./4))));
        derivatives.put(5, x-> (float) (75*(160000*PI*PI*PI+24000*PI*PI*x+2100*PI*x*x+77*x*x*x)/(512*pow(5*PI+x, 21./4))));
        derivatives.put(6, x-> (float) (-675*(320000*PI*PI*PI+36000*PI*PI*x+2520*PI*x*x+77*x*x*x)/(2048*pow(5*PI+x, 25./4))));
        derivatives.put(8, x-> (float) (-1044225*(128000*PI*PI*PI+9600*PI*PI*x+480*PI*x*x+11*x*x*x)/(32768*pow(5*PI+x, 33./4))));
        derivatives.put(9, x-> (float) (3132675*(1344000*PI*PI*PI+86400*PI*PI*x+3780*PI*x*x+77*x*x*x)/(131072*pow(5*PI+x, 37./4))));
        m.forEach(M -> {
            try {
                NewtonChebyshevInterpolator interpolator = new NewtonChebyshevInterpolator(x -> (float) (10*x*x*x*Math.pow(Math.PI*5+x,-0.25)),
                        0, 10, M,
                        derivatives.get(M));
                interpolator.createOutput();
            } catch (IOException exc) {
                exc.printStackTrace();
            }
        });
    }

    public NewtonChebyshevInterpolator(Function<Float, Float> f,
                                       float a,
                                       float b,
                                       int m,
                                       Function<Float, Float> nthDerivative) {
        this.m = m;
        this.f = f;
        this.a = a;
        this.b = b;
        this.nthDerivative = nthDerivative;
        processData();
    }

    public static ArrayList<Float> chebyshevNodes(float a,
                                                  float b,
                                                  int m,
                                                  Function<Float, Float> f) {
        ArrayList<Float> list = new ArrayList<>(m);
        for (int i = 0; i < m; i++) {
            float result = (float) ((b + a) / 2 + (b - a) / 2 * cos(PI * (2 * i + 1) / (2 * m)));
            list.add(result);
        }
        Collections.reverse(list);
        return list;
    }

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

    private void processData() {
        nodeList = chebyshevNodes(a, b, m, f);
        table = table(nodeList, f);
        ArrayList<Float> coefficients = new ArrayList<>();
        for (int i = 0; i < m; i++) {
            coefficients.add(table[i][i]);
        }
        polynom = new NewtonPolynom(coefficients, nodeList);
        bestMistake = bestMistake(a, b, m, nthDerivative);
    }

    public void createOutput() throws IOException {
        PrintWriter writer = new PrintWriter("task1out/m" + m + ".txt");
        writer.println("m=" + m + ":");
        writer.println("x\tfunction\tpolynomial");
        nodeList.forEach(X -> {
            writer.printf("%.7f", X);
            writer.print("\t");
            writer.printf("%.7f", f.apply(X));
            writer.print("\t");
            writer.printf("%.7f", polynom.apply(X));
            writer.println();
        });
        writer.println("Best mistake: "+bestMistake);
        writer.close();
    }

    public static float bestMistake(float a,
                                    float b,
                                    int n,
                                    Function<Float, Float> nthDerivative) {
        float result = (float) (maxAbsValue(a, b, nthDerivative, 0.01f) * pow(b - a, n) * pow(2, 1 - 2 * n) / factorial(n));
        return result;
    }

    private static int factorial(int n) {
        int result = 1;
        int next = 2;
        while (next <= n) {
            result *= next;
            next += 1;
        }
        return result;
    }

    private static float maxAbsValue(float a,
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
}