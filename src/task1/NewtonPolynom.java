package task1;

import java.util.ArrayList;
import static java.lang.Math.*;

import java.util.LinkedList;
import java.util.function.Function;

public class NewtonPolynom implements Function<Float, Float> {

    private ArrayList<Float> a;
    private ArrayList<Float> nodes;

    public NewtonPolynom(ArrayList<Float> a, ArrayList<Float> nodes) {
        this.a = a;
        this.nodes = nodes;
    }

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
