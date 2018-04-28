package task2;

import additive.LinearSystemSolver;
import additive.Polynom;

import static additive.CommonAnalysis.*;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.function.Function;
import java.util.stream.Stream;

public class OrdinaryListSquaresInterpolator {

    public static void main(String[] args) {
        try {
            OrdinaryListSquaresInterpolator interpolator = new OrdinaryListSquaresInterpolator(
                    0f, 10f, 30, x -> (float) (10 * x * x * x * Math.pow(Math.PI * 5 + x, -0.25)));
            interpolator.createOutput();
        } catch (IOException exc) {
            exc.printStackTrace();
        }
    }

    private Function<Float, Float> f;
    private int n;
    private float a,b;
    private ArrayList<Float> coeffs;
    private Polynom polynom;
    private float mistake;

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
        polynom=new Polynom(coeffs);
        float sum=0;
        for (int i = 0; i < x1.size(); i++) {
            float temp=y.get(i)-polynom.apply(x1.get(i));
            temp*=temp;
            sum+=temp;
        }
        mistake= (float) Math.sqrt(1./28.*sum);
    }

    public ArrayList<Float> getCoeffs() {
        return coeffs;
    }

    public void createOutput() throws IOException {
        PrintWriter writer=new PrintWriter("task2out/output.txt");
        writer.print(coeffs.get(0));
        if (coeffs.get(1)>0) {
            writer.print("+"+coeffs.get(1)+"x");
        } else if (coeffs.get(1)<0) {
            writer.print(coeffs.get(1)+"x");
        }
        if (coeffs.get(2)>0) {
            writer.print("+"+coeffs.get(2)+"x^2");
        } else if (coeffs.get(2)<0) {
            writer.print(coeffs.get(2)+"x^2");
        }
        writer.println();
        writer.print("mistake: "+mistake);
        writer.close();
    }
}
