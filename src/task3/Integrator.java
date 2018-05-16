package task3;

import com.sun.istack.internal.Nullable;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.function.Function;

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
        integrateSimpson(f,a,b,eps1,writer);
        writer.println();
        integrateSimpson(f,a,b,eps2,writer);
        writer.println();
        writer.close();
    }

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
            n *= 2;
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

    public static float integrateSimpson(Function<Float, Float> f,
                                         float a,
                                         float b,
                                         float eps,
                                         @Nullable PrintWriter writer) {
        int n = 2;
        float i1 = 0, i2 = countSimpson(f, a, b, n);
        float delta = eps + 1;
        while (delta >= eps) {
            i1 = i2;
            n *= 2;
            i2 = countSimpson(f, a, b, n);
            delta = Math.abs(i2 - i1) / 15;
        }
        i2 = (16 * i2 - i1) / 15;
        if (writer != null) {
            writer.println("Mistake: " + eps);
            writer.println("Step: " + (b - a) / n);
            writer.println("Value: " + i2);
            writer.println("Score " + delta);
        }
        return i2;
    }

    private static float countSimpson(Function<Float, Float> f,
                                      float a,
                                      float b,
                                      int n) {
        float step = (b - a) / n;
        float s1 = 0, s2 = 0;
        int k = (n - 1) / 2;
        for (int i = 0; i < k; i++) {
            s1 += f.apply(a + step + i * 2f * step);
            s2 += f.apply(a + (i + 1) * 2f * step);
        }
        //return h*(func(a)+func(b)+4*sum1+2*sum2)/3;
        return step * (f.apply(a) + f.apply(b) + 4 * s1 + 2 * s2) / 3;
    }
}
