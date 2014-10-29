package ua.mind.meanimpl.helpers;

import java.util.List;

/**
 * Created by Сергій on 28.10.14.
 * Implementation of Counting job for harmonic mean. Counts sum of 1/xi values - denominator in harmonic mean formula.
 *
 * @see ua.mind.meanimpl.helpers.CountingJob
 */
public class HarmonicCountingJob extends CountingJob {

    public HarmonicCountingJob(List<Integer> baseList, int start, int end) {
        super(baseList, start, end);
    }

    @Override
    /**
     * Implementation of Callable interface method. Makes possible to call it in separate thread in executor.
     * Counts sum of 1/xi values. Throws <b>IllegalArgumentException</b> in case zero or negative values passed.
     * Such kind of values denied due to specification
     * @see http://en.wikipedia.org/wiki/Harmonic_mean
     */
    public Double call() throws IllegalArgumentException {
        Double sum = 0.0;
        for (int i = startIndex; i < endIndex; i++) {
            if (list.get(i) > 0) {
                sum += 1.0 / (list.get(i));
            } else {
                throw new IllegalArgumentException("For harmonic mean all values should be positive");
            }
        }
        return sum;
    }
}
