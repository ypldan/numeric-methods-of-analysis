package task1;

import java.util.function.Function;

public class MyFunction implements Function<Float, Float> {

    @Override
    public Float apply(Float aFloat) {
        return (float) (10*aFloat*aFloat*aFloat*Math.pow(Math.PI*5+aFloat,-0.25));
    }

}
