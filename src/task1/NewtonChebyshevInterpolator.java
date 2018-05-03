package task1;

import util.CommonAnalysis;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.function.Function;

import static java.lang.Math.PI;
import static java.lang.Math.pow;

/**
 * Class, that interpolates input function by Newton polynom
 */
public class NewtonChebyshevInterpolator {

    /**
     * number of points
     */
    private int m;
    /**
     * function
     */
    private Function<Float, Float> f;
    /**
     * nth derivative of function <i><b>f</b></i>
     */
    private Function<Float, Float> nthDerivative;
    /**
     * start of interval
     */
    private float a;
    /**
     * end of interval
     */
    private float b;
    /**
     * list of chebyshev nodes
     */
    private ArrayList<Float> nodeList;
    /**
     * Newton polynomial function
     */
    private NewtonPolynom polynom;
    /**
     * mistake of interpolating in chebyshev nodes
     */
    private float bestMistake;
    /**
     * mistakes of interpolating function in uniform nodes in the middles of intervals
     */
    private ArrayList<Float> mistakes;

    public static void main(String[] args) {
        ArrayList<Integer> m = new ArrayList<>(Arrays.asList(4, 5, 6, 8, 9));
        HashMap<Integer, Function<Float, Float>> derivatives = new HashMap<>();
        derivatives.put(4, x -> (float) (-15 * (64000 * PI * PI * PI + 14400 * PI * PI * x + 1680 * PI * x * x + 77 * x * x * x) / (128 * pow(5 * PI + x, 17. / 4))));
        derivatives.put(5, x -> (float) (75 * (160000 * PI * PI * PI + 24000 * PI * PI * x + 2100 * PI * x * x + 77 * x * x * x) / (512 * pow(5 * PI + x, 21. / 4))));
        derivatives.put(6, x -> (float) (-675 * (320000 * PI * PI * PI + 36000 * PI * PI * x + 2520 * PI * x * x + 77 * x * x * x) / (2048 * pow(5 * PI + x, 25. / 4))));
        derivatives.put(8, x -> (float) (-1044225 * (128000 * PI * PI * PI + 9600 * PI * PI * x + 480 * PI * x * x + 11 * x * x * x) / (32768 * pow(5 * PI + x, 33. / 4))));
        derivatives.put(9, x -> (float) (3132675 * (1344000 * PI * PI * PI + 86400 * PI * PI * x + 3780 * PI * x * x + 77 * x * x * x) / (131072 * pow(5 * PI + x, 37. / 4))));
        m.forEach(M -> {
            try {
                NewtonChebyshevInterpolator interpolator = new NewtonChebyshevInterpolator(x -> (float) (10 * x * x * x * Math.pow(Math.PI * 5 + x, -0.25)),
                        0, 10, M,
                        derivatives.get(M));
                interpolator.createOutput();
            } catch (IOException exc) {
                exc.printStackTrace();
            }
        });
    }

    /**
     * Constructs the interpolator class, processes data
     * @param f function
     * @param a start of interval
     * @param b end of interval
     * @param m number of points
     * @param nthDerivative nth derivative of function
     */
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

    /**
     * Processes input data
     */
    private void processData() {
        nodeList = CommonAnalysis.chebyshevNodes(a, b, m);
        float[][] table = CommonAnalysis.table(nodeList, f);
        ArrayList<Float> coefficients = new ArrayList<>();
        for (int i = 0; i < m; i++) {
            coefficients.add(table[i][i]);
        }
        polynom = new NewtonPolynom(coefficients, nodeList);
        bestMistake = CommonAnalysis.bestMistake(a, b, m, nthDerivative);
        mistakes = CommonAnalysis.mistake(a, b, m, nthDerivative);
    }

    /**
     * Creates output
     * @throws IOException
     */
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
        writer.println("Best mistake: " + bestMistake);
        writer.print("Mistakes: ");
        mistakes.forEach(x -> writer.print(x + " "));
        writer.close();
    }

}