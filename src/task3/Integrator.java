package task3;

import com.sun.istack.internal.Nullable;
import util.CommonAnalysis;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.function.Function;

import static java.lang.Math.PI;
import static java.lang.Math.pow;

/**
 * Class, that gives static methods, that allow to perform <b><i>numerical integrating</i></b>.
 */
public class Integrator {

    public static void main(String[] args) throws IOException {
        Function<Float, Float> f = (x -> (float) (10 * x * x * x * Math.pow(Math.PI * 5 + x, -0.25))),
                secondDerivative = x -> (float) (5f * x * (2400f * PI * PI + 840f * PI * x + 77f * x * x) / (8f * pow(5f * PI + x, 9. / 4))),
                fourthDerivative = x -> (float) (-15 * (64000 * PI * PI * PI + 14400 * PI * PI * x + 1680 * PI * x * x + 77 * x * x * x) / (128 * pow(5 * PI + x, 17. / 4)));
        float eps1 = 5e-4f,
                eps2 = 5e-6f,
                a = 0f,
                b = 10f;
        PrintWriter writer = new PrintWriter("task3out/output.txt");
        writer.println("Middle rectangles:");
        writer.println();
        integrateMiddleRectangles(f, secondDerivative, a, b, eps1, writer);
        writer.println();
        integrateMiddleRectangles(f, secondDerivative, a, b, eps2, writer);
        writer.println();
        writer.println("Simpson:");
        writer.println();
        integrateSimpson(f, fourthDerivative, a, b, eps1, writer);
        writer.println();
        integrateSimpson(f, fourthDerivative, a, b, eps2, writer);
        writer.println();
        writer.close();
    }

    /**
     * gives the numerical value of integral on function in interval
     *
     * @param f                function
     * @param secondDerivative second derivative of <i><b>f</b></i>
     * @param a                start of interval
     * @param b                end of interval
     * @param eps              accuracy
     * @param writer           could be null, parameter to output additive information
     * @return numerical value of integral
     */
    public static float integrateMiddleRectangles(Function<Float, Float> f,
                                                  Function<Float, Float> secondDerivative,
                                                  float a,
                                                  float b,
                                                  float eps,
                                                  @Nullable PrintWriter writer) {
        int n = numberStepsRectangles(eps, secondDerivative, a, b);
        float i1 = 0,
                i2 = countMiddleRectangles(f, a, b, n);
        float delta = eps + 1f;
        while (Math.abs(delta) >= eps) {
            i1 = i2;
            n++;
            i2 = countMiddleRectangles(f, a, b, n);
            delta = (i2 - i1) / 3;
        }
        i2 = (4 * i2 - i1) / 3;
        if (writer != null) {
            writer.println("Mistake: " + eps);
            writer.println("Step: " + (b - a) / n);
            writer.println("Value: " + i2);
            writer.println("Score " + delta);
        }
        return i2;
    }

    /**
     * numerical value of integral using method of middle rectangles
     *
     * @param f function
     * @param a start of interval
     * @param b end of interval
     * @param n number of steps
     * @return numerical value of integral
     */
    private static float countMiddleRectangles(Function<Float, Float> f,
                                               float a,
                                               float b,
                                               int n) {
        float step = (b - a) / n;
        float result = 0;
        for (int i = 0; i < n; i++) {
            result += step * f.apply(a + (i + 0.5f) * step);
        }
        return result;
    }

    /**
     * gives the numerical value of integral on function in interval
     *
     * @param f                function
     * @param fourthDerivative fourth derivative of <i><b>f</b></i>
     * @param a                start of interval
     * @param b                end of interval
     * @param eps              accuracy
     * @param writer           could be null, parameter to output additive information
     * @return numerical value of integral
     */
    public static float integrateSimpson(Function<Float, Float> f,
                                         Function<Float, Float> fourthDerivative,
                                         float a,
                                         float b,
                                         float eps,
                                         @Nullable PrintWriter writer) {
        int n = numberStepsSimpson(eps, fourthDerivative, a, b);
        float i1 = 0, i2 = countSimpson(f, a, b, n);
        float delta = eps + 1;
        while (delta >= eps) {
            i1 = i2;
            n++;
            i2 = countSimpson(f, a, b, n);
            delta = Math.abs(i2 - i1) / 15;
        }
        i2 = (16 * i2 - i1) / 15;
        if (writer != null) {
            writer.println("Mistake: " + eps);
            writer.println("Step: " + (b - a) / (2 * n));
            writer.println("Value: " + i2);
            writer.println("Score " + delta);
        }
        return i2;
    }

    /**
     * numerical value of integral using Simpson's method
     *
     * @param f function
     * @param a start of interval
     * @param b end of interval
     * @param n number of steps
     * @return numerical value of integral
     */
    private static float countSimpson(Function<Float, Float> f,
                                      float a,
                                      float b,
                                      int n) {
        float step = (b - a) / (2f * n);
        float s1 = 0, s2 = 0;
        for (int i = 1; i < n; i++) {
            s1 += f.apply(a + i * 2f * step);
            s2 += f.apply(a + (2 * i - 1f) * step);
        }
        s2 += f.apply(a + (2 * n - 1f) * step);
        return step * (f.apply(a) + f.apply(b) + 2 * s1 + 4 * s2) / 3f;
    }

    /**
     * gives estimated number of steps
     *
     * @param eps              accuracy
     * @param fourthDerivative fourth derivative of function
     * @param a                start of interval
     * @param b                end of interval
     * @return estimated number of steps
     */
    private static int numberStepsSimpson(float eps,
                                          Function<Float, Float> fourthDerivative,
                                          float a,
                                          float b) {
        float h = (float) pow(eps * 288f / (b - a) / CommonAnalysis.maxAbsValue(a, b, fourthDerivative, 1e-3f), 1. / 3);
        double result = Math.ceil((b - a) / h);
        return (int) result;
    }

    /**
     * gives estimated number of steps
     *
     * @param eps              accuracy
     * @param secondDerivative second derivative of function
     * @param a                start of interval
     * @param b                end of interval
     * @return estimated number of steps
     */
    private static int numberStepsRectangles(float eps,
                                             Function<Float, Float> secondDerivative,
                                             float a,
                                             float b) {
        float h = (float) pow(eps * 24f / ((b - a) * CommonAnalysis.maxAbsValue(a, b, secondDerivative, 1e-3f)), 0.5);
        double result = Math.ceil((b - a) / h);
        return (int) result;
    }
}
