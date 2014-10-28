package ua.mind.meanimpl;

import ua.mind.mean.Mean;
import ua.mind.mean.Means;

/**
 * Created by Сергій on 28.10.14.
 */
public class MeanCalculator implements Mean {
    private MeanImpl means;

    @Override
    public void reset() {
        means.reset();
    }

    @Override
    public Means add(int value) {
        means.add(value);
        return means;
    }
}
