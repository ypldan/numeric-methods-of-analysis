package task2;

import util.LinearSystemSolver;
import util.Polynom;

import static util.CommonAnalysis.*;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.function.Function;

/**
 * Class, that interpolates function with polynom of 2nd degree using Gauss and Householder methods of LS-solving
 */
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

    /**
     * function to interpolate
     */
    private Function<Float, Float> f;
    /**
     * number of intervals
     */
    private int n;
    /**
     * start of interval
     */
    private float a;
    /**
     * end of interval
     */
    private float b;
    /**
     * coefficients of polynomial, calculated using Gauss method
     */
    private ArrayList<Float> coeffsGauss;
    /**
     * polynomial with coefficients, calculated using Gauss method
     */
    private Polynom polynomGauss;
    /**
     * coefficients of polynomial, calculated using Householder method
     */
    private ArrayList<Float> coeffsHouseholder;
    /**
     * polynomial with coefficients, calculated using Householder method
     */
    private Polynom polynomHouseholder;
    /**
     * mistake of Gauss polynomial
     */
    private float mistakeGauss;
    /**
     * mistake of Householder polynomial
     */
    private float mistakeHouseholder;

    /**
     * Constructs the interpolator class
     * @param a start of interval
     * @param b end of interval
     * @param n number of intervals
     * @param f function
     */
    public OrdinaryListSquaresInterpolator(float a, float b, int n, Function<Float, Float> f) {
        this.f = f;
        this.n = n;
        this.a=a;
        this.b=b;
        process();
    }

    /**
     * processes input data
     */
    private void process() {
        ArrayList<Float> x1= uniformNodes(a,b,n);
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
        coeffsGauss =LinearSystemSolver.solveGauss(matrix,column);
        polynomGauss =new Polynom(coeffsGauss);
        coeffsHouseholder =LinearSystemSolver.solveHouseholder(matrix,column);
        polynomHouseholder =new Polynom(coeffsHouseholder);
        float sum=0;
        for (int i = 0; i < x1.size(); i++) {
            float temp=y.get(i)- polynomGauss.apply(x1.get(i));
            temp*=temp;
            sum+=temp;
        }
        mistakeGauss = (float) Math.sqrt(1./28.*sum);
        sum=0;
        for (int i = 0; i < x1.size(); i++) {
            float temp=y.get(i)- polynomHouseholder.apply(x1.get(i));
            temp*=temp;
            sum+=temp;
        }
        mistakeHouseholder = (float) Math.sqrt(1./28.*sum);
    }

    /**
     * creates output of task2
     * @throws IOException
     */
    public void createOutput() throws IOException {
        PrintWriter writer=new PrintWriter("task2out/output.txt");
        writer.print("Gauss: ");
        writer.print(coeffsGauss.get(0));
        if (coeffsGauss.get(1)>0) {
            writer.print("+"+ coeffsGauss.get(1)+"x");
        } else if (coeffsGauss.get(1)<0) {
            writer.print(coeffsGauss.get(1)+"x");
        }
        if (coeffsGauss.get(2)>0) {
            writer.print("+"+ coeffsGauss.get(2)+"x^2");
        } else if (coeffsGauss.get(2)<0) {
            writer.print(coeffsGauss.get(2)+"x^2");
        }
        writer.println();
        writer.println("mistake: "+ mistakeGauss);
        writer.println();
        writer.print("Householder: ");
        writer.print(coeffsHouseholder.get(0));
        if (coeffsHouseholder.get(1)>0) {
            writer.print("+"+ coeffsHouseholder.get(1)+"x");
        } else if (coeffsHouseholder.get(1)<0) {
            writer.print(coeffsHouseholder.get(1)+"x");
        }
        if (coeffsHouseholder.get(2)>0) {
            writer.print("+"+ coeffsHouseholder.get(2)+"x^2");
        } else if (coeffsHouseholder.get(2)<0) {
            writer.print(coeffsHouseholder.get(2)+"x^2");
        }
        writer.println();
        writer.println("mistake: "+ mistakeHouseholder);
        writer.close();
    }
}
