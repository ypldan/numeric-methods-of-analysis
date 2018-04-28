package task2;

import additive.LinearSystemSolver;

import static additive.CommonAnalysis.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.function.Function;

public class OrdinaryListSquaresInterpolator {

    public static void main(String[] args) {
        OrdinaryListSquaresInterpolator interpolator=new OrdinaryListSquaresInterpolator(
                0f,10f,30, x -> (float) (10 * x * x * x * Math.pow(Math.PI * 5 + x, -0.25)));
        interpolator.getCoeffs().forEach(System.out::println);
    }

    private Function<Float, Float> f;
    private int n;
    private float a,b;
    private ArrayList<Float> coeffs;

    public OrdinaryListSquaresInterpolator(float a, float b, int n, Function<Float, Float> f) {
        this.f = f;
        this.n = n;
        this.a=a;
        this.b=b;
        process();
    }

    private void process() {
        ArrayList<Float> x1=ordinaryNodes(a,b,n);
        ArrayList<Float> y=functionValues(x1,f);
        ArrayList<Float> x0=new ArrayList<>(x1.size()),
                x2=new ArrayList<>(x1.size()),
                x3=new ArrayList<>(x1.size()),
                x4=new ArrayList<>(x1.size()),
                yx=new ArrayList<>(x1.size()),
                yx2=new ArrayList<>(x1.size());
        x1.forEach(x -> {
            x0.add(1f);
            x2.add(x*x);
            x3.add(x*x*x);
            x4.add(x*x*x*x);
        });
        for (int i = 0; i < y.size(); i++) {
            yx.add(x1.get(i)*y.get(i));
            yx2.add(x2.get(i)*y.get(i));
        }
        float[][] matrix=new float[3][3];
        matrix[0][0]= (float) x0.stream().mapToDouble(x->x).sum();
        matrix[0][1]= (float) x1.stream().mapToDouble(x->x).sum();
        matrix[0][2]= (float) x2.stream().mapToDouble(x->x).sum();
        matrix[1][0]=matrix[0][1];
        matrix[1][1]=matrix[0][2];
        matrix[1][2]=(float) x3.stream().mapToDouble(x->x).sum();
        matrix[2][0]=matrix[1][1];
        matrix[2][1]=matrix[1][2];
        matrix[2][2]=(float) x4.stream().mapToDouble(x->x).sum();
        float[] column=new float[3];
        column[0]=(float) y.stream().mapToDouble(x->x).sum();
        column[1]=(float) yx.stream().mapToDouble(x->x).sum();
        column[2]=(float) yx2.stream().mapToDouble(x->x).sum();
        coeffs=LinearSystemSolver.solveGauss(matrix,column);
    }

    public ArrayList<Float> getCoeffs() {
        return coeffs;
    }
}
