package task3;

import com.sun.istack.internal.Nullable;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.function.Function;

/**
 * Class, that gives static methods, that allow to perform <b><i>numerical integrating</i></b>.
 */
public class Integrator {

    public static void main(String[] args) throws IOException {
        Function<Float, Float> f = (x -> (float) (10 * x * x * x * Math.pow(Math.PI * 5 + x, -0.25)));
        float eps1 = 5e-4f,
                eps2 = 5e-6f,
                a=0f,
                b=10f;
        PrintWriter writer=new PrintWriter("task3out/output.txt");
        writer.println("Middle rectangles:");
        writer.println();
        integrateMiddleRectangles(f,a,b,eps1,writer);
        writer.println();
        integrateMiddleRectangles(f,a,b,eps2,writer);
        writer.println();
        writer.println("Simpson:");
        writer.println();
        integrateSimpson(f,a,b,eps1,writer);
        writer.println();
        integrateSimpson(f,a,b,eps2,writer);
        writer.println();
        writer.close();
    }

    /**
     * gives the numerical value of integral on function in interval
     * @param f function
     * @param a start of interval
     * @param b end of interval
     * @param eps accuracy
     * @param writer could be null, parameter to output additive information
     * @return numerical value of integral
     */
    public static float integrateMiddleRectangles(Function<Float, Float> f,
                                                  float a,
                                                  float b,
                                                  float eps,
                                                  @Nullable PrintWriter writer) {
        int n = 1;
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
     * @param f function
     * @param a start of interval
     * @param b end of interval
     * @param eps accuracy
     * @param writer could be null, parameter to output additive information
     * @return numerical value of integral
     */
    public static float integrateSimpson(Function<Float, Float> f,
                                         float a,
                                         float b,
                                         float eps,
                                         @Nullable PrintWriter writer) {
        int n = 1;
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
            writer.println("Step: " + (b - a) / (2*n));
            writer.println("Value: " + i2);
            writer.println("Score " + delta);
        }
        return i2;
    }

    /**
     * numerical value of integral using Simpson's method
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
        float step = (b - a) / (2f*n);
        float s1=0, s2=0;
        for (int i = 1; i < n; i++) {
            s1+=f.apply(a+i*2f*step);
            s2+=f.apply(a+(2*i-1f)*step);
        }
        s2+=f.apply(a+(2*n-1f)*step);
        return step * (f.apply(a) + f.apply(b) + 2 * s1 + 4 * s2) / 3f;
    }
}
