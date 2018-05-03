package util;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.function.Function;

public class Polynom implements Function<Float, Float> {

    private ArrayList<Float> a;

    public Polynom(ArrayList<Float> a) {
        this.a = a;
    }

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

    @Override
    public String toString() {

        return "Polynom:";
    }
}