package ua.mind.meanimpl.helpers;

import java.util.List;
import java.util.concurrent.Callable;

/**
 * Created by Сергій on 28.10.14.
 * Interface of generic counting job used to run list calculation in separate threads
 */
public abstract class CountingJob implements Callable<Double> {
    /**
     * Base list with all available values.
     */
    protected List<Integer> list;
    /**
     * Start index of beginning of the calculation for current thread
     */
    protected int startIndex;
    /**
     * End index for the calculation for current thread
     */
    protected int endIndex;

    public CountingJob(List<Integer> baseList, int start, int end) {
        list = baseList;
        startIndex = start;
        endIndex = end;
    }
}
