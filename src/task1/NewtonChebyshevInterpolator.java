package task1;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;
import java.util.function.Function;
import static java.lang.Math.*;

public class NewtonChebyshevInterpolator {

    public static void main(String[] args) throws IOException {
        processData();
        createOutput();
    }

    static final ArrayList<Integer> m=new ArrayList<>(Arrays.asList(4,5,6,8,9));
    static final MyFunction f=new MyFunction();
    static HashMap<Integer,float[][]> tables=new HashMap<>();
    static HashMap<Integer,ArrayList<Float>> nodeLists=new HashMap<>();
    static HashMap<Integer, NewtonPolynom> polynoms=new HashMap<>();
    static final float a=0f;
    static final float b=10f;

    static <T extends Function<Float,Float>> ArrayList<Float> chebyshevNodes(float a, float b, int m, T f) {
        ArrayList<Float> list=new ArrayList<>(m);
        for (int i = 0; i < m; i++) {
            float result=(float) ((b+a)/2+(b-a)/2*cos(PI*(2*i+1)/(2*m)));
            list.add(result);
        }
        Collections.reverse(list);
        return list;
    }

    static <T extends Function<Float,Float>> float[][] table(ArrayList<Float> x, T f) {
        float[][] result=new float[x.size()][x.size()];
        for (int i = 0; i < x.size(); i++) {
            result[0][i]=f.apply(x.get(i));
        }
        for (int i = 1; i < x.size(); i++) {
            for (int j = i; j < x.size(); j++) {
                result[i][j]=(result[i-1][j]-result[i-1][j-1])/(x.get(j)-x.get(j-i));
            }
        }
        return result;
    }

    static void processData() {
        m.forEach(x -> {
            ArrayList<Float> nodes=chebyshevNodes(a,b,x,f);
            float[][] table=table(nodes,f);
            tables.put(x,table);
            nodeLists.put(x, nodes);
            ArrayList<Float> coefficients=new ArrayList<>(m.size());
            for (int i = 0; i < x; i++) {
                coefficients.add(table[i][i]);
            }
            polynoms.put(x,new NewtonPolynom(coefficients, nodes));
        });
    }

    static void createOutput() throws IOException {
        PrintWriter writer=new PrintWriter("output.txt");
        m.forEach(x -> {
            NewtonPolynom polynom=polynoms.get(x);
            writer.println("m="+x+":");
            writer.println("x\tfunction\tpolynomial");
            nodeLists.get(x).forEach(X -> {
                writer.printf("%.7f",X);
                writer.print("\t");
                writer.printf("%.7f",f.apply(X));
                writer.print("\t");
                writer.printf("%.7f",polynom.apply(X));
                writer.println();
            });
        });
        writer.close();
    }
}