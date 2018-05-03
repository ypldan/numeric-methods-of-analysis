package util;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.function.Function;

/**
 * Class of polynomial function
 */
public class Polynom implements Function<Float, Float> {

    /**
     * Polynomial coefficients
     */
    private ArrayList<Float> a;

    /**
     * Constructs polynomial function with its coefficients
     * @param a list of coefficients, <i>P<sub>n</sub>(x)=a<sub>0</sub>+
     *          a<sub>1</sub>x<sup>1</sup>+...+a<sub>n</sub>x<sup>n</sup></i>
     */
    public Polynom(ArrayList<Float> a) {
        this.a = a;
    }

    /**
     * Find value of polynom in point
     * @param x point
     * @return value P(x)
     */
    @Override
    public Float apply(Float x) {
        LinkedList<Float> list=new LinkedList<>();
        for (int i = 0; i < a.size(); i++) {
            float start=a.get(i);
            for (int j = 0; j < i; j++) {
                start*=x;
            }
            list.add(start);
        }
        return (float)list.stream().mapToDouble(hey->hey).sum();
    }
}