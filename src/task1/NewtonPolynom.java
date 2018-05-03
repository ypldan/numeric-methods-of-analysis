package task1;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.function.Function;

/**
 * Class of Newton polynomial function, that is defined by list of nodes and list of divided differences
 */
public class NewtonPolynom implements Function<Float, Float> {

    /**
     * List of divided differences
     */
    private ArrayList<Float> a;
    /**
     * List of nodes
     */
    private ArrayList<Float> nodes;

    /**
     * Constructs Newton polynomial function
     * @param a list of divided differences
     * @param nodes list of nodes
     */
    public NewtonPolynom(ArrayList<Float> a, ArrayList<Float> nodes) {
        this.a = a;
        this.nodes = nodes;
    }

    /**
     * Calculates the value of polynomial in point
     * @param x point
     * @return numerical value of <i>P(x)</i>
     */
    @Override
    public Float apply(Float x) {
        LinkedList<Float> list=new LinkedList<>();
        for (int i = 0; i < a.size(); i++) {
            float start=a.get(i);
            for (int j = 0; j < i; j++) {
                start=start*(x-nodes.get(j));
            }
            list.add(start);
        }
        return (float)list.stream().mapToDouble(hey->hey).sum();
    }
}