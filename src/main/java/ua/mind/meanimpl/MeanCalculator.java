package ua.mind.meanimpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ua.mind.mean.Mean;
import ua.mind.mean.Means;

/**
 * Created by Сергій on 28.10.14.
 * Implementation of Mean interface. Used for populating and reseting values for counting mean.
 *
 * @see ua.mind.mean.Mean
 */
@Component
public class MeanCalculator implements Mean {

    @Autowired
    /**
     * Inner values of Means. Used for retrieving different kind of means.
     */
    private MeansImpl means;

    @Override
    /**
     * Method that reset of all values in list of means.
     */
    public void reset() {
        means.reset();
    }

    @Override
    /**
     * Method that add value to list of means.
     */
    public Means add(int value) {
        means.add(value);
        return means;
    }
}
