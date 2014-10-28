package ua.mind.meanimpl.helpers;

import java.util.List;
import java.util.concurrent.Callable;

/**
 * Created by Сергій on 28.10.14.
 */
public abstract class CountingJob implements Callable<Double> {
    List<Integer> list;
    int startIndex;
    int endIndex;

    public CountingJob(List<Integer> baseList, int start, int end) {
        list = baseList;
        startIndex = start;
        endIndex = end;
    }


}
