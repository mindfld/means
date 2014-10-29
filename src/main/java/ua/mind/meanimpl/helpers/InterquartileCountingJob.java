package ua.mind.meanimpl.helpers;

import java.util.List;

/**
 * Created by Сергій on 28.10.14.
 * Implementation of Counting job for interquartile mean. Counts sum of xi values - numerator in interquartile mean formula.
 * @see ua.mind.meanimpl.helpers.CountingJob
 */
public class InterquartileCountingJob extends CountingJob {

    public InterquartileCountingJob(List<Integer> baseList, int start, int end) {
        super(baseList, start, end);
    }

    /**
     * Implementation of Callable interface method. Makes possible to call it in separate thread in executor.
     * Counts sum of xi values in list from <b>start<b/> index to <b>end<b/> index.
     */
    @Override
    public Double call() throws Exception {
        Double sum = 0.0;
        for (int i = startIndex; i < endIndex; i++) {
            sum += list.get(i);
        }
        return sum;
    }
}
